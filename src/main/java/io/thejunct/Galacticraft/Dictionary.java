/*
 * Copyright (c) 2016 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.Set;

/**
 * Created by david on 10/22.
 *
 * @author david
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Dictionary {

    /**
     * Converts color codes to colors.
     *
     * @param str The string to convert.
     * @return The converted string.
     */
    public String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    /**
     * Combines all strings in an array to a custom delimited string.
     *
     * @param omit  The position of a string to omit
     * @param delim The delimiter.
     * @param array The array to combine.
     * @return The combined array.
     */
    public String combine(int omit, String delim, String[] array) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i != omit) {
                builder.append(array[i]);
                if (i != array.length - 1) {
                    builder.append(delim);
                }
            }
        }
        return builder.toString();
    }

    public String combine(String delim, String[] array) {
        return combine(-1, delim, array);
    }

    public String combine(String[] array) {
        return combine(" ", array);
    }

    public String combine(String delim, List<String> array) {
        return combine(delim, array.toArray(new String[array.size()]));
    }

    public String combine(String delim, Set<String> array) {
        return combine(delim, array.toArray(new String[array.size()]));
    }

    /**
     * Combines all strings except 1 in an array to a space-delimited string.
     *
     * @param omit  The position of a string to omit
     * @param array The array to combine.
     * @return The combined array.
     */
    public String combine(int omit, String[] array) {
        StringBuilder builder = new StringBuilder();
        for (String string : array) {
            if (!string.equals(array[omit])) {
                builder.append(string);
                if (!string.equals(array[array.length - 1])) {
                    builder.append(" ");
                }
            }
        }
        return builder.toString();
    }

    /**
     * Capitalizes the first letter of the given string.
     *
     * @param input The input string.
     * @return The capitalized string.
     */
    public String capitalizeFirst(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
