package poi.tomtom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
<h3>POI category.</h3>

The POIs are organized in categories. 
There are different categories (Restaurants, Petrol Station, Parking...) that are hard-coded.
</br>
Each category is encoded as :
<ul>
	<li>a category ID : an unique integer that refer to the category</li>
	<li>a category description : a simple string, depending on the language. This description is different in each language</li>
</ul>
Here are the categories IDs that exists in Tomtom Navigator. This list is as complete as possible.
</p>
<table border="1">
  <tr><th>id</th><th>english description</th></tr>
  <tr><td>1C8E</td><td>Car repair/garage</td></tr>
  <tr><td>1C8F</td><td>Petrol station</td></tr>
  <tr><td>1C90</td><td>Car rental facility</td></tr>
  <tr><td>1C91</td><td>Parking garage</td></tr>
  <tr><td>1C92</td><td>Hotel/Motel</td></tr>
  <tr><td>1C93</td><td>Restaurant</td></tr>
  <tr><td>1C94</td><td>Tourist information office</td></tr>
  <tr><td>1C95</td><td>Museum/Art gallery</td></tr>
  <tr><td>1C96</td><td>Theater</td></tr>
  <tr><td>1C97</td><td>Cultural center</td></tr>
  <tr><td>1C98</td><td>Sports center</td></tr>
  <tr><td>1C99</td><td>Hospital/Clinic</td></tr>
  <tr><td>1C9A</td><td>Police station</td></tr>
  <tr><td>1C9C</td><td>Post office</td></tr>
  <tr><td>1C9E</td><td>Primacy</td></tr>
  <tr><td>1CA9</td><td>Scenic/Panoramic view</td></tr>
  <tr><td>1CAA</td><td>Swimming pool</td></tr>
  <tr><td>1CAB</td><td>Place of worship</td></tr>
  <tr><td>1CAD</td><td>Casino</td></tr>
  <tr><td>1CAE</td><td>Cinema</td></tr>
  <tr><td>1CB5</td><td>Winery</td></tr>
  <tr><td>1CB8</td><td>Ferry terminal</td></tr>
  <tr><td>1CC0</td><td>Camping ground</td></tr>
  <tr><td>1CC5</td><td>Embassy</td></tr>
  <tr><td>1CC6</td><td>Border crossing</td></tr>
  <tr><td>1CC7</td><td>Government office</td></tr>
  <tr><td>1CC9</td><td>Open parking</td></tr>
  <tr><td>1CCD</td><td>Shopping center</td></tr>
  <tr><td>1CCE</td><td>Stadium</td></tr>
  <tr><td>1CD0</td><td>Tourist attraction</td></tr>
  <tr><td>1CD1</td><td>College/University</td></tr>
  <tr><td>1CD3</td><td>City center</td></tr>
  <tr><td>1CD4</td><td>Railway station</td></tr>
  <tr><td>1CD7</td><td>Airport</td></tr>
  <tr><td>1CD9</td><td>Exhibition center</td></tr>
  <tr><td>1CE3</td><td>Rest area</td></tr>
  <tr><td>1CE5</td><td>Cash dispenser</td></tr>
  <tr><td>2488</td><td>Company</td></tr>
  <tr><td>248D</td><td>Beach</td></tr>
  <tr><td>2490</td><td>Ice skating ring</td></tr>
  <tr><td>2491</td><td>Shop</td></tr>
  <tr><td>2492</td><td>Park and recreation area</td></tr>
  <tr><td>2493</td><td>Courthouse</td></tr>
  <tr><td>2494</td><td>Mountain peak</td></tr>
  <tr><td>2495</td><td>Opera</td></tr>
  <tr><td>2497</td><td>Concert hall</td></tr>
  <tr><td>2499</td><td>Tennis court</td></tr>
  <tr><td>249B</td><td>Water sport</td></tr>
  <tr><td>249D</td><td>Doctor</td></tr>
  <tr><td>249E</td><td>Dentist</td></tr>
  <tr><td>249F</td><td>Veterinarian</td></tr>
  <tr><td>24A1</td><td>Convention center</td></tr>
  <tr><td>24A2</td><td>Leisure center</td></tr>
  <tr><td>24A3</td><td>Nightlife</td></tr>
  <tr><td>24A4</td><td>Marina/Yacht basin</td></tr>
  <tr><td>2648</td><td>&nbsp;</td></tr>
  <tr><td>2649</td><td>Legal/other</td></tr>
  <tr><td>26AE</td><td>Amusement Park</td></tr>
  <tr><td>26B2</td><td>Church</td></tr>
  <tr><td>26B6</td><td>Car dealer</td></tr>
  <tr><td>26B7</td><td>Golf course</td></tr>
  <tr><td>26B9</td><td>Library</td></tr>
  <tr><td>26C7</td><td>Zoo</td></tr>
  <tr><td>26CA</td><td>Rent car parking</td></tr>
  <tr><td>26CF</td><td>Mountain pass</td></tr>
  <tr><td>26FC</td><td>&nbsp;</td></tr>
</table>

* @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
*/
public class Category extends Pois {

	public static final int TYPE_CATEGORY = -4;
	public static final int TYPE_CATEGORIES = -5;

	/**
	 * id length in bytes.
	 */
	public static final int ID = 4;
	
	private static final Properties categories = new Properties();

	static {
		try {
			categories.load(new FileReader(System.getProperty("user.dir") + File.separator + "etc" + File.separator + "categories"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int categoryId;

	/**
	 * constructor.
	 */
	protected Category(int type, PoiContainer parent) {
		super(type, parent);
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		String key = Integer.toHexString(categoryId).toUpperCase();
		key = "0000".substring(key.length()) + key;
		return categories.getProperty(key, key);
	}
}
