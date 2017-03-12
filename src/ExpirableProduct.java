/*
 * file: ExpirableProduct.java
 * This is contains the class information of the ExpirableProduct.
 * ExpirableProduct inherits from Product.
 * It is like a product, but with the expiration information.
 * 
 * The date object uses the following Java API:
 * Joda-Time
 * http://www.joda.org/joda-time/ 
 * 
 * Public functions:
 * ExpirableProduct()
 * ExpirableProduct(int newQuantity, LocalDate newExpirationDate)
 * boolean setExpirationDate(LocalDate newExpirationDate)
 * LocalDate getExpirationDate()
 * JPanel display()
 * boolean update()
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.joda.time.LocalDate;

public class ExpirableProduct extends Product
{
	// variable to store the expiration date
	private LocalDate _expirationDate;
	
	// constructor
	public ExpirableProduct()
	{
		super(0);
		this.setExpirationDate(new LocalDate());
	}
	
	// constructor
	public ExpirableProduct(int newQuantity, LocalDate newExpirationDate)
	{
		super(newQuantity);
		this.setExpirationDate(newExpirationDate);
	}
	
	// changes the expiration date of the product
	public boolean setExpirationDate(LocalDate newExpirationDate)
	{
		this._expirationDate = newExpirationDate;
		return true;
	}
	
	// returns the expiration date
	public LocalDate getExpirationDate()
	{
		return this._expirationDate;
	}
	
	// function return the JPanel contains information of the product
	public JPanel display()
	{
		JLabel label1 = new JLabel("Quantity: " + _quantity);
		JLabel label2 = new JLabel("Expitation Date:  "+ _expirationDate);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(label1, BorderLayout.NORTH);
		panel.add(label2, BorderLayout.SOUTH);
		return panel;
	}
	
	// function creates a GUI to update the information of this product
	public boolean update()
	{
		// set up frame
		JFrame frame = new JFrame();
		frame.setTitle("New Product");
		frame.getContentPane().setLayout(new BorderLayout());
		
		// set up section for enter new data
		JTextField newValue = new JTextField();
		JTextField newDate = new JTextField();
		newValue.setText(Integer.toString(this._quantity));
		newDate.setText(_expirationDate.toString());

		JPanel data = new JPanel(new GridLayout(2,2));
		data.add(new JLabel("Quantity:"));
		data.add(newValue, BorderLayout.NORTH);
		data.add(new JLabel("Expiration Date:"));
		data.add(newDate, BorderLayout.SOUTH);
		
		// set up save button
		JButton save = new JButton("Save");
		save.addActionListener(new saveButton(newValue, newDate, frame));
		
		// add data section and save button to frame
		frame.getContentPane().add(data, BorderLayout.NORTH);
		frame.getContentPane().add(save, BorderLayout.SOUTH);
		
		// add padding to frame
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		((JComponent) frame.getContentPane()).setBorder(padding);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		return true;
	}
	
	// private class for the save button
	// When the save is successfully processed, the frame received will be closed automatically
	// CASE F
	private class saveButton implements ActionListener
	{
		private JTextField value;
		private JTextField date;
		private JFrame frame;
		
		// store the fields that have the new values
		public saveButton(JTextField newValue, JTextField newDate, JFrame newFrame)
		{
			value = newValue;
			date = newDate;
			frame = newFrame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			// parse the expiration date into 3 blocks yyyy-mm-dd
			String[] parts = date.getText().split("-");
			if(!setQuantity(Integer.parseInt(value.getText())) || !setExpirationDate(new LocalDate(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),Integer.parseInt(parts[2]))))
			{
				JOptionPane.showMessageDialog(null,
					"Invalid Input",
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(null,
					"New Value Saved",
					"Saved",
					JOptionPane.INFORMATION_MESSAGE);
				frame.setVisible(false);
				frame.dispose();
			}
		}
		
	}

}
