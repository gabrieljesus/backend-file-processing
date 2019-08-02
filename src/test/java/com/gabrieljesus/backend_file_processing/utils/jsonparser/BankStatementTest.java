package com.gabrieljesus.backend_file_processing.utils.jsonparser;

import java.util.List;
import java.util.Objects;

import com.gabrieljesus.backend_file_processing.EZJsonField;
import com.gabrieljesus.backend_file_processing.Payment;
import com.gabrieljesus.backend_file_processing.Receipt;

public class BankStatementTest {

	@EZJsonField(name = "pagamentos")
	private List<Payment> payment;
	
	@EZJsonField(name = "recebimentos")
	private List<Receipt> receipt;
	
	public List<Payment> getPayment() {
		return payment;
	}
	
	public void setPayment(List<Payment> payment) {
		this.payment = payment;
	}
	
	public List<Receipt> getReceipt() {
		return receipt;
	}
	
	public void setReceipt(List<Receipt> receipt) {
		this.receipt = receipt;
	}
	
	@Override
	public boolean equals(Object o) {
		if(payment.size() != ((BankStatementTest)o).getPayment().size())
			return false;
		
		if(receipt.size() != ((BankStatementTest)o).getReceipt().size())
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(payment, receipt);
	}
}
