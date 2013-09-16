package poi.tomtom;

/**
<h3>Record 13.</h3>
This Record is an POI Record. The POI description is compressed (compression type 13)
<p/>
The Format is the following :
<p/>
<table border="1">
  <tr><th width=110>Bytes</th><th>Description</th></tr>
  <tr><td>1 byte</td><td>T: The Record Type = <b>0x0d</b> or <b>0x1d</b></td></tr>
  <tr><td>1 bytes</td><td>N : the size of this record, minus 11 (the size of the fixed part of the record)</td></tr>
  <tr><td>4 bytes</td><td>X: {@link #longitude Longitude} (in decimal degrees). This value has to be divide by 100000</td></tr>
  <tr><td>4 bytes</td><td>Y: {@link #latitude Latitude} (in decimal degrees). This value has to be divide by 100000</td></tr>
  <tr><td>1 byte</td><td>C: {@link #code unknown code}</td></tr>
  <tr><td>N bytes</td><td>N: {@link #description encoded POI description}</td></tr>
</table>

* @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
*/
public class Poi13 extends PoiCommon implements PoiRecord {

	public static final int TYPE_1D = 29;

	/**
	 * size length in bytes.
	 */
	public static final int HEADER = 11;

	private int longitude;
	private int latitude;
	/**
	 * unknown code.
	 * Describes how many of the first bytes of the description are .... <i>(unknown! probably related with crpoi.dat)</i>
<p/>
<table border="1">
  <tr><th width=110>Value (binary)</th><th width=110>Size (bytes)</th></tr>
  <tr><td>xxxxxx00</td><td>0</td></tr>
  <tr><td>xxxxxx01</td><td>1</td></tr>
  <tr><td>xxxxxx10</td><td>3</td></tr>
  <tr><td>xxxxxx11</td><td>4</td></tr>
</table>
	 * 
	 */
	private int code;
	private byte[] unknown;
	private byte[] name = new byte[] {0};

	private final Dictionary dictionary;

	/**
	 * constructor.
	 * @param dictionary 
	 */
	public Poi13(int type, PoiContainer parent, Dictionary dictionary) {
		super(type, parent);
		this.dictionary = dictionary;
	}

	@Override
	public int size() {
		return super.size() - HEADER;
	}

	@Override
	public void setSize(int length) {
		switch (code & 3) { // 000000xx
			case 0:
				unknown = null;
				name = new byte[length];
				break;
			case 1:
				unknown = new byte[1];
				name = new byte[length - 1];
				break;
			case 2:
				unknown = new byte[3];
				name = new byte[length - 3];
				break;
			case 3:
				unknown = new byte[4];
				name = new byte[length - 4];
				break;
		}
		super.setSize(length + HEADER);
	}
	@Override
	public int getLon() {
		return longitude;
	}
	public void setLon(int longitude) {
		this.longitude = longitude;
	}
	@Override
	public int getLat() {
		return latitude;
	}
	public void setLat(int latitude) {
		this.latitude = latitude;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
		setSize(size());
	}
	public byte[] getUnknown() {
		return unknown;
	}
	public void setUnknown(byte[] unknown) {
		this.unknown = unknown;
		super.setSize(this.name.length + (unknown != null?unknown.length:0) + HEADER);
	}

	/**
	 * Decodes POI description.
	 *
	 * @return decoded description
	 */
	public String getName() {
		if (code == 6) {
			return super.decode(name);
		}
		return decode(name);
	}

	/**
	 * Encodes POI description.
	 *
	 * @return decoded description
	 */
	public void setName(String name) {
		setName(name, CharMode.UTF16);
	}

	/**
	 * Encodes POI description.
	 *
	 * @return decoded description
	 */
	public void setName(String name, CharMode charMode) {
		if (code == 6) {
			this.name = super.encode(name, charMode);
		} else {
			this.name = encode(name, charMode);
		}
		super.setSize(this.name.length + (unknown != null?unknown.length:0) + HEADER);
	}

	byte[] name() {
		return name;
	}
	public void setName(byte[] name) {
		this.name = name;
		super.setSize(this.name.length + (unknown != null?unknown.length:0) + HEADER);
	}

	/** Decodes POI description. */
	@Override
	protected String decode(byte[] description) {
		return dictionary.decode(description);
	}

	/** Encode plain text to byte array */
	@Override
	byte[] encode(String description, CharMode mode) {
		return dictionary.encode(description);
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("Poi13 [S:" + size() + ", lon:" + getLon() + ", lat:" + getLat() + ", c:" + getCode() + ", u:" + (unknown==null?null:hex(unknown)) + ", n:");
		return str.toString() + getName() + "]";
	}
}