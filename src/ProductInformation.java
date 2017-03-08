/*
 * file: ProductInformation.java
 * This is contains the class information of different types of products.
 * It stores a list of products that has the same "type"
 * 
 * The date object uses the following Java API:
 * Joda-Time
 * http://www.joda.org/joda-time/ 
 * 
 * Public functions:
 * ProductInformation()
 * ProductInformation(String newName, String newCategory, boolean newExpirable)
 * boolean isExpirable()
 * String getName()
 * String getCategory()
 * boolean setName(String newName)
 * boolean setCategory(String newCategory)
 * boolean setExpirable(boolean newExpirable)
 * int getTotalQuantity()
 * int getTotalNonExpiredQuantity()
 * boolean addProduct(Product newProduct)
 * boolean removeProduct(int i)
 * void display()
 * void createProduct()
 */

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import org.joda.time.LocalDate;

public class ProductInformation {
	
	// variable that will store information
	private String _name;
	private String _category;
	private boolean _expirable;
	
	//List of products and expirable products
	private ArrayList<Product> products;
	
	// constructor
	public ProductInformation()
	{
		_name= "";
		_category= "";
		_expirable = false;
		products = new ArrayList<Product>();
	}
	
	// constructor
	public ProductInformation(String newName, String newCategory, boolean newExpirable)
	{
		_name= newName;
		_category= newCategory;
		_expirable = newExpirable;
		products = new ArrayList<Product>();
		
		// If this product is not expirable, it only need one product in the ArrayList
		// This is the fault-safe for segmentation fault, in case there is not product in the array list
		// It should be covered when add new products
		if(!_expirable)
		{
			products.add(new Product(0));
		}
	};

	// returns whether this product is expirable
	public boolean isExpirable()
	{
		return _expirable;
	};
	
	// returns the name of this product
	public String getName()
	{
		return _name;
	};
	
	// returns the category of this product
	public String getCategory()
	{
		return _category;
	};
	
	// change the name of the product
	public boolean setName(String newName)
	{
		_name = newName;
		return true;
	};
	
	// change the category of the product
	public boolean setCategory(String newCategory)
	{
		_category= newCategory;
		return true;
	};
	
	// change the expirable of the product
	public boolean setExpirable(boolean newExpirable)
	{
		_expirable = newExpirable;
		return true;
	};
	
	// returns the total quantity of all products in this class
	public int getTotalQuantity()
	{
		int total = 0;
		for(int i = 0; i< products.size(); i++)
		{
			total += products.get(i).getQuantity();
		}
		return total;
	};
	
	// returns the total quantity of all products in this class that is not expired
	public int getTotalNonExpiredQuantity()
	{
		int total = 0;
		
		// if the item is not expirable, it will just return the total quantity
		if(!this._expirable)
		{
			System.out.println("ERROR: cannot get Expired Quantity, item is not expirable");
			return getTotalQuantity();
		}
		
		for(int i = 0; i< products.size(); i++)
		{
			if(((ExpirableProduct)products.get(i)).getExpirationDate().isAfter(new LocalDate()))
			{
				total += products.get(i).getQuantity();
			}
		}
		return total;
	}
	
	// add new product to the list of products
	public boolean addProduct(Product newProduct)
	{
		products.add(newProduct);
		return true;
	}
	
	// remove the product at index i
	public boolean removeProduct(int i)
	{
		products.remove(i);
		return true;
	}
	
	// private class for the edit button
	// this call the update function in the Product class or the ExpirableProduct class
	private class editButton implements ActionListener
	{
		private int _index;
		public editButton(int index)
		{
			this._index = index;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(_expirable)
			{
				((ExpirableProduct)products.get(_index)).update();
			}
			else
			{
				products.get(_index).update();
			}
		}
		
	}
	
