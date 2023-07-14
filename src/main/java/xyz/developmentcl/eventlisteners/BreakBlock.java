package xyz.developmentcl.eventlisteners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import xyz.developmentcl.factions.Faction;
import xyz.developmentcl.game_functions.GameFunctions;

public class BreakBlock implements Listener {
    List<Faction> factions;

    public BreakBlock(List<Faction> factions) {
        this.factions = factions;
    }
    
    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        Location blockLocation = block.getLocation();
        
        if (!GameFunctions.isPlayerAllowedToBuild(this.factions, player, blockLocation)) {
            player.sendMessage(ChatColor.RED + "You are not allowed to build here!");
            e.setCancelled(true);
        }
    }
}
