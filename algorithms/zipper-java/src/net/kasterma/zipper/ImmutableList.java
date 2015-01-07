package net.kasterma.zipper;

import java.util.Optional;

public class ImmutableList<T> {
	class ListNode {
		Optional<ListNode> next;
		T val;
		ListNode(T val) {
			next = Optional.empty();
			this.val = val;
		}
		ListNode(ListNode next, T val) {
			this.next = Optional.of(next);
			this.val = val;
		}
		Boolean hasNext() {
			return next.isPresent();
		}
		ListNode getNext() {
			return this.next.get();
		}
		
		T val() {
			return val;
		}
	}
	
	private final Optional<ListNode> node;
	
	ImmutableList() {
		node = Optional.empty();
	}
	
	ImmutableList(ListNode node) {
		this.node = Optional.of(node);
	}
	
	Boolean isEmpty() {
		return !node.isPresent();
	}
	
	Optional<ImmutableList<T>> tail() {
		if (node.isPresent() & node.get().hasNext())
			return Optional.of(new ImmutableList<T>(node.get().getNext()));
		return Optional.empty();
	}
	
	Optional<T> head() {
		if (node.isPresent()) {
			return Optional.of(node.get().val());
		} else {
			return Optional.empty();
		}
	}
	
	ImmutableList<T> add(T val) {
		if(node.isPresent())
			return new ImmutableList<T>(new ListNode(node.get(), val));
		else
			return new ImmutableList<T>(new ListNode(val));
	}
}
