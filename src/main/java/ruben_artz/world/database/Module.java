package ruben_artz.world.database;

import org.bukkit.entity.Player;
import ruben_artz.world.main.VOMain;
import ruben_artz.world.world.VOManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Module {
    private static final VOMain plugin = VOMain.getPlugin(VOMain.class);
    private enum DEFAULT {
        TELEPORT, JUMP, LIGHTNING, PARTICLES
    }

    public static void addInformation(UUID uuid) {
        String prepared = "INSERT INTO "+plugin.getTable()+" (UUID, TELEPORT, JUMP, LIGHTNING, PARTICLES) VALUES (?, ?, ?, ?, ?);";
        try {
            if (VOManager.isMySQL()) {
                try (Connection connection = MySQL.getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(prepared);
                    statement.setString(1, uuid.toString());
                    switch (DEFAULT.valueOf(plugin.getConfig().getString("ON_VOID_TP.SETTINGS.DEFAULT_OPTION"))) {
                        case TELEPORT: {
                            setDefault(statement, true, false, false, false);
                            break;
                        }
                        case JUMP: {
                            setDefault(statement, false, true, false, false);
                            break;
                        }
                        case LIGHTNING: {
                            setDefault(statement, false, false, true, false);
                            break;
                        }
                        case PARTICLES: {
                            setDefault(statement, false, false, false, true);
                            break;
                        }
                        default: {
                            setDefault(statement, true, false, false, false);
                        }
                    }
                    statement.executeUpdate();
                    MySQL.close(connection);
                }
            } else {
                SQLite.checkConnection();
                try (Connection connection = SQLite.getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(prepared);
                    statement.setString(1, uuid.toString());
                    switch (DEFAULT.valueOf(plugin.getConfig().getString("ON_VOID_TP.SETTINGS.DEFAULT_OPTION"))) {
                        case TELEPORT: {
                            setDefault(statement, true, false, false, false);
                            break;
                        }
                        case JUMP: {
                            setDefault(statement, false, true, false, false);
                            break;
                        }
                        case LIGHTNING: {
                            setDefault(statement, false, false, true, false);
                            break;
                        }
                        case PARTICLES: {
                            setDefault(statement, false, false, false, true);
                            break;
                        }
                        default: {
                            setDefault(statement, true, false, false, false);
                        }
                    }
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean getVerify(UUID uuid, String column) {
        String prepared = "SELECT * FROM "+plugin.getTable()+" WHERE (UUID=?)";
        try {
            if (VOManager.isMySQL()) {
                try (Connection connection = MySQL.getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(prepared);
                    statement.setString(1, uuid.toString());
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) return resultSet.getString(column).equals("true");
                    MySQL.close(connection);
                }
            } else {
                SQLite.checkConnection();
                try (Connection connection = SQLite.getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(prepared);
                    statement.setString(1, uuid.toString());
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) return resultSet.getString(column).equals("true");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static void setUpdate(UUID uuid, String column, boolean bool) {
        String prepared = "UPDATE "+plugin.getTable()+" SET "+column+"=? WHERE (UUID=?)";
        try {
            if (VOManager.isMySQL()) {
                try (Connection connection = MySQL.getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(prepared);
                    statement.setString(1, String.valueOf(bool));
                    statement.setString(2, uuid.toString());
                    statement.executeUpdate();
                    MySQL.close(connection);
                }
            } else {
                SQLite.checkConnection();
                try (Connection connection = SQLite.getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(prepared);
                    statement.setString(1, String.valueOf(bool));
                    statement.setString(2, uuid.toString());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean ifNotExists(UUID uuid) {
        String prepared = "SELECT* FROM "+plugin.getTable()+" WHERE (UUID=?)";
        try {
            if (VOManager.isMySQL()) {
                try (Connection connection = MySQL.getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(prepared);
                    statement.setString(1, uuid.toString());
                    ResultSet resultSet = statement.executeQuery();
                    if ( resultSet.next()) return false;
                    MySQL.close(connection);
                }
            } else {
                SQLite.checkConnection();
                try (Connection connection = SQLite.getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(prepared);
                    statement.setString(1, uuid.toString());
                    ResultSet resultSet = statement.executeQuery();
                    if ( resultSet.next()) return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static void updateBool(Player player, boolean teleport, boolean jump, boolean lightning, boolean particles) {
        VOManager.syncRunTask(() -> {
            try {
                setUpdate(player.getUniqueId(), "TELEPORT", teleport);
                setUpdate(player.getUniqueId(), "JUMP", jump);
                setUpdate(player.getUniqueId(), "LIGHTNING", lightning);
                setUpdate(player.getUniqueId(), "PARTICLES", particles);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            updatePlayer(player);
        });
    }

    public static void updatePlayer(Player player) {
        if (Module.getVerify(player.getUniqueId(), "TELEPORT")) {
            plugin.getIgnoreTeleportation().remove(player.getUniqueId());
        } else {
            plugin.getIgnoreTeleportation().add(player.getUniqueId());
        }
        if (Module.getVerify(player.getUniqueId(), "JUMP")) {
            plugin.getIgnoreJumping().remove(player.getUniqueId());
        } else {
            plugin.getIgnoreJumping().add(player.getUniqueId());
        }
        if (Module.getVerify(player.getUniqueId(), "LIGHTNING")) {
            plugin.getIgnoreLightning().remove(player.getUniqueId());
        } else {
            plugin.getIgnoreLightning().add(player.getUniqueId());
        }
        if (Module.getVerify(player.getUniqueId(), "PARTICLES")) {
            plugin.getIgnoreParticles().remove(player.getUniqueId());
        } else {
            plugin.getIgnoreParticles().add(player.getUniqueId());
        }
    }

    private static void setDefault(final PreparedStatement statement, boolean teleport, boolean jump, boolean lightning, boolean particles) {
        try {
            statement.setString(2, String.valueOf(teleport));
            statement.setString(3, String.valueOf(jump));
            statement.setString(4, String.valueOf(lightning));
            statement.setString(5, String.valueOf(particles));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
