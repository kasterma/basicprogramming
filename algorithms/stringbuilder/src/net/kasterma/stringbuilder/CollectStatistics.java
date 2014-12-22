package net.kasterma.stringbuilder;

public class CollectStatistics {

	public static void main(String[] args) {

		int bd = 23;
		long startTime;
		long[] durations1 = new long[bd];
		long[] durations2 = new long[bd];
		for (int i = 1; i < bd+1; i++) {
			FullBinaryTree fbt = new FullBinaryTree(i);
			FullBinaryTree.startReport();
			startTime = System.nanoTime();
			fbt.toString1();
			durations1[i-1] = (System.nanoTime() - startTime)/1000;
			startTime = System.nanoTime();
			fbt.toString2();
			durations2[i-1] = (System.nanoTime() - startTime)/1000;
		}
		for(long dur : durations1) {
			System.out.print(dur);
			System.out.print(" ");
		}
		System.out.println();
		for(long dur : durations2) {
			System.out.print(dur);
			System.out.print(" ");
		}
		System.out.println();
	}


}
