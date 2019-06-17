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

package com.psygate.minecraft.spigot.sovereignty.gaia.events;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by psygate on 13.05.2016.
 */
public class PlantGrowthEvent extends Event {
    private final static HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private final BlockState source;
    private final Set<BlockState> grow = new HashSet<>();

    public PlantGrowthEvent(BlockState source, BlockState... grown) {
        this(source, Arrays.asList(grown));
    }

    public PlantGrowthEvent(BlockState source, Collection<BlockState> grown) {
        this.source = source;
        grow.addAll(grown);
    }

    public Set<BlockState> getGrow() {
        return grow;
    }

    public BlockState getSource() {
        return source;
    }

    public void call() {
        Bukkit.getPluginManager().callEvent(this);
    }
}
