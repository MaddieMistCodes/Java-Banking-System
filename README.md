# Java-Banking-System
A simple Java Banking System that lets users create accounts, sign in, deposit and withdraw funds, track transactions, view balances and history, and permanently delete accounts. Includes text file storage for account data and transactions, demonstrating OOP and file handling.

## 📌 Features
• Text based menu with number options to navigate through.
• Creates user bank accounts and assigns each user an ID.
• Allows users with ID to access banking services.
• Allows deposits and withdrawals while writing this data to a text file.
• Allows users to check available balance on their account.
• Allows users to view their past transactions. 
• If user decides to stop using service, an option to permanently delete the account is provided. 

## 📌 Tech Stack
• Java
• Object-Orientated Programming
• Collections Framework, ie ArrayList and HashMap for storing data
• File I/O - reading/writing data to text files

## 📌 Getting Started
This project was created on Eclipse IDE - to run on Eclispse the instrcutions are below:

1. Open Eclipse
2. Go to File -> import -> Existing Projects in Workspace
3. Select the project folder that was cloned
4. Right click on MainBanking.java -> run as -> Java Application

This application can also run on the terminal too.

## 📌 File Structure
• MainBanking.java → Entry point of the program.
• SimpleBankingSystem.java → Core logic (menus, account management, file handling).  
• Accounts.java → Account model (stores balance, transactions, account ID).
• Transaction.java → Transaction model (represents deposits/withdrawals with category and date).
• accounts.txt → Stores account data.
• transactions.txt → Stores transaction history.

## 📌 Visual Guide

The below is the main menu of the program, user can input either 1,2 or 3.

<img width="600" height="250" alt="image" src="https://github.com/user-attachments/assets/b5895e8b-39db-44f0-8629-5ad2a97676ed" />


To access the banking servcies, first an account will have to made. Once made remember the SBS ID and pick option 2.

<img width="650" height="300" alt="image" src="https://github.com/user-attachments/assets/f7e6885f-d30a-4450-b1e0-4002db0f04fb" />


From there, the user can access all servces related to the account. 

<img width="700" height="550" alt="image" src="https://github.com/user-attachments/assets/88e73817-625b-4f6a-93a6-1e589c8dd106" />


Once done, choose the sign-out option to exit the program.

## 📌 Future Improvements
In time I plan to further improve the capabilities of this project. A few options I am considering are:
1. Password Protection on accounts
2. Interest Calculations on balances
3. Exporting Reports (CSV/PDF)
4. Error Handling Improvements
5. Generating unique payment details for each user - which will be password protected.
