package com.gabrieljesus.backend_file_processing.utils.jsonparser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import com.gabrieljesus.backend_file_processing.EZJsonField;

public class JsonParser {
	
	// 44 = ,, 58 = :, 91 = [, 93 = ], 123 = { and 124 = }
	private static char jsonTokens[] = { 44, 58, 91, 93, 123, 125 };
	// 45 = -, 46 = ., 48 ~ 57 = 0 ~ 9, 101 = e
	private static char numbers[] = { 45, 46, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 101 };
	
	public static <T> T fromString(String jsonString, Class<T> classname) throws Exception {
		T obj;
		
		List<Object> tokens = lexer(jsonString);
		
		String firstToken = (String)tokens.remove(0);
		
		if(firstToken.equals("{"))
			obj = parseObject(tokens, classname);
		else
			throw new Exception("This is not a valid json");
		
		return obj;
	}
	
	private static <T> List<T> parseArray(List<Object> tokens, Class<T> classname) throws Exception {
		List<T> obj = new ArrayList<>();
		
		while(!tokens.isEmpty()) {
			Object token = tokens.remove(0);
			if(token instanceof String) {
				String tokenString = (String)token;
				
				if(tokenString.equals("{"))
					obj.add(parseObject(tokens, classname));
				
				if(tokenString.equals("]"))
					return obj;
				
				if(tokenString.equals("["))
					return parseArray(tokens, classname);
			}
		}
		
		return null;
	}
	
	private static <T> T parseObject(List<Object> tokens, Class<T> classname) throws Exception {
		T obj = classname.getConstructor().newInstance();
		
		while(!tokens.isEmpty()) {
			Object token = tokens.remove(0);
			if(token instanceof String) {
				String tokenString = (String)token;
				
				if(tokenString.equals("{"))
					return parseObject(tokens, classname);
				
				if(tokenString.equals("}"))
					return obj;
				
				Field field = null;
				
				Field[] fields = classname.getDeclaredFields();
				for(Field innerField : fields) {
					if((innerField.isAnnotationPresent(EZJsonField.class) && innerField.getDeclaredAnnotation(EZJsonField.class).name().equals(tokenString)) || innerField.getName().toLowerCase().equals(tokenString)) {
						field = innerField;
						tokenString = innerField.getName();
						break;
					}
				}
				
				if(field == null) {
					fields = classname.getSuperclass().getDeclaredFields();
					for(Field innerField : fields) {
						if((innerField.isAnnotationPresent(EZJsonField.class) && innerField.getDeclaredAnnotation(EZJsonField.class).name().equals(tokenString)) || innerField.getName().toLowerCase().equals(tokenString)) {
							field = innerField;
							tokenString = innerField.getName();
							break;
						}
					}
				}
				
				Method[] methods = classname.getMethods();
				
				for(Method m : methods) {
					if(m.getName().toLowerCase().equals("set" + tokenString)) {
						// Removes :
						tokens.remove(0);
						if(tokens.get(0) instanceof String) {
							if(((String)tokens.get(0)).equals("{")) {
								tokens.remove(0);
								m.invoke(obj, parseObject(tokens, field.getType()));
							} else if(((String)tokens.get(0)).equals("[")) {
								tokens.remove(0);
								m.invoke(obj, parseArray(tokens, (Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]));
							} else
								m.invoke(obj, tokens.remove(0));
						} else
							m.invoke(obj, tokens.remove(0));
						
						// Removes ,
						if(((String)tokens.get(0)).equals(","))
							tokens.remove(0);
						
						break;
					}
				}
				
			}
		}
		
		return obj;
	}
	
	private static List<Object> lexer(String jsonString) throws Exception {
		List<Object> tokens = new ArrayList<>();
		
		// A trick to make a string mutable
		StringBuilder sb = new StringBuilder(jsonString);
		
		while(sb.length() > 0) {
			// String explosion!
			String string = stringLexer(sb);
			if(string != null) {
				tokens.add(string);
				continue;
			}
			
			// Number explosion!
			String number = numberLexer(sb);
			if(number != null) {
				if(number.contains("."))
					tokens.add(Double.parseDouble(number));
				else
					tokens.add(Integer.parseInt(number));
				
				continue;
			}
			
			// Boolean explosion!
			Boolean bool = booleanLexer(sb);
			if(bool != null) {
				tokens.add(bool.booleanValue());
				continue;
			}
			
			// Null explosion!
			Boolean isNull = nullLexer(sb);
			if(isNull != null) {
				tokens.add(null);
				continue;
			}
			
			// Misc characters
			if(sb.charAt(0) == ' ')
				sb.deleteCharAt(0);
			else if(isValidToken(jsonTokens, sb.charAt(0))) {
				tokens.add(Character.toString(sb.charAt(0)));
				sb.deleteCharAt(0);
			} else {
				throw new Exception("Invalid token: " + sb.charAt(0));
			}
		}
		
		return tokens;
	}
	
	private static String stringLexer(StringBuilder token) throws Exception {
		if(token.charAt(0) == '"')
			token.deleteCharAt(0);
		else
			return null;
		
		StringBuffer sb = new StringBuffer();
		
		for(int x = 0; x < token.length(); x++) {
			if(token.charAt(x) == '"') {
				token.delete(0, ++x);
				return sb.toString();
			}
			
			sb.append(token.charAt(x));
		}
		
		throw new Exception("Missing the ending quote");
	}
	
	private static String numberLexer(StringBuilder token) throws NumberFormatException {
		if(isValidToken(numbers, token.charAt(0)) || token.charAt(0) == 46) {
			StringBuffer sb = new StringBuffer(Character.toString(token.charAt(0)));
			token.deleteCharAt(0);
			
			while(isValidToken(numbers, token.charAt(0)) || token.charAt(0) == 46) {
				sb.append(token.charAt(0));
				token.deleteCharAt(0);
			}
			
			return sb.toString();
		}
		
		return null;
	}
	
	private static Boolean booleanLexer(StringBuilder token) throws Exception {
		if(token.charAt(0) == 't' || token.charAt(0) == 'T')
			if(token.substring(0, 4).toLowerCase().equals("true")) {
				token.delete(0, 4);
				return new Boolean("true");
			}
		
		if(token.charAt(0) == 'f' || token.charAt(0) == 'F')
			if(token.substring(0, 5).toLowerCase().equals("false")) {
				token.delete(0, 5);
				return new Boolean("false");
			}
		
		return null;
	}
	
	private static Boolean nullLexer(StringBuilder token) {
		if(token.charAt(0) == 'n' || token.charAt(0) == 'N') {
			token.delete(0, 5);
			return new Boolean("true");
		}
		
		return null;
	}
	
	// Binary search shines here! (primitive data to better performance)
	private static boolean isValidToken(char chars[], char token) {
		
		int left = 0;
		int right = chars.length - 1;
		
		while(left <= right) {
			int mid = (left + right) / 2;
			
			if(chars[mid] == token)
				return true;
			
			if(chars[mid] < token)
				left = mid + 1;
			else
				right = mid - 1;
		}
		
		return false;
	}

}
