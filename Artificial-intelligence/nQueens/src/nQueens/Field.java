package nQueens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random; 

public class Field {
	public static final char QUEEN = '*';
	public static final char EMPTY_SPACE = '_';

	private int size;
	private int[] queens;
	private int[] colConflicts;
	private int[] mainDiagConflicts;
	private int[] secondDiagConflicts;
	private List<Integer> possibleMoves;
	private int timesTried = 0;
	private boolean solved;
	
	public Field(int size) {
		this.size = size;
		this.colConflicts = new int[size];
		this.possibleMoves = new ArrayList<Integer>();
		this.mainDiagConflicts = new int[(2 * size) - 1];
		this.secondDiagConflicts = new int[(2 * size) - 1];
		this.queens = setRandomQueens();
	}
	
	private int[] setRandomQueens() {
		Random rand = new Random();
		int[] result = new int[this.size];
		int newQueenCol;

		Arrays.fill(this.colConflicts, 0);
		Arrays.fill(this.mainDiagConflicts, 0);
		Arrays.fill(this.secondDiagConflicts, 0);

		for (int i = 0; i < this.size; i++) {
			newQueenCol = rand.nextInt(this.size);
			result[i] = newQueenCol;
			this.colConflicts[newQueenCol]++;
			this.mainDiagConflicts[i - newQueenCol + this.size - 1]++;
			this.secondDiagConflicts[i + newQueenCol]++;
		}

		return result;
	}

	public void solve() {
//		for (queen in queens) {
//			calcMostconflicted one and move her to her most unConflicted space
//		}

		// index of most conflicted queen
		int currMaxConflict;
		do {
			this.timesTried++;
			currMaxConflict = getMaxConflictedIndex();
			
			if (this.timesTried > 2 * this.size) {
				this.queens = setRandomQueens();
				this.timesTried = 0;
			} else if (!this.solved){
				this.queens[currMaxConflict] = this.getMinConflicts(currMaxConflict);
			}
		}
		while (!this.solved);
	}
	
	private int getMaxConflictedIndex() {
		int result = -1;
		int conflictsForI;
		this.solved = true;

		for (int i = 0; i < this.size; i++) {
			conflictsForI = getConflicts(i, this.queens[i]);
			this.solved = this.solved & conflictsForI == 0;
			if (result < conflictsForI) {
				result = i;
			}
		}

		return result;
	}
	
	private int getConflicts(int row, int col) {
		int result = 0;

		result += this.colConflicts[row];
		result += this.mainDiagConflicts[row - col + this.size - 1];
		result += this.mainDiagConflicts[row + col];

		return result;
	}

	private int getMinConflicts(int row) {
		int result = this.size + 1;
		Random rand = new Random();

		for (int i = 0; i < this.size; i++) {
			if (getConflicts(row, this.queens[i]) <= result) {
				result = i;
			}
		}
		
		for (int i = 0; i < this.size; i++) {
			if (getConflicts(row, this.queens[i]) <= result) {
				System.out.println(getConflicts(row, this.queens[i]));
				this.possibleMoves.add(i);
			}
		}

		return possibleMoves.get(rand.nextInt(this.possibleMoves.size()));
	}

	public void printSolution() {
		char[] field = new char[(int) Math.pow(size, 2)];
		field = this.populateField(field);

		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				System.out.print(field[this.size*i + j] + " ");
			}
			System.out.println("");
		}
	}

	private char[] populateField(char[] field) {
		for (int i = 0; i < (int) Math.pow(size, 2); i++) {
			if (this.queens[i / size] == i % this.size) {
				field[i] = QUEEN;
			} else {
				field[i] = EMPTY_SPACE;
			}
		}

		return field;
	}
}