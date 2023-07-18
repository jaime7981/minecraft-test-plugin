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

    //Eliminar jugadores que no esten en la lista FAP del mas reciente al mas antiguo:

    //Para eso, ver si esta lleno el servidor o no, si es asi, seguimos, si no, no pasa nada.
    //Luego, comparamos si el que ingresa esta en FAP, si es asi, vemos el jugador mas reciente que no este en FAP y lo sacamos
    //Luego de sacarlo, podemos ingresarlo.
    //numero de FAP no puede superar el numero max de jugadores en el servidor

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
