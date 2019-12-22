package btserv;

public class Book {
	private BookTitle title;
	private double price;
	
	public Book (BookTitle title) {
		this.setTitle(title);
	}  

	public Book (BookTitle title, double price) {
		this.setTitle(title);
		this.setPrice(price);
	} 

	public BookTitle getTitle() {
		return title;
	} 

	public void setTitle(BookTitle title) {
		this.title = title;
	}
	public double getPrice(){
		return price;
	}
	
	private void setPrice(double price) {
		this.price = price;
		
	}
}

