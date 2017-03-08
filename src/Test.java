import org.joda.time.LocalDate;

public class Test {
	public static void main(String args[])
	{
		Product myProduct0 = new Product(0);
		Product myProduct1 = new Product(1);
		LocalDate date= new LocalDate(2017,10,21); 
		Product myExpProduct = new ExpirableProduct(0, date);
		System.out.println(((ExpirableProduct) myExpProduct).getExpirationDate());
		System.out.println(myExpProduct.getQuantity());
		System.out.println(myProduct1.getQuantity());
	}
}
