package chess.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static chess.pieces.Queen.isPathClear;

public class Bishop extends Piece implements Serializable {
    private final String color;

    public Bishop(String color) {
        super(color);
        this.color = color;
    }

    @Override
    public List<int[]> calculateValidMoves(Piece[][] board, int currentRow, int currentCol) {
        validMoves = new ArrayList<>();

        int[][] directions = {
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        for (int[] direction : directions) {
            int rowStep = direction[0];
            int colStep = direction[1];
            int newRow = currentRow + rowStep;
            int newCol = currentCol + colStep;

            while (isValidMove(board, currentRow, currentCol, newRow, newCol)) {
                validMoves.add(new int[]{newRow, newCol});
                newRow += rowStep;
                newCol += colStep;
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

        if (rowDiff == colDiff) {
            int rowStep = Integer.compare(newRow, currentRow);
            int colStep = Integer.compare(newCol, currentCol);

            return isPathClear(board, currentRow, currentCol, newRow, newCol, rowStep, colStep, rowDiff, this.color);
        }

        return false;
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public String getImagePath() {
        return "resources/" + color + "_bishop.png";
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
