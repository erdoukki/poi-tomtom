package poi.tomtom;

/**
<H3>Record 04 & 20.</H3>
This record is an {@link PoiRecord POI Record}. There is no POI description isn't compressed. It is plain text.
<BR>
The format is the following :
<P>
<table border="1">
  <TR><TD width=110>Bytes</TD><TD>Description</TD></TR>
  <TR><TD>1 byte</TD><TD>T: The Record Type = <b>0x04 or <b>0x14</b></TD></TR>
  <TR><TD>3 bytes</TD><TD>X: {@link #longitude Encoded Longitude}</a></TD></TR>
  <TR><TD>3 bytes</TD><TD>Y: {@link #latitude Encoded Latitude}</a></TD></TR>
</table>

* @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
*/
public class Poi04 extends PoiCommon implements PoiRecord {

	public static final int TYPE_04 = 4;
	public static final int TYPE_14 = 20;

	public static final int SIZE = 7;

	private int latitude;
	private int longitude;


	/**
	 * constructor.
	 */
	public Poi04(int type, PoiContainer parent) {
		super(type, parent);
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
<H3>Encodes latitude.</H3>
Latitudes in POI Records are encoded on 3 bytes unsigned value (little indian) (in decimal degrees).
<BR>
The final value is obtain by this formula (considering X as the 3 bytes value) :
<P>
<PRE>
  Latitude = (X/100000 - 80)
</PRE>
*/
	public double getLatitude() {
		return latitude / LL - 80;
	}

/**
<H3>Encodes longitude.</H3>
Longitudes in POI Records are encoded on 3 bytes unsigned value (little indian) (in decimal degrees).
<BR>
Longitude could be obtain with the same formula as latitude, but there is some corrective value : 3 bytes only let store 16777216 values (so nearly 167° on 360°). Depending on the area zone the POI exists (refer to his parent Area Record), an offset should be applied.
<P>
The primary value is obtain by this formula (considering X as the 3 bytes value) :
<P>
	<I>Note : let compute integer value to minimize floating approximations</I>
	<P>
	Let apply this formula <BR>
<PRE>
  Longitude = (X - 8000000) <BR>
</PRE>
	until Longitude inside the 2 longitudes values stored in his parent Area Record.<BR>
	If Longitude if under the -180°, then add 360°.
	<P>
Europe don't have to applied these offset, but it's the case for USA.<P>
For example :
<PRE>
  For an Area Record of (-165.12345, 55.12345) - (-151.12345, 58.12345)  (Alaska)
  The POI Longitude is : 0x6C9785  that is 8755052 in decimal
     8755052 - 8000000 =    755052
      755052 - 8000000 =  -7244948
    -7244948 - 8000000 = -15244948   the value !!!
</PRE>
*/
	public double getLongitude() {
		int l = longitude;
		//log.debug("L: " + l);
		int l1 = ((Poi01)getParent()).getLon1();
		//log.debug("1: " + l1);
		int l2 = ((Poi01)getParent()).getLon2();
		//log.debug("2: " + l2);
		if (l > Math.max(l1,l2)) {
			while (l > Math.max(l1,l2)) {
				l -= 8000000;
				//log.debug("L: " + l);
			}
		} else {
			while (l < Math.min(l1,l2)) {
				l += 12000000;
				//log.debug("L: " + l);
			}
		}
		return l / LL;
	}

	@Override
	public String toString() {
		return "Poi04 [S:" + size() + ", lon:" + nf(getLongitude()) + ", lat:" + nf(getLatitude()) + "]";
	}
}
