package xyz.developmentcl.commands;

import xyz.developmentcl.DatabaseConnector;
import xyz.developmentcl.password.PasswordHasher;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

public class LogginCommand implements CommandExecutor {
    
    private DatabaseConnector connector;
    private PasswordHasher passwordHasher;

    public LogginCommand(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;

        /*
        if (!player.hasPermission("xpshop.buy")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }
        */

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /login <user> <password> or /register <password> <password>");
            return true;
        }

        String passwd = args[0];
        String confirmation = args[1];

        if (label.equals("login")) {
            player.sendMessage(ChatColor.RED + "Trying to login as " + player.getDisplayName());

            return true;
        }
        else if (label.equals("register")){
            player.sendMessage(ChatColor.RED + "Trying to register as " + player.getDisplayName());
            if (passwd.equals(confirmation)) {
                if (insertPlayerOnDatabase(player.getDisplayName(), passwd)) {
                    player.sendMessage(ChatColor.GREEN + "Registration as " + player.getDisplayName() + " was successful.");
                    return true;
                }
                player.sendMessage(ChatColor.RED + "Registration as " + player.getDisplayName() + " went wrong.");
            }
            return true;
        }
        
        return true;
    }

    public boolean insertPlayerOnDatabase(String username, String passwd) {
        if (this.connector != null){
            String hashedPassword = passwordHasher.hashPassword(passwd);
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
