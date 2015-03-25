package poi.tomtom;

/**
<H3>Record 09 & 25.</H3>
This Record is an POI Record. The POI description is compressed (compression type 09)
<P>
The Format is the following :
<P>
<table border="1">
  <tr><TD width=110>Bytes</td><td>Description</td></tr>
  <tr><td>1 byte</td><td>T: The Record Type = <b>0x09</b> or <b>0x19</b></td></tr>
  <tr><td>1 bytes</td><td>S : the size of this record, minus 8 (the size of the fixed part of the record)</td></tr>
  <tr><td>3 bytes</td><td>X: {@link PoiRec04#longitude Encoded Longitude}</a></td></tr>
  <tr><td>3 bytes</td><td>Y: {@link PoiRec04#latitude Encoded Latitude}</a></td></tr>
  <tr><td>S bytes</td><td>N: {@link #description encoded POI description}</td></tr>
</table>

 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class Poi09 extends Poi07 {

	public static final int TYPE_09 = 9;
	public static final int TYPE_19 = 25;

	private final Dictionary dictionary;

	/**
	 * constructor.
	 */
	public Poi09(int type, PoiContainer parent, Dictionary dictionary) {
		super(type, parent);
		if (dictionary == null) {
			dictionary = new PoiContext().getDictionary();
		}
		this.dictionary = dictionary;
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
		return "Poi09 [S:" + size() + ", lon:" + nf(getLongitude()) + ", lat:" + nf(getLatitude()) + ", n:" + getName() + "]";
	}
}
