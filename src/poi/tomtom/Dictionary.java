package poi.tomtom;

import java.util.SortedMap;
import java.util.TreeMap;

/**
<h3>Dictionary.</h3>

This is a structure of dictionary used to decode {@link Poi13 Record 13}.
<p>
<table border="1">
  <tr><th width=110>bytes</th><th>description</th></tr>
  <tr><td>4 bytes</td><td><tt><b>50 4f 49 53 = "POIS"</b></tt></td></tr>
  <tr><td>14 bytes</td><td><tt><b>01 00 01 00 00 00 00 00 00 00 00 00 00 00</b></tt></td></tr>
  <tr><td>1 byte</td><td><b>I</b>: size in bits of id</td></tr>
  <tr><td>2 bytes</td><td><b>K</b>: size in bytes of keys description</td></tr>
  <tr><td>2 bytes</td><td><b>N</b>: keys number. <b>X</b> letters + <tt><b>0x200</b></tt> entries</td></tr>
  <tr><td>K bytes</td><td>keys description</td></tr>
  <tr><td>2 bytes</td><td><tt><b>01 01</b></tt></td></tr>
  <tr><td>1 byte</td><td><tt><b><B>S</B>: string size length in bits.</b></tt></td></tr>
  <tr><td>1 byte</td><td><b>B</b>: number of bits per char</td></tr>
  <tr><td>4 byte</td><td><b>A</b>: id value of biggest single char</td></tr>
  <tr><td>4 byte</td><td>unknown</td></tr>
  <tr><td>4 bytes</td><td><b>M</b>: size in bytes of entries description</td></tr>
  <tr><td>M bytes</td><td>entries description</td></tr>
</table>

<h4>Keys description.</h4>

Initially the bytes of the keys description are transform in reverse order bits from <tt>0bABCDEFGH</tt> to <tt>0bHGFEDCBA</tt> (in binary format). 
<br/>
The <b>K</b> bytes describe <b>N</b> keys.
<p/>
The keys are described in a pair of data: 
<ul>
  <li>appendix - number of set bits finish with a clear bit. Example <tt><b>1110</b></tt></li>
  <li>id - <b>I</b> bits in reverse order</li>
</ul>
The first key is composed by the first few set bits, without the follows clear bit at the end.
<br/>
The <b>id</b> of first key is <tt><b>0000000000</b></tt>. The first key is <b>&lt;end-of-string&gt;</b> for the {@link Poi13 Record 13} description.
<p/>
Each other key is composed by the bits of previous key next to the last set bit and concatenated with reversed order bits of appendix of the current key.
<br/>
Example: if the previous key is <tt><b>1100110</b></tt> and the current appendix is <tt><b>110</b></tt>, 
take the bits next to the last set bit <tt><b>11001</b></tt> and concatenate with reverse bits of appendix to <tt><b>011</b></tt>. 
Than the new key will be <tt><b>11001011</b></tt>.
<p/>
All keys with <b>id</b> up to value <b>A</b> presents a single char with ASCII value composed by reversed order bits of the <b>id</b>.
<br/>
Example: if the <b>id</b> is <tt><b>0010011000</b></tt>, reverse it bits <tt><b>0001100100</b></tt> 
presents hex value <tt><b>0x64</b></tt> - the ASCII value of small latin letter <b>d</b>.
<br/>
The rest keys are for the entries.

<h4>Entries description.</h4>

Initially the bytes of the entries description are transform in reverse order bits from <tt>0bABCDEFGH</tt> to <tt>0bHGFEDCBA</tt> (in binary format). 
<br/>
The <b>M</b> bytes describe <tt>0x200</tt> entries (except at beginning 20 unknown bits <tt>00000000010000000001</tt>).
<p/>
Every next entry pairs to the key with next <b>id</b>.
<br/>
Every entry starts with length of the string - 10 bits long, bits are in reverse order.
<br/>
The entry is a string composed by ASCII chars with fixed <b>B</b> number of bits in reverse order. Example: station
<pre>
1110000000 11001110 00101110 10000110 00101110 10010110 11110110 01110110
</pre>
<br/>
On that manner the entry is a pair of a key and a string: <tt>010000110</tt> - <tt>"station"</tt>
<p/>
Both, letters and entries, with the keys, present a dictionary table used to decode the {@link Poi13 Record 13} description. It is similar to the table of the {@link PoiRec09 Record 09}.

 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class Dictionary {

	protected static LogCategory log = LogCategory.getLogger(Dictionary.class);

	/**
	 * dictionary identifier.
	 * <br/>
	 * <tt>50 4f 49 53 = "POIS"</tt>
	 */
	public static final int POIS = 0x53494f50;

	/**
	 * unknown1 length in bytes.
	 * <br/>
	 * <tt>01 00 01 00 00 00 00 00 00 00 00 00 00 00</tt>
	 */
	public static final byte[] UNKNOWN1 = {0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

	 /**
	  * unknown2.
	  * <br/>
	  * <tt>01</tt>
	  */
	public static final byte UNKNOWN2 = 1;

	 /**
	  * unknown3.
	  * <br/>
	  * <tt>01</tt>
	  */
	public static final byte UNKNOWN3 = 1;

	/**
	 * unknown5 length in bits.
	 * <br/>
	 * <table>
	 * <tr><td><tt>0000000001</tt></td><td><tt>0000000001</tt></td></tr>
	 * </table>
	 */
	public static final String UNKNOWN5 = "00000000010000000001";

	/** id length in bits. */
	private int idLen;

	/** keys description */
	byte[] keysBuff;

	/** number of keys. */
	private int keyNum;

	/** string size length in bits. */
	public int strLen;

	/** size of a char in bits. */
	private int chSize;

	/** id value of biggest single char. */
	private int ascii;

	/** entries description */
	byte[] dictBuff;

	private BinaryTree<String> tree;

	/** parse keys. */
	private SortedMap<Integer, BitContainer> readKeys(BitContainer bits) {
		TreeMap<Integer, BitContainer> keys = new TreeMap<Integer, BitContainer>();
		int len = bits.nextClearBit(0);
		BitContainer key = new BitContainer(bits.toString(len));
		//log.debug("eos: " + key.toString());
		keys.put(0, key);
		bits.delete(len + 1 + idLen);
		while ((bits.length() > 0) && (bits.nextSetBit(0) >= 0)) {
			/** prepare new key - cuts the last set bit and follow */
			key = new BitContainer(key.toString(key.lastSetBit(0)));
			/** determine appendix - all bits to the first clear bit */
			len = bits.nextClearBit(0) + 1;
			BitContainer appendix = bits.flip(len);
			bits.delete(len);
			/** appends it */
			key.append(appendix);
			/** the id */
			int id = bits.flip(idLen).toInt();
			bits.delete(idLen);
			log.trace(id + ": " + key.toString());
			keys.put(id, key);
		}
		return keys;
	}

	/** parse short (single char) entries. */
	private void chars(SortedMap<Integer, BitContainer> keys) {
		while (true) {
			int i = keys.firstKey();
			if (i < ascii) {
				BitContainer key = keys.remove(i);
				String s = "";
				if (i != 0) {
					s = Character.toString((char) i);
				}
				log.trace(key.toString() + ": " + s + " (" + Integer.toHexString(i) + ")");
				tree.put(key, s);
			} else {
				break;
			}
		}
	}

	/** parse long (string) entries. */
	private void dict(SortedMap<Integer, BitContainer> keys, BitContainer dict) {
		while (dict.length() > chSize) {
			/** strLen */
			int strLen; 
			if (chSize < this.strLen) {
				strLen = dict.flip(chSize).toInt();
			} else {
				strLen = dict.flip(chSize).toInt();
			}
			//log.trace(strLen);
			dict.delete(this.strLen);
			/** value */
			StringBuffer value = new StringBuffer(strLen);
			for (int i = 0; i < strLen; i++) {
				value.append((char) dict.flip(chSize).toInt());
				dict.delete(chSize);
			}
			int i = keys.firstKey();
			BitContainer key = keys.remove(i);
			log.trace(key.toString() + ": \"" + value.toString() + "\"");
			tree.put(key, value.toString());
		}
	}

	/** id length in bits. */
	public void setIdLen(int idLen) {
		this.idLen = idLen;
	}

	/** keys header length in bytes. */
	public byte[] setHeaderLen(int headerLen) {
		/** keys description */
		keysBuff = new byte[headerLen];
		return keysBuff;
	}

	/** keys number */
	public void setKeyNum(int keyNum) {
		this.keyNum = keyNum;
	}

	/** string size length in bits. */
	public void setStrLen(int strLen) {
		this.strLen = strLen;
	}

	/** number of bits per char */
	public void setChSize(int chSize) {
		this.chSize = chSize;
	}

	/** id value of biggest single char */
	public void setAscii(int ascii) {
		this.ascii = ascii;
	}

	/** size in bytes of entries description */
	public byte[] setDict(int dictLen) {
		dictBuff = new byte[dictLen];
		return dictBuff;
	}

	/** decode dictionary. */
	public BinaryTree<String> getTree(BinaryTree<String> tree) {
		this.tree = tree;

		/** precede */
		BitContainer bits = new BitContainer(keysBuff, true);
		SortedMap<Integer, BitContainer> keys = readKeys(bits);

		chars(keys);

		BitContainer dict = new BitContainer(dictBuff, true);

		/**
		 * 00       02       x8
		 * 00000000 01000000 0001xxxx
		 */
		if (!UNKNOWN5.equalsIgnoreCase(dict.toString(UNKNOWN5.length()))) {
			log.warn("unexpected unknown 5!");
		}
		dict.delete(UNKNOWN5.length());

		dict(keys, dict);

		//Iterator<Bit> i = tree.keySet().iterator();
		//while (i.hasNext()) {
		//	Bit key = i.next();
		//	log.trace(key.bits().toString() + " - \"" + tree.get(key) + "\"");
		//}
		return tree;
	}
}
