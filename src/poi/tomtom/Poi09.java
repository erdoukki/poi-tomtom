package poi.tomtom;

import java.io.File;

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

	static final String DEFAULT_XML = "codes.xml";

	private static final BinaryTree<String> tree = new BinaryTree<String>(System.getProperty("user.dir") + File.separator + "etc" + File.separator + DEFAULT_XML);

	/**
	 * constructor.
	 */
	public Poi09(int type, PoiContainer parent) {
		super(type, parent);
	}

	/**
	 * Decodes POI description.

<p/>
The compressed method is the following :
<p/>
Each character of the POI description consume a variable number a bits, using 
a transposition table (see {@link Enc09 Enc09} for the transposition table). 
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

<h3>Transposition table.</h3>
<p/>
<table border=1>
  <tr><th>b-tree sequence of bits</th><th>character</th></tr>
  <tr><td>                    1011</td><td>end of string</td></tr>
  <tr><td>                    0010</td><td>space</td></tr>
  <tr><td>    01100001110111001001</td><td>!</td></tr>
  <tr><td>            010001100011</td><td>"</td></tr>
  <tr><td> 10101111110000100001110</td><td>#</td></tr>
  <tr><td>   101011111100001001100</td><td>$</td></tr>
  <tr><td>  0000101101110111010011</td><td>%</td></tr>
  <tr><td>              0100010010</td><td>&amp</td></tr>
  <tr><td>              0100011010</td><td>'</td></tr>
  <tr><td>             01001010011</td><td>(</td></tr>
  <tr><td>             01100001100</td><td>)</td></tr>
  <tr><td>      101011111100000100</td><td>*</td></tr>
  <tr><td>          00001011011111</td><td>+</td></tr>
  <tr><td>              1010111011</td><td>,</td></tr>
  <tr><td>                01001011</td><td>-</td></tr>
  <tr><td>                 0000110</td><td>.</td></tr>
  <tr><td>              0000001101</td><td>/</td></tr>
  <tr><td>             10101110001</td><td>0</td></tr>
  <tr><td>             00001011010</td><td>1</td></tr>
  <tr><td>             01000110111</td><td>2</td></tr>
  <tr><td>             10101110101</td><td>3</td></tr>
  <tr><td>             10101111111</td><td>4</td></tr>
  <tr><td>            010010100100</td><td>5</td></tr>
  <tr><td>            101000110110</td><td>6</td></tr>
  <tr><td>            101000110100</td><td>7</td></tr>
  <tr><td>            000010110110</td><td>8</td></tr>
  <tr><td>            101011100001</td><td>9</td></tr>
  <tr><td>       10101111110000011</td><td>:</td></tr>
  <tr><td>    00001011011101010100</td><td>;</td></tr>
  <tr><td>                        </td><td>&lt</td></tr>
  <tr><td> 10101111110000100001100</td><td>=</td></tr>
  <tr><td> 10101111110000100001101</td><td>&gt</td></tr>
  <tr><td>   101011111100001010110</td><td>?</td></tr>
  <tr><td>  0000101101110111011110</td><td>@</td></tr>
  <tr><td>                 1010110</td><td>A</td></tr>
  <tr><td>                00001010</td><td>B</td></tr>
  <tr><td>                00000010</td><td>C</td></tr>
  <tr><td>                01100011</td><td>D</td></tr>
  <tr><td>               010010101</td><td>E</td></tr>
  <tr><td>               010001000</td><td>F</td></tr>
  <tr><td>                01000111</td><td>G</td></tr>
  <tr><td>                10100000</td><td>H</td></tr>
  <tr><td>              0000101100</td><td>I</td></tr>
  <tr><td>              0100010011</td><td>J</td></tr>
  <tr><td>               000000111</td><td>K</td></tr>
  <tr><td>                10100001</td><td>L</td></tr>
  <tr><td>                00001110</td><td>M</td></tr>
  <tr><td>              0000001100</td><td>N</td></tr>
  <tr><td>              0100011001</td><td>O</td></tr>
  <tr><td>                01000101</td><td>P</td></tr>
  <tr><td>           1010111000000</td><td>Q</td></tr>
  <tr><td>                01100000</td><td>R</td></tr>
  <tr><td>                 0000100</td><td>S</td></tr>
  <tr><td>               000011111</td><td>T</td></tr>
  <tr><td>             01100001101</td><td>U</td></tr>
  <tr><td>               000011110</td><td>V</td></tr>
  <tr><td>               011000010</td><td>W</td></tr>
  <tr><td>          01001010010101</td><td>X</td></tr>
  <tr><td>           1010111111001</td><td>Y</td></tr>
  <tr><td>             01000110000</td><td>Z</td></tr>
  <tr><td>      000010110111010111</td><td>[</td></tr>
  <tr><td>   011000011101110010000</td><td>\</td></tr>
  <tr><td>      000010110111011100</td><td>]</td></tr>
  <tr><td>                        </td><td>^</td></tr>
  <tr><td>     0000101101110101011</td><td>_</td></tr>
  <tr><td>      011000011101110011</td><td>`</td></tr>
  <tr><td>                    0011</td><td>a</td></tr>
  <tr><td>                  101010</td><td>b</td></tr>
  <tr><td>                   11010</td><td>c</td></tr>
  <tr><td>                   01101</td><td>d</td></tr>
  <tr><td>                     111</td><td>e</td></tr>
  <tr><td>                 0100100</td><td>f</td></tr>
  <tr><td>                  010011</td><td>g</td></tr>
  <tr><td>                  000001</td><td>h</td></tr>
  <tr><td>                    0111</td><td>i</td></tr>
  <tr><td>               000010111</td><td>j</td></tr>
  <tr><td>                 0000000</td><td>k</td></tr>
  <tr><td>                   00011</td><td>l</td></tr>
  <tr><td>                  010000</td><td>m</td></tr>
  <tr><td>                    1001</td><td>n</td></tr>
  <tr><td>                    1000</td><td>o</td></tr>
  <tr><td>                  011001</td><td>p</td></tr>
  <tr><td>              1010111001</td><td>q</td></tr>
  <tr><td>                    0101</td><td>r</td></tr>
  <tr><td>                   00010</td><td>s</td></tr>
  <tr><td>                    1100</td><td>t</td></tr>
  <tr><td>                   11011</td><td>u</td></tr>
  <tr><td>                 1010011</td><td>v</td></tr>
  <tr><td>                10100010</td><td>w</td></tr>
  <tr><td>              1010001100</td><td>x</td></tr>
  <tr><td>                01100010</td><td>y</td></tr>
  <tr><td>                 1010010</td><td>z</td></tr>
  <tr><td>101011111100000101000010</td><td>{</td></tr>
  <tr><td>                        </td><td>|</td></tr>
  <tr><td>101011111100000101000000</td><td>}</td></tr>



  <tr><td>   000010110111010101010</td><td>°  (U+00B0)</td></tr>
  <tr><td>        1010111111000011</td><td>´  (U+00B4)</td></tr>
  <tr><td>     0110000111011100101</td><td>º  (U+00BA)</td></tr>
  <tr><td>        1010111111011001</td><td>ª  (U+00aa)</td></tr>

  <tr><td>      000010110111010110</td><td>À  (U+00C0)</td></tr>
  <tr><td>         101011111101101</td><td>Á  (U+00C2)</td></tr>
  <tr><td>   000010110111011101010</td><td>Â  (U+00C2)</td></tr>
  <tr><td>   101011111100001001101</td><td>Ã  (U+00C3)</td></tr>
  <tr><td>           1010001101010</td><td>Ä  (U+00C4)</td></tr>
  <tr><td>          01100001110110</td><td>Å  (U+00C5)</td></tr>
  <tr><td>     1010111111000010100</td><td>Æ  (U+00C6)</td></tr>
  <tr><td>   011000011101110010001</td><td>Ç  (U+00C7)</td></tr>
  <tr><td>        0100101001010001</td><td>È  (U+00C8)</td></tr>
  <tr><td>        1010111111011000</td><td>É  (U+00C9)</td></tr>
  <tr><td> 10101111110000100000110</td><td>Ê  (U+00CA)</td></tr>
  <tr><td> 00001011011101110100100</td><td>Ë  (U+00CB)</td></tr>
  <tr><td>                        </td><td>Ì  (U+00CC)</td></tr>
  <tr><td>       01100001110111010</td><td>Í  (U+00CD)</td></tr>
  <tr><td> 00001011011101110100101</td><td>Î  (U+00CE)</td></tr>
  <tr><td>   101011111100001000010</td><td>Ï  (U+00CF)</td></tr>
  <tr><td>                        </td><td>Ð  (U+00D0)</td></tr>
  <tr><td>    10101111110000101010</td><td>Ñ  (U+00D1)</td></tr>
  <tr><td>   000010110111011101000</td><td>Ò  (U+00D2)</td></tr>
  <tr><td>    00001011011101110110</td><td>Ó  (U+00D3)</td></tr>
  <tr><td>          00001011011110</td><td>ô  (U+00D4)</td></tr>
  <tr><td>                        </td><td>Õ  (U+00D5)</td></tr>
  <tr><td>           0100011011011</td><td>Ö  (U+00D6)</td></tr>
  <tr><td>                        </td><td>×  (U+00D7)</td></tr>
  <tr><td>         011000011101111</td><td>Ø  (U+00D8)</td></tr>
  <tr><td>                        </td><td>Ù  (U+00D9)</td></tr>
  <tr><td>      011000011101110001</td><td>Ú  (U+00DA)</td></tr>
  <tr><td>  1010111111000001011011</td><td>Û  (U+00DB)</td></tr>
  <tr><td>          00001011011100</td><td>Ü  (U+00DC)</td></tr>
  <tr><td>                        </td><td>Ý  (U+00DD)</td></tr>
  <tr><td>                        </td><td>Þ  (U+00DE)</td></tr>
  <tr><td>               101011110</td><td>ß  (U+00DF)</td></tr>
  <tr><td>           0110000111010</td><td>à  (U+00Å0)</td></tr>
  <tr><td>            010001101100</td><td>á  (U+00Å1)</td></tr>
  <tr><td>         010001101101001</td><td>â  (U+00Å2)</td></tr>
  <tr><td>          01000110110101</td><td>ã  (U+00Å3)</td></tr>
  <tr><td>              1010111110</td><td>ä  (U+00Å4)</td></tr>
  <tr><td>           1010001101011</td><td>å  (U+00Å5)</td></tr>
  <tr><td>            010001100010</td><td>æ  (U+00Å6)</td></tr>
  <tr><td>           1010111000001</td><td>ç  (U+00Å7)</td></tr>
  <tr><td>            101000110111</td><td>è  (U+00Å8)</td></tr>
  <tr><td>              1010001111</td><td>é  (U+00Å9)</td></tr>
  <tr><td>        0000101101110110</td><td>ê  (U+00ÅA)</td></tr>
  <tr><td>          10101111110001</td><td>ë  (U+00ÅB)</td></tr>
  <tr><td>          01001010010110</td><td>ì  (U+00ÅC)</td></tr>
  <tr><td>             10101110100</td><td>í  (U+00ÅD)</td></tr>
  <tr><td>       00001011011101111</td><td>î  (U+00ÅE)</td></tr>
  <tr><td>       01100001110111011</td><td>ï  (U+00ÅF)</td></tr>
  <tr><td>                        </td><td>ð  (U+00F0)</td></tr>
  <tr><td>           1010111111010</td><td>ñ  (U+00F1)</td></tr>
  <tr><td>          10101111110111</td><td>ò  (U+00F2)</td></tr>
  <tr><td>             01100001111</td><td>ó  (U+00F3)</td></tr>
  <tr><td>   101011111100001010111</td><td>Ô  (U+00F4)</td></tr>
  <tr><td>      000010110111010100</td><td>õ  (U+00F5)</td></tr>
  <tr><td>              1010001110</td><td>ö  (U+00F6)</td></tr>
  <tr><td>                        </td><td>÷  (U+00F7)</td></tr>
  <tr><td>            011000011100</td><td>ø  (U+00F8)</td></tr>
  <tr><td>         010001101101000</td><td>ù  (U+00F9)</td></tr>
  <tr><td>          01001010010111</td><td>ú  (U+00FA)</td></tr>
  <tr><td>      101011111100001011</td><td>û  (U+00FB)</td></tr>
  <tr><td>              0100101000</td><td>ü  (U+00FC)</td></tr>
  <tr><td>        0100101001010000</td><td>ý  (U+00FD)</td></tr>
  <tr><td>                        </td><td>þ  (U+00FE)</td></tr>
  <tr><td>   000010110111010101011</td><td>ÿ  (U+00FF)</td></tr>
  <tr><td>101011111100001000110111</td><td>L  (U+0141)</td></tr>
  <tr><td>        0000101101110100</td><td>l  (U+0142)</td></tr>
  <tr><td>  1010111111000001011010</td><td>space ???</td></tr>

  <tr><td>101011111100001000100111</td><td>í  (U+00ÅD)</td></tr>
<table>
<br/>
<i>Note : this table is not complete, and must be complete if others sequences are discovered</i>

	 *
	 * @return decoded description
	 */
	@Override
	protected String decode(byte[] name) {
		// return new Enc09.decode(description);
		Bit bit = new Bit(new BitContainer(name.clone(), true), 0);
		StringBuffer result = new StringBuffer();
		try {
			while (!bit.isLast()) {
				String value = tree.get(bit);
				if (value.length() == 0) {
					// eos
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

	@Override
	public String toString() {
		return "Poi09 [S:" + size() + ", lon:" + nf(getLongitude()) + ", lat:" + nf(getLatitude()) + ", n:" + getName() + "]";
	}
}
