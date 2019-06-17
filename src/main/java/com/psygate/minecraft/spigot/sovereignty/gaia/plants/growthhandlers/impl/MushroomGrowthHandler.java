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
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.util.Random;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by psygate on 09.05.2016.
 */
public class MushroomGrowthHandler implements GrowthHandler {
    private final static Logger LOG = Gaia.getLogger(MushroomGrowthHandler.class.getName());
    private final static BlockFace[] faces = new BlockFace[]{
            BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST,
            BlockFace.NORTH_WEST, BlockFace.NORTH_EAST, BlockFace.SOUTH_WEST, BlockFace.SOUTH_EAST
    };
    private final static Random rand = new Random();

    @Override
    public GrowthHandlerReturnState grow(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changedBlock) {
        if (rec.getPlantState() == PlantState.MATURE || rec.getPlantState() == PlantState.SPREAD) {
            BlockFace face = faces[rand.nextInt(faces.length)];
            BlockState rel = state.getBlock().getRelative(face).getState();
            BlockState reld = state.getBlock().getRelative(face).getRelative(BlockFace.DOWN).getState();

            if (rel.getType() == Material.AIR && (reld.getType() == Material.STONE || rel.getType() == Material.DIRT)) {
                LOG.finer("Growing mushroom: " + formatBlockstate(rel));
                rel.setType(state.getType());
                changedBlock.accept(rel);
                Gaia.DBI().asyncSubmit((conf) -> {
                    long now = System.currentTimeMillis();
                    GaiaStatesRecord nrec = new GaiaStatesRecord(
                            null,
                            rel.getX(),
                            rel.getY(),
                            rel.getZ(),
                            rec.getWorldUuid(),
                            PlantState.SPREAD,
                            new Timestamp(rec.getTransitionTime().getTime() - rec.getPlantedTime().getTime() + now),
                            new Timestamp(now),
                            rec.getCreator(),
                            rec.getPlant(),
                            rec.getGrowthCount() + 1
                    );

                    DSL.using(conf).executeInsert(nrec);
                });
            }
        }

        return GrowthHandlerReturnState.DELETE;
    }


    @Override
    public GrowthHandlerReturnState fail(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changedBlock) {
        LOG.fine("Failing mushroom (" + rec.getPlant() + ")" + state);
//        state.setType(Material.DEAD_BUSH);
//        changedBlock.accept(state);

        return GrowthHandlerReturnState.DELETE;
    }
}
