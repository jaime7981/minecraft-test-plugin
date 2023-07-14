package xyz.developmentcl.game_functions;

import java.util.List;

import xyz.developmentcl.factions.Faction;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GameFunctions {
    public static boolean isPointInRectangularPrism(double x1, double y1, double z1, double x2, double y2, double z2, double px, double py, double pz) {
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);
        double minZ = Math.min(z1, z2);
        double maxZ = Math.max(z1, z2);

        if (px >= minX && px <= maxX && py >= minY && py <= maxY && pz >= minZ && pz <= maxZ) {
            return true;
        }
        return false;
    }

    public static boolean isPointInRectangularPrismCoordinates(List<List<Integer>> corners, Location location) {
        return isPointInRectangularPrism(
                corners.get(0).get(0),
                corners.get(0).get(1),
                corners.get(0).get(2),
                corners.get(1).get(0),
                corners.get(1).get(1),
                corners.get(1).get(2),
                location.getX(),
                location.getY(),
                location.getZ()
        );
    }

    public static boolean isPlayerAllowedToBuild(List<Faction> factions, Player player, Location itemLocation) {
        List<List<Integer>> factionSafeZoneCorners = null;

        for (Faction faction : factions) {
            // Get the faction's safe zone corners
            factionSafeZoneCorners = faction.getSafeCoordinates();

            // check if faction has safe zone
            if (faction.getSafeCoordinates() != null && !faction.getSafeCoordinates().isEmpty()) {
                // Check if the player is in the faction's safe zone
                if (isPointInRectangularPrismCoordinates(factionSafeZoneCorners, itemLocation)) {
                    // check if player is not in faction
                    if (!faction.isPlayerOnFaction(player.getName())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
