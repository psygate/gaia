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

package com.psygate.minecraft.spigot.sovereignty.gaia.configuration.yamlmirror;

import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.gaia.GaiaGroupSetting;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.Plant;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.PlantManager;
import org.bukkit.block.Biome;

import javax.persistence.Transient;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by psygate on 06.05.2016.
 */
public class GroupSetting {
    //    private String name;
    private String inherit;
    private Map<String, PlantSetting> plantSettings = new HashMap<>();
    private GroupSetting parent;
    private String name;
    private GroupDefinition groupDefinition;

    public GroupSetting() {
    }

    public GroupSetting(String inherit, GroupDefinition definition) {
        this.inherit = inherit;
    }


    public String getInherit() {
        return inherit;
    }

    public void setInherit(String inherit) {
        this.inherit = inherit;
    }

    public Map<String, PlantSetting> getPlantSettings() {
        return plantSettings;
    }

    public void setPlantSettings(Map<String, PlantSetting> plantSettings) {
        this.plantSettings = plantSettings;
    }

    public GaiaGroupSetting asGroupSetting(GroupDefinition def) {
        try {
            GaiaGroupSetting set = new GaiaGroupSetting(def);
            plantSettings.forEach((plant, plantSettings) -> set.attach(plantSettings.asPlantSetting(plant)));
            return set;
        } catch (Exception e) {
            System.err.println("Error transalting " + this);
            throw e;
        }
    }

    @Override
    public String toString() {
        return "GroupSetting{" +
                "inherit='" + inherit + '\'' +
                ", plantSettings=" + plantSettings +
                '}';
    }

    public GroupSetting getParent() {
        return parent;
    }

    public void setParent(GroupSetting parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupDefinition getGroupDefinition() {
        return groupDefinition;
    }

    public void setGroupDefinition(GroupDefinition groupDefinition) {
        this.groupDefinition = groupDefinition;
    }
//
//    public void pushDown() {
//
//        if (this.inherit != null) {
//            this.inherit = this.inherit.toLowerCase();
//        }
//
//        if (this.inherit.equalsIgnoreCase("none")) {
//            this.inherit = null;
//        }
//
//        Map<String, PlantSetting> setting = new HashMap<>(plantSettings);
//
//        plantSettings.clear();
//        for (Map.Entry<String, PlantSetting> entry : setting.entrySet()) {
//            entry.getValue().setPlant(entry.getKey().toLowerCase());
//            entry.getValue().setSetting(this);
//            entry.getValue().pushDown();
//            String inherit = entry.getValue().getInherit();
//
//            if (inherit != null) {
//                if (!inherit.contains(":")) {
//                    if (this.inherit != null) {
//                        inherit = this.inherit + ":" + inherit;
//                    } else {
//                        inherit = name + ":" + inherit;
//                    }
//                }
//                inherit = inherit.toLowerCase();
//            }
//            entry.getValue().setInherit(inherit);
//
//            plantSettings.put(entry.getKey().toLowerCase(), entry.getValue());
//        }
//    }

    public void normalize() {
        inherit = inherit.toLowerCase();
        name = name.toLowerCase();
        Map<String, PlantSetting> settings = new HashMap<>(plantSettings);

        plantSettings.clear();

        for (Map.Entry<String, PlantSetting> en : settings.entrySet()) {
            en.getValue().setSetting(this);
            en.getValue().setPlant(en.getKey().toLowerCase());
            en.getValue().setPlant(en.getValue().getPlant().toLowerCase());
            String pinherit = en.getValue().getInherit();
            if (pinherit == null || pinherit.equalsIgnoreCase("none")) {
                en.getValue().setInherit(inherit + ":" + en.getValue().getPlant());
            } else if (!pinherit.contains(":")) {
                en.getValue().setInherit(inherit + ":" + pinherit);
            }
            en.getValue().setInherit(en.getValue().getInherit().toLowerCase());
            en.getValue().normalize();
            plantSettings.put(en.getKey().toLowerCase(), en.getValue());

        }

    }

    public void copyPlantsIfNotDefined(GroupSetting getting) {
        for (Map.Entry<String, PlantSetting> en : getting.getPlantSettings().entrySet()) {
            if (!plantSettings.containsKey(en.getKey())) {
                plantSettings.put(en.getKey(), en.getValue());
            }
        }
    }
}
