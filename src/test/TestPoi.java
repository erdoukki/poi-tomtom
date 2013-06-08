package test;

import java.io.FileInputStream;

import org.junit.Test;

import poi.tomtom.Poi;
import poi.tomtom.Poi01;
import poi.tomtom.PoiInputStream;

public class TestPoi {

	@Test
	public void test() {
		try {
			String path = System.getProperty("user.dir");
			PoiInputStream is = new PoiInputStream(new FileInputStream(path + "/LPG_Greece.ov2"));
			Poi parent = null;
			while (is.available() > 0) {
				Poi poi = is.readPoi();
				if (poi instanceof Poi01) 
					parent  = poi;
				System.out.println(poi);
			}
			System.out.println(parent); //24491
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
