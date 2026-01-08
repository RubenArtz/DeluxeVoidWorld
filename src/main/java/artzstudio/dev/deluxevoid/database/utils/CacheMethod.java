package artzstudio.dev.deluxevoid.database.utils;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import artzstudio.dev.deluxevoid.database.Cache;

import java.sql.Connection;
import java.sql.SQLException;

public interface CacheMethod {
    Connection getConnection() throws SQLException;

    void init(DeluxeVoidWorld plugin, Cache cacheInstance);

    void close(Connection connection) throws SQLException;

    void shutdown();
}