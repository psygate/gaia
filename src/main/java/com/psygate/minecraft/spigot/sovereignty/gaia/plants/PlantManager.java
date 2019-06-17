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

package com.psygate.minecraft.spigot.sovereignty.gaia.plants;

import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.crops.*;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.downwards.Vine;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.fruitbearing.Melon;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.fruitbearing.Pumpkin;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.mushrooms.BrownMushroom;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.mushrooms.RedMushroom;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.trees.*;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.upwards.Cactus;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.impl.upwards.SugarCane;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.MaterialType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by psygate on 06.05.2016.
 */
public class PlantManager {
    private static PlantManager instance;
    private List<Plant> plants = new ArrayList<>();
    private Map<String, Plant> plantByName = new HashMap<>();
    private Map<Material, Map<Byte, Plant>> plantBySeed = new HashMap<>();
    private Map<MaterialType, Plant> plantByBlock = new HashMap<>();

    private PlantManager() {
        addPlant(
                new Wheat(),
                new Potato(),
                new Carrot(),
                new Melon(),
                new Pumpkin(),
                new NetherWart(),
                new AcaciaTree(),
                new BirchTree(),
                new DarkOakTree(),
                new JungleTree(),
                new SpruceTree(),
                new OakTree(),
                new RedMushroom(),
                new BrownMushroom(),
                new Cocoa(),
                new Cactus(),
                new SugarCane(),
                new Vine()
        );
    }

    private void addPlant(Plant... plants) {
        for (Plant plant : plants) {
            this.plants.add(plant);
            if (plantByName.containsKey(plant.getName())) {
                throw new IllegalStateException("Duplicate name definition for plant: " + plant.getName());
            }

            plantByName.put(plant.getName(), plant);
            for (ItemStack stack : plant.getSeedItems()) {
                plantBySeed.putIfAbsent(stack.getType(), new HashMap<>());
                Map<Byte, Plant> mp = plantBySeed.get(stack.getType());

                if (mp.containsKey(stack.getData().getData())) {
                    throw new IllegalStateException("Duplicate plant seed item: " + stack);
                } else {
                    mp.put(stack.getData().getData(), plant);
                }
            }

            for (MaterialType type : plant.getBlocks()) {
                if (plantByBlock.containsKey(type)) {
                    throw new IllegalStateException("Duplicate plant block: " + type + " " + plant.getName());
                } else {
                    plantByBlock.put(type, plant);
                }
            }
        }
    }

    public static PlantManager getInstance() {
        if (instance == null) {
            instance = new PlantManager();
        }
        return instance;
    }

    public Optional<Plant> getPlant(String name) {
        return Optional.ofNullable(plantByName.get(name));
    }

    public void blacklist(String name) {
        if (plantByName.containsKey(name)) {
            throw new IllegalArgumentException("No plant for name: " + name);
        }
    }

    public List<Plant> getPlants() {
        return Collections.unmodifiableList(plants);
    }

    public Optional<Plant> getPlantBySeed(ItemStack item) {
        if (plantBySeed.containsKey(item.getType())) {
            Map<Byte, Plant> mp = plantBySeed.get(item.getType());
            if (mp.containsKey(item.getData().getData())) {
                return Optional.of(mp.get(item.getData().getData()));
            }
        }

        return Optional.empty();
    }

    public Optional<Plant> getPlantByBlock(Block block) {
        MaterialType type = new MaterialType(block.getType(), block.getData());
        if (plantByBlock.containsKey(type)) {
            return Optional.of(plantByBlock.get(type));
        } else {
            return Optional.empty();
        }
    }
}
