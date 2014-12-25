import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
	final static String testCaseFile = "/Users/kasterma/projects/basicprogramming/hackerrank/ai/bot-building/bot-saves/testcase.txt";
	
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
	
	static void moves(int n, String [] grid){
		for(String line : grid) {
			System.out.println(line);
		}
		Optional<Integer[]> robotLoc = getPosition(n, grid, 'm');
		Optional<Integer[]> princessLoc = getPosition(n, grid, 'p');
		if (princessLoc.isPresent() && robotLoc.isPresent()) {
			int p_y = princessLoc.get()[0];
			int r_y = robotLoc.get()[0];
			int p_x = princessLoc.get()[1];
			int r_x = robotLoc.get()[1];
			if(p_y < r_y) {
				for(int i = 0; i < r_y - p_y; i++)
					System.out.println("UP");
			} else {
				for(int i = 0; i < p_y - r_y; i++)
					System.out.println("DOWN");
			}
			if (p_x < r_x) {
				for(int i = 0; i < r_x - p_x; i++)
					System.out.println("LEFT");
			} else {
				for(int i = 0; i < p_x - r_x; i++)
					System.out.println("RIGHT");
			}
		} else {
			System.out.println("Princess not found");
		}
	}
	
	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(new File(testCaseFile));
			//Scanner in = new Scanner(System.in);
			
			int n;
			n = in.nextInt();
			in.useDelimiter("\n");
			String grid[] = new String[n];


			for(int i = 0; i < n; i++) {
				grid[i] = in.next();
			}

			moves(n, grid);
			
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
