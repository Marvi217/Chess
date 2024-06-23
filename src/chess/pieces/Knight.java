package chess.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece implements Serializable {
    private final String color;
    public Knight(String color) {
        super(color);
        this.color = color;
    }
    @Override
    public List<int[]> calculateValidMoves(Piece[][] board, int currentRow, int currentCol) {
        validMoves = new ArrayList<>();

        int[][] possibleMoves = {
                {currentRow - 2, currentCol - 1},
                {currentRow - 1, currentCol - 2},
                {currentRow + 1, currentCol - 2},
                {currentRow + 2, currentCol - 1},
                {currentRow + 2, currentCol + 1},
                {currentRow + 1, currentCol + 2},
                {currentRow - 1, currentCol + 2},
                {currentRow - 2, currentCol + 1}
        };

        for (int[] move : possibleMoves) {
            int newRow = move[0];
            int newCol = move[1];
            if (isValidMove(board, currentRow, currentCol, newRow, newCol)) {
                validMoves.add(new int[]{newRow, newCol});
            }
        }
        return validMoves;
    }
    @Override
    public void calculateThreatenedMoves(Piece[][] board, int currentRow, int currentCol) {
        List<int[]> validMoves = calculateValidMoves(board, currentRow, currentCol);
        threatenedMoves = new ArrayList<>();

        for (int[] move : validMoves) {
            if (isThreatened(board, move[0], move[1], this.color)) {
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

        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2) &&
                (board[newRow][newCol] == null || !board[newRow][newCol].getColor().equals(this.color));
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public String getImagePath() {
        return "resources/" + color + "_knight.png";
    }

    @Override
    public int getRow() {
        return 0;
    }

    @Override
    public int getCol() {
        return 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
