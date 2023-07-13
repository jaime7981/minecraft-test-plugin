package xyz.developmentcl.commands;

import xyz.developmentcl.database.DatabaseConnector;
import xyz.developmentcl.database.PlayerPlugin;
import xyz.developmentcl.factions.Faction;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
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
    private Boolean isPlayerLoggedIn;
    private Boolean isPlayerOnFaction;

    public FactionCommand(DatabaseConnector connector, List<Faction> factions, List<PlayerPlugin> activePlayers) {
        this.connector = connector;
        this.factions = factions;
        this.activePlayers = activePlayers;
    }

    private boolean isPlayerLoggedIn(String playerName) {
        for (PlayerPlugin player : activePlayers) {
            if (player.getPlayerName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlayerOnFaction(String playerName) {
        for (Faction faction : factions) {
            for (PlayerPlugin player : faction.getMembers()) {
                if (player.getPlayerName().equals(playerName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Faction getFactionByName(String factionName) {
        for (Faction faction : factions) {
            if (faction.getName().equals(factionName)) {
                return faction;
            }
        }
        return null;
    }

    private PlayerPlugin getPlayerFromActivePlayers(String playerName) {
        for (PlayerPlugin player : activePlayers) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
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

    private boolean showPlayersOnFaction(Faction faction) {
        List<PlayerPlugin> members = faction.getMembers();
        player.sendMessage(ChatColor.BLUE + faction.getName() + ":");
        for (PlayerPlugin member : members) {
            player.sendMessage(ChatColor.RED + member.getPlayerName());
        }
        return true;
    }

    private boolean showFactionInfo(Faction faction) {
        player.sendMessage(ChatColor.BLUE + faction.getName() + ":");
        player.sendMessage(ChatColor.RED + "Members: " + faction.getMembers().size());
        return true;
    }

    private boolean showFactionsInfo() {
        for (Faction faction : factions) {
            showFactionInfo(faction);
        }
        return true;
    }

    private boolean createFaction(String factionName) {
        if (this.connector.insertFaction(factionName)) {
            player.sendMessage(ChatColor.GREEN + "Faction Created Succesfully");
            this.factions.add(new Faction(factionName, -1));
            return true;
        }
        player.sendMessage(ChatColor.RED + "Error creating faction");
        return false;
    }

    private boolean playerJoinFaction(String factionName, String playerName) {
        if (this.connector.insertPlayerIntoFaction(factionName, playerName)) {
            Faction playerFaction = getPlayerFaction(playerName);
            PlayerPlugin playerPlugin = getPlayerFromActivePlayers(playerName);
            playerFaction.addMember(playerPlugin);
            player.sendMessage(ChatColor.GREEN + "Joined to faction Succesfully");
            return true;
        }
        player.sendMessage(ChatColor.RED + "Error joining faction");
        return false;
    }

    private boolean playerLeaveFaction(String playerName) {
        Faction playerFaction = getPlayerFaction(playerName);
        if (this.connector.removePlayerFromFaction(playerFaction.getName(), playerName)) {
            PlayerPlugin playerPlugin = getPlayerFromActivePlayers(playerName);
            playerFaction.removeMember(playerPlugin);
            player.sendMessage(ChatColor.GREEN + "Left faction Succesfully");
            return true;
        }
        player.sendMessage(ChatColor.RED + "Error leaving faction");
        return false;
    }

    private List<List<Integer>> setSafeZoneDimentions(Location playerLocation) {
        int startX = playerLocation.getBlockX() - 10;
        int startY = playerLocation.getBlockY() - 10;
        int startZ = playerLocation.getBlockZ() - 10;
        int endX = playerLocation.getBlockX() + 10;
        int endY = playerLocation.getBlockY() + 10;
        int endZ = playerLocation.getBlockZ() + 10;

        return List.of(List.of(startX, startY, startZ), List.of(endX, endY, endZ));
    }

    private boolean playerSetSafeZone(Player player) {
        Faction playerFaction = getPlayerFaction(player.getDisplayName());
        Location playerLocation = player.getLocation();
        List<List<Integer>> safeZoneCoordinates = setSafeZoneDimentions(playerLocation);
        connector.setSafeZoneCoordinates(playerFaction, safeZoneCoordinates);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        player = (Player) sender;
        playerName = player.getDisplayName();
        isPlayerLoggedIn = isPlayerLoggedIn(playerName);
        isPlayerOnFaction = isPlayerOnFaction(playerName);

        if (isPlayerLoggedIn == false) {
            player.sendMessage(ChatColor.RED + "You must be logged in to use this command");
            return false;
        }

        if (!(0 < args.length && args.length < 3)) {
            player.sendMessage(ChatColor.RED + "Usage: /faction <action> (show/join/leave/create/members/safe_zone) <faction_name>");
            return true;
        }

        action = args[0];

        if (action.equals("show")) {
            return showFactionsInfo();
        }
        else if (action.equals("leave")) {
            if (isPlayerOnFaction == false) {
                player.sendMessage(ChatColor.RED + "You are not in a faction");
                return false;
            }

            return playerLeaveFaction(playerName);
        }
        else if(action.equals("safe_zone")) {
            if (isPlayerOnFaction == false) {
                player.sendMessage(ChatColor.RED + "You are not in a faction");
                return false;
            }

            return playerSetSafeZone(player);
        }

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /faction <action> (show/join/leave/create/members/safe_zone) <faction_name>");
            return false;
        }

        factionName = args[1].toUpperCase();
        commandFaction = getFactionByName(factionName);

        if (factionName.length() <= 4) {
            player.sendMessage(ChatColor.RED + "Faction names must be longer than 3 characters");
            return false;
        }

        if (action.equals("create")) {
            if (isPlayerOnFaction == true) {
                player.sendMessage(ChatColor.RED + "You are already into a faction");
                return false;
            }
            return createFaction(factionName);
        }
        else if (action.equals("join")) {
            if (isPlayerOnFaction == true) {
                player.sendMessage(ChatColor.RED + "You are already into a faction");
                return false;
            }
            
            return playerJoinFaction(factionName, playerName);
        }
        else if (action.equals("members")) {
            if (commandFaction == null) {
                player.sendMessage(ChatColor.RED + "Faction " + factionName + " not found");
                return false;
            }

            return showPlayersOnFaction(commandFaction);
        }
        return false;
    }
}