	// create a frame and display the information of this product
	public void display()
	{
		// set up the frame
		final JFrame frame = new JFrame();
		
		// paint the content of the frame
		paint(frame);
		
		// add listener to the Window, such that when this window is refocused, refresh the content of the frame
		// in case there are changes to the content
		frame.addWindowFocusListener(new WindowFocusListener()
		{

			@Override
			public void windowGainedFocus(WindowEvent e) {
				paint(frame);
				
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
	
		});
	}
	
	// private class for the save button
	// When the save is successfully processed, the frame received will be closed automatically
	// this button is for change the basic product information
	private class saveButton implements ActionListener
	{
		private JTextField name;
		private JTextField category;
		private JFrame frame;
		
		// store the fields that have the new values
		public saveButton(JTextField newName, JTextField newCategory, JFrame newFrame)
		{
			this.name = newName;
			this.category = newCategory;
			frame = newFrame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!setName(this.name.getText()) || !setCategory(this.category.getText()))
			{
				JOptionPane.showMessageDialog(null,
					"Invalid Input",
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(null,
					"Changes Saved",
					"Saved",
					JOptionPane.INFORMATION_MESSAGE);
				frame.dispose();
			}
		}
	}
	
	// function that creates a GUI that allow to modify the information of this class
	private boolean editProductInformation()
	{
		// set up frame
		JFrame frame = new JFrame();
		frame.setTitle("Edit Information");
		frame.getContentPane().setLayout(new GridLayout(3,2));
		
		// set up data section
		JTextField newName = new JTextField();
		newName.setText(_name);
		frame.getContentPane().add(new JLabel("Name"));
		frame.getContentPane().add(newName);
		
		JTextField newCategory = new JTextField();
		newCategory.setText(_category);
		frame.getContentPane().add(new JLabel("Category"));
		frame.getContentPane().add(newCategory);
		
		// set up save button
		JButton save = new JButton("Save");
		save.addActionListener(new saveButton(newName, newCategory, frame));
		frame.add(save);
		
		// add padding to frame
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		((JComponent) frame.getContentPane()).setBorder(padding);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		return true;
	}
	
	// private class for the remove Button
	// this will remove the product from the list of products
	// the product is the one at the index location
	private class removeButton implements ActionListener
	{
		private int index;
		
		public removeButton(int i)
		{
			index = i;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to remove this product?");
			
			if(dialogResult == JOptionPane.YES_OPTION)
			{
				removeProduct(index);
			}
		}
		
	}
	
	// display the information of this class into the frame received
	private void paint(JFrame frame)
	{
		// set up the frame
		frame.getContentPane().removeAll();
		frame.setTitle(_name);
		
		// cluster the array of products, aka, merge the one that has the same information
		organize();
		
		// set up basic information section
		JPanel info = new JPanel(new BorderLayout());
		
		JPanel productInfo = new JPanel(new BorderLayout());
		
		JLabel productName = new JLabel(_name);
		
		productName.setFont(new Font("Arial", Font.PLAIN, 20));
		
		// button to edit the basic information
		JButton editInfo = new JButton("Edit Info");
		editInfo.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						editProductInformation();
					}
				});
		
		// button to add a new product to the list of products
		JButton newProduct = new JButton("New");
		newProduct.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Product newProduct;
				if(_expirable)
				{
					newProduct = new ExpirableProduct();
					((ExpirableProduct)newProduct).update();
				}
				else
				{
					newProduct = new Product();
					newProduct.update();
				}
				
				addProduct(newProduct);
			}
		});
		
		// add the buttons to the panel
		JPanel buttons = new JPanel(new BorderLayout());
		buttons.add(editInfo, BorderLayout.NORTH);
		buttons.add(newProduct, BorderLayout.SOUTH);
		productInfo.add(productName, BorderLayout.NORTH);
		productInfo.add(new JLabel(_category), BorderLayout.CENTER);
		productInfo.add(buttons, BorderLayout.EAST);
		
		// section for each product in the list of products
		JPanel detailInfo = new JPanel(new GridLayout(products.size(),3));
		for(int i=0; i<products.size(); i++)
		{
			detailInfo.add(products.get(i).display());
			JButton editQuantity = new JButton("Edit");
			editQuantity.addActionListener(new editButton(i));
			detailInfo.add(editQuantity);
			JButton removeProduct = new JButton("Remove");
			removeProduct.addActionListener(new removeButton(i));
			detailInfo.add(removeProduct);
		}
		
		// add the panels to the frame
		info.add(productInfo, BorderLayout.NORTH);
		info.add(detailInfo, BorderLayout.CENTER);
		
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(info, BorderLayout.CENTER);
		
		// add padding to the frame
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		((JComponent) frame.getContentPane()).setBorder(padding);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	// private class for the saveNewInfoButton
	// this button is used when creating a new ProductInformation class
	// where the user is required to enter the information of the first product -> quantity + [expiration date]
	// when the save is successfully processed, the frame is automatically closed,
	// and a new frame is created to display the new product created
	private class saveNewInfoButton implements ActionListener
	{
		JTextField name;
		JTextField category;
		JCheckBox expirable;
		JTextField quantity;
		JTextField expirationDate;
		JFrame frame;
		
		public saveNewInfoButton(JTextField newName, JTextField newCategory, JCheckBox newExpirable, JTextField newQuantity, JTextField newExpirationDate, JFrame newFrame)
		{
			name = newName;
			category = newCategory;
			expirable = newExpirable;
			quantity = newQuantity;
			expirationDate = newExpirationDate;
			frame = newFrame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// set the name and category
			if(!setName(name.getText()) || !setCategory(category.getText()))
			{
				JOptionPane.showMessageDialog(null,
						"Invalid Input",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Product newProduct;
			
			// check if the product is expirable or not
			if(this.expirable.isSelected())
			{
				// set up expiration information
				newProduct = new ExpirableProduct();
				_expirable = true;
				String[] parts = expirationDate.getText().split("-");
				if(!(((ExpirableProduct) newProduct).setExpirationDate(new LocalDate(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),Integer.parseInt(parts[2])))))
				{
					JOptionPane.showMessageDialog(null,
							"Invalid Input",
							"ERROR",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			else
			{
				newProduct = new Product();
			}
			
			// set up quantity
			if(!newProduct.setQuantity(Integer.parseInt(quantity.getText())))
			{
				JOptionPane.showMessageDialog(null,
						"Invalid Input",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			addProduct(newProduct);
			JOptionPane.showMessageDialog(null,
				"New Value Saved",
				"Saved",
				JOptionPane.INFORMATION_MESSAGE);
				frame.dispose();
				display();
		}
	}
	
	// private class that listens to the checkbox for expiration
	// if the checkbox is selected, allow the user to enter expiration date info
	// other wise, hide it
	private class checkBoxListener implements ItemListener{

		private JLabel label;
		private JTextField field;
		
		public checkBoxListener(JLabel newLabel, JTextField newField)
		{
			label = newLabel;
			field = newField;
		}
		
		@Override
		public void itemStateChanged(ItemEvent e) {
		    if (e.getStateChange() == ItemEvent.DESELECTED)
		    {
		    	label.setVisible(false);
		    	field.setVisible(false);
		    }
		    else if(e.getStateChange() == ItemEvent.SELECTED)
		    {
		    	label.setVisible(true);
		    	field.setVisible(true);
		    }
		}
		
	}
	
	// function that creates a GUI that allow the user to create a new ProductInfomation
	public void createProduct()
	{
		// set up the frame
		JFrame frame = new JFrame();
		frame.setTitle("New Product Information");
		frame.getContentPane().setLayout(new GridLayout(6,2));
		
		// set up a dummy label for formatting purposes
		JLabel invisible = new JLabel();
		invisible.setVisible(false);
		
		// set up the data section
		JTextField newName = new JTextField();
		newName.setText(_name);
		frame.getContentPane().add(new JLabel("Name"));
		frame.getContentPane().add(newName);
		
		JTextField newCategory = new JTextField();
		newCategory.setText(_category);
		frame.getContentPane().add(new JLabel("Category"));
		frame.getContentPane().add(newCategory);
		
		JCheckBox newExpirable = new JCheckBox("Expirable");
		frame.getContentPane().add(newExpirable);
		frame.getContentPane().add(invisible);
		newExpirable.setMnemonic(KeyEvent.VK_E);
		
		JTextField newQuantity = new JTextField();
		newQuantity.setText("0");
		frame.getContentPane().add(new JLabel("Quantity"));
		frame.getContentPane().add(newQuantity);
		
		JTextField newExpirationDate = new JTextField();
		newExpirationDate.setText(new LocalDate().toString());
		JLabel expirationLabel = new JLabel("Expiration Date");
		frame.getContentPane().add(expirationLabel);
		frame.getContentPane().add(newExpirationDate);
		
		newExpirationDate.setVisible(false);
		expirationLabel.setVisible(false);
		
		// add listener to checkbox
		newExpirable.addItemListener(new checkBoxListener(expirationLabel,newExpirationDate));
		
		// set up button for save
		JButton save = new JButton("Save");
		save.addActionListener(new saveNewInfoButton(newName, newCategory, newExpirable, newQuantity, newExpirationDate, frame));
		frame.add(save);
		
		// add padding to frame
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		((JComponent) frame.getContentPane()).setBorder(padding);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	// function that returns the location if there is a similar product in the ArrayList but before this product
	// return -1 if not found
	private int hasProductBefore(ExpirableProduct product, int location)
	{
		for(int i =0; i<location; i++)
		{
			if(((ExpirableProduct)products.get(i)).getExpirationDate().equals(product.getExpirationDate()))
			{
				return i;
			}
		}
		return -1;
	}
	
	// function that reorganizes the ArrayList of products
	// clusters - such that products with same expiration date are grouped together
	// if for non-expirable products, they simply grouped all together
	private Boolean organize()
	{
		for(int i=products.size()-1; i>0; i--)
		{
			if(this._expirable)
			{
				int index;
				index = hasProductBefore((ExpirableProduct)products.get(i), i);
				if(index != -1)
				{
					products.get(index).increaseQuantity(products.get(i).getQuantity());
					products.remove(i);
				}
			}
			else
			{
				products.get(0).increaseQuantity(products.get(i).getQuantity());
				products.remove(i);
			}
		}
		return true;
	}
	
	// test function
	// need to be removed when all are done
	public static void main(String[] args)
	{
		/*ProductInformation a = new ProductInformation("Apple", "Fruit", true);
		Product myProduct0 = new Product(0);
		Product myProduct1 = new Product(1);
		LocalDate date= new LocalDate(2017,10,21); 
		Product myExpProduct = new ExpirableProduct(20, date);
		
		//a.addProduct(myProduct0);
		//a.addProduct(myProduct1);
		a.addProduct(myExpProduct);
		a.display();
		*/
		ProductInformation b = new ProductInformation();
		b.createProduct();
	}
}
