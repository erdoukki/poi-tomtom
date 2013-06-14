package poi.tomtom;

/**
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class BitContainer {

	private byte[] buff;
	private int length;
	private int start;
	boolean flip;

	public BitContainer(byte[] buff, boolean flip) {
		this(buff, 0, buff.length * 8, flip);
	}

	BitContainer(byte[] buff, int start, int length) {
		this(buff, start, length, false);
	}

	BitContainer(byte[] buff, int start, int length, boolean flip) {
		this.buff = buff;
		this.start = start;
		this.length = length;
		this.flip = flip;
	}

	BitContainer(String s) {
		this(s, false);
	}

	public BitContainer(String s, boolean flip) {
		start = 0;
		length = s.length();
		buff = new byte[(length + 7) / 8];
		for (int i = 0; i < buff.length; i++) {
			String t;
			if (s.length() < 8) {
				t = s + "00000000".substring(s.length());
			} else {
				t = s.substring(0, 8);
				s = s.substring(8);
			}
			buff[i] = (byte)Integer.parseInt(t, 2);
		}
		this.flip = flip;
	}

	/**
	 * Returns the length of this bit set.
	 */
	public int length() {
		return length;
	}

	/**
	 * Returns the start of this bit set.
	 */
	public int start() {
		return start;
	}

	public boolean isEmpty() {
		return length() == 0;
	}

	// ------------------ container

	/**
	 * Returns a copy of BitContainer starts <b>from</b> bits with given <b>size</b>.
	 */
	public BitContainer get(int from, int size) {
		if (size > length - start) {
			throw new BitException("Size is more than lenght!");
		}
		BitContainer result = new BitContainer(buff, start + from, size, flip);
		return result;
	}

	/**
	 * Append b to current.
	 */
	public void append(BitContainer b) {
		if (start + length + b.length > buff.length * 8) {
			/** new buffer */
			byte[] result = new byte[((start % 8) + length + b.length + 7) / 8];
			/** copy old to new (bytes) without unused */
			for (int k = 0; k < ((length + 7) / 8); k++) {
				result[k] = buff[k + (start / 8)];
			}
			start = start % 8;
			buff = result;
		}
		//Bit b1 = new Bit(b, b.start);
		//Bit b2 = new Bit(result, start + length);
		for (int k = 0; k < b.length; k++, length++) {
			set(length, b.get(k));
		}
	}

	// ------------------ bit

	/**
	 * Does index bit is set
	 */
	boolean get(int index) {
		int mask;
		if (flip) {
			mask = 1 << ((start + index) % 8);
		} else {
			mask = 128 >> ((start + index) % 8);
		}
		int i = (start + index) / 8;
		return (mask & buff[i]) != 0;
	}

	/**
	 * Set index bit with value.
	 */
	boolean set(int index, boolean value) {
		int mask;
		if (flip) {
			mask = 1 << ((start + index) % 8);
		} else {
			mask = 128 >> ((start + index) % 8);
		}
		int i = (start + index) / 8;
		boolean old = (mask & buff[i]) != 0;
		mask(i, mask, value);
		return old;
	}

	/**
	 */
	public void delete(int count) {
		length -= count;
		start += count;
	}

	/**
	 * Returns the index of the first bit that is set to false that occurs on or after the specified starting index.
	 */
	public int nextClearBit(int fromIndex) {
		for (int k = fromIndex; k < length; k++) {
			if (!get(k)) {
				return k - fromIndex;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the first bit that is set to true that occurs on or after the specified starting index.
	 */
	public int nextSetBit(int fromIndex) {
		for (int k = fromIndex; k < length; k++) {
			if (get(k)) {
				return k - fromIndex;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the last bit that is set to true that occurs on or after the specified starting index.
	 */
	public int lastSetBit(int fromIndex) {
		int last = -1;
		for (int k = fromIndex; k < length; k++) {
			if (get(k)) {
				last = length - k - 1;
				break;
			}
		}
		return last;
	}

	// ------------------ byte

	/**
	 * Masks a byte from this bit set. If op true is plus; op false is minus. 
	 * 
	 * @param index
	 * @param mask
	 * @param op
	 */
	private void mask(int index, int mask, boolean op) {
		if (op) {
			buff[index] |= mask;
		} else {
			buff[index] &= ~mask;
		}
	}

	// ------------------ common

	/**
	 * integer value of first count, may flip.
	 */
	public int toInt() {
		return Integer.parseInt(toString(), 2);
	}

	/**
	 * Returns a string representation of this bit set.
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		for (int i = start / 8; (i < buff.length) && (i < ((start + length + 7) / 8)); i++) {
			String s = Integer.toBinaryString(buff[i] & 0xff);
			s = "00000000".substring(s.length()) + s;
			if (flip) {
				if ((i == start / 8) && ((start % 8) != 0)) {
					s = s.substring(0, 8 - (start) % 8);
				} else if (i == ((start + length) / 8)) {
					s = s.substring(8 - ((start + length) % 8));
				}
				for (int j = s.length() - 1; j >= 0; j --) {
					result.append(s.charAt(j));
				}
			} else {
				if ((i == start / 8) && ((start % 8) != 0)) {
					s = s.substring((start) % 8);
				} else if (i == ((start + length) / 8)) {
					s = s.substring(0, ((start + length) % 8));
				}
				result.append(s);
			}
		}
		return result.toString();
	}
}
