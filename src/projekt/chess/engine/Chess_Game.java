package projekt.chess.engine;

import projekt.chess.board.Board;

import javax.swing.*;
import java.awt.*;

public class Chess_Game {

    private JTextArea chatArea;
    private JFrame startFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Chess_Game().startApplication());
    }

    private void startApplication() {
        startFrame = new JFrame("Start Game");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(300, 200);
        startFrame.setLocationRelativeTo(null);

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(120, 40));
        startButton.addActionListener(e -> {
            Start.startGame(); // Pass startFrame to startGame method
            startFrame.setVisible(false);
        });

        JButton loadSaveButton = new JButton("Load Save");
        loadSaveButton.setPreferredSize(new Dimension(120, 40));
        loadSaveButton.addActionListener(e -> loadSave());

        JButton loadGameButton = new JButton("Load Game");
        loadGameButton.setPreferredSize(new Dimension(120, 40));
        loadGameButton.addActionListener(e -> loadGame());

        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(120, 40));
        exitButton.addActionListener(e -> System.exit(0));

        startPanel.add(startButton);
        startPanel.add(loadGameButton);
        startPanel.add(loadSaveButton);
        startPanel.add(exitButton);

        startFrame.add(startPanel);
        startFrame.setVisible(true);
    }

    private void loadSave() {
        Board.loadSave(startFrame);
    }

    private void loadGame() {
        if (chatArea == null) {
            chatArea = new JTextArea();
            chatArea.setEditable(false);
        }
        GameLoader.loadGame(startFrame, chatArea);
    }
}
