package Invcmodel;

import java.util.ArrayList;

public class InvcHeade {
    private int idNumber;
    private String invoiceDate;
    private String invoiceCustomerName;
    private ArrayList<InvcLine> lines;
    
    public InvcHeade() {
    }

    public InvcHeade(int num, String date, String customer) {
        this.idNumber = num;
        this.invoiceDate = date;
        this.invoiceCustomerName = customer;
    }

    public double getInvoiceTotal() {
        double total = 0.0;
        for (InvcLine line : getLines()) {
            total += line.getLineTotal();
        }
        return total;
    }
    
    public ArrayList<InvcLine> getLines() {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines;
    }

    public String getCustomerName() {
        return invoiceCustomerName;
    }

    public void setCustomerName(String customer) {
        this.invoiceCustomerName = customer;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int num) {
        this.idNumber = num;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String date) {
        this.invoiceDate = date;
    }

    @Override
    public String toString() {
        return "Invoice{" + "num=" + idNumber + ", date=" + invoiceDate + ", customer=" + invoiceCustomerName + '}';
    }
        
        public String getAsCSV() {
        return idNumber + "," + invoiceDate + "," + invoiceCustomerName;
    }
    }
    
   