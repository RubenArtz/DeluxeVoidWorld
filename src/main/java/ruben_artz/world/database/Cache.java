package ruben_artz.world.database;

import lombok.Getter;
import org.bukkit.entity.Player;
import ruben_artz.world.database.utils.CacheMethod;
import ruben_artz.world.DeluxeVoidWorld;
import ruben_artz.world.utils.ProjectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Cache {
    private final DeluxeVoidWorld plugin = DeluxeVoidWorld.getPlugin(DeluxeVoidWorld.class);

    @Getter CacheMethod method;

    private final String CREATE_TABLE_MYSQL = "CREATE TABLE IF NOT EXISTS " + plugin.getTable() +" " +
            "(UUID VARCHAR(200) NOT NULL, " +
            "TELEPORT VARCHAR(200) NOT NULL, " +
            "JUMP VARCHAR(200) NOT NULL, " +
            "LIGHTNING VARCHAR(200) NOT NULL, " +
            "PARTICLES VARCHAR(200) NOT NULL, PRIMARY KEY (UUID))";
    private final String CREATE_TABLE_H2 = "CREATE TABLE IF NOT EXISTS "+plugin.getTable()+" (UUID VARCHAR(200), " +
            "TELEPORT VARCHAR(200), " +
            "JUMP VARCHAR(200), " +
            "LIGHTNING VARCHAR(200), " +
            "PARTICLES VARCHAR(200))";
    private final String ADD_INFORMATION = "INSERT INTO "+plugin.getTable()+" (UUID, TELEPORT, JUMP, LIGHTNING, PARTICLES) VALUES (?, ?, ?, ?, ?);";
    private final String GET_VERIFY = "SELECT * FROM "+plugin.getTable()+" WHERE (UUID=?)";
    private final String SET_UPDATE = "UPDATE "+plugin.getTable()+" SET %0=? WHERE (UUID=?)";
    private final String NOT_EXISTS = "SELECT* FROM "+plugin.getTable()+" WHERE (UUID=?)";
    private enum DEFAULT {
        TELEPORT, JUMP, LIGHTNING, PARTICLES
    }

    public Cache() {
        if (ProjectUtils.isMySQL()) {
            method = new MysqlMethod();
        } else {
            method = new H2Method();
        }

        method.init(plugin, this);

        createTable();
    }

    private void createTable() {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = method.getConnection();
            ps = connection.prepareStatement(ProjectUtils.isMySQL() ? CREATE_TABLE_MYSQL : CREATE_TABLE_H2);

            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.sendConsole(plugin.getPrefix() + "&cWe have problems connecting to the database.");
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                plugin.sendConsole(plugin.getPrefix() + "&cWe have problems connecting to the database.");
            }
        }
    }

    public void addInformation(UUID uuid) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = method.getConnection();
            ps = connection.prepareStatement(ADD_INFORMATION);

            ps.setString(1, uuid.toString());

            switch (DEFAULT.valueOf(plugin.getConfig().getString("ON_VOID_TP.SETTINGS.DEFAULT_OPTION"))) {
                case TELEPORT: {
                    setDefault(ps, true, false, false, false);
                    break;
                }
                case JUMP: {
                    setDefault(ps, false, true, false, false);
                    break;
                }
                case LIGHTNING: {
                    setDefault(ps, false, false, true, false);
                    break;
                }
                case PARTICLES: {
                    setDefault(ps, false, false, false, true);
                    break;
                }
                default: {
                    setDefault(ps, true, false, false, false);
                }
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.sendConsole(plugin.getPrefix() + "&cWe have problems connecting to the database.");
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                plugin.sendConsole(plugin.getPrefix() + "&cWe have problems connecting to the database.");
            }
        }
    }

    public boolean getVerify(UUID uuid, String column) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = method.getConnection();
            ps = connection.prepareStatement(GET_VERIFY);

            ps.setString(1, uuid.toString());

            resultSet = ps.executeQuery();

            if (resultSet.next()) return resultSet.getString(column).equals("true");
        } catch (SQLException e) {
            plugin.sendConsole(plugin.getPrefix() + "&cWe have problems connecting to the database.");
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                plugin.sendConsole(plugin.getPrefix() + "&cWe have problems connecting to the database.");
            }
        }
        return false;
    }

    public void setUpdate(UUID uuid, String column, boolean bool) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = method.getConnection();
            ps = connection.prepareStatement(SET_UPDATE
                    .replace("%0", column));

            ps.setString(1, String.valueOf(bool));
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.sendConsole(plugin.getPrefix() + "&cWe have problems connecting to the database.");
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                plugin.sendConsole(plugin.getPrefix() + "&cWe have problems connecting to the database.");
            }
        }
    }

    public boolean ifNotExists(UUID uuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = method.getConnection();
            ps = connection.prepareStatement(NOT_EXISTS);

            ps.setString(1, uuid.toString());

            resultSet = ps.executeQuery();

            if (resultSet.next()) return false;
        } catch (SQLException e) {
            plugin.sendConsole(plugin.getPrefix() + "&cWe have problems connecting to the database.");
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                plugin.sendConsole(plugin.getPrefix() + "&cWe have problems connecting to the database.");
            }
        }
        return true;
    }

    public void updateBool(Player player, boolean teleport, boolean jump, boolean lightning, boolean particles) {
        CompletableFuture.runAsync(() -> {
            setUpdate(player.getUniqueId(), "TELEPORT", teleport);
            setUpdate(player.getUniqueId(), "JUMP", jump);
            setUpdate(player.getUniqueId(), "LIGHTNING", lightning);
            setUpdate(player.getUniqueId(), "PARTICLES", particles);
            
            updatePlayer(player);
        });
    }

    public void updatePlayer(Player player) {
        if (getVerify(player.getUniqueId(), "TELEPORT")) {
            plugin.getIgnoreTeleportation().remove(player.getUniqueId());
        } else {
            plugin.getIgnoreTeleportation().add(player.getUniqueId());
        }
        if (getVerify(player.getUniqueId(), "JUMP")) {
            plugin.getIgnoreJumping().remove(player.getUniqueId());
        } else {
            plugin.getIgnoreJumping().add(player.getUniqueId());
        }
        if (getVerify(player.getUniqueId(), "LIGHTNING")) {
            plugin.getIgnoreLightning().remove(player.getUniqueId());
        } else {
            plugin.getIgnoreLightning().add(player.getUniqueId());
        }
        if (getVerify(player.getUniqueId(), "PARTICLES")) {
            plugin.getIgnoreParticles().remove(player.getUniqueId());
        } else {
            plugin.getIgnoreParticles().add(player.getUniqueId());
        }
    }

    private void setDefault(final PreparedStatement statement, boolean teleport, boolean jump, boolean lightning, boolean particles) throws SQLException {
        statement.setString(2, String.valueOf(teleport));
        statement.setString(3, String.valueOf(jump));
        statement.setString(4, String.valueOf(lightning));
        statement.setString(5, String.valueOf(particles));
    }
}
