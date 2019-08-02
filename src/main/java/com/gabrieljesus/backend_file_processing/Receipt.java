package com.gabrieljesus.backend_file_processing;

public class Receipt extends Record {
	
	@Override
	public String toString() {
		return "{\"data\":\"" + date + "\",\"descricao\":\"" + description + "\",\"moeda\":\"" + currency + "\",\"valor\":\"" + amount + "\"}";
	}

}
