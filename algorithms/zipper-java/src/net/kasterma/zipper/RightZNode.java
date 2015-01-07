package net.kasterma.zipper;
import java.util.LinkedList;
import java.util.Optional;

import net.kasterma.zipper.ImmutableBinTree.LR;

public class RightZNode implements ZNode {
	protected final Optional<ImmutableBinTree> right;
	protected final Optional<String> value;

	RightZNode(ImmutableBinTree current) {
		right = current.getRight();
		value = current.get(new LinkedList<LR>());
	}


	@Override
	public ImmutableBinTree tree() {
		return new ImmutableBinTree(Optional.empty(), right, value);
	}

	@Override
	public ImmutableBinTree tree(ImmutableBinTree t) {
		return new ImmutableBinTree(Optional.of(t), right, value.map(x -> new String(x)));
	}
}
