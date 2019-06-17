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
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockChangeDelegateCollector;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.MaterialType;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by psygate on 09.05.2016.
 */
public class TreeGrowthHandler implements GrowthHandler {
    private final static Logger LOG = Gaia.getLogger(TreeGrowthHandler.class.getName());
    private TreeType treeType;

    public TreeGrowthHandler(TreeType treeType) {
        this.treeType = treeType;
    }

    @Override
    public GrowthHandlerReturnState grow(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changeConsumer) {
        if (rec.getPlantState() == PlantState.MATURE) {
            BlockChangeDelegateCollector col = new BlockChangeDelegateCollector(
                    new HashSet<>(
                            Arrays.asList(
                                    new MaterialType(Material.SAPLING, 0),
                                    new MaterialType(Material.AIR, 0)
                            )
                    ),
                    state.getWorld()
            );

            state.getWorld().generateTree(state.getLocation(), treeType, col);
        }

        return GrowthHandlerReturnState.DELETE;
    }

    @Override
    public GrowthHandlerReturnState fail(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changedBlock) {
        LOG.fine("Failing tree (" + rec.getPlant() + ")" + state);
        state.setType(Material.DEAD_BUSH);
        changedBlock.accept(state);
        return GrowthHandlerReturnState.DELETE;
    }
}
