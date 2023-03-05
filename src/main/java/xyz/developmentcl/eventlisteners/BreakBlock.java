package xyz.developmentcl.eventlisteners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlock implements Listener {
    
    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        player.sendMessage(ChatColor.RED + "Player: " + player.getName() + "\nBlock broken: " + block.getType());

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
