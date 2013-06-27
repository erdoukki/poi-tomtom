import java.util.Iterator;

public class RedBlackTree<T extends Comparable<T>> implements Iterable<RedBlackTree<T>> {

	private RedBlackTree<T> parent;
	private RedBlackTree<T> left;
	private RedBlackTree<T> right;
	private T value;
	private Colour colour;

	public enum Colour {BLACK, RED}

	public RedBlackTree(RedBlackTree<T> parent) {
		this.parent = parent;
	}

	protected T getValue() {
		return value;
	}

	private RedBlackTree<T> root() {
		RedBlackTree<T> root = this;
		while (root.parent != null) {
			root = root.parent;
		}
		return root;
	}

	public RedBlackTree<T> find(T value) {
		RedBlackTree<T> z = root();
		while (z != null) {
			if (z.value.compareTo(value) > 0) {
				z = z.right;
			} else if( z.value.compareTo(value) < 0) {
				z = z.left;
			} else {
				return z;
			}
		}
		return null;
	}

	public RedBlackTree<T> add(T value) {
		return root()._add(value);
	}

	private RedBlackTree<T> _add(T value) {
		if (this.value == null) {
			this.value = value;

			colour = Colour.RED;
			balancingCase1(this);
			return this;
		} else {
			if (value.compareTo(this.value) < 0) {
				if (left == null) {
					left = new RedBlackTree<T>(this);
				}
				return left._add(value);
			} else {
				if (right == null) {
					right = new RedBlackTree<T>(this);
				}
				return right._add(value);
			}
		}
	}

	private RedBlackTree<T> prev() {
		if (null != left) {
			RedBlackTree<T> next = left;
			while (null != next.right) {
				next = next.right;
			}
			return next;
		} else{
			return prev(this);
		}
	}

	private RedBlackTree<T> prev(RedBlackTree<T> from) {
		if (null == from.parent) {
			return null;
		}
		if (from.parent.left == from) {
			return prev(from.parent);
		}
		return from.parent;
	}

	private RedBlackTree<T> next() {
		if (null != right) {
			RedBlackTree<T> next = right;
			while (null != next.left) {
				next = next.left;
			}
			return next;
		} else {
			return next(this);
		}
	}

	private RedBlackTree<T> next(RedBlackTree<T> from) {
		if (null == from.parent) {
			return null;
		}
		if (from.parent.right == from) {
			return next(from.parent);
		}
		return from.parent;
	}

	@Override
	public Iterator<RedBlackTree<T>> iterator() {
		return new Iterator<RedBlackTree<T>>() {
			private RedBlackTree<T> current = null;

			@Override
			public boolean hasNext() {
				return ((null == current) ? null != root() : null != current.next());
			}

			@Override
			public RedBlackTree<T> next() {
				if (null == current) {
					current = root();
					while (current.prev() != null) {
						current = current.prev();
					}
				} else {
					current = current.next();
				}
				return current;
			}

			@Override
			public void remove() {throw new RuntimeException("unimplemented");}
		};
	}

	private void balancingCase1(RedBlackTree<T> node) {
		if (node.parent == null) {
			node.colour = Colour.BLACK;
		} else {
			balancingCase2(node);
		}
	}

	private void balancingCase2(RedBlackTree<T> node) {
		if (node.parent.colour == Colour.BLACK) {
			return; /* Tree is still valid */
		} else {
			balancingCase3(node);
		}
	}

	private void balancingCase3(RedBlackTree<T> node) {
		RedBlackTree<T> uncle = uncle(node);

		if ((uncle != null) && (uncle.colour == Colour.RED)) {
			node.parent.colour = Colour.BLACK;
			uncle.colour = Colour.BLACK;
			RedBlackTree<T> granny = granny(node);
			granny.colour = Colour.RED;
			balancingCase1(granny);
		 } else {
			 balancingCase4(node);
		 }
	}

	private void balancingCase4(RedBlackTree<T> node) {
		RedBlackTree<T> granny = granny(node);

		if ((node == node.parent.right) && (node.parent == granny.left)) {
			rotateLeft(node.parent);
			node = node.left;
		} else if ((node == node.parent.left) && (node.parent == granny.right)) {
			rotateRight(node.parent);
			node = node.right;
		}
		balancingCase5(node);
	}

	private void balancingCase5(RedBlackTree<T> node) {
		RedBlackTree<T> granny = granny(node);

		node.parent.colour = Colour.BLACK;
		granny.colour = Colour.RED;
		if (node == node.parent.left) {
			rotateRight(granny);
		} else {
			rotateLeft(granny);
		}
	}

	private void rotateLeft(RedBlackTree<T> node) {
		RedBlackTree<T> right = node.right;
		node.right = right.left;
		if (right.left != null) {
			right.left.parent = node;
		}
		right.parent = node.parent;
		if (node.parent == null) {
			//root = right;
		} else if (node == node.parent.left) {
			node.parent.left = right;
		} else {
			node.parent.right = right;
		}
		right.left = node;
		node.parent = right;
	}

	private void rotateRight(RedBlackTree<T> node) {
		RedBlackTree<T> left = node.left;
		node.left = left.right;
		if (left.right != null) {
			left.right.parent = node;
		}
		left.parent = node.parent;
		if (node.parent == null) {
			//root = left;
		} else if (node == node.parent.left) {
			node.parent.left = left;
		} else {
			node.parent.right = left;
		}
		left.right = node;
		node.parent = left;
	}

	private RedBlackTree<T> uncle(RedBlackTree<T> node) {
		RedBlackTree<T> granny = granny(node);
		if (granny == null) {
			return null; // No grandparent means no uncle
		}
		if (node.parent == granny.left) {
			return granny.right;
		} else {
		  return granny.left;
		}
	}

	private RedBlackTree<T> granny(RedBlackTree<T> node) {
		if ((node != null) && (node.parent != null)) {
			return node.parent.parent;
		} else {
			return null;
		}
	}

	public String toString() {
		return 
		//((left != null)? left + " / ": "")  + 
		value.toString() 
		//+ ((right != null)? " \\ " + right: "")
		;
	}
}
