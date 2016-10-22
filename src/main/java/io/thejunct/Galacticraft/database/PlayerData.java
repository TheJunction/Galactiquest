/*
 * Copyright (c) 2016 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft.database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by david on 10/22.
 *
 * @author david
 */
@SuppressWarnings({"unchecked", "MismatchedQueryAndUpdateOfCollection", "SqlNoDataSourceInspection", "SqlResolve"})
public class PlayerData extends Database {

    private Map<String, String> lastname = new HashMap<>();
    private Map<String, String> station = new HashMap<>();

    public PlayerData() {
        super("SQLite", "playerdata", "(" +
                "`uuid` varchar(32) NOT NULL," +
                "`lastname` varchar(32) NOT NULL," +
                "`station` varchar(32) NOT NULL", "uuid");
    }

    public boolean createDataNotExist(String uuid) {

        if (getData("uuid", uuid, "uuid") != null) {
            return true;
        }

        Player p = Bukkit.getPlayer(UUID.fromString(uuid));

        Connection conn = getSQLConnection();
        try (
                PreparedStatement ps = conn.prepareStatement("INSERT INTO " + dbName +
                        " (uuid, lastname, station) " +
                        "VALUES (?,?,?);")
        ) {
            setValues(ps, uuid, p != null ? p.getName() : "", "");
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Map<String, Map> getData() {
        Map<String, Map> data = new HashMap<>();
        data.put("lastname", lastname);
        data.put("station", station);
        return data;
    }

    private Object getData(String uuid, String label) {
        if (getData().containsKey(label)) {
            if (!getData().get(label).containsKey(uuid)) {
                getData().get(label).put(uuid, getData("uuid", uuid, label));
            }
            return getData().get(label).get(uuid);
        }
        return getData("uuid", uuid, label);
    }

    private void setData(String uuid, String label, Object labelVal) {
        if (getData().containsKey(label)) {
            getData().get(label).put(uuid, labelVal);
        }
        setData("uuid", uuid, label, labelVal);
    }

    public String getName(String uuid) {
        return (String) getData(uuid, "lastname");
    }

    public void setName(String uuid, String name) {
        setData(uuid, "lastname", name);
    }

    public String getStation(String uuid) {
        return (String) getData(uuid, "station");
    }

    public void setStation(String uuid, String name) {
        setData(uuid, "station", name);
    }
}
