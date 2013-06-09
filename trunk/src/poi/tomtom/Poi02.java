package poi.tomtom;

/**
<h3>Record 02 & 15.</h3>
This record is a {@link PoiRecord POI Record}. This record is used in .OV2 file. It is a plain text record
<br/>
The format is the following :
<p/>
<table border="1">
  <tr><th width=110>Bytes</th><th>Description</th></tr>
  <tr><td>1 byte</td><td>T: Record Type = <b>0x02</b> or <b>0x0f</b></td></tr>
  <tr><td>4 bytes</td><td>S: the total size of this record in bytes</td></tr>
  <tr><td>4 bytes</td><td>X: {@link #longitude Longitude} (in decimal degrees). This value has to be divide by 100000</td></tr>
  <tr><td>4 bytes</td><td>Y: {@link #latitude Latitude} (in decimal degrees). This value has to be divide by 100000</td></tr>
  <tr><td>N bytes</td><td>Plain text {@link #description POI description}</td></tr>
</table>

 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class Poi02 extends PoiCommon implements PoiRecord {

	public static final byte HEADER = 13;
	
	public static final byte TYPE_02 = 2;
	public static final byte TYPE_03 = 3;
	public static final byte TYPE_0F = 15;

	private int longitude;
	private int latitude;
	protected byte[] name = new byte[] {0};

	public Poi02(int type, PoiContainer parent) {
		super(type, parent);
		/** T, L, X, Y, N */
		setSize(HEADER + name.length);
	}

	@Override
	public void setSize(int length) {
		name = new byte[length - HEADER];
		super.setSize(length);
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

	/**
	 * Decodes POI description.
	 *
	 * @return decoded description
	 */
	public String getName() {
		return decode(name);
	}

	/**
	 * Encodes POI description.
	 *
	 * @return decoded description
	 */
	public void setName(String name, CharMode charMode) {
		this.name = encode(name, charMode);
		super.setSize(this.name.length + HEADER);
	}

	byte[] name() {
		return name;
	}
	public void setName(byte[] name) {
		this.name = name;
		super.setSize(this.name.length + HEADER);
	}

	@Override
	public String toString() {
		return "Poi0"+Integer.toHexString(getType())+" [S:" + size() + ", X:" + longitude + ", Y:" + latitude + ", N:" + getName() + "]";
	}
}
