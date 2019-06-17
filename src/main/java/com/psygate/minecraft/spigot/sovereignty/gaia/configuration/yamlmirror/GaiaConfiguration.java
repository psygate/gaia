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

import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.gaia.GaiaConf;
import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.block.Biome.*;

/**
 * Created by psygate on 06.05.2016.
 */
public class GaiaConfiguration {
    private List<String> dontHandle = new LinkedList<>();
    private List<GroupDefinition> groupDefinitions = new LinkedList<>();
    private Map<String, GroupSetting> groupSettings = new HashMap<>();

    public List<String> getDontHandle() {
        return dontHandle;
    }

    public void setDontHandle(List<String> dontHandle) {
        this.dontHandle = dontHandle;
    }

    public List<GroupDefinition> getGroupDefinitions() {
        return groupDefinitions;
    }

    public void setGroupDefinitions(List<GroupDefinition> groupDefinitions) {
        this.groupDefinitions = groupDefinitions;
    }

    public Map<String, GroupSetting> getGroupSettings() {
        return groupSettings;
    }

    public void setGroupSettings(Map<String, GroupSetting> groupSettings) {
        this.groupSettings = groupSettings;
    }

    public GaiaConf asGaiaConf() {
        GaiaConf conf = new GaiaConf();
        HashMap<String, GroupDefinition> defs = new HashMap<>();
        groupDefinitions.forEach(v -> defs.put(v.getName(), v));
//        groupSettings.forEach((name, groupsetting) -> conf.attach(groupsetting.asGroupSetting(defs.get(name))));

        for (Map.Entry<String, GroupSetting> en : groupSettings.entrySet()) {
            GroupSetting gs = en.getValue();
            String name = en.getKey();
            conf.attach(gs.asGroupSetting(defs.get(name)));
        }

        return conf;
    }

    @Override
    public String toString() {
        return "GaiaConfiguration{" +
                "dontHandle=" + dontHandle +
                ", groupDefinitions=" + groupDefinitions +
                ", groupSettings=" + groupSettings +
                '}';
    }

    public void resolveInheritance() {
        System.out.println("Filling up group settings.");

        for (String groupname : groupSettings.keySet()) {
            GroupSetting gs = groupSettings.get(groupname);
            GroupSetting getting = Objects.requireNonNull(groupSettings.get(gs.getInherit()), () -> "Inheritance failed for " + gs.getName() + " (" + gs.getInherit() + ")");
            gs.copyPlantsIfNotDefined(getting);
        }
        for (String groupname : groupSettings.keySet()) {
            GroupSetting gs = groupSettings.get(groupname);
            if (gs.getInherit() != null && !gs.getInherit().equalsIgnoreCase("none")) {
                GroupSetting inherit = Objects.requireNonNull(groupSettings.get(gs.getInherit()),
                        () -> groupname + " inherits " + gs.getInherit() + ", but no group with that name has been defined.");
                gs.setParent(inherit);
            }

            for (String plantname : gs.getPlantSettings().keySet()) {
                PlantSetting ps = gs.getPlantSettings().get(plantname);
                if (ps.getInherit() != null && !ps.getInherit().equalsIgnoreCase("none")) {
                    String group, plant;
                    if (ps.getInherit().contains(":")) {
                        group = ps.getInherit().split(":")[0];
                        plant = ps.getInherit().split(":")[1];
                    } else {
                        group = groupname;
                        plant = ps.getInherit();
                    }

                    GroupSetting sgroup = Objects.requireNonNull(groupSettings.get(group), () -> "Group undefined: " + group + ".");
                    PlantSetting inherit = Objects.requireNonNull(sgroup.getPlantSettings().get(plant),
                            () -> "Plant " + groupname + "." + plantname + " inherits " + group + "." + plant + ","
                                    + " but the parent plant isn't defined.");
                    ps.setParent(groupSettings.get(group).getPlantSettings().get(plant));
                }
            }
        }
    }

    public void normalize() {
        HashMap<String, GroupDefinition> groupdef = new HashMap<>();
        for (GroupDefinition def : groupDefinitions) {
            def.setName(def.getName().toLowerCase());
            groupdef.put(def.getName(), def);
        }
        HashMap<String, GroupSetting> settings = new HashMap<>(groupSettings);

        groupSettings.clear();

        for (Map.Entry<String, GroupSetting> en : settings.entrySet()) {
            en.getValue().setName(en.getKey().toLowerCase());

            if (en.getValue().getInherit() == null || en.getValue().getInherit().equalsIgnoreCase("none")) {
                en.getValue().setInherit(en.getKey().toLowerCase());
            } else {
                en.getValue().setInherit(en.getValue().getInherit().toLowerCase());
            }

            GroupDefinition def = Objects.requireNonNull(groupdef.get(en.getKey().toLowerCase()),
                    () -> "Cannot find group definition for " + en.getKey());
            en.getValue().setGroupDefinition(def);

            en.getValue().normalize();

            groupSettings.put(en.getKey().toLowerCase(), en.getValue());
        }
    }

    public void pushDown() {
        HashMap<String, GroupSetting> settings = new HashMap<>(groupSettings);

        groupSettings.clear();

        for (Map.Entry<String, GroupSetting> en : settings.entrySet()) {
            groupSettings.put(en.getKey().toLowerCase(), en.getValue());
        }

        for (GroupDefinition definition : groupDefinitions) {
            System.out.println("Renaming " + definition.getName() + " to " + definition.getName().toLowerCase());
            definition.setName(definition.getName().toLowerCase());
            Objects.requireNonNull(definition, () -> "Definition broken. null value in list.");

            if (!groupSettings.containsKey(definition.getName())) {
                System.out.println(definition.getName() + " is grouped but has no settings.");
            } else {
                GroupSetting setting = Objects.requireNonNull(groupSettings.get(definition.getName()),
                        () -> "No setting for name " + definition.getName() + " [" + groupSettings.keySet() + "]");
                setting.setName(definition.getName());
                setting.setGroupDefinition(definition);
//                setting.pushDown();
            }
        }
    }

    public void checkBiomes() {
        System.out.println("Defined groups: " + groupDefinitions.stream().map(GroupDefinition::getName).collect(Collectors.toList()));

        List<Biome> biomeList = groupDefinitions.stream().map(GroupDefinition::getBiomes).flatMap(Collection::stream).collect(Collectors.toList());

        for (Biome biome : Biome.values()) {
            if (!biomeList.contains(biome)) {
                System.err.println("Biome " + biome + " not defined.");
            }
        }
    }

//    private void normalizeNames() {
//        for (GroupDefinition def : groupDefinitions) {
//            def.setName(def.getName().toLowerCase());
//        }
//
//        HashMap<String, GroupSetting> settings = new HashMap<>(groupSettings);
//
//        groupSettings.clear();
//        for (Map.Entry<String, GroupSetting> en : settings.entrySet()) {
//            groupSettings.put(en.getKey().toLowerCase(), en.getValue());
//        }
//    }
}
