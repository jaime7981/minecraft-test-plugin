package xyz.developmentcl.eventlisteners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.developmentcl.database.PlayerPlugin;

public class ServerQuit implements Listener {

    private List<PlayerPlugin> activePlayers;
    private String playerName;

    public ServerQuit(List<PlayerPlugin> activePlayers) {
        this.activePlayers = activePlayers;
    }
    
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        Player loggedPlayer = e.getPlayer();
        playerName = loggedPlayer.getDisplayName();

        for (PlayerPlugin player : activePlayers) {
            if (player.getPlayerName().equals(playerName)) {
                activePlayers.remove(player);
                break;
            }
        }

    }
}
