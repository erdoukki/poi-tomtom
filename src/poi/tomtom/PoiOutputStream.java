package poi.tomtom;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class PoiOutputStream extends OutputStream {

	private OutputStream os;

	public PoiOutputStream(OutputStream os) {
		this.os = os;
	}

	/**
	 * Closes this output stream and releases any system resources associated with this stream.
	 */
	@Override
	public void close() throws IOException {
		os.close();
	}

	/**
	 * Flushes this output stream and forces any buffered output bytes to be written out.
	 */
	@Override
	public void flush() throws IOException {
		os.flush();
	}

	/**
	 * Writes b.length bytes from the specified byte array to this output stream.
	 */
	@Override
	public void write(byte[] b) throws IOException {
		os.write(b);
	}

	/**
	 * Writes len bytes from the specified byte array starting at offset off to this output stream.
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		os.write(b, off, len);
	}

	/**
	 * Writes the specified byte to this output stream.
	 */
	@Override
	public void write(int v) throws IOException {
		os.write(v);
	}

	/**
	 * Writes the specified byte to this output stream.
	 */
	public void writeByte(int v) throws IOException {
		write(v);
	}

	/**
	 * Writes the specified integer to this output stream.
	 */
	public void writeInt(int v) throws IOException {
		byte[] buff = new byte[4];
		for (int i = 0; i < buff.length; i++) {
			buff[i] = (byte) (v & 0xff);
			//log.debug(": 0x" + Integer.toHexString(buff[i]));
			v >>= 8;
		}
		write(buff);
	}

	/**
	 * Writes the specified integer to this output stream.
	 */
	public void writeInt3(int v) throws IOException {
		byte[] buff = new byte[3];
		for (int i = 0; i < buff.length; i++) {
			buff[i] = (byte) (v & 0xff);
			//log.debug(": 0x" + Integer.toHexString(buff[i]));
			v >>= 8;
		}
		write(buff);
	}

	/**
	 * Writes the specified integer to this output stream.
	 */
	public void writeInt2(int v) throws IOException {
		byte[] buff = new byte[2];
		for (int i = 0; i < buff.length; i++) {
			buff[i] = (byte) (v & 0xff);
			//log.debug(": 0x" + Integer.toHexString(buff[i]));
			v >>= 8;
		}
		write(buff);
	}

	/**
	 * Writes the specified {@link Poi Poi} to this output stream.
	 */
	public void writePoi(Poi poi) throws IOException {
		switch (poi.getType()) {
			case Categories.TYPE_CATEGORIES: {
				write((Categories)poi);
				return;
			}
			case Poi01.TYPE_01: {
				write((Poi01)poi);
				return;
			}
			case Poi02.TYPE_02: 
			case Poi02.TYPE_03: 
			case Poi02.TYPE_0F: {
				write((Poi02)poi);
				return;
			}
			case Poi04.TYPE_04: 
			case Poi04.TYPE_14: {
				write((Poi04)poi);
				return;
			}
			case Poi05.TYPE_05: 
			case Poi05.TYPE_15: {
				write((Poi05)poi);
				return;
			}
			case Poi06.TYPE_06: 
			case Poi06.TYPE_16: {
				write((Poi06)poi);
				return;
			}
			case Poi07.TYPE_07: 
			case Poi07.TYPE_17: {
				write((Poi07)poi);
				return;
			}
			case Poi08.TYPE_08: 
			case Poi08.TYPE_18: {
				write((Poi08)poi);
				return;
			}
			case Poi09.TYPE_09: 
			case Poi09.TYPE_19: {
				write((Poi09)poi);
				return;
			}
			case Poi10.TYPE_0A: 
			case Poi10.TYPE_1A: {
				write((Poi10)poi);
				return;
			}
			case Poi12.TYPE_0C: 
			case Poi12.TYPE_1C: {
				write((Poi12)poi);
				return;
			}
			case Poi13.TYPE_1D: {
				write((Poi13)poi);
				return;
			}
			default: {
				throw new StreamCorruptedException(
						String.format("invalid type code: %02X", poi.getType()));
			}
		}
	}

	/**
	 * Writes the specified {@link Categories Categories} to this output stream.
	 * @throws IOException 
	 */
	private void write(Categories poi) throws IOException {
		int count = poi.count(); 
		writeInt(count);
		for (Poi category: poi) {
			int id = ((Category) category).getCategoryId();
			writeInt(id);
		}
		int offset = poi.count() * 8 + 8; 
		writeInt(offset);
		for (Poi category: poi) {
			offset += ((PoiContainer) category).size();
			writeInt(offset);
		}
	}

	/**
	 * Writes the specified {@link Poi01 Poi01} to this output stream.
	 * @throws IOException 
	 */
	private void write(Poi01 poi) throws IOException {
		int type = poi.getType();
		write(type);
		int size = poi.size(); 
		writeInt(size);
		int longitude1 = poi.getLon1();
		writeInt(longitude1);
		int latitude1 = poi.getLat1();
		writeInt(latitude1);
		int longitude2 = poi.getLon2();
		writeInt(longitude2);
		int latitude2 = poi.getLat2();
		writeInt(latitude2);
	}

	/**
	 * Writes the specified {@link Poi02 Poi02} to this output stream.
	 * @throws IOException 
	 */
	private void write(Poi02 poi) throws IOException {
		int type = poi.getType();
		write(type);
		int size = poi.size(); 
		writeInt(size);
		int longitude = poi.getLon();
		writeInt(longitude);
		int latitude = poi.getLat();
		writeInt(latitude);
		byte[] name = poi.name();
		write(name);
	}

	/**
	 * Writes the specified {@link Poi04 Poi04} to this output stream.
	 * @throws IOException 
	 */
	private void write(Poi04 poi) throws IOException {
		int type = poi.getType();
		write(type);
		int longitude = poi.getLon();
		writeInt3(longitude);
		int latitude = poi.getLat();
		writeInt3(latitude);
	}

	/**
	 * Writes the specified {@link Poi05 Poi05} to this output stream.
	 * @throws IOException 
	 */
	private void write(Poi05 poi) throws IOException {
		int type = poi.getType();
		write(type);
		int longitude = poi.getLon();
		writeInt3(longitude);
		int latitude = poi.getLat();
		writeInt3(latitude);
		int name = poi.getName();
		writeInt2(name);
	}

	/**
	 * Writes the specified {@link Poi06 Poi06} to this output stream.
	 * @throws IOException 
	 */
	private void write(Poi06 poi) throws IOException {
		int type = poi.getType();
		write(type);
		int longitude = poi.getLon();
		writeInt3(longitude);
		int latitude = poi.getLat();
		writeInt3(latitude);
		int name = poi.getName();
		writeInt3(name);
	}

	/**
	 * Writes the specified {@link Poi07 Poi07} to this output stream.
	 * @throws IOException 
	 */
	private void write(Poi07 poi) throws IOException {
		int type = poi.getType();
		write(type);
		int size = poi.size(); 
		write(size);
		int longitude = poi.getLon();
		writeInt3(longitude);
		int latitude = poi.getLat();
		writeInt3(latitude);
		byte[] name = poi.name();
		write(name);
	}

	/**
	 * Writes the specified {@link Poi08 Poi08} to this output stream.
	 * @throws IOException 
	 */
	private void write(Poi08 poi) throws IOException {
		int type = poi.getType();
		write(type);
		int size = poi.size(); 
		write(size);
		int longitude = poi.getLon();
		writeInt3(longitude);
		int latitude = poi.getLat();
		writeInt3(latitude);
		byte[] name = poi.name();
		write(name);
	}

	/**
	 * Writes the specified {@link Poi09 Poi09} to this output stream.
	 * @throws IOException 
	 */
	private void write(Poi09 poi) throws IOException {
		int type = poi.getType();
		write(type);
		int size = poi.size(); 
		write(size);
		int longitude = poi.getLon();
		writeInt3(longitude);
		int latitude = poi.getLat();
		writeInt3(latitude);
		byte[] name = poi.name();
		write(name);
	}

	/**
	 * Writes the specified {@link Poi10 Poi10} to this output stream.
	 * @throws IOException 
	 */
	private void write(Poi10 poi) throws IOException {
		int type = poi.getType();
		write(type);
		int size = poi.size(); 
		write(size);
		int longitude = poi.getLon();
		writeInt3(longitude);
		int latitude = poi.getLat();
		writeInt3(latitude);
		byte[] name = poi.name();
		write(name);
	}

	/**
	 * Writes the specified {@link Poi12 Poi12} to this output stream.
	 * @throws IOException 
	 */
	private void write(Poi12 poi) throws IOException {
		int type = poi.getType();
		write(type);
		int size = poi.size(); 
		write(size);
		int longitude = poi.getLon();
		writeInt3(longitude);
		int latitude = poi.getLat();
		writeInt3(latitude);
		byte[] name = poi.name();
		write(name);
	}

	/**
	 * Writes the specified {@link Poi13 Poi13} to this output stream.
	 * @throws IOException 
	 */
	private void write(Poi13 poi) throws IOException {
		int type = poi.getType();
		write(type);
		int size = poi.size(); 
		write(size);
		int longitude = poi.getLon();
		writeInt(longitude);
		int latitude = poi.getLat();
		writeInt(latitude);
		int code = poi.getCode(); 
		write(code);
		byte[] unknown = poi.getUnknown();
		if (unknown != null) {
			write(unknown);
		}
		byte[] name = poi.name();
		write(name);
	}
}
