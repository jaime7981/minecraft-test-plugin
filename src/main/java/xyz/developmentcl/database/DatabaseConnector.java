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

    public boolean insertPlayerIntoFaction(String faction_name, String player_name) {
        int factionId;
        int playerId;

        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT f.id, p.id FROM faction f " + 
                                                                            "JOIN faction_members f_mem ON f.id = f_mem.id " +
                                                                            "JOIN player p ON f_mem.id = p.id WHERE p.player_name = ? AND f.faction_name = ?");
            statement.setString(1, player_name);
            statement.setString(2, faction_name);
            ResultSet result = statement.executeQuery();
            
            if (result.next()) {
                factionId = result.getInt("f.id");
                playerId = result.getInt("p.id");

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
            PreparedStatement statement = this.connection.prepareStatement("SELECT f.id, p.id FROM faction f " + 
                                                                            "JOIN faction_members f_mem ON f.id = f_mem.id " +
                                                                            "JOIN player p ON f_mem.id = p.id WHERE p.player_name = ? AND f.faction_name = ?");
            statement.setString(1, player_name);
            statement.setString(2, faction_name);
            ResultSet result = statement.executeQuery();
            
            if (result.next()) {
                factionId = result.getInt("f.id");
                playerId = result.getInt("p.id");

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

    public List<Faction> getAllFactions() {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT f.faction_name, p.player_name FROM faction f " + 
                                                                            "JOIN faction_members f_mem ON f.id = f_mem.id " +
                                                                            "JOIN player p ON f_mem.id = p.id");
            ResultSet result = statement.executeQuery();
            List<String> loadedFactions = new ArrayList<>();
            List<Faction> loadedFactionsEntity = new ArrayList<>();
            String factionName;
            String playerName;
            
            while (result.next()) {
                factionName = result.getString("faction_name");
                playerName = result.getString("player_name");

                if (loadedFactions.contains(factionName)) {
                    for(Faction faction : loadedFactionsEntity) {
                        if (faction.getName() == factionName) {
                            faction.addMember(new PlayerPlugin(playerName));
                            break;
                        }
                    }
                }
                else {
                    loadedFactions.add(factionName);
                    Faction faction = new Faction(factionName);
                    faction.addMember(new PlayerPlugin(playerName));
                    loadedFactionsEntity.add(faction);
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

