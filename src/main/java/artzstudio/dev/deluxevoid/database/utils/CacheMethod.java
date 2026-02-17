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