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

package com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.crops;

import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.GrowthHandler;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.impl.CropGrowthHandler;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.APlant;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by psygate on 06.05.2016.
 */
public class NetherWart extends ACrop {
    public NetherWart(GrowthHandler handler) {
        this("netherwart", handler);
    }

    public NetherWart() {
        this(new CropGrowthHandler(3));
    }

    private NetherWart(String carrot, GrowthHandler handler) {
        super(handler, carrot, new ItemStack[]{new ItemStack(Material.NETHER_WARTS)}, Material.NETHER_WARTS);
    }
}
