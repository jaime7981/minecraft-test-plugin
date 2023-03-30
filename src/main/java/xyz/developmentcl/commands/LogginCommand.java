package xyz.developmentcl.commands;

import xyz.developmentcl.database.DatabaseConnector;
import xyz.developmentcl.database.PlayerPlugin;
import xyz.developmentcl.password.PasswordHasher;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

public class LogginCommand implements CommandExecutor {
    
    private DatabaseConnector connector;
    private PasswordHasher passwordHasher = new PasswordHasher();

    private List<PlayerPlugin> activePlayers;
    
    private Player player;
    private String passwd;
    private String confirmation;
    private String playerName;

    public LogginCommand(DatabaseConnector connector, List<PlayerPlugin> activePlayers) {
        this.connector = connector;
        this.activePlayers = activePlayers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        player = (Player) sender;
        playerName = player.getDisplayName();

        /*
        if (!player.hasPermission("login.login")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }
        */

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /login <user> <password>");
            return true;
        }

        passwd = args[0];
        confirmation = args[1];

        player.sendMessage(ChatColor.RED + "Trying to login as " + playerName);
        if (passwd.equals(confirmation)) {
            if (checkPlayerLogin(playerName, passwd)) {
                PlayerPlugin newPlayer = connector.getPlayerPluginByUsername(playerName);
                if (newPlayer != null) {
                    activePlayers.add(newPlayer);
                }
                else {
                    activePlayers.add(new PlayerPlugin(playerName, 0));
                }
                player.sendMessage(ChatColor.GREEN + "Login as " + playerName + " was successful.");
                return true;
            }
            player.sendMessage(ChatColor.RED + "Login as " + playerName + " went wrong.");
            return false;
        }
        
        sender.sendMessage(ChatColor.RED + "Something Went Wrong,\nUsage: /login <user> <password> or /register <password> <password>");
        return false;
    }

    public boolean checkPlayerLogin(String username, String password) {
        if (this.connector != null){
            String hashedPassword = passwordHasher.hashPassword(password);
            if (hashedPassword == null) {
                return false;
            }
            if (hashedPassword.equals(this.connector.getPlayerPasswordByUsername(username, password))) {
                return true;
            }
        }
        return false;
    }
}
