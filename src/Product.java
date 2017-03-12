/*
 * file: Product.java
 * This file contains the class for the Products and its operations
 * The Product class contains the statistic information of a product.
 * For example, the quantity of the product.
 * This class also contains the GUI for the product class
 * 
 * Public functions:
 * 	Product()
 * 	Product(int newQuantity)
 * 	int getQuantity()
 * 	boolean setQuantity(int newQuantity)
 * 	boolean increaseQuantity(int amount)
 * 	boolean decreaseQuantity(int amount)
 * 	JPanel display()
 * 	boolean update()
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

public class Product
{
	// variable to store the quantity of the product
	protected int _quantity;
	
	
	// constructor
	public Product()
	{
		this(0);
	}
	
	// constructor
	public Product(int newQuantity)
	{
		this._quantity = newQuantity;
	}
	
	// returns the quantity of the product
	public int getQuantity()
	{
		return this._quantity;
	}
	
	// set the quantity of the product
	public boolean setQuantity(int newQuantity)
	{
		if(newQuantity < 0)
		{
			System.out.println("Invalid quantity value");
			return false;
		}
		this._quantity = newQuantity;
		return true;
	}
	
	// increase the quantity of the product
	// quantity = quantity + newQuantity
	public boolean increaseQuantity(int amount)
	{
		if(this._quantity+amount < 0)
		{
			System.out.println("Invalid quantity value");
			return false;
		}
		this._quantity += amount;
		return true;
	}
	
	// decrease the quantity of the product
	// quantity = quantity - newQuantity
	public boolean decreaseQuantity(int amount)
	{
		return increaseQuantity(-amount);
	}
	
	// return the JPanel that contains the information of the product
	public JPanel display()
	{
		JLabel label = new JLabel("Quantity: "+_quantity);
		JPanel panel = new JPanel();
		panel.add(label);
		return panel;
	}
	
	// private class for the updateButton
	// When the update is successfully processed, the frame received will be closed automatically
	// CASE F
	private class updateButton implements ActionListener
	{
		private JTextField value;
		private JFrame frame;
		
		// store the fields that have the new value
		public updateButton(JTextField newValue, JFrame newFrame)
		{
			value = newValue;
			frame = newFrame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!setQuantity(Integer.parseInt(value.getText())))
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
				frame.dispose();
			}
		}
	}
	
	// function that creates a GUI to update the info of this product
	public boolean update()
	{
		// set up frame
		JFrame frame = new JFrame();
		frame.setTitle("Change Quantity");
		frame.getContentPane().setLayout(new BorderLayout());
		
		// set up section that will enter the new data
		JPanel data = new JPanel(new GridLayout(1,2));
		
		JTextField newValue = new JTextField();
		newValue.setText(Integer.toString(_quantity));
		data.add(new JLabel("Quantity:"));
		data.add(newValue);
		
		// set up the save button
		JButton save = new JButton("Save");
		save.addActionListener(new updateButton(newValue, frame));
		
		// add the date input section and button to frame
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
}
