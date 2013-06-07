package poi.tomtom;

/**
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public interface PoiContainer extends Poi {

	/**
	 *  count of records.
	 */
	public int count();
	public Poi get(int index);
	public void add(int index, Poi poi);
	public Poi set(int index, Poi poi);

	/**
	 * returns offset to particular child poi record
	 * 
	 * @return offset to the child poi record
	 */
	public int getOffset(Poi to);
}
