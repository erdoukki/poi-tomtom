package poi.tomtom;

public class Categories extends Pois {

	public static final int TYPE_CATEGORIES = -5;

	public Categories(int type, PoiContainer parent) {
		super(type, parent);
	}

	public String toString() {
		return "Categories : " + count();
	}
}
