package FinalProjectBeta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardLayoutFrameServer extends JFrame {
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

    private JPanel cardPanel;
    private JPanel welcomePanel;
    private JPanel loginPanel;
    private JPanel regPanel;
    private JPanel quizPickPanel;
    private JPanel gamePanel;
    private JPanel resultPanel;

    private JButton loginButton;
    private JButton regButton;
    private JButton saveLoginInfoButton;
    private JButton saveNewUserInfoButton;
    private JButton quizOneButton;
    private JButton quizTwoButton;
    private JButton restartButton;

    private JTextField loginUsernameField;
    private JTextField newUserUsernameField;

    private JPasswordField loginPasswordField;
    private JPasswordField newUserPasswordField;

    private GameServer gameServer;
    private CardLayout cardLayout;
    private JLabel questionLabel;
    private JLabel timerLabel;
    private JLabel correctAnswerLabel;
    private JLabel[] answerLabels = new JLabel[4];

    private Timer timer;
    private int time = 10;
    private int currentQuestionsIdx = 0;

    private HashMap<Integer, ArrayList<String>> questionPair;
    private List<Integer> correctAnswers;

    private String clientUsername;
    private int clientScore = 0;
    private ButtonHandler handler;

    public CardLayoutFrameServer (GameServer gameServer){
        this.gameServer = gameServer;
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createWelcomePanel();
        createLoginPanel();
        createRegistrationPanel();
        createQuizPickPanel();
        createGamePanel();
        questionTimer();


        handler = new ButtonHandler();

        loginButton.addActionListener(handler);
        regButton.addActionListener(handler);
        saveLoginInfoButton.addActionListener(handler);
        saveNewUserInfoButton.addActionListener(handler);
        quizOneButton.addActionListener(handler);
        quizTwoButton.addActionListener(handler);

        add(cardPanel);

        setTitle("Server App");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void createWelcomePanel(){
        welcomePanel = new JPanel(new BorderLayout());

        JPanel welcomeButtonsPanel = new JPanel(new GridLayout(1, 2, 20, 5));

        loginButton = new JButton("Log in");
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 25));
        welcomeButtonsPanel.add(loginButton);

        regButton = new JButton("Create new user");
        regButton.setFont(new Font("Tahoma", Font.BOLD, 25));
        welcomeButtonsPanel.add(regButton);


        JLabel title = new JLabel("Trivia Game Server", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 50));
        welcomePanel.add(title, BorderLayout.CENTER);
        welcomePanel.add(welcomeButtonsPanel, BorderLayout.SOUTH);

        welcomePanel.setBackground(new Color(229,252,164));

        cardPanel.add(welcomePanel, "W");
    }

    private void createLoginPanel(){
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
        bigPanel.add(saveLoginInfoButton);

        loginPanel.add(bigPanel, BorderLayout.NORTH);

        loginPanel.setBackground(new Color(229,252,164));

        cardPanel.add(loginPanel, "L");
    }
    private void createRegistrationPanel(){
        regPanel = new JPanel(new BorderLayout());

        JPanel bigPanel = new JPanel(new GridLayout(1, 2, 30, 10));
        JPanel infoButtonsPanel = new JPanel(new GridLayout(2, 1, 10, 40));

        newUserUsernameField = new JTextField("Enter your username.");
        newUserUsernameField.setFont(new Font("Tahoma", Font.PLAIN, 40));
        newUserPasswordField = new JPasswordField("password");
        newUserPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 40));

        infoButtonsPanel.add(newUserUsernameField);
        infoButtonsPanel.add(newUserPasswordField);
        bigPanel.add(infoButtonsPanel);

        saveNewUserInfoButton = new JButton("Create User");
        saveNewUserInfoButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
        bigPanel.add(saveNewUserInfoButton);

        regPanel.add(bigPanel, BorderLayout.NORTH);

        regPanel.setBackground(new Color(229,252,164));

        cardPanel.add(regPanel, "N");
    }
    private void createQuizPickPanel() {

        // 1. main panel creation
        quizPickPanel = new JPanel(new BorderLayout());

        // 2. title / top section
        JLabel title = new JLabel("Choose a Quiz", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 40));
        quizPickPanel.add(title, BorderLayout.NORTH);

        // 3. quiz 1
        JPanel quizOnePanel = new JPanel(new FlowLayout());
        JLabel quizOneLabel = new JLabel("Marine Animals Quiz");
        quizOneLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        quizOnePanel.add(quizOneLabel);

        quizOneButton = new JButton(fish);
        quizOneButton.setBackground(new Color(50, 172, 255));
        quizOnePanel.add(quizOneButton);

        // 4. quiz 2
        JPanel quizTwoPanel = new JPanel(new FlowLayout());

        JLabel quizTwoLabel = new JLabel("Solar System Quiz");
        quizTwoLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        quizTwoPanel.add(quizTwoLabel);

        quizTwoButton = new JButton(solarSystem);
        quizTwoButton.setBackground(new Color(0, 0, 0));
        quizTwoPanel.add(quizTwoButton);

        // 5. middle layout
        JPanel midPanel = new JPanel(new GridLayout(1, 2, 0, 10));
        midPanel.add(quizOnePanel);
        midPanel.add(quizTwoPanel);

        quizPickPanel.add(midPanel, BorderLayout.CENTER);

        JLabel prompt = new JLabel("Pick a category", SwingConstants.CENTER);
        prompt.setFont(new Font("Tahoma", Font.BOLD, 24));
        quizPickPanel.add(prompt, BorderLayout.SOUTH);

        // transparent panels
        midPanel.setOpaque(false);
        quizOnePanel.setOpaque(false);
        quizTwoPanel.setOpaque(false);

        quizPickPanel.setBackground(new Color(229, 252, 164));

        cardPanel.add(quizPickPanel, "Q");
    }

    private void createGamePanel() {

        gamePanel = new JPanel(new BorderLayout());
        questionLabel = new JLabel("Question", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 32));
        gamePanel.add(questionLabel, BorderLayout.NORTH);

        //buttonPanel
        JPanel answerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        for (int i = 0; i < 4; i++) {
            answerLabels[i] = new JLabel("Option " + (i+1));
            answerLabels[i].setFont(new Font("Tahoma", Font.BOLD, 24));
            answerLabels[i].setOpaque(true);
            answerLabels[i].setBackground(Color.WHITE);
            answerPanel.add(answerLabels[i]);
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
    private void createResultsPanel() {
        resultPanel = new JPanel(new BorderLayout());

        JLabel resultLabel = new JLabel("Results Screen", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 40));

        JLabel scoreLabel = new JLabel(
                clientUsername + "'s score is " + clientScore,
                SwingConstants.CENTER
        );
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 45));

        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 30));
        restartButton.addActionListener(handler);

        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(scoreLabel, BorderLayout.CENTER);
        resultPanel.add(restartButton, BorderLayout.SOUTH);

        cardPanel.add(resultPanel, "R");
    }
    public void setClientResults(String username, int score) {
        clientUsername = username;
        clientScore = score;
    }
    public void showResultsPanel() {
        createResultsPanel();
        cardLayout.show(cardPanel, "R");
    }
    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {
                gameServer.sendLogin("LOGIN");
                cardLayout.show(cardPanel, "L");
            } else if (e.getSource() == regButton) {
                gameServer.sendLogin("NEW");
                cardLayout.show(cardPanel, "N");
            } else if (e.getSource() == saveLoginInfoButton) {
                String message = gameServer.sendLoginInfo(
                        loginUsernameField.getText(),
                        new String(loginPasswordField.getPassword())
                );
                if (message.equalsIgnoreCase("USER NOT FOUND. Re-enter username and password.")) {
                    JOptionPane.showMessageDialog(
                            CardLayoutFrameServer.this,
                            "USER NOT FOUND. Re-enter username and password."
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            CardLayoutFrameServer.this,
                            "Welcome " + loginUsernameField.getText() + "! Game start!"
                    );

                    cardLayout.show(cardPanel, "Q");
                }

            } else if (e.getSource() == saveNewUserInfoButton) {
                String message = gameServer.sendLoginInfo(
                        newUserUsernameField.getText(),
                        new String(newUserPasswordField.getPassword())
                );

                if (message.equalsIgnoreCase("Username: " + newUserUsernameField.getText()
                        + " already taken. Please re-enter username and password: ")) {
                    JOptionPane.showMessageDialog(
                            CardLayoutFrameServer.this,
                            "USERNAME TAKEN. Re-enter username and password."
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            CardLayoutFrameServer.this,
                            "New user created! Welcome " + newUserUsernameField.getText() + "!"
                    );

                    cardLayout.show(cardPanel, "Q");
                }
            } else if (e.getSource() == quizOneButton) {
                importQuestions(1);
                loadNextQuestions();
                cardLayout.show(cardPanel, "G");

            } else if (e.getSource() == quizTwoButton) {
                importQuestions(2);
                loadNextQuestions();
                cardLayout.show(cardPanel, "G");

            } else if (e.getSource() == restartButton) {
                currentQuestionsIdx = 0;
                cardLayout.show(cardPanel, "W");
            }
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

    private void loadNextQuestions() {
        if (questionPair == null || currentQuestionsIdx >= questionPair.size()) {
            timer.stop();
            showResultsPanel();
            return;
        }

        time = 10;
        timerLabel.setText("" + time);
        correctAnswerLabel.setText("");

        for (int i = 0; i < 4; i++) {
            answerLabels[i].setBackground(Color.WHITE);
        }

        questionLabel.setText(questionPair.get(currentQuestionsIdx).get(0));

        for (int i = 0; i < 4; i++) {
            answerLabels[i].setText(questionPair.get(currentQuestionsIdx).get(i + 1));
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

                    answerLabels[correctIndex].setBackground(Color.GREEN);

                    correctAnswerLabel.setText(
                            "Correct Answer: " + answerLabels[correctIndex].getText()
                    );

                    Timer nextQuestionTimer = new Timer(2000, new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            currentQuestionsIdx++;
                            loadNextQuestions();
                        }
                    });

                    nextQuestionTimer.setRepeats(false);
                    nextQuestionTimer.start();
                }
            }
        });
    }
}
