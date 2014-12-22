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
}
