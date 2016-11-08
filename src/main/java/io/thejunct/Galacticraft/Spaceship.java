/*
 * Copyright (c) 2016 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft;

import org.bukkit.entity.Player;

/**
 * Created by david on 11/07.
 *
 * @author david
 */
class Spaceship {
    private GPlayer owner;
    private Player p;

    Spaceship(GPlayer owner) {
        this.owner = owner;
        p = owner.getBukkitPlayer();
        owner.setSpaceship(this);
        owner.getBukkitPlayer().getInventory().setChestplate(CustomItem.SPACESHIP.create(1));
        owner.getBukkitPlayer().getInventory().setHelmet(CustomItem.SPACESHIP_HEAD.create(1));
    }

    void thrust(int amount) {
        p.setVelocity(p.getLocation().getDirection().multiply(amount + 1));
    }

    void reverse() {
        //TODO: Make reverse go smoothly (gradually slow down, then start reversing)
        p.setVelocity(p.getLocation().getDirection().multiply(-1));
    }

    void laser() {

    }
}
