package ruben_artz.world.database.utils;

import ruben_artz.world.database.Cache;
import ruben_artz.world.main.VOMain;

import java.sql.Connection;
import java.sql.SQLException;

public interface CacheMethod {
    Connection getConnection() throws SQLException;
    void init(VOMain plugin, Cache cacheInstance);
    void close(Connection connection) throws SQLException;
    void shutdown();
}