package com.gabrieljesus.backend_file_processing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;

import com.gabrieljesus.backend_file_processing.utils.Report;
import com.gabrieljesus.backend_file_processing.utils.Utils;
import com.gabrieljesus.backend_file_processing.utils.jsonparser.JsonParser;

public class App {
    
	public static void main(String[] args) {
        
        try {
        	String db = Utils.get("https://my-json-server.typicode.com/cairano/backend-test/db");
        	BankStatement bs1 = JsonParser.fromString(db, BankStatement.class);
        	
        	String recebimentos = Utils.get("https://my-json-server.typicode.com/cairano/backend-test/recebimentos");
        	String pagamentos = Utils.get("https://my-json-server.typicode.com/cairano/backend-test/pagamentos");
        	
        	BankStatement bs2 = JsonParser.fromString("{\"pagamentos\":" + pagamentos + ",\"recebimentos\":" + recebimentos + "}", BankStatement.class);
        	
        	InputStream is = App.class.getClassLoader().getResourceAsStream("resources/db.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String s = br.readLine();
            
            // Discard the title line
            s = br.readLine();
            
            while (s != null) {
                String fields[] = s.split("\t");
                
                // Pagamento
                if(fields[2].startsWith("-")) {
                	Payment p = new Payment();
                	p.setCurrency("R$");
                	p.setDate(fields[0]);
                	p.setDescription(fields[1]);
                	p.setAmount(fields[2]);
                	
                	if(fields.length == 4)
                		p.setCategory(fields[3]);
                	
                	if(!bs1.contains(p))
                		bs1.getPayments().add(p);
                } else { // Recebimento
                	Receipt r = new Receipt();
                	r.setCurrency("R$");
                	r.setDate(fields[0]);
                	r.setDescription(fields[1]);
                	r.setAmount(fields[2]);
                	
                	if(!bs1.contains(r))
                		bs1.getReceipts().add(r);
                }
                
                s = br.readLine();
            }

            br.close();
            
            // Clean and merge bs1 and bs2 into bs1
            Utils.mergeAndCleanStatements(bs1, bs2);
            
            PrintStream ps = new PrintStream(System.out);
            
            // exibir o log de movimentações de forma ordenada
            // informar o total de gastos por categoria
            // informar qual categoria cliente gastou mais
            // informar qual foi o mês que cliente mais gastou
            // quanto de dinheiro o cliente gastou
            // quanto de dinheiro o cliente recebeu
            // saldo total de movimentações do cliente
            Report.generateReport(bs1, ps);
            Report.generateCategoryReport(bs1, ps);
            Report.generateMonthReport(bs1, ps);
            
            ps.close();
        } catch(MalformedURLException mue) {
        	mue.printStackTrace(); // new URL
        } catch(IOException ioe) {
        	ioe.printStackTrace(); // openConnection
        } catch(Exception e) {
        	e.printStackTrace(); // json conversion
        }
    }
}
