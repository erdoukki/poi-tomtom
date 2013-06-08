package poi.tomtom;

/**
<H3>Record 06 & 22.</H3>
This Record is an POI Record. The POI description is plain text. It isn't compressed. .
<BR>
The Format is the following :
<P>
<table border="1">
  <TR><TD width=110>Bytes</TD><TD>Description</TD></TR>
  <TR><TD>1 byte</TD><TD>T: The Record Type = <b>0x07</b> or <b>0x17</b></TD></TR>
  <TR><TD>1 bytes</TD><TD>S : the size of this record, minus 8 (the size of the fixed part of the record)</TD></TR>
  <TR><TD>3 bytes</TD><TD>X: {@link Poi04#longitude Encoded Longitude}</a></TD></TR>
  <TR><TD>3 bytes</TD><TD>Y: {@link Poi04#latitude Encoded Latitude}</a></TD></TR>
  <tr><td>S-8 bytes</td><td>N: Plain text {@link #name POI description}</td></tr>
</table>

* @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
*/
public class Poi07 extends Poi04 {

	public static final int TYPE_07 = 7;
	public static final int TYPE_17 = 23;

	public static final int HEADER = 8;

	private byte[] name = new byte[] {0};

	/**
	 * constructor.
	 */
	protected Poi07(int type, PoiContainer parent) {
		super(type, parent);
		setSize(HEADER + name.length);
	}

	@Override
	public void setSize(int length) {
		name = new byte[length - HEADER];
		super.setSize(length);
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
		this.name = super.encode(name, charMode);
		super.setSize(this.name.length + HEADER);
	}

	public void setName(byte[] name) {
		this.name = name;
		super.setSize(this.name.length + HEADER);
	}

	@Override
	public String toString() {
		return "Poi07 [S:" + size() + ", lon:" + nf(getLongitude()) + ", lat:" + nf(getLatitude()) + ", n:" + getName() + "]";
	}
}