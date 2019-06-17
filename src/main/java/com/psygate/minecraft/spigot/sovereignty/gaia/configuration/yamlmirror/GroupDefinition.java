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

import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by psygate on 06.05.2016.
 */
public class GroupDefinition {
    private String name;
    private List<Biome> biomes;

    public GroupDefinition() {
    }

    public GroupDefinition(String name, Biome... biomes) {
        this.name = name;
        this.biomes = new LinkedList<>(Arrays.asList(biomes));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Biome> getBiomes() {
        return biomes;
    }

    public void setBiomes(List<Biome> biomes) {
        this.biomes = biomes;
    }

    @Override
    public String toString() {
        return "GroupDefinition{" +
                "name='" + name + '\'' +
                ", biomes=" + biomes +
                '}';
    }

//    public void normalize() {
//        name = name.toLowerCase();
//        if (biomes.isEmpty()) {
//            throw new IllegalStateException("Empty group definition: " + name);
//        }
//    }
}
