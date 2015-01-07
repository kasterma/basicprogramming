package net.kasterma.zipper;

import java.util.LinkedList;
import java.util.Optional;

import net.kasterma.zipper.ImmutableBinTree.LR;

public class LeftZNode implements ZNode {
	protected final Optional<ImmutableBinTree> left;
	protected final Optional<String> value;

	LeftZNode(ImmutableBinTree current) {
		left = current.getLeft();
		value = current.get(new LinkedList<LR>());
	}


	@Override
	public ImmutableBinTree tree() {
		return new ImmutableBinTree(left, Optional.empty(), value);
	}

	@Override
	public ImmutableBinTree tree(ImmutableBinTree t) {
		return new ImmutableBinTree(left, Optional.of(t), value.map(x -> new String(x)));
	}
}

