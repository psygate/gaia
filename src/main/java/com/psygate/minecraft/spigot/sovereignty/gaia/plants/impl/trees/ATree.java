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

package com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.trees;

import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.GrowthHandler;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.APlant;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.MaterialType;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * Created by psygate on 09.05.2016.
 */
public abstract class ATree extends APlant {
    private TreeType tree;
    private Material sapling;
    private byte saplingData;

    public ATree(GrowthHandler ghandler, String name, TreeType tree, Material sapling, byte saplingData) {
        super(ghandler, name, new ItemStack[]{new ItemStack(sapling, 1, (short) 0, saplingData)},
                new MaterialType[]{new MaterialType(sapling, saplingData), new MaterialType(sapling, saplingData | 8)});
        this.tree = Objects.requireNonNull(tree);
        this.sapling = Objects.requireNonNull(sapling);
        this.saplingData = Objects.requireNonNull(saplingData);
    }
}
