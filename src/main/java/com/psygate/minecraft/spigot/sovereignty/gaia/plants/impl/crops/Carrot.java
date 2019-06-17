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
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.MaterialType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by psygate on 06.05.2016.
 */
public class Carrot extends ACrop {
    public Carrot(GrowthHandler handler) {
        this("carrot", handler);
    }

    public Carrot() {
        this(new CropGrowthHandler(7));
    }

    private Carrot(String carrot, GrowthHandler handler) {
        super(
                handler,
                carrot,
                new ItemStack[]{new ItemStack(Material.CARROT_ITEM, 1)},
                Material.CARROT
        );
    }
}
