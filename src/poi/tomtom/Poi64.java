package poi.tomtom;

/**
 * 1 byte      	T:    	type (always 100)
 * 4 bytes     	L:    	length of this record in bytes (including the T and L fields)
 * P bytes     	Name: 	zero-terminated ASCII string specifying the name of the POI type
 * L-P-5 bytes 	Icon: 	zero-terminated ASCII string specifying the name of the .BMP file
 * 
 * @author Orlin Tomov
 */
public class Poi64 {

	static final byte type = 0x64;
	private int length;
	private String name;
	private String icon;

	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public static byte getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Poi64 [L:" + length + " N:" + name + " I:" + icon + "]";
	}
}
