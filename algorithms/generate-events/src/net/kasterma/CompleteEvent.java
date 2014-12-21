package net.kasterma;

import java.util.HashMap;
import java.util.Map;

public class CompleteEvent {
	private Map<String, String> events;
	
	public CompleteEvent() {
		events = new HashMap<String, String>();
	}
	
	public void add(Event en) {
		events.put(en.name, en.value);
	}
	
	@Override
	public String toString() {
		StringBuilder rv = new StringBuilder();
		for(Map.Entry<String, String> en : events.entrySet()) {
			rv.append("<" + en.getKey() + ":" + en.getValue() + ">");
		}
		return rv.toString();
	}
}
