package poi.tomtom;

/**
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class BitContainer {

	private byte[] buff;
	private int length;
	private int start;

	public BitContainer(byte[] buff) {
		this(buff, false);
	}

	public BitContainer(byte[] buff, boolean flip) {
		this.buff = buff;
		length = buff.length * 8;
		start = 0;
		if (flip) {
			flipBytes(0);
		}
	}

	BitContainer(byte[] buff, int start, int length) {
		this.buff = buff;
		this.start = start;
		this.length = length;
	}

	public BitContainer(String s) {
		start = 0;
		length = s.length();
		buff = new byte[(length + 7) >> 3]; // /8
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
	}

	/**
	 * Flip the bits.
	 */
	public void flip() {
		BitContainer temp = new BitContainer(new byte[(length + 7) >> 3], 0, length); // /8
		Bit b1 = new Bit(this, start);
		Bit b2 = new Bit(temp, start + length - 1);
		for (int k = 0; k < length; k++, b1.inc(), b2.dec()) {
			b2.set(b1.get());
		}
		buff = temp.buff;
	}

	/**
	 * Returns a new BitContainer composed of bits from this BitContainer from fromIndex(inclusive) to toIndex(exclusive).
	 */
	public BitContainer get(int fromIndex, int toIndex) {
		int size = toIndex - fromIndex;
		BitContainer temp = new BitContainer(new byte[(size + 7) >> 3], 0, size); // /8
		Bit b1 = new Bit(this, start + fromIndex);
		Bit b2 = new Bit(temp, 0);
		for (int k = 0; k < size; k++, b1.inc(), b2.inc()) {
			b2.set(b1.get());
		}
		return temp;
	}

	/**
	 */
	public void append(BitContainer b) {
		BitContainer temp = null;
		if (start + length + b.length > buff.length * 8) {
			temp = new BitContainer(new byte[(length + b.length + 7) >> 3], 0, length + b.length); // /8
			Bit b1 = new Bit(this, start);
			Bit b2 = new Bit(temp, 0);
			for (int k = 0; k < length; k++, b1.inc(), b2.inc()) {
				b2.set(b1.get());
			}
			start = 0;
		} else {
			temp = this;
		}
		Bit b1 = new Bit(b, b.start);
		Bit b2 = new Bit(temp, start + length);
		for (int k = 0; k < b.length; k++, b1.inc(), b2.inc()) {
			b2.set(b1.get());
		}
		length += b.length;
		buff = temp.buff;
	}

	/**
	 * Returns the index of the first bit that is set to false that occurs on or after the specified starting index.
	 */
	public int nextClearBit(int fromIndex) {
		Bit b = new Bit(this, start + fromIndex);
		for (int k = 0; k < length - fromIndex; k++, b.inc()) {
			if (!b.get()) {
				return k;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the first bit that is set to true that occurs on or after the specified starting index.
	 */
	public int nextSetBit(int fromIndex) {
		Bit b = new Bit(this, start + fromIndex);
		for (int k = 0; k < length - fromIndex; k++, b.inc()) {
			if (b.get()) {
				return k;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the last bit that is set to true that occurs on or after the specified starting index.
	 */
	public int lastSetBit(int fromIndex) {
		Bit b = new Bit(this, start + length - 1);
		int last = -1;
		for (int k = 0; k < length - fromIndex; k++, b.dec()) {
			if (b.get()) {
				last = length - fromIndex - k - 1;
				break;
			}
		}
		return last;
	}

	/**
	 * Flips the bits for the bytes.
	 */
	public byte[] flipBytes(int fromIndex) {
		for (int i = fromIndex; i < buff.length; i++) {
			byte b = 0;
			byte mask = 1;
			byte result;
			for (int j = 0; j < 4; j++) {
				result = (byte) (mask & buff[i]);
				result <<= 7 - (j << 1); // *2
				b |= result;
				mask <<= 1;
			}
			int filter = 8;
			for (int j = 0; j < 4; j++) {
				result = (byte) (mask & buff[i]);
				result >>= 1 + (j << 1); // *2
				b |= (result & filter);
				mask <<= 1;
				filter >>= 1;
			}
			buff[i] = b;
		}
		return buff;
	}

	/**
	 */
	public void delete(int count) {
		length -= count;
		start += count;
	}

	/**
	 * Masks a byte from this bit set.
	 * 
	 * @param index
	 * @param mask
	 * @param op
	 */
	void mask(int index, int mask, boolean op) {
		if (op) {
			buff[index] |= mask;
		} else {
			buff[index] &= ~mask;
		}
	}

	/**
	 * Returns a byte from this bit set.
	 * 
	 * @param index
	 */
	byte buff(int index) {
		return buff[index];
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

	/**
	 */
	public int toInt(int count, boolean flip) {
		BitContainer i = get(0, count);
		if (flip) {
			i.flip();
		}
		return i.toInt();
	}

	/**
	 */
	int toInt() {
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
	 * Returns a string representation of <b>count</b> bits of this bit set.
	 */
	public String toString(int count) {
		StringBuffer result = new StringBuffer();
		for (int i = start / 8; (i < buff.length) && (i < ((start + count + 7) >> 3)); i++) {
			String s = Integer.toBinaryString(buff[i] & 0xff);
			if ((i == start / 8) && ((start % 8) != 0)) {
				s = "00000000".substring(s.length()) + s;
				s = s.substring((start) % 8);
			} else if (i == ((start + count) >> 3)) {
				s = "00000000".substring(s.length()) + s;
				s = s.substring(0, ((start + count) % 8));
			} else {
				result.append("00000000".substring(s.length()));
			}
			result.append(s);
		}
		return result.toString();
	}
}
