package poi.tomtom;

/**
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public abstract class PoiCommon implements Poi {

	private int type;
	private int size = 0;
	private PoiContainer parent;

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
	protected void setSize(int size) {
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
}
