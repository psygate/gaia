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

package com.psygate.minecraft.spigot.sovereignty.gaia.configuration.gaia;

import com.psygate.minecraft.spigot.sovereignty.gaia.plants.Plant;
import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.util.TimeUtil;
import org.bukkit.Material;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Created by psygate on 06.05.2016.
 */
public class GaiaPlantSetting {
    private Optional<GaiaPlantSetting> parent = Optional.empty();
    private Plant plant;
    private OptionalInt minLightLevel;
    private OptionalInt maxLightLevel;
    private Optional<Boolean> requireSkyLight;
    private OptionalLong growthTime;
    private Optional<Material> accelerator;
    private OptionalInt maxAccelerators;
    private OptionalDouble acceleration;
    private OptionalLong rotsAfter;
    private OptionalDouble failureRate;
    private Boolean canRot;

    public GaiaPlantSetting() {
    }

    public GaiaPlantSetting(Plant plant, Integer minLightLevel, Integer maxLightLevel, Boolean requireSkyLight, String growthTime, Material accelerator, Integer maxAccelerators, String acceleration, String rotsAfer, String failureRate) {
        this.minLightLevel = (minLightLevel != null) ? OptionalInt.of(minLightLevel) : OptionalInt.empty();
        this.maxLightLevel = (maxLightLevel != null) ? OptionalInt.of(maxLightLevel) : OptionalInt.empty();
        this.requireSkyLight = (requireSkyLight != null) ? Optional.of(requireSkyLight) : Optional.empty();
        this.growthTime = (growthTime != null) ? OptionalLong.of(TimeUtil.parseTimeStringToMillis(growthTime)) : OptionalLong.empty();
        this.accelerator = (accelerator != null) ? Optional.of(accelerator) : Optional.empty();
        this.maxAccelerators = (maxAccelerators != null) ? OptionalInt.of(maxAccelerators) : OptionalInt.empty();
        this.acceleration = (acceleration != null) ? OptionalDouble.of(TimeUtil.parsePercent(acceleration)) : OptionalDouble.empty();
        this.rotsAfter = (rotsAfer != null) ? OptionalLong.of(TimeUtil.parseTimeStringToMillis(rotsAfer)) : OptionalLong.empty();
        this.failureRate = (failureRate != null) ? OptionalDouble.of(TimeUtil.parsePercent(failureRate)) : OptionalDouble.empty();
        this.plant = plant;
        this.canRot = getRotsAfter() != Long.MAX_VALUE;
    }

    public GaiaPlantSetting getParent() {
        return parent.orElseThrow(() -> new IllegalArgumentException("No parent. " + plant.getName()));
    }

    public void setParent(Optional<GaiaPlantSetting> parent) {
        this.parent = parent;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public int getMinLightLevel() {
        return minLightLevel.orElseGet(() -> getParent().getMinLightLevel());
    }

    public void setMinLightLevel(int minLightLevel) {
        this.minLightLevel = OptionalInt.of(minLightLevel);
    }

    public int getMaxLightLevel() {
        return maxLightLevel.orElseGet(() -> getParent().getMaxLightLevel());
    }

    public void setMaxLightLevel(int maxLightLevel) {
        this.maxLightLevel = OptionalInt.of(maxLightLevel);
    }

    public boolean getRequireSkyLight() {
        return requireSkyLight.orElseGet(() -> getParent().getRequireSkyLight());
    }

    public void setRequireSkyLight(boolean requireSkyLight) {
        this.requireSkyLight = Optional.of(requireSkyLight);
    }

    public long getGrowthTime() {
        return growthTime.orElseGet(() -> getParent().getGrowthTime());
    }

    public void setGrowthTime(long growthTime) {
        this.growthTime = OptionalLong.of(growthTime);
    }

    public Material getAccelerator() {
        return accelerator.orElseGet(() -> getParent().getAccelerator());
    }

    public void setAccelerator(Material accelerator) {
        this.accelerator = Optional.of(accelerator);
    }

    public int getMaxAccelerators() {
        return maxAccelerators.orElseGet(() -> getParent().getMaxAccelerators());
    }

    public void setMaxAccelerators(int maxAccelerators) {
        this.maxAccelerators = OptionalInt.of(maxAccelerators);
    }

    public double getAcceleration() {
        return acceleration.orElseGet(() -> getParent().getAcceleration());
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = OptionalDouble.of(acceleration);
    }

    public long getRotsAfter() {
        return rotsAfter.orElseGet(() -> getParent().getRotsAfter());
    }

    public void setRotsAfter(long rotsAfter) {
        this.rotsAfter = OptionalLong.of(rotsAfter);
    }

    public double getFailureRate() {
        return failureRate.orElseGet(() -> getParent().getRotsAfter());
    }

    public void setFailureRate(double failureRate) {
        this.failureRate = OptionalDouble.of(failureRate);
    }

    public Boolean getCanRot() {
        return canRot;
    }

    @Override
    public String toString() {
        return "GaiaPlantSetting{" +
                "parent=" + getParent() +
                ", plant=" + plant +
                ", minLightLevel=" + getMinLightLevel() +
                ", maxLightLevel=" + getMaxLightLevel() +
                ", requireSkyLight=" + getRequireSkyLight() +
                ", growthTime=" + getGrowthTime() +
                ", accelerator=" + getAccelerator() +
                ", maxAccelerators=" + getMaxAccelerators() +
                ", acceleration=" + getAcceleration() +
                ", rotsAfter=" + getRotsAfter() +
                ", failureRate=" + getFailureRate() +
                '}';
    }
}
