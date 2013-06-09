package poi.tomtom;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class PoiInputStream extends InputStream {

	/** underlying stream */
	private final InputStream in;
	/** peeked byte */
	private int peekb = -1;
	/** parents map */
	private Map<PoiContainer, Integer> parents;

	public PoiInputStream(InputStream in) {
		this.in = in;
		parents = new LinkedHashMap<PoiContainer, Integer>();
	}

	private PoiContainer getParent() {
		if (parents.size() > 0) {
			return parents.keySet().iterator().next();
		}
		return null;
	}

	private void addParent(Poi01 parent, int length) {
		parents.put(parent, length);
	}

	private void addChild(int size) {
		PoiContainer parent = getParent();
		if (parent != null) {
			int rest = parents.get(parent) - size;
			if (rest > 0) {
				parents.put(parent, rest);
			} else if (rest == 0) {
				parents.remove(parent);
			} else {
				//log.error("Unread " + rest + " bytes from " + parent);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <P extends Poi> P readPoi() throws IOException {
		byte type = peekByte();

		switch (type) {
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
			case Poi64.TYPE_64: {
				return (P) readPoi64(type);
			}
			default: {
				throw new StreamCorruptedException(
				String.format("invalid type code: %02X", type));
			}
		}
	}

	private Poi01 readPoi01(int type) throws IOException {
		Poi01 poi = new Poi01(type, getParent());
		type = readByte();
		int size = readInt();
		//poi.setLength(length);
		int longitude1 = readInt();
		poi.setLon1(longitude1);
		int latitude1 = readInt();
		poi.setLat1(latitude1);
		int longitude2 = readInt();
		poi.setLon2(longitude2);
		int latitude2 = readInt();
		poi.setLat2(latitude2);

		addParent(poi, size - poi.size());
		return poi;
	}

	private Poi02 readPoi02(int type) throws IOException {
		Poi02 poi = new Poi02(type, getParent());
		type = readByte();
		int size = readInt();
		poi.setSize(size);
		int longitude = readInt();
		poi.setLon(longitude);
		int latitude = readInt();
		poi.setLat(latitude);
		byte[] name = new byte[size - Poi02.HEADER];
		read(name, 0, name.length);
		poi.setName(name);

		addChild(size);
		return poi;
	}

	private Poi04 readPoi04(int type) throws IOException {
		Poi04 poi = new Poi04(type, getParent());
		type = readByte();
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);

		addChild(poi.size());
		return poi;
	}

	private Poi05 readPoi05(int type) throws IOException {
		Poi05 poi = new Poi05(type, getParent());
		type = readByte();
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		int name = readInt2();
		poi.setName(name);

		addChild(poi.size());
		return poi;
	}

	private Poi06 readPoi06(int type) throws IOException {
		Poi06 poi = new Poi06(type, getParent());
		type = readByte();
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		int name = readInt3();
		poi.setName(name);

		addChild(poi.size());
		return poi;
	}

	private Poi07 readPoi07(int type) throws IOException {
		Poi07 poi = new Poi07(type, getParent());
		type = readByte();
		int size = readByte();
		poi.setSize(size);
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		byte[] name = new byte[size - Poi07.HEADER];
		read(name, 0, name.length);
		poi.setName(name);

		addChild(poi.size());
		return poi;
	}

	private Poi08 readPoi08(int type) throws IOException {
		Poi08 poi = new Poi08(type, getParent());
		type = readByte();
		int size = readByte();
		poi.setSize(size);
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		byte[] name = new byte[size - Poi08.HEADER];
		read(name, 0, name.length);
		poi.setName(name);

		addChild(poi.size());
		return poi;
	}

	private Poi09 readPoi09(int type) throws IOException {
		Poi09 poi = new Poi09(type, getParent());
		type = readByte();
		int size = readByte();
		poi.setSize(size);
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		byte[] name = new byte[size - Poi09.HEADER];
		read(name, 0, name.length);
		poi.setName(name);

		addChild(poi.size());
		return poi;
	}

	private Poi10 readPoi10(int type) throws IOException {
		Poi10 poi = new Poi10(type, getParent());
		type = readByte();
		int size = readByte();
		poi.setSize(size);
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		byte[] name = new byte[size - Poi10.HEADER];
		read(name, 0, name.length);
		poi.setName(name);

		addChild(poi.size());
		return poi;
	}

	private Poi12 readPoi12(int type) throws IOException {
		Poi12 poi = new Poi12(type, getParent());
		type = readByte();
		int size = readByte();
		poi.setSize(size);
		int longitude = readInt3();
		poi.setLon(longitude);
		int latitude = readInt3();
		poi.setLat(latitude);
		byte[] name = new byte[size - Poi12.HEADER];
		read(name, 0, name.length);
		poi.setName(name);

		addChild(poi.size());
		return poi;
	}

	private Poi64 readPoi64(int type) throws IOException {
		Poi64 poi = new Poi64(type, getParent());
		type = readByte();
		int size = readInt();
		poi.setSize(size);
		byte[] unknown1 = poi.getUnknown1();
		read(unknown1, 0, Poi64.UNKNOWN1);
		//log.debug(hex(unknown1));
		int version = readInt();
		poi.setVersion(version);
		byte[] unknown2 = poi.getUnknown2();
		read(unknown2, 0, Poi64.UNKNOWN2);
		//log.debug(hex(unknown2));
		byte check = readByte();
		poi.setCheck(check);
		byte[] unknown3 = poi.getUnknown3();
		read(unknown3, 0, Poi64.UNKNOWN3);
		//log.debug(hex(unknown3));

		addChild(size);
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
