package poi.tomtom;

/**
<H3>Record 05 & 21.</H3>
This Record is an POI Record. The POI description is a short numeric value.
<BR>
The Format is the following :
<P>
<table border="1">
  <TR><TD width=110>Bytes</TD><TD>Description</TD></TR>
  <TR><TD>1 byte</TD><TD>T: The Record Type = <b>0x05</b> or <b>0x15</b></TD></TR>
  <TR><TD>3 bytes</TD><TD>X: {@link Poi04#longitude Encoded Longitude}</a></TD></TR>
  <TR><TD>3 bytes</TD><TD>Y: {@link Poi04#latitude Encoded Latitude}</a></TD></TR>
  <TR><TD>2 bytes</TD><TD>N: unsigned 16 bits {@link #name numeric value} (little indian)</TD></TR>
</table>

* @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
*/
public class Poi05 extends Poi04 {

	public static final int TYPE_05 = 5;
	public static final int TYPE_15 = 21;

	private static final int SIZE = 11;

	private short name;

	/**
	 * constructor.
	 */
	protected Poi05(int type, PoiContainer parent) {
		super(type, parent);
		setSize(SIZE);
	}

	public short getName() {
		return name;
	}
	public void setName(short name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Poi04 [S:" + size() + ", lon:" + nf(getLongitude()) + ", lat:" + nf(getLatitude()) + ", n:" + name + "]";
	}
}