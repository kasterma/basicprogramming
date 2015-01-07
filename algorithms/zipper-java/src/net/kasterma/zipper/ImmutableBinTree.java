package net.kasterma.zipper;

import java.util.Optional;
import java.util.Queue;

public class ImmutableBinTree {
	private final Optional<ImmutableBinTree> left;
	private final Optional<ImmutableBinTree> right;
	private final Optional<String> value;
	
	public enum LR {
		LEFT, RIGHT;
	}
	
	ImmutableBinTree() {
		value = Optional.empty();
		left = Optional.empty();
		right = Optional.empty();
	}
	
	ImmutableBinTree(String value) {
		this.value = Optional.of(new String(value));
		left = Optional.empty();
		right = Optional.empty();
	}
	
	ImmutableBinTree(Optional<ImmutableBinTree> left, Optional<ImmutableBinTree> right) {
		this.value = Optional.empty();
		this.right = right;
		this.left = left;
	}

	ImmutableBinTree(Optional<ImmutableBinTree> left, Optional<ImmutableBinTree> right, String value) {
		this.value = Optional.of(new String(value));
		this.right = right;
		this.left = left;
	}
	
	ImmutableBinTree(Optional<ImmutableBinTree> left, Optional<ImmutableBinTree> right, Optional<String> value) {
		if(value.isPresent()) {
			this.value = Optional.of(new String(value.get()));
		} else {
			this.value = Optional.empty();
		}
		this.right = right;
		this.left = left;
	}
	
	Optional<String> get(Queue<LR> path) {
		if (path.isEmpty()) {
			if (value.isPresent()) {
				return Optional.of(new String(value.get()));
			}
		} else {
			LR next = path.poll();
			if(next == LR.LEFT) {
				if (left.isPresent()) {
					return this.left.get().get(path);
				}
			} else {
				if (right.isPresent()) {
					return this.right.get().get(path);
				}
			}
		}
		return Optional.empty();
	}
	
	ImmutableBinTree assoc(Queue<LR> path, String value) {
		if (path.isEmpty()) {
			return new ImmutableBinTree(left, right, value);
		} else {
			if(path.poll() == LR.LEFT) {
				if (left.isPresent()) {
					return new ImmutableBinTree(Optional.of(left.get().assoc(path, value)),
							right,
							this.value);
				} else {
					return new ImmutableBinTree(Optional.of(new ImmutableBinTree().assoc(path, value)),
							right,
							this.value);
				}
			} else {
				if (right.isPresent()) {
					return new ImmutableBinTree(left,
							Optional.of(right.get().assoc(path, value)),
							this.value);
				} else {
					return new ImmutableBinTree(left,
							Optional.of(new ImmutableBinTree().assoc(path, value)),
							this.value);
				}
			}
		}
	}
	
	public String toString() {
		StringBuilder rv = new StringBuilder();
		rv.append("(");
		left.ifPresent(x -> rv.append(x.toString()));
		rv.append(",");
		right.ifPresent(x -> rv.append(x.toString()));
		rv.append(":");
		value.ifPresent(x -> rv.append(x.toString()));
		rv.append(")");
		return rv.toString();
	}

	public Optional<ImmutableBinTree> getRight() {
		return right;
	}
	
	public Optional<ImmutableBinTree> getLeft() {
		return left;
	}
}
