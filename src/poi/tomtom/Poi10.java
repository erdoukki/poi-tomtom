package poi.tomtom;

/**
<H3>Record 10 & 26.</H3>
This Record is an POI Record. The POI description is compressed (compression type 10)
<P>
The Format is the following :
<P>
<table border="1">
  <TR><TD width=110>Bytes</TD><TD>Description</TD></TR>
  <TR><TD>1 byte</TD><TD>T: The Record Type = <b>0x0A</b> or <b>0x1A</b></TD></TR>
  <TR><TD>1 bytes</TD><TD>S: the size of this record, minus 8 (the size of the fixed part of the record)</TD></TR>
  <tr><td>3 bytes</td><td>X: {@link PoiRec04#longitude Encoded Longitude}</a></td></tr>
  <tr><td>3 bytes</td><td>Y: {@link PoiRec04#latitude Encoded Latitude}</a></td></tr>
  <TR><TD>S bytes</TD><TD>N: {@link #description encoded POI description}</TD></TR>
</table>

 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class Poi10 extends Poi07 {

	public static final int TYPE_0A = 10;
	public static final int TYPE_1A = 26;

	/**
Record 10 transposition table.
<p/>
<table border=1>
  <tr><th>code</th><th>text character</th></tr>
  <tr><td align=center>0</td><td align=center>a</td></tr>
  <tr><td align=center>1</td><td align=center>b</td></tr>
  <tr><td align=center>2</td><td align=center>c</td></tr>
  <tr><td align=center>3</td><td align=center>d</td></tr>
  <tr><td align=center>4</td><td align=center>e</td></tr>
  <tr><td align=center>5</td><td align=center>f</td></tr>
  <tr><td align=center>6</td><td align=center>g</td></tr>
  <tr><td align=center>7</td><td align=center>h</td></tr>
  <tr><td align=center>8</td><td align=center>i</td></tr>
  <tr><td align=center>9</td><td align=center>j</td></tr>
  <tr><td align=center>10</td><td align=center>k</td></tr>
  <tr><td align=center>11</td><td align=center>l</td></tr>
  <tr><td align=center>12</td><td align=center>m</td></tr>
  <tr><td align=center>13</td><td align=center>n</td></tr>
  <tr><td align=center>14</td><td align=center>o</td></tr>
  <tr><td align=center>15</td><td align=center>p</td></tr>
  <tr><td align=center>16</td><td align=center>q</td></tr>
  <tr><td align=center>17</td><td align=center>r</td></tr>
  <tr><td align=center>18</td><td align=center>s</td></tr>
  <tr><td align=center>19</td><td align=center>t</td></tr>
  <tr><td align=center>20</td><td align=center>u</td></tr>
  <tr><td align=center>21</td><td align=center>v</td></tr>
  <tr><td align=center>22</td><td align=center>w</td></tr>
  <tr><td align=center>23</td><td align=center>x</td></tr>
  <tr><td align=center>24</td><td align=center>y</td></tr>
  <tr><td align=center>25</td><td align=center>z</td></tr>
  <tr><td align=center>26</td><td align=center>0</td></tr>
  <tr><td align=center>27</td><td align=center>1</td></tr>
  <tr><td align=center>28</td><td align=center>2</td></tr>
  <tr><td align=center>29</td><td align=center>3</td></tr>
  <tr><td align=center>30</td><td align=center>4</td></tr>
  <tr><td align=center>31</td><td align=center>5</td></tr>
  <tr><td align=center>32</td><td align=center>6</td></tr>
  <tr><td align=center>33</td><td align=center>7</td></tr>
  <tr><td align=center>34</td><td align=center>8</td></tr>
  <tr><td align=center>35</td><td align=center>9</td></tr>
  <tr><td align=center>36</td><td align=center>space</td></tr>
  <tr><td align=center>37</td><td align=center>.</td></tr>
  <tr><td align=center>38</td><td align=center>-</td></tr>
</table>
	 */
	public static final char[] letters = "abcdefghijklmnopqrstuvwxyz0123456789 .-".toCharArray();

	/**
	 * constructor.
	 */
	public Poi10(int type, PoiContainer parent) {
		super(type, parent);
	}

	/**
	 * Decodes POI description.

<p/>
The compressed method is the following :
<p/>
Each pairs of 2 bytes (little indian encoding) can be decompressed in 3 character. The optional last byte give one character.
<br/>
The characters can be obtained by calculate the modulus-40 three times on the 2 bytes, and 1 time on the 1 byte. This give 3 indexes on a transposition table :
<p/>
if modulus is 0, this is consider as the end of string.
<p/>
For example
<pre>
starting with the bytes : 0x59 0x20, this give the value 0x2059 (8281)
get the modulus 1 : 8281 % 40 = 1           : A
get the modulus 2 : (8281/40) % 40 = 7      : G
get the modulus 3 : ((8281/40)/40) % 40 = 5 : E
</pre>

	 * @return decoded description
	 */
	@Override
	protected String decode(byte[] name) {
		StringBuffer s = new StringBuffer();
		int index = name.length;
		while (index > 0) {
			int i = 2;
			if (index < 2) {
				i = 1;
			}
			int n = readN(name.length - index, i);
			index -= i;
			if (i == 2) {
				i++;
			}
			while (i > 0) {
				int k = (int) n % 40;
				if (k == 0) {
					return s.toString();
				}
				s.append(letters[k - 1]);
				i--;
				n = n / 40;
			}
		}
		return s.toString();
	}

	int readN(int index, int n) {
		int l = 0;
		for (int i = 0; i < n; i++) {
			int b = name[index + i] & 0xff;
			//log.debug(": 0x" + Integer.toHexString(b));
			b <<= 8 * i;
			l += b;
		}
		return l;
	}
}