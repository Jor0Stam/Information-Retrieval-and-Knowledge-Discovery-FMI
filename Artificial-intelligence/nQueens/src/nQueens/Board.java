package nQueens;

import java.util.Arrays;
import java.util.Random;

public class Board {
	public static final char QUEEN = '*';
	public static final char EMPTY_SPACE = '_';

	private int size;
	private int[] queens;
	private int[] colConflicts;
	private int[] mainDiagConflicts;
	private int[] secondDiagConflicts;
	private int timesTried = 0;
	private boolean solved;
	Random rand = new Random();
	
	public Board(int size) {
		this.size = size;
		this.colConflicts = new int[size];
		this.mainDiagConflicts = new int[(2 * size) - 1];
		this.secondDiagConflicts = new int[(2 * size) - 1];
		this.queens = setRandomQueens();
	}
	
	private int[] setRandomQueens() {
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
		int currMaxConflict;
		do {
			this.timesTried++;
			currMaxConflict = getMaxConflictedQueen();

			if (this.timesTried > 2 * this.size) {
				this.queens = setRandomQueens();
				this.timesTried = 0;
			} else if (!this.solved){
				this.updateConflicts(currMaxConflict);
			}
		}
		while (!this.solved);
	}

	private void updateConflicts(int currMaxConflict) {
		this.colConflicts[this.queens[currMaxConflict]]--;
		this.mainDiagConflicts[currMaxConflict - this.queens[currMaxConflict] + this.size - 1]--;
		this.secondDiagConflicts[currMaxConflict + this.queens[currMaxConflict]]--;
		this.queens[currMaxConflict] = this.getMinConflicts(currMaxConflict);
	}

	private int getMaxConflictedQueen() {
		int result = 0;
		int conflictsForI;
		this.solved = true;

		for (int i = 0; i < this.size; i++) {
			conflictsForI = getConflicts(i, this.queens[i]);
			this.solved = this.solved & conflictsForI == 0;
			if (result < conflictsForI) {
				result = i;
			}
		}

		for (int i = 0; i < this.size; i++) {
			conflictsForI = getConflicts(i, this.queens[i]);
			if (result <= conflictsForI) {
				result = rand.nextInt(2) == 1 ? i : result;
			}
		}
		if (result == -1) {
			printSolution();
		}

		return result;
	}

	private int getMinConflicts(int currMax) {
		int result = this.size - 1;

		for (int i = 0; i < this.size; i++) {
			if (getConflicts(currMax, i) < result) {
				result = i;
			}
		}

		for (int i = 0; i < this.size; i++) {
			if (getConflicts(currMax, i) <= result) {
				result = rand.nextInt(2) == 1 ? i : result;
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
