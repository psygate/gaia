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
import org.jooq.TransactionalRunnable;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by psygate on 09.05.2016.
 */
public class DownwardsHandler implements GrowthHandler {
    private final static Logger LOG = Gaia.getLogger(DownwardsHandler.class.getName());

    @Override
    public GrowthHandlerReturnState grow(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changeConsumer) {
        if (rec.getPlantState() == PlantState.MATURE || rec.getPlantState() == PlantState.SPREAD) {
            if (state.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                LOG.fine("Growing (" + rec.getPlant() + ") " + state.getType() + " downwards. " + formatBlockstate(state));
                BlockState ns = state.getWorld().getBlockAt(state.getX(), state.getY() - 1, state.getZ()).getState();
                ns.setType(state.getType());
                changeConsumer.accept(ns);
                LOG.fine(formatBlockstate(ns) + " changed.");
                if (ns.getY() > 2) {
                    long now = System.currentTimeMillis();
                    GaiaStatesRecord nrec = new GaiaStatesRecord(
                            null,
                            ns.getX(),
                            ns.getY(),
                            ns.getZ(),
                            rec.getWorldUuid(),
                            PlantState.SPREAD,
                            new Timestamp(now + (rec.getTransitionTime().getTime() - rec.getPlantedTime().getTime())),
                            new Timestamp(now),
                            rec.getCreator(),
                            rec.getPlant(),
                            rec.getGrowthCount() + 1
                    );

                    LOG.fine("Emitting new growth record for (" + rec.getPlant() + "):\n" + nrec);
                    Gaia.DBI().asyncSubmit((TransactionalRunnable) (conf) -> DSL.using(conf).executeInsert(nrec));
                }
            } else {
                LOG.fine("Unable to grow, block downwards isn't air.");
            }
        } else {
            LOG.fine("Ignoring state: " + rec.getPlantState() + " for " + rec.getPlant() + ".");
        }

        return GrowthHandlerReturnState.DELETE;
    }


    @Override
    public GrowthHandlerReturnState fail(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changedBlock) {
        LOG.fine("Failing downwards (" + rec.getPlant() + ")" + state);
//        state.setType(Material.DEAD_BUSH);
//        changedBlock.accept(state);

        return GrowthHandlerReturnState.DELETE;
    }
}
