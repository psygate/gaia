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
import org.bukkit.block.BlockState;

import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by psygate on 09.05.2016.
 */
public class CocoaHandler implements GrowthHandler {
    private final static Logger LOG = Gaia.getLogger(CocoaHandler.class.getName());
//    private final static byte BYTE_3_4 = 2;

    @Override
    public GrowthHandlerReturnState grow(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changedBlock) {
        if (rec.getPlantState() == PlantState.MATURE) {
            LOG.fine("Maturing cocoa (" + rec.getPlant() + ")" + state);
            state.setRawData((byte) (state.getRawData() | 3));
            changedBlock.accept(state);
        } else if (rec.getPlantState() == PlantState.ROT) {
            LOG.fine("Rotting cocoa (" + rec.getPlant() + ")" + state);
            state.setType(Material.AIR);
            changedBlock.accept(state);
        }

        return GrowthHandlerReturnState.DELETE;
    }

    @Override
    public GrowthHandlerReturnState fail(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changedBlock) {
        LOG.fine("Failing cocoa (" + rec.getPlant() + ")" + state);
        state.setType(Material.AIR);
        changedBlock.accept(state);

        return GrowthHandlerReturnState.DELETE;
    }
}
