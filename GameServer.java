package FinalProjectBeta;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GameServer {
    private static final int PORT = 8081;
    private static Connection connection;

    private static GameServer gameServer;
    private static ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;

    public static void main(String[] args) throws SQLException {
        System.out.println("Game server started...");
        connection = DBConnection.getConnection();
        gameServer = new GameServer();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket);

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            //Whether user is logging in
            String login = (String) inputStream.readObject();

            // Receive username from client
            String username = (String) inputStream.readObject();
            String password = (String) inputStream.readObject();

            //Condition plays if LOGIN was entered.
            if(login.equalsIgnoreCase("LOGIN")) {
                System.out.println("Login request from: " + username + " Password: " + password);
                User user = getUser(username, password);

                /*
                This while loop uses the fact that getUser will return a user with -1 score to prevent the server
                from moving on until an existing user is found.
                */
                while(user.getScore() < 0) {
                    String message = "USER NOT FOUND. Re-enter username and password.";
                    System.out.println("Login failed.");
                    outputStream.writeObject(message);
                    //Waits for Client to send new username and password in this order before moving on
                    username = (String) inputStream.readObject();
                    password = (String) inputStream.readObject();
                    System.out.println("Login request from: " + username + " Password: " + password);
                    user = getUser(username, password);//reruns getUser to see if the newly entered info is valid
                    //if the info is not valid, while loop will continue
                }
                String message = "Welcome, " + user.getUsername() + "! Your highest score: " + user.getScore() + "\n"
                        + "Game Start!";
                int score = user.getScore();
                outputStream.writeObject(message);
                outputStream.writeObject(score);

            } else {
                System.out.println("New user request from: " + username + " Password: " + password);
                User user = addUser(username, password);
                while(user.getScore() == -1) {
                    String message = "Username: " + username + " already taken. Please re-enter username and password: ";
                    outputStream.writeObject(message);
                    username = (String) inputStream.readObject();
                    password = (String) inputStream.readObject();
                    user = addUser(username, password);
                }

                String message = "New user " + user.getUsername() + " created. ";
                outputStream.writeObject(message);

            }
            gameServer.createQuiz(username);
            System.out.println("Response sent to client. Closing connection...");

        } catch(IOException e) {
            throw new RuntimeException(e);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static User getUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try {
            PreparedStatement selectStmt = connection.prepareStatement(sql);
            selectStmt.setString(1, username);
            selectStmt.setString(2, password);
            ResultSet rs = selectStmt.executeQuery();
            if(rs.next()) {
                int score = rs.getInt("score");
                System.out.println("Welcome back " + username + " (highest score=" + score + ")");
                return new User(username, score);
            } else {
                return new User(username, -1);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return new User(username, -1);
    }

    public static User checkUsernameAvailability(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            PreparedStatement selectStmt = connection.prepareStatement(sql);
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int score = rs.getInt("score");
                System.out.println("USERNAME ALREADY TAKEN");
                return new User(username, score);
            } else {
                return new User(username, -1);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return new User(username, -1);
    }

    public static User addUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        User user = checkUsernameAvailability(username);
        if(user.getScore() != -1) {
            return new User(username, -1);
        }
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            System.out.println("New user registration: " + username);
            return new User(username, 0);
        } catch(SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
        return new User(username, -1);
    }
    public void createQuiz(String username) {
        CardLayoutFrameServer frame = new CardLayoutFrameServer(gameServer, username);
        frame.setTitle("Quiz Game (Server)");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int receiveScore() {
        try {
            int clientScore = (int) inputStream.readInt();
            return clientScore;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateScore(String username, int score) {
        try {
            String sql = "UPDATE users SET score = ? WHERE username = ?";

            if(getScore(username) >= score) {
                return;
            }

            else {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, score);
                pstmt.setString(2, username);
                pstmt.executeUpdate();
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public int getScore(String username) {
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement selectStmt = connection.prepareStatement(sql);
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();

            if(rs.next()) {
                int score = rs.getInt("score");
                return score;
            }

            else {
                return 0;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String receiveUsername() {
        try {
            String clientUsername = (String) inputStream.readObject();
            return clientUsername;
        } catch(IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public String getLeaderboard() {
        StringBuilder leaderboard = new StringBuilder();

        leaderboard.append("Top 10 Scores\n");
        leaderboard.append("-------------------------\n");

        try {
            String sql = "SELECT username, score FROM users ORDER BY score DESC LIMIT 10";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            int rank = 1;

            while (rs.next()) {
                String username = rs.getString("username");
                int score = rs.getInt("score");

                leaderboard.append(rank)
                        .append(". ")
                        .append(username)
                        .append(" - ")
                        .append(score)
                        .append("\n");

                rank++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return leaderboard.toString();
    }
}