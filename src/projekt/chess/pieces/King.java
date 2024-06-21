package projekt.chess.pieces;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private final boolean hasMoved;

    public King(String color) {
        super(color);
        this.hasMoved = false;
    }

    @Override
    public List<int[]> calculateValidMoves(Piece[][] board, int currentRow, int currentCol) {
        validMoves = new ArrayList<>();

        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
        };

        for (int[] direction : directions) {
            int newRow = currentRow + direction[0];
            int newCol = currentCol + direction[1];

            if (isValidMove(board, currentRow, currentCol, newRow, newCol)) {
                validMoves.add(new int[]{newRow, newCol});
            }
        }

        if (!hasMoved) {
            if (isCastlingPossible(board, currentRow, currentCol + 3)) {
                validMoves.add(new int[]{currentRow, currentCol + 2});
            }
            if (isCastlingPossible(board, currentRow, currentCol - 4)) {
                validMoves.add(new int[]{currentRow, currentCol - 2});
            }
        }
        return validMoves;
    }

    private boolean isCastlingPossible(Piece[][] board, int row, int rookCol) {
        if (rookCol < 0 || rookCol >= 8) return false;

        int colStep = (rookCol > 4) ? 1 : -1;
        for (int col = 4 + colStep; col != rookCol; col += colStep) {
            if (board[row][col] != null) return false;
        }
        return board[row][rookCol] instanceof Rook && !((Rook) board[row][rookCol]).hasMoved;
    }

    @Override
    public void calculateThreatenedMoves(Piece[][] board, int currentRow, int currentCol) {
        List<int[]> validMoves = calculateValidMoves(board, currentRow, currentCol);
        threatenedMoves = new ArrayList<>();
        for (int[] move : validMoves) {
            if (isThreatened(board, move[0], move[1], this.getColor())) {
                threatenedMoves.add(move);
            }
        }
    }

    private boolean isValidMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol) {
        if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
            return false;
        }

        int rowDiff = Math.abs(currentRow - newRow);
        int colDiff = Math.abs(currentCol - newCol);

        return (rowDiff <= 1 && colDiff <= 1) &&
                (board[newRow][newCol] == null || !board[newRow][newCol].getColor().equals(this.getColor()));
    }

    @Override
    public String getImagePath() {
        return "resources/" + getColor() + "_king.png";
    }
}
