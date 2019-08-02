package com.gabrieljesus.backend_file_processing.utils.jsonparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gabrieljesus.backend_file_processing.BankStatement;
import com.gabrieljesus.backend_file_processing.Payment;
import com.gabrieljesus.backend_file_processing.Receipt;

public class JsonParserTest {
	
	private static Method lexer;
//	private static Method parseArray;
	private static Method parseObject;
	
	@BeforeClass
	public static void setup() throws Exception {
		lexer = JsonParser.class.getDeclaredMethod("lexer", new Class[] { String.class });
		lexer.setAccessible(true);
		
		parseObject = JsonParser.class.getDeclaredMethod("parseObject", new Class[] { List.class, Class.class });
		parseObject.setAccessible(true);
	}
	
	@Test
	public void completeJson() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("{");
		expected.add("foo");
		expected.add(":");
		expected.add("[");
		expected.add(1);
		expected.add(",");
		expected.add(-2);
		expected.add(",");
		expected.add("{");
		expected.add("bar");
		expected.add(":");
		expected.add(2.1);
		expected.add("}");
		expected.add("]");
		expected.add("}");
		List<?> result = (List<?>)lexer.invoke(null, "{\"foo\": [1, -2, {\"bar\": 2.1}]}");
		assertEquals(expected, result);
	}
	
	@Test
	public void sampleValues() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("{");
		expected.add("pagamentos");
		expected.add(":");
		expected.add("[");
		expected.add("{");
		expected.add("data");
		expected.add(":");
		expected.add("11/jul");
		expected.add(",");
		expected.add("descricao");
		expected.add(":");
		expected.add("Auto Posto Shell");
		expected.add(",");
		expected.add("moeda");
		expected.add(":");
		expected.add("R$");
		expected.add(",");
		expected.add("valor");
		expected.add(":");
		expected.add(-50.00);
		expected.add(",");
		expected.add("categoria");
		expected.add(":");
		expected.add("transporte");
		expected.add("}");
		expected.add("]");
		expected.add("}");
		List<?> result = (List<?>)lexer.invoke(null, "{\"pagamentos\": [{\"data\": \"11/jul\",\"descricao\": \"Auto Posto Shell\",\"moeda\": \"R$\",\"valor\": -50.00,\"categoria\": \"transporte\"}]}");
		assertEquals(expected, result);
	}
	
	@Test
	public void stringObject() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("{");
		expected.add("a");
		expected.add(":");
		expected.add("abcd");
		expected.add("}");
		List<?> result = (List<?>)lexer.invoke(null, "{\"a\": \"abcd\" }");
		assertEquals(expected, result);
	}
	
	@Test
	public void stringArray() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("[");
		expected.add("abcd");
		expected.add("]");
		List<?> result = (List<?>)lexer.invoke(null, "[ \"abcd\" ]");
		assertEquals(expected, result);
	}
	
	@Test
	public void numberIntegerObject() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("{");
		expected.add("a");
		expected.add(":");
		expected.add(2);
		expected.add("}");
		List<?> result = (List<?>)lexer.invoke(null, "{\"a\": 2 }");
		assertEquals(expected, result);
	}
	
	@Test
	public void numberIntegerArray() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("[");
		expected.add(-3);
		expected.add("]");
		List<?> result = (List<?>)lexer.invoke(null, "[ -3 ]");
		assertEquals(expected, result);
	}
	
	@Test
	public void numberDoubleObject() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("{");
		expected.add("a");
		expected.add(":");
		expected.add(2.0);
		expected.add("}");
		List<?> result = (List<?>)lexer.invoke(null, "{\"a\": 2.0 }");
		assertEquals(expected, result);
	}
	
	@Test
	public void numberDoubleArray() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("[");
		expected.add(-3.3);
		expected.add("]");
		List<?> result = (List<?>)lexer.invoke(null, "[ -3.3 ]");
		assertEquals(expected, result);
	}
	
	@Test
	public void booleanObject() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("{");
		expected.add("a");
		expected.add(":");
		expected.add(true);
		expected.add("}");
		List<?> result = (List<?>)lexer.invoke(null, "{\"a\": true }");
		assertEquals(expected, result);
	}
	
	@Test
	public void booleanArray() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("[");
		expected.add(true);
		expected.add("]");
		List<?> result = (List<?>)lexer.invoke(null, "[ true ]");
		assertEquals(expected, result);
	}
	
	@Test
	public void nullObject() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("{");
		expected.add("a");
		expected.add(":");
		expected.add(null);
		expected.add("}");
		List<?> result = (List<?>)lexer.invoke(null, "{\"a\": null }");
		assertEquals(expected, result);
	}
	
	@Test
	public void nullArray() throws Exception {
		List<Object> expected = new ArrayList<>();
		expected.add("[");
		expected.add(null);
		expected.add("]");
		List<?> result = (List<?>)lexer.invoke(null, "[ null ]");
		assertEquals(expected, result);
	}
	
	@Test
	public void parseSimpleObject() throws Exception {
		SimpleObject expected = new SimpleObject();
		expected.setField1("Test1");
		expected.setField2(3);
		expected.setField3(2.3);
		
		List<Object> tokens = new ArrayList<>();
		
		tokens.add("field1");
		tokens.add(":");
		tokens.add("Test1");
		
		tokens.add(",");
		
		tokens.add("field2");
		tokens.add(":");
		tokens.add(3);
		
		tokens.add(",");
		
		tokens.add("field3");
		tokens.add(":");
		tokens.add(2.3);
		
		tokens.add("}");
		
		SimpleObject result = (SimpleObject)parseObject.invoke(null, tokens, SimpleObject.class);
		assertTrue(expected.equals(result));
	}
	
	@Test
	public void parseSimpleNestedObject() throws Exception {
		SimpleNestedObject expected = new SimpleNestedObject();
		Sub1 sub1 = new Sub1();
		sub1.setSub1(33);
		expected.setField1(sub1);
		expected.setField2(3);
		expected.setField3(2.3);
		
		List<Object> tokens = new ArrayList<>();
		
		tokens.add("field1");
		tokens.add(":");
		tokens.add("{");
		tokens.add("sub1");
		tokens.add(":");
		tokens.add(33);
		tokens.add("}");
		
		tokens.add(",");
		
		tokens.add("field2");
		tokens.add(":");
		tokens.add(3);
		
		tokens.add(",");
		
		tokens.add("field3");
		tokens.add(":");
		tokens.add(2.3);
		
		tokens.add("}");
		
		SimpleNestedObject result = (SimpleNestedObject)parseObject.invoke(null, tokens, SimpleNestedObject.class);
		assertTrue(expected.equals(result));
	}
	
	@Test
	public void convertSimpleObject() throws Exception {
		SimpleObject expected = new SimpleObject();
		expected.setField1("Test1");
		expected.setField2(3);
		expected.setField3(2.3);
		
		String json = "{\"field1\":\"Test1\",\"field2\":3,\"field3\":2.3}";
		
		SimpleObject result = JsonParser.fromString(json, SimpleObject.class);
		assertTrue(expected.equals(result));
	}
	
	@Test
	public void convertSimpleNestedObject() throws Exception {
		SimpleNestedObject expected = new SimpleNestedObject();
		Sub1 sub1 = new Sub1();
		sub1.setSub1(55);
		expected.setField1(sub1);
		expected.setField2(3);
		expected.setField3(2.3);
		
		String json = "{\"field1\":{\"sub1\":55},\"field2\":3,\"field3\":2.3}";
		
		SimpleNestedObject result = JsonParser.fromString(json, SimpleNestedObject.class);
		assertTrue(expected.equals(result));
	}
	
	
	
	
	
	
	
	
	
	
	
	
