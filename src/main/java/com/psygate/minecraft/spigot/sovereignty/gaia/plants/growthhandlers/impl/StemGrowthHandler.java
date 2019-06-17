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
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.Plant;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.PlantManager;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.PlantState;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.GrowthHandler;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.GrowthHandlerReturnState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by psygate on 09.05.2016.
 */
public class StemGrowthHandler implements GrowthHandler {
    private final static Logger LOG = Gaia.getLogger(StemGrowthHandler.class.getName());
    private final static byte BYTE_SEVEN = 7;
    private final static BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
    private final static Random rand = new Random();

    //Mappings for stem -> fruit
    private final static Map<Material, Material> mapping = new HashMap<>();

    static {
        mapping.put(Material.MELON_STEM, Material.MELON_BLOCK);
        mapping.put(Material.PUMPKIN_STEM, Material.PUMPKIN);
    }

    @Override
    public GrowthHandlerReturnState grow(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changeConsumer) {
        if (rec.getPlantState() == PlantState.MATURE) {
            return matureStem(state, rec, changeConsumer);
        } else if (rec.getPlantState() == PlantState.SPREAD) {
            return spreadStem(state, rec, changeConsumer);
        } else if (rec.getPlantState() == PlantState.ROT) {
            if (mapping.containsKey(state.getType())) {
                LOG.fine("Refusing to rot source plant: " + formatBlockstate(state));
            } else {
                LOG.fine("Rotting plant fruit: " + formatBlockstate(state));
                state.setType(Material.DEAD_BUSH);
            }
            return GrowthHandlerReturnState.DELETE;
        }

        return GrowthHandlerReturnState.DELETE;
    }

    private GrowthHandlerReturnState spreadStem(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changeConsumer) {
        BlockFace face = faces[rand.nextInt(faces.length)];
        BlockState fruit = state.getBlock().getRelative(face).getState();
        Material fruittype = mapping.get(state.getType());

        if (Arrays.stream(faces).map(v -> state.getBlock().getRelative(v)).map(Block::getType).noneMatch(v -> v == fruittype)) {
            if (fruit.getType() == Material.AIR) {
                Material down = fruit.getBlock().getRelative(BlockFace.DOWN).getType();
                if (down.isOccluding() && !down.isTransparent() || down == Material.SOIL) {
                    LOG.fine("Growing fruit: " + formatBlockstate(state) + " Fruit: " + formatBlockstate(fruit));
                    fruit.setType(fruittype);
                    changeConsumer.accept(fruit);
                    Optional<Plant> plant = PlantManager.getInstance().getPlant(rec.getPlant());
                    plant.ifPresent(p -> {
                        Gaia.getConfiguration().getConf().getSetting(fruit.getBlock().getBiome(), p).ifPresent(c -> {
                            if (c.getCanRot()) {
                                int x = fruit.getX(), y = fruit.getY(), z = fruit.getZ();
                                UUID w = fruit.getWorld().getUID();
                                Gaia.DBI().asyncSubmit((conf) -> {
                                    long now = System.currentTimeMillis();
                                    GaiaStatesRecord nrec = new GaiaStatesRecord(
                                            null,
                                            x, y, z, w,
                                            PlantState.ROT,
                                            new Timestamp(now + c.getRotsAfter()),
                                            new Timestamp(now),
                                            rec.getCreator(),
                                            p.getName(),
                                            0
                                    );

                                    DSL.using(conf).executeInsert(nrec);
                                });
                            }
                        });
                    });
                }
            }
        }

        long now = System.currentTimeMillis();
        rec.setTransitionTime(new Timestamp(rec.getTransitionTime().getTime() - rec.getPlantedTime().getTime() + now));
        rec.setPlantedTime(new Timestamp(now));
        return GrowthHandlerReturnState.UPDATE;
    }

    @Override
    public GrowthHandlerReturnState fail(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changedBlock) {
        LOG.fine("Failing stem (" + rec.getPlant() + ")" + state);
        state.setType(Material.DEAD_BUSH);
        changedBlock.accept(state);
        return GrowthHandlerReturnState.DELETE;
    }

    private GrowthHandlerReturnState matureStem(BlockState state, GaiaStatesRecord rec, Consumer<BlockState> changeConsumer) {
        // Mature the stem and reschedule as spread.
        LOG.finer("Maturing stem: " + formatBlockstate(state));
        state.setRawData(BYTE_SEVEN);
        changeConsumer.accept(state);
        rec.setPlantState(PlantState.SPREAD);
        long now = System.currentTimeMillis();
        rec.setTransitionTime(new Timestamp(rec.getTransitionTime().getTime() - rec.getPlantedTime().getTime() + now));
        rec.setPlantedTime(new Timestamp(now));
        return GrowthHandlerReturnState.UPDATE;
    }
}
