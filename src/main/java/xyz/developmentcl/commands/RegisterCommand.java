package xyz.developmentcl.commands;

import xyz.developmentcl.database.DatabaseConnector;
import xyz.developmentcl.password.PasswordHasher;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {
    private DatabaseConnector connector;
    private PasswordHasher passwordHasher = new PasswordHasher();
    
    private String passwd;
    private String confirmation;
    private Player player;
    private String playerName;

    public RegisterCommand(DatabaseConnector connector) {
        this.connector = connector;
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
            player.sendMessage(ChatColor.RED + "Usage: /register <password> <password>");
            return true;
        }

        passwd = args[0];
        confirmation = args[1];

        player.sendMessage(ChatColor.RED + "Trying to register as " + playerName);
        if (passwd.equals(confirmation)) {
            if (insertPlayerOnDatabase(playerName, passwd)) {
                player.sendMessage(ChatColor.GREEN + "Registration as " + playerName + " was successful.");
                return true;
            }
            player.sendMessage(ChatColor.RED + "Registration as " + playerName + " went wrong.");
            return false;
        }
        
        sender.sendMessage(ChatColor.RED + "Something Went Wrong,\nUsage: /login <user> <password> or /register <password> <password>");
        return false;
    }

    public boolean insertPlayerOnDatabase(String username, String password) {
        if (this.connector != null){
            String hashedPassword = passwordHasher.hashPassword(password);
            if (hashedPassword == null) {
                return false;
            }
            if (this.connector.insertPlayer(username, hashedPassword)) {
                return true;
            }
        }
        return false;
    }
}
