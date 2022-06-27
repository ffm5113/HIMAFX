package model;

import java.io.Serializable;

public class UserAccount extends Person implements ManageData, Serializable{
    private String accountID; // E.g., "1"
    private String accountPassword; // E.g., "password"
    
    public UserAccount(String userFirstName, String userLastName, String userEmailAddress, // Constructor with all parameters
            String userAccountID, String userPassword){
        super(userFirstName, userLastName, userEmailAddress);
        this.accountID = userAccountID;
        this.accountPassword = userPassword;
    }
    
    public UserAccount(){} // No argument constructor

    public String getAccountID() { // Accessor method
        return accountID;
    }

    public void setAccountID(String accountID) { // Mutator method
        this.accountID = accountID;
    }

    public String getPassword() { // Accessor method
        return accountPassword;
    }

    public void setPassword(String password) { // Mutator method
        this.accountPassword = password;
    }
    
    public String hidePassword(){ // Method to hide password as string of '*' characters
        try{
            String xPassword = "";
            for (int i = 0; i < getPassword().length(); i++){
                xPassword += "*";
            }
            if (!"".equals(xPassword) && xPassword != null){
                return xPassword;
            }
            else {
                return "Password not found.";
            }
        }
        catch (NullPointerException nPE){
            return "Password not found.";
        }
    }
    
    @Override
    public String toString(){ // Returns user account as string
        String userAccountAsString = "";
        userAccountAsString += ("Email: " + this.getUserEmail() + "\nAccount ID: " + 
                this.getAccountID() + "\nName: " + 
                this.getFirstName() + " " +
                this.getLastName() + "\nPassword: " +
                this.hidePassword());
        return userAccountAsString;
    }

    @Override
    public void createAccount() { // Method from Manage Data interface
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean isNull () {
       boolean isNull = false;
        if(getUserEmail().equals("") &&
               getFirstName().equals("") &&
               getLastName().equals("") &&
               getPassword().equals("")) {
            isNull = true;
        }
        return isNull;       
    }
}
