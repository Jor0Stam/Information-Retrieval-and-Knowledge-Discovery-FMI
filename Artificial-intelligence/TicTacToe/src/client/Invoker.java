package client;

import npc.NPC;
import board.Board;

import java.awt.*;
import java.util.Scanner;

public class Invoker {
    public static void main(String[] args) {
        System.out.println("Enter p/P if you want the player to be first\nEnter n/N if you want the NPC to be first");
        Board board = new Board();
        Point npcMove;
        Scanner sc = new Scanner(System.in);
        int xCoord, yCoord;
        String whoIsFirst;

        do {
            whoIsFirst = sc.nextLine();
        }
        while (!(whoIsFirst.equals("p") || whoIsFirst.equals("P") || whoIsFirst.equals("n") || whoIsFirst.equals("N")));

        if (whoIsFirst.equals("p") || whoIsFirst.equals("P")) {
            while (!board.isGameFinished()) {
                do {
                    System.out.println("Enter X position:");
                    xCoord = sc.nextInt();
                    yCoord = sc.nextInt();
                } while (!board.playX(xCoord, yCoord));

                if (board.isGameFinished()) {
                    break;
                }
                do {
                    npcMove = NPC.makeMove(board, 1);
                } while (!board.playO((int) npcMove.getX(), (int) npcMove.getY()));
                board.printBoard();
            }
        } else {
            boolean playerWon = false;
            while(!board.isGameFinished()) {
                do {
                    npcMove = NPC.makeMove(board, 1);
                } while (!board.playO((int)npcMove.getX(), (int)npcMove.getY()));
                board.printBoard();

                if (board.isGameFinished()) {
                    break;
                }

                do {
                    System.out.println("Enter X position:");
                    xCoord = sc.nextInt();
                    yCoord = sc.nextInt();
                } while (!board.playX(xCoord, yCoord));
                if (board.getWinner().equals("X")) {
                    playerWon = true;
                }
            }

            if (playerWon) {
                board.printBoard();
            }
        }

        if (!board.getWinner().equals("")) {
            System.out.println("Winner is " + board.getWinner());
        } else {
            System.out.println("Draw!");
        }

        sc.close();
    }
}
