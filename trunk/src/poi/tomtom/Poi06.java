package poi.tomtom;

/**
<H3>Record 06 & 22.</H3>
This Record is an POI Record. The POI description is a numeric value.
<BR>
The Format is the following :
<P>
<table border="1">
  <TR><TD width=110>Bytes</TD><TD>Description</TD></TR>
  <TR><TD>1 byte</TD><TD>T: The Record Type = <b>0x06</b> or <b>0x16</b></TD></TR>
  <TR><TD>3 bytes</TD><TD>X: {@link Poi04#longitude Encoded Longitude}</a></TD></TR>
  <TR><TD>3 bytes</TD><TD>Y: {@link Poi04#latitude Encoded Latitude}</a></TD></TR>
  <TR><TD>3 bytes</TD><TD>N: unsigned 24 bits {@link #name numeric value} (little indian)</TD></TR>
</table>

* @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
*/
public class Poi06 extends Poi04 {

	public static final int TYPE_06 = 6;
	public static final int TYPE_16 = 22;

	public static final int SIZE = 10;

	private int name;

	/**
	 * constructor.
	 */
	public Poi06(int type, PoiContainer parent) {
		super(type, parent);
	}

	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Poi06 [S:" + size() + ", lon:" + nf(getLongitude()) + ", lat:" + nf(getLatitude()) + ", n:" + name + "]";
	}
}