/*
 *
 *  Copyright (c) 2026 Ruben_Artz and Artz Studio. All rights reserved.
 *
 *  This code is proprietary software. It is strictly prohibited to
 *  copy, modify, distribute, or use this code for any purpose
 *  without the express written permission of the owner.
 *
 *  Project: Deluxe Void World
 *
 */

package artzstudio.dev.deluxevoid.database;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.database.utils.CacheMethod;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MysqlMethod implements CacheMethod {
    private static HikariDataSource ds;
    private final HikariConfig hikariConfig = new HikariConfig();

    @Override
    public Connection getConnection() throws SQLException {
        if (ds == null) return null;
        return ds.getConnection();
    }

    @Override
    public void init(DeluxeVoidWorld plugin, Cache cacheInstance) {
        String host = plugin.getConfigYaml().getString("ADMIN-CONFIG.DATABASE.HOST");
        String username = plugin.getConfigYaml().getString("ADMIN-CONFIG.DATABASE.USER");
        String password = plugin.getConfigYaml().getString("ADMIN-CONFIG.DATABASE.PASSWORD");
        String database = plugin.getConfigYaml().getString("ADMIN-CONFIG.DATABASE.DATABASE");
        int minCount = plugin.getConfigYaml().getInt("ADMIN-CONFIG.DATABASE.MIN_CONNECTIONS");
        int maxCount = plugin.getConfigYaml().getInt("ADMIN-CONFIG.DATABASE.MAX_CONNECTIONS");

        String url = "jdbc:mysql://" + host + "/" + database + "?" + setParameters(plugin);

        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maxCount);
        hikariConfig.setMinimumIdle(minCount);
        hikariConfig.setMaxLifetime(60000);
        hikariConfig.setIdleTimeout(30000);

        ds = new HikariDataSource(hikariConfig);
        ds.setLeakDetectionThreshold(60 * 1000);

        try (Connection conn = getConnection()) {

            if (conn.isValid(5000)) {
                plugin.sendConsole(plugin.getPrefix() + plugin.getFileTranslations().getString("MESSAGES_MYSQL_CONNECTED"));
            }
        } catch (SQLException e) {
            plugin.sendConsole(plugin.getPrefix() + plugin.getFileTranslations().getString("MESSAGE_MYSQL_NOT_CONNECTED"));
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    private String setParameters(DeluxeVoidWorld plugin) {
        List<String> parameterList = plugin.getConfigYaml().getStringList("ADMIN-CONFIG.DATABASE.PARAMETERS");

        StringBuilder stringBuilder = new StringBuilder();

        for (String parameter : parameterList) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append("&");
            }
            stringBuilder.append(parameter);
        }

        return stringBuilder.toString();
    }


    @Override
    public void close(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public void shutdown() {
        ds.close();
    }
}