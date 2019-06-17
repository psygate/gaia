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

package com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl;

import com.psygate.minecraft.spigot.sovereignty.gaia.plants.Plant;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.GrowthHandler;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.MaterialType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * Created by psygate on 09.05.2016.
 */
public abstract class APlant implements Plant {
    private GrowthHandler ghandler;
    private String name;
    private ItemStack[] seeds;
    private MaterialType[] blocks;

    public APlant(GrowthHandler ghandler, String name, ItemStack[] seeds, MaterialType[] blocks) {
        this.seeds = Objects.requireNonNull(seeds);
        this.blocks = Objects.requireNonNull(blocks);
        this.ghandler = Objects.requireNonNull(ghandler);
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public GrowthHandler getGrowthHandler() {
        return ghandler;
    }

    @Override
    public void setGrowthHandler(GrowthHandler handler) {
        this.ghandler = Objects.requireNonNull(handler);
    }

    @Override
    public ItemStack[] getSeedItems() {
        return seeds;
    }

    @Override
    public MaterialType[] getBlocks() {
        return blocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        APlant aPlant = (APlant) o;

        return name != null ? name.equals(aPlant.name) : aPlant.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
