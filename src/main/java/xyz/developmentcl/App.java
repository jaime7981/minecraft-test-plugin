package xyz.developmentcl;

import xyz.developmentcl.commands.FactionCommand;
import xyz.developmentcl.commands.LogginCommand;
import xyz.developmentcl.commands.RegisterCommand;
import xyz.developmentcl.commands.XPShopCommand;

import xyz.developmentcl.database.DatabaseConnector;
import xyz.developmentcl.database.PlayerPlugin;

import xyz.developmentcl.eventlisteners.BreakBlock;
import xyz.developmentcl.eventlisteners.ServerLogin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    DatabaseConnector database = null;
    List<PlayerPlugin> activePlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        getLogger().info("Hello, SpigotMC!");

        String url = "jdbc:postgresql://localhost:5432/minecraft_server_db";
        String username = "jaime_admin";
        String password = "Jaime@79811";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            getLogger().severe("Failed to load jdbc driver.");
        }

        database = new DatabaseConnector(url, username, password);
        if (database.connect()) {
            getLogger().info("Connected to database.");
        } else {
            getLogger().severe("Failed to connect to database.");
            database = null;
        }

        getServer().getPluginManager().registerEvents(new BreakBlock(), this);
        getServer().getPluginManager().registerEvents(new ServerLogin(), this);
        getCommand("xpshop").setExecutor(new XPShopCommand());
        getCommand("login").setExecutor(new LogginCommand(database));
        getCommand("register").setExecutor(new RegisterCommand(database));
        getCommand("faction").setExecutor(new FactionCommand(database));
    }

    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
        
        if (database == null) {
            getLogger().info("Database object was null.");
        }
        else {
            database.disconnect();
            getLogger().info("Disconnected from database.");
        }
    }

    public DatabaseConnector getConnector() {
        if (database != null) {
            return database;
        }
        return null;
    }
}
