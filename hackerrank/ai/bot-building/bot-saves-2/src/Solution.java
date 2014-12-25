import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
	final static String testCaseFile = "/Users/kasterma/projects/basicprogramming/hackerrank/ai/bot-building/bot-saves-2/testcase.txt";
	
	static Optional<Integer[]> getPosition(int n, String [] grid, char letter) {
		for(int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i].charAt(j) == letter) {
					Integer[] rv = new Integer[2];
					rv[0] = i;
					rv[1] = j;
					return Optional.of(rv);
				}
			}
		}
		return Optional.empty();
	}
	
	static void nextMove(int n, int r, int c, String [] grid){
		for(String line : grid) {
			System.out.println(line);
		}
		Optional<Integer[]> princessLoc = getPosition(n, grid, 'p');
		if (princessLoc.isPresent()) {
			if(princessLoc.get()[0] < r) {
				System.out.println("UP");
			} else if (princessLoc.get()[0] > r) {
				System.out.println("DOWN");
			} else if (princessLoc.get()[1] < c) {
				System.out.println("LEFT");
			} else {
				System.out.println("RIGHT");
			}
		} else {
			System.out.println("Princess not found");
		}
	}
	
	public static void main(String[] args) {
		try {
			//Scanner in = new Scanner(new File(testCaseFile));
			Scanner in = new Scanner(System.in);
			
			int n,r,c;
			n = in.nextInt();
			r = in.nextInt();
			c = in.nextInt();
			in.useDelimiter("\n");
			String grid[] = new String[n];


			for(int i = 0; i < n; i++) {
				grid[i] = in.next();
			}

			nextMove(n,r,c,grid);
			
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
