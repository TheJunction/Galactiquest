/*
 * Copyright (c) 2016 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft.database;

import io.thejunct.Galacticraft.Galacticraft;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by david on 10/22.
 *
 * @author david
 */
@SuppressWarnings({"WeakerAccess", "SqlNoDataSourceInspection", "unused"})
public abstract class Database {
    static Galacticraft gc;
    String dbName;
    private String dbType;
    private String primary;
    private Connection connection;
    private int uses = 0;

    public Database(String dbType, String dbName, String setupSql, String primary) {
        gc = Galacticraft.getInstance();
        this.dbType = dbType;
        this.dbName = dbName;
        this.primary = primary;
        load(setupSql, primary);
    }

    static Set<String> setFromString(String input) {
        Set<String> set = new HashSet<>();
        if (input != null && !input.equals("")) {
            Collections.addAll(set, input.split(";"));
        }
        return set;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    Connection getSQLConnection() {
        if (dbType.equalsIgnoreCase("SQLite")) {
            File dbFile = new File(gc.getDataFolder(), dbName + ".db");
            if (!dbFile.exists()) {
                try {
                    dbFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (uses > 250) {
                    connection.close();
                }

                if (connection != null && !connection.isClosed()) {
                    uses++;
                    return connection;
                }

                File sqliteLib = new File(gc.getLibDir(), "sqlite-jdbc-3.8.11.2.jar");

                if (!sqliteLib.exists()) {
                    gc.getLogger().log(Level.INFO, "Downloading SQLite JDBC library...");
                    String dlLink = "https://bitbucket.org/xerial/sqlite-jdbc/downloads/sqlite-jdbc-3.8.11.2.jar";
                    URLConnection con;
                    try {
                        URL url = new URL(dlLink);
                        con = url.openConnection();
                    } catch (IOException e) {
                        gc.getLogger().log(Level.SEVERE, "Invalid SQLite download link. Please contact plugin author.");
                        return null;
                    }

                    try (
                            InputStream in = con.getInputStream();
                            FileOutputStream out = new FileOutputStream(sqliteLib)
                    ) {
                        byte[] buffer = new byte[1024];
                        int size;
                        while ((size = in.read(buffer)) != -1) {
                            out.write(buffer, 0, size);
                        }
                    } catch (IOException e) {
                        gc.getLogger().log(Level.WARNING, "Failed to download update, please download it manually from https://bitbucket.org/xerial/sqlite-jdbc/downloads/sqlite-jdbc-3.8.11.2.jar and put it in the /plugins/BungeeEssentials/lib folder.");
                        gc.getLogger().log(Level.WARNING, "Error message: ");
                        e.printStackTrace();
                        return null;
                    }
                }

                URLClassLoader loader = new URLClassLoader(new URL[]{sqliteLib.toURI().toURL()});
                Method m = DriverManager.class.getDeclaredMethod("getConnection", String.class, Properties.class, Class.class);
                m.setAccessible(true);

                connection = (Connection) m.invoke(null, "jdbc:sqlite:" + dbFile.getPath(), new Properties(), Class.forName("org.sqlite.JDBC", true, loader));

                uses = 0;
                return connection;

            } catch (ClassNotFoundException e) {
                gc.getLogger().log(Level.SEVERE, "You need the SQLite JDBC library. Download it from https://bitbucket.org/xerial/sqlite-jdbc/downloads/sqlite-jdbc-3.8.11.2.jar and put it in the /plugins/BungeeEssentials/lib folder.");
            } catch (Exception e) {
                gc.getLogger().log(Level.SEVERE, "Exception on SQLite initialize", e);
            }
        } else if (dbType.equalsIgnoreCase("MySQL")) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "david", "DavidShen");
            } catch (ClassNotFoundException e) {
                gc.getLogger().log(Level.SEVERE, "Unable to load the MySQL Library.");
            } catch (Exception e) {
                gc.getLogger().log(Level.SEVERE, "Exception on MySQL initialize", e);
            }
        }

        return null;
    }

    public abstract boolean createDataNotExist(String keyVal);

    public List<Object> listAllData(String label) {
        List<Object> datas = new ArrayList<>();

        Connection conn = getSQLConnection();
        try (
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + dbName + ";");
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                datas.add(rs.getObject(label));
            }

            if (datas.size() > 0) {
                return datas;
            }

        } catch (SQLException e) {
            gc.getLogger().log(Level.SEVERE, "Couldn't execute SQL statement: ", e);
        }
        return null;
    }

    public List<Object> getDataMultiple(String key, String keyVal, String label) {
        //TODO: Cache queries into memory
        List<Object> datas = new ArrayList<>();

        Connection conn = getSQLConnection();
        try (
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + dbName + " WHERE " + key + " = ?;")
        ) {
            ps.setObject(1, keyVal);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString(key).equals(keyVal)) {
                    datas.add(rs.getObject(label));
                }
            }

            if (datas.size() > 0) {
                return datas;
            }

        } catch (SQLException e) {
            gc.getLogger().log(Level.SEVERE, "Couldn't execute SQL statement: ", e);
        }
        return null;
    }

    public Object getData(String key, String keyVal, String label) {
        List<Object> datas = getDataMultiple(key, keyVal, label);
        if (datas != null) {
            return datas.get(0);
        }
        return null;
    }

    public void setData(String key, String keyVal, String label, Object labelVal) {
        if (key.equals(primary) && !createDataNotExist(keyVal)) {
            return;
        }

        Connection conn = getSQLConnection();
        try (
                PreparedStatement ps = conn.prepareStatement("UPDATE " + dbName + " SET " + label + " = ? WHERE " + key + " = ?;")
        ) {
            ps.setObject(1, labelVal);
            ps.setObject(2, keyVal);
            ps.executeUpdate();
        } catch (SQLException e) {
            gc.getLogger().log(Level.SEVERE, "Couldn't execute SQL statement: ", e);
        }
    }

    void setValues(PreparedStatement ps, Object... values) throws SQLException {
        setValues(1, ps, values);
    }

    void setValues(int start, PreparedStatement ps, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            ps.setObject(i + start, values[i]);
        }
    }

    private void load(String setupSql, String primary) {
        connection = getSQLConnection();

        try (Statement s = connection.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS " + dbName + " " + setupSql + ",PRIMARY KEY (`" + primary + "`));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
