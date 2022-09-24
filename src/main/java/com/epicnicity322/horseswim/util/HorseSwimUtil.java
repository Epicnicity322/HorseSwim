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

package com.epicnicity322.horseswim.util;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class HorseSwimUtil {
    private static final boolean has_isInWater_Method;

    static {
        boolean hasIsInWater;

        try {
            Entity.class.getMethod("isInWater");
            hasIsInWater = true;
        } catch (NoSuchMethodException ignored) {
            hasIsInWater = false;
        }

        has_isInWater_Method = hasIsInWater;
    }

    private HorseSwimUtil() {
    }

    public static boolean inWater(@NotNull Entity entity) {
        if (has_isInWater_Method) {
            return entity.isInWater();
        } else {
            return entity.getLocation().getBlock().getType() == Material.WATER;
        }
    }
}
