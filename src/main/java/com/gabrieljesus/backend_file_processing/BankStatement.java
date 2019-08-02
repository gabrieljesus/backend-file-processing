package com.gabrieljesus.backend_file_processing;

import java.util.List;

public class BankStatement {
	
	@EZJsonField(name = "pagamentos")
	private List<Payment> payments;
	
	@EZJsonField(name = "recebimentos")
	private List<Receipt> receipts;
	
	public List<Payment> getPayments() {
		return payments;
	}
	
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	
	public List<Receipt> getReceipts() {
		return receipts;
	}
	
	public void setReceipts(List<Receipt> receipts) {
		this.receipts = receipts;
	}
	
	public boolean contains(Payment p) {
		for(Payment payment : payments) {
			if(
				payment.getDate().equals(p.getDate()) &&
				payment.getDescription().toLowerCase().equals(p.getDescription().toLowerCase()) &&
				payment.getCurrency().equals(p.getCurrency()) &&
				payment.getCategory().toLowerCase().equals(p.getCategory().toLowerCase()) &&
				payment.getAmount() == p.getAmount()
			)
				return true;
		}
			
		return false;
	}

	public boolean contains(Receipt r) {
		for(Receipt receipt : receipts) {
			if(
				receipt.getDate().equals(r.getDate()) &&
				receipt.getDescription().toLowerCase().equals(r.getDescription().toLowerCase()) &&
				receipt.getCurrency().equals(r.getCurrency()) &&
				receipt.getAmount() == r.getAmount()
			)
				return true;
		}
		
		return false;
	}
}