//	@Test
//	public void parseSimpleArray() throws Exception {
//		SimpleObject expected = new SimpleObject();
//		expected.setField1("Test1");
//		expected.setField2(3);
//		expected.setField3(2.3);
//		
//		List<Object> tokens = new ArrayList<>();
//		
//		tokens.add("field1");
//		tokens.add(":");
//		tokens.add("Test1");
//		
//		tokens.add(",");
//		
//		tokens.add("field2");
//		tokens.add(":");
//		tokens.add(3);
//		
//		tokens.add(",");
//		
//		tokens.add("field3");
//		tokens.add(":");
//		tokens.add(2.3);
//		
//		tokens.add("}");
//		
//		SimpleObject result = (SimpleObject)parseObject.invoke(null, tokens, SimpleObject.class);
//		assertTrue(expected.equals(result));
//	}
//	
//	@Test
//	public void parseSimpleNestedArray() throws Exception {
//		SimpleNestedObject expected = new SimpleNestedObject();
//		Sub1 sub1 = new Sub1();
//		sub1.setSub1(33);
//		expected.setField1(sub1);
//		expected.setField2(3);
//		expected.setField3(2.3);
//		
//		List<Object> tokens = new ArrayList<>();
//		
//		tokens.add("field1");
//		tokens.add(":");
//		tokens.add("{");
//		tokens.add("sub1");
//		tokens.add(":");
//		tokens.add(33);
//		tokens.add("}");
//		
//		tokens.add(",");
//		
//		tokens.add("field2");
//		tokens.add(":");
//		tokens.add(3);
//		
//		tokens.add(",");
//		
//		tokens.add("field3");
//		tokens.add(":");
//		tokens.add(2.3);
//		
//		tokens.add("}");
//		
//		SimpleNestedObject result = (SimpleNestedObject)parseObject.invoke(null, tokens, SimpleNestedObject.class);
//		assertTrue(expected.equals(result));
//	}
//	
//	@Test
//	public void convertSimpleArray() throws Exception {
//		List<String> data = new ArrayList<>();
//		data.add("field1");
//		data.add("Test1");
//		data.add("field2");
//		data.add("3");
//		data.add("field3");
//		data.add("2.3");
//		
//		SimpleArray expected = new SimpleArray();
//		expected.setData(data);
//		
//		String json = "[\"field1\",\"Test1\",\"field2\",\"3\",\"field3\",\"2.3\"]";
//		
//		SimpleArray result = JsonParser.fromString(json, SimpleArray.class);
//		assertTrue(expected.equals(result));
//	}
//	
//	@Test
//	public void convertSimpleNestedArray() throws Exception {
//		SimpleNestedObject expected = new SimpleNestedObject();
//		Sub1 sub1 = new Sub1();
//		sub1.setSub1(55);
//		expected.setField1(sub1);
//		expected.setField2(3);
//		expected.setField3(2.3);
//		
//		String json = "{\"field1\":{\"sub1\":55},\"field2\":3,\"field3\":2.3}";
//		
//		SimpleNestedObject result = JsonParser.fromString(json, SimpleNestedObject.class);
//		assertTrue(expected.equals(result));
//	}
	
	
	
	
	
	
	@Test
	public void convertSampleJson1() throws Exception {
		Payment payment1 = new Payment();
		payment1.setDate("11/jul");
		payment1.setDescription("Auto Posto Shell");
		payment1.setCurrency("R$");
		payment1.setAmount("-50,00");
		payment1.setCategory("transporte");
		
		Payment payment2 = new Payment();
		payment1.setDate("24/jun");
		payment1.setDescription("Ofner");
		payment1.setCurrency("R$");
		payment1.setAmount("-23,80");
		payment1.setCategory("transporte");
		
		Payment payment3 = new Payment();
		payment1.setDate("25/jun");
		payment1.setDescription("Urbe Cafe");
		payment1.setCurrency("R$");
		payment1.setAmount("-45,10");
		payment1.setCategory("alimentação");
		
		List<Payment> payments = new ArrayList<>();
		payments.add(payment1);
		payments.add(payment2);
		payments.add(payment3);
		
		Receipt receipt1 = new Receipt();
		receipt1.setDate("10 / jul");
		receipt1.setDescription("Marcelo B.");
		receipt1.setCurrency("R$");
		receipt1.setAmount("50,00");
		
		Receipt receipt2 = new Receipt();
		receipt2.setDate("04 / jul");
		receipt2.setDescription("Antonio C.");
		receipt2.setCurrency("R$");
		receipt2.setAmount("15,00");
		
		Receipt receipt3 = new Receipt();
		receipt3.setDate("02 / jul");
		receipt3.setDescription("Luciano N.");
		receipt3.setCurrency("R$");
		receipt3.setAmount("68,00");
		
		List<Receipt> receipts = new ArrayList<>();
		receipts.add(receipt1);
		receipts.add(receipt2);
		receipts.add(receipt3);
		
		BankStatementTest expected = new BankStatementTest();
		expected.setPayment(payments);
		expected.setReceipt(receipts);
		
		BankStatementTest result = JsonParser.fromString("{\"pagamentos\":[{\"data\": \"11/jul\",\"descricao\": \"Auto Posto Shell\",\"moeda\": \"R$\",\"valor\": \"-50,00\",\"categoria\": \"transporte\"},{\"data\": \"24/jun\",\"descricao\": \"Ofner\",\"moeda\": \"R$\",\"valor\": \"-23,80\",\"categoria\": \"transporte\"},{\"data\": \"25/jun\",\"descricao\": \"Urbe Cafe\",\"moeda\": \"R$\",\"valor\": \"-45,10\",\"categoria\": \"alimentação\"}],\"recebimentos\":[{\"data\": \"10 / jul\",\"descricao\": \"Marcelo B.\",\"moeda\": \"R$\",\"valor\": \"50,00\"},{\"data\": \"04 / jul\",\"descricao\": \"Antonio C.\",\"moeda\": \"R$\",\"valor\": \"15,00\"},{\"data\": \"02 / jul\",\"descricao\": \"Luciano N.\",\"moeda\": \"R$\",\"valor\": \"68,00\"}]}", BankStatementTest.class);
		assertEquals(expected, result);
	}
}
