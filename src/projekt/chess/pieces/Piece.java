package projekt.chess.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece implements Serializable {
    private final String color;
    private int row;
    private int col;
    public List<int[]> validMoves;
    protected List<int[]> threatenedMoves;
    protected List<int[]> take;

    public Piece(String color) {
        this.color = color;
        this.row = -1;
        this.col = -1;
        this.validMoves = new ArrayList<>();
        this.threatenedMoves = new ArrayList<>();
    }

    public abstract List<int[]> calculateValidMoves(Piece[][] board, int currentRow, int currentCol);
    public abstract void calculateThreatenedMoves(Piece[][] board, int selectedRow, int selectedCol);
    public String getColor() {
        return color;
    }

    public String getImagePath() {
        return "";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public List<int[]> getValidMoves() {
        return validMoves;
    }

    public List<int[]> getThreatenedMoves() {
        return threatenedMoves;
    }
    static boolean isThreatened(Piece[][] board, int row, int col, String color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && !piece.getColor().equals(color)) {
                    List<int[]> opponentMoves;
                    if (piece instanceof Pawn) {
                        opponentMoves = ((Pawn) piece).calculateTake(i, j);
                    } else {
                        opponentMoves = piece.calculateValidMoves(board, i, j);
                    }
                    if (opponentMoves != null) {
                        for (int[] move : opponentMoves) {
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
}
