/*
 * Copyright (c) 2016 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Created by david on 10/22.
 *
 * @author david
 */
class Listeners implements Listener {
    private static Galacticraft gc = Galacticraft.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (gc.getPlayerData().getName(p.getUniqueId().toString()) != null) { //If the player is joining for the first time, start the tutorial
            gc.getPlayerData().setName(p.getUniqueId().toString(), p.getName());
            new Tutorial(GPlayer.getGPlayer(p));
        } else if (gc.getPlayerData().getStation(p.getUniqueId().toString()).equals("")) { //Create new spaceship if player
            new Spaceship(GPlayer.getGPlayer(p));
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        String stationId = gc.getPlayerData().getStation(p.getUniqueId().toString());
        if (!stationId.equals("")) { //If player has a station, respawn there
            e.setRespawnLocation(gc.getStations().getRespawn(stationId));
        } else {
            new Spaceship(GPlayer.getGPlayer(p));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        CustomItem cItem = CustomItem.getCustomItem(e.getItem());
        GPlayer p = GPlayer.getGPlayer(e.getPlayer());
        if (cItem != null) {
            switch (cItem) {
                case SPACESHIP_CONTROL_THRUST:
                    if (p.getSpaceship() != null) p.getSpaceship().thrust(e.getItem().getAmount());
                    break;
                case SPACESHIP_CONTROL_REVERSE:
                    if (p.getSpaceship() != null) p.getSpaceship().reverse();
                    break;
                case SPACESHIP_CONTROL_LASER:
                    if (p.getSpaceship() != null) p.getSpaceship().laser();
                    break;
            }
        }
    }
}
