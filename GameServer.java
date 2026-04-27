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

    private ObjectOutputStream output;
    private ObjectInputStream input;

    public static void main(String[] args) throws SQLException {
        System.out.println("Game server started...");
        connection = DBConnection.getConnection();

        gameServer = new GameServer();

        JFrame waitingFrame = new JFrame("Server Waiting");
        JLabel waitingLabel = new JLabel("Waiting for client connection ..");

        waitingFrame.add(waitingLabel);
        waitingFrame.setSize(600, 300);
        waitingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        waitingFrame.setVisible(true);

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket);

            gameServer.output = new ObjectOutputStream(socket.getOutputStream());
            gameServer.input = new ObjectInputStream(socket.getInputStream());

            waitingFrame.dispose();

            gameServer.CreateQuiz();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public void sendLogin(String login) {
        try {
            output.writeObject(login);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String sendLoginInfo(String username, String password) {
        try {
            output.writeObject(username);
            output.writeObject(password);
            output.flush();

            String message = (String) input.readObject();

            return message;
        } catch(IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void CreateQuiz() {
        CardLayoutFrameServer frame = new CardLayoutFrameServer(gameServer);
        frame.setTitle("Trivia Game");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
