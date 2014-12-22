package net.kasterma.stringbuilder;

public class FullBinaryTree {
	FullBinaryTree left;
	FullBinaryTree right;

	FullBinaryTree(int depth) {
		if(depth > 0) {
			left = new FullBinaryTree(depth - 1);
			right = new FullBinaryTree(depth - 1);
		}
	}
	
	String toString1() {
		if (left != null) return "(" + left.toString1() + "," + right.toString1() + ")";
		else return "()";
	}
	
	String toString2() {
		StringBuilder sb = new StringBuilder();
		this.toString2(sb);
		return sb.toString();
	}
	
	void toString2(StringBuilder sb) {
		sb.append("(");
		if (left != null) {
			left.toString2(sb);
			sb.append(",");
			if (right != null) right.toString2(sb);
		}
		sb.append(")");
	}
}