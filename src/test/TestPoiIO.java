package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;

import poi.tomtom.Categories;
import poi.tomtom.Category;
import poi.tomtom.LogCategory;
import poi.tomtom.Poi01;
import poi.tomtom.Poi02;
import poi.tomtom.Poi04;
import poi.tomtom.Poi05;
import poi.tomtom.Poi06;
import poi.tomtom.Poi07;
import poi.tomtom.Poi08;
import poi.tomtom.Poi09;
import poi.tomtom.Poi10;
import poi.tomtom.Poi12;
import poi.tomtom.Poi13;
import poi.tomtom.PoiCommon;
import poi.tomtom.PoiInputStream;
import poi.tomtom.PoiInputStream.Mode;
import poi.tomtom.PoiOutputStream;

public class TestPoiIO {

	protected static LogCategory log = LogCategory.getLogger(TestPoiIO.class);

	private static final int LON1 = 1234567;
	private static final int LAT1 = 2345678;
	private static final double LON2 = 34.56789;
	private static final double LAT2 = 45.67890;
	private static final int LON3 = 5678901;
	private static final int LAT3 = 6789012;
	private static final String NAME = "station";

	private static final byte[] POI01 = new byte[] {0x01, 0x15, 0x00, 0x00, 0x00, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x35, (byte) 0xa7, 0x56, 0x00, (byte) 0x94, (byte) 0x97, 0x67, 0x00};
	private static final byte[] POI02 = new byte[] {0x02, 0x1e, 0x00, 0x00, 0x00, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x3e, 0x3e, 0x73, 0x21, 0x74, 0x21, 0x61, 0x21, 0x74, 0x21, 0x69, 0x21, 0x6f, 0x21, 0x6e, 0x21, 0x00};
	private static final byte[] POI04 = new byte[] {0x01, 0x1c, 0x00, 0x00, 0x00, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x35, (byte) 0xa7, 0x56, 0x00, (byte) 0x94, (byte) 0x97, 0x67, 0x00, 0x04, 0x15, (byte) 0xbf, 0x34, 0x52, (byte) 0xc5, (byte) 0xbf};
	private static final byte[] POI05 = new byte[] {0x01, 0x1e, 0x00, 0x00, 0x00, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x35, (byte) 0xa7, 0x56, 0x00, (byte) 0x94, (byte) 0x97, 0x67, 0x00, 0x05, 0x15, (byte) 0xbf, 0x34, 0x52, (byte) 0xc5, (byte) 0xbf, 0x05, 0x00};
	private static final byte[] POI06 = new byte[] {0x01, 0x1f, 0x00, 0x00, 0x00, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x35, (byte) 0xa7, 0x56, 0x00, (byte) 0x94, (byte) 0x97, 0x67, 0x00, 0x06, 0x15, (byte) 0xbf, 0x34, 0x52, (byte) 0xc5, (byte) 0xbf, 0x05, 0x00, 0x00};
	private static final byte[] POI07 = new byte[] {0x01, 0x2e, 0x00, 0x00, 0x00, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x35, (byte) 0xa7, 0x56, 0x00, (byte) 0x94, (byte) 0x97, 0x67, 0x00, 0x07, 0x11, 0x15, (byte) 0xbf, 0x34, 0x52, (byte) 0xc5, (byte) 0xbf, 0x3e, 0x3e, 0x73, 0x21, 0x74, 0x21, 0x61, 0x21, 0x74, 0x21, 0x69, 0x21, 0x6f, 0x21, 0x6e, 0x21, 0x00};
	private static final byte[] POI08 = new byte[] {0x01, 0x22, 0x00, 0x00, 0x00, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x35, (byte) 0xa7, 0x56, 0x00, (byte) 0x94, (byte) 0x97, 0x67, 0x00, 0x08, 0x05, 0x15, (byte) 0xbf, 0x34, 0x52, (byte) 0xc5, (byte) 0xbf, 0x29, 0x64, 0x2C, (byte) 0xE8, 0x24};
	private static final byte[] POI09 = new byte[] {0x01, 0x22, 0x00, 0x00, 0x00, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x35, (byte) 0xa7, 0x56, 0x00, (byte) 0x94, (byte) 0x97, 0x67, 0x00, 0x09, 0x05, 0x15, (byte) 0xbf, 0x34, 0x52, (byte) 0xc5, (byte) 0xbf, 0x68, 0x78, 0x3c, (byte) 0xb2, 0x01};
	private static final byte[] POI10 = new byte[] {0x01, 0x22, 0x00, 0x00, 0x00, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x35, (byte) 0xa7, 0x56, 0x00, (byte) 0x94, (byte) 0x97, 0x67, 0x00, 0x0a, 0x05, 0x15, (byte) 0xbf, 0x34, 0x52, (byte) 0xc5, (byte) 0xbf, 0x73, 0x09, 0x3c, 0x5f, 0x0e};
	private static final byte[] POI12 = new byte[] {0x01, 0x24, 0x00, 0x00, 0x00, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x35, (byte) 0xa7, 0x56, 0x00, (byte) 0x94, (byte) 0x97, 0x67, 0x00, 0x0c, 0x07, 0x15, (byte) 0xbf, 0x34, 0x52, (byte) 0xc5, (byte) 0xbf, 0x51, 0x02, (byte) 0x89, 0x5c, (byte) 0xd3, 0x21, 0x03};
	private static final byte[] POI13 = new byte[] {0x1d, 0x14, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x06, 0x00, 0x00, 0x00, 0x3e, 0x3e, 0x73, 0x21, 0x74, 0x21, 0x61, 0x21, 0x74, 0x21, 0x69, 0x21, 0x6f, 0x21, 0x6e, 0x21, 0x00};
	private static final byte[] POICAT = new byte[] {0x01, 0x00, 0x00, 0x00, (byte) 0x8e, 0x1c, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x2f, 0x00, 0x00, 0x00, 
		0x1d, 0x14, (byte) 0x87, (byte) 0xd6, 0x12, 0x00, (byte) 0xce, (byte) 0xca, 0x23, 0x00, 0x06, 0x00, 0x00, 0x00, 0x3e, 0x3e, 0x73, 0x21, 0x74, 0x21, 0x61, 0x21, 0x74, 0x21, 0x69, 0x21, 0x6f, 0x21, 0x6e, 0x21, 0x00};
	
