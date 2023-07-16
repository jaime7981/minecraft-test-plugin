package xyz.developmentcl.eventlisteners;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.io.File;
import java.io.FileReader;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;


public class PreServerLogin implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        String playerName = e.getName();
    }


    public List<String> getFabPlayers(){
        String name = new File(".").getCanonicalPath();
        String pathToJson = "plugins";
        File file = new File(name, "/" + pathToJson + "/"+ "FabPlayers.json");

        BufferedReader br = new BufferedReader(new FileReader(file));
        br.close();
    }

}
