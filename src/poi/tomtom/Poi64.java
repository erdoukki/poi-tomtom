package poi.tomtom;

/**
<h3>Record 0x64.</h3>
This record is a {@link PoiRecord POI Record}.
<br/>
The format is the following :
<p/>
<table border="1">
  <tr><th width=110>Bytes</th><th>Description</th></tr>
  <tr><td>1 byte </td><td>T:  type (always 100)</td></tr>
  <tr><td>4 bytes</td><td>S:  the total size of this record in bytes</td></tr>
  <tr><td>4 bytes</td><td>U1: ???</td></tr>
  <tr><td>4 bytes</td><td>V:  version</td></tr>
  <tr><td>3 bytes</td><td>U2: ???</td></tr>
  <tr><td>1 byte </td><td>C:  check sum</td></tr>
  <tr><td>4 bytes</td><td>U3: ???</td></tr>
</table>
 * 
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class Poi64 extends PoiCommon implements Poi {

	public static final byte TYPE_64 = 100;

	public static final int SIZE = 21;
	public static final int UNKNOWN1 = 4;
	public static final int UNKNOWN2 = 3;
	public static final int UNKNOWN3 = 4;

	private byte[] unknown1 = new byte[UNKNOWN1];
	private int version;
	private byte[] unknown2 = new byte[UNKNOWN2];
	private byte check;
	private byte[] unknown3 = new byte[UNKNOWN3];

	public Poi64(int type, PoiContainer parent) {
		super(type, parent);
		setSize(SIZE);
	}
	
	public byte[] getUnknown1() {
		return unknown1;
	}
	public void setUnknown1(byte[] unknown1) {
		this.unknown1 = unknown1;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public byte[] getUnknown2() {
		return unknown2;
	}
	public void setUnknown2(byte[] unknown2) {
		this.unknown2 = unknown2;
	}
	public int getCheck() {
		return check;
	}
	public void setCheck(byte check) {
		this.check = check;
	}
	public byte[] getUnknown3() {
		return unknown3;
	}
	public void setUnknown3(byte[] unknown3) {
		this.unknown3 = unknown3;
	}

	public void doCheck() {
		int check = getType();
		// System.out.println(Integer.toHexString(check));
		for (int i = 0; i < 4; i++) {
			check += ((size() >> (8 * i)) & 0xff);
			// System.out.println(Integer.toHexString((size() >> (8*i)) & 0xff));
			// System.out.println(Integer.toHexString(check));
		}
		for (int i = 0; i < UNKNOWN1; i++) {
			check += (unknown1[i] & 0xff);
			// System.out.println(Integer.toHexString((unknown1 >> (8*i)) & 0xff));
			// System.out.println(Integer.toHexString(check));
		}
		for (int i = 0; i < 4; i++) {
			check += ((version >> (8 * i)) & 0xff);
			// System.out.println(Integer.toHexString((version >> (8*i)) & 0xff));
			// System.out.println(Integer.toHexString(check));
		}
		for (int i = 0; i < UNKNOWN2; i++) {
			check += (unknown2[i] & 0xff);
			// System.out.println(Integer.toHexString((unknown2 >> (8*i)) & 0xff));
			// System.out.println(Integer.toHexString(check));
		}
		this.check = (byte) (check & 0xff);
		// System.out.println(Integer.toHexString(this.check));
	}

	@Override
	public String toString() {
		return "Poi64 [S:" + size() + ", u1:" + hex(unknown1) + ", ver:0x" + Integer.toHexString(version) + ", u2:" + hex(unknown2) + ", chc:0x" + Integer.toHexString(check & 0xff) + ", u3:" + hex(unknown3) + "]";
	}
}
