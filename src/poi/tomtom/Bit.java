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
		return index > bits.start() + bits.length() - 1;
	}

	void inc() {
		index++;
	}

	void dec() {
		index--;
	}

	boolean get() {
		int mask = 128 >> (index % 8);
		int i = index >> 3;
		return (mask & bits.buff(i)) != 0;
	}

	void set(boolean value) {
		int mask = 128 >> (index % 8);
		int i = index >> 3;
		//boolean old = mask & buff[i];
		bits.mask(i, mask, value);
		//return old;
	}
}
