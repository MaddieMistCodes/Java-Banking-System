import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class SimpleBankingSystem {

	//Using a map to store accounts. Most efficient to retrieve accounts, no need to loop
	//Map also ensures no duplicate account numbers.
	private Map<String, Accounts> accounts = new HashMap<>();
    private final Scanner keyboard = new Scanner(System.in);
    
    int accountCheck = 1;
    boolean mainSignIn = true;
    
    // ---------------- Method to run application  ----------------
    public void run() {
    		//load method to be able to refer to user accounts and retrieve associated data
        loadAccAndTrans();
        //First page sign-in
      
        while (mainSignIn) {
            System.out.println("\n🏦 Welcome to your Banking System 🏦");
            System.out.println("Please pick from one of the below:");
            System.out.println("1. Create a new account");
            System.out.println("2. Sign in");
            System.out.println("3. Exit");

            //choice equals return value of getIntInput - which is the number the user picks
         // Check if input is an integer
            if (keyboard.hasNextInt()) {
                int choice = keyboard.nextInt();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    signIn();
                    break;
                case 3:
                    System.out.println("You have successfully exited the program. Please log on at any time");
                    mainSignIn = false;
                    break;
                default:
                    System.out.println("The number you chose is invalid, kindly pick 1,2 or 3.\n");
            }//end of switch
          } //end of if statement
            else {
            	 // Handle non-numeric input
                System.out.println("Invalid input. Please enter a number (1, 2, or 3).\n");
            }
        }
    }

    //Core Features

    private void createAccount() {
    		//Default Balance
    		keyboard.nextLine();
        double accountBalance = 0.0;
        //Using account check + 1 to ensure no duplicates
        String accountID = "SBS" + accountCheck++;
        
        System.out.println("Please enter your name:");
        String accountName = keyboard.nextLine().trim();
        
        // Validate name input
        if (accountName.length() == 0) {
            System.out.println("Name is invalid. Kindly choose option 1 and try again.\n");
            return; // Exit early if name is invalid
        }

        //Create the new account
        Accounts account = new Accounts(accountName, accountID, accountBalance);
        //Adding account object to accounts map
        accounts.put(accountID, account);
        
        //Add the account to the accounts.txt file in CSV format
        saveAccountToFile(account);
        
        //Notify user that all was successful
        System.out.println("All set " + accountName + "! Your account ID is " + accountID +
                " and your balance is " + accountBalance + ".\nKindly choose option 2 to sign in to your account.");
    }

    private void signIn() {
    		keyboard.nextLine();
        System.out.println("Enter your account ID *This starts with SBS*");
        //To upper case to ensure matches format on .txt file
        String inputID = keyboard.nextLine().trim().toUpperCase();

        	//Finds current user account with ID
        Accounts currentUser = findAccountById(inputID);

        //Error message to inform user account does not exist
        if (currentUser == null) {
            System.out.println("Sorry, this account number does not exist.");
            //return back to main menu
            return;
        }
        
        System.out.println("\nWelcome back, " + currentUser.getName() + "!\n");
        //Second menu sign in
        boolean accSignIn = true;

        while (accSignIn) {
        	
            System.out.println("Please choose from one of the below options:\n");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit funds");
            System.out.println("3. Withdraw funds");
            System.out.println("4. View Transactions");
            System.out.println("5. Delete Account");
            System.out.println("6. Sign Out");

            	if(keyboard.hasNextInt()) {
            int accChoice = keyboard.nextInt();
            keyboard.nextLine();

            switch (accChoice) {
                case 1:
                    checkBalance(currentUser);
                    break;
                case 2:
                    depositFunds(currentUser);
                    break;
                case 3:
                    withdrawFunds(currentUser);
                    break;
                case 4:
                    transactionHistory(currentUser);
                    break;
                   
                case 5:
                		deleteAccount(currentUser);
                		break;
                case 6:
                		//print goodbye message
                    signOut();
                    //end second menu sign in
                    accSignIn = false;
                    mainSignIn = false;
                    //end switch block - avoids accidentally running into other cases
                    break;
                //Fallback if a number input is not valid, will inform user
                default:
                    System.out.println("Invalid option, try again.\n");
                    //clear buffer
                    keyboard.nextLine();
            }
            	}
            	else {
            		System.out.println("Invalid input. Kindly enter a number from 1-6");
            	}
            //if accSignIn is still true, continue with the second menu
            if (accSignIn) {
               accSignIn = toContinue(keyboard);
               //If toContinue is false, close main program too
               if (accSignIn == false){
            	     mainSignIn = false;
               }
            }
            
        }
    }

    // ---------------- Helper Methods ----------------

    private void loadAccAndTrans() {
    	//load accounts from accounts.txt
    	
    	//BufferReader will read text from file(line by line). FileReader opens the file to read.
        try (BufferedReader accReader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
          //Keeps reading file until line equals nothing{ensures it scans through all the text
            while ((line = accReader.readLine()) != null) {
            	//Will break line into parts with commas to form array.ie, "John,SBS5,1000.50" goes to ["John", "SBS5", "1000.50"]
                String[] parts = line.split(",");
                //Checks to ensure exactly three parts
                if (parts.length == 3) {
                    String name = parts[0];
                    String id = parts[1];
                  //Converts the string balance into double
                    double balance = Double.parseDouble(parts[2]);
                    //After loading data, that info is used to recreate the objects needed
                    Accounts account = new Accounts(name, id, balance);
                    accounts.put(id, account);

                    //Extract the number from the ID and keep track of the highest. This ensures no repeats
                    try {
                    		//This removes SBS from id to just get number. Replaces SBS with ""(nothing)
                        int num = Integer.parseInt(id.replace("SBS", ""));
                        if (num >= accountCheck) {
		                    accountCheck = num + 1;
                        }
                    } 
                    // skip malformed IDs
                    catch (NumberFormatException e) {}
                }
            }
          //Lets user know if working properly
            System.out.println("Accounts loaded from file.\n");
        } //end of try statement 
        catch (IOException e) {
        		//print error message and inform user
            System.out.println("Could not load accounts: " + e.getMessage());
        }
        
    //read transactions from file
        
        try(BufferedReader transReader = new BufferedReader(new FileReader("transactions.txt"))){
        	//assign line to read in the text from the file
        	String line;
        	while ((line = transReader.readLine()) != null) {
        	    String[] parts = line.split(",");
        		//when it reads in 5 parts, assign to appropriate variable
        		if(parts.length ==5) {
        			String accountID = parts[0];
                String date = parts[1];
                //convert string balance to double
                double amount = Double.parseDouble(parts[2]);
                String type = parts[3];
                String category = parts[4];
                
                Accounts account = accounts.get(accountID);
                if(account != null) {
                	account.getTransactions().add(new Transaction(date, amount, type, category));
                }
        		}
        	} 	
      }
     catch (IOException e) {
         System.out.println("Could not load transactions: " + e.getMessage());
     }
        
    }
    

    private void saveAccountToFile(Accounts account) {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream("accounts.txt", true))) {
            writer.println(account.getName() + "," + account.getAccountID() + "," + account.getBalance());
            System.out.println("Account saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving account:" + e.getMessage());
        }
    }

    	private void saveTransactionsToFile(String accountID, double amount, String type, String category) {
    		//The file output argument is set to true, ensuring if text already exists - it will add instead of overwrite
    		try (PrintWriter writer = new PrintWriter(new FileOutputStream("transactions.txt", true))) {
    			//writer.print to ensure it appears on our transactions.txt
    			writer.println(accountID + "," + LocalDate.now() + "," + amount + "," + type + "," + category);
    	    } catch (IOException e) {
    	        System.out.println("Error saving transaction: " + e.getMessage());
    	    }
    		
    		}
    	
    private void updateAccountsFile() {
    	
    		//Creates writer object and passes accounts.txt as an arguement.
		//False ensures an overwrite, this ensures only the current account data is displayed on accounts.txt
		//PrintWriter also allows use of print ie. writer.println("")
        try (PrintWriter writer = new PrintWriter(new FileOutputStream("accounts.txt", false))) {
        	//accounts.values() gives us collection of accounts, this stores all the account objects stored in map
        	for (Accounts acc : accounts.values()) {
            //prints onto .txt file the accounts
            writer.printf("%s,%s,%.2f%n", acc.getName(), acc.getAccountID(), acc.getBalance());
        }
            System.out.println("\nAccounts file updated.");
        } 
        //if error update user with error
	    catch (IOException e) {
	        System.out.println("Error updating file: " + e.getMessage());
	    }
    }

    private Accounts findAccountById(String id) {
    		return accounts.get(id);
    }

    private void updateTransactionsFile() {
        try {
            FileWriter writer = new FileWriter("transactions.txt", false); // Overwrite file

            for (Accounts account : accounts.values()) {
                String accountID = account.getAccountID();
                List<Transaction> transactions = account.getTransactions();

                for (Transaction t : transactions) {
                	//formatting must be same as the loanAccTrans ,split(,). If not there will be parse error
                    writer.write(accountID + "," + t.toString() + "\n");
                }
            }

            writer.close();
            System.out.println("Transaction file successfully updated.");
        } catch (IOException e) {
            System.out.println("Error updating transaction file: " + e.getMessage());
        }
    }

    private void depositFunds(Accounts currentUser) {
        System.out.println("How much money would you like to deposit?\n*If above 2 digits are input, amount will be rounded.*");
      //ensuring a double is provided
        if (keyboard.hasNextDouble()) {
            double userDeposit = keyboard.nextDouble();
            
            if (userDeposit > 0) {
            keyboard.nextLine();
            // Round to two decimal places
            userDeposit = Math.round(userDeposit * 100.0) / 100.0;
            System.out.println("What is the category? (e.g. Monthly Pay, Gift)");
            String depositCategory = keyboard.nextLine();

          //Deposit funds using custom method, will also be added to transaction list
            currentUser.deposit(userDeposit, depositCategory);
            updateAccountsFile();
            saveTransactionsToFile(currentUser.getAccountID(), userDeposit, "Deposit",depositCategory );
            System.out.println("Deposit successful.\n"); }
            else {
            	System.out.println("Amount must be greater than 0");
            }
        } else {
            System.out.println("Invalid input - please enter a figure to 2 decimal digits.");
            keyboard.nextLine();
        }
    }

    private void withdrawFunds(Accounts currentUser) {
        System.out.println("How much money would you like to withdraw?");
        //ensuring a double is provided
        if (keyboard.hasNextDouble()) {
            double userWithdraw = keyboard.nextDouble();
            if(userWithdraw > 0) {
          //issues caused as newline character after number is not consumed.
            keyboard.nextLine();
            if (userWithdraw > currentUser.getBalance()) {
                System.out.println("Insufficient funds. Withdrawal cancelled.\n");
                return; // exit the method early
            }
            
            System.out.print("What is the category for this withdrawal? ");
            String withdrawCategory = keyboard.nextLine();

          //Deposit funds using custom method, will also be added to transaction list
            currentUser.withdraw(userWithdraw, withdrawCategory);
            updateAccountsFile();
            saveTransactionsToFile(currentUser.getAccountID(), userWithdraw, "Withdrawal", withdrawCategory);
            System.out.println("Withdrawal successful.\n");}
            else {
            	System.out.println("Amount must be greater than 0 - please try again");
            }
        } else {
            System.out.println("Invalid input - please enter a figure to 2 decimal digits.");
            keyboard.nextLine();
        }
    }

    private void checkBalance(Accounts currentUser) {
    	//print balance
        System.out.printf("Your current balance is $%.2f.\n", currentUser.getBalance());
    }

    private void transactionHistory(Accounts currentUser) {
    	
    		//Gather transactions related to that user
     	//use a list so transactions are displayed in order,also allows duplicates which may be needed.
        List<Transaction> userTransactions = currentUser.getTransactions();
        //if none - inform user
        if (userTransactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("Transaction History:");
            //for loop to run through and print all user transactions
            for (int i = 0; i < userTransactions.size(); i++) {
                System.out.println((i + 1) + ". " + userTransactions.get(i));
            }
        }
    }

    private void deleteAccount(Accounts currentUser) {

    	
    		String confirmation;
    		
    		System.out.println("Are you sure you want to delete your account?");
    		
    		confirmation = keyboard.nextLine().trim().toLowerCase();
    		
    		if(confirmation.equals("yes")) {
    			//find ID related to the user and remove
    			String accountID = currentUser.getAccountID();

    	        // Remove account from accounts map
    	        accounts.remove(accountID);
    	        updateAccountsFile();

    	        // Clear transaction history
    	        currentUser.getTransactions().clear();
    	        updateTransactionsFile();
    	        
    	        System.out.println("Your account has been permanently deleted, thank you for using our services.\nKindly go to a branch to collect any remaining funds.");
    	        System.exit(0);
    		 }
    	     else if (confirmation.equals("no")) {
    	    	  System.out.println("Account deletion has been aborted - thank you for continuing to use our services");

    	      }
    	     else {
    	        System.out.println("Problem occured during account deletion - please try again");
    	    }
    			
    		}
    
    
    
    private void signOut() {
    	//print to inform user of signing out
        System.out.println("You have successfully signed out. Have a good day!");
    }

    private static boolean toContinue(Scanner keyboard) {
    		
    	String input;
    		
    		//use a while loop to ensure it repeats on invalids inputs.
        while (true) {
            System.out.println("Would you like to continue? (yes/no)");
            input = keyboard.nextLine().trim().toLowerCase();

            if (input.equals("yes")) {
                return true;
            } 
            else if (input.equals("no")) {
                System.out.println("\nYou have successfully logged out - come back soon!");
                return false;
            } 
            else {
                System.out.println("Invalid input - please type yes or no.");
            }
        }
    }
}
