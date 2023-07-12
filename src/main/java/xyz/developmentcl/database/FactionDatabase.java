package xyz.developmentcl.database;

import java.sql.*;

public class FactionDatabase {

    // Insert coordinates into the database
    public boolean insertFactionSafeCoordinates(Connection connection, int faction_id, double startX, double startY, double startZ, double endX, double endY, double endZ) {
        String query = "INSERT INTO Coordinates (start_x, start_y, start_z, end_x, end_y, end_z, faction_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, startX);
            statement.setDouble(2, startY);
            statement.setDouble(3, startZ);
            statement.setDouble(4, endX);
            statement.setDouble(5, endY);
            statement.setDouble(6, endZ);
            statement.setInt(7, faction_id);

            statement.executeUpdate();
            System.out.println("Coordinates inserted successfully.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update coordinates in the database
    public boolean updateCoordinates(Connection connection, int faction_id, int id, double startX, double startY, double startZ, double endX, double endY, double endZ) {
        String query = "UPDATE Coordinates SET start_x = ?, start_y = ?, start_z = ?, end_x = ?, end_y = ?, end_z = ?, faction_id = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, startX);
            statement.setDouble(2, startY);
            statement.setDouble(3, startZ);
            statement.setDouble(4, endX);
            statement.setDouble(5, endY);
            statement.setDouble(6, endZ);
            statement.setInt(7, faction_id);
            statement.setInt(8, id);

            statement.executeUpdate();
            System.out.println("Coordinates updated successfully.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve coordinates from the database based on the foreign key
    public boolean getCoordinatesByForeignKey(Connection connection, int faction_id) {
        String query = "SELECT * FROM Coordinates WHERE faction_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, faction_id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double startX = resultSet.getDouble("start_x");
                double startY = resultSet.getDouble("start_y");
                double startZ = resultSet.getDouble("start_z");
                double endX = resultSet.getDouble("end_x");
                double endY = resultSet.getDouble("end_y");
                double endZ = resultSet.getDouble("end_z");

                System.out.println("ID: " + id);
                System.out.println("Start X: " + startX);
                System.out.println("Start Y: " + startY);
                System.out.println("Start Z: " + startZ);
                System.out.println("End X: " + endX);
                System.out.println("End Y: " + endY);
                System.out.println("End Z: " + endZ);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete coordinates from the database based on the foreign key
    public boolean deleteCoordinatesByForeignKey(Connection connection, int faction_id) {
        String query = "DELETE FROM Coordinates WHERE faction_id = ?";

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
