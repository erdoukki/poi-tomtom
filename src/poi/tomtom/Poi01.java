package poi.tomtom;

/**
<h3>Record 01.</h3>
This record is an {@link AreaRecord Area Record}. His goal is to determine a square.
<br/>
The format is the following :
<p/>
<table border="1">
  <tr><th width=110>Bytes</th><th>Description</th></tr>
  <tr><td>1 byte</td><td>The record type = <b>0x01</b></td></tr>
  <tr><td>4 bytes</td><td>S : the total size of this record in bytes (including the 21 bytes header)</td></tr>
  <tr><td>4 bytes</td><td>{@link #longitude1 Longitude1} (in decimal degrees). This value has to be divide by 100000</td></tr>
  <tr><td>4 bytes</td><td>{@link #latitude1 Latitude1} (in decimal degrees). This value has to be divide by 100000</td></tr>
  <tr><td>4 bytes</td><td>{@link #longitude2 Longitude2} (in decimal degrees). This value has to be divide by 100000</td></tr>
  <tr><td>4 bytes</td><td>{@link #latitude2 Latitude2} (in decimal degrees). This value has to be divide by 100000</td></tr>
  <tr><td>N bytes</td><td>Block of data containing other records</td></TR>
</table>

 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public class Poi01 extends Pois implements AreaRecord {

	public static final byte POI01 = 1;

	public static final int SIZE = 21;

	private int longitude1;
	private int latitude1;
	private int longitude2;
	private int latitude2;

	protected Poi01(int type, PoiContainer parent) {
		super(type, parent);
		setSize(SIZE);
	}

	@Override
	public int getLongitude1() {
		return longitude1;
	}
	@Override
	public void setLongitude1(int longitude1) {
		this.longitude1 = longitude1;
	}
	@Override
	public int getLatitude1() {
		return latitude1;
	}
	@Override
	public void setLatitude1(int latitude1) {
		this.latitude1 = latitude1;
	}
	@Override
	public int getLongitude2() {
		return longitude2;
	}
	@Override
	public void setLongitude2(int longitude2) {
		this.longitude2 = longitude2;
	}
	@Override
	public int getLatitude2() {
		return latitude2;
	}
	@Override
	public void setLatitude2(int latitude2) {
		this.latitude2 = latitude2;
	}

	@Override
	public boolean add(Poi poi) {
		if (poi instanceof PoiRecord) {
			return add((PoiRecord) poi);
		}
		return false;
	}

	public boolean add(PoiRecord poi) {
		if (longitude1 < poi.getLongitude()) {
			longitude1 = poi.getLongitude();
		}
		if (longitude2 > poi.getLongitude()) {
			longitude2 = poi.getLongitude();
		}
		if (latitude1 < poi.getLatitude()) {
			latitude1 = poi.getLatitude();
		}
		if (latitude2 > poi.getLatitude()) {
			latitude2 = poi.getLatitude();
		}
		int i = 0;
		while (i < count()) {
			if (((PoiRecord) get(i)).getLongitude() > poi.getLongitude()) {
				break;
			}
			i++;
		}
		add(i, poi);
		return true;
	}

	public void split(boolean hv, int n) {
		// System.out.println(n);
		int half = count() / 2;
		Poi01 root = new Poi01(1, this);
		add(0, root);
		//root.size(TYPE + SIZE + LONGITUDE + LATITUDE + LONGITUDE + LATITUDE);
		if (hv) {
			root.setLongitude1(getLongitude2()); // 2!!!
			root.setLatitude1(getLatitude1());
		} else {
			root.setLongitude1(getLongitude1());
			root.setLatitude1(getLatitude2()); // 2!!!
		}
		root.setLongitude2(getLongitude2());
		root.setLatitude2(getLatitude2());
		while (half > 0) {
			int i = 1;
			if (!hv) {
				int lat = ((PoiRecord) get(1)).getLatitude();
				for (int j = 2; j < count(); j++) {
					if (((PoiRecord) get(j)).getLatitude() < lat) {
						i = j;
					}
				}
			}
			Poi poi = remove(i);
			root.add(poi);
			half--;
		}
		root = new Poi01(1, this);
		add(1, root);
		//root.size(TYPE + SIZE + LONGITUDE + LATITUDE + LONGITUDE + LATITUDE);
		root.setLongitude1(getLongitude1());
		root.setLatitude1(getLatitude1());
		if (hv) {
			root.setLongitude2(((AreaRecord) get(0)).getLongitude1()); // !!!
			root.setLatitude2(getLatitude2());
		} else {
			root.setLongitude2(getLongitude2());
			root.setLatitude2(((AreaRecord) get(0)).getLatitude1()); // !!!
		}
		while (count() > 2) {
			int i = 2;
			Poi poi = remove(i);
			root.add(poi);
		}
		if ((n >> 1) > 0) {
			((Poi01) get(0)).split(!hv, n >> 1);
			((Poi01) get(1)).split(!hv, n >> 1);
		}
	}

	@Override
	public int getOffset(Poi to) {
		int offset = super.getOffset(to);
		offset += 1 + 4 + 4 + 4 + 4 + 4;//TYPE + .SIZE + LONGITUDE + LATITUDE + LONGITUDE + LATITUDE;
		return offset;
	}

	@Override
	public String toString() {
		return "Poi01 [S:" + size() + ", X1:" + longitude1 + ", Y1:" + latitude1 + ", X2:" + longitude2 + ", Y2:" + latitude2 + "]";
	}
}
