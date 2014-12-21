package net.kasterma;

public class EventNode {
	public final String name;
	private final EventValueGenerator genValue;
	private final NextNodeGenerator genNode;
	
	EventNode(String name, EventValueGenerator gv, NextNodeGenerator gn) {
		this.name = name;
		genValue = gv;
		genNode = gn;
	}
	
	CompleteEvent generateComplete() {
		CompleteEvent rv = new CompleteEvent();
		try {
			EventNode en = this;
			while(true) {
				rv.add(new Event(en.name, en.genValue.generate()));
				en = en.genNode.generate();
			}
		} catch (NoNode e) {
			return rv;
		}
	}
}
