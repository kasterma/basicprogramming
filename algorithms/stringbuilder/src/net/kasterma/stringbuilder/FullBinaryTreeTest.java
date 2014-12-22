package net.kasterma.stringbuilder;

import static org.junit.Assert.*;

import org.junit.Test;

public class FullBinaryTreeTest {

	@Test
	public void rrtest() {
		long startTime = System.currentTimeMillis();
		FullBinaryTree fbt = new FullBinaryTree(20);
		System.out.println(System.currentTimeMillis() - startTime);
		assertEquals(fbt.toString1(), fbt.toString2());
		startTime = System.currentTimeMillis();
		fbt.toString1();
		long duration1 = System.currentTimeMillis() - startTime;
		startTime = System.currentTimeMillis();
		fbt.toString2();
		long duration2 = System.currentTimeMillis() - startTime;
		System.out.println(duration1);
		System.out.println(duration2);
	}
	
	@Test
	public void testGetTimings() {
		int bd = 22;
		long startTime;
		long[] durations1 = new long[bd];
		long[] durations2 = new long[bd];
		for (int i = 1; i < bd+1; i++) {
			FullBinaryTree fbt = new FullBinaryTree(i);
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
