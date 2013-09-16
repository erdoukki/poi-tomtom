package poi.tomtom;

public class PoiContext {

	private Dictionary dict;

	/** Returns the dictionary. If there is no any will create implicit one */
	public Dictionary getDictionary() {
		if (dict == null) {
			dict = new ImplDictionary();
		}
		return dict;
	}

	public void setDictionary(Dictionary dict) {
		this.dict = dict;
	}

	public boolean hasExplDictionary() {
		return (dict != null) && (dict instanceof ExplDictionary);
	}

}
