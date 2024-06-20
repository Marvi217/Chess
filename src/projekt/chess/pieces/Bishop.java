package projekt.chess.pieces;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    private String color;

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
    public List<int[]> calculateThreatenedMoves(Piece[][] board, int currentRow, int currentCol) {
        List<int[]> validMoves = calculateValidMoves(board, currentRow, currentCol);

        for (int[] move : validMoves) {
            if (isThreatened(board, move[0], move[1], this.color)) {
                threatenedMoves.add(move);
            }
        }

        return threatenedMoves;
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

            for (int i = 1; i < rowDiff; i++) {
                if (board[currentRow + i * rowStep][currentCol + i * colStep] != null) {
                    return false;
                }
            }

            return board[newRow][newCol] == null || !board[newRow][newCol].getColor().equals(this.color);
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
}
