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
import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.gaia.GaiaPlantSetting;
import com.psygate.minecraft.spigot.sovereignty.gaia.db.model.tables.records.GaiaStatesRecord;
import com.psygate.minecraft.spigot.sovereignty.gaia.events.PlantGrowthEvent;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.Plant;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.PlantManager;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.GrowthHandlerReturnState;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.logging.Logger;

/**
 * Created by psygate on 12.05.2016.
 */
public class GrowthWorker implements Runnable {
    private final static Logger LOG = Gaia.getLogger("Gaia-Growth-Plant-Worker");
    private final List<ChunkBB> loadedChunks;
    private final TransferQueue<GaiaStatesRecord> input;
    private final TransferQueue<ReturnContainer> output;
    private final Random failrandom = new Random();

    public GrowthWorker(List<ChunkBB> loadedChunks, TransferQueue<GaiaStatesRecord> input, TransferQueue<ReturnContainer> output) {
        this.loadedChunks = loadedChunks;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
//        LOG.finer("Growth worker running.");
        long start = System.nanoTime();

        synchronized (loadedChunks) {
            loadedChunks.clear();
            Bukkit.getWorlds().stream()
                    .map(World::getLoadedChunks)
                    .flatMap(Arrays::stream)
                    .forEach(v -> loadedChunks.add(new ChunkBB(v)));
        }

        GaiaStatesRecord lrec;
        while ((lrec = input.poll()) != null && System.nanoTime() - start < TimeUnit.SECONDS.toNanos(1)) {
            Optional<Plant> plant = PlantManager.getInstance().getPlant(lrec.getPlant());
            final GaiaStatesRecord rec = lrec;
            plant.ifPresent(p -> {
                Block block = Bukkit.getWorld(rec.getWorldUuid()).getBlockAt(rec.getX(), rec.getY(), rec.getZ());
                Optional<Plant> bplant = PlantManager.getInstance().getPlantByBlock(block);

                if (bplant.isPresent() && bplant.get().equals(p)) {
                    Optional<GaiaPlantSetting> conf = Gaia.getConfiguration().getConf().getSetting(block.getBiome(), bplant.get());
                    boolean fail = false;
                    if (conf.isPresent() &&  failrandom.nextDouble() <= conf.get().getFailureRate()) {
                        fail = true;
                    }
                    if (!fail) {
                        LOG.fine("Growing:\n" + rec);
                        Set<BlockState> changed = new HashSet<>();
                        GrowthHandlerReturnState rstate = p.getGrowthHandler().grow(
                                block,
                                rec,
                                (state) -> changed.add(state.getState())
                        );

                        new PlantGrowthEvent(block.getState(), changed).call();

                        output.add(new ReturnContainer(rstate, rec));
                    } else {
                        LOG.fine("FaIling:\n" + rec);
                        GrowthHandlerReturnState rstate = p.getGrowthHandler().fail(
                                block,
                                rec,
                                (state) -> {
                                }
                        );

                        output.add(new ReturnContainer(rstate, rec));
                    }
                } else {
                    LOG.warning("Plant change in worker: " + p + " but is " + ((bplant.isPresent()) ? bplant.get() : bplant) + "?");
                    output.add(new ReturnContainer(GrowthHandlerReturnState.DELETE, rec));
                }
            });

            if (!plant.isPresent()) {
                output.add(new ReturnContainer(GrowthHandlerReturnState.DELETE, rec));
            }
        }


        if (System.nanoTime() - start >= TimeUnit.SECONDS.toNanos(1)) {
            LOG.warning("Overburdened and hitting the 1 second limit!");
        }
    }
}
