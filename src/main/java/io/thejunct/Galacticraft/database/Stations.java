/*
 * Copyright (c) 2016 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by david on 10/22.
 *
 * @author david
 */
@SuppressWarnings({"unchecked", "MismatchedQueryAndUpdateOfCollection", "SqlNoDataSourceInspection", "SqlResolve"})
public class Stations extends Database {

    private Map<String, String> owner = new HashMap<>();
    private Map<String, String> respawn = new HashMap<>();
    private Map<String, String> members = new HashMap<>();
    private Map<String, String> invited = new HashMap<>();
    private Map<String, String> allies = new HashMap<>();
    private Map<String, String> allyRequests = new HashMap<>();
    private Map<String, String> enemies = new HashMap<>();

    public Stations() {
        super("SQLite", "stations", "(" +
                "`id` varchar(32) NOT NULL," +
                "`owner` varchar(32) NOT NULL," +
                "`respawn` varchar(32) NOT NULL," +
                "`members` varchar(32) NOT NULL," +
                "`invited` varchar(32) NOT NULL" +
                "`allies` varchar(32) NOT NULL," +
                "`allyRequests` varchar(32) NOT NULL," +
                "`enemies` varchar(32) NOT NULL", "id");
    }

    public boolean createDataNotExist(String id) {

        if (getData("id", id, "id") != null) {
            return true;
        }

        Connection conn = getSQLConnection();
        try (
                PreparedStatement ps = conn.prepareStatement("INSERT INTO " + dbName +
                        " (id, owner, respawn, members, invited, allies, allyRequests, enemies) " +
                        "VALUES (?,?,?,?,?,?,?,?);")
        ) {
            setValues(ps, id, "", "", "", "", "", "", "");
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void remove(String id) {
        if (getData("id", id, "id") == null) {
            return;
        }

        Connection conn = getSQLConnection();
        try (
                PreparedStatement ps = conn.prepareStatement("DELETE FROM " + dbName + " WHERE id = ?;")
        ) {
            setValues(ps, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Map> getData() {
        Map<String, Map> data = new HashMap<>();
        data.put("owner", owner);
        data.put("respawn", respawn);
        data.put("members", members);
        data.put("invited", invited);
        data.put("allies", allies);
        data.put("allyRequests", allyRequests);
        data.put("enemies", enemies);
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

    private void setData(String uuid, String label, Set<String> labelVal) {
        setData(uuid, label, gc.getDictionary().combine(";", labelVal));
    }

    private void setData(String uuid, String label, Object labelVal) {
        if (getData().containsKey(label)) {
            getData().get(label).put(uuid, labelVal);
        }
        setData("uuid", uuid, label, labelVal);
    }

    public String getOwner(String id) {
        return (String) getData(id, "lastname");
    }

    public void setOwner(String id, String uuid) {
        setData(id, "lastname", uuid);
    }

    public Location getRespawn(String id) {
        String coords[] = ((String) getData(id, "respawn")).split(";");
        return new Location(Bukkit.getWorld(id), Double.valueOf(coords[0]), Double.valueOf(coords[1]), Double.valueOf(coords[2]), Float.valueOf(coords[3]), Float.valueOf(coords[4]));
    }

    public void setRespawn(String id, Location loc) {
        String coords = gc.getDictionary().combine(";", Arrays.asList(String.valueOf(loc.getX()), String.valueOf(loc.getY()), String.valueOf(loc.getZ()), String.valueOf(loc.getYaw()), String.valueOf(loc.getPitch())));
        setData(id, "lastname", coords);
    }

    public Set getMembers(String id) {
        return setFromString((String) getData(id, "members"));
    }

    public void addMember(String id, String uuid) {
        Set<String> members = getMembers(id);
        members.add(uuid);
        setData(id, "members", members);
    }

    public void removeMember(String id, String uuid) {
        Set<String> members = getMembers(id);
        members.remove(uuid);
        if (members.isEmpty()) {
            remove(id);
        } else {
            setData(id, "members", members);
        }
    }

    public Set getInvited(String id) {
        return setFromString((String) getData(id, "invited"));
    }

    public void addInvited(String id, String uuid) {
        Set<String> invited = getInvited(id);
        invited.add(uuid);
        setData(id, "invited", invited);
    }

    public void removeInvited(String id, String uuid) {
        Set<String> invited = getInvited(id);
        invited.remove(uuid);
        setData(id, "invited", invited);
    }

    public Set getAllies(String id) {
        return setFromString((String) getData(id, "allies"));
    }

    public void addAlly(String id, String ally) {
        Set<String> allies = getAllies(id);
        allies.add(ally);
        setData(id, "allies", allies);
    }

    public void removeAlly(String id, String ally) {
        Set<String> allies = getAllies(id);
        allies.remove(ally);
        setData(id, "allies", allies);
    }

    public Set getAllyRequests(String id) {
        return setFromString((String) getData(id, "allyRequests"));
    }

    public void addAllyRequest(String id, String requester) {
        Set<String> allyRequests = getAllyRequests(id);
        allyRequests.add(requester);
        setData(id, "allyRequests", allyRequests);
    }

    public void removeAllyRequest(String id, String requester) {
        Set<String> allyRequests = getAllyRequests(id);
        allyRequests.remove(requester);
        setData(id, "allyRequests", allyRequests);
    }

    public Set getEnemies(String id) {
        return setFromString((String) getData(id, "enemies"));
    }

    public void addEnemy(String id, String enemy) {
        Set<String> enemies = getEnemies(id);
        enemies.add(enemy);
        setData(id, "enemies", enemies);
    }

    public void removeEnemy(String id, String enemy) {
        Set<String> enemies = getEnemies(id);
        enemies.remove(enemy);
        setData(id, "enemies", enemies);
    }
}

