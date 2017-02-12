/*
 * Copyright (c) 2017 The Junction Network. All Rights Reserved.
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
class CustomItem {
    static int dHoeIter = Material.DIAMOND_HOE.getMaxDurability();

    enum Item {
        SPACESHIP(),
        SPACESHIP_HEAD(),
        SPACESHIP_CONTROL_CONTROLS(ChatColor.BLUE + "Ship Controls", ChatColor.BLUE + "Sprint to move forward", ChatColor.BLUE + "Sneak to slow down", ChatColor.BLUE + "Right click to fire a laser");

        private String name;
        private int dmg;
        private Material type;
        private List<String> lore;

        Item(String name, int dmg, Material type, String... lore) {
            this.name = name;
            this.dmg = dmg;
            this.type = type;
            this.lore = Arrays.asList(lore);
        }

        Item(String name, String... lore) {
            this(name, dHoeIter--, Material.DIAMOND_HOE, lore);
        }

        Item() {
            this(null, dHoeIter--, Material.DIAMOND_HOE);
        }

        public static Item getCustomItem(ItemStack item) {
            for (Item cItem : Item.values()) {
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

    enum Block {

    }
}
