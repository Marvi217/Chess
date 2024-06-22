package projekt.chess.engine;

import projekt.chess.board.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Start {
    private static JFrame chessFrame;
    private static Board chessBoard;
     static void startGame() {
        chessFrame = new JFrame("Chess Board");

        JPanel mainPanel = new JPanel(new BorderLayout());
        chessBoard = new Board();
        mainPanel.add(chessBoard, BorderLayout.CENTER);

        JPanel timerAndChatPanel = new JPanel(new BorderLayout());

        JPanel timersPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JPanel playerWhitePanel = new JPanel(new BorderLayout());
        playerWhitePanel.setBackground(Color.WHITE);
        playerWhitePanel.setPreferredSize(new Dimension(200, 40));
        timersPanel.add(playerWhitePanel);

        JPanel playerBlackPanel = new JPanel(new BorderLayout());
        playerBlackPanel.setBackground(Color.BLACK);
        playerBlackPanel.setPreferredSize(new Dimension(200, 40));
        timersPanel.add(playerBlackPanel);

        timerAndChatPanel.add(timersPanel, BorderLayout.NORTH);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setPreferredSize(new Dimension(400, 150));
        timerAndChatPanel.add(chatScrollPane, BorderLayout.CENTER);

        mainPanel.add(timerAndChatPanel, BorderLayout.LINE_END);

        chessFrame.add(mainPanel);
        chessFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chessFrame.pack();
        chessFrame.setSize(1150, 800);
        chessFrame.setLocationRelativeTo(null);
        chessFrame.setVisible(true);

        chessFrame.getRootPane().registerKeyboardAction(e -> showPauseDialog(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        if (chessBoard != null) {
            chessBoard.setChatArea(chatArea);
        }
    }
     static void showPauseDialog() {
        int option = JOptionPane.showOptionDialog(chessFrame, "Game is paused. What do you want to do?", "Pause", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Continue", "Save Game"}, "Continue");

        if (option == JOptionPane.NO_OPTION) {
            saveGame();
            System.exit(0);
        }
    }
    private static void saveGame() {
        if (chessBoard != null) {
            chessBoard.saveGame();
            chessFrame.dispose();

        }
    }
}
