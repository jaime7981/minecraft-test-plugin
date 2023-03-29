package xyz.developmentcl.commands;

import xyz.developmentcl.database.DatabaseConnector;
import xyz.developmentcl.database.PlayerPlugin;
import xyz.developmentcl.factions.Faction;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

public class FactionCommand implements CommandExecutor {
    private DatabaseConnector connector;
    private List<Faction> factions;

    private Player player;
    private String playerName;
    private String action;
    private String factionName;
    private String infoString;

    public FactionCommand(DatabaseConnector connector) {
        this.connector = connector;
        factions = this.connector.getAllFactions();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        player = (Player) sender;
        playerName = player.getDisplayName();

        if (args.length >= 1 || args.length <= 2) {
            player.sendMessage(ChatColor.RED + "Usage: /faction <action> (show/join/leave/create/members) <faction_name>");
            return true;
        }

        action = args[0];

        if (action.equals("show")) {
            infoString = "";
            for (Faction faction : factions) {
                infoString += faction.getName() + " -> members: " + faction.getMembers().size()  + "\n";
            }
            player.sendMessage(ChatColor.BLUE + infoString);
            return true;
        }
        else if (action.equals("leave")) {
            if (this.connector.removePlayerFromFaction(factionName, playerName)) {
                for (Faction faction : factions) {
                    if (faction.getName() == factionName) {
                        List<PlayerPlugin> members = faction.getMembers();
                        for (PlayerPlugin member : members) {
                            if (member.getPlayerName() == playerName) {
                                faction.removeMember(member);
                                break;
                            }
                        }
                        break;
                    }
                }
                player.sendMessage(ChatColor.GREEN + "You left your faction Succesfully");
                return true;
            }
            player.sendMessage(ChatColor.RED + "Error leaving faction");
            return false;
        }

        factionName = args[1].toUpperCase();

        if (factionName.length() <= 4) {
            player.sendMessage(ChatColor.RED + "Faction names must be longer than 3 characters");
            return false;
        }

        if (action.equals("create")) {
            if (this.connector.insertFaction(factionName)) {
                player.sendMessage(ChatColor.GREEN + "Faction Created Succesfully");
                Faction newFaction = new Faction(factionName);
                this.factions.add(newFaction);
                return true;
            }
            player.sendMessage(ChatColor.RED + "Error creating faction");
            return false;
        }
        else if (action.equals("join")) {
            if (this.connector.insertPlayerIntoFaction(factionName, playerName)) {
                for (Faction faction : factions) {
                    if (faction.getName() == factionName) {
                        faction.addMember(new PlayerPlugin(playerName));
                        break;
                    }
                }
                player.sendMessage(ChatColor.GREEN + "Joined to faction Succesfully");
                return true;
            }
            player.sendMessage(ChatColor.RED + "Error joining faction");
            return false;
        }
        else if (action.equals("members")) {
            String membersString = ChatColor.BLUE + factionName + "\n";
            for (Faction faction : factions) {
                if (faction.getName() == factionName) {
                    List<PlayerPlugin> members = faction.getMembers();
                    for (PlayerPlugin member : members) {
                        if (member.getPlayerName() == playerName) {
                            faction.removeMember(member);
                            membersString += ChatColor.RED + playerName + "\n";
                            break;
                        }
                    }
                    break;
                }
            }
            player.sendMessage(membersString);
            return true;
        }
        return false;
    }
}
