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

package com.psygate.minecraft.spigot.sovereignty.gaia.listeners;

import com.psygate.minecraft.spigot.sovereignty.gaia.Gaia;
import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.gaia.GaiaPlantSetting;
import com.psygate.minecraft.spigot.sovereignty.gaia.db.model.Tables;
import com.psygate.minecraft.spigot.sovereignty.gaia.db.model.tables.records.GaiaStatesRecord;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.PlantManager;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.PlantState;
import com.psygate.minecraft.spigot.sovereignty.gaia.util.GaiaUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static com.psygate.minecraft.spigot.sovereignty.gaia.db.model.Tables.GAIA_STATES;

/**
 * Created by psygate on 11.05.2016.
 */
public class PlantListener implements Listener {
    private final static Logger LOG = Gaia.getLogger(PlantListener.class.getName());

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlant(BlockPlaceEvent ev) {
        PlantManager.getInstance().getPlantByBlock(ev.getBlock()).ifPresent(p -> {
            Optional<GaiaPlantSetting> gpsopt = Gaia.getConfiguration().getConf().getSetting(ev.getBlockPlaced().getBiome(), p);
            if (!gpsopt.isPresent()) {
                ev.setCancelled(true);
                ev.getPlayer().sendMessage(ChatColor.RED + p.getName() + " doesn't grow here.");
            } else {
                GaiaPlantSetting gps = gpsopt.get();
                boolean skylight = GaiaUtil.hasSkyLight(ev.getBlockPlaced());
                int lightlevel = GaiaUtil.getLight(ev.getBlockPlaced());
                int accelerators = GaiaUtil.getAccelerators(gps, ev.getBlockPlaced().getRelative(BlockFace.DOWN, 2));

                if (gps.getRequireSkyLight() && !skylight) {
                    ev.setCancelled(true);
                    ev.getPlayer().sendMessage(ChatColor.RED + p.getName() + " requires sky light.");
                    return;
                }

                if (gps.getMinLightLevel() > lightlevel) {
                    ev.setCancelled(true);
                    ev.getPlayer().sendMessage(ChatColor.RED + " Not enough light.");
                    return;
                }


                if (gps.getMaxLightLevel() < lightlevel) {
                    ev.setCancelled(true);
                    ev.getPlayer().sendMessage(ChatColor.RED + " Too much light.");
                    return;
                }

                long growthTime = (long) ((1 - accelerators * gps.getAcceleration()) * gps.getGrowthTime());
                int x = ev.getBlockPlaced().getX(), y = ev.getBlockPlaced().getY(), z = ev.getBlockPlaced().getZ();
                UUID world = ev.getBlockPlaced().getWorld().getUID();

                long growthTickTime = System.currentTimeMillis() + growthTime;
                GaiaStatesRecord rec = new GaiaStatesRecord(
                        null,
                        x, y, z, world, PlantState.MATURE, new Timestamp(growthTickTime),
                        new Timestamp(System.currentTimeMillis()), ev.getPlayer().getUniqueId(),
                        p.getName(), 0
                );

                LOG.finer("Emitting growth record:\n" + rec);
                Gaia.DBI().asyncSubmit((conf) -> {
                    DSL.using(conf).executeInsert(rec);
                });

                if (gps.getCanRot()) {
                    GaiaStatesRecord rotrec = new GaiaStatesRecord(
                            null,
                            x, y, z, world, PlantState.ROT, new Timestamp(growthTickTime + gps.getRotsAfter()),
                            new Timestamp(System.currentTimeMillis()), ev.getPlayer().getUniqueId(),
                            p.getName(), 0
                    );

                    LOG.finer("Emitting rot record:\n" + rotrec);
                    Gaia.DBI().asyncSubmit((conf) -> {
                        DSL.using(conf).executeInsert(rotrec);
                    });
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void breakBlock(BlockBreakEvent ev) {
        int x = ev.getBlock().getX(), y = ev.getBlock().getY(), z = ev.getBlock().getZ();
        UUID world = ev.getBlock().getWorld().getUID();

        Gaia.DBI().asyncSubmit((conf) -> {
            int val = DSL.using(conf).deleteFrom(GAIA_STATES)
                    .where(GAIA_STATES.WORLD_UUID.eq(world))
                    .and(GAIA_STATES.X.eq(x))
                    .and(GAIA_STATES.Y.eq(y))
                    .and(GAIA_STATES.Z.eq(z))
                    .execute();
        });
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void grow(BlockGrowEvent ev) {
        if (PlantManager.getInstance().getPlantByBlock(ev.getBlock()).isPresent()) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void grow(BlockSpreadEvent ev) {
        if (PlantManager.getInstance().getPlantByBlock(ev.getBlock()).isPresent()) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void grow(BlockFromToEvent ev) {
        if (PlantManager.getInstance().getPlantByBlock(ev.getBlock()).isPresent()) {
            ev.setCancelled(true);
        }
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void grow(BlockFormEvent ev) {
        if (PlantManager.getInstance().getPlantByBlock(ev.getBlock()).isPresent()) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void boneMeal(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK && ev.getItem() != null && ev.getItem().getType() == Material.INK_SACK && ev.getItem().getData().getData() == 15) {
            if (PlantManager.getInstance().getPlantByBlock(ev.getClickedBlock()).isPresent()) {
                ev.setCancelled(true);
                ev.getPlayer().sendMessage(ChatColor.RED + "Bone meal usage on " + ev.getClickedBlock().getType() + " is disabled.");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void boneMeal(BlockDispenseEvent ev) {
        if (ev.getItem() != null && ev.getItem().getType() == Material.INK_SACK && ev.getItem().getData().getData() == 15) {
            ev.setCancelled(true);
        }
    }
}
