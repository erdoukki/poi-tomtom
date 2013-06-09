package poi.tomtom;

/**
<h3>Record 08 & 24.</h3>
This Record is an POI Record. The POI description is compressed (compression type 08)
<br/>
The Format is the following :
<p/>
<table border="1">
  <tr><td width=110>Bytes</td><td>Description</td></tr>
  <tr><td>1 byte</td><td>The Record Type = <b>0x08</b> or <b>0x18</b></td></tr>
  <tr><td>1 bytes</td><td>S: the size of this record, minus 8 (the size of the fixed part of the record)</td></tr>
  <tr><td>3 bytes</td><td>X: {@link PoiRec04#longitude Encoded Longitude}</a></td></tr>
  <tr><td>3 bytes</td><td>Y: {@link PoiRec04#latitude Encoded Latitude}</a></td></tr>
  <tr><td>S bytes</td><td>N: {@link #description encoded POI description}</td></tr>
</table>

 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class Poi08 extends Poi07 {

	public static final int TYPE_08 = 8;
	public static final int TYPE_18 = 24;

	/**
<h3>Transposition table <b>when (byte & 0x80) == 0.</b> </h3>
This table refers to what 5 bits must be transposed in what letter.
<p/>
<table border="1">
  <tr><th>5 bits value</th><th>Transposition letter</th></tr>
  <tr><td align=center>00000</td><td align=center>not used</td></tr>
  <tr><td align=center>00001</td><td align=center>.</td></tr>
  <tr><td align=center>00010</td><td align=center>&lt;space&gt;</td></tr>
  <tr><td align=center>00011</td><td align=center>S</td></tr>
  <tr><td align=center>00100</td><td align=center>a</td></tr>
  <tr><td align=center>00101</td><td align=center>e</td></tr>
  <tr><td align=center>00110</td><td align=center>r</td></tr>
  <tr><td align=center>00111</td><td align=center>i</td></tr>
  <tr><td align=center>01000</td><td align=center>o</td></tr>
  <tr><td align=center>01001</td><td align=center>n</td></tr>
  <tr><td align=center>01010</td><td align=center>s</td></tr>
  <tr><td align=center>01011</td><td align=center>t</td></tr>
  <tr><td align=center>01100</td><td align=center>l</td></tr>
  <tr><td align=center>01101</td><td align=center>d</td></tr>
  <tr><td align=center>01110</td><td align=center>c</td></tr>
  <tr><td align=center>01111</td><td align=center>h</td></tr>
  <tr><td align=center>10000</td><td align=center>u</td></tr>
  <tr><td align=center>10001</td><td align=center>m</td></tr>
  <tr><td align=center>10010</td><td align=center>g</td></tr>
  <tr><td align=center>10011</td><td align=center>p</td></tr>
  <tr><td align=center>10100</td><td align=center>b</td></tr>
  <tr><td align=center>10101</td><td align=center>k</td></tr>
  <tr><td align=center>10110</td><td align=center>f</td></tr>
  <tr><td align=center>10111</td><td align=center>z</td></tr>
  <tr><td align=center>11000</td><td align=center>v</td></tr>
  <tr><td align=center>11001</td><td align=center>A</td></tr>
  <tr><td align=center>11010</td><td align=center>C</td></tr>
  <tr><td align=center>11011</td><td align=center>B</td></tr>
  <tr><td align=center>11100</td><td align=center>M</td></tr>
  <tr><td align=center>11101</td><td align=center>P</td></tr>
  <tr><td align=center>11110</td><td align=center>G</td></tr>
  <tr><td align=center>11111</td><td align=center>-</td></tr>
</table>
	 */
	public static final char[] letters0 = "?. SaerionstldchumgpbkfzvACBMPG-".toCharArray();

	/**
<h3>Transposition table <b>when (byte & 0x80) == 0x80</b>.</h3>
This table refers transcription of the first quartet (4 high bits)
<p/>
<table border=1>
  <tr><th>quartet source</th><th>quartet destination</th></tr>
  <tr><td align=center>1000</td><td align=center>0010</td></tr>
  <tr><td align=center>1001</td><td align=center>0011</td></tr>
  <tr><td align=center>1010</td><td align=center>0100</td></tr>
  <tr><td align=center>1011</td><td align=center>0101</td></tr>
  <tr><td align=center>1100</td><td align=center>0110</td></tr>
  <tr><td align=center>1101</td><td align=center>0111</td></tr>
  <tr><td align=center>1110</td><td align=center>1110</td></tr>
  <tr><td align=center>1111</td><td align=center>1111</td></tr>
</table>
	 */
	public static final char[] letters8 = null;

	/**
	 * constructor.
	 */
	public Poi08(int type, PoiContainer parent) {
		super(type, parent);
	}

	/**
	 * Decodes POI description.
<p/>
The compressed method is the following :
<p/>
<ul>
  <li><b>if (next byte & 0x80) == 0</b>
  <br/>The next 2 bytes can be decompressed in 3 letters, each letter encoded on 
  5 bits (1 + 3*5 = 2 bytes)<br/>
  These 5 bits (32 values) must be transposed in letter (see {@link #letters0 letters0}
  for transposition table)<p/>
  example : these 2 bytes (binary format) present 3 characters : 0AAAAABB BBBCCCCC</li>

  <li><b>if (next byte & 0x80) == 0x80</b>
  <br/>The next byte encode one character, with a transcription of the first quartet (4 high bits) 
  (see {@link #letters8 letters8} for transposition table)<br/>The second quartet is 
  remain unchange</li>
</ul>
	 *
	 * @return decoded description
	 */
	@Override
	protected String decode(byte[] name) {
		StringBuffer s = new StringBuffer(size());
		for (int i = 0; i < name.length; i++) {
			int b = name[i] & 0xff;
			if ((b & 0x80) == 0) {
				// next 3 x 5 bits encoded to 3 chars
				i++;
				s.append(letters0[(b & 0x7c) >> 2]);
				if (i == name.length) {
					break;
				}
				int b2 = name[i];
				s.append(letters0[((b & 0x3) << 3) + ((b2 & 0xe0) >> 5)]);
				s.append(letters0[b2 & 0x1f]);
			} else {
				if (b < 0xe0) {
					s.append((char) (b - 0x60));
				} else {
					s.append((char) b);
				}
			}
		}
		return s.toString();
	}

}