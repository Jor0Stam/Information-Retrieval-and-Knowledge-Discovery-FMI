package npc;

import board.Board;

import java.awt.*;

public class NPC {
    private int alpha = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;
    private int player = 0;
    private Board board;
    
    public static Point makeMove(Board board, int player) {
        NPC npc = new NPC(Board.copyBoard(board), player);
        return npc.calcMove();
    }

    private NPC(Board board, int player) {
        this.board = board;
        this.player = player;
    }

    private Point calcMove() {
        // Merge with calculateOptimalMove?
        Board copiedBoard;
        int bestMove = -1, localResult, bestMoveValue = 0;

        for (int i = 0; i < 9; i++) {
            copiedBoard = Board.copyBoard(board);
            if (player == 1 && copiedBoard.playO(i / 3, i % 3) && (copiedBoard.getWinner().equals("O"))) {
                return new Point(i / 3, i % 3);
            } else if (player == -1 && copiedBoard.playX(i / 3, i % 3) && (copiedBoard.getWinner().equals("X"))) {
                return new Point(i / 3, i % 3);
            }
        }

        for (int i = 0; i < 9; i++) {
            if (board.getBoard()[i] == 0) {
                copiedBoard = Board.copyBoard(board);
                simulateCurrentPlayerMove(player, copiedBoard, i);
                localResult = calculateOptimalMove(copiedBoard, switchPlayer(player));
                if (resultBetterForPlayer(player, bestMoveValue, localResult)) {
                    bestMove = i;
                    bestMoveValue = localResult;
                }
            }
        }

        return new Point(bestMove / 3, bestMove % 3);
    }

    private int calculateOptimalMove(Board board, int player) {
        /*
            @ Returns:
             1 if O wins
             -1 if X wins
             0 if draw
         */
        Board copiedBoard;
        int localResult, bestMove = 0, bestMoveValue = (player == 1 ? -2 : 2);
        if (board.isGameFinished()) {
            return board.getWinner().equals("O") ? 1 :
                    board.getWinner().equals("X") ? -1 : 0;
        }

        for (int i = 0; i < 9; i++) {
            if (board.getBoard()[i] == 0) {
                copiedBoard = Board.copyBoard(board);
                simulateCurrentPlayerMove(player, copiedBoard, i);
                localResult = calculateOptimalMove(copiedBoard, switchPlayer(player));
                if (resultBetterForPlayer(player, bestMoveValue, localResult)) {
                    bestMove = i;
                    bestMoveValue = localResult;
                }
            }
        }

        if (player == 1) {
            board.playO(bestMove / 3, bestMove % 3);
        } else {
            board.playX(bestMove / 3, bestMove % 3);
        }
        return calculateOptimalMove(board, switchPlayer(player));
    }

    private boolean resultBetterForPlayer(int player, int bestMove, int localResult) {
        return (player == 1 && bestMove <= localResult) || (player == -1 && bestMove >= localResult);
    }

    private void simulateCurrentPlayerMove(int player, Board board, int move) {
        if (player == 1) {
            board.playO(move / 3, move % 3);
        } else if (player == -1) {
            board.playX(move / 3, move % 3);
        } else {
            System.out.println("Something went wrong in currentPlayerMove: " + player);
            board.printBoard();
            System.out.println("-------------");
        }
    }

    private int switchPlayer(int currentPlayer) {
        return currentPlayer == 1 ? -1 : 1;
    }
}