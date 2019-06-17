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

package com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers;

import com.psygate.minecraft.spigot.sovereignty.gaia.db.model.tables.records.GaiaStatesRecord;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.function.Consumer;

/**
 * Created by psygate on 09.05.2016.
 */
public interface GrowthHandler {
    default GrowthHandlerReturnState grow(Block block, GaiaStatesRecord rec, Consumer<Block> changedBlock) {
        BlockState state = block.getState();
        return grow(state, rec, (blockState -> {
            blockState.update(true, false);
            changedBlock.accept(blockState.getBlock());
        }));
    }

    GrowthHandlerReturnState grow(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changeConsumer);

    default GrowthHandlerReturnState fail(Block block, GaiaStatesRecord rec, Consumer<Block> changedBlock) {
        BlockState state = block.getState();
        return fail(state, rec, (blockState -> {
            blockState.update(true, false);
            changedBlock.accept(blockState.getBlock());
        }));
    }

    GrowthHandlerReturnState fail(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changeConsumer);

    default String formatBlockstate(BlockState state) {
        return "(" + state.getType() + ": " + state.getX() + ", " + state.getY() + ", " + state.getZ() + "; " + state.getWorld().getName() + ")";
    }
}
