import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.function.Function;
import java.util.regex.*;

public class Solution {
	final static String testCaseFile = "/Users/kasterma/projects/basicprogramming/hackerrank/ai/bot-building/bot-clean-stochastic/testcase.txt";
	
	static class Position {
		public int x,y;
		
		// Arguments in board convention
		Position(int y, int x) {
			this.x = x;
			this.y = y;
		}
	}
	
	static Optional<Position> getNextDirty(int n, String [] grid, Position bot, char letter) {
		Function<Position, Integer> botDist = (p) -> Math.abs(bot.x - p.x) + Math.abs(bot.y - p.y);
		PriorityQueue<Position> queue =
				new PriorityQueue<Position>((p1, p2) -> botDist.apply(p1).compareTo(botDist.apply(p2)));
		for(int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i].charAt(j) == letter) {
					queue.add(new Position(i,j));
				}
			}
		}
		if (queue.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(queue.poll());
		}
	}
	
	static void move(Position bot, String [] grid){
		Optional<Position> dirtyLoc = getNextDirty(5, grid, bot, 'd');

		if (dirtyLoc.isPresent()) {
			Position goal = dirtyLoc.get();
			if(goal.y < bot.y) {
				System.out.println("UP");
			} else if (goal.y > bot.y) {
				System.out.println("DOWN");
			} else if (goal.x < bot.x) {
				System.out.println("LEFT");
			} else if (goal.x > bot.x) {
				System.out.println("RIGHT");
			} else {
				System.out.println("CLEAN");
			}
		} else {
			System.out.println("Princess not found");
		}
	}
	
	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(new File(testCaseFile));
			//Scanner in = new Scanner(System.in);

			Position bot = new Position(in.nextInt(), in.nextInt());

			in.useDelimiter("\n");
			String grid[] = new String[5];
			for(int i = 0; i < 5; i++) {
				grid[i] = in.next();
			}

			move(bot, grid);
			
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
