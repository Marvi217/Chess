package projekt.chess.pieces;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private String color;

    public Pawn(String color) {
        super(color);
        this.color = color;
    }

    @Override
    public List<int[]> calculateValidMoves(Piece[][] board, int currentRow, int currentCol) {
        validMoves = new ArrayList<>();

        int direction = color.equals("white") ? -1 : 1;
        int startRow = color.equals("white") ? 6 : 1;

        int newRow = currentRow + direction;
        int newCol = currentCol;
        if (newRow >= 0 && newRow < 8 && board[newRow][newCol] == null) {
            validMoves.add(new int[]{newRow, newCol});
        }

        if (currentRow == startRow && board[currentRow + direction][currentCol] == null
                && board[currentRow + 2 * direction][currentCol] == null) {
            validMoves.add(new int[]{currentRow + 2 * direction, currentCol});
        }
        newRow = currentRow + direction;
        newCol = currentCol - 1;
        if (newRow >= 0 && newRow < 8 && newCol >= 0 && board[newRow][newCol] != null) {
            validMoves.add(new int[]{newRow, newCol});
        }

        newRow = currentRow + direction;
        newCol = currentCol + 1;
        if (newRow >= 0 && newRow < 8 && newCol < 8 && board[newRow][newCol] != null) {
            validMoves.add(new int[]{newRow, newCol});
        }

        return validMoves;
    }
    public List<int[]> calculateTake(Piece[][] board, int currentRow, int currentCol) {
        take = new ArrayList<>();
        int direction = color.equals("white") ? -1 : 1;
        int startRow = color.equals("white") ? 6 : 1;

        int newRow;
        int newCol;
        newRow = currentRow + direction;
        newCol = currentCol - 1;
        if (newRow >= 0 && newRow < 8 && newCol >= 0) {
            take.add(new int[]{newRow, newCol});
        }

        newRow = currentRow + direction;
        newCol = currentCol + 1;
        if (newRow >= 0 && newRow < 8 && newCol < 8) {
            take.add(new int[]{newRow, newCol});
        }
        return take;
    }

    @Override
    public List<int[]> calculateThreatenedMoves(Piece[][] board, int currentRow, int currentCol) {
        List<int[]> validMoves = calculateValidMoves(board, currentRow, currentCol);

        for (int[] move : validMoves) {
            if (isThreatened(board, move[0], move[1], this.color)) {
                threatenedMoves.add(move);
            }
        }

        return threatenedMoves;
    }
    private boolean isThreatened(Piece[][] board, int row, int col, String color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && !piece.getColor().equals(color)) {
                    List<int[]> opponentMoves;
                    if (piece instanceof Pawn) {
                        opponentMoves = ((Pawn) piece).calculateTake(board, i, j);
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

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getImagePath() {
        return "resources/" + color + "_pawn.png";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
