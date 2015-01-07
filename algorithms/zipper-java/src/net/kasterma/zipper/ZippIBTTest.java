package net.kasterma.zipper;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;

public class ZippIBTTest {

	@Test
	public void test() {
		ImmutableBinTree ibt2 = new ImmutableBinTree(Optional.of(new ImmutableBinTree("one")),
				Optional.of(new ImmutableBinTree("two")));
		ZippIBT z = new ZippIBT(ibt2);
		assertEquals(z.tree().toString(), "((,:one),(,:two):)");
		ZippIBT z2 = z.downLeft().get();
		assertEquals(z2.getCurrent().toString(), "(,:one)");
		assertEquals(z2.tree().toString(), "((,:one),(,:two):)");
		ZippIBT z3 = z.downRight().get();
		assertEquals(z3.getCurrent().toString(), "(,:two)");
		assertEquals(z3.tree().toString(), "((,:one),(,:two):)");
	}

}
