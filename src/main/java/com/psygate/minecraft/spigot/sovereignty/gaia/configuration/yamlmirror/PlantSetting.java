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

import com.psygate.minecraft.spigot.sovereignty.gaia.configuration.gaia.GaiaPlantSetting;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.Plant;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.PlantManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Objects;

/**
 * Created by psygate on 06.05.2016.
 */
public class PlantSetting {
    private GroupSetting setting;
    private String plant;
    private String inherit;
    private Integer minLightLevel;
    private Integer maxLightLevel;
    private Boolean requireSkyLight;
    private String growthTime;
    private Material accelerator;
    private Integer maxAccelerators;
    private String acceleration;
    private String rotsAfter;
    private String failureRate;

    private PlantSetting parent;

    public PlantSetting() {
    }

    public PlantSetting(String inherit, Integer minLightLevel, Integer maxLightLevel, Boolean requireSkyLight, String growthTime, Material accelerator, Integer maxAccelerators, String acceleration, String rotsAfer, String failureRate) {
        this.inherit = inherit;
        this.minLightLevel = minLightLevel;
        this.maxLightLevel = maxLightLevel;
        this.requireSkyLight = requireSkyLight;
        this.growthTime = growthTime;
        this.accelerator = accelerator;
        this.maxAccelerators = maxAccelerators;
        this.acceleration = acceleration;
        this.rotsAfter = rotsAfer;
        this.failureRate = failureRate;
    }

    public Integer getMaxAccelerators() {
        if (maxAccelerators != null) {
            return maxAccelerators;
        } else {
            try {
                return getParent().getMaxAccelerators();
            } catch (StackOverflowError ex) {
                throw new IllegalStateException("Circular dependency detected. " + setting.getName() + "." + plant);
            } catch (IllegalStateException ex) {
                throw new IllegalStateException(ex.getMessage());
            } catch (NoParentException ex) {
                throw new IllegalStateException("Property undefined on " + setting.getName() + "." + plant);
            }
        }
    }

    public void setMaxAccelerators(Integer maxAccelerators) {
        this.maxAccelerators = maxAccelerators;
    }

    public String getInherit() {
        return inherit;
    }

    public void setInherit(String inherit) {
        this.inherit = inherit;
    }

    public Integer getMinLightLevel() {
        if (minLightLevel != null) {
            return minLightLevel;
        } else {
            try {
                return getParent().getMinLightLevel();
            } catch (StackOverflowError ex) {
                throw new IllegalStateException("Circular dependency detected. " + setting.getName() + "." + plant);
            } catch (IllegalStateException ex) {
                throw new IllegalStateException(ex.getMessage());
            } catch (NoParentException ex) {
                throw new IllegalStateException("Property undefined on " + setting.getName() + "." + plant);
            }
        }
    }

    public void setMinLightLevel(Integer minLightLevel) {
        this.minLightLevel = minLightLevel;
    }

    public Integer getMaxLightLevel() {
        if (maxLightLevel != null) {
            return maxLightLevel;
        } else {
            try {
                return getParent().getMaxLightLevel();
            } catch (StackOverflowError ex) {
                throw new IllegalStateException("Circular dependency detected. " + setting.getName() + "." + plant);
            } catch (IllegalStateException ex) {
                throw new IllegalStateException(ex.getMessage());
            } catch (NoParentException ex) {
                throw new IllegalStateException("Property undefined on " + setting.getName() + "." + plant);
            }
        }
    }

    public void setMaxLightLevel(Integer maxLightLevel) {
        this.maxLightLevel = maxLightLevel;
    }

    public Boolean getRequireSkyLight() {
        if (requireSkyLight != null) {
            return requireSkyLight;
        } else {
            try {
                return getParent().getRequireSkyLight();
            } catch (StackOverflowError ex) {
                throw new IllegalStateException("Circular dependency detected. " + setting.getName() + "." + plant);
            } catch (IllegalStateException ex) {
                throw new IllegalStateException(ex.getMessage());
            } catch (NoParentException ex) {
                throw new IllegalStateException("Property undefined on " + setting.getName() + "." + plant);
            }
        }
    }

    public void setRequireSkyLight(Boolean requireSkyLight) {
        this.requireSkyLight = requireSkyLight;
    }

    public String getGrowthTime() {
        if (growthTime != null) {
            return growthTime;
        } else {
            try {
                return getParent().getGrowthTime();
            } catch (StackOverflowError ex) {
                throw new IllegalStateException("Circular dependency detected. " + setting.getName() + "." + plant);
            } catch (IllegalStateException ex) {
                throw new IllegalStateException(ex.getMessage());
            } catch (NoParentException ex) {
                throw new IllegalStateException("Property undefined on " + setting.getName() + "." + plant);
            }
        }
    }

