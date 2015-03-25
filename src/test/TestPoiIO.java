package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;

import poi.tomtom.Categories;
import poi.tomtom.Category;
import poi.tomtom.Dictionary;
import poi.tomtom.ExplDictionary;
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
import poi.tomtom.PoiContext;
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

	private static final String POI01  = "01 15 00 00 00 87 d6 12 00 ce ca 23 00 35 a7 56 00 94 97 67 00";
	private static final String POI02  = "02 1e 00 00 00 87 d6 12 00 ce ca 23 00 3e 3e 73 21 74 21 61 21 74 21 69 21 6f 21 6e 21 00";
	private static final String POI04  = "01 1c 00 00 00 87 d6 12 00 ce ca 23 00 35 a7 56 00 94 97 67 00 04 15 bf 34 52 c5 bf";
	private static final String POI05  = "01 1e 00 00 00 87 d6 12 00 ce ca 23 00 35 a7 56 00 94 97 67 00 05 15 bf 34 52 c5 bf 05 00";
	private static final String POI06  = "01 1f 00 00 00 87 d6 12 00 ce ca 23 00 35 a7 56 00 94 97 67 00 06 15 bf 34 52 c5 bf 05 00 00";
	private static final String POI07  = "01 2e 00 00 00 87 d6 12 00 ce ca 23 00 35 a7 56 00 94 97 67 00 07 11 15 bf 34 52 c5 bf 3e 3e 73 21 74 21 61 21 74 21 69 21 6f 21 6e 21 00";
	private static final String POI08  = "01 22 00 00 00 87 d6 12 00 ce ca 23 00 35 a7 56 00 94 97 67 00 08 05 15 bf 34 52 c5 bf 29 64 2C E8 24";
	private static final String POI09  = "01 22 00 00 00 87 d6 12 00 ce ca 23 00 35 a7 56 00 94 97 67 00 09 05 15 bf 34 52 c5 bf 68 78 3c b2 01";
	private static final String POI10  = "01 22 00 00 00 87 d6 12 00 ce ca 23 00 35 a7 56 00 94 97 67 00 0a 05 15 bf 34 52 c5 bf 73 09 3c 5f 0e";
	private static final String POI12  = "01 24 00 00 00 87 d6 12 00 ce ca 23 00 35 a7 56 00 94 97 67 00 0c 07 15 bf 34 52 c5 bf 51 02 89 5c d3 21 03";
	private static final String POI13  = "1d 14 87 d6 12 00 ce ca 23 00 06 00 00 00 3e 3e 73 21 74 21 61 21 74 21 69 21 6f 21 6e 21 00";
	private static final String POICAT = "01 00 00 00 8e 1c 00 00 10 00 00 00 2f 00 00 00 1d 14 87 d6 12 00 ce ca 23 00 06 00 00 00 3e 3e 73 21 74 21 61 21 74 21 69 21 6f 21 6e 21 00";
	private static final String POIS   = "50 4f 49 53 01 00 01 00 00 00 00 00 00 00 00 00 00 00 06 04 00 33 00 03 cc c5 64 01 01 04 07 33 00 00 00 ff ff ff ff 0a 00 00 00 00 02 88 73 7a 98 9e 7e bb 41";
	public byte[] hexToBytes(String hexString) {
		return DatatypeConverter.parseHexBinary(hexString.replaceAll(" ", ""));
	}
	
	@Test 
	public void testRead01() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POI01));
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
		assertArrayEquals(hexToBytes(POI01), result);
	}

	@Test 
	public void testRead02() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POI02));
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
		assertArrayEquals(hexToBytes(POI02), result);
	}

	@Test 
	public void testRead04() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POI04));
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
		assertArrayEquals(hexToBytes(POI04), result);
	}

	@Test 
	public void testRead05() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POI05));
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
		assertArrayEquals(hexToBytes(POI05), result);
	}

	@Test 
	public void testRead06() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POI06));
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
		assertArrayEquals(hexToBytes(POI06), result);
	}

	@Test 
	public void testRead07() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POI07));
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
		assertArrayEquals(hexToBytes(POI07), result);
	}

	@Test 
	public void testRead08() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POI08));
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
		assertArrayEquals(hexToBytes(POI08), result);
	}

	@Test 
	public void testRead09() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POI09));
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
		Poi09 poi = new Poi09(Poi09.TYPE_09, parent, null);
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
		assertArrayEquals(hexToBytes(POI09), result);
	}

	@Test 
	public void testRead10() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POI10));
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
		assertArrayEquals(hexToBytes(POI10), result);
	}

	@Test 
	public void testRead12() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POI12));
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
		assertArrayEquals(hexToBytes(POI12), result);
	}

	@Test 
	public void testRead13() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POI13));
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
		Poi13 poi = new Poi13(Poi13.TYPE_1D, null, null);
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
		assertArrayEquals(hexToBytes(POI13), result);
	}

	@Test 
	public void testReadDic() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POIS));
		PoiInputStream is = new PoiInputStream(bais, Mode.DAT);
		Dictionary dict = null;
		try {
			is.readPoi();
			dict = is.getContext().getDictionary();
			//log.info(dict);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(dict);
	}

	@Test 
	public void testWriteDic() {
		Collection<String> texts = new ArrayList<String>();
		texts.add(NAME + " 1");
		texts.add(NAME + " 2");
				
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PoiOutputStream is = new PoiOutputStream(baos);
		ExplDictionary dict = new ExplDictionary();
		dict.create(texts);
		try {
			is.writePOIS(dict);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		//System.out.println(PoiCommon.hex(result));
		assertArrayEquals(hexToBytes(POIS), result);
	}

	@Test 
	public void testReadCategories() {
		ByteArrayInputStream bais = new ByteArrayInputStream(hexToBytes(POICAT));
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
		try {
		//ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileOutputStream fos = new FileOutputStream("poi.dat");
		PoiOutputStream is = new PoiOutputStream(fos);//(baos);
		Categories categories = new Categories(Categories.TYPE_CATEGORIES, null);
		Category category = new Category(Category.TYPE_CATEGORY, categories);
		category.setCategoryId(0x1C8E);
		categories.add(category);
		Poi01 area = new Poi01(Poi01.TYPE_01, category);
		area.setSize(Poi01.SIZE);
		area.setLon1(3038260);
		area.setLat1(4073770);
		area.setLon2(2913010);
		area.setLat2(4041332);
		Poi09 poi = new Poi09(Poi09.TYPE_09, area, null);
		poi.setSize(Poi09.SIZE);
		poi.setLon(10979490);
		poi.setLat(12071913);
		poi.setName("abcdefghijklmnopqrstuvwxyz");
		try {
			is.writePoi(categories);
			is.writePoi(area);
			is.writePoi(poi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//byte[] result = baos.toByteArray();
		//System.out.println(PoiCommon.hex(result));
		//assertArrayEquals(POICAT, result);
			is.flush();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
