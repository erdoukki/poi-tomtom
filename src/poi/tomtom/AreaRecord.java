package poi.tomtom;

/**
<h3>Area Records</h3>

The logic of Area Record is to divide the complete map (we say level 0) in more subsquares (level 1), each level 1's square is divide in more subsquares (level 2) and so on. There are many sublevels. The last Area Record contains several POI Records, but no more that 10 (this seems to be a rule).
Also, the way the areas are cut is not fixed. Each Area record has a specific size, depending of the POI density in the Area.

 * @author <a href="mailto:oritomov@yahoo.com">orlin tomov</a>
 */

public interface AreaRecord extends PoiContainer {

  /**
   * longitude 1.
   */
  public int getLon1();
  public void setLon1(int longitude);

  /**
   * latitude 1.
   */
  public int getLat1();
  public void setLat1(int latitude);

  /**
   * longitude 2.
   */
  public int getLon2();
  public void setLon2(int longitude);

  /**
   * latitude 2.
   */
  public int getLat2();
  public void setLat2(int latitude);
}