	@Test 
	public void testRead01() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POI01);
		PoiInputStream is = new PoiInputStream(bais);
		Poi01 poi = null;
		try {
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi01.TYPE_01);
		assertEquals(poi.size(), Poi01.SIZE);
		assertEquals(poi.getLon1(), LON1);
		assertEquals(poi.getLat1(), LAT1);
		assertEquals(poi.getLon2(), LON3);
		assertEquals(poi.getLat2(), LAT3);
	}

	@Test 
	public void testWrite01() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Poi01 poi = new Poi01(Poi01.TYPE_01, null);
		poi.setSize(Poi01.SIZE);
		poi.setLon1(LON1);
		poi.setLat1(LAT1);
		poi.setLon2(LON3);
		poi.setLat2(LAT3);
		try {
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		assertArrayEquals(POI01, result);
	}

	@Test 
	public void testRead02() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POI02);
		PoiInputStream is = new PoiInputStream(bais);
		Poi02 poi = null;
		try {
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi02.TYPE_02);
		assertEquals(poi.size(), Poi02.HEADER + NAME.length() * 2 + 3);
		assertEquals(poi.getLon(), LON1);
		assertEquals(poi.getLat(), LAT1);
		assertEquals(poi.getName(), NAME);
	}

	@Test 
	public void testWrite02() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Poi02 poi = new Poi02(Poi02.TYPE_02, null);
		poi.setSize(Poi02.HEADER);
		poi.setLon(LON1);
		poi.setLat(LAT1);
		poi.setName(NAME);
		try {
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		assertArrayEquals(POI02, result);
	}

	@Test 
	public void testRead04() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POI04);
		PoiInputStream is = new PoiInputStream(bais);
		Poi04 poi = null;
		try {
			Poi01 parent = is.readPoi();
			log.debug(parent);
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi04.TYPE_04);
		assertEquals(poi.size(), Poi04.SIZE);
