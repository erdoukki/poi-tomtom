package poi.tomtom;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
<h3>Dictionary.</h3>

This is a structure of dictionary used to decode {@link PoiRec29 Record 29}.
<p>
<table border="1">
  <tr><th width=110>bytes</th><th>description</th></tr>
  <tr><td>4 bytes</td><td><tt><b>50 4f 49 53 = "POIS"</b></tt></td></tr>
  <tr><td>14 bytes</td><td><tt><b>01 00 01 00 00 00 00 00 00 00 00 00 00 00</b></tt></td></tr>
  <tr><td>1 byte</td><td><b>I</b>: size in bits of id</td></tr>
  <tr><td>2 bytes</td><td><b>K</b>: size in bytes of keys description</td></tr>
  <tr><td>2 bytes</td><td><b>N</b>: keys number. <b>X</b> letters + <tt><b>0x200</b></tt> entries</td></tr>
  <tr><td>K bytes</td><td>keys description</td></tr>
  <tr><td>3 bytes</td><td><tt><b>01 01 0a (last byte may be {@link #STR_LEN STR_LEN})</b></tt></td></tr>
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
The <b>id</b> of first key is <tt><b>0000000000</b></tt>. The first key is <b>&lt;end-of-string&gt;</b> for the {@link PoiRec29 Record 29} description.
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
Both, letters and entries, with the keys, present a dictionary table used to decode the {@link PoiRec29 Record 29} description. It is similar to the table of the {@link PoiRec09 Record 09}.

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
	  * unknown2 length in bytes.
	  * <br/>
	  * <tt>01 01 0a</tt>
	  */
	public static final byte[] UNKNOWN2 = {0x01, 0x01, 0x0a};

	/** unknown3 length in bytes */
	public static final int UNKNOWN3 = 4;

	/**
	 * unknown4 length in bits.
	 * <br/>
	 * <table>
	 * <tr><th><tt>00      </tt></th><th><tt>02      </tt></th><th><tt>x8</tt></th></tr>
	 * <tr><td><tt>00000000</tt></td><td><tt>01000000</tt></td><td><tt>0001xxxx</tt></td></tr>
	 * </table>
	 */
	public static final String UNKNOWN4 = "00000000010000000001";

	/** string size length in bits. */
	public static final int STR_LEN = 10;

	/** id length in bits. */
	private int idLen;

	/** keys header length in bytes. */
	private int headerLen;

	/** number of keys. */
	private int keyNum;

	/** size of a char in bits. */
	private int chSize;

	/** id value of biggest single char. */
	private int ascii;

	/** entries header length in bytes. */
	private int dictLen;

	/** parse keys. */
	private SortedMap<Integer, BitContainer> readKeys(BitContainer bits) {
		TreeMap<Integer, BitContainer> keys = new TreeMap<Integer, BitContainer>();
		int len = bits.nextClearBit(0);
		BitContainer key = bits.get(0, len);
		//log.debug("eos: " + key.toString());
		keys.put(0, key);
		bits.delete(len + 1 + idLen);
		while ((bits.length() > 0) && (bits.nextSetBit(0) >= 0)) {
			/** prepare new key - cuts the last set bit and follow */
			key = key.get(0, key.lastSetBit(0));
			/** determine appendix - all bits to the first clear bit */
			len = bits.nextClearBit(0) + 1;
			BitContainer appendix = bits.get(0, len);
			bits.delete(len);
			appendix.flip();
			/** appends it */
			key.append(appendix);
			/** the id */
			int id = bits.toInt(idLen, true);
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
				log.debug(key.toString() + ": " + s + " (" + Integer.toHexString(i) + ")");
				Poi13.put(key, s);
			} else {
				break;
			}
		}
	}

	/** parse long (string) entries. */
	private void dict(SortedMap<Integer, BitContainer> keys, BitContainer dict) {
		while (dict.length() > chSize) {
			/** strLen */
			int strLen = dict.toInt(chSize, true);
			//log.trace(strLen);
			dict.delete(STR_LEN);
			/** value */
			StringBuffer value = new StringBuffer(strLen);
			for (int i = 0; i < strLen; i++) {
				value.append((char) dict.toInt(chSize, true));
				dict.delete(chSize);
			}
			int i = keys.firstKey();
			BitContainer key = keys.remove(i);
			log.trace(key.toString() + ": \"" + value.toString() + "\"");
			Poi13.put(key, value.toString());
		}
	}

	/** reads dictionary. */
	public void read(PoiInputStream is) throws IOException {
		/** 01 00 01 00 00 00 00 00 00 00 00 00 00 00 */
		byte[] unknown1 = new byte[UNKNOWN1.length];
		is.read(unknown1);
		if (!Arrays.equals(unknown1, UNKNOWN1)) {
			log.warn("unexpected unknown 1!");
		}

		/** size in bits of id */
		idLen = is.readByte();

		/** size in bytes of keys description */
		headerLen = is.readInt2();
		log.trace("header len: 0x" + Integer.toHexString(headerLen));

		/** keys number */
		keyNum = is.readInt2();
		log.trace("keys number: " + keyNum / 2); // -1

		/** keys description */
		byte[] keysBuff = new byte[headerLen];
		is.read(keysBuff);

		/** 01 01 0a */
		byte[] unknown2 = new byte[UNKNOWN2.length];
		is.read(unknown2);
		if (!Arrays.equals(unknown2, UNKNOWN2)) {
			log.warn("unexpected unknown 2!");
		}

		/** number of bits per char */
		chSize = is.readByte();
		log.trace("char size: " + chSize);

		/** id value of biggest single char */
		ascii = is.readInt();
		log.trace("ascii: 0x" + Integer.toHexString(ascii));

		/** */
		byte[] unknown3 = new byte[UNKNOWN3];
		is.read(unknown3);
		log.debug("unknown: 0x" + Integer.toHexString(unknown3[0] & 0xFF) + " 0x" + Integer.toHexString(unknown3[1] & 0xFF) + " 0x" + Integer.toHexString(unknown3[2] & 0xFF) + " 0x" + Integer.toHexString(unknown3[3] & 0xFF));

		/** size in bytes of entries description */
		dictLen = is.readInt();
		log.trace("dict len: 0x" + Integer.toHexString(dictLen));

		/** entries description */
		byte[] dictBuff = new byte[dictLen];
		is.read(dictBuff);

		/** precede */
		BitContainer bits = new BitContainer(keysBuff, true);
		SortedMap<Integer, BitContainer> keys = readKeys(bits);

		chars(keys);

		BitContainer dict = new BitContainer(dictBuff, true);

		/**
		 * 00       02       x8
		 * 00000000 01000000 0001xxxx
		 */
		if (!UNKNOWN4.equalsIgnoreCase(dict.toString(UNKNOWN4.length()))) {
			log.warn("unexpected unknown 4!");
		}
		dict.delete(UNKNOWN4.length());

		dict(keys, dict);

		BinaryTree<String> tree = Poi13.tree;
		Iterator<Bit> i = tree.keySet().iterator();
		while (i.hasNext()) {
			log.debug(i.next().bits().toString());
		}
	}
}
