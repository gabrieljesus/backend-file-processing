package com.gabrieljesus.backend_file_processing;

public abstract class Record {

	@EZJsonField(name = "data")
	protected String date;
	
	@EZJsonField(name = "descricao")
	protected String description;
	
	@EZJsonField(name = "moeda")
	protected String currency;
	
	// This is odd, but it is what we get from the service
	@EZJsonField(name = "valor")
	protected String amount;
	
	public String getDate() {
		return date;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public int getAmount() {
		return convertAmount(amount);
	}

	public void setDate(String date) {
		String tmp[] = date.toLowerCase()
				.replace("jan", "01")
				.replace("feb", "02")
				.replace("fev", "02")
				.replace("mar", "03")
				.replace("apr", "04")
				.replace("abr", "04")
				.replace("may", "05")
				.replace("mai", "05")
				.replace("jun", "06")
				.replace("jul", "07")
				.replace("aug", "08")
				.replace("ago", "08")
				.replace("sep", "09")
				.replace("set", "09")
				.replace("oct", "10")
				.replace("out", "10")
				.replace("nov", "11")
				.replace("dec", "12")
				.replace("dez", "12")
				.replace(" ", "")
				.replace("-", "/")
				.split("/");
		
		this.date = tmp[1] + "/" + tmp[0];
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setAmount(String amount) {
		this.amount = amount.replace(" ", "");
	}
	
	// Convert the value to an int value representing cents
	public int convertAmount(String amount) throws NumberFormatException {
		// Take the . from the value
		amount = amount.replace(".", "");
		
		// The result variable
		int result = 0;
		
		// Split to check the decimal part
		String value[] = amount.split(",");
		
		if(value.length == 1)
			result = Integer.parseInt(value[0] + "00");
		else if(value.length == 2) {
			if(value[1].length() == 1)
				result = Integer.parseInt(value[0] + value[1] + "0");
			else if(value[1].length() == 2)
				result = Integer.parseInt(value[0] + value[1]);
			else {
				String twoDecimals = value[1].substring(0, 2);
				String remainingDecimals = value[1].substring(2).replaceAll("0", "");
				if(remainingDecimals.isEmpty())
					result = Integer.parseInt(value[0] + twoDecimals);
				else
					throw new NumberFormatException();
			}
		} else
			throw new NumberFormatException();
		
		return result;
	}
	
}
