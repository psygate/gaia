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
import org.bukkit.block.BlockState;

import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by psygate on 09.05.2016.
 */
public class CropGrowthHandler implements GrowthHandler {
    private final Logger LOG = Gaia.getLogger(CropGrowthHandler.class.getName());
    private final byte GROWTH_BYTE;

    public CropGrowthHandler(int i) {
        GROWTH_BYTE = (byte) i;
    }

    @Override
    public GrowthHandlerReturnState grow(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changedBlock) {
        if (rec.getPlantState() == PlantState.MATURE) {
            LOG.fine("Maturing crop (" + rec.getPlant() + ")" + state);
            state.setRawData(GROWTH_BYTE);
            changedBlock.accept(state);
        } else if (rec.getPlantState() == PlantState.ROT) {
            LOG.fine("Rotting crop (" + rec.getPlant() + ")" + state);
            state.setType(Material.DEAD_BUSH);
            changedBlock.accept(state);
        }

        return GrowthHandlerReturnState.DELETE;
    }

    @Override
    public GrowthHandlerReturnState fail(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changedBlock) {
        LOG.fine("Failing crop (" + rec.getPlant() + ")" + state);
        state.setType(Material.DEAD_BUSH);
        changedBlock.accept(state);

        return GrowthHandlerReturnState.DELETE;
    }
}
