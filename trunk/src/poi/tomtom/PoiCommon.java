package poi.tomtom;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public abstract class PoiCommon implements Poi {

	private int type;
	private int size = 0;
	private PoiContainer parent;

	private static final DecimalFormat nf;
	static {
		DecimalFormatSymbols ns = new DecimalFormatSymbols();
		ns.setDecimalSeparator('.');
		nf = new DecimalFormat("0.00000", ns);
	};

	/**
	 * constructor.
	 */
	protected PoiCommon(int type, PoiContainer parent) {
		this.type = type;
		this.parent = parent;
	}

	/**
	 *  parent.
	 */
	@SuppressWarnings("unchecked")
//	@Override
	public <P extends PoiContainer> P getParent() {
		return (P) parent;
	}

	/**
	 *  type.
	 */
	public int getType() {
		return type;
	}

	/**
	 *  in bytes.
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 *  in bytes.
	 */
	public void setSize(int size) {
		//System.out.println(getClass() + " size=" + this.size + " delta=" + (size - this.size));
		if (parent != null) {
			int delta = size - this.size;
			((PoiCommon)getParent()).setSize(getParent().size() + delta);
		}
		this.size = size;
	}

	/**
	 *  in bytes.
	 */
	@Override
	public int offset() {
		if (parent == null) {
			return size;
		}
		int offset = parent.getOffset(this);
		return offset;
	}

	/** Encode plain text to byte array */
	byte[] encode(String description, CharMode mode) {
		byte[] buff = null;
		switch (mode) {
			case UTF16: {
				buff = new byte[description.length() * 2 + CharMode.UTF16.prefix.length + 1];
				int i = 0;
				for (byte b: CharMode.UTF16.prefix) {
					buff[i++] = b;
				}
				for (i = 0; i < description.length(); i++) {
					char c = description.charAt(i);
					if ((0x42e <= c) && (c <= 0x44f)) {
						buff [i * 2 + CharMode.UTF16.prefix.length] = (byte)(c - 0x405); buff [i * 2 + CharMode.UTF16.prefix.length + 1] = 0x29;
					} else if (0x410 <= c) {
						buff [i * 2 + CharMode.UTF16.prefix.length] = (byte)(c - 0x32f); buff [i * 2 + CharMode.UTF16.prefix.length + 1] = 0x28;
					} else {
						buff [i * 2 + CharMode.UTF16.prefix.length] = (byte)c; buff [i * 2 + CharMode.UTF16.prefix.length + 1] = 0x21;
					}
				}
				break;
			}
		}
		return buff;
	}

	/** Decode plain text from byte array */
	String decode(byte[] description) {
		byte[] buff = description;
		if ((0x9b == b2ch(buff[0])) && (0x9b == b2ch(buff[1]))) {
			System.out.println();
//TODO			buff = new BitContainer(buff).flipBytes(2);
		}
		if ((CharMode.UTF16.prefix[0] == b2ch(buff[0])) && (CharMode.UTF16.prefix[1] == b2ch(buff[1]))) {
			/** to UTF16 */
			StringBuffer result = new StringBuffer();
			for (int i = 2; i < buff.length - 1; i+=2) {
				switch (buff[i+1]) {
					case 0x21: {
						result.append((char)buff[i]);
						break;
					} 
					case 0x28: {
						if ((0xe1 <= b2ch(buff[i])) || ((0x57 <= b2ch(buff[i])) && (b2ch(buff[i]) <= 0x9f))) {
							result.append((char)(b2ch(buff[i]) + 0x32f));
						} else {
							result.append(" 0x");
							String x = "0" + Integer.toHexString(b2ch(buff[i+1]));
							x = x.substring(x.length() - 2);
							result.append(x);
							x = "0" + Integer.toHexString(b2ch(buff[i]));
							x = x.substring(x.length() - 2);
							result.append(x);
							result.append(" ");
						}
						break;
					} 
					case 0x29: {
						if ((b2ch(buff[i]) >= 0x29) && (b2ch(buff[i]) <= 0x4b)) {
							result.append((char)(buff[i] + 0x405));
						} else {
							result.append(" 0x");
							String x = "0" + Integer.toHexString(b2ch(buff[i+1]));
							x = x.substring(x.length() - 2);
							result.append(x);
							x = "0" + Integer.toHexString(b2ch(buff[i]));
							x = x.substring(x.length() - 2);
							result.append(x);
							result.append(" ");
						}
						break;
					}
					default: {
						result.append(" 0x");
						String x = "0" + Integer.toHexString(b2ch(buff[i+1]));
						x = x.substring(x.length() - 2);
						result.append(x);
						x = "0" + Integer.toHexString(b2ch(buff[i]));
						x = x.substring(x.length() - 2);
						result.append(x);
						result.append(" ");
					}
				}
			}
			return result.toString();
		}
		if (buff[buff.length - 1] == 0) {
			return new String(buff,0,buff.length - 1);
		}
		return new String(buff);
	}

	private static int b2ch(byte b) {
		return b & 0xff;
	}

	public static String hex(byte[] buff) {
		StringBuffer result = new StringBuffer();
		int j = 0;
		// String s = reverseBits(buff);
		int size = buff.length;
		while (j < size) {
			for (int i = 0; (i < 16) && (j + i < buff.length); i++) {
				String x = "0" + Integer.toHexString(b2ch(buff[j + i]));
				x = x.substring(x.length() - 2);
				result.append(x);
				result.append(" ");
				// System.out.print(x + " ");
			}
			// if ((j+16) > size) {
			// System.out.print(" " + s.substring(j*8,size*8) + " ");
			// } else {
			// System.out.print(" " + s.substring(j*8,(j+16)*8) + " ");
			// }
			// System.out.println();
			j += 16;
		}
		return result.toString();
	}

	public enum CharMode {
		//Unicode("Unicode", null),
		//ANSI("ANSI", null),
		//UTF8("UTF-8", null),   /** ef bb bf */
		UTF16("UTF-16", new byte[] {0x3e, 0x3e}); /** ff fe */

		private final String text;
		private final byte[] prefix;

		private CharMode(String text, byte[] prefix) {
			this.text = text;
			this.prefix = prefix;
		}

		@Override
		public String toString() {
			return text;
		}
	};

	public static String nf(double n) {
		return nf.format(n);
	}
}
