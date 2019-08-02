package com.gabrieljesus.backend_file_processing.utils.jsonparser;

import java.util.Objects;

import com.gabrieljesus.backend_file_processing.EZJsonField;

public class SimpleNestedObject {

	@EZJsonField(name = "field1")
	private Sub1 field1;
	
	private int field2;
	
	@EZJsonField(name = "field3")
	private double field3;
	
	public Sub1 getField1() {
		return field1;
	}
	
	public void setField1(Sub1 field1) {
		this.field1 = field1;
	}
	
	public int getField2() {
		return field2;
	}
	
	public void setField2(int field2) {
		this.field2 = field2;
	}
	
	public double getField3() {
		return field3;
	}
	
	public void setField3(double field3) {
		this.field3 = field3;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		
		if(getClass() != o.getClass())
			return false;
		
		if(field1.getSub1() != ((SimpleNestedObject)o).field1.getSub1())
			return false;
		
		if(field2 != (((SimpleNestedObject)o).field2))
			return false;
		
		if(field3 != (((SimpleNestedObject)o).field3))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(field1, field2, field3);
	}
}
