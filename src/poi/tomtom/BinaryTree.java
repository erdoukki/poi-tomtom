package poi.tomtom;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class BinaryTree<T> {

	private static LogCategory log = LogCategory.getLogger(BinaryTree.class);

	private T item = null;
	private BitContainer key = null;
	private BinaryTree<T> node0;
	private BinaryTree<T> node1;
	private final BinaryTree<T> root;

	public BinaryTree() {
		root = null;
	}

	public BinaryTree(BinaryTree<T> root) {
		this.root = root;
	}

	public BinaryTree(String fileName) {
		root = null;
		loadFromXml(fileName);
	}

	public void loadFromXml(String fileName) {
		clean();
		Properties codes = new Properties();
		try {
			codes.loadFromXML(new FileInputStream(fileName));
			for (Entry<Object, Object> code: codes.entrySet()) {
				String key = (String) code.getKey();
				if (key.contains("?")) {
					continue;
				}
				@SuppressWarnings("unchecked")
				T value = (T) code.getValue();
				put(new BitContainer(key), value);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clean() {
		item = null;
		key = null;
		node0 = null;
		node1 = null;
		//root = null;
	}

	public T getItem() {
		return item;
	}

	public BitContainer getKey() {
		if (key == null) {
			return key();
		}
		return key;
	}

	public void put(BitContainer key, T item) {
		if (getKey().length() == key.length()) {
			if ((this.item == null) && (this.key == null)) {
				this.item = item;
				this.key = key;
				//log.trace(getKey().toString());
			} else {
				log.error("conflict when add " + item + " key " + this.key + ", existing " + node1.item);
			}
		} else {
			if (key.get(getKey().length())) {
				if (node1 == null) {
					node1 = new BinaryTree<T>(this);
				} else if (node1.item != null) {
					log.error("conflict when add " + item + ", existing " + node1.item);
				}
				node1.put(key, item);
			} else {
				if (node0 == null) {
					node0 = new BinaryTree<T>(this);
				} else if (node0.item != null) {
					log.error("conflict when add " + item + ", existing " + node0.item);
				}
				node0.put(key, item);
			}
		}
	}

	public BinaryTree<T> find(BitContainer key) {
		if (getKey().length() == key.length()) {
			//log.trace(getKey().toString());
			return this;
		}
		if (key.get(getKey().length())) {
			if (node1 == null) {
				if (null == item) {
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
				if (null == item) {
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

	private BinaryTree<T> prev() {
		if (null != node0) {
			return node0.prev();
		} else if (null != node1) {
			return node1.prev();
		} else {
			return this;
		}
	}

	private BinaryTree<T> next() {
		if (null != node1) {
			if (null != node1.item) {
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

	private BinaryTree<T> next(BinaryTree<T> from) {
		if (from == node1) {
			if (null != root) {
				return root.next(this);
			} else {
				return null;
			}
		} else if (null != item) {
			return this;
		} else if (null != node1) {
			return node1.prev();
		} else if (null != root) {
			return root.next(this);
		} else {
			return null;
		}
	}

	public Set<BitContainer> keySet() {
		return new Set<BitContainer>() {

			@Override
			public boolean add(BitContainer arg0) {throw new RuntimeException("unimplemented");}

			@Override
			public boolean addAll(Collection arg0) {throw new RuntimeException("unimplemented");}

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

			@Override
			public Object[] toArray(Object[] arg0) {throw new RuntimeException("unimplemented");}
		};
	}

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
			//log.trace(node.getKey() + " \"" + node.getItem() + "\"");
		}
		return unknownKeys;
	}
}
