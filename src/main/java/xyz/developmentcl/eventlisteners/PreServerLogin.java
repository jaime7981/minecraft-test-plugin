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
import org.bukkit.Bukkit;



    //Eliminar jugadores que no esten en la lista FAP del mas reciente al mas antiguo:

    //Para eso, ver si esta lleno el servidor o no, si es asi, seguimos, si no, no pasa nada.
    //Luego, comparamos si el que ingresa esta en FAP, si es asi, vemos el jugador mas reciente que no este en FAP y lo sacamos
    //Luego de sacarlo, podemos ingresarlo.
    //numero de FAP no puede superar el numero max de jugadores en el servidor

public class PreServerLogin implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) throws IOException {
        String playerName = e.getName();
        String[] onlinePlayers = (String[]) Bukkit.getOnlinePlayers().toArray();
        if((isFap(playerName)) && (onlinePlayers.length == Bukkit.getMaxPlayers())){
            List<String> noFapPlayers = getNoFapPlayersOnline();
            // Sacar al ultimo que entró
        }
    }

    

    public List<String> getFapPlayers() throws IOException{

        String name = new File(".").getCanonicalPath();
        String pathToJson = "plugins";
        File file = new File(name, "/" + pathToJson + "/"+ "FapPlayers.json");


        String currentName = "";
        BufferedReader br = new BufferedReader(new FileReader(file));

        List<String> listNames = new ArrayList<String>();
        while ((currentName = br.readLine()) != null) {
            listNames.add(currentName);
        }
        br.close();
        return listNames;
    }

    public List<String> getNoFapPlayersOnline() throws IOException{

        String[] onlinePlayers = (String[]) Bukkit.getOnlinePlayers().toArray();
        List<String> fapPlayers = getFapPlayers();
        List<String> noFapPlayers = new ArrayList<String>();
        
        boolean Fap = false;
        for (String player: onlinePlayers){
            for (String fapPlayer: fapPlayers){
                if(player.equals(fapPlayer)){
                    Fap = true;
                }
            }
            if(Fap == false){
                noFapPlayers.add(player);
            }
        }
        return noFapPlayers;
    }

    public boolean isFap(String player) throws IOException{

        boolean value = false;
        List<String> FapPlayers = getFapPlayers();

        for(String _player: FapPlayers){
            if(player.equals(_player)){
                value = true;
                break;
            }
        }

        return value;
    }


    

}
