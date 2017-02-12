/*
 * Copyright (c) 2017 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.*;

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
            new Spaceship(GPlayer.getGPlayer(p), -1, -1, -1, -1, -1);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        String stationId = gc.getPlayerData().getStation(p.getUniqueId().toString());
        if (!stationId.equals("")) { //If player has a station, respawn there
            e.setRespawnLocation(gc.getStations().getRespawn(stationId));
        } else {
            new Spaceship(GPlayer.getGPlayer(p), -1, -1, -1, -1, -1);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        CustomItem.Item cItem = CustomItem.Item.getCustomItem(e.getItem());
        GPlayer p = GPlayer.getGPlayer(e.getPlayer());
        if (cItem != null && e.getItem().getAmount() == 1) { // Check if it's a valid custom item, and the cooldown (determined by the number of items) is 0 (1 item)
            switch (cItem) {
                case SPACESHIP_CONTROL_CONTROLS:
                    if (p.getSpaceship() != null) p.getSpaceship().laser();
                    break;
            }
        }
    }

    @EventHandler
    public void onToggleElytra(EntityToggleGlideEvent e) { // Cancel the automatic toggle elytra off after Player#setGliding(true) if Player doesn't have elytra
        if (!e.isGliding() && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (GPlayer.getGPlayer(p).getSpaceship() != null) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onToggleSprint(PlayerToggleSprintEvent e) {
        GPlayer p = GPlayer.getGPlayer(e.getPlayer());
        if (p.getSpaceship() != null) {
            p.getSpaceship().setThrust(e.isSprinting());
        }
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        GPlayer p = GPlayer.getGPlayer(e.getPlayer());
        if (p.getSpaceship() != null) {
            p.getSpaceship().setReverse(e.isSneaking());
        }
    }
}
