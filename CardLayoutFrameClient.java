package FinalProjectBeta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardLayoutFrameClient extends JFrame {

    private static Icon stopWatch = new ImageIcon(
            CardLayoutFrameServer.class.getResource("stopWatch.png"));
    private static Icon fish = new ImageIcon(
            CardLayoutFrameServer.class.getResource("fishy.png"));
    private static Icon thinkingGIF = new ImageIcon(
            CardLayoutFrameServer.class.getResource("batman_think_120.gif"));
    private static Icon solarSystem = new ImageIcon(
            CardLayoutFrameServer.class.getResource("solarSystem.png"));
    private static Icon fireworks = new ImageIcon(
            CardLayoutFrameServer.class.getResource("fireworks.gif"));

    private int time = 10;
    private int currentQuestionsIdx;
    private int score;

    private String username;

    private Timer timer;
    private boolean answered = false;

    private JPanel cardPanel;
    private JPanel welcomePanel;
    private JPanel loginPanel;
    private JPanel newUserPanel;
    private JPanel gamePanel;
    private JPanel resultPanel;

    private JLabel questionLabel;
    private JLabel timerLabel;
    private JLabel correctAnswerLabel;
    private JLabel[] answerLabels = new JLabel[4];

    private HashMap<Integer, ArrayList<String>> questionPair;
    private List<Integer> correctAnswers;

    private JButton loginButton;
    private JButton newUserButton;
    private JButton saveLoginInfoButton;
    private JButton saveNewUserInfoButton;
    private JButton[] optionButtons = new JButton[4];
    private ButtonHandler buttonHandler;

    private CardLayout cardLayout;

    private GameClient1 gameClient1;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField newUserUsernameField;
    private JPasswordField newUserPasswordField;


    public CardLayoutFrameClient(GameClient1 gameClient1) {
        this.gameClient1 = gameClient1;
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        currentQuestionsIdx = 0;
        score = 0;

        createWelcomePanel();

        createLoginPanel();

        createNewUserPanel();

        createGamePanel();

        questionTimer();

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
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        styleButton(loginButton,50,242,161,187);
        welcomeButtonsPanel.add(loginButton);

        newUserButton = new JButton("Create new user");
        newUserButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        styleButton(newUserButton,50,242,161,187);
        welcomeButtonsPanel.add(newUserButton);


        JLabel title = new JLabel("Trivia Game", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 50));
        welcomePanel.add(title, BorderLayout.CENTER);
        welcomePanel.add(welcomeButtonsPanel, BorderLayout.SOUTH);
        welcomePanel.setBackground(new Color(229, 252,164));

        cardPanel.add(welcomePanel, "W");
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new BorderLayout());

        JPanel bigPanel = new JPanel(new GridLayout(1, 2, 30, 10));
        JPanel infoButtonsPanel = new JPanel(new GridLayout(2, 1, 10, 40));

        loginUsernameField = new JTextField("Enter your username.");
        loginUsernameField.setFont(new Font("Tahoma", Font.PLAIN, 40));
        loginPasswordField = new JPasswordField("password");
        loginPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 40));

        infoButtonsPanel.add(loginUsernameField);
        infoButtonsPanel.add(loginPasswordField);
        bigPanel.add(infoButtonsPanel);

        saveLoginInfoButton = new JButton("Log in");
        saveLoginInfoButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
        styleButton(saveLoginInfoButton,50,242,161,187);
        bigPanel.add(saveLoginInfoButton);

        loginPanel.add(bigPanel, BorderLayout.NORTH);
        loginPanel.setBackground(new Color(229, 252,164));

        cardPanel.add(loginPanel, "L");
    }

    private void createNewUserPanel() {
        newUserPanel = new JPanel(new BorderLayout());

        JPanel bigPanel = new JPanel(new GridLayout(1, 2, 30, 10));
        bigPanel.setBackground(new Color(229, 252,164));
        JPanel infoButtonsPanel = new JPanel(new GridLayout(2, 1, 10, 40));
        infoButtonsPanel.setBackground(new Color(229, 252,164));

        newUserUsernameField = new JTextField("Enter your username.");
        newUserUsernameField.setFont(new Font("Tahoma", Font.PLAIN, 40));
        newUserPasswordField = new JPasswordField("password");
        newUserPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 40));

        infoButtonsPanel.add(newUserUsernameField);
        infoButtonsPanel.add(newUserPasswordField);
        bigPanel.add(infoButtonsPanel);

        saveNewUserInfoButton = new JButton("Create User");
        saveNewUserInfoButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
        styleButton(saveNewUserInfoButton,50,242,161,187);
        bigPanel.add(saveNewUserInfoButton);

        newUserPanel.add(bigPanel, BorderLayout.NORTH);
        newUserPanel.setBackground(new Color(229, 252,164));

        cardPanel.add(newUserPanel, "N");
    }

    private void createGamePanel() {

        gamePanel = new JPanel(new BorderLayout());
        questionLabel = new JLabel("Question", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 32));
        gamePanel.add(questionLabel, BorderLayout.NORTH);

        //buttonPanel
        JPanel answerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton("Option " + (i+1));
            optionButtons[i].addActionListener(new OptionButtonHandler(i + 1));
            optionButtons[i].setOpaque(true);
            styleButton(optionButtons[i], 20,242,161,187);
            answerPanel.add(optionButtons[i]);
        }
        //timerPanel
        JPanel timerPanel = new JPanel(new FlowLayout());

        timerLabel= new JLabel(""+ time,stopWatch,SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial",Font.BOLD,40));
        timerLabel.setForeground(new Color(168,41,64));
        timerLabel.setHorizontalTextPosition(JLabel.CENTER);
        timerLabel.setVerticalTextPosition(JLabel.CENTER);

        correctAnswerLabel = new JLabel("" + SwingConstants.CENTER);
        correctAnswerLabel.setFont(new Font("Tahoma",Font.BOLD,24));


        timerPanel.add(timerLabel);
        timerPanel.add(correctAnswerLabel);
        JPanel middlePanel = new JPanel(new GridLayout(2,1,0,0));
        middlePanel.add(timerPanel);
        middlePanel.add(answerPanel);

        gamePanel.add(middlePanel,BorderLayout.CENTER);
        //set extra panels clear
        middlePanel.setOpaque(false);
        timerPanel.setOpaque(false);
        answerPanel.setOpaque(false);

        gamePanel.setBackground(new Color(229, 252,164));
        cardPanel.add(gamePanel, "G");
    }

    public void createResultsPanel() {
        resultPanel = new JPanel(new BorderLayout());
        JLabel resultLabel = new JLabel("Results Screen", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 40));

        JLabel scoreLabel = new JLabel(
                username + "'s score is " + score,
                SwingConstants.CENTER
        );
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 45));

        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(scoreLabel, BorderLayout.CENTER);

        cardPanel.add(resultPanel, "R");
    }

    public void showResultsPanel() {
        createResultsPanel();
        cardLayout.show(cardPanel, "R");
    }

    private void quizSelect(String serverQuiz) {
        if(serverQuiz.equalsIgnoreCase("Q1")) {
            importQuestions(1);
            loadNextQuestions();
            cardLayout.show(cardPanel, "G");

            startServerListener();
        }
        else if(serverQuiz.equalsIgnoreCase("Q2")) {
            importQuestions(2);
            loadNextQuestions();
            cardLayout.show(cardPanel, "G");

            startServerListener();
        }
    }

    private void importQuestions(int index) {
        QuestionLoader questionLoader = new QuestionLoader();
        questionPair = questionLoader.readQuestions(index);

        correctAnswers = new ArrayList<>();

        if (index == 1) {
            correctAnswers.add(2);
            correctAnswers.add(1);
            correctAnswers.add(1);
            correctAnswers.add(4);
            correctAnswers.add(3);
            correctAnswers.add(2);
            correctAnswers.add(1);
            correctAnswers.add(4);
            correctAnswers.add(3);
            correctAnswers.add(3);
        } else if (index == 2) {
            correctAnswers.add(4);
            correctAnswers.add(1);
            correctAnswers.add(3);
            correctAnswers.add(2);
            correctAnswers.add(2);
            correctAnswers.add(1);
            correctAnswers.add(3);
            correctAnswers.add(4);
            correctAnswers.add(1);
            correctAnswers.add(2);
        }
    }

    private void serverNextQuestion() {
        String message = gameClient1.receiveMessage();
        if(message.equalsIgnoreCase("Next Question")) {
            currentQuestionsIdx++;
            loadNextQuestions();
        } else if(message.equalsIgnoreCase("Game Over")) {
            timer.stop();
            gameClient1.sendScore(score);
            showResultsPanel();
        }

        else {
            JDialog popup = new JDialog(CardLayoutFrameClient.this, "Server Message", true);
            popup.setSize(300, 150);
            popup.setLocationRelativeTo(CardLayoutFrameClient.this);

            JLabel errorMessage = new JLabel("<html>MISSING SERVER INPUT<html>");
            errorMessage.setFont(new Font("Tahoma", Font.PLAIN, 20));

            popup.add(errorMessage);

            popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            popup.setVisible(true);
        }
    }

    private void loadNextQuestions() {
        if (questionPair == null || currentQuestionsIdx >= questionPair.size()) {
            timer.stop();
            gameClient1.sendScore(score);
            showResultsPanel();
            return;
        }

        time = 10;
        timerLabel.setText("" + time);
        correctAnswerLabel.setText("");
        answered=false;

        for(int i = 0; i < 4; i++) {
            styleButton(optionButtons[i], 20,242,161,187);
        }

        questionLabel.setText(questionPair.get(currentQuestionsIdx).get(0));

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(
                    questionPair.get(currentQuestionsIdx).get(i + 1));
        }

        timer.start();
    }

    private void questionTimer() {
        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                time--;
                timerLabel.setText("" + time);

                if (time == 0) {
                    timer.stop();

                    int correctIndex = correctAnswers.get(currentQuestionsIdx) - 1;

                    optionButtons[correctIndex].setBackground(Color.GREEN);

                    correctAnswerLabel.setText(
                            "Correct Answer: " + optionButtons[correctIndex].getText()
                    );
                }
            }
        });
    }

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == loginButton) {
                gameClient1.sendLogin("LOGIN");
                cardLayout.show(cardPanel, "L");
            }

            else if( e.getSource() == newUserButton) {
                gameClient1.sendLogin("NEW");
                cardLayout.show(cardPanel, "N");
            }

            else if(e.getSource() == saveLoginInfoButton) {
                String message = null;
                message = gameClient1.sendLoginInfo(loginUsernameField.getText(),
                        new String(loginPasswordField.getPassword()));

                if(message.equalsIgnoreCase("USER NOT FOUND. Re-enter username and password.")) {
                    JDialog popup = new JDialog(CardLayoutFrameClient.this, "Server Message", true);
                    popup.setSize(300, 150);
                    popup.setLocationRelativeTo(CardLayoutFrameClient.this);

                    JLabel errorMessage = new JLabel("<html>USER NOT FOUND. Re-enter username and password.<html>",
                            SwingConstants.CENTER);
                    errorMessage.setFont(new Font("Tahoma", Font.PLAIN, 20));

                    popup.add(errorMessage);

                    popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    popup.setVisible(true);
                } else {
                    username = loginUsernameField.getText();
                    JDialog popup = new JDialog(CardLayoutFrameClient.this, "Server Message", true);
                    popup.setSize(300, 150);
                    popup.setLocationRelativeTo(CardLayoutFrameClient.this);

                    JLabel successMessage = new JLabel("<html>Welcome <html>" + loginUsernameField.getText() +
                            "<html>! <html>" +
                            "<html>Your highest score is <html>" + gameClient1.readScore()
                            + "<html>. Game start! <html>", SwingConstants.CENTER);
                    successMessage.setFont(new Font("Tahoma", Font.PLAIN, 20));

                    popup.add(successMessage);

                    popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    popup.setVisible(true);
                    message = gameClient1.receiveMessage();
                    quizSelect(message);
                }
            }

            else if(e.getSource() == saveNewUserInfoButton) {
                String message = null;
                message = gameClient1.sendLoginInfo(newUserUsernameField.getText(),
                        new String(newUserPasswordField.getPassword()));

                if(message.equalsIgnoreCase("Username: " + newUserUsernameField.getText()
                        + " already taken. Please re-enter username and password: ")) {
                    JDialog popup = new JDialog(CardLayoutFrameClient.this, "Server Message", true);
                    popup.setSize(300, 150);
                    popup.setLocationRelativeTo(CardLayoutFrameClient.this);

                    JLabel errorMessage = new JLabel("<html>USERNAME TAKEN. Re-enter username and password.<html>",
                            SwingConstants.CENTER);
                    errorMessage.setFont(new Font("Tahoma", Font.PLAIN, 20));

                    popup.add(errorMessage);

                    popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    popup.setVisible(true);

                } else {
                    username = newUserUsernameField.getText();
                    JDialog popup = new JDialog(CardLayoutFrameClient.this, "Server Message", true);
                    popup.setSize(300, 150);
                    popup.setLocationRelativeTo(CardLayoutFrameClient.this);

                    JLabel successMessage = new JLabel("<html>New user created! <html>"
                            + "<html>Welcome <html>" + newUserUsernameField.getText() + "<html>! <html>");
                    successMessage.setFont(new Font("Tahoma", Font.PLAIN, 20));

                    popup.add(successMessage);

                    popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    popup.setVisible(true);
                    message = gameClient1.receiveMessage();
                    quizSelect(message);
                }
            }
        }
    }

    private class OptionButtonHandler implements ActionListener {
        private int index;

        public OptionButtonHandler(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            answered=true;

            //check answer
            if(index == correctAnswers.get(currentQuestionsIdx)){
                int correctIndex = correctAnswers.get(currentQuestionsIdx) - 1;

                optionButtons[correctIndex].setBackground(Color.GREEN);

                correctAnswerLabel.setText(
                        "Correct Answer: " + optionButtons[correctIndex].getText()
                );
                score = score + 10;
            }else{

                int correctIndex = correctAnswers.get(currentQuestionsIdx) - 1;

                optionButtons[correctIndex].setBackground(Color.GREEN);
                optionButtons[index - 1].setBackground(Color.RED);

                correctAnswerLabel.setText(
                        "Correct Answer: " + optionButtons[correctIndex].getText()
                );
            }
        }
    }

    private void styleButton(JButton button, int size ,int r, int g, int b){
        button.setFont(new Font ("Tahoma",Font.BOLD, size));
        button.setBackground(new Color(r,g,b));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
    private void startServerListener() {

        Thread listenerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    String message = gameClient1.receiveMessage();

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            if (message.equalsIgnoreCase("Next Question")) {

                                currentQuestionsIdx++;
                                loadNextQuestions();

                            } else if (message.equalsIgnoreCase("Game Over")) {

                                timer.stop();
                                gameClient1.sendScore(score);
                                showResultsPanel();

                                return;
                            }
                        }
                    });
                }
            }
        });

        listenerThread.start();
    }

}
