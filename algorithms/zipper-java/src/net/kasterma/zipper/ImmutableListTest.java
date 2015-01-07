package net.kasterma.zipper;

import static org.junit.Assert.*;

import org.junit.Test;

public class ImmutableListTest {

	@Test
	public void test() {
		ImmutableList<Integer> l1 = new ImmutableList<>();
		assertEquals(l1.toString(), "EMPTY");
		assertTrue(l1.isEmpty());
		ImmutableList<Integer> l2 = l1.add(3);
		assertEquals(l2.toString(), "Optional[3:Optional.empty]");
		assertFalse(l2.isEmpty());
		ImmutableList<Integer> l3 = l2.add(4);
		assertEquals(l3.toString(), "Optional[4:Optional[3:Optional.empty]]");
		assertEquals(l3.tail().get().toString(), "Optional[3:Optional.empty]");
		assertEquals(l3.head().get(), new Integer(4));
	}

}
