
package Invcmodel;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class Fileop {
    
    private ArrayList<InvcHeade> invoiceHeader;
      
    public ArrayList<InvcHeade> read(){
        
        
        JFileChooser fc = new JFileChooser();

        try {
            JOptionPane.showMessageDialog(null, "Select Invoice Header File",
                    "Invoice Header", JOptionPane.INFORMATION_MESSAGE);
            int result = fc.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                System.out.println("Invoices have been read");
                ArrayList<InvcHeade> invoicesArray = new ArrayList<>();
                for (String headerLine : headerLines) {
                    try {
                        String[] headerParts = headerLine.split(",");
                        int invoiceNum = Integer.parseInt(headerParts[0]);
                        String invoiceDate = headerParts[1];
                        String customerName = headerParts[2];

                        InvcHeade invoice = new InvcHeade(invoiceNum, invoiceDate, customerName);
                        invoicesArray.add(invoice);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                System.out.println("Check point");
                JOptionPane.showMessageDialog(null, "Select Invoice Line File",
                        "Invoice Line", JOptionPane.INFORMATION_MESSAGE);
                result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    System.out.println("Lines have been read");
                    for (String lineLine : lineLines) {
                        try {
                            String lineParts[] = lineLine.split(",");
                            int invoiceNum = Integer.parseInt(lineParts[0]);
                            String itemName = lineParts[1];
                            double itemPrice = Double.parseDouble(lineParts[2]);
                            int count = Integer.parseInt(lineParts[3]);
                            InvcHeade inv = null;
                            for (InvcHeade invoice : invoicesArray) {
                                if (invoice.getIdNumber() == invoiceNum) {
                                    inv = invoice;
                                    break;
                                }
                            }

                            InvcLine line = new InvcLine(itemName, itemPrice, count, inv);
                            inv.getLines().add(line);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                           }
                        }
                    
                    System.out.println("Check point");
                    
                  }
              
                this.invoiceHeader = invoicesArray;  
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cannot read file", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
        return invoiceHeader;
    }
    
    
    
    
    
    public void write(ArrayList<InvcHeade> invoices)
    {
        for(InvcHeade inv : invoices)
      {
          int invId = inv.getIdNumber();
          String date = inv.getInvoiceDate();
          String customer = inv.getCustomerName();
          System.out.println("\n Invice " + invId + "\n {\n " + date + "," + customer);
          ArrayList<InvcLine> lines = inv.getLines();
          for(InvcLine line : lines)
          {
              System.out.println( line.getLineItem() + "," + line.getLinePrice() + "," + line.getLineCount());
          }
          
          System.out.println(" } \n");
      }
        
    }
    
    
    
    
    
     public static void main(String[] args)
   {
       Fileop fo = new Fileop();
       ArrayList<InvcHeade> invoices = fo.read();
       fo.write(invoices);
              
   }
    
     
}
