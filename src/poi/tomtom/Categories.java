package poi.tomtom;

public class Categories extends Pois {

	protected Categories(int type, PoiContainer parent) {
		super(type, parent);
	}

	public String toString() {
		return "Categories : " + count();
	}
}
