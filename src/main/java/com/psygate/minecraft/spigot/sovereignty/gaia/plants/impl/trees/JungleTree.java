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

import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.impl.TreeGrowthHandler;
import org.bukkit.Material;
import org.bukkit.TreeType;

/**
 * Created by psygate on 09.05.2016.
 */
public class JungleTree extends ATree {
    public JungleTree() {
        this(new TreeGrowthHandler(TreeType.JUNGLE));
    }

    public JungleTree(TreeGrowthHandler treeGrowthHandler) {
        super(treeGrowthHandler, "jungle", TreeType.JUNGLE, Material.SAPLING, (byte) 3);
    }
}
