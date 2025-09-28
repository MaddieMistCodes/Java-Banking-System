import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class Accounts {
	
	//declare variables needed for an account
	private String name; 
	private String accountID;
	private double balance;
	//create list to hold account transactions
	private List<Transaction> transactions; 
	
	
	//custom constructor to make object creation easier
	public Accounts(String name,String accountID, double balance) { 
		this.name = name;
		this.accountID = accountID;
		this.balance = balance;
		//Not a parameter. Every new account starts with an empty list of transactions. 
		this.transactions = new ArrayList<>();
	}
	
	//create setters an getters to update and print our variables
	public String getName() { 
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	//Method to deposit funds into the account.
	public void deposit(double amount, String category) {
		if (amount > 0) {
			this.balance = amount + balance;
			Transaction t = new Transaction(getCurrentDate(), amount, "Deposit", category);
			transactions.add(t);//saving the transaction into the transactions list
		}
		else {
			System.out.println("Sorry, the amount must be greater than 0 for a deposit, please try again");
		}
	}
	
	//Method to withdraw funds from balance
	public void withdraw(double amount,String category) { 
		//check if amount is above 0 and ensure that the balance is greater or equal to withdrawal
		if(amount > 0 && balance >= amount) { 
			this.balance = balance - amount;
			Transaction t = new Transaction(getCurrentDate(), amount, "Withdrawal", category);
			transactions.add(t);
			}
			else {
				 System.out.println("Insufficient funds or invalid amount.");
				 return;//exit early 
		}
	}
	//Method to get current date, outputs like "2025-06-09"
	private String getCurrentDate() {
	    return LocalDate.now().toString(); 
	}
		
	//This method gives controlled access to our Transactions List
	public List<Transaction> getTransactions() { 
	    return transactions;
	}		
}
