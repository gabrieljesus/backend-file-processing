package com.gabrieljesus.backend_file_processing;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class RegistryTest {
	
	private static Record reg;
	
	@BeforeClass
	public static void setup() {
		reg = new Record() {};
	}

	@Test
	public void convertSingleDecimalValueWithComa() {
		int result = reg.convertAmount("268,5");
		assertEquals(26850, result);
	}
	
	@Test
	public void convertDecimalValueWithComa() {
		int result = reg.convertAmount("45,00");
		assertEquals(4500, result);
	}
	
	@Test
	public void convertIntegerValue() {
		int result = reg.convertAmount("30");
		assertEquals(3000, result);
	}
	
	@Test
	public void convertSingleDecimalValueWithComaAndPoint() {
		int result = reg.convertAmount("1.268,5");
		assertEquals(126850, result);
	}
	
	@Test
	public void convertDecimalValueWithComaAndPoint() {
		int result = reg.convertAmount("1.545,00");
		assertEquals(154500, result);
	}
	
	@Test
	public void convert3DecimalValueWithComa() {
		int result = reg.convertAmount("45,050");
		assertEquals(4505, result);
	}
	
	@Test
	public void convertIntegerValueWithPoint() {
		int result = reg.convertAmount("45.051");
		assertEquals(4505100, result);
	}
	
	@Test(expected = NumberFormatException.class)
	public void convertDecimalInvalidValueWithComas() {
		reg.convertAmount("45,05,1");
	}
	
	@Test(expected = NumberFormatException.class)
	public void convertDecimalInvalidValueWithPointAndComas() {
		reg.convertAmount("45,051");
	}
	
	@Test
	public void convert3DecimalValueWithComaAndManyZeroes() {
		int result = reg.convertAmount("45,0500000");
		assertEquals(4505, result);
	}
}
