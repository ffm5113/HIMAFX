package model;

import java.io.Serializable;

public class Item implements Serializable{ // Allow for item data persisent via Serializable interface
    public String itemCategory; // E.g., "Office supplies"    
    public String itemID; // E.g., "1"
    public String itemName; // E.g., "Bluetooth keyboard"
    public String itemLocation; // E.g., "Office desk"
    public String userEmail; // E.g., "ffm5113@psu.edu"
    
    public Item (String itemCategory, String itemName, String itemID, String itemLocation, String userEmail) { // Constructor with all parameters
        this.itemCategory = itemCategory;        
        this.itemName = itemName;
        this.itemID = itemID;        
        this.itemLocation = itemLocation;
        this.userEmail = userEmail;
    }

    public String getItemID() { // Accessor method
        return itemID;
    }

    public void setItemID(String itemID) { // Mutator method
        this.itemID = itemID;
    }

    public String getItemName() { // Accessor method
        return itemName;
    }

    public void setItemName(String itemName) { // Mutator method
        this.itemName = itemName;
    }

    public String getItemLocation() { // Accessor method
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) { // Mutator method
        this.itemLocation = itemLocation;
    }

    public String getItemCategory() { // Accessor method
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) { // Mutator method
        this.itemCategory = itemCategory;
    }

    public String getUserEmail() { // Accessor method
        return userEmail;
    }
    
    public void setUserEmail(String userEmail){ // Mutator method
        this.userEmail = userEmail;
    }
    
    @Override
    public String toString() { // Returns item as a string
        String itemAsString = "";
        itemAsString += "Item Name: " + getItemID() + ", Item ID: " +
                getItemName() + "\nItem Location: " + getItemLocation()
                + ", Item Category: " + getItemCategory();
        return itemAsString;
    }
}
