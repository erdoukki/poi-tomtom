package poi.tomtom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public abstract class Pois extends PoiCommon implements PoiContainer, Iterable<Poi> {

	private List<Poi> records = new ArrayList<Poi>();

	protected Pois(int type, PoiContainer parent) {
		super(type, parent);
	}

	@Override
	public Iterator<Poi> iterator() {
		return records.iterator();
	}

	/**
	 *  count of records.
	 */
	@Override
	public int count() {
		return records.size();
	}

	@Override
	public Poi get(int index) {
		return records.get(index);
	}

	public boolean add(Poi poi) {
		if (poi instanceof PoiRecord) {
			return add((PoiRecord) poi);
		}
		add(count(),poi);
		return true;
	}

	public boolean add(PoiRecord poi) {
		Poi01 root = null;
		for (int i = 0; i < records.size(); i++) {
			if (records.get(i) instanceof Poi01) {
				root = (Poi01)records.get(i);
				break;
			}
		}
		if (root == null) {
			root = new Poi01((byte)1, this);
			root.setLat1(poi.getLat());
			root.setLon1(poi.getLon());
			root.setLat2(poi.getLat());
			root.setLon2(poi.getLon());
			setSize(size() + root.size());
			records.add(count(), root);
		}
		return root.add(poi);
	}

	@Override
	public void add(int index, Poi poi) {
		setSize(size() + poi.size());
		records.add(index, poi);
	}

	@Override
	public Poi set(int index, Poi poi) {
		Poi old = records.set(index, poi);
		int delta = poi.size() - old.size();
		//log.debug(delta);
		setSize(size() + delta);
		return old;
	}

	public Poi remove(int index) {
		Poi poi = records.remove(index);
		setSize(size() - poi.size());
		return poi;
	}

	@Override
	public int getOffset(Poi to) {
		int offset = super.offset();
		for (int i = 0; i < records.size(); i++) {
			Poi poi = records.get(i);
			if (poi == to) {
				break;
			}
			offset += poi.size();
		}
		return offset;
	}
}
