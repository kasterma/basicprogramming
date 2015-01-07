package net.kasterma.zipper;

import java.util.Optional;

public class ImmutableList<T> {
	private class ListNode {
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
		
		public String toString() {
			return val.toString() + ":" + next.toString();
		}
	}
	
	private final Optional<ListNode> node;
	
	public ImmutableList() {
		node = Optional.empty();
	}
	
	private ImmutableList(ListNode node) {
		this.node = Optional.of(node);
	}
	
	public Boolean isEmpty() {
		return !node.isPresent();
	}
	
	public Optional<ImmutableList<T>> tail() {
		if (node.isPresent() & node.get().hasNext())
			return Optional.of(new ImmutableList<T>(node.get().getNext()));
		return Optional.empty();
	}
	
	public Optional<T> head() {
		if (node.isPresent()) {
			return Optional.of(node.get().val());
		} else {
			return Optional.empty();
		}
	}
	
	public ImmutableList<T> add(T val) {
		if(node.isPresent())
			return new ImmutableList<T>(new ListNode(node.get(), val));
		else
			return new ImmutableList<T>(new ListNode(val));
	}
	
	public String toString() {
		if(node.isPresent()) {
			return node.toString();
		} else {
			return "EMPTY";
		}
	}
}
