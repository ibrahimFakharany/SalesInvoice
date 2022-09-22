package controller;

import Invcmodel.InvcHeade;
import Invcmodel.InvcModel22;
import Invcmodel.InvcLine;
import Invcmodel.InvcTable;

import java.awt.HeadlessException;

import sales.appear.InvMosHead;
import sales.appear.JFrame;
import sales.appear.InvcLine88;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Salesinvc implements ActionListener, ListSelectionListener {

    private final JFrame frame;
    private InvMosHead invoiceDialog;
    private InvcLine88 lineDialog;

    public Salesinvc(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("Action: " + actionCommand);
        switch (actionCommand) {
            case "Load File":
                loadFile();
                break;
            case "Create New Invoice":
                createNewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Create New Item":
                createNewItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
            case "createInvoiceCancel":
                createInvoiceCancel();
                break;
            case "createInvoiceAdd":
                createInvoiceOK();
                break;
            case "createLineAdd":
                createLineOK();
                break;
            case "createLineCancel":
                createLineCancel();
                break;
            case "Save File":
                saveFile();
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = frame.getInvoiceTable().getSelectedRow();
        if (selectedIndex != -1) {
            System.out.println("You have selected row: " + selectedIndex);
            InvcHeade currentInvoice = frame.getInvoices().get(selectedIndex);
            frame.getInvoiceNumLabel().setText("" + currentInvoice.getIdNumber());
            frame.getInvoiceDateLabel1().setText("" + currentInvoice.getInvoiceDate());
            frame.getCustomerNameLabel().setText(currentInvoice.getCustomerName());
            frame.getInvoiceTotalLabel().setText("" + currentInvoice.getInvoiceTotal());
            InvcTable linesTableModel = new InvcTable(currentInvoice.getLines());
            frame.getLineTable().setModel(linesTableModel);
            linesTableModel.fireTableDataChanged();
        }
    }

    private void loadFile() {
        JFileChooser fc = new JFileChooser();

        try {
            JOptionPane.showMessageDialog(frame, "Select Invoice Header File",
                    "Information Message", JOptionPane.INFORMATION_MESSAGE);
            int result = fc.showOpenDialog(frame);
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
                        JOptionPane.showMessageDialog(frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                System.out.println("Check point");
                JOptionPane.showMessageDialog(frame, "Select Invoice Line File",
                        "Information Message", JOptionPane.INFORMATION_MESSAGE);
                result = fc.showOpenDialog(frame);
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
                            JOptionPane.showMessageDialog(frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);

                        }
                    }
                    System.out.println("Check point");
                }
                frame.setInvoices(invoicesArray);
                InvcModel22 invoicesTableModel = new InvcModel22(invoicesArray);
                frame.setInvoicesTableModel(invoicesTableModel);
                frame.getInvoiceTable().setModel(invoicesTableModel);
                frame.getInvoicesTableModel().fireTableDataChanged();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Cannot read file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFile() {
        ArrayList<InvcHeade> invoices = frame.getInvoices();
        String headers = "";
        String lines = "";
        for (InvcHeade invoice : invoices) {
            String invCSV = invoice.getAsCSV();
            headers += invCSV;
            headers += "\n";

            for (InvcLine line : invoice.getLines()) {
                String lineCSV = line.getAsCSV();
                lines += lineCSV;
                lines += "\n";
            }
        }
        System.out.println("Check point");

        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                try (FileWriter hfw = new FileWriter(headerFile)) {
                    hfw.write(headers);
                    hfw.flush();
                }
                result = fc.showSaveDialog(frame);
                JOptionPane.showMessageDialog(frame, "File saved successfully",
                        "Information Message", JOptionPane.INFORMATION_MESSAGE);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    try (FileWriter lfw = new FileWriter(lineFile)) {
                        lfw.write(lines);
                        lfw.flush();
                    }
                }
            }
        } catch (HeadlessException | IOException ex) {


        }
    }

    private void createNewInvoice() {
        invoiceDialog = new InvMosHead(frame);
        invoiceDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedRow = frame.getInvoiceTable().getSelectedRow();
        if (selectedRow != -1) {
            frame.getInvoices().remove(selectedRow);
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createNewItem() {
        lineDialog = new InvcLine88(frame);
        lineDialog.setVisible(true);
    }

    private void deleteItem() {
        int selectedRow = frame.getLineTable().getSelectedRow();

        if (selectedRow != -1) {
            InvcTable linesTableModel = (InvcTable) frame.getLineTable().getModel();
            linesTableModel.getLines().remove(selectedRow);
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createInvoiceCancel() {
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
    }

    private void createInvoiceOK() {
        String date = invoiceDialog.getInvDateField().getText();
        String customer = invoiceDialog.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();
        try {
            String[] dateParts = date.split("-");  // 
            if (dateParts.length < 3) {
                JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                if (day > 31 || month > 12) {
                    JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    InvcHeade invoice = new InvcHeade(num, date, customer);
                    frame.getInvoices().add(invoice);
                    frame.getInvoicesTableModel().fireTableDataChanged();
                    invoiceDialog.setVisible(false);
                    invoiceDialog.dispose();
                    invoiceDialog = null;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void createLineOK() {
        String item = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = frame.getInvoiceTable().getSelectedRow();
        if (selectedInvoice != -1) {
            InvcHeade invoice = frame.getInvoices().get(selectedInvoice);
            InvcLine line = new InvcLine(item, price, count, invoice);
            invoice.getLines().add(line);
            InvcTable linesTableModel = (InvcTable) frame.getLineTable().getModel();
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void createLineCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

}
