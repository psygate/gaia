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

import com.psygate.minecraft.spigot.sovereignty.gaia.db.model.tables.records.GaiaStatesRecord;
import com.psygate.minecraft.spigot.sovereignty.gaia.plants.growthhandlers.GrowthHandlerReturnState;

/**
 * Created by psygate on 12.05.2016.
 */
public class ReturnContainer {
    private GrowthHandlerReturnState state;
    private GaiaStatesRecord rec;

    public ReturnContainer(GrowthHandlerReturnState state, GaiaStatesRecord rec) {
        this.state = state;
        this.rec = rec;
    }

    public GrowthHandlerReturnState getState() {
        return state;
    }

    public void setState(GrowthHandlerReturnState state) {
        this.state = state;
    }

    public GaiaStatesRecord getRec() {
        return rec;
    }

    public void setRec(GaiaStatesRecord rec) {
        this.rec = rec;
    }

    @Override
    public String toString() {
        return "ReturnContainer{" +
                "state=" + state +
                ", rec=" + rec +
                '}';
    }
}
