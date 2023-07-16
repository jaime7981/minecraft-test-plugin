package xyz.developmentcl.eventlisteners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;


public class PreServerLogin implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        String playerName = e.getName();
    }


    public List<String> getFabPlayers() throws IOException{

        String name = new File(".").getCanonicalPath();
        String pathToJson = "plugins";
        File file = new File(name, "/" + pathToJson + "/"+ "FabPlayers.json");


        String currentName = "";
        BufferedReader br = new BufferedReader(new FileReader(file));

        List<String> listNames = new ArrayList<String>();
        while ((currentName = br.readLine()) != null) {
            listNames.add(currentName);
        }
        br.close();
        return listNames;
    }
    
}
