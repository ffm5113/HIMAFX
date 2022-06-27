package model;

import java.io.IOException;
import java.io.Serializable;

public class SerializedDataCollection implements Serializable{
    UserAccountList userAccounts;
    ItemList itemList;
    CategoryList categoryList;
    // Constructor
    public SerializedDataCollection () throws IOException{
        userAccounts = new UserAccountList();
        itemList = new ItemList();
        categoryList = new CategoryList();
    }

    public UserAccountList getUserAccounts() { // Accessor method
        return userAccounts;
    }

    public void setUserAccounts(UserAccountList userAccounts) { // Mutator method
        this.userAccounts = userAccounts;
    }

    public ItemList getItemList() { // Accessor method
        return itemList;
    }

    public void setItemList(ItemList itemList) { // Mutator method
        this.itemList = itemList;
    }

    public CategoryList getCategoryList() { // Accessor method
        return categoryList;
    }

    public void setCategoryList(CategoryList categoryList) { // Mutator method
        this.categoryList = categoryList;
    }
}
