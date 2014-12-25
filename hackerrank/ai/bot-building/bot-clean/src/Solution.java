import java.io.*;
import java.util.*;
import static java.util.Comparator.comparing;

public class Solution {
	final static String testCaseFile = "/Users/kasterma/projects/basicprogramming/hackerrank/ai/bot-building/bot-clean/testcase.txt";
	
	final static int SIZE = 5;
	
	static int dist(Position p1, Position p2) {
		return Math.abs(p1.row - p2.row) + Math.abs(p1.col - p2.col);
	}
	
	static class Position {
		public int row, col;

		Position(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}
	
	/**
	 * A (partial) planned path with positions still to visit and distance
	 * traveled so far.  Also the first point is kept.
	 */
	static class PathPlan {
		public Position first;
		public Position last;
		public List<Position> toVisit;
		public Integer len;
		
		PathPlan(List<Position> toVisit, Position first, Position last, Integer len) {
			this.toVisit = toVisit;
			this.first = first;
			this.last = last;
			this.len = len;
		}
	}
	
	static List<Position> getDirties(String [] grid) {
		List<Position> poss = new ArrayList<Position>();
		for(int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (grid[i].charAt(j) == 'd') {
					poss.add(new Position(i,j));
				}
			}
		}
		return poss;
	}
	
	static Optional<Position> getBest(Position bot, List<Position> dirtyLoc) {
		PriorityQueue<PathPlan> queue =
				new PriorityQueue<>(comparing(p -> p.len));
		for(Position pos : dirtyLoc) {
			List<Position> otherPos = new ArrayList<>(dirtyLoc);
			otherPos.remove(pos);
			queue.add(new PathPlan(otherPos, pos, pos, dist(bot, pos)));
		}
		while(!queue.isEmpty()) {
			PathPlan pp = queue.poll();
			if(pp.toVisit.isEmpty()) {
				return Optional.of(pp.first);
			}
			for(Position pos: pp.toVisit) {
				List<Position> tv = new ArrayList<>(pp.toVisit);
				tv.remove(pos);
				queue.add(new PathPlan(tv, pp.first, pos, pp.len + dist(pp.last, pos)));
			}
		}
		assert(false);
		return Optional.empty();
	}
	
	static void move(Position bot, String [] grid){
		List<Position> dirtyLoc = getDirties(grid);

		Optional<Position> bestFirst = getBest(bot, dirtyLoc);
		if (bestFirst.isPresent()) {
			Position goal = bestFirst.get();
			if(goal.row < bot.row) {
				System.out.println("UP");
			} else if (goal.row > bot.row) {
				System.out.println("DOWN");
			} else if (goal.col < bot.col) {
				System.out.println("LEFT");
			} else if (goal.col > bot.col) {
				System.out.println("RIGHT");
			} else {
				System.out.println("CLEAN");
			}
		} else {
			System.out.println("No first step found");
		}
	}
	
	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(new File(testCaseFile));
			//Scanner in = new Scanner(System.in);

			Position bot = new Position(in.nextInt(), in.nextInt());

			in.useDelimiter("\n");
			String grid[] = new String[SIZE];
			for(int i = 0; i < SIZE; i++) {
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
