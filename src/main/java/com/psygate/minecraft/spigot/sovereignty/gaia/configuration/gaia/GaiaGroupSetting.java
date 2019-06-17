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

import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.yamlmirror.GroupDefinition;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.Plant;
import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.util.TimeUtil;
import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.*;

/**
 * Created by psygate on 06.05.2016.
 */
public class GaiaGroupSetting {
    private Optional<GaiaGroupSetting> parent = Optional.empty();
    private Set<Biome> biomes;
    private String name;
    private Map<Plant, GaiaPlantSetting> plantSettings = new HashMap<>();

    public GaiaGroupSetting() {
    }

    public GaiaGroupSetting(GroupDefinition def) {
        this.biomes = new HashSet<>(def.getBiomes());
        this.name = def.getName();
    }

    public GaiaGroupSetting getParent() {
        return parent.orElseThrow(() -> new IllegalArgumentException("No parent. "));
    }

    public void setParent(Optional<GaiaGroupSetting> parent) {
        this.parent = parent;
    }

    public Set<Biome> getBiomes() {
        return biomes;
    }

    public String getName() {
        return name;
    }

    public void attach(GaiaPlantSetting gaiaPlantSetting) {
        plantSettings.put(gaiaPlantSetting.getPlant(), gaiaPlantSetting);
    }

    public Map<Plant, GaiaPlantSetting> getPlantSettings() {
        return plantSettings;
    }

    public Optional<GaiaPlantSetting> getPlantSetting(Plant plant) {
        return plantSettings.containsKey(plant) ? Optional.of(plantSettings.get(plant)) : Optional.empty();
    }

    public void cleanup() {
        Iterator<Map.Entry<Plant, GaiaPlantSetting>> it = plantSettings.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue().getFailureRate() >= 1) {
                it.remove();
            }
        }
    }
}
