package poi.tomtom;

/**

<h3>POI Records</h2>

A POI is a simple association of 3 kinds of data :
<ul>
  <li>a longitude</li>
  <li>a latitude</li>
  <li>a description</li>
</ul>

There are multiple methods used by Tomtom to compress (or not) the description. There are as many POI record types as encoding methods.

 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */
public interface PoiRecord extends Poi {

  /**
   * longitude.
   */
  public int getLongitude();

  /**
   * latitude.
   */
  public int getLatitude();

}