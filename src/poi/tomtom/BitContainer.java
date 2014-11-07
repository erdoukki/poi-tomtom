package poi.tomtom;

/**
 * Bit set of a byte array.
 * 
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class BitContainer {

	private byte[] buff;
	private int length;
	private int start;
	private boolean flip;

	/**
	 * Construct bit set from a byte array <b>buff</b>. Could be flipped.
	 * 
	 * @param buff to construct from
	 * @param flip order
	 */
	public BitContainer(byte[] buff, boolean flip) {
		this(buff, 0, buff.length * 8, flip);
	}

	/**
	 * Construct bit set from a byte array <b>buff</b> beginning from <b>start</b> with a <b>length</b>.
	 * 
	 * @param buff to construct from
	 * @param start from
	 * @param length with
	 */
	BitContainer(byte[] buff, int start, int length) {
		this(buff, start, length, false);
	}

	/**
	 * Construct bit set from a byte array <b>buff</b> beginning from <b>start</b> with a <b>length</b>. Could be flipped.
	 * 
	 * @param buff to construct from
	 * @param start from
	 * @param length with
	 * @param flip order
	 */
	BitContainer(byte[] buff, int start, int length, boolean flip) {
		this.buff = buff;
		this.start = start;
		this.length = length;
		this.flip = flip;
	}

	/**
	 * Construct bit set from a <b>string</b>.
	 * 
	 * @param string to construct from
	 */
	BitContainer(String string) {
		start = 0;
		length = string.length();
		buff = new byte[(length + 7) / 8];
		for (int i = 0; i < buff.length; i++) {
			String t;
			if (string.length() < 8) {
				t = string + "00000000".substring(string.length());
			} else {
				t = string.substring(0, 8);
				string = string.substring(8);
			}
			buff[i] = (byte)Integer.parseInt(t, 2);
		}
		/** flip string, but not bytes */
		this.flip = false;
	}

	/**
	 * Returns the buff.
	 * 
	 * @return the buffer
	 */
	public byte[] buff() {
		return buff;
	}

	/**
	 * Returns the length of this bit set.
	 * 
	 * @return the length
	 */
	public int length() {
		return length;
	}

	/**
	 * Returns the start of this bit set.
	 * 
	 * @return the start of this bit set
	 */
	public int start() {
		return start;
	}

	/**
	 * Is it empty.
	 * 
	 * @return is it empty
	 */
	public boolean isEmpty() {
		return length() == 0;
	}

	// ------------------ container

	/**
	 * Append <b>bits</b> to the current set.
	 * 
	 * @param bits to add
	 */
	public void append(BitContainer bits) {
		if (start + length + bits.length > buff.length * 8) {
			/** new buffer */
			byte[] result = new byte[((start % 8) + length + bits.length + 7) / 8];
			/** copy old to new (bytes) without unused */
			for (int k = 0; k < ((length + 7) / 8); k++) {
				result[k] = buff[k + (start / 8)];
			}
			start = start % 8;
			buff = result;
		}
		//Bit b1 = new Bit(b, b.start);
		//Bit b2 = new Bit(result, start + length);
		for (int k = 0; k < bits.length; k++, length++) {
			set(length, bits.get(k));
		}
	}

	/**
	 * Returns a flipped copy of the set with given <b>size</b>.
	 * 
	 * @param size
	 */
	public BitContainer flip(int size) {
		if (size > length) {
			throw new BitException("Size is more than lenght!");
		}
		String s = toString(size);
		StringBuffer b = new StringBuffer();
		for (int i = s.length() - 1; i >=0; i--) {
			b.append(s.charAt(i));
		}
		BitContainer result = new BitContainer(b.toString());
		return result;
	}

	// ------------------ bit

	/**
	 * Does <b>index</b> bit is set.
	 * 
	 * @param index
	 * @return 
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
	 * Set <b>index</b> bit with <b>value</b>.
	 * 
	 * @param index
	 * @param value
	 * @return
	 */
	private boolean set(int index, boolean value) {
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
	 * Delete count bits from the beginning of the set.
	 * 
	 * @param count bits to delete.
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
		for (int k = length - 1; k >= fromIndex; k--) {
			if (get(k)) {
				last = k - fromIndex;
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
		return toString(length);
	}

	/**
	 * Returns a string representation of part of this bit set.
	 */
	public String toString(int size) {
		StringBuffer result = new StringBuffer();
		for (int i = start / 8; (i < buff.length) && (i < ((start + size + 7) / 8)); i++) {
			String s = Integer.toBinaryString(buff[i] & 0xff);
			s = "00000000".substring(s.length()) + s;
			if (flip) {
				if (i == ((start + size) / 8)) {
					s = s.substring(8 - ((start + size) % 8));
				}
				if ((i == start / 8) && ((start % 8) != 0)) {
					s = s.substring(0, s.length() - (start) % 8);
				}
				for (int j = s.length() - 1; j >= 0; j --) {
					result.append(s.charAt(j));
				}
			} else {
				if ((i == start / 8) && ((start % 8) != 0)) {
					s = s.substring((start) % 8);
				} else if (i == ((start + size) / 8)) {
					s = s.substring(0, ((start + size) % 8));
				}
				result.append(s);
			}
		}
		return result.toString();
	}
}
