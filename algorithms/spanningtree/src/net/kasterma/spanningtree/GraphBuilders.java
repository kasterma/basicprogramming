package net.kasterma.spanningtree;

public class GraphBuilders {
	public static Graph CompleteGraph(int k) {
		assert k > 0;  // not for empty graphs
		GraphNode[] nodes = new GraphNode[k];
		for(int i = 0; i < k; i++) {
			nodes[i] = new GraphNode(i);
		}
		for(GraphNode fromNode : nodes) {
			for(GraphNode toNode : nodes) {
				if (!(fromNode == toNode)) {
					fromNode.addNbd(toNode);
				}
			}
		}
		return new Graph(nodes[0]);
	}
}
