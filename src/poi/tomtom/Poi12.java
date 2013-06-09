package poi.tomtom;

/**
<H3>Record 12 & 28.</H3>
This Record is an POI Record. The POI description is compressed (compression type 12)
<p/>
The Format is the following :
<p/>
<table border="1">
  <tr><th width=110>Bytes</th><th>Description</th></tr>
  <tr><td>1 byte</td><td>T: The Record Type = <b>0x0C</b> or <b>0x1C</b></td></tr>
  <tr><td>1 bytes</td><td>N: the size of this record, minus 8 (the size of the fixed part of the record)</td></tr>
  <tr><td>3 bytes</td><td>X: {@link PoiRec04#longitude Encoded Longitude}</a></td></tr>
  <tr><td>3 bytes</td><td>Y: {@link PoiRec04#latitude Encoded Latitude}</a></td></tr>
  <tr><td>S bytes</td><td>N: {@link #description encoded POI description}</td></tr>
</table>

 * @since TomTom v5.
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class Poi12 extends Poi07 {

	public static final int TYPE_0C = 12;
	public static final int TYPE_1C = 28;

	/**
Record 12 transposition text table.
<p/>
<b>Caution</b> : this table DOES NOT contain letter <b>Q</b> ! 
<p/>
  <table border=1>
    <tr><th>5-bit code</th><th>text character</th></tr>
    <tr><td align=center>00000</td><td align=center>a</td></tr>
    <tr><td align=center>00001</td><td align=center>b</td></tr>
    <tr><td align=center>00010</td><td align=center>c</td></tr>
    <tr><td align=center>00011</td><td align=center>d</td></tr>
    <tr><td align=center>00100</td><td align=center>e</td></tr>
    <tr><td align=center>00101</td><td align=center>f</td></tr>
    <tr><td align=center>00110</td><td align=center>g</td></tr>
    <tr><td align=center>00111</td><td align=center>h</td></tr>
    <tr><td align=center>01000</td><td align=center>i</td></tr>
    <tr><td align=center>01001</td><td align=center>j</td></tr>
    <tr><td align=center>01010</td><td align=center>k</td></tr>
    <tr><td align=center>01011</td><td align=center>l</td></tr>
    <tr><td align=center>01100</td><td align=center>m</td></tr>
    <tr><td align=center>01101</td><td align=center>n</td></tr>
    <tr><td align=center>01110</td><td align=center>o</td></tr>
    <tr><td align=center>01111</td><td align=center>p</td></tr>
    <tr><td align=center>10000</td><td align=center>r</td></tr>
    <tr><td align=center>10001</td><td align=center>s</td></tr>
    <tr><td align=center>10010</td><td align=center>t</td></tr>
    <tr><td align=center>10011</td><td align=center>u</td></tr>
    <tr><td align=center>10100</td><td align=center>v</td></tr>
    <tr><td align=center>10101</td><td align=center>w</td></tr>
    <tr><td align=center>10110</td><td align=center>x</td></tr>
    <tr><td align=center>10111</td><td align=center>y</td></tr>
    <tr><td align=center>11000</td><td align=center>z</td></tr>
    <tr><td align=center>11001</td><td align=center>space</td></tr>
    <tr><td align=center>11010</td><td align=center>end of string</td></tr>
    <tr><td align=center>11011</td><td align=center>(</td></tr>
    <tr><td align=center>11100</td><td align=center>)</td></tr>
    <tr><td align=center>11101</td><td align=center>&</td></tr>
    <tr><td align=center>11110</td><td align=center>'</td></tr>
    <tr><td align=center>11111</td><td align=center>-</td></tr>
</table>
	 */
	public static final char[] letters = "abcdefghijklmnoprstuvwxyz >()&\'-".toCharArray();

	/**
Record 12 transposition numeric table.
<p/>
<table border=1>
  <tr><th>4-bit code</th><th>Numeric character</th></tr>
  <tr><td align=center>0000</td><td align=center>End of String</td></tr>
  <tr><td align=center>0001</td><td align=center>0</td></tr>
  <tr><td align=center>0010</td><td align=center>1</td></tr>
  <tr><td align=center>0011</td><td align=center>2</td></tr>
  <tr><td align=center>0100</td><td align=center>3</td></tr>
  <tr><td align=center>0101</td><td align=center>4</td></tr>
  <tr><td align=center>0110</td><td align=center>5</td></tr>
  <tr><td align=center>0111</td><td align=center>6</td></tr>
  <tr><td align=center>1000</td><td align=center>7</td></tr>
  <tr><td align=center>1001</td><td align=center>8</td></tr>
  <tr><td align=center>1010</td><td align=center>9</td></tr>
  <tr><td align=center>1011</td><td align=center>-</td></tr>
  <tr><td align=center>1100</td><td align=center>(</td></tr>
  <tr><td align=center>1101</td><td align=center>)</td></tr>
  <tr><td align=center>1110</td><td align=center>+</td></tr>
  <tr><td align=center>1111</td><td align=center>#</td></tr>
</table>
	 */
	public static final char[] digits =  "0123456789-()+#".toCharArray();

	/**
	 * constructor.
	 */
	protected Poi12(int type, PoiContainer parent) {
		super(type, parent);
	}

	/**
	 * Decodes POI description.

<p/>
These records have 2 blocks of data
<ul>
  <li>Text Block that contain the POI description (alphabetic characters). These characters are coded on 5 bits</li>
  <li>Phone number (numeric characters). These characters are coded on 4 bits</li>
</ul>
<p/>
Each block have a special 5-bit code that is an end of string. After this code, the 2nd block directly begin, until the special 4-bit code that is the end of the phone number.
<p/>
The way the bits are arranged in the block is a little special : the coded string data have to be first reverse. Then, the group of 5 bits (and then 4 bits) have to be parse from the end of the reversed data, as the following :
<br/>
The bytes (binary format) 0bABCDEFGH 0bIJKLMNOP 0bQRSTUVWX will be reversed in 0bQRSTUVWX 0bIJKLMNOP 0bABCDEFGH, and the the groups of 5-bits will be (QRST) UVWXI JKLMN OPABC DEFGH
<p/>
Then, the data can be decoded, using the transposition table, and the resulting clear text have to be reverse.
<p/>
For example
<pre>
starting with the bytes : 0x51 0x02 0x89 0x5c 0xd3 0x21 0x03
This can be transform in binary : 01010001 00000010 10001001 01011100 11010011 00100001 00000011
and the revert string           : 00000011 00100001 11010011 01011100 10001001 00000010 01010001
and then, using the tables      : 0000 0011 0010 0001 11010 01101 01110 01000 10010 00000 10010 10001
and then                        : "210" & "noitats"
and then, using b-tree table    : Description:"station" & Phone Number:"012"
</pre>

	 *
	 * @return decoded description
	 */
	@Override
	protected String decode(byte[] name) {
		StringBuffer result = new StringBuffer();
		StringBuffer temp = reverseString(name);
		// log.debig(temp);
		/** leters */
		while (temp.length() > 4) {
			String key = temp.substring(temp.length() - 5);
			// log.debig(key);
			int i = Integer.parseInt(key, 2);
			// log.debig(i);
			// System.out.println(letters[i]);
			result.append(letters[i]);
			temp.delete(temp.length() - 5, temp.length());
			if (letters[i] == '>') {
				break;
			}
		}
		/** digits */
		while (temp.length() > 3) {
			String key = temp.substring(temp.length() - 4);
			// log.debig(key);
			int i = Integer.parseInt(key, 2);
			if (i == 0) {
				break;
			}
			// log.debig(i);
			// log.debig(letters[i]);
			result.append(digits[i - 1]);
			temp.delete(temp.length() - 4, temp.length());
		}
		return result.toString();
	}

	private StringBuffer reverseString(byte[] name) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < name.length; i++) {
			String s = Integer.toBinaryString(name[i] & 0xff);
			// log.debig(s);
			s = "00000000".substring(s.length()) + s;
			// log.debig(s);
			result.insert(0, s);
		}
		return result;
	}
}