package net.kasterma.spanningtree;

import java.util.ArrayList;
import java.util.List;

public class GraphNode {
	List<GraphNode> nbds;
	int value;
	{
		nbds = new ArrayList<GraphNode>();
	}
	
	GraphNode(int value) {
		this.value = value;
	}
	
	GraphNode(int value, GraphNode... nbds) {
		this.value = value;
		for(GraphNode nbd : nbds){
			this.nbds.add(nbd);
		}
	}
	
	GraphNode(GraphNode n) {
		value = n.value;
	}
	
	void addNbd(GraphNode nbd) {
		nbds.add(nbd);
	}
	
	List<GraphNode> getNbds() {
		return nbds;
	}
	
	int getValue() {
		return value;
	}
	
	void setValue(int value) {
		this.value = value;
	}
}
