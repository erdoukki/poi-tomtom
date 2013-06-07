package poi.tomtom;

/**
<h3>Record 02 & 15.</h3>
This record is a {@link PoiRecord POI Record}. This record is used in .OV2 file. It is a plain text record
<br/>
The format is the following :
<p/>
<table border="1">
  <tr><th width=110>Bytes</th><th>Description</th></tr>
  <tr><td>1 byte</td><td>T: Record Type = <b>0x02 or <b>0x0f</b></b></td></tr>
  <tr><td>4 bytes</td><td>L: the total size of this record (including the T and L fields)</td></tr>
  <tr><td>4 bytes</td><td>X: {@link #longitude Longitude} (in decimal degrees). This value has to be divide by 100000</td></tr>
  <tr><td>4 bytes</td><td>Y: {@link #latitude Latitude} (in decimal degrees). This value has to be divide by 100000</td></tr>
  <tr><td>N bytes</td><td>Plain text {@link #description POI description}</td></tr>
</table>

 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class Poi02 extends PoiCommon implements PoiRecord {

	public static final byte POI02 = 2;
	public static final byte POI03 = 3;
	public static final byte POI0F = 15;

	private int length;
	private int longitude;
	private int latitude;
	private String name;

	public Poi02(int type, PoiContainer parent) {
		super(type, parent);
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
