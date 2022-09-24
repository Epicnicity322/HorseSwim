/*
 * HorseSwim - Minecraft Spigot plugin that allows rideable entities to swim.
 * Copyright (C) 2022 Christiano Rangel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.epicnicity322.horseswim.listener;

import com.epicnicity322.horseswim.util.HorseSwimUtil;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.HorseJumpEvent;

public final class HorseJumpListener implements Listener {
    //Config: "Manual Swim Jump Multiplier"
    private double jumpMultiplier = 1.4;

    public void setJumpMultiplier(double jumpMultiplier) {
        this.jumpMultiplier = jumpMultiplier;
    }

    @EventHandler
    public void onHorseJump(HorseJumpEvent event) {
        Horse horse = (Horse) event.getEntity();
        if (!HorseSwimUtil.inWater(horse)) return;

        horse.setVelocity(horse.getVelocity().setY(event.getPower() * horse.getJumpStrength() * jumpMultiplier));
    }
}
