package poi.tomtom;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class PoiInputStream extends InputStream {

	public enum Mode {
		OV2, DAT
	}

	private final Mode mode;
	/** underlying stream */
	private final InputStream in;
	/** peeked byte */
	private int peekb = -1;
	/** parents map */
	private Map<PoiContainer, Integer> parents;

	private Dictionary dict;

	public PoiInputStream(InputStream in) {
		this(in, Mode.OV2);
	}

	public PoiInputStream(InputStream in, Mode mode) {
		this.in = in;
		this.mode = mode;
		parents = new LinkedHashMap<PoiContainer, Integer>();
	}

	private PoiContainer getParent() {
		Iterator<PoiContainer> i = parents.keySet().iterator();
		PoiContainer result = null;
		while (i.hasNext()) {
			result  = i.next();
		}
		return result;
	}

	private void addParent(PoiContainer parent, int length) {
		parents.put(parent, length);
	}

	private void addChild(PoiContainer parent, int size) {
		if ((parent != null) && (parents.get(parent) != null)) {
			int rest = parents.get(parent) - size;
			if (rest > 0) {
				parents.put(parent, rest);
			} else if (rest == 0) {
				parents.remove(parent);
			} else {
				parents.remove(parent);
//				log.error("Unread " + rest + " bytes from " + parent);
			}
			if (parent instanceof PoiCommon) {
				addChild(((PoiCommon)parent).getParent(), size);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <P extends Poi> P readPoi() throws IOException {
		byte type;
		if ((mode == Mode.DAT) && (getParent() == null)) {
			type = Category.TYPE_CATEGORIES;
		} else {
			type = peekByte();
		}

		switch (type) {
			case Category.TYPE_CATEGORIES: {
				return (P) readCategory(type);
			}
			case Poi01.TYPE_01: {
				return (P) readPoi01(type);
			}
			case Poi02.TYPE_02: 
			case Poi02.TYPE_03: 
			case Poi02.TYPE_0F: {
				return (P) readPoi02(type);
			}
			case Poi04.TYPE_04: 
			case Poi04.TYPE_14: {
				return (P) readPoi04(type);
			}
			case Poi05.TYPE_05: 
			case Poi05.TYPE_15: {
				return (P) readPoi05(type);
			}
			case Poi06.TYPE_06: 
			case Poi06.TYPE_16: {
				return (P) readPoi06(type);
			}
			case Poi07.TYPE_07: 
			case Poi07.TYPE_17: {
				return (P) readPoi07(type);
			}
			case Poi08.TYPE_08: 
			case Poi08.TYPE_18: {
				return (P) readPoi08(type);
			}
			case Poi09.TYPE_09: 
			case Poi09.TYPE_19: {
				return (P) readPoi09(type);
			}
			case Poi10.TYPE_0A: 
			case Poi10.TYPE_1A: {
				return (P) readPoi10(type);
			}
			case Poi12.TYPE_0C: 
			case Poi12.TYPE_1C: {
				return (P) readPoi12(type);
			}
			case Poi13.TYPE_1D: {
				return (P) readPoi13(type);
			}
			case Poi64.TYPE_64: {
				return (P) readPoi64(type);
			}
			default: {
				throw new StreamCorruptedException(
				String.format("invalid type code: %02X", type));
			}
		}
	}

	private int readPOIS() throws IOException {
		int count = readInt();
		if (count == Dictionary.POIS) {
			dict = new Dictionary();
			dict.read(this);
			count = readInt();
		} else {
			Poi13.tree.loadFromXml(System.getProperty("user.dir") + File.separator + "etc" + File.separator + Poi09.DEFAULT_XML);
		}
		return count;
	}

	private Pois readCategory(byte type) throws IOException {
		Pois pois = new Category(type, getParent());
		int count = readPOIS();
		for (int i = 0; i < count; i++) {
			Category category = new Category(Category.TYPE_CATEGORY, pois);
			int id = readInt();
			category.setCategoryId(id);
			pois.add(category);
		}
		Map<PoiContainer, Integer> stack = new LinkedHashMap<PoiContainer, Integer>();
		if (dict == null) {
			int offset = readInt(); 
			for (Poi category: pois) {
				int catOffset = readInt();
				int size = catOffset - offset;
				offset = catOffset;
				stack.put((PoiContainer) category, size);
				/**/System.out.println(size + " - " + category);
			}
		} else {
			for (Poi category: pois) {
				int size = readInt();
				stack.put((PoiContainer) category, size);
				//log.debug(size + " - " + category);
			}
		}
		/** reverse categories to parents */
		while (stack.size() > 0) {
			Iterator<PoiContainer> i = stack.keySet().iterator();
			PoiContainer category = null;
			while (i.hasNext()) {
				category = i.next();
			}
			addParent(category, stack.get(category));
			stack.remove(category);
		}
		return pois;
	}

	private Poi01 readPoi01(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi01 poi = new Poi01(type, parent);
		type = readByte();
		int size = readInt();
		poi.setSize(size);
		int longitude1 = readInt();
		poi.setLon1(longitude1);
		int latitude1 = readInt();
		poi.setLat1(latitude1);
		int longitude2 = readInt();
		poi.setLon2(longitude2);
		int latitude2 = readInt();
		poi.setLat2(latitude2);

		addParent(poi, size - Poi01.SIZE);
		addChild(parent, Poi01.SIZE);
		return poi;
	}

	private Poi02 readPoi02(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi02 poi = new Poi02(type, parent);
		type = readByte();
		int size = readInt();
		poi.setSize(size);
		int longitude = readInt();
		poi.setLon(longitude);
		int latitude = readInt();
		poi.setLat(latitude);
		byte[] name = poi.name();
		read(name);
		poi.setName(name);

		addChild(parent, size);
		return poi;
	}

	private Poi04 readPoi04(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi04 poi = new Poi04(type, parent);
		type = readByte();
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);

		addChild(parent, poi.size());
		return poi;
	}

	private Poi05 readPoi05(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi05 poi = new Poi05(type, parent);
		type = readByte();
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		int name = readInt2();
		poi.setName(name);

		addChild(parent, poi.size());
		return poi;
	}

	private Poi06 readPoi06(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi06 poi = new Poi06(type, parent);
		type = readByte();
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		int name = readInt3();
		poi.setName(name);

		addChild(parent, poi.size());
		return poi;
	}

	private Poi07 readPoi07(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi07 poi = new Poi07(type, parent);
		type = readByte();
		int size = readByte();
		poi.setSize(size);
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		byte[] name = poi.name();
		read(name);
		poi.setName(name);

		addChild(parent, poi.size() + Poi07.HEADER);
		return poi;
	}

	private Poi08 readPoi08(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi08 poi = new Poi08(type, parent);
		type = readByte();
		int size = readByte();
		poi.setSize(size);
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		byte[] name = poi.name();
		read(name);
		poi.setName(name);

		addChild(parent, poi.size() + Poi07.HEADER);
		return poi;
	}

	private Poi09 readPoi09(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi09 poi = new Poi09(type, parent);
		type = readByte();
		int size = readByte();
		poi.setSize(size);
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		byte[] name = poi.name();
		read(name);
		poi.setName(name);

		addChild(parent, poi.size() + Poi07.HEADER);
		return poi;
	}

	private Poi10 readPoi10(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi10 poi = new Poi10(type, parent);
		type = readByte();
		int size = readByte();
		poi.setSize(size);
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		byte[] name = poi.name();
		read(name);
		poi.setName(name);

		addChild(parent, poi.size() + Poi07.HEADER);
		return poi;
	}

	private Poi12 readPoi12(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi12 poi = new Poi12(type, parent);
		type = readByte();
		int size = readByte();
		poi.setSize(size);
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		byte[] name = poi.name();
		read(name);
		poi.setName(name);

		addChild(parent, poi.size() + Poi07.HEADER);
		return poi;
	}

	private Poi13 readPoi13(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi13 poi = new Poi13(type, parent);
		type = readByte();
		int size = readByte();
		poi.setSize(size);
		int longitude = readInt();
		poi.setLon(longitude);
		int latitude = readInt();
		poi.setLat(latitude);
		byte code = readByte();
		poi.setCode(code);
		byte[] unknown = poi.getUnknown();
		if (unknown != null) {
			read(unknown);
			poi.setUnknown(unknown);
		}
		byte[] name = poi.name();
		read(name);
		poi.setName(name);

		addChild(parent, poi.size() + Poi13.HEADER);
		return poi;
	}

	private Poi64 readPoi64(int type) throws IOException {
		PoiContainer parent = getParent();
		Poi64 poi = new Poi64(type, parent);
		type = readByte();
		int size = readInt();
		poi.setSize(size);
		byte[] unknown1 = poi.getUnknown1();
		read(unknown1);
		//log.debug(hex(unknown1));
		int version = readInt();
		poi.setVersion(version);
		byte[] unknown2 = poi.getUnknown2();
		read(unknown2);
		//log.debug(hex(unknown2));
		byte check = readByte();
		poi.setCheck(check);
		byte[] unknown3 = poi.getUnknown3();
		read(unknown3);
		//log.debug(hex(unknown3));

		addChild(parent, size);
		return poi;
	}

	/**
	 * Peeks at next byte value in stream. Similar to read(), except that it
	 * does not consume the read value.
	 */
	int peek() throws IOException {
		return (peekb >= 0) ? peekb : (peekb = in.read());
	}

	/**
	 * Peeks at (but does not consume) and returns the next byte value in
	 * the stream, or throws EOFException if end of stream/block data has
	 * been reached.
	 */
	byte peekByte() throws IOException {
		int val = peek();
		if (val < 0) {
			throw new EOFException();
		}
		return (byte) val;
	}

	@Override
	public int read() throws IOException {
		if (peekb >= 0) {
			int v = peekb;
			peekb = -1;
			return v;
		} else {
			return in.read();
		}
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (len == 0) {
			return 0;
		} else if (peekb < 0) {
			return in.read(b, off, len);
		} else {
			b[off++] = (byte) peekb;
			len--;
			peekb = -1;
			int n = in.read(b, off, len);
			return (n >= 0) ? (n + 1) : 1;
		}
	}

	@Override
	public long skip(long n) throws IOException {
		if (n <= 0) {
			return 0;
		}
		int skipped = 0;
		if (peekb >= 0) {
			peekb = -1;
			skipped++;
			n--;
		}
		return skipped + skip(n);
	}

	@Override
	public int available() throws IOException {
		return in.available() + ((peekb >= 0) ? 1 : 0);
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	@Override
	public void mark(int mark) {
		//log.error("skip is not implemeted!");
	}

	@Override
	public void reset() {
		//log.error("skip is not implemeted!");
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	public byte readByte() throws IOException {
		int v = read();
		if (v < 0) {
			throw new EOFException();
		}
		return (byte) v;
	}

	public int readInt() throws IOException {
		int v = 0;
		for (int i = 0; i < 4; i++) {
			int b = read();
			//log.debug(": 0x" + Integer.toHexString(b));
			b <<= 8 * i;
			v += b;
		}
		return v;
	}

	public int readInt2() throws IOException {
		int v = 0;
		for (int i = 0; i < 2; i++) {
			int b = read();
			//log.debug(": 0x" + Integer.toHexString(b));
			b <<= 8 * i;
			v += b;
		}
		return v;
	}

	public int readInt3() throws IOException {
		int v = 0;
		for (int i = 0; i < 3; i++) {
			int b = read();
			//log.debug(": 0x" + Integer.toHexString(b));
			b <<= 8 * i;
			v += b;
		}
		return v;
	}
}
