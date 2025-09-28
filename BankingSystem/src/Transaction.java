
public class Transaction {

	private String date;
	private double amount;
	//whether is it a deposit or withdraw
	private String type; 
	//what category, ie, food or bills
	private String category;
	
	//custom constructor 
	public Transaction(String date,double amount,String type,String category) {
		this.date = date;
		this.amount = amount;
		this.type = type;
		this.category = category;
	}
	
	
	public String getDate() {
		return date;	
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	//formatting for transactions
	public String toString() {
		return date +"," + amount + "," + type + "," + category;
	}
	
}
