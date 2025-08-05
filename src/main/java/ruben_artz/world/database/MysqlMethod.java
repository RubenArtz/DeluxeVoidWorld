package ruben_artz.world.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.database.utils.CacheMethod;

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
        String host = plugin.getConfig().getString("ADMIN-CONFIG.DATABASE.HOST");
        String username = plugin.getConfig().getString("ADMIN-CONFIG.DATABASE.USER");
        String password = plugin.getConfig().getString("ADMIN-CONFIG.DATABASE.PASSWORD");
        String database = plugin.getConfig().getString("ADMIN-CONFIG.DATABASE.DATABASE");
        int minCount = plugin.getConfig().getInt("ADMIN-CONFIG.DATABASE.MIN_CONNECTIONS");
        int maxCount = plugin.getConfig().getInt("ADMIN-CONFIG.DATABASE.MAX_CONNECTIONS");

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
        List<String> parameterList = plugin.getConfig().getStringList("ADMIN-CONFIG.DATABASE.PARAMETERS");

        StringBuilder stringBuilder = new StringBuilder();

        for (String parameter : parameterList) {
            if (stringBuilder.length() > 0) {
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