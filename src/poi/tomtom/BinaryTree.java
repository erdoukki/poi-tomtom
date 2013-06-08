package poi.tomtom;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
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
		Properties codes = new Properties();
		try {
			codes.loadFromXML(new FileInputStream(fileName));
			for (Entry<Object, Object> code: codes.entrySet()) {
				String key = (String) code.getKey();
				@SuppressWarnings("unchecked")
				T value = (T) code.getValue();
				put(new Bit(new BitContainer(key), 0), value);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void put(Bit key, T item) {
		if (key.isLast()) {
			this.item = item;
			log.trace(key().bits().toString());
		} else {
			if (key.get()) {
				if (node1 == null) {
					node1 = new BinaryTree<T>(this);
				}
				key.inc();
				node1.put(key, item);
			} else {
				if (node0 == null) {
					node0 = new BinaryTree<T>(this);
				}
				key.inc();
				node0.put(key, item);
			}
		}
	}

	public T get(Bit key) {
		if (key.isLast()) {
			log.trace(key().bits().toString());
			return item;
		}
		if (key.get()) {
			if (node1 == null) {
				if (null == item) {
					throw new BitException(Integer.toString(key.index()));
				}
				return item;
			}
			key.inc();
			try {
				return node1.get(key);
			} catch (BitException e) {
				int index = Integer.parseInt(e.getMessage());
				throw new BitException(Integer.toString(index - 1));
			}
		} else {
			if (node0 == null) {
				if (null == item) {
					throw new BitException(Integer.toString(key.index()));
				}
				return item;
			}
			key.inc();
			try {
				return node0.get(key);
			} catch (BitException e) {
				int index = Integer.parseInt(e.getMessage());
				throw new BitException(Integer.toString(index - 1));
			}
		}
	}

	private Bit key() {
		if (root != null) {
			if (root.node0 == this) {
				Bit key = root.key();
				key.bits().append(new BitContainer(new byte[] {0}, 0, 1)); // + "0";
				return key;
			} else {
				Bit key = root.key();
				key.bits().append(new BitContainer(new byte[] {1}, 7, 1)); // + "1";
				return key;
			}
		}
		return new Bit(new BitContainer(new byte[0], 0, 0),0);
	}

	private BinaryTree<T> prev() {
		if (null != node0) {
			return node0.prev();
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

	public Set<Bit> keySet() {
		return new Set<Bit>() {

			@Override
			public boolean add(Bit arg0) {throw new RuntimeException("unimplemented");}

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
			public Iterator<Bit> iterator() {
				return new Iterator<Bit>() {
					private BinaryTree<T> current = prev();

					@Override
					public boolean hasNext() {
						return null != current.next();
					}

					@Override
					public Bit next() {
						current = current.next();
						return current.key();
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
}
