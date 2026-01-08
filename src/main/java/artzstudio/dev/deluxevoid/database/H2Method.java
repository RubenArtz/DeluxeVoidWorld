package artzstudio.dev.deluxevoid.database;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.database.utils.CacheMethod;
import artzstudio.dev.deluxevoid.database.utils.UnClosableConnection;
import org.h2.jdbc.JdbcConnection;
import org.h2.message.DbException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class H2Method implements CacheMethod {
    private Connection conn;
    private DeluxeVoidWorld plugin;
    private Cache cacheInstance;

    @Override
    public Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                plugin.getLogger().warning("H2 connection is dead or null, making a new one");
                init(plugin, cacheInstance);
            }

            if (conn == null) {
                throw new SQLException("Could not establish H2 connection even after retry.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new UnClosableConnection(conn);
    }

    @Override
    public void init(DeluxeVoidWorld plugin, Cache cacheInstance) {
        this.plugin = plugin;
        this.cacheInstance = cacheInstance;

        try {
            Field field = DbException.class.getDeclaredField("MESSAGES");
            field.setAccessible(true);
            Properties props = (Properties) field.get(null);
            if (props == null) props = new Properties();

            var stream = getClass().getResourceAsStream("/h2_messages.prop");
            if (stream != null) {
                props.load(stream);
            }
        } catch (IllegalAccessException | NoSuchFieldException | IOException ignored) {
        }

        File junkFile = new File(plugin.getDataFolder(), "cache.trace.db");
        if (junkFile.exists()) {
            if (!junkFile.delete()) {
                plugin.getLogger().warning("Failed to delete junk trace file!");
            }
        }

        String dbPath = plugin.getDataFolder().getAbsolutePath() + File.separator + "cache" + File.separator + "cache";
        String url = "jdbc:h2:file:" + dbPath + ";TRACE_LEVEL_FILE=0";

        try {
            conn = new JdbcConnection(url, new Properties(), null, null, false);
            printSuccessMessage();

        } catch (SQLException e) {
            plugin.getLogger().warning("Detected database error (Possible relocation change or corruption).");
            plugin.getLogger().warning("Error: " + e.getMessage());
            plugin.getLogger().warning("Attempting to RESET cache database...");

            deleteCacheFolder();

            try {
                conn = new JdbcConnection(url, new Properties(), null, null, false);
                plugin.getLogger().info("Database reset successful! Connection established.");
                printSuccessMessage();
            } catch (SQLException fatalError) {
                plugin.getLogger().severe("FATAL: Could not create H2 database even after delete. Plugin functionality will be limited.");

                conn = null;

                throw new RuntimeException(fatalError);
            }
        }
    }

    private void printSuccessMessage() {
        if (plugin != null) {
            plugin.sendConsole(plugin.getPrefix() + plugin.getFileTranslations().getString("MESSAGE_USING_SQLITE"));
        }
    }

    private void deleteCacheFolder() {
        File cacheFolder = new File(plugin.getDataFolder(), "cache");
        if (cacheFolder.exists() && cacheFolder.isDirectory()) {
            File[] files = cacheFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        plugin.getLogger().warning("Could not delete file: " + file.getName());
                    }
                }
            }
            if (!cacheFolder.delete()) {
                plugin.getLogger().warning("Could not delete cache folder.");
            }
        }
    }

    @Override
    public void close(Connection connection) throws SQLException {
    }

    @Override
    public void shutdown() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}