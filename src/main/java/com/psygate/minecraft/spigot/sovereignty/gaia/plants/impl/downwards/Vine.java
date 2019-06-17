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

package com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.downwards;

import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.GrowthHandler;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.impl.DownwardsHandler;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.APlant;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.MaterialType;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by psygate on 09.05.2016.
 */
public class Vine extends APlant {
    public Vine() {
        this(new DownwardsHandler());
    }

    public Vine(GrowthHandler handler) {
        this(handler, "vines");
    }

    private Vine(GrowthHandler ghandler, String name) {
        super(ghandler, name, new ItemStack[]{new ItemStack(Material.VINE)},
                IntStream.rangeClosed(Byte.MIN_VALUE, Byte.MAX_VALUE).mapToObj(v -> new MaterialType(Material.VINE, v)).toArray(MaterialType[]::new)
        );
    }
}
