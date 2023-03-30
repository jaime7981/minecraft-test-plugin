package xyz.developmentcl.commands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import xyz.developmentcl.database.PlayerPlugin;

public class XPShopCommand implements CommandExecutor {
    private ItemRegistry itemRegistry = new ItemRegistry();
    private List<PlayerPlugin> activePlayers;

    private String playerName;
    private Boolean isLoggedIn;

    public XPShopCommand(List<PlayerPlugin> activePlayers) {
        this.activePlayers = activePlayers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    // Check if the command sender is a player
    if (!(sender instanceof Player)) {
        sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
        return true;
    }
    
    Player player = (Player) sender;
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
    
    // Check if the player has permission to use the command
    /*
    if (!player.hasPermission("xpshop.buy")) {
        player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
        return true;
    }
    */
    
    // Check if the command has the correct number of arguments
    if (args.length != 1) {
        player.sendMessage(ChatColor.RED + "Usage: /xpshop <item> or /xpshop show to see all items and costs");
        return true;
    }
    
    String itemName = args[0].toUpperCase();

    if (itemName.equals("SHOW")) {
        itemRegistry.showItems(player);
        return true;
    }
    
    // Get the item from the item registry
    ItemStack item = itemRegistry.getItem(itemName);
    
    if (item == null) {
        player.sendMessage(ChatColor.RED + "Item not found.");
        return true;
    }
    
    // Get the cost of the item
    int cost = itemRegistry.getItemCost(item);

    player.sendMessage(ChatColor.BLUE + "Item: " + item.getType().toString());
    
    // Check if the player has enough XP to buy the item
    int playerLevel = player.getLevel();
    
    if (playerLevel < cost) {
        player.sendMessage(ChatColor.RED + "You do not have enough XP to buy this item.");
        return true;
    }
    
    // Subtract the cost from the player's XP
    player.setLevel(playerLevel - cost);
    player.sendMessage(ChatColor.GREEN + "You have bought " + item.getAmount() + " " + item.getType().toString() + " for " + cost + " XP.");
    
    // Give the item to the player
    HashMap<Integer, ItemStack> remaining = player.getInventory().addItem(item);
    
    // Drop any remaining items on the ground
    for (ItemStack remainingItem : remaining.values()) {
        player.getWorld().dropItem(player.getLocation(), remainingItem);
    }
    
    return true;
}


}