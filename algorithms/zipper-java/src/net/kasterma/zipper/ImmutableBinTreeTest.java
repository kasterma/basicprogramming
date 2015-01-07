package net.kasterma.zipper;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import net.kasterma.zipper.ImmutableBinTree.LR;

import org.junit.Test;

public class ImmutableBinTreeTest {

	@Test
	public void test() {
		ImmutableBinTree ibt1 = new ImmutableBinTree();
		assertEquals(Optional.empty(), ibt1.get(new LinkedList<LR>()));
		Queue<LR> q1 = new LinkedList<LR>();
		q1.add(LR.LEFT);
		assertEquals(Optional.empty(), ibt1.get(q1));
		
		ImmutableBinTree ibt2 = new ImmutableBinTree(Optional.of(new ImmutableBinTree("one")),
				Optional.of(new ImmutableBinTree("two")));
		assertEquals(Optional.empty(), ibt2.get(new LinkedList<LR>()));
		q1.add(LR.LEFT);
		assertEquals("one", ibt2.get(q1).get());
		q1.add(LR.RIGHT);
		assertEquals("two", ibt2.get(q1).get());
		q1.add(LR.RIGHT);
		q1.add(LR.LEFT);
		assertEquals(Optional.empty(), ibt2.get(q1));
		LR[] lrs = {LR.LEFT, LR.RIGHT};
		for (LR l : lrs) {
			q1.add(l);
		}
		assertEquals(Optional.empty(), ibt2.get(q1));
		for (LR l : lrs) {
			q1.add(l);
		}
		ImmutableBinTree ibt3 = ibt2.assoc(q1, "added");
		assertEquals("((,(,:added):one),(,:two):)", ibt3.toString());
	}

}
