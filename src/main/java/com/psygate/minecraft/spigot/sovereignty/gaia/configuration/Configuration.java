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

package com.psygate.minecraft.spigot.sovereignty.gaia.configuration;

import com.psygate.minecraft.spigot.sovereignty.gaia.Gaia;
import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.gaia.GaiaConf;
import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.yamlmirror.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import java.util.logging.Logger;

/**
 * Created by psygate on 06.05.2016.
 */
public class Configuration {
    private final static Logger LOG = Gaia.getLogger(Configuration.class.getName());
    private final GaiaConf conf;

    public Configuration(String config) {
        Yaml yaml = getYaml();

        GaiaConfiguration conf = yaml.loadAs(config, GaiaConfiguration.class);
        conf.normalize();
        conf.checkBiomes();
        conf.pushDown();
        conf.resolveInheritance();
        GaiaConf gc = conf.asGaiaConf();
        gc.checkIntegrity();
        gc.cleanup();
        this.conf = gc;
        LOG.finer("Loaded conf: " + conf);
    }

    public Yaml getYaml() {
        BaseConstructor constr = new CustomClassLoaderConstructor(Configuration.class.getClassLoader());
        Yaml yaml = new Yaml(constr, getRepresenter(), getDumperOptions());
        return yaml;
    }

    private Representer getRepresenter() {
        Representer repr = new Representer();
        repr.setPropertyUtils(getPropertyUtils());
        return new Representer();
    }

    private PropertyUtils getPropertyUtils() {
        PropertyUtils utils = new PropertyUtils();
        utils.setSkipMissingProperties(true);
        return utils;
    }

    private DumperOptions getDumperOptions() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setAllowUnicode(true);
        dumperOptions.setCanonical(false);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        return dumperOptions;
    }

    public GaiaConf getConf() {
        return conf;
    }
}
