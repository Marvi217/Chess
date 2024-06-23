package chess.engine;

import chess.board.Board;

import javax.swing.*;
import java.awt.*;

public class Chess_Game {
    private static JTextArea chatArea;
    private static JFrame startFrame;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Chess_Game::startApplication);
    }
    static void startApplication() {
        startFrame = new JFrame("Start Game");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(300, 200);
        startFrame.setLocationRelativeTo(null);

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton startButton = new JButton("Nowa gra");
        startButton.setPreferredSize(new Dimension(120, 40));
        startButton.addActionListener(e -> {
            GameGui.startGame();
            startFrame.setVisible(false);
        });

        JButton loadSaveButton = new JButton("Wczytaj zapis");
        loadSaveButton.setPreferredSize(new Dimension(120, 40));
        loadSaveButton.addActionListener(e -> loadSave());

        JButton loadGameButton = new JButton("Wczytaj grę");
        loadGameButton.setPreferredSize(new Dimension(120, 40));
        loadGameButton.addActionListener(e -> loadGame());

        JButton exitButton = new JButton("Wyjdź");
        exitButton.setPreferredSize(new Dimension(120, 40));
        exitButton.addActionListener(e -> System.exit(0));

        startPanel.add(startButton);
        startPanel.add(loadGameButton);
        startPanel.add(loadSaveButton);
        startPanel.add(exitButton);

        startFrame.add(startPanel);
        startFrame.setVisible(true);
    }
    private static void loadSave() {
        Board.loadSave(startFrame);
    }
    private static void loadGame() {
        if (chatArea == null) {
            chatArea = new JTextArea();
            chatArea.setEditable(false);
        }
        GameLoader.loadGame(startFrame, chatArea);
    }
}
