/*
 * Copyright (c) 2016 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Created by david on 11/07.
 *
 * @author david
 */
enum CustomItem {
    SPACESHIP(0, Material.ELYTRA),
    SPACESHIP_HEAD(1561),
    SPACESHIP_CONTROL_THRUST(ChatColor.GREEN + "Thrust", ChatColor.BLUE.toString() + "Right click to move forward.", 1560),
    SPACESHIP_CONTROL_REVERSE(ChatColor.RED + "Reverse", ChatColor.BLUE.toString() + "Right click to move backwards.", 1559),
    SPACESHIP_CONTROL_LASER(ChatColor.DARK_RED + "Laser", 1558, ChatColor.BLUE + "Right click to move shoot a laser.", "Mines meteors.");

    private String name;
    private List<String> lore;
    private int dmg;
    private Material type;

    CustomItem(String name, int dmg, String... lore) {
        this.name = name;
        this.lore = Arrays.asList(lore);
        this.dmg = dmg;
        this.type = Material.DIAMOND_HOE;
    }

    CustomItem(int dmg, Material type) {
        this.name = null;
        this.lore = null;
        this.dmg = dmg;
        this.type = type;
    }

    CustomItem(int dmg) {
        this.name = null;
        this.lore = null;
        this.dmg = dmg;
        this.type = Material.DIAMOND_HOE;
    }

    public static CustomItem getCustomItem(ItemStack item) {
        for (CustomItem cItem : CustomItem.values()) {
            if (item.getType() == cItem.getType() && item.getDurability() == cItem.getDmg()) {
                ItemMeta meta = item.getItemMeta();
                if (meta == null) {
                    if (cItem.getName() == null && cItem.getLore() == null) {
                        return cItem;
                    }
                    continue;
                }
                if (meta.getDisplayName().equals(cItem.getName()) && meta.getLore().equals(cItem.getLore())) {
                    return cItem;
                }
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getDmg() {
        return dmg;
    }

    public Material getType() {
        return type;
    }

    public ItemStack create(int number) {
        ItemStack item = new ItemStack(type, number, (short) dmg);
        ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.setDisplayName(name);
        }
        if (lore != null) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }
}
