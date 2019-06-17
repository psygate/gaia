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
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Created by psygate on 11.05.2016.
 */
public class GaiaWorker {
    private final static Logger LOG = Gaia.getLogger(GaiaWorker.class.getName());

    private static GaiaWorker instance;
    private Thread plantSelectionThread;
    private final List<ChunkBB> chunks = new LinkedList<>();
    private final TransferQueue<GaiaStatesRecord> toWorkerQueue = new LinkedTransferQueue<>();
    private final TransferQueue<ReturnContainer> fromWorkerQueue = new LinkedTransferQueue<>();
    private GaiaDatabaseWorker databaseWorker;
    private GrowthWorker growthWorker;
    private int workerID = -1;

    private GaiaWorker() {
        databaseWorker = new GaiaDatabaseWorker(chunks, toWorkerQueue, fromWorkerQueue, 1000);
        growthWorker = new GrowthWorker(chunks, toWorkerQueue, fromWorkerQueue);
    }

    public static GaiaWorker getInstance() {
        if (instance == null) {
            instance = new GaiaWorker();
        }

        return instance;
    }

    public void start() {
        if (!databaseWorker.isRunning()) {
            LOG.info("Starting worker thread.");
            databaseWorker.init();
            Thread workerThread = new Thread(databaseWorker, "Gaia-Database-Worker");
            workerThread.setDaemon(true);
            workerThread.start();
        }

        if (workerID == -1) {
            LOG.info("Scheduling growth worker.");
            workerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Gaia.getInstance(), new GrowthWorker(chunks, toWorkerQueue, fromWorkerQueue), 20, 20);
        }
    }

    public void stop() {
        if (databaseWorker.isRunning()) {
            databaseWorker.stop();
        }

        if (workerID != -1) {
            Bukkit.getScheduler().cancelTask(workerID);
        }
    }
}
