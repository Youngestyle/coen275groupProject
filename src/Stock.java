/*
 * file: Stock.java
 * This is the file for the stock class
 * stock is a container that contains a list of different products that the user owned.
 * 
 * Public functions:
 * Stock()
 * void display()
 * void organize()
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import org.joda.time.LocalDate;

public class Stock {
	
	// list of different products
	private ArrayList<ProductInformation> _products;
	private JPanel _list;
	private JLabel _listContent;
	
	// constructor
	public Stock()
	{
		_products = new ArrayList<ProductInformation>(); 
	}
	
	// function display the GUI
	public void display()
	{
		// set up the frame
		final JFrame frame = new JFrame();
		
		// paint the content of the frame
		paint(frame);
	}
	
	// class that returns the total quantity of the list received
	private int totalInList(ArrayList<Product> products)
	{
		int total =0;
		for(int i=0; i<products.size(); i++)
		{
			total += products.get(i).getQuantity();
		}
		return total;
	}
	
	// class that implements the action listerned for the search button
	// this prints the product's name and category that contains the keywords on the list
	private class searchButton implements ActionListener
	{

		private JTextField data;
		
		public searchButton(JTextField newData)
		{
			data = newData;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			organize();
			_list.removeAll();
			boolean found = false;
			for(int i=0; i< _products.size(); i++)
			{
				if(_products.get(i).getName().toLowerCase().contains(data.getText().toLowerCase()) || _products.get(i).getCategory().toLowerCase().contains(data.getText().toLowerCase()))
				{
					found = true;
					printProducts(_products.get(i), _products.get(i).getAll());
				}
			}
			
			if(!found)
			{
				_list.add(new JLabel("No Product found!"));
			}
			_list.revalidate();
			_list.repaint();
			_listContent.setText("Search: '"+ data.getText() +"'");
		}
		
	}
	
	// button to show detail information of a product
	private class detailButton implements ActionListener
	{
		private ProductInformation product;
		
		detailButton(ProductInformation newProduct)
		{
			product = newProduct;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame frame = new JFrame();
			product.paint(frame);
		}
		
	}
	
	// function that prints the products to display
	private void printProducts(ProductInformation product, ArrayList<Product> products)
	{		
		if(product.isExpirable())
		{
			for(int i=0; i<products.size(); i++)
			{
				JPanel row = new JPanel(new BorderLayout());
				Product eachProduct = products.get(i);
				row.add(new JLabel(product.getName() + ", " + eachProduct.getQuantity() + ", " + ((ExpirableProduct)eachProduct).getExpirationDate().toString()), BorderLayout.WEST);
				JButton detail = new JButton("Detail");
				detail.addActionListener(new detailButton(product));
				row.add(detail, BorderLayout.EAST);
				row.setMaximumSize(new Dimension(250,20));
				_list.add(row);
			}
		}
		else
		{
			for(int i=0; i<products.size(); i++)
			{
				JPanel row = new JPanel(new BorderLayout());
				Product eachProduct = products.get(i);
				row.add(new JLabel(product.getName() + ", " + eachProduct.getQuantity()), BorderLayout.WEST);
				JButton detail = new JButton("Detail");
				detail.addActionListener(new detailButton(product));
				row.add(detail, BorderLayout.EAST);
				row.setMaximumSize(new Dimension(250,20));
				_list.add(row);
			}
		}
	}
	
	// function creates the GUI
	private void paint(JFrame frame)
	{
		// set up the frame
		frame.getContentPane().removeAll();
		frame.setTitle("Stock");
		frame.getContentPane().setLayout(new BorderLayout());
		
		// set up a dummy label for formatting purposes
		JLabel invisible = new JLabel();
		invisible.setVisible(false);
		
		// cluster the array of products, aka, merge the one that has the same information
		organize();
		
		JPanel left = new JPanel(new BorderLayout());
		JPanel right = new JPanel(new BorderLayout());
		frame.getContentPane().add(left, BorderLayout.WEST);
		frame.getContentPane().add(right, BorderLayout.EAST);
		
		// set up left side - control side
		JLabel title = new JLabel("Stock");
		title.setFont(new Font("Arial", Font.PLAIN, 20));
		
		// set up control
		JPanel controls = new JPanel(new GridLayout(7,1));
		left.add(title, BorderLayout.NORTH);
		left.add(controls, BorderLayout.CENTER);
		
		
		// CASE B
		JButton addProduct = new JButton("New");
		addProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ProductInformation newProduct = new ProductInformation();
				newProduct.createProduct();
				_products.add(newProduct);
				organize();
			}
		});
		
		// CASE C
		// CASE D
		// show the list of all expired products
		JButton expired = new JButton("Expired");
		expired.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				organize();
				_list.removeAll();
				boolean found = false;
				for(int i=0; i<_products.size(); i++)
				{
					ArrayList<Product> products = _products.get(i).getExpired();
					if(products.size()>0)
					{
						found = true;
						printProducts(_products.get(i), products);
					}
				}
				if(!found)
				{
					_list.add(new JLabel("No product that are expired!"));
				}
				_list.revalidate();
				_list.repaint();
				_listContent.setText("Search: Expired Products");
			}
		});
		
		// CASE C
		// CASE E
		// show the list of all products what soon will be expired
		JButton soonExpired = new JButton ("Soon Expired");
		soonExpired.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				organize();
				_list.removeAll();
				boolean found = false;
				for(int i=0; i<_products.size(); i++)
				{
					ArrayList<Product> products = _products.get(i).getSoonExpired();
					if(products.size() > 0)
					{
						found = true;
						printProducts(_products.get(i), products);
					}
				}
				if(!found)
				{
					_list.add(new JLabel("No product that will soon be expired!"));
				}
				_list.revalidate();
				_list.repaint();
				_listContent.setText("Search: Soon to be Expired Products");
			}
		});
		
		// CASE C
		// show the list of all products that are not expired yet
		JButton notExpired = new JButton ("Not Expired");
		notExpired.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				organize();
				_list.removeAll();
				boolean found = false;
				for(int i=0; i<_products.size(); i++)
				{
					ArrayList<Product> products = _products.get(i).getNotExpired();
					if(products.size() > 0)
					{
						found = true;
						printProducts(_products.get(i), products);
					}
				}
				if(!found)
				{
					_list.add(new JLabel("No product that is not expired!"));
				}
				_list.revalidate();
				_list.repaint();
				_listContent.setText("Search: Not Expired Products");
			}
		});
		
		// CASE C
		// show the list of all products that are not expired and below 5 quantity
		JButton lowStock = new JButton("Low Stock");
		lowStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				organize();
				_list.removeAll();
				boolean found = false;
				for(int i=0; i<_products.size(); i++)
				{
					ArrayList<Product> products = _products.get(i).getLowStock();
					if(products.size() > 0)
					{
						found = true;
						printProducts(_products.get(i), products);
					}
				}
				if(!found)
				{
					_list.add(new JLabel("No product is low in stock!"));
				}
				_list.revalidate();
				_list.repaint();
				_listContent.setText("Search: Low Stock Products");
			}
		});
		
		// CASE C
		JButton all = new JButton("All");
		all.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				organize();
				_list.removeAll();
				boolean found = false;
				for(int i=0; i<_products.size(); i++)
				{
					ArrayList<Product> products = _products.get(i).getAll();
					if(products.size() > 0)
					{
						found = true;
						printProducts(_products.get(i), products);
					}
				}
				if(!found)
				{
					_list.add(new JLabel("No product exists!"));
				}
				_list.revalidate();
				_list.repaint();
				_listContent.setText("Search: All Products");
			}
		});
		
		controls.add(addProduct);
		controls.add(invisible);
		controls.add(expired);
		controls.add(soonExpired);
		controls.add(notExpired);
		controls.add(lowStock);
		controls.add(all);
		
		
		
		// set up right side - list side
		
		// set up search section - right side top
		// CASE C
		JPanel searchPanel = new JPanel(new BorderLayout());
		JTextField searchCriteria = new JTextField();
		searchCriteria.setPreferredSize(new Dimension(200,0));
		JButton search = new JButton("Search");
		search.addActionListener(new searchButton(searchCriteria));
		searchPanel.add(searchCriteria, BorderLayout.WEST);
		searchPanel.add(search, BorderLayout.EAST);
		_listContent = new JLabel("Search: ");
		searchPanel.add(_listContent, BorderLayout.SOUTH);
		
		right.add(searchPanel, BorderLayout.NORTH);
		
		// set up list section that will display the list of products
		_list = new JPanel();
		_list.setLayout(new BoxLayout(_list, BoxLayout.Y_AXIS));
		JScrollPane _scrollableList = new JScrollPane(_list);
		_scrollableList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		_scrollableList.setPreferredSize(new Dimension(300, 80*8));
		right.add(_scrollableList, BorderLayout.CENTER);
		
		
		// add padding to the frame
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		((JComponent) frame.getContentPane()).setBorder(padding);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	// function that returns the location if there is a similar product in the ArrayList but before this product
	// return -1 if not found
	private int hasProductBefore(ProductInformation product, int location)
	{
		for(int i =0; i<location; i++)
		{
			if(_products.get(i).getName().toLowerCase().equals(product.getName().toLowerCase()))
			{
				return i;
			}
		}
		return -1;
	}
	
	// function that organizes the list of products
	// merge same products together -> the ones with the same name and expirable
	public void organize()
	{
		for(int i=this._products.size()-1; i>0; i--)
		{
			_products.get(i).organize();
			int index;
			index = hasProductBefore(_products.get(i), i);
			if(index != -1)
			{
				System.out.println("found");
				_products.get(index).addProducts(_products.get(i));
				_products.remove(i);
			}
		}
	}
	
	// CASE A
	public static void main(String[] args)
	{
		Stock myStock = new Stock();
		
		ProductInformation a = new ProductInformation("Apple", "fruit", true);
		Product myProduct0 = new Product(2);
		//Product myProduct1 = new Product(1);
		LocalDate date= new LocalDate(2017,10,21); 
		Product myExpProduct = new ExpirableProduct(20, date);
		
		//a.addProduct(myProduct0);
		//a.addProduct(myProduct1);
		a.addProduct(myExpProduct);
		//a.display();
		
		ProductInformation b = new ProductInformation("Soap", "Cleaning", false);
		ProductInformation c = new ProductInformation("Noodle", "Food", true);
		ExpirableProduct noodle = new ExpirableProduct(10, new LocalDate(2017,2,5));
		c.addProduct(noodle);
		c.organize();
		myStock._products.add(c);
		b.addProduct(myProduct0);
		//b.createProduct();
		myStock._products.add(a);
		myStock._products.add(b);
		b.organize();
		
		//LocalDate today = new LocalDate();
		//LocalDate tomorrow = new LocalDate(2017,3,11);
		//LocalDate yesterday = new LocalDate(2017,3,9);
		//System.out.println("today-tomorrow: "+ Days.daysBetween(today, tomorrow).getDays());
		//System.out.println("today-yesterday: "+ Days.daysBetween(today, yesterday).getDays());
		
		myStock.display();
	}
}
