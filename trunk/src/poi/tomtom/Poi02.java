package poi.tomtom;

/**
 * Simple POI record:
 * 1 byte    	T: 	type (always 2)
 * 4 bytes   	L: 	length of this record in bytes (including the T and L fields)
 * 4 bytes   	X: 	longitude coordinate of the POI
 * 4 bytes   	Y: 	latitude coordinate of the POI
 * L-13 bytes	N: 	zero-terminated ASCII string specifying the name of the POI
 * 
 * @author Orlin Tomov
 */
public class Poi02 implements Poi {

	static final byte type = 2;
	private int length;
	private int longitude;
	private int latitude;
	private String name;

	public static byte getType() {
		return type;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getLongitude() {
		return longitude;
	}
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	public int getLatitude() {
		return latitude;
	}
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Poi02 [L:" + length + " X:" + longitude + " Y:" + latitude + " N:" + name + "]";
	}
}
