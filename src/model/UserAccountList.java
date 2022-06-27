/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.io.*;

/**
 *
 * @author forre
 */
public class UserAccountList {
    public ArrayList<UserAccount> userAccountArrayList = new ArrayList<>(); // Array List of user accounts
    private String userAccountListFile = "userAccountListFile.ser"; // Serializable file name
    // Constructor
    public UserAccountList(){

        try {
        readUserAccountListFile();
        }
        catch(Exception ex) {
            System.out.println("User account list file not found but will be created.");
            
        }
        if(userAccountArrayList.isEmpty() || userAccountArrayList == null) {
            createUserAccountList();
            writeUserAccountListFile();
            readUserAccountListFile();
        }
        this.printUserAccountList();
        System.out.println("Total number of users: " + userAccountArrayList.size() + ".\n"); // For debugging
    }

    public void readUserAccountListFile() {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(userAccountListFile);
            in = new ObjectInputStream(fis);
            userAccountArrayList = (ArrayList)in.readObject();
            in.close();
        }catch(FileNotFoundException ex) {
            System.out.println("Create new user account list file.");
        }catch(IOException ex) {
            ex.printStackTrace();
        }catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        }        
    }
    
    public void createUserAccountList(){
       // Create test users
       UserAccount u1 = new UserAccount("Forrest", "Moulin", "ffm5113@psu.edu", "Unassigned", "password");
       UserAccount u2 = new UserAccount("Max", "Naughton", "mrn5199@psu.edu", "Unassigned", "password");
       UserAccount u3 = new UserAccount("Brianna", "Price", "bfp5184@psu.edu", "Unassigned", "password");
       UserAccount u4 = new UserAccount("Professor", "Lupoli", "svl27@psu.edu", "Unassigned", "password");
       UserAccount u5 = new UserAccount("testUser", "testUser", "testUser", "Unassigned", "testPass");
       // Add test users to the user account list
       userAccountArrayList.add(u1);
       userAccountArrayList.add(u2);
       userAccountArrayList.add(u3);
       userAccountArrayList.add(u4);
       userAccountArrayList.add(u5);
    }
    
    public void writeUserAccountListFile(){
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(userAccountListFile);
            out = new ObjectOutputStream(fos);
            out.writeObject(userAccountArrayList);
            out.close();
        }catch(IOException ex) {
            ex.printStackTrace();
        }         
    }
    
    public void printUserAccountList(){
//        System.out.println("The user list contains the following user accounts: \n");
//        for(int i = 0; i < userAccountArrayList.size(); i++) {
//            UserAccount currentUser = (UserAccount) userAccountArrayList.get(i);
//            System.out.println(currentUser.getUserEmail());
//        }        
//        System.out.println("\n");
        System.out.print(toString());        
    }
    
    @Override
    public String toString(){
        String userAccountListAsString = "The user list contains the following user accounts: \n\n";
        for(UserAccount u : userAccountArrayList) {
            userAccountListAsString += (u.toString() + "\n\n");          
        }
        return userAccountListAsString;
    }

    public ArrayList<UserAccount> getUserAccountArrayList() {
        return userAccountArrayList;
    }

    public void setUserAccountArrayList(ArrayList<UserAccount> userAccountList) {
        this.userAccountArrayList = userAccountList;
    }
}
