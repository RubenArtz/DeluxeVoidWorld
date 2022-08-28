package ruben_artz.world.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import ruben_artz.world.main.VOMain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
    private static HikariDataSource ds;
    private final HikariConfig hikariConfig = new HikariConfig();
    public static final String users = "%%__USER__%%";

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public void init(VOMain plugin) {
        String host = plugin.getConfig().getString("ADMIN-CONFIG.MYSQL.HOST");
        String username = plugin.getConfig().getString("ADMIN-CONFIG.MYSQL.USER");
        String password = plugin.getConfig().getString("ADMIN-CONFIG.MYSQL.PASSWORD");
        String database = plugin.getConfig().getString("ADMIN-CONFIG.MYSQL.DATABASE");
        boolean useSSL = plugin.getConfig().getBoolean("ADMIN-CONFIG.MYSQL.USE_SSL");
        boolean allowPublicKeyRetrieval = plugin.getConfig().getBoolean("ADMIN-CONFIG.MYSQL.ALLOW_PUBLIC_KEY_RETRIEVAL");
        int minCount = plugin.getConfig().getInt("ADMIN-CONFIG.MYSQL.MIN_CONNECTIONS");
        int maxCount = plugin.getConfig().getInt("ADMIN-CONFIG.MYSQL.MAX_CONNECTIONS");

        String url = "jdbc:mysql://" + host + "/" + database + "?useSSL=" + useSSL + "&allowPublicKeyRetrieval=" + allowPublicKeyRetrieval + "&characterEncoding=utf8&useInformationSchema=true";
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maxCount);
        hikariConfig.setMinimumIdle(minCount);

        ds = new HikariDataSource(hikariConfig);
        ds.setLeakDetectionThreshold(25 * 1000);

        try (Connection connection = ds.getConnection()) {
            Statement statement = connection.createStatement();
            String prepared = "CREATE TABLE IF NOT EXISTS " + plugin.table +" " +
                    "(UUID VARCHAR(200) NOT NULL, " +
                    "TELEPORT VARCHAR(200) NOT NULL, " +
                    "JUMP VARCHAR(200) NOT NULL, " +
                    "LIGHTNING VARCHAR(200) NOT NULL, " +
                    "PARTICLES VARCHAR(200) NOT NULL, PRIMARY KEY (UUID))";
            statement.executeUpdate(prepared);
            plugin.sendConsole(plugin.prefix + plugin.getFileTranslations().getString("MESSAGES_MYSQL_CONNECTED"));
        } catch (SQLException exception) {
            plugin.sendConsole(plugin.prefix + plugin.getFileTranslations().getString("MESSAGE_MYSQL_NOT_CONNECTED"));
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    public static void close(Connection connection) throws SQLException {
        connection.close();
    }

    public static void shutdown() {
        ds.close();
    }
}