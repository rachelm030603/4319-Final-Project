package FinalProjectRachel2;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameClient1 {
    private static final String SERVER_HOST = "localhost";
    private static final int PORT = 8081;

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;
    private static GameClient1 gameClient1;

    public static void main(String[] args) {
        gameClient1 = new GameClient1();
        gameClient1.ConnectToServer();
        gameClient1.CreateQuiz();
    }

    public void ConnectToServer() {
        try {
            socket = new Socket(SERVER_HOST, PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server.");

        } catch (IOException e) {
            System.out.println("Failed to connect to Server.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void CreateQuiz() {
        CardLayoutFrameClient frame = new CardLayoutFrameClient(gameClient1);
        frame.setTitle("Quiz Game (Client)");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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

    public String receiveMessage() {
        String message = null;
        try {
            message = (String) input.readObject();
            return message;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int readScore() {
        try {
            int score = (int) input.readObject();
            return score;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendScore(int score) {
        try {
            output.writeInt(score);
            output.flush();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

}