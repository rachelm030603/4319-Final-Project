package FinalProjectBeta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardLayoutFrame extends JFrame {

    private JPanel cardPanel;
    private JPanel welcomePanel;
    private JPanel loginPanel;
    private JPanel newUserPanel;
    private JButton loginButton;
    private JButton newUserButton;
    private JButton saveLoginInfoButton;
    private JButton saveNewUserInfoButton;
    private CardLayout cardLayout;
    private GameClient1 gameClient1;
    private ButtonHandler buttonHandler;
    private JTextField loginUsernameField;
    private JTextField loginPasswordField;
    private JTextField newUserUsernameField;
    private JTextField newUserPasswordField;


    public CardLayoutFrame(GameClient1 gameClient1) {
        this.gameClient1 = gameClient1;
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createWelcomePanel();

        createLoginPanel();

        createNewUserPanel();

        buttonHandler = new ButtonHandler();
        loginButton.addActionListener(buttonHandler);
        newUserButton.addActionListener(buttonHandler);
        saveLoginInfoButton.addActionListener(buttonHandler);
        saveNewUserInfoButton.addActionListener(buttonHandler);

        add(cardPanel);
    }

    private void createWelcomePanel() {
        welcomePanel = new JPanel(new BorderLayout());

        JPanel welcomeButtonsPanel = new JPanel(new GridLayout(1, 2, 20, 5));

        loginButton = new JButton("Log in");
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 25));
        welcomeButtonsPanel.add(loginButton);

        newUserButton = new JButton("Create new user");
        newUserButton.setFont(new Font("Tahoma", Font.BOLD, 25));
        welcomeButtonsPanel.add(newUserButton);


        JLabel title = new JLabel("Trivia Game", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 50));
        welcomePanel.add(title, BorderLayout.CENTER);
        welcomePanel.add(welcomeButtonsPanel, BorderLayout.SOUTH);

        cardPanel.add(welcomePanel, "W");
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new BorderLayout());

        JPanel bigPanel = new JPanel(new GridLayout(1, 2, 30, 10));
        JPanel infoButtonsPanel = new JPanel(new GridLayout(2, 1, 10, 40));

        loginUsernameField = new JTextField("Enter your username.");
        loginUsernameField.setFont(new Font("Tahoma", Font.PLAIN, 40));
        loginPasswordField = new JTextField("Enter your password.");
        loginPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 40));

        infoButtonsPanel.add(loginUsernameField);
        infoButtonsPanel.add(loginPasswordField);
        bigPanel.add(infoButtonsPanel);

        saveLoginInfoButton = new JButton("Log in");
        saveLoginInfoButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
        bigPanel.add(saveLoginInfoButton);

        loginPanel.add(bigPanel, BorderLayout.NORTH);

        cardPanel.add(loginPanel, "L");
    }

    private void createNewUserPanel() {
        newUserPanel = new JPanel(new BorderLayout());

        JPanel bigPanel = new JPanel(new GridLayout(1, 2, 30, 10));
        JPanel infoButtonsPanel = new JPanel(new GridLayout(2, 1, 10, 40));

        newUserUsernameField = new JTextField("Enter your username.");
        newUserUsernameField.setFont(new Font("Tahoma", Font.PLAIN, 40));
        newUserPasswordField = new JTextField("Enter your password.");
        newUserPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 40));

        infoButtonsPanel.add(newUserUsernameField);
        infoButtonsPanel.add(newUserPasswordField);
        bigPanel.add(infoButtonsPanel);

        saveNewUserInfoButton = new JButton("Create User");
        saveNewUserInfoButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
        bigPanel.add(saveNewUserInfoButton);

        newUserPanel.add(bigPanel, BorderLayout.NORTH);

        cardPanel.add(newUserPanel, "N");
    }

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == loginButton) {
                gameClient1.sendLogin("LOGIN");
                cardLayout.show(cardPanel, "L");
            } else if( e.getSource() == newUserButton) {
                gameClient1.sendLogin("NEW");
                cardLayout.show(cardPanel, "N");
            } else if(e.getSource() == saveLoginInfoButton) {
                String message = null;
                message = gameClient1.sendLoginInfo(loginUsernameField.getText(), loginPasswordField.getText());

                if(message.equalsIgnoreCase("USER NOT FOUND. Re-enter username and password.")) {
                    JDialog popup = new JDialog(CardLayoutFrame.this, "Server Message", true);
                    popup.setSize(300, 150);
                    popup.setLocationRelativeTo(CardLayoutFrame.this);

                    JLabel errorMessage = new JLabel("<html>USER NOT FOUND. Re-enter username and password.<html>",
                            SwingConstants.CENTER);
                    errorMessage.setFont(new Font("Tahoma", Font.PLAIN, 20));

                    popup.add(errorMessage);

                    popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    popup.setVisible(true);
                } else {
                    JDialog popup = new JDialog(CardLayoutFrame.this, "Server Message", true);
                    popup.setSize(300, 150);
                    popup.setLocationRelativeTo(CardLayoutFrame.this);

                    JLabel successMessage = new JLabel("<html>Welcome <html>" + loginUsernameField.getText() +
                            "<html>! <html>" +
                            "<html>Your highest score is <html>" + gameClient1.readScore()
                            + "<html>. Game start! <html>", SwingConstants.CENTER);
                    successMessage.setFont(new Font("Tahoma", Font.PLAIN, 20));

                    popup.add(successMessage);

                    popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    popup.setVisible(true);
                }
            } else if(e.getSource() == saveNewUserInfoButton) {
                String message = null;
                message = gameClient1.sendLoginInfo(newUserUsernameField.getText(), newUserPasswordField.getText());

                if(message.equalsIgnoreCase("Username: " + newUserUsernameField.getText()
                        + " already taken. Please re-enter username and password: ")) {
                    JDialog popup = new JDialog(CardLayoutFrame.this, "Server Message", true);
                    popup.setSize(300, 150);
                    popup.setLocationRelativeTo(CardLayoutFrame.this);

                    JLabel errorMessage = new JLabel("<html>USERNAME TAKEN. Re-enter username and password.<html>",
                            SwingConstants.CENTER);
                    errorMessage.setFont(new Font("Tahoma", Font.PLAIN, 20));

                    popup.add(errorMessage);

                    popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    popup.setVisible(true);

                } else {
                    JDialog popup = new JDialog(CardLayoutFrame.this, "Server Message", true);
                    popup.setSize(300, 150);
                    popup.setLocationRelativeTo(CardLayoutFrame.this);

                    JLabel successMessage = new JLabel("<html>New user created! <html>"
                            + "<html>Welcome <html>" + newUserUsernameField.getText() + "<html>! <html>");
                    successMessage.setFont(new Font("Tahoma", Font.PLAIN, 20));

                    popup.add(successMessage);

                    popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    popup.setVisible(true);
                }
            }
        }
    }

}
