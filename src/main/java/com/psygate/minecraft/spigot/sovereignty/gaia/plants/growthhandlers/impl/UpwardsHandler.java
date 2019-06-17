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

package com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.impl;

import com.psygate.minecraft.spigot.sovereignty.gaia.Gaia;
import com.psygate.minecraft.spigot.sovereignty.gaia.db.model.tables.records.GaiaStatesRecord;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.PlantState;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.GrowthHandler;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.GrowthHandlerReturnState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import java.sql.Timestamp;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by psygate on 09.05.2016.
 */
public class UpwardsHandler implements GrowthHandler {
    private final static Logger LOG = Gaia.getLogger(UpwardsHandler.class.getName());
    private final static byte BYTE_3_4 = 2;

    @Override
    public GrowthHandlerReturnState grow(Block block, GaiaStatesRecord rec, Consumer<Block> changedBlock) {
        BlockState state = block.getState();
        return grow(state, rec, (blockState -> {
            blockState.update(true, false);
            changedBlock.accept(blockState.getBlock());
        }));
    }

    @Override
    public GrowthHandlerReturnState grow(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changeConsumer) {
        state.setRawData((byte) (state.getRawData() | BYTE_3_4));
        changeConsumer.accept(state);
        if (rec.getGrowthCount() < 2) {
            if (rec.getPlantState() == PlantState.MATURE) {
                if (state.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
                    BlockState ns = state.getBlock().getRelative(BlockFace.UP).getState();
                    ns.setType(state.getType());
                    changeConsumer.accept(ns);
                    if (rec.getY() < 256) {
                        long now = System.currentTimeMillis();
                        rec.setY(rec.getY() + 1);
                        rec.setGrowthCount(rec.getGrowthCount() + 1);
                        rec.setTransitionTime(new Timestamp(rec.getTransitionTime().getTime() - rec.getPlantedTime().getTime() + now));
                        rec.setPlantedTime(new Timestamp(now));
                        return GrowthHandlerReturnState.UPDATE;
                    }
                }
            } else {
                LOG.fine("Ignoring state: " + rec.getPlantState() + " for " + rec.getPlant() + ".");
            }
        }

        return GrowthHandlerReturnState.DELETE;
    }

    @Override
    public GrowthHandlerReturnState fail(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changedBlock) {
        LOG.fine("Failing upwards (" + rec.getPlant() + ")" + state);
//        state.setType(Material.DEAD_BUSH);
//        changedBlock.accept(state);

        return GrowthHandlerReturnState.DELETE;
    }
}
