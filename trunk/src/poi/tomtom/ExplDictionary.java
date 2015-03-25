package poi.tomtom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
<h3>Explicit Dictionary.</h3>

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
  <tr><td>1 byte</td><td><b>S</b>: string size length in bits.</td></tr>
  <tr><td>1 byte</td><td><b>B</b>: number of bits per char</td></tr>
  <tr><td>4 byte</td><td><b>A</b>: id value of biggest single char +1</td></tr>
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
public class ExplDictionary extends Dictionary {

	private static final int MAX_COUNT = 0x200;

	protected static LogCategory log = LogCategory.getLogger(ExplDictionary.class);

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

	/** id value of biggest single char NOTE! +1. */
	private int ascii;

	/** entries description */
	byte[] dictBuff;

	/** parse keys. */
	private SortedMap<Integer, BitContainer> readKeys(BitContainer bits) {
		TreeMap<Integer, BitContainer> keys = new TreeMap<Integer, BitContainer>();
		int len = bits.nextClearBit(0);
		BitContainer key = new BitContainer(bits.toString(len));
		//log.debug("eos: " + key.toString());
		keys.put(0, key);
		log.debug("0: " + key.toString());
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
			log.debug(id + ": " + key.toString());
			keys.put(id, key);
		}
		return keys;
	}

	/** key buffer for chars */
	private BitContainer writeKeys(SortedMap<BitContainer, Integer> keys) {
		BitContainer bits = new BitContainer(new byte[0], true);
		BitContainer last = keys.firstKey();
		bits.append(new BitContainer(last.toString()));
		bits.append(new BitContainer("0"));
		bits.append(key(idLen, 0));
		keys.remove(last);
		Iterator<BitContainer> i = keys.keySet().iterator();
		while (i.hasNext()) {
			BitContainer key = i.next();
			int id = keys.get(key);
			if (id < ascii) {
				i.remove();
			}
			int len = last.lastSetBit(0);
			if (!key.toString().substring(0, len).equals(last.toString().substring(0, len))) {
				log.error("ERROR! last key: " + last.toString().substring(0, len) + " but next key: " + key.toString().substring(0, len));
			}
			BitContainer appendix = new BitContainer(key.toString().substring(len)).flip();
			if (appendix.nextClearBit(0) != appendix.length() - 1) {
				log.error("ERROR! appendix: " + appendix.toString() + " last clear bit is not last!");
			}
			/* clear bit */
			bits.append(appendix);
			bits.append(key(idLen, id).flip());
			last = key;
		}
		return bits;
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
				put(key, s);
			} else {
				break;
			}
		}
	}

	/** parse long (string) entries. */
	private void dict(SortedMap<Integer, BitContainer> keys, BitContainer dict) {
		while (dict.length() > chSize) {
			/** strLen */
			int str; 
			if (chSize < strLen) {
				str = dict.flip(chSize).toInt();
			} else {
				str = dict.flip(strLen).toInt();
			}
			//log.trace(strLen);
			dict.delete(strLen);
			/** value */
			StringBuffer value = new StringBuffer(str);
			for (int i = 0; i < str; i++) {
				value.append((char) dict.flip(chSize).toInt());
				dict.delete(chSize);
			}
			int i = keys.firstKey();
			BitContainer key = keys.remove(i);
			log.debug(key.toString() + ": \"" + value.toString() + "\"");
			put(key, value.toString());
		}
	}

	private BitContainer writeDict(SortedMap<BitContainer, Integer> keys) {
		BitContainer bits = new BitContainer(new byte[0], true);
		bits.append(new BitContainer(UNKNOWN5));
		for (BitContainer key: keys.keySet()) {
			BinaryTree<String> node = binTree.find(key);
			String string = node.getValue();
			/** strLen */
			int str = string.length(); 
			BitContainer len = key(strLen, str);
			bits.append(len.flip());
			for (int i = 0; i < string.length(); i++) {
				int character = string.charAt(i);
				BitContainer ch = key(chSize, character);
				bits.append(ch.flip());
			}
		}
		return bits;
	}

	/** @return id length in bits. */
	public int getIdLen() {
		return idLen;
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

	/** @return keys header length in bytes. */
	public int getHeaderLen() {
		return keysBuff.length;
	}

	/** @return keys description buffer. */
	public byte[] getKeysBuff() {
		return keysBuff;
	}

	/** @return keys number */
	public int getKeyNum() {
		return keyNum;
	}

	/** keys number */
	public void setKeyNum(int keyNum) {
		this.keyNum = keyNum;
	}

	/** @return string size length in bits. */
	public int getStrLen() {
		return strLen;
	}

	/** string size length in bits. */
	public void setStrLen(int strLen) {
		this.strLen = strLen;
	}

	/** @return number of bits per char */
	public int getChSize() {
		return chSize;
	}

	/** number of bits per char */
	public void setChSize(int chSize) {
		this.chSize = chSize;
	}

	/** @return id value of biggest single char */
	public int getAscii() {
		return ascii;
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

	/** @return size in bytes of entries description */
	public int getDictLen() {
		return dictBuff.length;
	}

	/** @return entries description */
	public byte[] getDictBuff() {
		return dictBuff;
	}

	/** create dictionary. */
	public void create() {
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
	}
	
	/* ------------------------- generate ----------------------------- */
	
	/** string container */
	private class Element implements Comparable<Element> {
		private int repeat;
		private String string;
		private TreeSet<Element> affected;
		private TreeSet<Element> affects;
		
		public Element(int repeat, String string, Collection<Element> elements) {
			this.repeat = repeat;
			this.string = string;
			affected = new TreeSet<Element>();
			affects = new TreeSet<Element>();
			if (elements != null) {
				for (Element element: elements) {
					if (string.indexOf(element.string) >= 0) {
						element.affects.add(this);
						affected.add(element);
					} else if (element.string.indexOf(string) >= 0) {
						element.affected.add(this);
						affects.add(element);
					}
				}
			}
		}

		/** how many efficient is */
		public int efficiency() {
			return repeat * string.length();
		}

		public void remove() {
			for (Element affected: affects) {
				affected.affected.remove(this);
			}
		}

		@Override
		public int compareTo(Element element) {
			if (string.equals(EOS) || element.string.equals(EOS)) {
				/* always first */
				if (string.equals(EOS) && !element.string.equals(EOS)) {
					return -1;
				} else if (!string.equals(EOS) && element.string.equals(EOS)) {
					return 1;
				} else {
					return 0;
				}
			}
			int result = element.efficiency() - efficiency(); /* reverse */
			if (result != 0) {
				return result;
			}
			return string.compareTo(element.string);
		}
		
		@Override
		public String toString() {
			return repeat + "("+efficiency()+") ->" + string + "<-";
		}
	}

	/** find max length of two words. */
	private int maxLen(Collection<String> texts) {
		int maxLen = 20;
		for (String text: texts) {
			String[] words = text.split(" ");
			for (int i = 1; i < words.length; i++) {
				if (maxLen < words[i - 1].length() + words[i].length()) {
					maxLen = words[i - 1].length() + words[i].length();
				}
			}
		}
		maxLen += 3; /*spaces before, after and in the middle */
		return maxLen;
	}

	/** finds any string. */
	private Map<String, Collection<String>> strings(Collection<String> texts) {
		/* find max length of two words */
		int maxLen = maxLen(texts);
		
		Map<String, Collection<String>> strings = new HashMap<String, Collection<String>>();
		for (String text: texts) {
			for (int i = 0; i < text.length(); i++) {
				for (int j = i + 1; j < text.length() && j < i + maxLen; j++) {
					String string = text.substring(i, j + 1);
					Collection<String> usage = strings.get(string);
					if (usage == null) {
						usage = new ArrayList<String>();
						strings.put(string, usage);
					}
					usage.add(text);
				}
			}
		}
		return strings;
	}

	/** order by repetition. */
	private Map<Integer, Collection<String>> repeats(Collection<String> texts) {
		/* finds any string */
		Map<String, Collection<String>> strings = strings(texts);
		
		Map<Integer, Collection<String>> repeats = new TreeMap<Integer, Collection<String>>(new Comparator<Integer>(){
			@Override
			public int compare(Integer i1, Integer i2) {
				/* reverse order */
				return i2.compareTo(i1);
			}});
//		int count = 0;
		for (Entry<String, Collection<String>> entry: strings.entrySet()) {
			String string = entry.getKey();
			int n = entry.getValue().size();
			if (n==1) {
				/* don't care for texts and unique parts */
				continue;
			}
			Collection<String> set = repeats.get(n);
			if (set == null) {
				set = new TreeSet<String>();
				repeats.put(n, set);
			}
			/* remove partitional repeats. need no 'map' and 'ma' - the first one covers the second one */
			boolean add = true;
			Iterator<String> j = set.iterator();
			while (j.hasNext()) {
				String s = j.next();
				if (s.indexOf(string) > -1) {
					//log.debug(" ->"+string+"<- ->"+text+"<-");
					add = false;
					break;
				} else if (string.indexOf(s) > -1) {
					j.remove();
//					count--;
				}
			}
			if (add) {
				set.add(string);
//				count++;
			}
		}
//		log.debug("count: " + count);
		return repeats;
	}

	/** order by efficiency. */
	private Map<Integer, Collection<String>> efficiency(Collection<String> texts) {
		/* order by repetition */
		Map<Integer, Collection<String>> repeats = repeats(texts);
		
		Map<Integer, Collection<String>> efficiency = new TreeMap<Integer, Collection<String>>(new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				/* reverse order */
				return i2.compareTo(i1);
			}});
		for (Entry<Integer, Collection<String>> entry: repeats.entrySet()) {
			int n = entry.getKey();
			Collection<String> strings = entry.getValue();
			for (String string: strings) {
				Collection<String> set = efficiency.get(n * string.length());
				if (set == null) {
					set = new TreeSet<String>(new Comparator<String>() {
						@Override
						public int compare(String s1, String s2) {
							int result = s2.length() - s1.length();
							if (result != 0) {
								return result;
							}
							return s1.compareTo(s2);
						}});
					efficiency.put(n * string.length(), set);
				}
				set.add(string);
			}
		}
		return efficiency;
	}

	/** breaks all messages to parts and how many time they appear. */
	private TreeSet<Element> stat(Collection<String> texts) {
		/* order by efficiency */
		Map<Integer, Collection<String>> efficiency = efficiency(texts);
		
		/* NOTE! for a word 'map' there may be two parts 'ma' and 'ap' with different appearing numbers. */
		Map<String, Integer> repeats = new HashMap<String, Integer>();
		for (String poi: texts) {
			List<String> text = new ArrayList<String>();
			text.add(poi);
			for (Entry<Integer, Collection<String>> entry: efficiency.entrySet()) {
				Collection<String> strings = entry.getValue();
				for (String string: strings) {
					for (int i = 0; i < text.size();) {
						String part = text.get(i);
						int index = part.indexOf(string);
						if (index >= 0) {
							text.remove(i);
							if (index + string.length() < part.length()) {
								text.add(i, part.substring(index + string.length()));
							}
							if (index > 0) {
								text.add(i, part.substring(0, index));
							}
							Integer count = repeats.get(string);
							if (count == null) {
								repeats.put(string, 1);
							} else {
								count++;
								repeats.put(string, count);
							}
						} else {
							i++;
						}
					}
				}
			}
		}
		
		TreeSet<Element> elements = new TreeSet<Element>();
		for (Entry<String, Integer> entry: repeats.entrySet()) {
			int repeat = entry.getValue();
			if (repeat > 1) {
				String string = entry.getKey();
				Element element = new Element(repeat, string, elements);
				elements.add(element);
			}
		}
		return elements;
	}

	/** check selected efficiency. remove composite elements. returns last one. */
	private Element selected(Collection<Element> elements) {
		int efficiency = 0;
		int n = 0;
		Element last = null;
		Iterator<Element> i = elements.iterator();
		while (i.hasNext()) {
			Element element = i.next();
			if (element.affected.isEmpty()) {
				n++;
				last = element;
				efficiency += last.efficiency();
				if (n > MAX_COUNT) {
					break;
				}
			} else {
				i.remove(); /* don't care for those what affects other element */
			}
		}
		/**/log.trace("efficiency: "+efficiency);
		return last;
	}
	
	/** sort by efficiency. select only elements what are not parts of other elements unless its more efficient opposite */
	private TreeSet<Element> sort(Collection<String> texts) {
		/** breaks all messages to parts and how many time they appear. */
		TreeSet<Element> elements = stat(texts);
		
		Element last = selected(elements);
		Iterator<Element> i = elements.iterator();
		while (i.hasNext()) {
			Element element = i.next();
			if (element.affected.isEmpty()) {
				int efficiency = (elements.higher(last)==null?0:elements.higher(last).efficiency()); /* remove will change the last */
				Element tempLast = last;
				for (Element affected = (element.affects.isEmpty()?null:element.affects.last()); affected != null; affected = element.affects.lower(affected)) { /* backward */
					if (affected.affected.size() == 1) { 
						if (affected.compareTo(tempLast) < 0) {
							//log.debug(element + " : " + affected);
							efficiency += affected.efficiency() - tempLast.efficiency(); /* cause it will remove the last */
							tempLast = elements.lower(tempLast); /* it will be new last if */
						}
					}
				}
				if (efficiency > element.efficiency()) {
					//log.debug(element.efficiency() + " : " + efficiency);
					element.remove();
					i.remove();
					for (Element affected: element.affects) {
						elements.add(affected);
					}
					last = selected(elements);
					i = elements.iterator();
				}
			}
			if (element.compareTo(last) > 0) {
				break;
			}
		}
		if (elements.higher(last) != null) {
			return new TreeSet<Element>(elements.headSet(elements.higher(last)));
		}
		return elements;
	}

	private int searchWord(String text, SortedSet<Element> words) {
		for (Element word: words) {
			if (text.startsWith(word.string)) {
				return word.string.length();
			}
		}
		return 0;
	}

	private TreeSet<Element> chars(Collection<String> texts, TreeSet<Element> words) {
		Map<String, Integer> chars = new HashMap<String, Integer>();
		for (String text: texts) {
			for (int i = 0; i < text.length(); i++) {
				int delta;
				do {
					delta = 0;
					delta = searchWord(text.substring(i), words);
					i += delta;
					if (i >= text.length()) {
						 break;
					}
				} while (delta != 0);
				if (i >= text.length()) {
					 continue;
				}
				String character = Character.toString(text.charAt(i));
				if (chars.get(character) == null) {
					chars.put(character, 1);
				} else {
					chars.put(character, chars.get(character) + 1);
				}
			}
		}
		/* order by repeats */
		Map<Integer, Collection<String>> repeats = new TreeMap<Integer, Collection<String>>(new Comparator<Integer>(){
			@Override
			public int compare(Integer i1, Integer i2) {
				/* reverse order */
				return i2.compareTo(i1);
			}});
		for (Entry<String, Integer> entry: chars.entrySet()) {
			String character = entry.getKey();
			int repetition = entry.getValue();
			Collection<String> set = repeats.get(repetition);
			if (set == null) {
				set = new ArrayList<String>();
				repeats.put(repetition, set);
			}
			set.add(character);
		}
		TreeSet<Element> elements = new TreeSet<Element>();
		for (Entry<Integer, Collection<String>> entry: repeats.entrySet()) {
			int repeat = entry.getKey();
			Collection<String> set = entry.getValue();
			for (String string: set) {
				Element element = new Element(repeat, string, elements);
				elements.add(element);
			}
		}
		/* eol */
		Element element = new Element(texts.size(), EOS, elements);
		elements.add(element);
		return elements;
	}

	private TreeSet<Element>[] copy(TreeSet<Element>[] levels) {
		@SuppressWarnings("unchecked")
		TreeSet<Element>[] copy = new TreeSet[levels.length];
		for (int depth = 0; depth < copy.length; depth++) {
			copy[depth] = new TreeSet<Element>(levels[depth]);
		}
		return copy;
	}

	/** move two on prev */
	private void move(TreeSet<Element>[] levels, int depth) {
		if (depth < levels.length - 1) {
			for (int i = 0; i < 2; i++) {
				if (!levels[depth].isEmpty()) {
					Element element = levels[depth].last();
					levels[depth].remove(element);
					levels[depth + 1].add(element);
				} else {
					move(levels, depth + 1);
				}
			}
		}
	}

	/** is there is room on next level for shift */
	private boolean size(TreeSet<Element>[] levels, int current) {
		int size = 0;
		for (int depth = 0; depth < levels.length; depth++) {
			size <<= 1;
			size  += levels[depth].size();
		}
		return size < (1 << levels.length);
	}

	private int size(TreeSet<Element>[] levels) {
		int size = 0;
		for (int depth = 0; depth < levels.length; depth++) {
			TreeSet<Element> level = levels[depth];
			for (Element element: level) {
				size += (element.string.equals(EOS)?1:element.string.length()) * element.repeat * depth;
			}
		}
		return size;
	}

	private TreeSet<Element>[] opt(TreeSet<Element>[] levels, int current) {
		if (current > 0) {
			int size = size(levels);
			/* is there is room on next level for shift */
			if (size(levels, current)) {
				/* shift one element */
				if (!levels[current].isEmpty()) {
					TreeSet<Element>[] copy = copy(levels);
					Element element = copy[current].first();
					copy[current].remove(element);
					copy[current - 1].add(element);
					if (size(copy) < size) {
						levels = copy;
						size = size(levels);
						levels = opt(levels, current);
					}
				} else {
					/* check next level */
					levels = opt(levels, current - 1);
				}
			} else {
				if (current < levels.length - 1) {
					if (!levels[current].isEmpty()) {
						TreeSet<Element>[] copy = copy(levels);
						/* one on next */
						Element element = copy[current].first();
						copy[current].remove(element);
						copy[current - 1].add(element);
						/* two on prev */
						move(copy, current);
						if (size(copy) < size) {
							levels = copy;
							size = size(levels);
							levels = opt(levels, current);
						} else {
							levels = opt(levels, current - 1);
						}
					}
				} 
			}
		}
		return levels;
	}

	private TreeSet<Element>[] levels(TreeSet<Element> words) {
		/* init levels */
		@SuppressWarnings("unchecked")
		TreeSet<Element>[] levels = new TreeSet[20];
		for (int depth = 0; depth < levels.length; depth++) {
			levels[depth] = new TreeSet<Element>();
		}
		levels[19].addAll(words);
		
		levels = opt(levels, levels.length - 1);
		return levels;
	}

	/** bits with length for i. */
	public BitContainer key(int len, int i) {
		return new BitContainer("00000000000000000000".substring(20 - len + Integer.toBinaryString(i).length()) + Integer.toBinaryString(i));
	}

	/** create dictionary by pois. */
	public void init() {
		ascii = 0;
		idLen = 0;
		chSize = 0;
		strLen = 0;
		for (BitContainer key: binTree.keySet()) {
			String string = binTree.find(key).getValue();
			if (string.length() <= 1) {
				if (!string.equals(EOS)) {
					int character = string.charAt(0);
					if (ascii < character) {
						ascii = character;
						idLen = Integer.toBinaryString(ascii).length();
					}
				}
			} else {
				for (int i = 0; i < string.length(); i++) {
					int character = string.charAt(i);
					if (chSize < Integer.toBinaryString(character).length()) {
						chSize = Integer.toBinaryString(character).length();
					}
				}
				if (strLen < Integer.toBinaryString(string.length()).length()) {
					strLen = Integer.toBinaryString(string.length()).length();
				}
			}
		}
		keyNum = ascii;
		ascii++; /* NOTE! +1 */
		SortedMap<BitContainer, Integer> keys = new TreeMap<BitContainer, Integer>(new Comparator<BitContainer>() {
			@Override
			public int compare(BitContainer key1, BitContainer key2) {
				/* reverse order */
				return key2.toString().compareTo(key1.toString());
			}});
		for (BitContainer key: binTree.keySet()) {
			String string = binTree.find(key).getValue();
			if (string.equals(EOS)) {
				keys.put(key, 0);
			} else if (string.length() == 1) {
				int character = string.charAt(0);
				keys.put(key, character);
			} else {
				keyNum++;
				keys.put(key, keyNum);
			}
		}
		if (idLen < Integer.toBinaryString(keyNum).length()) {
			idLen = Integer.toBinaryString(keyNum).length();
		}
		keysBuff = writeKeys(keys).buff();
		dictBuff = writeDict(keys).buff();
	}

	/** create dictionary by pois. */
	public void create(Collection<String> texts) {
		TreeSet<Element> words = sort(texts);
		TreeSet<Element> chars = chars(texts, words);
		words.addAll(chars);
		TreeSet<Element>[] levels = levels(words);

		BitContainer xor = null;
		BitContainer key = new BitContainer(""); 
		for (int depth = 0; depth < levels.length; depth++) {
			key = new BitContainer(key.toString() + "0");
			TreeSet<Element> level = levels[depth];
			if (!level.isEmpty()) {
				for (Element element: level) {
					int i = key.toInt();
					/* in order EOL with set bits only */
					if (xor == null) {
						xor = new BitContainer("1111111111111111111".substring(0, depth + 1));
					}
					key.xor(xor);
					log.debug(key + " ->" + element.string + "<-");
					put(key, element.string);
					i++;
					if (key.length() - Integer.toBinaryString(i).length() >= 0) {
						key = key(key.length(), i);
					} /* else key generating error */
				}
			}
		}
		init();
	}
}
