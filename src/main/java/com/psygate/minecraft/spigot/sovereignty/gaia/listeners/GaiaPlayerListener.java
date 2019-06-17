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
import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.gaia.GaiaConf;
import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.gaia.GaiaPlantSetting;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.Plant;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.PlantManager;
import com.psygate.minecraft.spigot.sovereignty.gaia.util.GaiaUtil;
import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.util.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

/**
 * Created by psygate on 11.05.2016.
 */
public class GaiaPlayerListener implements Listener {
    private final static Set<Material> hoes = new HashSet<>(
            Arrays.asList(
                    Material.WOOD_HOE,
                    Material.GOLD_HOE,
                    Material.IRON_HOE,
                    Material.DIAMOND_HOE,
                    Material.STONE_HOE
            )
    );

    @EventHandler(priority = EventPriority.MONITOR)
    public void onStickInteraction(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK && ev.getItem() != null && ev.getItem().getType() == Material.WOOD_HOE) {
            List<Plant> plants = PlantManager.getInstance().getPlants();
            List<String> output = new LinkedList<>();

            Biome biome = ev.getClickedBlock().getBiome();

            output.add(ChatColor.YELLOW + "Plant information:");
            GaiaConf conf = Gaia.getConfiguration().getConf();
            for (Plant plant : plants) {
                Optional<GaiaPlantSetting> gpsopt = conf.getSetting(biome, plant);
                if (gpsopt.isPresent()) {
                    GaiaPlantSetting gps = gpsopt.get();
                    output.add(ChatColor.GREEN + "- " + plant.getName());
                    output.add(ChatColor.WHITE + "  Growth time: " + TimeUtil.prettyfyTime(gps.getGrowthTime()));
                    if (gps.getCanRot()) {
                        output.add(ChatColor.WHITE + "  Rot time   : " + TimeUtil.prettyfyTime(gps.getRotsAfter()));
                    } else {
                        output.add(ChatColor.WHITE + "  Rot time   : DOESN'T ROT");
                    }
                    if (gps.getAccelerator() != Material.STICK) {
                        output.add(ChatColor.WHITE + "  Accelerator: " + gps.getAccelerator().name());
                        output.add(ChatColor.WHITE + "  Max. Accelerators: " + gps.getMaxAccelerators());
                        output.add(ChatColor.WHITE + "  Acceleration:" + (gps.getAcceleration() * 100) + "%");
                    }
                    output.add(ChatColor.WHITE + "  Min. Light:  " + gps.getMinLightLevel());
                    output.add(ChatColor.WHITE + "  Max. Light:  " + gps.getMaxLightLevel());
                    output.add(ChatColor.WHITE + "  Req. Sky Light: " + gps.getRequireSkyLight());
                    output.add(ChatColor.WHITE + "  Failure Rate: " + (gps.getFailureRate() * 100) + "%");
                } else {
                    output.add(ChatColor.RED + "- " + plant.getName() + ": Doesn't grow here.");
                }
            }

            ev.getPlayer().sendMessage(output.toArray(new String[output.size()]));
        } else if (ev.getAction() == Action.LEFT_CLICK_BLOCK && ev.getItem() != null) {
            Optional<Plant> plantopt = PlantManager.getInstance().getPlantBySeed(ev.getItem());
            Block checkBlock = ev.getClickedBlock().getRelative(ev.getBlockFace());
            plantopt.ifPresent(p -> {
//                ev.setCancelled(true);
                List<String> output = new LinkedList<>();
                output.add(ChatColor.YELLOW + p.getName() + " information:");
                Optional<GaiaPlantSetting> gpsopt = Gaia.getConfiguration().getConf().getSetting(checkBlock.getBiome(), p);
                if (!gpsopt.isPresent()) {
                    output.add(ChatColor.RED + p.getName() + " doesn't grow here.");
                } else {
                    GaiaPlantSetting gps = gpsopt.get();
                    boolean skylight = GaiaUtil.hasSkyLight(checkBlock);
                    int lightlevel = GaiaUtil.getLight(checkBlock);
                    if (gps.getRequireSkyLight() && skylight) {
                        output.add(ChatColor.GREEN + " Has sky light.");
                    } else if (gps.getRequireSkyLight() && !skylight) {
                        output.add(ChatColor.RED + " Doesn't have sky light.");
                    }

                    if (gps.getMinLightLevel() > lightlevel) {
                        output.add(ChatColor.RED + " Not enough light.");
                    }


                    if (gps.getMaxLightLevel() < lightlevel) {
                        output.add(ChatColor.RED + " Too much light.");
                    }

                    if (gps.getMaxLightLevel() >= lightlevel && gps.getMinLightLevel() <= lightlevel) {
                        output.add(ChatColor.GREEN + " Enough light.");
                    }

                    output.add(ChatColor.WHITE + "  Growth time: " + TimeUtil.prettyfyTime(gps.getGrowthTime()));
                    if (gps.getCanRot()) {
                        output.add(ChatColor.WHITE + "  Rot time   : " + TimeUtil.prettyfyTime(gps.getRotsAfter()));
                    } else {
                        output.add(ChatColor.WHITE + "  Rot time   : DOESN'T ROT");
                    }
                    if (gps.getAccelerator() != Material.STICK) {
                        output.add(ChatColor.WHITE + "  Accelerator: " + gps.getAccelerator().name());
                        output.add(ChatColor.WHITE + "  Max. Accelerators: " + gps.getMaxAccelerators());
                        output.add(ChatColor.WHITE + "  Acceleration:" + (gps.getAcceleration() * 100) + "%");
                    }
                    output.add(ChatColor.WHITE + "  Failure Rate: " + (gps.getFailureRate() * 100) + "%");
                }

                ev.getPlayer().sendMessage(output.toArray(new String[output.size()]));
            });
        }
    }
}
