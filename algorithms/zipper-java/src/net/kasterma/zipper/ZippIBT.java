package net.kasterma.zipper;

import java.util.Optional;

public class ZippIBT {
	private final ImmutableBinTree current;
	private final ImmutableList<ZNode> path;
	
	ZippIBT(ImmutableBinTree t) {
		current = t;
		path = new ImmutableList<ZNode>();
	}
	
	ZippIBT(ImmutableBinTree t, ImmutableList<ZNode> path) {
		current = t;
		this.path = path;
	}
	
	Optional<ZippIBT> downLeft() {
		if(current.getLeft().isPresent()) {
			return Optional.of(new ZippIBT(current.getLeft().get(), path.add(new RightZNode(current))));
		} else {
			return Optional.empty();
		}
	}
	
	Optional<ZippIBT> downRight() {
		if(current.getRight().isPresent()) {
			return Optional.of(new ZippIBT(current.getRight().get(), path.add(new LeftZNode(current))));
		} else {
			return Optional.empty();
		}
	}
	
	Optional<ZippIBT> up() {
		if (path.isEmpty()) {
			return Optional.empty();
		} else {
			if(path.tail().isPresent())
				return Optional.of(new ZippIBT(path.head().get().tree(current), path.tail().get()));
			else
				return Optional.of(new ZippIBT(path.head().get().tree(current)));
		}
	}
	
	ImmutableBinTree tree() {
		if (path.isEmpty())
			return current;
		else
			return up().get().tree();
	}
	
	ImmutableBinTree getCurrent() {
		return current;
	}
}
