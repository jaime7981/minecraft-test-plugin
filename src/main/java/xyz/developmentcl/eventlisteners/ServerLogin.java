package xyz.developmentcl.eventlisteners;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerLogin implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent e) {
        Player loggedPlayer = e.getPlayer();
        e.setJoinMessage(ChatColor.BLUE + "Como a " + ChatColor.RED + loggedPlayer.getName() + ChatColor.BLUE + " no le gustan subtituladas\nSe las come dobladas\nWelcome back!");
    }
}
