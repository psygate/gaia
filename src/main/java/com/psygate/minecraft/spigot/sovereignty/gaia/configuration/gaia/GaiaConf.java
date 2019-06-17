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

package com.psygate.minecraft.spigot.sovereignty.gaia.configuration.gaia;

import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.yamlmirror.GaiaConfiguration;
import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.yamlmirror.GroupDefinition;
import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.yamlmirror.PlantSetting;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.Plant;
import org.bukkit.block.Biome;

import java.util.*;

/**
 * Created by psygate on 06.05.2016.
 */
public class GaiaConf {
    private Map<Biome, GaiaGroupSetting> biomeSettings = new HashMap<>();

    public void attach(GaiaGroupSetting setting) {
        for (Biome b : setting.getBiomes()) {
            if (biomeSettings.containsKey(b)) {
                throw new IllegalArgumentException("Duplicate definition for biome: " + setting.getName() + " (" + biomeSettings.get(b).getName() + ")");
            }

            biomeSettings.put(b, setting);
        }
    }

    public Optional<GaiaPlantSetting> getSetting(Biome biome, Plant plant) {
        if (!biomeSettings.containsKey(biome)) {
            return Optional.empty();
        } else {
            return biomeSettings.get(biome).getPlantSetting(plant);
        }
    }

    public void checkIntegrity() {
        for (GaiaGroupSetting setting : biomeSettings.values()) {
            for (GaiaPlantSetting ps : setting.getPlantSettings().values()) {
                ps.getFailureRate();
                ps.getRotsAfter();
                ps.getAcceleration();
                ps.getMaxAccelerators();
                ps.getAccelerator();
                ps.getGrowthTime();
                ps.getMaxLightLevel();
                ps.getMinLightLevel();
                ps.getPlant();
                ps.getRequireSkyLight();
            }
        }
    }

    public void cleanup() {
        for (GaiaGroupSetting setting : biomeSettings.values()) {
            setting.cleanup();
        }
    }
}
