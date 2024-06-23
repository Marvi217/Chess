package chess.board;
import chess.exception.InvalidMoveException;
import chess.exception.TurnException;
import chess.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Board extends JComponent implements Serializable, Saveable {
    private boolean isWhiteTurn = true;
    public  final String BLACK = "black";
    public  final String WHITE = "white";
    private final Piece[][] board = new Piece[8][8];
    private int selectedRow = -1;
    private int selectedCol = -1;
    private JTextArea chatArea;
    private boolean gameover = false;

    public Board() {
        initialize();
        setupMouseListener();
    }
    public static void continueGame(Board board) {
        board.setupMouseListener();
    }

    public void setChatArea(JTextArea chatArea) {
        this.chatArea = chatArea;
    }

    private void initialize() {
        clearBoard();
        setupPiecePosition();
        setTheChessPieces();
    }

    private void clearBoard() {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                board[x][y] = null;
            }
        }
    }

    private void setupPiecePosition() {
        for (int x = 0; x < 8; x++) {
            board[1][x] = new Pawn(BLACK);
        }
        for (int x = 0; x < 8; x++) {
            board[6][x] = new Pawn(WHITE);
        }

        board[0][0] = new Rook(BLACK);
        board[0][7] = new Rook(BLACK);
        board[7][7] = new Rook(WHITE);
        board[7][0] = new Rook(WHITE);

        board[0][1] = new Knight(BLACK);
        board[0][6] = new Knight(BLACK);
        board[7][6] = new Knight(WHITE);
        board[7][1] = new Knight(WHITE);

        board[0][2] = new Bishop(BLACK);
        board[0][5] = new Bishop(BLACK);
        board[7][2] = new Bishop(WHITE);
        board[7][5] = new Bishop(WHITE);

        board[0][3] = new Queen(BLACK);
        board[7][3] = new Queen(WHITE);

        board[0][4] = new King(BLACK);
        board[7][4] = new King(WHITE);
    }

    public void setTheChessPieces() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    piece.setPosition(row, col);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tileSize = getSize().width / 8;
        drawTiles(g, tileSize);
        drawValidMoves(g, tileSize);
        drawSelectedTiles(g, tileSize);
        drawPieces(g, tileSize);
    }

    public void drawValidMoves(Graphics g, int tileSize) {
        if (selectedRow != -1 && selectedCol != -1) {
            Piece selectedPiece = board[selectedRow][selectedCol];
            if (selectedPiece != null) {
                selectedPiece.calculateValidMoves(board, selectedRow, selectedCol);
                List<int[]> validMoves = selectedPiece.getValidMoves();
                selectedPiece.calculateThreatenedMoves(board, selectedRow, selectedCol);
                List<int[]> isThreatened = selectedPiece.getThreatenedMoves();

                for (int[] move : validMoves) {
                    int row = move[0];
                    int col = move[1];

                    Color color;
                    if (containsMove(isThreatened, move)) {
                        color = new Color(255, 124, 17, 120);
                    } else {
                        color = new Color(0, 255, 0, 120);
                    }
                    g.setColor(color);
                    g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    public void drawSelectedTiles(Graphics g, int tileSize) {
        if (selectedRow != -1 && selectedCol != -1 && board[selectedRow][selectedCol] != null) {
            Color transparentBlue = new Color(0, 0, 255, 128);
            g.setColor(transparentBlue);
            g.fillRect(selectedCol * tileSize, selectedRow * tileSize, tileSize - 1, tileSize - 1);
        }
    }

    public void drawPieces(Graphics g, int tileSize) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] != null) {
                    Piece piece = board[row][col];
                    String imagePath = piece.getImagePath();
                    try {
                        Image pieceImage = new ImageIcon(imagePath).getImage();
                        g.drawImage(pieceImage, col * tileSize, row * tileSize, tileSize, tileSize, this);
                    } catch (Exception e) {
                        System.out.println("Could not load image: " + imagePath);
                    }
                }
            }
        }
    }

    public void drawTiles(Graphics g, int tileSize) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boolean isWhite = (row + col) % 2 == 0;
                Color tileColor = isWhite ? Color.LIGHT_GRAY : Color.DARK_GRAY;
                g.setColor(tileColor);
                g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
    }

    private boolean containsMove(List<int[]> moves, int[] move) {
        for (int[] m : moves) {
            if (m[0] == move[0] && m[1] == move[1]) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                if(gameover){
                    return;
                }
                int tileSize = getSize().width / 8;
                int row = mouseEvent.getY() / tileSize;
                int col = mouseEvent.getX() / tileSize;

                if (Board.isValidPosition(row, col)) {
                    if (selectedRow == -1 && selectedCol == -1) {
                        if (board[row][col] != null) {
                            try {
                                checkTurn(row, col);
                                selectedRow = row;
                                selectedCol = col;
                                repaint();
                            } catch (TurnException e) {
                                chatArea.append(e.getMessage() + "\n");
                            }
                        }
                    } else {
                        if (board[selectedRow][selectedCol] != null) {
                            Piece selectedPiece = board[selectedRow][selectedCol];
                            String piece = String.valueOf(selectedPiece);
                            selectedPiece.calculateValidMoves(board, selectedRow, selectedCol);
                            List<int[]> validMoves = selectedPiece.getValidMoves();
                            try {
                                isValid(validMoves, row, col, piece);
                            } catch (InvalidMoveException e) {
                                chatArea.append(e.getMessage() + "\n");
                                selectedRow = -1;
                                selectedCol = -1;
                            }
                            repaint();
                        }
                    }
                }
            }
        });
    }

    private void checkTurn(int row, int col) throws TurnException {
        if (board[row][col] != null &&
                ((!isWhiteTurn && WHITE.equals(board[row][col].getColor())) || (isWhiteTurn && BLACK.equals(board[row][col].getColor())))) {
            throw new TurnException();
        }
    }

    private void isValid(List<int[]> validMoves, int row, int col, String piece) throws InvalidMoveException {
        for (int[] move : validMoves) {
            if (move[0] == row && move[1] == col) {
                movePiece(selectedRow, selectedCol, row, col);
                selectedRow = -1;
                selectedCol = -1;
                repaint();
                isWhiteTurn = !isWhiteTurn;
                checkKings();
                return;
            }
        }
        throw new InvalidMoveException(String.valueOf(piece));
    }
    public void checkKings(){
        King currentKing = (King) findKing(isWhiteTurn);
        if (isCheckmate(currentKing)) {
            handleCheckmate();
        }
    }

    public void movePiece(int startRow, int startCol, int targetRow, int targetCol) {
        Piece movingPiece = board[startRow][startCol];

        if (board[targetRow][targetCol] != null) {
            capturePiece(movingPiece, targetRow, targetCol);
        }

        board[targetRow][targetCol] = movingPiece;
        board[startRow][startCol] = null;

        movingPiece.setPosition(targetRow, targetCol);

        if (movingPiece instanceof Pawn && (targetRow == 0 || targetRow == 7)) {
            promotePawn(targetRow, targetCol, movingPiece.getColor());
        } else if (movingPiece instanceof King && Math.abs(targetCol - startCol) == 2) {
            castleKing(startCol, targetRow, targetCol);
        }
        logMove(movingPiece, startRow, startCol, targetRow, targetCol);

        repaint();
    }

    private void logMove(Piece movingPiece, int startRow, int startCol, int targetRow, int targetCol) {
        chatArea.append(movingPiece.getColor() + " " + movingPiece.getClass().getSimpleName() +
                " przenosi się z (" + startRow + "," + startCol + ") na (" + targetRow + "," + targetCol + ")\n");
    }

    public void promotePawn(int row, int col, String color) {
        int choice = getUserPromotionChoice();

        Piece promotedPiece = switch (choice) {
            case 1 -> new Rook(color);
            case 2 -> new Bishop(color);
            case 3 -> new Knight(color);
            default -> new Queen(color);
        };

        board[row][col] = promotedPiece;
        promotedPiece.setPosition(row, col);
    }
    private int getUserPromotionChoice() {
        return JOptionPane.showOptionDialog(null, "Promocja pionka do:", "Promocja pionka",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Hetman", "Wieża", "Goniec", "Skoczek"}, "Hetman");

    }

    private void castleKing(int startCol, int targetRow, int targetCol) {
        if (targetCol - startCol > 0) {
            board[targetRow][5] = board[targetRow][7];
            board[targetRow][7] = null;
        } else {
            board[targetRow][3] = board[targetRow][0];
            board[targetRow][0] = null;
        }
    }

    private void capturePiece(Piece movingPiece, int targetRow, int targetCol) {
        chatArea.append(movingPiece.getColor() + " " + movingPiece.getClass().getSimpleName() +
                " bije " + board[targetRow][targetCol].getColor() + " " +
                board[targetRow][targetCol].getClass().getSimpleName() + "\n");
    }

    public Piece findKing(boolean isWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece instanceof King && isWhite == WHITE.equals(piece.getColor())) {
                    return piece;
                }
            }
        }
        return null;
    }

    private boolean isCheckmate(King king) {
        if (king == null) {
            return true;
        }
        if (isInCheck(king)) {
            String color = king.getColor();
            chatArea.append(color + " król jest zagrożony!\n");
            if (canKingEscape(king)) {
                return false;
            }
            return !canBlockOrCaptureThreat(king);
        }

        return false;
    }

    private boolean isInCheck(King king) {
        int kingRow = king.getRow();
        int kingCol = king.getCol();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && !piece.getColor().equals(king.getColor()) && canAttackEnemyKing(kingRow, kingCol, i, j, piece))
                    return true;
            }
        }
        return false;
    }

    private boolean canAttackEnemyKing(int kingRow, int kingCol, int i, int j, Piece piece) {
        List<int[]> validMoves = piece.calculateValidMoves(board, i, j);
        if (validMoves != null) {
            for (int[] move : validMoves) {
                if (move[0] == kingRow && move[1] == kingCol) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canKingEscape(King king) {
        int kingRow = king.getRow();
        int kingCol = king.getCol();
        List<int[]> validMoves = king.calculateValidMoves(board, kingRow, kingCol);
        for (int[] move : validMoves) {
            int newRow = move[0];
            int newCol = move[1];
            if (!isPositionThreatened(newRow, newCol, king.getColor())) {
                return true;
            }
        }
        return false;
    }

    private boolean isPositionThreatened(int row, int col, String color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && !piece.getColor().equals(color) && canAttackEnemyKing(row, col, i, j, piece))
                    return true;
            }
        }
        return false;
    }

    private boolean canBlockOrCaptureThreat(King king) {
        List<int[]> threateningMoves = king.getThreatenedMoves();
        for (int[] threatMove : threateningMoves) {
            int threatRow = threatMove[0];
            int threatCol = threatMove[1];

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece piece = board[i][j];
                    if (piece != null && piece.getColor().equals(king.getColor())) {
                        List<int[]> validMoves = piece.calculateValidMoves(board, i, j);
                        for (int[] move : validMoves) {
                            if (move[0] == threatRow && move[1] == threatCol) {
                                Piece temp = board[threatRow][threatCol];
                                board[threatRow][threatCol] = piece;
                                board[i][j] = null;
                                if (!isInCheck(king)) {
                                    board[i][j] = piece;
                                    board[threatRow][threatCol] = temp;
                                    return true;
                                }
                                board[i][j] = piece;
                                board[threatRow][threatCol] = temp;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void handleCheckmate() {
        String winner = isWhiteTurn ? BLACK : WHITE;
        JOptionPane.showMessageDialog(this, winner + " wygrywa!");
        gameover = true;
        saveChatToFile();
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(new Date());
    }

    private void saveChatToFile() {
        String uniqueFileName = "saves/Chat/" + "Save" + "_"+ getDate() + ".txt";

        try (FileWriter writer = new FileWriter(uniqueFileName)) {
            writer.write(chatArea.getText());
        } catch (IOException e) {
            System.out.println("Nie udało się zapisać czatu: " + e.getMessage());
        }
    }
    private void saveChatToGameFile(File gameFile) {
        String chatFileName = "saves/Chat/Game/Chat_" + gameFile.getName().substring(4, 20) + ".txt";
        try (FileWriter writer = new FileWriter(chatFileName)) {
            writer.write(chatArea.getText());
        } catch (IOException e) {
            System.out.println("Nie udało się zapisać czatu: " + e.getMessage());
        }
    }
    @Override
    public void save() {
        saveGame();
    }
    public void saveGame() {
        String uniqueFileName = "saves/Game/Game_" + getDate() + ".ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(uniqueFileName))) {
            oos.writeObject(this);
            saveChatToGameFile(new File(uniqueFileName)); // Save chat along with the game
            JOptionPane.showMessageDialog(this, "Gra zapisana!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Nieudało się zapisać gry: " + e.getMessage(), "Bład", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Board loadGame(File file, JTextArea chatArea) {
        Board loadedBoard = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            loadedBoard = (Board) ois.readObject();

            File chatFile = new File("saves/Chat/Game/Chat_" + file.getName().substring(4, 20) + ".txt");
            System.out.println("Ścieżka do pliku czatu: " + chatFile.getAbsolutePath()); // Dodaj to tymczasowo
            if (chatFile.exists()) {
                String chatContent = new String(Files.readAllBytes(chatFile.toPath()));
                chatArea.setText(chatContent);
            } else {
                chatArea.setText("");
            }

        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Load Game, Nie udało się załadować gry: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return loadedBoard;
    }

    public static void loadSave(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser("saves/Chat");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String fileContents = readFile(selectedFile);
                displayFileContents(frame, fileContents);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Błąd załadowania pliku: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static void displayFileContents(JFrame frame, String fileContents) {
        JTextArea textArea = new JTextArea(fileContents);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(frame, scrollPane, "Zapis czatu", JOptionPane.PLAIN_MESSAGE);
    }
}
