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

package com.psygate.minecraft.spigot.sovereignty.gaia.util;

import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.gaia.GaiaPlantSetting;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.Plant;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.OptionalLong;

/**
 * Created by psygate on 11.05.2016.
 */
public class GaiaUtil {
    public static boolean hasSkyLight(Block block) {
        Block check = block;
        int highest = block.getWorld().getHighestBlockYAt(block.getX(), block.getZ());

        while (check.getY() < highest) {
            if (check.getType().isOccluding()) {
                return false;
            }

            check = check.getRelative(BlockFace.UP);
        }

        return true;
    }

    public static int getLight(Block block) {
        return Math.max(block.getLightFromSky(), Math.max(block.getLightFromBlocks(), block.getLightLevel()));
    }

    public static int getAccelerators(GaiaPlantSetting setting, Block block) {
        int max = setting.getMaxAccelerators();
        Material type = setting.getAccelerator();

        int count = 0;
        for (int i = 0; i < max; i++) {
            if (block.getType() == type) {
                count++;
            } else {
                return count;
            }
            block = block.getRelative(BlockFace.DOWN);
        }

        return count;
    }
}
