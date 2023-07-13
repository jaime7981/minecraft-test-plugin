package xyz.developmentcl.game_functions;

import java.util.List;

import xyz.developmentcl.factions.Faction;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GameFunctions {
    public static boolean isPointInRectangularPrism(double x1, double y1, double z1, double x2, double y2, double z2, double px, double py, double pz) {
        // Calculate the vectors from the top-left-front corner to the other corners
        double[] v1 = {x2 - x1, y2 - y1, z2 - z1};
        double[] v2 = {px - x1, py - y1, pz - z1};

        // Calculate the scalar triple product
        double scalarTripleProduct = v1[0] * (v2[1] * v1[2] - v2[2] * v1[1])
                + v1[1] * (v2[2] * v1[0] - v2[0] * v1[2])
                + v1[2] * (v2[0] * v1[1] - v2[1] * v1[0]);

        // Check if the scalar triple product is zero (point lies on one of the prism faces)
        if (scalarTripleProduct == 0) {
            // Check if the prism has a non-zero volume
            if (v1[0] != 0 || v1[1] != 0 || v1[2] != 0) {
                return true;
            }
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

            // Check if the player is in the faction's safe zone
            if (isPointInRectangularPrismCoordinates(factionSafeZoneCorners, itemLocation)) {
                // check if player is not in faction
                if (!faction.isPlayerOnFaction(player.getName())) {
                    return false;
                }
            }
        }

        return true;
    }
}
