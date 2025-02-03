package assignment3;

import java.awt.*;
import java.sql.*;

public class DatabaseHandler {
    public Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost/tron_game?serverTimezone=UTC&user=root&password=toot");
        } catch (Exception e) {
            System.out.println("Database connection error: " + e);
            return null;
        }
    }

    public boolean playerExists(String name) {
        String query = "SELECT COUNT(*) FROM tron_game WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking player: " + e);
        }
        return false;
    }

    public void addPlayer(String name, Color color) {
        if (playerExists(name)) {
            updatePlayerColor(name, color);
        } else {
            String query = "INSERT INTO tron_game (name, red, green, blue, score) VALUES (?, ?, ?, ?, 0)";
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setInt(2, color.getRed());
                stmt.setInt(3, color.getGreen());
                stmt.setInt(4, color.getBlue());
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error adding player: " + e);
            }
        }
    }

    private void updatePlayerColor(String name, Color color) {
        String query = "UPDATE tron_game SET red = ?, green = ?, blue = ? WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, color.getRed());
            stmt.setInt(2, color.getGreen());
            stmt.setInt(3, color.getBlue());
            stmt.setString(4, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating player color: " + e);
        }
    }

    public void updateScore(String name) {
    String query = "UPDATE tron_game SET score = score + 1 WHERE name = ?";
    try (Connection conn = connect();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, name);
        stmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Error updating score: " + e);
    }
}


    public String getTopScores() {
    StringBuilder leaderboard = new StringBuilder("Top 10 Players:\n");
    String query = "SELECT name, score FROM tron_game ORDER BY score DESC LIMIT 10";
    try (Connection conn = connect();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        int rank = 1;
        while (rs.next()) {
            leaderboard.append(rank++)
                      .append(". ")
                      .append(rs.getString("name"))
                      .append(" - ")
                      .append(rs.getInt("score"))
                      .append("\n");
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving top scores: " + e);
    }
    return leaderboard.toString();
}

}