//		assertEquals(poi.getLongitude(), LON2);
//		assertEquals(poi.getLatitude(), LAT2);
	}

	@Test 
	public void testWrite04() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Poi01 parent = new Poi01(Poi01.TYPE_01, null);
		parent.setSize(Poi01.SIZE);
		parent.setLon1(LON1);
		parent.setLat1(LAT1);
		parent.setLon2(LON3);
		parent.setLat2(LAT3);
		Poi04 poi = new Poi04(Poi04.TYPE_04, parent);
		poi.setSize(Poi04.SIZE);
		poi.setLongitude(LON2);
		poi.setLatitude(LAT2);
		try {
			is.writePoi(parent);
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		assertArrayEquals(POI04, result);
	}

	@Test 
	public void testRead05() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POI05);
		PoiInputStream is = new PoiInputStream(bais);
		Poi05 poi = null;
		try {
			Poi01 parent = is.readPoi();
			log.debug(parent);
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi05.TYPE_05);
		assertEquals(poi.size(), Poi05.SIZE);
		//assertEquals(poi.getLongitude(), LON2);
		//assertEquals(poi.getLatitude(), LAT2);
		assertEquals(poi.getName(), 5);
	}

	@Test 
	public void testWrite05() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Poi01 parent = new Poi01(Poi01.TYPE_01, null);
		parent.setSize(Poi01.SIZE);
		parent.setLon1(LON1);
		parent.setLat1(LAT1);
		parent.setLon2(LON3);
		parent.setLat2(LAT3);
		Poi05 poi = new Poi05(Poi05.TYPE_05, parent);
		poi.setSize(Poi05.SIZE);
		poi.setLongitude(LON2);
		poi.setLatitude(LAT2);
		poi.setName(5);
		try {
			is.writePoi(parent);
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		assertArrayEquals(POI05, result);
	}

	@Test 
	public void testRead06() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POI06);
		PoiInputStream is = new PoiInputStream(bais);
		Poi06 poi = null;
		try {
			Poi01 parent = is.readPoi();
			log.debug(parent);
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi06.TYPE_06);
		assertEquals(poi.size(), Poi06.SIZE);
		//assertEquals(poi.getLongitude(), LON2);
		//assertEquals(poi.getLatitude(), LAT2);
		assertEquals(poi.getName(), 5);
	}

	@Test 
	public void testWrite06() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Poi01 parent = new Poi01(Poi01.TYPE_01, null);
		parent.setSize(Poi01.SIZE);
		parent.setLon1(LON1);
		parent.setLat1(LAT1);
		parent.setLon2(LON3);
		parent.setLat2(LAT3);
		Poi06 poi = new Poi06(Poi06.TYPE_06, parent);
		poi.setSize(Poi06.SIZE);
		poi.setLongitude(LON2);
		poi.setLatitude(LAT2);
		poi.setName(5);
		try {
			is.writePoi(parent);
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		assertArrayEquals(POI06, result);
	}

	@Test 
	public void testRead07() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POI07);
		PoiInputStream is = new PoiInputStream(bais);
		Poi07 poi = null;
		try {
			Poi01 parent = is.readPoi();
			log.debug(parent);
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi07.TYPE_07);
		assertEquals(poi.size(), NAME.length() * 2 + 3);
		//assertEquals(poi.getLongitude(), LON2);
		//assertEquals(poi.getLatitude(), LAT2);
		assertEquals(poi.getName(), NAME);
	}

	@Test 
	public void testWrite07() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Poi01 parent = new Poi01(Poi01.TYPE_01, null);
		parent.setSize(Poi01.SIZE);
		parent.setLon1(LON1);
		parent.setLat1(LAT1);
		parent.setLon2(LON3);
		parent.setLat2(LAT3);
		Poi07 poi = new Poi07(Poi07.TYPE_07, parent);
		poi.setSize(Poi07.SIZE);
		poi.setLongitude(LON2);
		poi.setLatitude(LAT2);
		poi.setName(NAME);
		try {
			is.writePoi(parent);
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		assertArrayEquals(POI07, result);
	}

	@Test 
	public void testRead08() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POI08);
		PoiInputStream is = new PoiInputStream(bais);
		Poi08 poi = null;
		try {
			Poi01 parent = is.readPoi();
			log.debug(parent);
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi08.TYPE_08);
		assertEquals(poi.size(), 5);
		//assertEquals(poi.getLongitude(), LON2);
		//assertEquals(poi.getLatitude(), LAT2);
		assertEquals(poi.getName(), NAME);
	}

	@Test 
	public void testWrite08() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Poi01 parent = new Poi01(Poi01.TYPE_01, null);
		parent.setSize(Poi01.SIZE);
		parent.setLon1(LON1);
		parent.setLat1(LAT1);
		parent.setLon2(LON3);
		parent.setLat2(LAT3);
		Poi08 poi = new Poi08(Poi08.TYPE_08, parent);
		poi.setSize(Poi08.SIZE);
		poi.setLongitude(LON2);
		poi.setLatitude(LAT2);
		poi.setName(new byte[] {0x29, 0x64, 0x2C, (byte) 0xE8, 0x24});
		try {
			is.writePoi(parent);
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		assertArrayEquals(POI08, result);
	}

	@Test 
	public void testRead09() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POI09);
		PoiInputStream is = new PoiInputStream(bais);
		Poi09 poi = null;
		try {
			Poi01 parent = is.readPoi();
			log.debug(parent);
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi09.TYPE_09);
		assertEquals(poi.size(), 5);
		//assertEquals(poi.getLongitude(), LON2);
		//assertEquals(poi.getLatitude(), LAT2);
		assertEquals(poi.getName(), NAME);
	}

	@Test 
	public void testWrite09() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Poi01 parent = new Poi01(Poi01.TYPE_01, null);
		parent.setSize(Poi01.SIZE);
		parent.setLon1(LON1);
		parent.setLat1(LAT1);
		parent.setLon2(LON3);
		parent.setLat2(LAT3);
		Poi09 poi = new Poi09(Poi09.TYPE_09, parent);
		poi.setSize(Poi09.SIZE);
		poi.setLongitude(LON2);
		poi.setLatitude(LAT2);
		poi.setName(NAME);
		try {
			is.writePoi(parent);
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		assertArrayEquals(POI09, result);
	}

	@Test 
	public void testRead10() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POI10);
		PoiInputStream is = new PoiInputStream(bais);
		Poi10 poi = null;
		try {
			Poi01 parent = is.readPoi();
			log.debug(parent);
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi10.TYPE_0A);
		assertEquals(poi.size(), 5);
		//assertEquals(poi.getLongitude(), LON2);
		//assertEquals(poi.getLatitude(), LAT2);
		assertEquals(poi.getName(), NAME);
	}

	@Test 
	public void testWrite10() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Poi01 parent = new Poi01(Poi01.TYPE_01, null);
		parent.setSize(Poi01.SIZE);
		parent.setLon1(LON1);
		parent.setLat1(LAT1);
		parent.setLon2(LON3);
		parent.setLat2(LAT3);
		Poi10 poi = new Poi10(Poi10.TYPE_0A, parent);
		poi.setSize(Poi10.SIZE);
		poi.setLongitude(LON2);
		poi.setLatitude(LAT2);
		poi.setName(new byte[] {0x73, 0x09, 0x3c, 0x5f, 0x0e});
		try {
			is.writePoi(parent);
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		assertArrayEquals(POI10, result);
	}

	@Test 
	public void testRead12() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POI12);
		PoiInputStream is = new PoiInputStream(bais);
		Poi12 poi = null;
		try {
			Poi01 parent = is.readPoi();
			log.debug(parent);
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi12.TYPE_0C);
		assertEquals(poi.size(), 7);
		//assertEquals(poi.getLongitude(), LON2);
		//assertEquals(poi.getLatitude(), LAT2);
		assertEquals(poi.getName(), NAME + ">012");
	}

	@Test 
	public void testWrite12() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Poi01 parent = new Poi01(Poi01.TYPE_01, null);
		parent.setSize(Poi01.SIZE);
		parent.setLon1(LON1);
		parent.setLat1(LAT1);
		parent.setLon2(LON3);
		parent.setLat2(LAT3);
		Poi12 poi = new Poi12(Poi12.TYPE_0C, parent);
		poi.setSize(Poi12.HEADER);
		poi.setLongitude(LON2);
		poi.setLatitude(LAT2);
		poi.setName(new byte[] {0x51, 0x02, (byte) 0x89, 0x5c, (byte) 0xd3, 0x21, 0x03});
		try {
			is.writePoi(parent);
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		assertArrayEquals(POI12, result);
	}

	@Test 
	public void testRead13() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POI13);
		PoiInputStream is = new PoiInputStream(bais);
		Poi13 poi = null;
		try {
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi13.TYPE_1D);
		assertEquals(poi.size(), NAME.length() * 2 + 3 + 3);
		assertEquals(poi.getLon(), LON1);
		assertEquals(poi.getLat(), LAT1);
		assertEquals(poi.getName(), NAME);
	}

	@Test 
	public void testWrite13() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Poi13 poi = new Poi13(Poi13.TYPE_1D, null);
		poi.setSize(Poi13.HEADER);
		poi.setLon(LON1);
		poi.setLat(LAT1);
		poi.setCode(6);
		poi.setName(NAME);
		try {
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		//System.out.println(PoiCommon.hex(result));
		assertArrayEquals(POI13, result);
	}

	@Test 
	public void testReadCategories() {
		ByteArrayInputStream bais = new ByteArrayInputStream(POICAT);
		PoiInputStream is = new PoiInputStream(bais, Mode.DAT);
		Categories categories = null;
		Poi13 poi = null;
		try {
			categories = is.readPoi();
			poi = is.readPoi();
			log.info(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(categories);
		assertEquals(categories.count(), 1);
		assertNotNull(poi);
		assertEquals(poi.getType(), Poi13.TYPE_1D);
		assertEquals(poi.size(), NAME.length() * 2 + 3 + 3);
		assertEquals(poi.getLon(), LON1);
		assertEquals(poi.getLat(), LAT1);
		assertEquals(poi.getName(), NAME);
	}

	@Test 
	public void testWriteCategories() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		Categories categories = new Categories(Categories.TYPE_CATEGORIES, null);
		Category category = new Category(Category.TYPE_CATEGORY, categories);
		category.setCategoryId(0x1C8E);
		categories.add(category);
		Poi13 poi = new Poi13(Poi13.TYPE_1D, category);
		poi.setSize(Poi13.HEADER);
		poi.setLon(LON1);
		poi.setLat(LAT1);
		poi.setCode(6);
		poi.setName(NAME);
		try {
			is.writePoi(categories);
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		//System.out.println(PoiCommon.hex(result));
		assertArrayEquals(POICAT, result);
	}
}
