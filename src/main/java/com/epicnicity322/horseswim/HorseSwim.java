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

package com.epicnicity322.horseswim;

import com.epicnicity322.epicpluginlib.bukkit.logger.Logger;
import com.epicnicity322.epicpluginlib.core.config.ConfigurationHolder;
import com.epicnicity322.epicpluginlib.core.config.ConfigurationLoader;
import com.epicnicity322.horseswim.command.HorseReloadCommand;
import com.epicnicity322.horseswim.listener.DismountListener;
import com.epicnicity322.horseswim.listener.HorseJumpListener;
import com.epicnicity322.horseswim.listener.SwimListener;
import com.epicnicity322.yamlhandler.Configuration;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;
import java.util.Map;

public final class HorseSwim extends JavaPlugin {
    private static final @NotNull ConfigurationHolder config = new ConfigurationHolder(Paths.get("plugins", "HorseSwim", "config.yml"),
            "# Swim modes: AUTO and MANUAL. MANUAL works only for horses, all other animals swim in AUTO.\n" +
                    "Swim Mode: AUTO\n" +
                    "\n" +
                    "# Prevents the player from being dismounted automatically when they are under water.\n" +
                    "# Players can still crouch to dismount underwater, this only prevents the vanilla dismount behavior.\n" +
                    "Prevent Auto Underwater Dismount: true\n" +
                    "\n" +
                    "# In manual mode, horses jump force in water is: Power of the jump * Horse max jump strength * This multiplier.\n" +
                    "Manual Swim Jump Multiplier: 1.4\n" +
                    "\n" +
                    "# The Y velocity to set to the entity when Swim Mode is AUTO.\n" +
                    "Auto Swim Velocity: 0.15");
    private static final @NotNull ConfigurationLoader loader = new ConfigurationLoader();
    private static final @NotNull Logger logger = new Logger("[HorseSwim] ");
    private static @Nullable HorseSwim instance;

    static {
        loader.registerConfiguration(config);
    }

    private final @NotNull HorseJumpListener horseJumpListener = new HorseJumpListener();
    private final @NotNull DismountListener dismountListener = new DismountListener();
    private final @NotNull SwimListener swimListener = new SwimListener();

    public HorseSwim() {
        instance = this;
        logger.setLogger(getLogger());
    }

    /**
     * Reloads configurations and listeners of HorseSwim.
     *
     * @return Whether configuration was loaded successfully.
     */
    public static boolean reload() {
        if (instance == null) return false;

        Map<ConfigurationHolder, Exception> exceptions = loader.loadConfigurations();

        if (!exceptions.isEmpty()) {
            logger.log("Failed to load config! Is the YAML Syntax valid?");
            exceptions.forEach((c, e) -> e.printStackTrace());
        }

        instance.loadListeners();
        return exceptions.isEmpty();
    }

    @Override
    public void onEnable() {
        logger.setLogger(getLogger());
        reload();

        PluginCommand horseReload = getCommand("horsereload");

        if (horseReload != null) {
            horseReload.setExecutor(new HorseReloadCommand());
        }
    }

    private void loadListeners() {
        PluginManager pm = getServer().getPluginManager();
        Configuration config = HorseSwim.config.getConfiguration();

        if (config.getBoolean("Prevent Auto Underwater Dismount").orElse(false)) {
            pm.registerEvents(dismountListener, this);
        } else {
            HandlerList.unregisterAll(dismountListener);
        }

        String mode = config.getString("Swim Mode").orElse("AUTO");

        if (mode.equalsIgnoreCase("MANUAL")) {
            horseJumpListener.setJumpMultiplier(config.getNumber("Manual Swim Jump Multiplier").orElse(1.4).doubleValue());
            pm.registerEvents(horseJumpListener, this);
            swimListener.setPreventAutoForHorses(true);
        } else {
            HandlerList.unregisterAll(horseJumpListener);
            swimListener.setPreventAutoForHorses(false);
        }

        swimListener.setAutoSwimVelocity(config.getNumber("Auto Swim Velocity").orElse(0.2).doubleValue());
        pm.registerEvents(swimListener, this);
    }
}
