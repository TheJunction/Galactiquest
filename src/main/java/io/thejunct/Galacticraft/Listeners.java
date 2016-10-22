/*
 * Copyright (c) 2016 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Created by david on 10/22.
 *
 * @author david
 */
class Listeners implements Listener {
    private static Galacticraft gc;

    Listeners() {
        gc = Galacticraft.getInstance();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (gc.getPlayerData().getName(p.getUniqueId().toString()) != null) { //If the player is joining for the first time
            gc.getPlayerData().setName(p.getUniqueId().toString(), p.getName());
            new Tutorial(p);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        String stationId = gc.getPlayerData().getStation(p.getUniqueId().toString());
        if (!stationId.equals("")) {
            e.setRespawnLocation(gc.getStations().getRespawn(stationId));
        } else {
            //TODO: Create new spaceship for player
        }
    }
}
