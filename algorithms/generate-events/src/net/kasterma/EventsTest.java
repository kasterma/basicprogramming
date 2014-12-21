package net.kasterma;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;

public class EventsTest {

	@Test
	public void test() {
		Event en = new Event("hi", "ho");
		assertEquals(en.toString(), "<hi:ho>");
		CompleteEvent ce = new CompleteEvent();
		ce.add(en);
		System.out.println(ce.toString());
		assertEquals(ce.toString(), "<hi:ho>");
		
		Event en2 = Event.UniformEvent("uni1", "val1", "val2");
		System.out.println(en2);
		
		Event en3 = Event.GenerateValue("gen1", () -> (new Integer(new Random().nextInt(10)).toString()));
		System.out.println(en3);
		
		EventNode enode2 = new EventNode("node2",
				() -> (new Integer(new Random().nextInt(10)).toString()),
				() -> {throw new NoNode();});
		
		EventNode enode1 = new EventNode("node1",
				() -> (new Integer(new Random().nextInt(10)).toString()),
				() -> enode2);
		
		EventNode enode0 = new EventNode("node0",
				() -> (new Integer(new Random().nextInt(10)).toString()),
				() -> {Random rnd = new Random();
				       if (rnd.nextBoolean()) return enode1;
				       else return enode2;});
		
		CompleteEvent cevent = enode0.generateComplete();
		
		System.out.println(cevent.toString());
	}

}
