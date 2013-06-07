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

}
