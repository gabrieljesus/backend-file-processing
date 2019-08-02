package com.gabrieljesus.backend_file_processing.utils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.gabrieljesus.backend_file_processing.BankStatement;
import com.gabrieljesus.backend_file_processing.Payment;
import com.gabrieljesus.backend_file_processing.Receipt;
import com.gabrieljesus.backend_file_processing.Record;

public class Report {

	public static void generateReport(BankStatement bankstatement, PrintStream ps) throws IOException {
		Locale locale = Locale.getDefault();
		List<Object> records = new ArrayList<>();
		records.addAll(bankstatement.getPayments());
		records.addAll(bankstatement.getReceipts());
		
		records.sort(new Comparator<Object>() {
			@Override
			public int compare(Object obj1, Object obj2) {
				return ((Record)obj1).getDate().compareTo(((Record)obj2).getDate());
			}
		});
		
		ps.print("Movimentações\n");
		ps.print("-------------\n");
		
		int total = 0;
		int totalPayments = 0;
		int totalReceipts = 0;
		
		for(Object r : records) {
			Record record = (Record) r;
			String date[] = record.getDate().split("/");
			if(record instanceof Receipt) {
				ps.printf(locale, "%s/%s %-25s %s %10.2f%n", date[1], date[0], record.getDescription(), record.getCurrency(), (float)record.getAmount()/100);
				totalReceipts += record.getAmount();
			} else {
				ps.printf(locale, "%s/%s %-25s %s %10.2f %s%n", date[1], date[0], record.getDescription(), record.getCurrency(), (float)record.getAmount()/100, ((Payment)record).getCategory());
				totalPayments += record.getAmount();
			}
			
			total += record.getAmount();
		}
		
		ps.print("=============\n");
		ps.printf(locale, "Total de Pagamentos: R$ %10.2f%n", (float)totalPayments/100);
		ps.print("=============\n");
		ps.printf(locale, "Total Recebido:      R$ %10.2f%n", (float)totalReceipts/100);
		ps.print("=============\n");
		ps.printf(locale, "Movimentação Total:  R$ %10.2f%n", (float)total/100);
		ps.print("=============\n");
	}
	
	public static void generateCategoryReport(BankStatement bankstatement, PrintStream ps) throws IOException {
		Locale locale = Locale.getDefault();
		List<Payment> payments = bankstatement.getPayments();
		
		Map<String, Integer> categories = new HashMap<>();
		
		for(Payment payment : payments) {
			String category = normalizeString(payment.getCategory());
			if(category == null)
				category = "Sem categoria";
			
			if(!categories.containsKey(category))
				categories.put(category, payment.getAmount());
			else {
				int amount = categories.get(category) + payment.getAmount();
				categories.put(category, amount);
			}
		}
		
		ps.print("Total por Categoria\n");
		ps.print("-------------\n");
		
		String expensiveCategoryName = null;
		float expensiveCategoryAmount = 0;
		
		for(Map.Entry<String, Integer> entry : categories.entrySet()) {
			float amount = (float)entry.getValue()/100;
			if(amount < expensiveCategoryAmount) {
				expensiveCategoryAmount = amount;
				expensiveCategoryName = entry.getKey();
			}
			
			ps.printf(locale, "%15s: R$ %10.2f%n", entry.getKey(), amount);
		}
		
		ps.print("=============\n");
		
		ps.printf("Categoria com maior gasto: %s%n", expensiveCategoryName);
		
		ps.print("=============\n");
	}
	
	public static void generateMonthReport(BankStatement bankstatement, PrintStream ps) throws IOException {
		List<Payment> payments = bankstatement.getPayments();
		
		Map<String, Integer> months = new HashMap<>();
		
		for(Payment payment : payments) {
			String month = payment.getDate().split("/")[0];
			
			if(!months.containsKey(month))
				months.put(month, payment.getAmount());
			else {
				int amount = months.get(month) + payment.getAmount();
				months.put(month, amount);
			}
		}
		
		String expensiveMonth = null;
		float expensiveCategoryAmount = 0;
		
		for(Map.Entry<String, Integer> entry : months.entrySet()) {
			float amount = (float)entry.getValue()/100;
			if(amount < expensiveCategoryAmount) {
				expensiveCategoryAmount = amount;
				expensiveMonth = entry.getKey();
			}
		}
		
		ps.printf("Mês com maior gasto: %s%n", expensiveMonth);
		ps.print("=============\n");
	}
	
	public static String normalizeString(String string) {
		if(string == null || string.isEmpty())
			return null;
		return string.toLowerCase().replace("ç", "c").replace("ã", "a");
	}
}
