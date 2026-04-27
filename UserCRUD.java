package FinalProjectBeta;

import java.sql.*;

public class UserCRUD {
    private Connection connection;

    public UserCRUD(Connection connection) {
        this.connection = connection;
    }

    public void listUsers() {
        String sql = "SELECT * FROM users";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Current users:");
            while(rs.next()) {
                System.out.println(rs.getInt("id") + " | " + rs.getString("username"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String username) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            System.out.println("User added: " + username);
        } catch(SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addUserAtID(int id, String username) {
        String sql = "INSERT INTO users(id, username, password) VALUES(?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(2, username);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("User added! Index: " + id + "Username: " + username);
        } catch(SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateUser(int id, String newUsername) {
        String sql = "UPDATE users SET username = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newUsername);
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "User updated!" : "User not found.");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM users where id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "User deleted." : "User not found.");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
