package xyz.developmentcl.database;

public class PlayerPlugin {
    private String playerName;
    private int id;

    public PlayerPlugin(String playerName, int id) {
        this.playerName = playerName;
        this.id = id;
    }

    public String getPlayerName() {
        return this.playerName;
    }
    
    public int getPlayerId() {
        return this.id;
    }
}
