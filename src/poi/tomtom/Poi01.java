package poi.tomtom;

/**
 * Skipper record:
 * 1 byte T: type (always 1)
 * 4 bytes L: number of bytes in the file, including and starting at this record, that contain data for POI enclosed in the given rectangle
 * 4 bytes X1: longitude coordinate of the west edge of the rectangle
 * 4 bytes Y1: latitude coordinate of the south edge of the rectangle
 * 4 bytes X2: longitude coordinate of the east edge of the rectangle
 * 4 bytes Y2: latitude coordinate of the north edge of the rectangle
 * 
 * @author Orlin Tomov
 */
public class Poi01 implements Poi  {

	static final byte type = 1;
	private int length;
	private int longitude1;
	private int latitude1;
	private int longitude2;
	private int latitude2;

	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getLongitude1() {
		return longitude1;
	}
	public void setLongitude1(int longitude1) {
		this.longitude1 = longitude1;
	}
	public int getLatitude1() {
		return latitude1;
	}
	public void setLatitude1(int latitude1) {
		this.latitude1 = latitude1;
	}
	public int getLongitude2() {
		return longitude2;
	}
	public void setLongitude2(int longitude2) {
		this.longitude2 = longitude2;
	}
	public int getLatitude2() {
		return latitude2;
	}
	public void setLatitude2(int latitude2) {
		this.latitude2 = latitude2;
	}
	public static byte getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Poi01 [L:" + length + " X1:" + longitude1 + " Y1:" + latitude1 + " X2:" + longitude2 + " Y2:" + latitude2 + "]";
	}
}
