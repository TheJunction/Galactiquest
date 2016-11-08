/*
 * Copyright (c) 2016 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 11/07.
 *
 * @author david
 */
class GPlayer {
    private static Galacticraft gc = Galacticraft.getInstance();
    private static Map<Player, GPlayer> gPlayers = new HashMap<>();

    private Player bukkitPlayer;
    private Spaceship spaceship = null;

    private GPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    static GPlayer getGPlayer(Player p) {
        if (!gPlayers.containsKey(p)) {
            GPlayer gP = new GPlayer(p);
            gPlayers.put(p, gP);
        }
        return gPlayers.get(p);
    }

    Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    Spaceship getSpaceship() {
        return spaceship;
    }

    void setSpaceship(Spaceship spaceship) {
        this.spaceship = spaceship;
        if (spaceship == null) {
            gc.getPlayerData().setSpaceShip(bukkitPlayer.getUniqueId().toString(), false);
        } else {
            gc.getPlayerData().setSpaceShip(bukkitPlayer.getUniqueId().toString(), true);
        }
    }
}
