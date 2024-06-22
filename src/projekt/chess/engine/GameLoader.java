package projekt.chess.engine;

import projekt.chess.board.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class GameLoader {

    public static void loadGame(JFrame parentFrame, JTextArea chatArea) {
        JFileChooser fileChooser = new JFileChooser();
        File defaultDirectory = new File("saves/Game");
        fileChooser.setCurrentDirectory(defaultDirectory);

        int returnVal = fileChooser.showOpenDialog(parentFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Board loadedBoard = Board.loadGame(file, chatArea);
            if (loadedBoard != null) {
                rebuildBoard(parentFrame, loadedBoard, chatArea);
            }
        }
    }

    public static void rebuildBoard(JFrame frame, Board loadedBoard, JTextArea chatArea) {
        SwingUtilities.invokeLater(() -> {
            frame.dispose();

            JFrame chessFrame = new JFrame("Chess Board");

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(loadedBoard, BorderLayout.CENTER);

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

            chessFrame.getRootPane().registerKeyboardAction(e -> Start.showPauseDialog(),
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

            loadedBoard.setChatArea(chatArea);
            loadedBoard.setTheChessPieces();
            Board.continueGame();
        });
    }
}
