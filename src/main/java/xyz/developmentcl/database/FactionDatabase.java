package xyz.developmentcl.database;

import java.sql.*;

import java.util.List;
import java.util.ArrayList;

public class FactionDatabase {

    public static List<List<Integer>> formatCoordinates(int startX, int startY, int startZ, int endX, int endY, int endZ) {
        List<List<Integer>> coordinates = new ArrayList<>();

        coordinates.add(List.of(startX, startY, startZ));
        coordinates.add(List.of(endX, endY, endZ));

        return coordinates;
    }

    // Insert coordinates into the database
    public static List<List<Integer>> insertFactionSafeCoordinates(Connection connection, int faction_id, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        String query = "INSERT INTO faction_safe_coordinates (start_x, start_y, start_z, end_x, end_y, end_z, faction_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, startX);
            statement.setInt(2, startY);
            statement.setInt(3, startZ);
            statement.setInt(4, endX);
            statement.setInt(5, endY);
            statement.setInt(6, endZ);
            statement.setInt(7, faction_id);

            statement.executeUpdate();
            System.out.println("Coordinates inserted successfully.");
            return formatCoordinates(startX, startY, startZ, endX, endY, endZ);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update coordinates in the database
    public static List<List<Integer>> updateCoordinates(Connection connection, int faction_id, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        String query = "UPDATE faction_safe_coordinates SET start_x = ?, start_y = ?, start_z = ?, end_x = ?, end_y = ?, end_z = ? WHERE faction_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, startX);
            statement.setInt(2, startY);
            statement.setInt(3, startZ);
            statement.setInt(4, endX);
            statement.setInt(5, endY);
            statement.setInt(6, endZ);
            statement.setInt(7, faction_id);

            statement.executeUpdate();
            System.out.println("Coordinates updated successfully.");
            return formatCoordinates(startX, startY, startZ, endX, endY, endZ);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieve coordinates from the database based on the foreign key
    public static List<List<Integer>> getCoordinatesByForeignKey(Connection connection, int faction_id) {
        String query = "SELECT * FROM faction_safe_coordinates WHERE faction_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, faction_id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int startX = resultSet.getInt("start_x");
                int startY = resultSet.getInt("start_y");
                int startZ = resultSet.getInt("start_z");
                int endX = resultSet.getInt("end_x");
                int endY = resultSet.getInt("end_y");
                int endZ = resultSet.getInt("end_z");

                System.out.println("ID: " + id);
                System.out.println("Start X: " + startX);
                System.out.println("Start Y: " + startY);
                System.out.println("Start Z: " + startZ);
                System.out.println("End X: " + endX);
                System.out.println("End Y: " + endY);
                System.out.println("End Z: " + endZ);

                return formatCoordinates(startX, startY, startZ, endX, endY, endZ);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Delete coordinates from the database based on the foreign key
    public static boolean deleteCoordinatesByForeignKey(Connection connection, int faction_id) {
        String query = "DELETE FROM faction_safe_coordinates WHERE faction_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, faction_id);

            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected + " coordinate(s) deleted successfully.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
