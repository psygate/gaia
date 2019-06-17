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

package com.psygate.minecraft.spigot.sovereignty.gaia;

import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.Configuration;
import com.psygate.minecraft.spigot.sovereignty.gaia.listeners.GaiaPlayerListener;
import com.psygate.minecraft.spigot.sovereignty.gaia.listeners.PlantListener;
import com.psygate.minecraft.spigot.sovereignty.gaia.workers.GaiaWorker;
import com.psygate.minecraft.spigot.sovereignty.nucleus.Nucleus;
import com.psygate.minecraft.spigot.sovereignty.nucleus.managment.NucleusPlugin;
import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.DatabaseInterface;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

/**
 * Created by psygate (https://github.com/psygate) on 20.02.2016.
 */
public class Gaia extends JavaPlugin implements NucleusPlugin {
    private static Gaia instance;
    private final static Logger LOG = Logger.getLogger(Gaia.class.getName());
    //    private final File aliasFile = new File(getDataFolder(), "biome_aliases.yml");
    private DatabaseInterface dbi;
    private Configuration config;

    static {
        LOG.setUseParentHandlers(false);
        LOG.setLevel(Level.ALL);
        List<Handler> handlers = Arrays.asList(LOG.getHandlers());

        if (handlers.stream().noneMatch(h -> h instanceof FileHandler)) {
            try {
                File logdir = new File("logs/nucleus_logs/gaia/");
                if (!logdir.exists()) {
                    logdir.mkdirs();
                }
                FileHandler fh = new FileHandler(
                        "logs/nucleus_logs/gaia/gaia.%u.%g.log",
                        8 * 1024 * 1024,
                        12,
                        true
                );
                fh.setLevel(Level.ALL);
                fh.setEncoding("UTF-8");
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                LOG.addHandler(fh);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Logger getLogger(String name) {
        Logger log = Logger.getLogger(name);
        log.setParent(LOG);
        log.setUseParentHandlers(true);
        log.setLevel(Level.ALL);
        return log;
    }

    public static Configuration getConfiguration() {
        return getInstance().config;
    }

    @Override
    public void onEnable() {
        try {
            subEnable();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void subEnable() {
        instance = this;
        Nucleus.getInstance().register(this);
        saveDefaultConfig();
        config = new Configuration(loadConfig());
        GaiaWorker.getInstance().start();
    }

    @Override
    public void onDisable() {
        GaiaWorker.getInstance().stop();
    }

    private String loadConfig() {
        try (FileInputStream in = new FileInputStream(new File(getDataFolder(), "config.yml"))) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read;

            while ((read = in.read()) != -1) {
                out.write(read);
            }

            return new String(out.toByteArray(), Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Gaia getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Gaia is not initialized.");
        }

        return instance;
    }

    @Override
    public int getWantedDBVersion() {
        return 0;
    }

    @Override
    public void fail() {
        Logger.getAnonymousLogger().info("Gaia unable to run.");
        Bukkit.shutdown();
    }

    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(new GaiaPlayerListener(), new PlantListener());
    }

    @Override
    public void setLogger(Logger logger) {
        //this.LOG = logger;
    }

    @Override
    public Logger getPluginLogger() {
        return LOG;
    }

    public static DatabaseInterface DBI() {
        return getInstance().dbi;
    }

    @Override
    public void setDatabaseInterface(DatabaseInterface databaseInterface) {
        dbi = databaseInterface;
    }
}
