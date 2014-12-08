package net.kasterma.spanningtree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Map;

public class Graph {
	GraphNode root;
	
	Graph() {
		// generate empty graph, root == null
	}
	
	Graph(GraphNode root) {
		this.root = root;
	}
	
	Set<GraphNode> vertices () {
		Set<GraphNode> seen = new HashSet<GraphNode>();
		Queue<GraphNode> boundary = new LinkedList<GraphNode>();
		boundary.add(root);
		GraphNode current;
		while(!(null == (current = boundary.poll()))) {
			if(!seen.contains(current)) {
				seen.add(current);
				for(GraphNode nbd : current.getNbds()) {
					if(!seen.contains(nbd)) {
						boundary.add(nbd);
					}
				}
			}
		}
		return seen;
	}
	
	int countNodes() {
		return vertices().size();
	}
	
	int countEdges() {
		Set<GraphNode> vertices = vertices();
		int ct = 0;
		for(GraphNode node: vertices) {
			ct += node.getNbds().size();
		}
		return ct;
	}
	
	Graph spanningTree() {
		Map<GraphNode, GraphNode> nodeMap = new HashMap<GraphNode,GraphNode>();
		Queue<GraphNode> boundary = new LinkedList<GraphNode>();
		nodeMap.put(root, new GraphNode(root));
		boundary.add(root);
		GraphNode current;
		while(!(null == (current = boundary.poll()))) {
			if(!nodeMap.containsKey(current)) {
				nodeMap.put(current, new GraphNode(current));
			}
			for(GraphNode nbd : current.getNbds()) {
				if(!nodeMap.containsKey(nbd)) {
					boundary.add(nbd);
					nodeMap.put(nbd, new GraphNode(nbd));
					nodeMap.get(current).addNbd(nodeMap.get(nbd));
				}
			}
		}
		return new Graph(nodeMap.get(root));
	}
}
