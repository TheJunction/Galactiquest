/*
 * Copyright (c) 2016 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft;

import io.thejunct.Galacticraft.database.PlayerData;
import io.thejunct.Galacticraft.database.Stations;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by david on 10/22.
 *
 * @author david
 */
public class Galacticraft extends JavaPlugin {
    private static Galacticraft instance;
    private PlayerData playerData;
    private Stations stations;
    private Dictionary dictionary;

    private File libDir;

    public static Galacticraft getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        libDir = new File(getDataFolder(), "lib");

        if (!libDir.exists() && !libDir.mkdir()) {
            getLogger().log(Level.SEVERE, "Unable to create lib directory. No write access?");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        playerData = new PlayerData();
        stations = new Stations();
        dictionary = new Dictionary();

        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
    }

    public File getLibDir() {
        return this.libDir;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public Stations getStations() {
        return stations;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }
}
