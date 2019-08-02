package com.gabrieljesus.backend_file_processing.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.gabrieljesus.backend_file_processing.BankStatement;
import com.gabrieljesus.backend_file_processing.Payment;
import com.gabrieljesus.backend_file_processing.Receipt;

public class Utils {

	public static String get(String url) throws MalformedURLException, IOException {
		URL externalSystemDb = new URL(url);        	
    	HttpURLConnection connection = (HttpURLConnection)externalSystemDb.openConnection();
    	connection.setRequestMethod("GET");
    	connection.setRequestProperty("Accept", "application/json");
    	
    	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null)
		    content.append(inputLine);
		
		in.close();
    	connection.disconnect();
    	
    	return content.toString();
	}
	
	public static void mergeAndCleanStatements(BankStatement bs1, BankStatement bs2) {
		for(Payment p : bs2.getPayments()) {
        	try {
            	if(!bs1.contains(p))
            		bs1.getPayments().add(p);
        	} catch(NullPointerException npe) {
        		System.out.println("Somenthing wrong happened with this payment record: " + p);
        	}
        }
        
        for(Receipt r : bs2.getReceipts()) {
        	try {
            	if(!bs1.contains(r))
            		bs1.getReceipts().add(r);
        	} catch(NullPointerException npe) {
        		System.out.println("Somenthing wrong happened with this receipt record: " + r);
        	}
        }
        
        List<Payment> payments = bs1.getPayments();
        
        for(int x = 0; x < payments.size(); x++) {
        	Payment p = payments.get(x);
        	
        	if(p.getCategory() == null)
        		p.setCategory("");
        	
        	if(p.getCurrency() == null)
        		bs1.getPayments().remove(x);
        }
	}
}
