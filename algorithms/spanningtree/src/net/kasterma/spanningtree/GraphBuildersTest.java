package net.kasterma.spanningtree;

import static org.junit.Assert.*;

import org.junit.Test;

public class GraphBuildersTest {

	@Test
	public void test() {
		Graph c2 = GraphBuilders.CompleteGraph(2);

		assertEquals(2, c2.countNodes());
		
		Graph t2 = c2.spanningTree();
		assertEquals(t2.countNodes(), c2.countNodes());
		assertEquals(t2.countEdges(), 1);
		assertEquals(c2.countEdges(), 2);
		
		
		c2 = GraphBuilders.CompleteGraph(5);

		assertEquals(5, c2.countNodes());
		
		t2 = c2.spanningTree();
		assertEquals(t2.countNodes(), c2.countNodes());
		assertEquals(t2.countEdges(), 4);
		assertEquals(c2.countEdges(), 20);
	}
}
