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
            CardLayoutFrameServer.class.getResource("fish.png"));
    private static Icon thinkingGIF = new ImageIcon(
            CardLayoutFrameServer.class.getResource("batman_think_120.gif"));
    private static Icon solarSystem = new ImageIcon(
            CardLayoutFrameServer.class.getResource("solarSystem.png"));
    private static Icon fireworks = new ImageIcon(
            CardLayoutFrameServer.class.getResource("Fire_Night_Sticker_by_haenaillust.gif"));

    private CardLayout cardLayout;

    private Timer timer;

    private int time = 10;
    private int clientScore;
    private int currentQuestionsIdx;

    private String clientUsername;

    private JPanel cardPanel;
    private JPanel quizSelectPanel;
    private JPanel gamePanel;
    private JPanel resultPanel;

    private JButton quizOneButton;
    private JButton quizTwoButton;
    private JButton restartButton;
    private ButtonHandler handler;

    private JLabel questionLabel;
    private JLabel timerLabel;
    private JLabel correctAnswerLabel;
    private JLabel[] answerLabels = new JLabel[4];

    private HashMap<Integer, ArrayList<String>> questionPair;
    private List<Integer> correctAnswers;

    private GameServer gameServer;

    public CardLayoutFrameServer(GameServer gameServer, String clientUsername) {
        this.gameServer = gameServer;
        this.clientUsername = clientUsername;
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        currentQuestionsIdx = 0;

        createQuizSelectPanel();
        createGamePanel();
        questionTimer();

        handler = new ButtonHandler();
        quizOneButton.addActionListener(handler);
        quizTwoButton.addActionListener(handler);


        add(cardPanel);
    }

    public void createQuizSelectPanel() {
        quizSelectPanel = new JPanel(new BorderLayout());

        //quiz 1
        JPanel quizOnePanel = new JPanel(new FlowLayout());
        JLabel quizOneLabel = new JLabel("Marine Animals Quiz");
        quizOneLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        quizOnePanel.add(quizOneLabel);
        quizOneButton = new JButton(fish);
        //no button background
        quizOneButton.setBackground(new Color(50,172,255));
        quizOnePanel.add(quizOneButton);

        //quiz2
        JPanel quizTwoPanel = new JPanel(new FlowLayout());
        JLabel quizTwoLabel = new JLabel("Solar System Quiz");
        quizTwoLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        quizTwoPanel.add(quizTwoLabel);
        quizTwoButton = new JButton(solarSystem);
        //no button background
        quizTwoButton.setBackground(new Color(0,0,0));
        quizTwoPanel.add(quizTwoButton);

        JPanel midPanel = new JPanel(new GridLayout(1,2,0,10));
        midPanel.add(quizOnePanel);
        midPanel.add(quizTwoPanel);

        quizSelectPanel.add(midPanel,BorderLayout.CENTER);
        JLabel prompt = new JLabel("Pick a category",SwingConstants.CENTER);
        prompt.setFont(new Font("Tahoma", Font.BOLD, 24));
        quizSelectPanel.add(prompt,BorderLayout.SOUTH);
        //set panels clear
        midPanel.setOpaque(false);
        quizOnePanel.setOpaque(false);
        quizTwoPanel.setOpaque(false);

        quizSelectPanel.setBackground(new Color(229, 252,164));
        cardPanel.add(quizSelectPanel, "U");
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

    public void showResultsPanel() {
        createResultsPanel();
        cardLayout.show(cardPanel, "R");
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
                            gameServer.sendMessage("Next Question");
                        }
                    });

                    nextQuestionTimer.setRepeats(false);
                    nextQuestionTimer.start();
                }
            }
        });
    }

    private void loadNextQuestions() {
        if (questionPair == null || currentQuestionsIdx >= questionPair.size()) {
            timer.stop();
            gameServer.sendMessage("Game Over");
            clientScore = gameServer.receiveScore();
            gameServer.updateScore(clientUsername, clientScore);
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

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == quizOneButton) {
                importQuestions(1);
                loadNextQuestions();
                cardLayout.show(cardPanel, "G");
                gameServer.sendMessage("Q1");
            }

            else if(e.getSource() == quizTwoButton) {
                importQuestions(2);
                loadNextQuestions();
                cardLayout.show(cardPanel, "G");
                gameServer.sendMessage("Q2");
            }
        }
    }
}
