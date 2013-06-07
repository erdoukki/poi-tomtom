package poi.tomtom;

/**
<h3>Poi Record description.</h3>

<p>
There are 2 kinds of records :
<ul>
  <li>{@link AreaRecord Area Record} : they divide the map in subsquares. This kind of record is a {@link PoiContainer container} of other records - area record or POIs records</li>
  <li>{@link PoiRecord POIs Record} : they contain a POI. </li>
</ul>

 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public interface Poi {

//	public <P extends PoiContainer> P getParent();
	public int size();
	public int offset();
}
