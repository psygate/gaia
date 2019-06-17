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

package com.psygate.minecraft.spigot.sovereignty.gaia.workers;

import org.bukkit.Chunk;

import java.util.UUID;

/**
 * Created by psygate on 12.05.2016.
 */
public class ChunkBB {
    private int x, z;
    private UUID world;

    public ChunkBB(int x, int z, UUID world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    public ChunkBB(Chunk v) {
        x = v.getX();
        z = v.getZ();
        this.world = v.getWorld().getUID();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public UUID getWorld() {
        return world;
    }

    public void setWorld(UUID world) {
        this.world = world;
    }
}
