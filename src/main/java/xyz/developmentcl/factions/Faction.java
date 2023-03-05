package xyz.developmentcl.factions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Faction {
    private String name;
    private List<Player> members;
    private List<Faction> enemies;
    private List<Faction> allies;

    public Faction(String name) {
        this.name = name;
        // Load faction info from database
        // Whenever new data is comming, 
        // first insert to database then
        // load it into the program
        members = new ArrayList<Player>();
        enemies = new ArrayList<Faction>();
        allies = new ArrayList<Faction>();
    }

    public boolean loadFromDatabase() {
        return false;
    }

    public String getName() {
        return name;
    }

    public void addMember(Player player) {
        members.add(player);
    }

    public void removeMember(Player player) {
        members.remove(player);
    }

    public List<Player> getMembers() {
        return members;
    }

    public void declareWarOn(Faction faction) {
        this.enemies.add(faction);
    }

    public void makePeaceWith(Faction faction) {
        enemies.remove(faction);
        allies.add(faction);
    }

    public boolean isEnemy(Faction faction) {
        return enemies.contains(faction);
    }

    public boolean isAlly(Faction faction) {
        return allies.contains(faction);
    }
}

