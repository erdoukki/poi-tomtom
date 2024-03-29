package poi.tomtom;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Binary tree container. Every element has a {@link BitContainer} key and contains a value.
 * The length of key defines level of the element. The length of the root element's key is zero. 
 * On every next level the length is plus one.
 * 
 * (r)oot
 *   \ 
 *   (t)his
 *   / \
 * (0) (1) nodes
 * 
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class BinaryTree<T> {

	private static LogCategory log = LogCategory.getLogger(BinaryTree.class);

	private T value = null;
	private BitContainer key = null;
	private BinaryTree<T> node0;
	private BinaryTree<T> node1;
	private final BinaryTree<T> root;

	/**
	 * Constructs the root element contains key with zero length.
	 * 
	 */
	public BinaryTree() {
		this(null);
	}

	/**
	 * Constructs an element with the parameter root.
	 *  
	 * @param root
	 */
	public BinaryTree(BinaryTree<T> root) {
		this.root = root;
	}

	/**
	 * Extracts the value of the element.
	 * 
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Extracts the key of the element even if key is not set yet.
	 * 
	 * @return the key
	 */
	public BitContainer getKey() {
		if (key == null) {
			return key();
		}
		return key;
	}

	/**
	 * Recursive put. If element exists already then logs an error.
	 * 
	 * @param key
	 * @param value
	 * @return the element
	 */
	public BinaryTree<T> put(BitContainer key, T value) {
		if (getKey().length() == key.length()) {
			if ((this.value == null) && (this.key == null)) {
				this.value = value;
				this.key = key;
				//log.trace(getKey().toString());
			} else {
				log.error("conflict when add " + value + " key " + this.key + ", existing " + node1.value);
			}
			return this;
		} else {
			if (key.get(getKey().length())) {
				if (node1 == null) {
					node1 = new BinaryTree<T>(this);
				} else if (node1.value != null) {
					log.error("conflict when add " + value + ", existing " + node1.value);
				}
				return node1.put(key, value);
			} else {
				if (node0 == null) {
					node0 = new BinaryTree<T>(this);
				} else if (node0.value != null) {
					log.error("conflict when add " + value + ", existing " + node0.value);
				}
				return node0.put(key, value);
			}
		}
	}

	/**
	 * Recursive find.
	 * 
	 * @param key looking for
	 * @return found element
	 */
	public BinaryTree<T> find(BitContainer key) {
		if (getKey().length() == key.length()) {
			//log.trace(getKey().toString());
			return this;
		}
		if (key.get(getKey().length())) {
			if (node1 == null) {
				if (null == value) {
					throw new BitException(Integer.toString(getKey().length()));
				}
				return this;
			}
			try {
				return node1.find(key);
			} catch (BitException e) {
				int index = Integer.parseInt(e.getMessage());
				throw new BitException(Integer.toString(index - 1));
			}
		} else {
			if (node0 == null) {
				if (null == value) {
					throw new BitException(Integer.toString(getKey().length()));
				}
				return this;
			}
			try {
				return node0.find(key);
			} catch (BitException e) {
				int index = Integer.parseInt(e.getMessage());
				throw new BitException(Integer.toString(index - 1));
			}
		}
	}

	/**
	 * Extracts the key of the element when it is not set yet.
	 * 
	 * @return the key
	 */
	private BitContainer key() {
		if (root != null) {
			if (root.node0 == this) {
				BitContainer key = root.key();
				key.append(new BitContainer(new byte[] {0}, 0, 1)); // + "0";
				return key;
			} else {
				BitContainer key = root.key();
				key.append(new BitContainer(new byte[] {1}, 7, 1)); // + "1";
				return key;
			}
		}
		return new BitContainer(new byte[0], 0, 0);
	}

	/**
	 * Recursive extracts the previous element, if exist.
	 * 
	 * @return the previous element
	 */
	private BinaryTree<T> prev() {
		if (null != node0) {
			return node0.prev();
		} else if (null != node1) {
			return node1.prev();
		} else {
			return this;
		}
	}

	/**
	 * Recursive extracts the next element, if exist.
	 * 
	 * @return the next element
	 */
	private BinaryTree<T> next() {
		if (null != node1) {
			if (null != node1.value) {
				return node1;
			} else {
				return node1.next();
			}
		} else if (null != root) {
			return root.next(this);
		} else {
			return null;
		}
	}

	/**
	 * Recursive extracts the next element of the parameter element, if exist.
	 * 
	 * @return the next element
	 */
	private BinaryTree<T> next(BinaryTree<T> from) {
		if (from == node1) {
			if (null != root) {
				return root.next(this);
			} else {
				return null;
			}
		} else if (null != value) {
			return this;
		} else if (null != node1) {
			return node1.prev();
		} else if (null != root) {
			return root.next(this);
		} else {
			return null;
		}
	}

	/**
	 * Extracts the key set.
	 * 
	 * @return the key set
	 */
	public Set<BitContainer> keySet() {
		return new Set<BitContainer>() {

			@Override
			public boolean add(BitContainer arg0) {throw new RuntimeException("unimplemented");}

			@Override
			public boolean addAll(@SuppressWarnings("rawtypes") Collection arg0) {throw new RuntimeException("unimplemented");}

			@Override
			public void clear() {throw new RuntimeException("unimplemented");}

			@Override
			public boolean contains(Object arg0) {throw new RuntimeException("unimplemented");}

			@Override
			public boolean containsAll(Collection<?> arg0) {throw new RuntimeException("unimplemented");}

			@Override
			public boolean isEmpty() {throw new RuntimeException("unimplemented");}

			@Override
			public Iterator<BitContainer> iterator() {
				return new Iterator<BitContainer>() {
					private BinaryTree<T> current = null;

					@Override
					public boolean hasNext() {
						return ((null == current) ? null != prev() : null != current.next());
					}

					@Override
					public BitContainer next() {
						if (null == current) {
							current = prev();
						} else {
							current = current.next();
						}
						return current.getKey();
					}

					@Override
					public void remove() {throw new RuntimeException("unimplemented");}
				};
			}

			@Override
			public boolean remove(Object arg0) {throw new RuntimeException("unimplemented");}

			@Override
			public boolean removeAll(Collection<?> arg0) {throw new RuntimeException("unimplemented");}

			@Override
			public boolean retainAll(Collection<?> arg0) {throw new RuntimeException("unimplemented");}

			@Override
			public int size() {throw new RuntimeException("unimplemented");}

			@Override
			public Object[] toArray() {throw new RuntimeException("unimplemented");}

			@SuppressWarnings("unchecked")
			@Override
			public Object[] toArray(Object[] arg0) {throw new RuntimeException("unimplemented");}
		};
	}

	/**
	 * Extracts a set of undefined keys of the tree.
	 * 
	 * @return the set of undefined keys of the tree
	 */
	public Set<String> unknownKeys() {
		Set<String> unknownKeys = new HashSet<String>();
		for (BitContainer key: keySet()) {
			BinaryTree<T> node = find(key);
			BinaryTree<T> root = node.root;
			while (root != null) {
				if (root.node0 == null || root.node1 == null) {
					if (root.node0 == null) {
						if (!unknownKeys.contains(root.getKey() + "0")) {
							unknownKeys.add(root.getKey() + "0");
							//log.trace(root.getKey() + "0");
						}
					} 
					if (root.node1 == null) {
						if (!unknownKeys.contains(root.getKey() + "1")) {
							unknownKeys.add(root.getKey() + "1");
							//log.trace(root.getKey() + "1");
						}
					}
				}
				root = root.root;
			}
			//log.trace(node.getKey() + " \"" + node.getValue() + "\"");
		}
		return unknownKeys;
	}
}
