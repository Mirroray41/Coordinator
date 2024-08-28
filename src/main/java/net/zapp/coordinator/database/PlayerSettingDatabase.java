package net.zapp.coordinator.database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.logging.Logger;

public class PlayerSettingDatabase {
    private final Connection connection;

    Logger logger = Bukkit.getLogger();

    private boolean visibilityDefault;
    private boolean locationDefault;
    private int locationTypeDefault;
    private boolean directionDefault;
    private int directionTypeDefault;
    private boolean timeDefault;
    private int timeTypeDefault;

    public PlayerSettingDatabase(String path,
                                 boolean visibilityDefault,
                                 boolean locationDefault, int locationTypeDefault,
                                 boolean directionDefault, int directionTypeDefault,
                                 boolean timeDefault, int timeTypeDefault) throws SQLException {
        this.visibilityDefault = visibilityDefault;
        this.locationDefault = locationDefault;
        this.locationTypeDefault = locationTypeDefault;
        this.directionDefault = directionDefault;
        this.directionTypeDefault = directionTypeDefault;
        this.timeDefault = timeDefault;
        this.timeTypeDefault = timeTypeDefault;

        logger.info(visibilityDefault + " " + locationDefault + " " +  locationTypeDefault + " " +  directionDefault + " " +  directionTypeDefault + " " +  timeDefault + " " +  timeTypeDefault);

        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "uuid TEXT PRIMARY KEY," +
                    "visibility BOOLEAN NOT NULL DEFAULT " + visibilityDefault +"," +
                    "location BOOLEAN NOT NULL DEFAULT " + locationDefault +"," +
                    "location_type TINYINT NOT NULL DEFAULT " + locationTypeDefault +"," +
                    "direction BOOLEAN NOT NULL DEFAULT " + directionDefault +"," +
                    "direction_type TINYINT NOT NULL DEFAULT " + directionTypeDefault +"," +
                    "time BOOLEAN NOT NULL DEFAULT " + timeDefault +"," +
                    "time_type TINYINT NOT NULL DEFAULT " + timeTypeDefault +")");
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void addPlayer(Player player) {
        if (!playerExists(player)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (uuid) VALUES (?)")) {
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                logger.warning("Failed to add player " + player + " from the database. Please report this to the author");
                ex.printStackTrace();
            }
        }
    }

    public boolean playerExists(Player player) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT visibility FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            logger.warning("Failed to get player " + player + " from the database. Please report this to the author");
            ex.printStackTrace();
        }
        return false;
    }

    public int getIntStatement(Player player, String statement) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + statement + " FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(statement);
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            logger.warning("Failed to get setting of player: " + player + ". Please report this to the author");
            ex.printStackTrace();
            return 0;
        }
    }

    public boolean getBoolStatement(Player player, String statement) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + statement + " FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean(statement);
            } else {
                return true;
            }
        } catch (SQLException ex) {
            logger.warning("Failed to get setting of player: " + player + ". Please report this to the author");
            ex.printStackTrace();
            return true;
        }
    }

    public void setIntStatement(Player player, int value, String statement){
        if (!playerExists(player)){
            addPlayer(player);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET " + statement + " = ? WHERE uuid = ?")) {
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.warning("Failed to change setting of player: " + player + ". Please report this to the author");
            ex.printStackTrace();
        }
    }

    public void setDefault(Player player, String statement) {
        if (!playerExists(player)){
            addPlayer(player);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET " + statement + " = ? WHERE uuid = ?")) {
            logger.info(visibilityDefault + " " + locationDefault + " " +  locationTypeDefault + " " +  directionDefault + " " +  directionTypeDefault + " " +  timeDefault + " " +  timeTypeDefault);
            switch (statement) {
                case "visibility": preparedStatement.setBoolean(1, true); break;
                case "location": preparedStatement.setBoolean(1, locationDefault); break;
                case "location_type": preparedStatement.setInt(1, locationTypeDefault); break;
                case "direction": preparedStatement.setBoolean(1, directionDefault); break;
                case "direction_type": preparedStatement.setInt(1, directionTypeDefault); break;
                case "time": preparedStatement.setBoolean(1, timeDefault); break;
                case "time_type": preparedStatement.setInt(1, timeTypeDefault); break;
            }
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.warning("Failed to change setting of player: " + player + ". Please report this to the author");
            ex.printStackTrace();
        }
    }


    public void setBoolStatement(Player player, boolean value, String statement){
        if (!playerExists(player)){
            addPlayer(player);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET " + statement + " = ? WHERE uuid = ?")) {
            preparedStatement.setBoolean(1, value);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.warning("Failed to change setting of player: " + player + ". Please report this to the author");
            ex.printStackTrace();
        }
    }

    public void setVisibility(Player player, boolean state){
        setBoolStatement(player, state, "visibility");
    }

    public boolean getVisibility(Player player) {
        return getBoolStatement(player, "visibility");
    }

    public void setLocation(Player player, boolean state){
        setBoolStatement(player, state, "location");
    }

    public boolean getLocation(Player player) {
        return getBoolStatement(player, "location");
    }

    public void setLocationType(Player player, int state){
        setIntStatement(player, state, "location_type");
    }

    public int getLocationType(Player player) {
        return getIntStatement(player, "location_type");
    }

    public void setDirection(Player player, boolean state){
        setBoolStatement(player, state, "direction");
    }

    public boolean getDirection(Player player) {
        return getBoolStatement(player, "direction");
    }

    public void setDirectionType(Player player, int state){
        setIntStatement(player, state, "direction_type");
    }

    public int getDirectionType(Player player) {
        return getIntStatement(player, "direction_type");
    }

    public void setTime(Player player, boolean state){
        setBoolStatement(player, state, "time");
    }

    public boolean getTime(Player player) {
        return getBoolStatement(player, "time");
    }

    public void setTimeType(Player player, int state){
        setIntStatement(player, state, "time_type");
    }

    public int getTimeType(Player player) {
        return getIntStatement(player, "time_type");
    }

}
