package test;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import poi.tomtom.Poi;
import poi.tomtom.PoiInputStream;

public class TestPoi {

	@Test 
	public void testRead01() {
		byte[] buff = new byte[] {0x01, 0x7F, (byte) 0x8D, 0x00, 0x00, 0x38, (byte) 0xC8, 0x42, 0x00, 0x33, 0x1A, 0x40, 0x00, 0x37, 0x25, 0x28, 0x00};
		ByteArrayInputStream bais = new ByteArrayInputStream(buff);
		PoiInputStream is = new PoiInputStream(bais);
		Poi poi;
		try {
			while (is.available() > 0) {
				poi = is.readPoi();
				System.out.println(poi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test 
	public void testRead02() {
		byte[] buff = new byte[] {0x02, 0x15, 0x00, 0x00, 0x00, 0x33, 0x1A, 0x40, 0x00, 0x37, 0x25, 0x28, 0x00, 0x73, 0x74, 0x61, 0x74, 0x69, 0x6f, 0x6e, 0x00};
		ByteArrayInputStream bais = new ByteArrayInputStream(buff);
		PoiInputStream is = new PoiInputStream(bais);
		Poi poi;
		try {
			while (is.available() > 0) {
				poi = is.readPoi();
				System.out.println(poi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
