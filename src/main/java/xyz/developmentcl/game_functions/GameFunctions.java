package xyz.developmentcl.game_functions;

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
}
