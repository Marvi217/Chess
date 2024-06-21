package projekt.chess.engine;

import projekt.chess.board.Board;

import javax.swing.*;
import java.awt.*;

public class Start {
    private static Timer swingTimer;
    private static JLabel timerLabelWhite;
    private static JLabel timerLabelBlack;
    public static boolean isWhiteTurn = true;
    private static ChessTimer timerWhite;
    private static ChessTimer timerBlack;

    public static void main(String[] args) {
        JFrame startFrame = new JFrame("Start Game");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(300, 150);
        startFrame.setLocationRelativeTo(null);

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(120, 40));
        startButton.addActionListener(e -> {
            startGame();
            startFrame.setVisible(false);
        });
        startPanel.add(startButton);

        startFrame.add(startPanel);
        startFrame.setVisible(true);
    }

    private static void startGame() {
        JFrame frame = new JFrame("Chess Board");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        Board chessBoard = new Board();
        mainPanel.add(chessBoard, BorderLayout.CENTER);

        JPanel timerAndChatPanel = new JPanel();
        timerAndChatPanel.setLayout(new BorderLayout());

        JPanel timersPanel = new JPanel();
        timersPanel.setLayout(new GridLayout(1, 2, 10, 0));

        JPanel playerWhitePanel = new JPanel();
        playerWhitePanel.setBackground(Color.WHITE);
        playerWhitePanel.setPreferredSize(new Dimension(200, 40));
        playerWhitePanel.setLayout(new BorderLayout());

        timerLabelWhite = new JLabel("15:00");
        timerLabelWhite.setHorizontalAlignment(SwingConstants.CENTER);
        playerWhitePanel.add(timerLabelWhite, BorderLayout.CENTER);

        timersPanel.add(playerWhitePanel);

        JPanel playerBlackPanel = new JPanel();
        playerBlackPanel.setBackground(Color.BLACK);
        playerBlackPanel.setPreferredSize(new Dimension(200, 40));
        playerBlackPanel.setLayout(new BorderLayout());

        timerLabelBlack = new JLabel("15:00");
        timerLabelBlack.setHorizontalAlignment(SwingConstants.CENTER);
        playerBlackPanel.add(timerLabelBlack, BorderLayout.CENTER);

        timersPanel.add(playerBlackPanel);

        timerAndChatPanel.add(timersPanel, BorderLayout.NORTH);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setPreferredSize(new Dimension(400, 150));
        timerAndChatPanel.add(chatScrollPane, BorderLayout.CENTER);

        mainPanel.add(timerAndChatPanel, BorderLayout.LINE_END);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1150, 800);
        frame.setVisible(true);

        timerWhite = new ChessTimer(15, 0);
        timerBlack = new ChessTimer(15, 0);

        swingTimer = new Timer(1000, e -> {
            if (isWhiteTurn) {
                timerWhite.decrementTime();
                timerLabelWhite.setText(String.format("%02d:%02d", timerWhite.getMinutes(), timerWhite.getSeconds()));
                if (timerWhite.getMinutes() == 0 && timerWhite.getSeconds() == 0) {
                    JOptionPane.showMessageDialog(frame, "Czas białego gracza minął. Czarny wygrywa!");
                    swingTimer.stop();
                }
            } else {
                timerBlack.decrementTime();
                timerLabelBlack.setText(String.format("%02d:%02d", timerBlack.getMinutes(), timerBlack.getSeconds()));
                if (timerBlack.getMinutes() == 0 && timerBlack.getSeconds() == 0) {
                    JOptionPane.showMessageDialog(frame, "Czas czarnego gracza minął. Biały wygrywa!");
                    swingTimer.stop();
                }
            }
        });

        swingTimer.start();

        chessBoard.setChatArea(chatArea);
    }
}
