package net.kasterma;

import java.util.Random;

public class Event {
	public final String name;
	public final String value;
	
	public Event(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "<" + name + ":" + value +">";
	}
	
	public static Event ConstantEvent(Event val) {
		return new Event(val.name, val.value);
	}
	
	public static Event UniformEvent(String name, String... values) {
		Random rnd = new Random();
		int idx = rnd.nextInt(values.length);
		return new Event(name, values[idx]);
	}
	
	public static Event GenerateValue(String name, EventValueGenerator gen) {
		return new Event(name, gen.generate());
	}
}