    public void setGrowthTime(String growthTime) {
        this.growthTime = growthTime;
    }

    public Material getAccelerator() {
        if (accelerator != null) {
            return accelerator;
        } else {
            try {
                return getParent().getAccelerator();
            } catch (StackOverflowError ex) {
                throw new IllegalStateException("Circular dependency detected. " + setting.getName() + "." + plant);
            } catch (IllegalStateException ex) {
                throw new IllegalStateException(ex.getMessage());
            } catch (NoParentException ex) {
                throw new IllegalStateException("Property undefined on " + setting.getName() + "." + plant);
            }
        }
    }

    public void setAccelerator(Material accelerator) {
        this.accelerator = accelerator;
    }

    public String getAcceleration() {
        if (acceleration != null) {
            return acceleration;
        } else {
            try {
                return getParent().getAcceleration();
            } catch (StackOverflowError ex) {
                throw new IllegalStateException("Circular dependency detected. " + setting.getName() + "." + plant);
            } catch (IllegalStateException ex) {
                throw new IllegalStateException(ex.getMessage());
            } catch (NoParentException ex) {
                throw new IllegalStateException("Property undefined on " + setting.getName() + "." + plant);
            }
        }
    }

    public void setAcceleration(String acceleration) {
        this.acceleration = acceleration;
    }

    public String getRotsAfter() {
        if (rotsAfter != null) {
            return rotsAfter;
        } else {
            try {
                return getParent().getRotsAfter();
            } catch (StackOverflowError ex) {
                throw new IllegalStateException("Circular dependency detected. " + setting.getName() + "." + plant);
            } catch (IllegalStateException ex) {
                throw new IllegalStateException(ex.getMessage());
            } catch (NoParentException ex) {
                throw new IllegalStateException("Property undefined on " + setting.getName() + "." + plant);
            }
        }
    }

    public void setRotsAfter(String rotsAfter) {
        this.rotsAfter = rotsAfter;
    }

    public String getFailureRate() {
        if (failureRate != null) {
            return failureRate;
        } else {
            try {
                return getParent().getFailureRate();
            } catch (StackOverflowError ex) {
                throw new IllegalStateException("Circular dependency detected. " + setting.getName() + "." + plant);
            } catch (IllegalStateException ex) {
                throw new IllegalStateException(ex.getMessage());
            } catch (NoParentException ex) {
                throw new IllegalStateException("Property undefined on " + setting.getName() + "." + plant);
            }
        }
    }

    public void setFailureRate(String failureRate) {
        this.failureRate = failureRate;
    }

    public PlantSetting getParent() {
        if (parent == this) {
            throw new NoParentException("Parent undefined. " + setting.getName() + "." + plant);
        }
        return Objects.requireNonNull(parent, () -> "Inheritance required but parent not set. " + inherit);
    }

    public void setParent(PlantSetting parent) {
        this.parent = parent;
    }

    public GaiaPlantSetting asPlantSetting(String plantname) {
        return new GaiaPlantSetting(
                PlantManager.getInstance().getPlant(plantname).orElseThrow(() -> new IllegalArgumentException("No plant for name: " + plantname)),
                getMinLightLevel(),
                getMaxLightLevel(),
                getRequireSkyLight(),
                getGrowthTime(),
                getAccelerator(),
                getMaxAccelerators(),
                getAcceleration(),
                getRotsAfter(),
                getFailureRate()
        );
    }

    @Override
    public String toString() {
        return "GaiaPlantSetting{" +
                "inherit='" + inherit + '\'' +
                ", minLightLevel=" + minLightLevel +
                ", maxLightLevel=" + maxLightLevel +
                ", requireSkyLight=" + requireSkyLight +
                ", growthTime='" + growthTime + '\'' +
                ", accelerator=" + accelerator +
                ", maxAccelerators=" + maxAccelerators +
                ", acceleration='" + acceleration + '\'' +
                ", rotsAfter='" + rotsAfter + '\'' +
                ", failureRate='" + failureRate + '\'' +
                '}';
    }

    public void normalize(String inheritanceBiome) {
        if (inherit != null) {
            inherit = inherit.toLowerCase();
            if (!inherit.contains(":")) {
                inherit = Objects.requireNonNull(inheritanceBiome) + ":" + inherit;
            }
        }
    }

    public GroupSetting getSetting() {
        return setting;
    }

    public void setSetting(GroupSetting setting) {
        this.setting = setting;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public void normalize() {

    }
}
