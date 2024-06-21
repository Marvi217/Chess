package projekt.chess.board;
import projekt.chess.exception.InvalidMoveException;
import projekt.chess.exception.TurnException;
import projekt.chess.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static projekt.chess.engine.Start.isWhiteTurn;

public class Board extends JComponent {
    public static final String BLACK = "black";
    public static final String WHITE = "white";
    private final Piece[][] board = new Piece[8][8];
    private int selectedRow = -1;
    private int selectedCol = -1;
    static JTextArea chatArea;

    public Board() {
        initialize();
        setupMouseListener();
    }

    public void setChatArea(JTextArea chatArea) {
        Board.chatArea = chatArea;
    }

    private void initialize() {
        setupPiecePosition();
        setTheChessPieces();
    }
    private void setupPiecePosition() {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                board[x][y] = null;
            }
        }
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
    public void setTheChessPieces () {
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
        if (selectedRow != -1 && selectedCol != -1) {
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
                    Image pieceImage = new ImageIcon(imagePath).getImage();
                    g.drawImage(pieceImage, col * tileSize, row * tileSize, tileSize, tileSize, this);
                }
            }
        }
    }
    public void drawTiles(Graphics g, int tileSize){
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
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int tileSize = getSize().width / 8;
                int row = mouseEvent.getY() / tileSize;
                int col = mouseEvent.getX() / tileSize;

                if (Board.isValidPosition(row, col)) {
                    if (selectedRow == -1 && selectedCol == -1) {
                        try {
                            checkTurn(row, col);
                            selectedRow = row;
                            selectedCol = col;
                            repaint();
                        } catch (TurnException e) {
                            chatArea.append(e.getMessage());
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
                                chatArea.append(e.getMessage());
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

                King currentKing = (King) findKing(isWhiteTurn);
                if (isCheckmate(currentKing)) {
                    handleCheckmate();
                }
                return;
            }
        }
        throw new InvalidMoveException(String.valueOf(piece));
    }

    private void movePiece(int startRow, int startCol, int targetRow, int targetCol) {
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

    private void capturePiece(Piece movingPiece, int targetRow, int targetCol) {
        chatArea.append(movingPiece.getColor() + " " + movingPiece.getClass().getSimpleName() +
                " bije " + board[targetRow][targetCol].getColor() + " " +
                board[targetRow][targetCol].getClass().getSimpleName() + "\n");
    }

    private void promotePawn(int targetRow, int targetCol, String color) {
        Object[] options = {"Hetman", "Wieża", "Goniec", "Skoczek"};
        int choice = JOptionPane.showOptionDialog(this,
                "Wybierz figurę:",
                "Awans",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        Piece promotedPiece = switch (choice) {
            case 1 -> new Rook(color);
            case 2 -> new Bishop(color);
            case 3 -> new Knight(color);
            default -> new Queen(color);
        };
        board[targetRow][targetCol] = promotedPiece;
    }
    private void logMove(Piece movingPiece, int startRow, int startCol, int targetRow, int targetCol) {
        chatArea.append(movingPiece.getColor() + " " + movingPiece.getClass().getSimpleName() +
                " przenosi się z (" + startRow + "," + startCol + ") na (" + targetRow + "," + targetCol + ")\n");
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

    private Piece findKing(boolean isWhiteTurn) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece instanceof King && piece.getColor().equals(isWhiteTurn ? WHITE : BLACK)) {
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
                if (piece != null && !piece.getColor().equals(king.getColor())) {
                    List<int[]> validMoves = piece.calculateValidMoves(board, i, j);
                    if (validMoves != null) {
                        for (int[] move : validMoves) {
                            if (move[0] == kingRow && move[1] == kingCol) {
                                return true;
                            }
                        }
                    }
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
                if (piece != null && !piece.getColor().equals(color)) {
                    List<int[]> validMoves = piece.calculateValidMoves(board, i, j);
                    if (validMoves != null) {
                        for (int[] move : validMoves) {
                            if (move[0] == row && move[1] == col) {
                                return true;
                            }
                        }
                    }
                }
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
        String winner = isWhiteTurn ? "Black" : WHITE;
        JOptionPane.showMessageDialog(this, winner + " wins!");
        saveChatToFile();

    }

    private void saveChatToFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = dateFormat.format(new Date());
        String uniqueFileName = "saves/" + "Save" + "_" + timeStamp + ".txt";

        try (FileWriter writer = new FileWriter(uniqueFileName)) {
            writer.write(chatArea.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
