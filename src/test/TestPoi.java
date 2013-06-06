package test;

import java.io.FileInputStream;

import org.junit.Test;

import poi.tomtom.Poi;
import poi.tomtom.PoiInputStream;

public class TestPoi {

	@Test
	public void test() {
		try {
			String path = System.getProperty("user.dir");
			PoiInputStream is = new PoiInputStream(new FileInputStream(path + "/AutoGas.ov2"));
			while (is.available() > 0) {
				Poi poi = is.readPoi();
				System.out.println(poi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
