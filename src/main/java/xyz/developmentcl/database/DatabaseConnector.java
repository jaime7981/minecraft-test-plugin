package xyz.developmentcl.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import xyz.developmentcl.factions.Faction;

public class DatabaseConnector {
    private final String url;
    private final String username;
    private final String password;
    
    private Connection connection;
    
    public DatabaseConnector(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    public boolean connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void disconnect() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean insertPlayer(String player_name, String hashed_passwrd) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO player (player_name, passwrd) VALUES (?, ?)");
            statement.setString(1, player_name);
            statement.setString(2, hashed_passwrd);
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPlayerPasswordByUsername(String username, String password) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT passwrd FROM player WHERE player_name = ?");
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            
            if (result.next()) {
                String psswrd = result.getString("passwrd");
                statement.close();
                return psswrd;
            }
            
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public PlayerPlugin getPlayerPluginByUsername(String username) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT id FROM player WHERE player_name = ?");
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            
            if (result.next()) {
                int playerId = result.getInt("id");
                statement.close();
                return new PlayerPlugin(username, playerId);
            }
            
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Faction getFactionByName(String factionName) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT id FROM faction WHERE faction_name = ?");
            statement.setString(1, factionName);
            ResultSet result = statement.executeQuery();
            
            if (result.next()) {
                int factionId = result.getInt("id");
                statement.close();
                return new Faction(factionName, factionId);
            }
            
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertPlayerIntoFaction(String faction_name, String player_name) {
        int factionId;
        int playerId;

        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT id FROM faction WHERE faction_name = ?");
            statement.setString(1, faction_name);
            ResultSet result = statement.executeQuery();

            factionId = -1;
            if (result.next()) {
                factionId = result.getInt("id");
            }

            if (factionId == -1) {
                return false;
            }

            statement = this.connection.prepareStatement("SELECT id FROM player WHERE player_name = ?");
            statement.setString(1, player_name);
            result = statement.executeQuery();
            
            if (result.next()) {
                playerId = result.getInt("id");
                statement = connection.prepareStatement("INSERT INTO faction_members (faction_id, player_id) VALUES (?, ?)");
                statement.setInt(1, factionId);
                statement.setInt(2, playerId);
                statement.executeUpdate();
                statement.close();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removePlayerFromFaction(String faction_name, String player_name) {
        int factionId;
        int playerId;

        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT f.id AS faction_id, f.faction_name, p.id AS player_id, p.player_name FROM faction f " + 
                                                                        "JOIN faction_members f_mem ON f.id = f_mem.faction_id " +
                                                                        "JOIN player p ON f_mem.player_id = p.id WHERE p.player_name = (?) AND f.faction_name = (?)");
            statement.setString(1, player_name);
            statement.setString(2, faction_name);
            ResultSet result = statement.executeQuery();
            
            if (result.next()) {
                // Leave error
                factionId = result.getInt("faction_id");
                playerId = result.getInt("player_id");

                statement = connection.prepareStatement("DELETE FROM faction_members WHERE (faction_id, player_id) IN (VALUES (?, ?))");
                statement.setInt(1, factionId);
                statement.setInt(2, playerId);
                statement.executeUpdate();
                statement.close();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertFaction(String faction_name) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO faction (faction_name) VALUES (?)");
            statement.setString(1, faction_name);
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Faction> getAllFactionsFromDB() {
        List<String> loadedFactions = new ArrayList<>();
        List<Faction> loadedFactionsEntity = new ArrayList<>();
        String factionName;
        int factionId;
        String playerName;
        int playerId;
        
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT id, faction_name FROM faction");
            ResultSet result = statement.executeQuery();
            
            while (result.next()) {
                factionName = result.getString("faction_name");
                factionId = result.getInt("id");

                if (!loadedFactions.contains(factionName)) {
                    loadedFactions.add(factionName);
                    Faction faction = new Faction(factionName, factionId);
                    loadedFactionsEntity.add(faction);
                }
            }

            statement = this.connection.prepareStatement("SELECT f.id AS faction_id, f.faction_name, p.id AS player_id, p.player_name FROM faction f " + 
                                                        "JOIN faction_members f_mem ON f.id = f_mem.faction_id " +
                                                        "JOIN player p ON p.id = f_mem.player_id");
            result = statement.executeQuery();

            while (result.next()) {
                factionName = result.getString("faction_name");
                factionId = result.getInt("faction_id");
                playerName = result.getString("player_name");
                playerId = result.getInt("player_id");

                for (Faction faction : loadedFactionsEntity) {
                    if (faction.getName().equals(factionName)) {
                        faction.addMember(new PlayerPlugin(playerName, playerId));
                        break;
                    }
                }
            }

            statement.close();
            return loadedFactionsEntity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}

