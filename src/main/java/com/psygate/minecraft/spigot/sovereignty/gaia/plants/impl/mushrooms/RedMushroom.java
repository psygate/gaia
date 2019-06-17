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

package com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.mushrooms;

import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.GrowthHandler;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.impl.MushroomGrowthHandler;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.APlant;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.MaterialType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by psygate on 09.05.2016.
 */
public class RedMushroom extends APlant {
    public RedMushroom() {
        this(new MushroomGrowthHandler());
    }

    public RedMushroom(GrowthHandler ghandler) {
        super(ghandler, "redmushroom", new ItemStack[]{new ItemStack(Material.RED_MUSHROOM)}, new MaterialType[]{new MaterialType(Material.RED_MUSHROOM, 0)});
    }
}
