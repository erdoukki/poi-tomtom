package poi.tomtom;

import java.io.File;

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
	public static final int SIZE = 11;

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
	private byte code;
	private byte[] unknown;
	private byte[] name = new byte[] {0};

	protected static final BinaryTree<String> tree = new BinaryTree<String>();

	/**
	 * constructor.
	 */
	public Poi13(int type, PoiContainer parent) {
		super(type, parent);
		//TODO setSize(SIZE);
	}

	@Override
	public void setSize(int size) {
		switch (code & 3) { // 000000xx
			case 0:
				unknown = null;
				name = new byte[size];
				break;
			case 1:
				unknown = new byte[1];
				name = new byte[size - 1];
				break;
			case 2:
				unknown = new byte[3];
				name = new byte[size - 3];
				break;
			case 3:
				unknown = new byte[4];
				name = new byte[size - 4];
				break;
		}
		super.setSize(size);
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
	public byte getCode() {
		return code;
	}
	public void setCode(byte code) {
		this.code = code;
		setSize(size());
	}
	public byte[] getUnknown() {
		return unknown;
	}
	public void setUnknown(byte[] unknown) {
		this.unknown = unknown;
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
	public void setName(String name, CharMode charMode) {
		this.name = encode(name, charMode);
		//TODO super.setSize(this.name.length + HEADER);
	}

	byte[] name() {
		return name;
	}
	public void setName(byte[] name) {
		this.name = name;
		//TODO super.setSize(this.name.length + HEADER);
	}

	/**
	 * Decodes POI description.
<p/>
The compressed method is the following :
<p/>
Each character of the POI description consume a variable number a bits, using 
a transposition table (see {@link Enc29 Enc29} for the transposition table). 
<p/>
The block of data has to be used as a series of bits. There is a special sequence 
of bit that have a special meaning (End of String).
<br/>
The way the bits are arranged in the block is a little special : each byte must 
reverse the position of his bits as the following :
<br/>
The byte (binary format) 0bABCDEFGH will be transform in 0bHGFEDCBA
<br/>
So 0b10010010 will be transform in 0b01001001
<p/>
Then, the data can be decoded, using the transposition table.
<br/>
For example
<pre>
starting with the bytes : 0x68 0x78 0x3c 0xb2 0x01
This can be transform in binary : 01101000 01111000 00111100 10010010 00000001
and then revert                 : 00010110 00011110 00111100 01001101 10000000
and then, using b-tree table    : 00010 1100 0011 1100 0111 1000 1001 1011 (0000000)
and then                        : station
</pre>
	 *
	 * @return decoded description
	 */
	@Override
	protected String decode(byte[] name) {
		Bit bit = new Bit(new BitContainer(name,true), 0);
		StringBuffer result = new StringBuffer();
		try {
			while (!bit.isLast()) {
				String value = tree.get(bit);
				if (value.length() == 0) {
					/** eos */
					return result.toString();
				}
				result.append(value);
			}
		} catch (BitException e) {
			int index = Integer.parseInt(e.getMessage());
			throw new BitException("unknown (" + (result.length() + 1) + ") " + bit.bits().toString().substring(index));
		}
		return result.toString();
	}

	static void putAll() {
		tree.loadFromXml(System.getProperty("user.dir") + File.separator + "etc" + File.separator + Poi09.DEFAULT_XML);
	}

	static void put(BitContainer key, String s) {
		tree.put(new Bit(key, key.start()), s);
	}

	@Override
	public String toString() {
		return "Poi13 [S:" + size() + ", lon:" + getLon() + ", lat:" + getLat() + ", c:" + getCode() + ", u:" + hex(unknown) + ", n:" + getName() + "]";
	}
}