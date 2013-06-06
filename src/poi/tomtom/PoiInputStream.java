package poi.tomtom;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;

public class PoiInputStream extends InputStream {

	private final PeekInputStream pin;

	public PoiInputStream(InputStream is) {
		pin = new PeekInputStream(is);
	}

	@Override
	public int available() throws IOException {
		return pin.available();
	}

	@Override
	public int read() throws IOException {
		return pin.read();
	}

	@SuppressWarnings("unchecked")
	public <P extends Poi> P readPoi() throws IOException {
		byte type = pin.peekByte();

		switch (type) {
			case Poi01.type: {
				return (P) readPoi01();
			}
			case Poi02.type: {
				return (P) readPoi02();
			}
			case Poi64.type: {
				return (P) readPoi64();
			}
			default: {
				throw new StreamCorruptedException(
				String.format("invalid type code: %02X", type));
			}
		}
	}

	private Poi01 readPoi01() throws IOException {
		Poi01 poi = new Poi01();
		/**byte type =*/ pin.readByte();
		int length = pin.readInt();
		poi.setLength(length);
		int longitude1 = pin.readInt();
		poi.setLongitude1(longitude1);
		int latitude1 = pin.readInt();
		poi.setLatitude1(latitude1);
		int longitude2 = pin.readInt();
		poi.setLongitude2(longitude2);
		int latitude2 = pin.readInt();
		poi.setLatitude2(latitude2);
		return poi;
	}

	private Poi02 readPoi02() throws IOException {
		Poi02 poi = new Poi02();
		/**byte type =*/ pin.readByte();
		int length = pin.readInt();
		poi.setLength(length);
		int longitude = pin.readInt();
		poi.setLongitude(longitude);
		int latitude = pin.readInt();
		poi.setLatitude(latitude);
		String name = pin.readString(length - 13);
		poi.setName(name);
		return poi;
	}

	private Poi64 readPoi64() throws IOException {
		Poi64 poi = new Poi64();
		/**byte type =*/ pin.readByte();
		int length = pin.readInt();
		poi.setLength(length);
		String name = pin.readString(length - 5);
		poi.setName(name);
		String icon = pin.readString(length - 5);
		poi.setName(icon);
		return poi;
	}

	/**
	 * Input stream supporting single-byte peek operations.
	 */
	private static class PeekInputStream extends InputStream {

		/** underlying stream */
		private final InputStream in;
		/** peeked byte */
		private int peekb = -1;

		/**
		 * Creates new PeekInputStream on top of given underlying stream.
		 */
		PeekInputStream(InputStream in) {
			this.in = in;
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

		public byte readByte() throws IOException {
			int v = read();
			if (v < 0) {
				throw new EOFException();
			}
			return (byte) v;
		}

		public int readInt() throws IOException {
			byte[] buf = new byte[4];
			read(buf, 0, 4);
			int v = Bits.getInt(buf, 0);
			return v;
		}

		public String readString(int len) throws IOException {
			byte[] buf = new byte[len];
			read(buf, 0, len);
			for (int i = 0; i < len; i++) {
				if (buf[i] == 0) {
					return new String(buf, 0, i);
				}
			}
		    return new String(buf);
		}
	}
}
