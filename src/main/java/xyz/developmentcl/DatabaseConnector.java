package xyz.developmentcl;

import java.sql.*;

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
}

