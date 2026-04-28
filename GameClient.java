package FinalProjectBeta;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    private static final String SERVER_HOST = "BigLaptop57";
    private static final int SERVER_PORT = 8081;

    public static void main(String[] args) {
        System.out.println("Connecting to the server...");

        /*
        CardLayoutFrame frame = new CardLayoutFrame();
        frame.setTitle("Trivia Game");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        */

        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            Scanner scanner = new Scanner(System.in);
            System.out.println("If logging in, type LOGIN.");
            System.out.println("If creating new user, type NEW.");
            String login = scanner.nextLine(); // Determines if the Server will create a new user or login

            //Repeats until valid input is entered.
            while(!login.equalsIgnoreCase("LOGIN") && !login.equalsIgnoreCase("NEW")) {
                System.out.println("Invalid input, please enter LOGIN if logging in or NEW if creating new user");
                login = scanner.nextLine();
            }

            if(login.equalsIgnoreCase("LOGIN")) {
                //Message displayed is Server is logging in
                System.out.println("Please enter your username and password below when prompted.");
            } else {
                //Message displayed if Server is creating a new user
                System.out.println("Please enter a username and password below when prompted.");
                System.out.println("If username IS NOT unique, you will be asked to re-enter it.");
            }

            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();
            //The order of the outputs determines the order the Server reads them
            //Server must be setup with this order in mind
            output.writeObject(login); //First
            output.writeObject(username); //Second
            output.writeObject(password); //Third

            //The following line of code is receiving the response from the server
            //The message varies depending on whether logging in or creating user, and again on if conditions for
            //method are correct (i.e. if you're creating a user and the username was NOT unique)
            String message = (String) input.readObject();

            //Activated in the case you're trying to login but either your username or password were incorrect
            //Essentially, it will have you keep sending the username and password to the Server until the Server finds
            //that both are correct, which logs you in
            while(message.equals("USER NOT FOUND. Re-enter username and password.")) {
                System.out.println("Server says: " + message);
                System.out.print("Re-enter your username: ");
                username = scanner.nextLine();
                System.out.print("Re-enter your password: ");
                password = scanner.nextLine();
                output.writeObject(username);
                output.writeObject(password);
                message = (String) input.readObject();
            }

            //Activated in the case you're trying to create a new User but the username is already taken
            //Essentially, it just tells you the username is taken and asks you to reinput your username to the Server
            //until you choose a username that isn't already taken, creating a new user
            while(message.equals("Username: " + username + " already taken. Please re-enter username and password: ")) {
                System.out.println("Server says: " + message);
                System.out.print("Re-enter your username: ");
                username = scanner.nextLine();
                System.out.print("Re-enter your password: ");
                password = scanner.nextLine();
                output.writeObject(username);
                output.writeObject(password);
                message = (String) input.readObject();
            }
            //This message prints the Server's response once you're either logged in or successfully created a new user
            System.out.println("Server says: " + message);
        } catch(IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
