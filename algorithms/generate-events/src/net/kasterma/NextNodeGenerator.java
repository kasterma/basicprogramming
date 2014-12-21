package net.kasterma;

@FunctionalInterface
public interface NextNodeGenerator {
	EventNode generate() throws NoNode;
}
