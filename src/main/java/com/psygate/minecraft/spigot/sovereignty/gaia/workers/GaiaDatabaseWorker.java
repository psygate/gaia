/*
 *     Copyright (C) 2016 psygate (https://github.com/psygate)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 */

package com.psygate.minecraft.spigot.sovereignty.gaia.workers;

import com.psygate.minecraft.spigot.sovereignty.gaia.Gaia;
import com.psygate.minecraft.spigot.sovereignty.gaia.db.model.tables.records.GaiaStatesRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.psygate.minecraft.spigot.sovereignty.gaia.db.model.Tables.GAIA_STATES;

/**
 * Created by psygate on 12.05.2016.
 */
public class GaiaDatabaseWorker implements Runnable {
    private final static Logger LOG = Gaia.getLogger("Gaia-Database-Worker");
    private final List<ChunkBB> loadedChunks;
    private final TransferQueue<GaiaStatesRecord> output;
    private final TransferQueue<ReturnContainer> input;
    private final AtomicBoolean run = new AtomicBoolean(false);
    private Thread workerThread;
    private AtomicReference<DSLContext> ctxref = new AtomicReference<>(null);
    private AtomicLong cycleID = new AtomicLong(0);

    public GaiaDatabaseWorker(List<ChunkBB> loadedChunks, TransferQueue<GaiaStatesRecord> output, TransferQueue<ReturnContainer> input, int maxQueueSize) {
        this.loadedChunks = loadedChunks;
        this.output = output;
        this.input = input;
    }

    @Override
    public void run() {
//        LOG.info("[" + cycleID + "]Running.");
        workerThread = Thread.currentThread();
        while (run.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                if (run.get()) {
                    LOG.warning("[" + cycleID + "]Thread sleep failed.");
                } else {
                    break;
                }
            }
            try {
                process();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "[" + cycleID + "]Failed to process records.", e);
            }
        }
        LOG.info("Terminating.");
    }

    public void stop() {
        LOG.info("Stopping.");
        run.set(false);
        DSLContext ctx = ctxref.get();
        if (ctx != null) {
            try {
                ctx.close();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Failed to close dsl context.", e);
            }
        }
        if (workerThread != null) {
            workerThread.interrupt();
            try {
                workerThread.join(TimeUnit.SECONDS.toMillis(10));
            } catch (InterruptedException e) {
                LOG.log(Level.SEVERE, "Failed to join worker thread.", e);
            }
        }
    }

    public void process() {
//        LOG.finer("[" + cycleID + "]Polling database.");
        cycleID.incrementAndGet();
        List<ChunkBB> chunkbbs;
        synchronized (loadedChunks) {
            chunkbbs = new LinkedList<>(loadedChunks);
        }

        Condition cond = chunkbbs.stream().map(
                b -> GAIA_STATES.WORLD_UUID.eq(b.getWorld())
                        .and(GAIA_STATES.X.gt(b.getX() * 16))
                        .and(GAIA_STATES.Z.gt(b.getZ() * 16))
                        .and(GAIA_STATES.X.le(b.getX() * 16 + 16))
                        .and(GAIA_STATES.Z.le(b.getZ() * 16 + 16))
        ).reduce(DSL.falseCondition(), (a, b) -> a.or(b));

        Gaia.DBI().submit((conf) -> {
            DSLContext ctx = DSL.using(conf);
            ctxref.set(ctx);
            ReturnContainer rec;

            while ((rec = input.poll()) != null) {
                switch (rec.getState()) {
                    case DELETE:
                        LOG.fine("[" + cycleID + "]Deleting:\n" + rec);
                        ctx.executeDelete(rec.getRec());
                        break;
                    case UPDATE:
                        try {
                            LOG.fine("[" + cycleID + "]Updating:\n" + rec);
                            ctx.executeUpdate(rec.getRec());
                        } catch (Exception e) {
                            LOG.log(Level.SEVERE, "[" + cycleID + "]Failed to update record.\n" + rec.getRec(), e);
                        }
                        break;
                    default:
                        LOG.severe("[" + cycleID + "]Failed to process record.\n" + rec.getRec() + "\nUnknown state: " + rec.getState());
                }
            }
            if (output.isEmpty()) {
                List<GaiaStatesRecord> recs = ctx
                        .selectFrom(GAIA_STATES)
                        .where(GAIA_STATES.TRANSITION_TIME.lt(new Timestamp(System.currentTimeMillis())))
                        .and(cond)
                        .fetch();
                if (!recs.isEmpty()) {
                    LOG.fine("Adding " + recs.size() + " records to output queue.");
                    output.addAll(recs);
                }
            }
            ctxref.set(null);
        });
    }

    public boolean isRunning() {
        return run.get();
    }

    public void init() {
        run.set(true);
    }
}
