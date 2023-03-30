package xyz.developmentcl.factions;

import xyz.developmentcl.database.PlayerPlugin;

import java.util.ArrayList;
import java.util.List;

public class Faction {
    private int id;
    private String name;
    private List<PlayerPlugin> members;
    private List<Faction> enemies;
    private List<Faction> allies;

    public Faction(String name, int id) {
        this.name = name;
        this.id = id;
        members = new ArrayList<PlayerPlugin>();
        enemies = new ArrayList<Faction>();
        allies = new ArrayList<Faction>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return this.id;
    }

    public void addMember(PlayerPlugin player) {
        members.add(player);
    }

    public void removeMember(PlayerPlugin player) {
        members.remove(player);
    }

    public List<PlayerPlugin> getMembers() {
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

