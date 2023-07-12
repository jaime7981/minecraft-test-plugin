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
    private List<PlayerPlugin> activePlayers;

    private Player player;
    private String playerName;
    private String action;
    private String factionName;
    private Faction commandFaction;
    private String infoString;
    private Boolean isLoggedIn;
    private Boolean isOnFaction;

    public FactionCommand(DatabaseConnector connector, List<Faction> factions, List<PlayerPlugin> activePlayers) {
        this.connector = connector;
        this.factions = factions;
        this.activePlayers = activePlayers;
    }

    private Faction getFactionByName(String factionName) {
        for (Faction faction : factions) {
            if (faction.getName().equals(factionName)) {
                return faction;
            }
        }
        return null;
    }

    private Faction getPlayerFaction(String playerName) {
        for (Faction faction : factions) {
            if (faction.isPlayerOnFaction(playerName) == true) {
                return faction;
            }
        }
        return null;
    }

    private void showPlayersOnFaction(Faction faction) {
        List<PlayerPlugin> members = faction.getMembers();
        player.sendMessage(ChatColor.BLUE + faction.getName() + ":");
        for (PlayerPlugin member : members) {
            player.sendMessage(ChatColor.RED + member.getPlayerName());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        player = (Player) sender;
        playerName = player.getDisplayName();
        
        isLoggedIn = false;
        for (PlayerPlugin aPlayer : activePlayers) {
            if (aPlayer.getPlayerName().equals(playerName)) {
                isLoggedIn = true;
                break;
            }
        }

        if (isLoggedIn == false) {
            player.sendMessage(ChatColor.RED + "You must be logged in to use this command");
            return false;
        }

        isOnFaction = false;
        for (Faction faction : factions) {
            for (PlayerPlugin player : faction.getMembers()) {
                if (player.getPlayerName().equals(playerName)) {
                    isOnFaction = true;
                    break;
                }
            }
            if (isOnFaction == true) {
                break;
            }
        }

        if (!(0 < args.length && args.length < 3)) {
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
            if (isOnFaction == false) {
                player.sendMessage(ChatColor.RED + "You are not into any faction");
                return false;
            }
            factionName = "";
            for (Faction faction : factions) {
                for (PlayerPlugin player : faction.getMembers()) {
                    if (player.getPlayerName().equals(playerName)) {
                        factionName = faction.getName();
                        break;
                    }
                }
                if (factionName != "") {
                    break;
                }
            }
            if (this.connector.removePlayerFromFaction(factionName, playerName)) {
                for (Faction faction : factions) {
                    if (faction.getName().equals(factionName)) {
                        for (PlayerPlugin member : faction.getMembers()) {
                            if (member.getPlayerName().equals(playerName)) {
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

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /faction <action> (show/join/leave/create/members) <faction_name>");
            return false;
        }

        factionName = args[1].toUpperCase();
        commandFaction = getFactionByName(factionName);

        if (factionName.length() <= 4) {
            player.sendMessage(ChatColor.RED + "Faction names must be longer than 3 characters");
            return false;
        }
        
        if (commandFaction == null) {
            player.sendMessage(ChatColor.RED + "Faction " + factionName + " not found");
            return false;
        }

        if (action.equals("create")) {
            if (this.connector.insertFaction(factionName)) {
                player.sendMessage(ChatColor.GREEN + "Faction Created Succesfully");
                // Function to get faction from DB
                Faction newFaction = new Faction(factionName, -1);
                this.factions.add(newFaction);
                return true;
            }
            player.sendMessage(ChatColor.RED + "Error creating faction");
            return false;
        }
        else if (action.equals("join")) {
            if (isOnFaction == true) {
                player.sendMessage(ChatColor.RED + "You are already into a faction");
                return false;
            }
            if (this.connector.insertPlayerIntoFaction(factionName, playerName)) {
                for (Faction faction : factions) {
                    if (faction.getName().equals(factionName)) {
                        for (PlayerPlugin pPlayer : activePlayers) {
                            if (pPlayer.getPlayerName().equals(playerName)) {
                                faction.addMember(pPlayer);
                                break;
                            }
                        }
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
            showPlayersOnFaction(commandFaction);
            return true;
        }
        else if(action.equals("set_safe_zone")) {
            Faction playerFaction = getPlayerFaction(playerName);
            if (playerFaction == null) {
                player.sendMessage(ChatColor.RED + "You are not into any faction");
                return false;
            }
            if (playerFaction.getName().equals(factionName)) {
                player.sendMessage(ChatColor.RED + "You can't set your faction as safe zone");
                return false;
            }
            /*
            if (this.connector.setSafeZone(playerFaction)) {
                player.sendMessage(ChatColor.GREEN + "Safe zone set Succesfully");
                return true;
            }
             */
            return true;
        }
        return false;
    }
}
