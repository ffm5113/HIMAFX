package model;

import java.io.Serializable;

public class Category implements Serializable{
    public String categoryName; // E.g., "Office supplies"
    public String categoryID; // E.g., "1"    
    public String userEmail; // E.g., "ffm5113@psu.edu" - The email associated with this item   
    
    public Category(String categoryName, String categoryID, // Constructor with all parameters
            String userEmail) {
        this.categoryName = categoryName;
        this.categoryID = categoryID;
        this.userEmail = userEmail;          
    }

    public String getCategoryID() { // Accessor method
        return categoryID;
    }

    public void setCategoryID(String categoryID) { // Mutator method
        this.categoryID = categoryID;
    }

    public String getCategoryName() { // Accessor method
        return categoryName;
    }

    public void setCategory(String categoryName) { // Mutator method
        this.categoryName = categoryName;
    }

    public String getUserEmail() { // Accessor method
        return userEmail;
    }

    public void setUserEmail(String userEmail) { // Mutator method
        this.userEmail = userEmail;
    }
    @Override
    public String toString() {
        String categoryAsString = "Category Name: " + getCategoryName() + "\n" +
                "Category ID: " + getCategoryID() + "\n" +
                "User name: " + getUserEmail();
        return categoryAsString;
    }
}
