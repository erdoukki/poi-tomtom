package poi.tomtom;

/**
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class Bit {
	private final BitContainer bits;
	private int index;

	public Bit(BitContainer bits, int index) {
		this.bits = bits;
		this.index = index;
	}

	public int index() {
		return index;
	}

	public BitContainer bits() {
		return bits;
	}

	public boolean isLast() {
		return index > (bits.length() - 1);
	}

	void inc() {
		index++;
	}

	void dec() {
		index--;
	}

	boolean get() {
		return bits.get(index);
	}

	boolean set(boolean value) {
		return bits.set(index, value);
	}
}
