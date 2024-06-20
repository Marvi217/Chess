package projekt.chess.pieces;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private boolean hasMoved;

    public King(String color) {
        super(color);
        this.hasMoved = false;
        boolean castled = false;
    }

    @Override
    public List<int[]> calculateValidMoves(Piece[][] board, int currentRow, int currentCol) {
        validMoves = new ArrayList<>();
        threatenedPositions = new ArrayList<>();

        // Possible movements for the king
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
        };

        for (int[] direction : directions) {
            int newRow = currentRow + direction[0];
            int newCol = currentCol + direction[1];

            if (isValidMove(board, currentRow, currentCol, newRow, newCol)) {
                validMoves.add(new int[]{newRow, newCol});
                if (board[newRow][newCol] != null && !board[newRow][newCol].getColor().equals(this.getColor())) {
                    threatenedPositions.add(new int[]{newRow, newCol});
                }
            }
        }

        // Adding castling logic
        if (!hasMoved) {
            // Short castling
            if (board[currentRow][currentCol + 1] == null && board[currentRow][currentCol + 2] == null &&
                    board[currentRow][currentCol + 3] instanceof Rook && !((Rook) board[currentRow][currentCol + 3]).hasMoved) {
                validMoves.add(new int[]{currentRow, currentCol + 2});
            }
            // Long castling
            if (board[currentRow][currentCol - 1] == null && board[currentRow][currentCol - 2] == null &&
                    board[currentRow][currentCol - 3] == null && board[currentRow][currentCol - 4] instanceof Rook &&
                    !((Rook) board[currentRow][currentCol - 4]).hasMoved) {
                validMoves.add(new int[]{currentRow, currentCol - 2});
            }
        }
        return validMoves;
    }

    @Override
    public List<int[]> calculateThreatenedMoves(Piece[][] board, int currentRow, int currentCol) {
        List<int[]> validMoves = calculateValidMoves(board, currentRow, currentCol);

        for (int[] move : validMoves) {
            if (isThreatened(board, move[0], move[1], this.getColor())) {
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

        return (rowDiff <= 1 && colDiff <= 1) &&
                (board[newRow][newCol] == null || !board[newRow][newCol].getColor().equals(this.getColor()));
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
    public String getImagePath() {
        return "resources/" + getColor() + "_king.png";
    }
}
