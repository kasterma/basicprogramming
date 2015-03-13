import java.io.*;
import java.util.*;

public class Solution {
	final static String testCaseFile = "/Users/kasterma/projects/basicprogramming/hackerrank/week-14/superman2/testcase.txt";
	
    int N, H, I;
    int floorCounts[][];
    int dynArray[][];
    
    void getInputs(Scanner in) {
    	N = in.nextInt();
    	H = in.nextInt();
    	I = in.nextInt();
    	floorCounts = new int[N][H];
    	dynArray = new int[N][H];
    	for(int building = 0; building < N; building++) {
    		int numPerson = in.nextInt();
    		for(int personidx = 0;personidx < numPerson; personidx++) {
    			int personFloor = in.nextInt();
    			floorCounts[building][personFloor-1] = floorCounts[building][personFloor-1] + 1;
    		}
    	}
    }

    void showFloors() {
    	for(int building = 0; building < N; building++) {
    		for(int floorn = 0;floorn < H; floorn++) {
    			System.out.print(floorCounts[building][floorn]);
    			System.out.print(" ");
    		}
    		System.out.println();
    	}
    }
    
    void showArray() {
    	for(int building = 0; building < N; building++) {
    		for(int floorn = 0;floorn < H; floorn++) {
    			System.out.print(dynArray[building][floorn]);
    			System.out.print(" ");
    		}
    		System.out.println();
    	}
    }
    
    void fillDynArray() {
    	for(int building = 0; building < N; building++) {
    		dynArray[building][0] = floorCounts[building][0];
    	}
    	for(int floor = 1;floor < H; floor++) {
    		int floorMax1 = 0;
    		int floorMax2 = 0;
			if(I <= floor) {
				int downFloor = floor - I;
				for(int b = 0; b < N; b++) {
					int val = dynArray[b][downFloor];
					if(val > floorMax2) {
						if (val > floorMax1) {
							floorMax2 = floorMax1;
							floorMax1 = val;
						} else {
							floorMax2 = val;
						}
					}
				}
			}
    		
    		for(int building = 0; building < N; building++) {
    			if(I <= floor) {
    				int max = floorMax1;
    				if (dynArray[building][floor-I] == floorMax1) {
    					max = floorMax2;
    				}
    				dynArray[building][floor] =
    						floorCounts[building][floor] +
    						Math.max(max,
    								 dynArray[building][floor - 1]);
    			} else {
    				dynArray[building][floor] =
        					floorCounts[building][floor] + dynArray[building][floor - 1];
    			}
    		}
    	}
    }
    
    int getMax() {
    	int m = 0;
    	for(int building = 0; building < N; building++) {
    		if (dynArray[building][H-1] > m) {
    			m = dynArray[building][H-1];
    		}
    	}
    	return m;
    }
	
	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(new File(testCaseFile));
			//Scanner in = new Scanner(System.in);

			Solution sol = new Solution();
			sol.getInputs(in);

			in.close();
			
			sol.fillDynArray();
			
			sol.showFloors();
			System.out.println();
			sol.showArray();
			
			System.out.println(sol.getMax());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
