
package sales.appear;


import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class InvMosHead extends JDialog {
    private final JTextField custNameField;
    private final JTextField invDateField;
    private final JLabel custNameLbl;
    private final JLabel invDateLbl;
    private final JButton addBton;
    private final JButton cancelBton;

    public InvMosHead(JFrame ssframe) {
        custNameLbl = new JLabel("Customer Name:");
        custNameField = new JTextField(20);
        invDateLbl = new JLabel("Invoice Date:");
        invDateField = new JTextField(20);
        addBton = new JButton("Add");
        cancelBton = new JButton("Cancel");
        
        addBton.setActionCommand("createInvoiceAdd");
        cancelBton.setActionCommand("createInvoiceCancel");
        
        addBton.addActionListener(ssframe.getController());
        cancelBton.addActionListener(ssframe.getController());
        setLayout(new GridLayout(3, 2));
        
        add(invDateLbl);
        add(invDateField);
        add(custNameLbl);
        add(custNameField);
        add(addBton);
        add(cancelBton);
        
        pack();
        
    }

    public JTextField getCustNameField() {
        return custNameField;
    }

    public JTextField getInvDateField() {
        return invDateField;
    }   
}