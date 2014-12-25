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
		
		static PathPlan makeInitialPath(List<Position> dirtyPositions, Position first, Position bot) {
			List<Position> otherPos = new ArrayList<>(dirtyPositions);
			otherPos.remove(first);
			return new PathPlan(otherPos, first, first, dist(bot, first));
		}
		
		static PathPlan makeExtendedPath(PathPlan current, Position newPos) {
			List<Position> tv = new ArrayList<>(current.toVisit);
			tv.remove(newPos);
			return new PathPlan(tv, current.first, newPos, current.len + dist(current.last, newPos));
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
	
	static Position getBest(Position bot, List<Position> dirtyLoc) {
		PriorityQueue<PathPlan> queue = new PriorityQueue<>(comparing(p -> p.len));
		
		for(Position pos : dirtyLoc) {
			queue.add(PathPlan.makeInitialPath(dirtyLoc, pos, bot));
		}
		
		while(true) {
			assert(!queue.isEmpty());
			PathPlan pp = queue.poll();
			if(pp.toVisit.isEmpty()) {
				return pp.first;
			}
			for(Position pos: pp.toVisit) {
				queue.add(PathPlan.makeExtendedPath(pp, pos));
			}
		}
	}
	
	static void move(Position bot, String [] grid){
		Position goal = getBest(bot, getDirties(grid));

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
