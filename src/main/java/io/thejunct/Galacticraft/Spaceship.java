/*
 * Copyright (c) 2017 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by david on 11/07.
 *
 * @author david
 */
class Spaceship {
    private static final double REVERSE_MIN = 0.1;
    private static final int CHUNK_LENGTH = 16;

    private static List<Spaceship> spaceships;

    static {
        spaceships = new ArrayList<>();
        Bukkit.getScheduler().runTaskTimerAsynchronously(Galacticraft.getInstance(), () -> {
            for (int i = 0; i < spaceships.size(); i++) {
                Spaceship ship = spaceships.get(i);
                Player p = ship.getOwner().getBukkitPlayer();
                if (p.isGliding()) {
                    double speed = ship.getNormal();
                    if (ship.isThrust()) { // Multiply normal speed by thrust multiplier if thrusting
                        speed *= ship.getThrustMult();
                    }

                    if (!ship.isReverse()) {
                        if (p.getVelocity().length() < speed * 0.8 && p.getLocation().getY() <= p.getWorld().getMaxHeight()) { // Change normal if moving slower than minimum (80% of normal) and under max height
                            p.setVelocity(p.getVelocity().normalize().multiply(ship.getNormal())); // Set player speed to normal speed, but retain direction
                        }
                        p.getLocation().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation(), 10);
                    } else {
                        p.setVelocity(p.getVelocity().multiply(ship.getReverseMult())); // Multiply speed by reverseMult
                        if (p.getVelocity().length() < REVERSE_MIN) { // Stop the player if going slow enough
                            p.setVelocity(p.getVelocity().zero());
                        }
                    }
                } else { // If player is no longer gliding, remove the Spaceship from both list and GPlayer
                    spaceships.remove(i);
                    ship.getOwner().setSpaceship(null);
                    i--;
                }
            }
        }, 0, 5); // Repeat every 5 ticks
    }

    private GPlayer owner;
    private boolean thrust = false,
            reverse = false;
    private double normal,
            reverseMult,
            thrustMult;
    private int laserRadius, // Blocks broken by laser
            laserRange;

    Spaceship(GPlayer owner, double normal, double reverseMult, double thrustMult, int laserRadius, int laserRange) {
        this.owner = owner;
        this.normal = normal;
        this.reverseMult = reverseMult;
        this.thrustMult = thrustMult;
        this.laserRadius = laserRadius;
        this.laserRange = laserRange;

        owner.setSpaceship(this);
        owner.getBukkitPlayer().getInventory().setChestplate(CustomItem.Item.SPACESHIP.create(1));  // Set the chest item to the textured elytra
        owner.getBukkitPlayer().getInventory().setHelmet(CustomItem.Item.SPACESHIP_HEAD.create(1)); // Set the head item to the spaceship cockpit
        owner.getBukkitPlayer().setGliding(true);
        spaceships.add(this);
    }

    Spaceship(GPlayer owner) {
        this(owner, 1.6, 0.8, 1.2, 0, Bukkit.getViewDistance() * CHUNK_LENGTH); // Set default values
    }

    private GPlayer getOwner() {
        return owner;
    }

    private double getNormal() {
        return normal;
    }

    private boolean isThrust() {
        return thrust;
    }

    public void setThrust(boolean thrust) {
        this.thrust = thrust;
        if (thrust) {
            reverse = false;
        }
    }

    private double getThrustMult() {
        return thrustMult;
    }

    private boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
        if (reverse) {
            thrust = false;
        }
    }

    private double getReverseMult() {
        return reverseMult;
    }

    void laser() {
        Player p = owner.getBukkitPlayer();
        Set<Material> transparent = new HashSet<>();
        transparent.add(Material.AIR);

        List<Block> blocks = p.getLineOfSight(transparent, laserRange);
        Location loc = null;
        for (Block block : blocks) {
            loc = block.getLocation();
            break;
        }

        if (loc != null) {
            for (int x = loc.getBlockX() - laserRange; x <= loc.getBlockX() + laserRange; x++) {
                for (int y = loc.getBlockY() - laserRange; y <= loc.getBlockY() + laserRange; y++) {
                    for (int z = loc.getBlockZ() - laserRange; z <= loc.getBlockZ() + laserRange; z++) {
                        Block block = loc.getWorld().getBlockAt(x, y, z);
                        if (block.getType() != Material.AIR) {
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        } else {
            loc = p.getLocation().add(p.getVelocity().normalize().multiply(laserRange));
        }
    }
}
