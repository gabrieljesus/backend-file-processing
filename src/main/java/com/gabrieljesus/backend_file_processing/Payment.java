package com.gabrieljesus.backend_file_processing;

public class Payment extends Record {
	
	@EZJsonField(name = "categoria")
	private String category;
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "{\"data\":\"" + date + "\",\"descricao\":\"" + description + "\",\"categoria\":\"" + category + "\",\"moeda\":\"" + currency + "\",\"valor\":\"" + amount + "\"}";
	}
}
