package xyz.developmentcl.eventlisteners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import xyz.developmentcl.factions.Faction;

public class BreakBlock implements Listener {
    List<Faction> factions;

    public BreakBlock(List<Faction> factions) {
        this.factions = factions;
    }
    
    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        player.sendMessage(ChatColor.RED + "Player: " + player.getName() + "\nBlock broken: " + block.getType());

        String factionTest = "";
        for (Faction faction : factions) {
            factionTest += ChatColor.GREEN + faction.getName() + "\n";
        }
        player.sendMessage(factionTest);
        /*
        // Check if the player has permission to break the block
        if (!player.hasPermission("blockprotection.break")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You do not have permission to break this block.");
            return;
        }
        
        // Check if the block is protected
        if (isProtected(block.getLocation())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "This block is protected.");
        }
         */
    }
}
