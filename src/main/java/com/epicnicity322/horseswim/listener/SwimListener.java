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

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public final class SwimListener implements Listener {
    private boolean preventAutoForHorses = false;
    private double autoSwimVelocity = 0.15;

    public void setPreventAutoForHorses(boolean preventAutoForHorses) {
        this.preventAutoForHorses = preventAutoForHorses;
    }

    public void setAutoSwimVelocity(double autoSwimVelocity) {
        this.autoSwimVelocity = autoSwimVelocity;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to != null && from.getY() > to.getY()) {
            Player player = event.getPlayer();
            Entity vehicle = player.getVehicle();

            if (vehicle != null) {
                if (preventAutoForHorses && vehicle.getType() == EntityType.HORSE) return;
                if (player.isInWater() && vehicle.isInWater()) {
                    vehicle.setVelocity(vehicle.getVelocity().setY(autoSwimVelocity));
                }
            }
        }
    }
}
