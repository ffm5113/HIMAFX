/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Category;
import model.SerializedDataCollection;
import model.UserAccount;

/**
 *
 * @author forre
 */

public class InventoryGUIController implements Initializable{

    public UserAccount userAccount; // Current user account object
    public ArrayList<UserAccount> userAccounts; // Current user account list modeled after sdc user acount list
    public ObservableList<Item> observableItemList; // Observable list of items
    public ArrayList<Item> userItemArrayList = new ArrayList<>(); // Full array list of user's items
    public ArrayList<Item> filteredUserItemArrayList; // Filtered array list of user's items
    ArrayList<Category> categoryArrayList; // Array list of user's categories
    // TableView columns 
    private TableColumn<Item, String> itemCategoryColumn;    
    private TableColumn<Item, String> itemNameColumn;
    private TableColumn<Item, String> itemIDColumn;
    private TableColumn<Item, String> itemLocationColumn;
    private TableColumn<Item, String> userEmailColumn;    
    
    Label tableLabel = new Label(); // Table lable to use as placeholder for table view message when table is null
    ObservableList<String> observableOptions;
    ObservableList<String> dropDownOptions;
    ArrayList<String> options; // Used to fill category combo boxes
    SerializedDataCollection sDC; // Provides access to stored serialized objects and allows new objects to be written to serializable file
    
    @FXML
    private Label header;
    @FXML
    private Label message;
    @FXML
    private TableView<Item> inventoryTableView;
    @FXML
    private Button dashboardButton;
    @FXML
    private Label itemNameLabel;
    @FXML
    private Label itemIDLabel;
    @FXML
    private Label itemCategoryLabel;
    @FXML
    private Label itemLocationLabel;
    @FXML
    private Button addItemButton;
    @FXML
    private Button deleteItemButton;
    @FXML
    private TextField itemNameField;
    @FXML
    private TextField itemIDField;
    @FXML
    private TextField itemLocationField;
    @FXML
    private Button copyItemButton;
    @FXML
    private ComboBox<String> listDropDown;
    @FXML
    private ComboBox<String> addCategoryDropDown;
    @FXML
    private AnchorPane window;
    @FXML
    private Button clearFieldsButton;

    public InventoryGUIController(){
        Platform.runLater(() -> {
            try {
                sDC = new SerializedDataCollection();
            } catch (IOException ex) {
                Logger.getLogger(InventoryGUIController.class.getName()).log(Level.SEVERE, null, ex);
            }            setUserItemArrayList(new ArrayList<>()); 
           // Add items with username attribute to the user item array list
            for(int i = 0; i < sDC.getItemList().getItemArrayList().size(); i++) {
                if(sDC.getItemList().getItemArrayList().get(i).getUserEmail().equals(userAccount.getUserEmail())) {
                    Item item = new Item(sDC.getItemList().getItemArrayList().get(i).getItemCategory(), 
                            sDC.getItemList().getItemArrayList().get(i).getItemName(), sDC.getItemList().getItemArrayList().get(i).getItemID(), 
                            sDC.getItemList().getItemArrayList().get(i).getItemLocation(), sDC.getItemList().getItemArrayList().get(i).getUserEmail());
                    userItemArrayList.add(item);
                }
            }
            itemCategoryColumn = new TableColumn<>("Category");
            itemCategoryColumn.setMinWidth(158.1); // Used to alignn columns without the scroll bar appearing            
            itemNameColumn = new TableColumn<>("Item Name");
            itemNameColumn.setMinWidth(158.1);
            itemIDColumn = new TableColumn<>("Item ID");    
            itemIDColumn.setMinWidth(158.1);
            itemLocationColumn = new TableColumn<>("Item Location");     
            itemLocationColumn.setMinWidth(158.1);
            // Add columns to the table view
            inventoryTableView.getColumns().addAll(itemCategoryColumn, itemNameColumn, itemIDColumn, itemLocationColumn);       
            options = new ArrayList<>(); // Create new empty array list of strings
            for(int i = 0; i < sDC.getCategoryList().getCategoryArrayList().size(); i++) {
                if(sDC.getCategoryList().getCategoryArrayList().get(i).getUserEmail().equals(userAccount.getUserEmail())) { // If the category name on the list belongs to the user
                    options.add(sDC.getCategoryList().getCategoryArrayList().get(i).getCategoryName()); // Add category name to the drop down/combo box
                }
            }
        });
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            // Create new property value factories for the columns 
            itemCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("itemCategory"));            
            itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
            itemIDColumn.setCellValueFactory(new PropertyValueFactory<>("itemID"));
            itemLocationColumn.setCellValueFactory(new PropertyValueFactory<>("itemLocation"));
           
            if(!filteredUserItemArrayList.isEmpty()){ // Parameter passed from dashboard search contains items
                inventoryTableView.requestFocus();
                observableItemList = FXCollections.observableArrayList(filteredUserItemArrayList); // Observable list is set to the value of the user item array list
                inventoryTableView.setItems(observableItemList); // Observable list data loaded into table view                
                inventoryTableView.getSelectionModel().clearSelection(); // Clear the selection model in the table so the last item that was added to the table view is not selected when the table is initialized
                if(observableItemList.size() == 1){ // Singular match
                    message.setText(observableItemList.size() + " search results match is currently displayed in the inventory table."); 
                }
                else{ // Multiple matches
                    message.setText(observableItemList.size() + " search results matches are currently displayed in the inventory table.");
                }
            }
            else{ // Parameter passed from dashboard search does not contain any items (display the full inventory
                observableItemList = FXCollections.observableArrayList(userItemArrayList); // Observable list is set to the value of the user item array list
                inventoryTableView.setItems(observableItemList); // Observable list data loaded into table view
                addCategoryDropDown.requestFocus(); // Highlight drop down once the GUI is initialized 
            }
            inventoryTableView.setEditable(true); // Make table view editable (in progress)  
            tableLabel.setTextFill(Color.web("#1e4994")); // Blue font color for HIMA
            tableLabel.setFont(new Font("System", 16)); // Set table label font
            inventoryTableView.setPlaceholder(tableLabel); // Change text of table view if table is empty
            
            // Set prompt text for user to view examples in the text fields
            itemNameField.setPromptText("\"iPad Pro\"");
            itemIDField.setPromptText("\"1\"");
            itemLocationField.setPromptText("\"Office desk\"");
            
            observableOptions = FXCollections.observableArrayList(options); // Observable list is set to the value of the options array list       
            FXCollections.sort(observableOptions); // Sort drop down list in alphabetical order
            listDropDown.getItems().add("Full Inventory"); // Add Full Inventory option to the top of the drop down list    
            listDropDown.getItems().addAll(observableOptions); // Load observable options into list drop down
            
            dropDownOptions = FXCollections.observableArrayList(options); // Select category drop down options observable list set to value of options array list
            FXCollections.sort(dropDownOptions); // Sort drop down list in alphabetical order
            addCategoryDropDown.getItems().add("Create New Category"); // Add create new category as first selection          
            addCategoryDropDown.getItems().addAll(dropDownOptions); // Load drop down options into add category drop down       
        });    
    }

    @FXML
    private void addItemButtonClicked() {
        refreshFormatting();
        System.out.println("Add Item button clicked or enter key pressed");     
        // User text fields to add new item to the observable list (automatically updates the tableview)
        if(itemNameField.getText().equals("") ||
                addCategoryDropDown.getSelectionModel().isEmpty() ||
                itemIDField.getText().equals("") ||
                itemLocationField.getText().equals("")) {
            message.setId("message2"); // Set CSS Id to message2, which displays red text for errors instead of dark blue
            message.setText("One or more fields are blank. Please fill all fields before adding an item.");
            allFieldsInvalid();
            System.out.println("Item not added. One of more fields null.");
        }
        else {
            Item i = new Item(addCategoryDropDown.getValue(), // Create a new item from the text fields/category selection
                    itemNameField.getText(), itemIDField.getText(), 
                    itemLocationField.getText(), 
                    userAccount.getUserEmail());  
            model.Item mI = new model.Item(addCategoryDropDown.getValue(), // Create a new item from the text fields/category selection
                    itemNameField.getText(), itemIDField.getText(), 
                    itemLocationField.getText(), 
                    userAccount.getUserEmail());
            System.out.println("mI: " + mI.toString());
            sDC.getItemList().getItemArrayList().add(mI); // Add item to the serialized item list
            sDC.getItemList().writeItemListFile(); // Update the serialized item list file
            userItemArrayList.add(i); // Add to user specific list for all of user's items
            filteredUserItemArrayList.add(i); // Add to filtered user item array list for user's filtered items
            if(listDropDown.getSelectionModel().isEmpty()){
                observableItemList = FXCollections.observableArrayList(userItemArrayList); // Observable list is set to the value of the user item array list
                inventoryTableView.setItems(observableItemList); // Load updated observable list into the table view                
                listDropDown.setValue("Full Inventory");
            }
            else if(!listDropDown.getSelectionModel().isEmpty() && listDropDown.getValue().equals("Full Inventory")){
                observableItemList = FXCollections.observableArrayList(userItemArrayList); // Observable list is set to the value of the user item array list
                inventoryTableView.setItems(observableItemList); // Load updated observable list into the table view                                
            }
            else {
                observableItemList = FXCollections.observableArrayList(filteredUserItemArrayList); // Set item list to value of filtered user item array list
                inventoryTableView.setItems(observableItemList); // Set inventory table view to the value of item list                
            }
            message.setId("message"); // Set CSS Id to default dark blue font
            message.setText("\"" + itemNameField.getText() + "\" successfully added to your inventory.");
            System.out.println(itemNameField.getText() + " added to inventory for user account: " + userAccount.getUserEmail());
            clearTextFields();
            addCategoryDropDown.requestFocus();
        }
    }
    
    @FXML
    private void deleteButtonClicked() {
        System.out.println("Delete Item button clicked");         
        refreshFormatting();
        if(observableItemList.isEmpty()) { // If the observable list is empty
            message.setId("message2"); // Set CSS Id to message2, which displays red text for errors instead of dark blue
            inventoryTableView.getStyleClass().add("table-view-custom");  // Update table error formatting           
            inventoryTableView.getSelectionModel().clearSelection();
            inventoryTableView.requestFocus(); 
            message.setText("There are no items to remove from inventory.");
            System.out.println("There are no items to remove from inventory");
        }    
        else if(inventoryTableView.getSelectionModel().isEmpty()) { // No items are selected in the table
            message.setId("message2"); // Set CSS Id to message2, which displays red text for errors instead of dark blue
            inventoryTableView.getStyleClass().add("table-view-custom");  // Update table error formatting           
            inventoryTableView.getSelectionModel().clearSelection();
            inventoryTableView.requestFocus();           
            message.setText("Please select an item in the table, and select Delete to remove it from inventory.");
            System.out.println("No row selected");
        }              
        else{
            if(!message.getText().equals("Are you sure you would like to delete the selected item?\n"  // If the current message text is not the confirmation message
                    + "Click delete again to confirm.")){
                message.setText("Are you sure you would like to delete the selected item?\n" // Then set the message text and format for the confirmation message
                    + "Click delete again to confirm.");
                message.setId("message2");
            inventoryTableView.requestFocus(); // Focus on the table to highlight that an item is selected for deletion and to avoid the user accidentally clicked the delete button a second time without reading message
            }   
            else{
                refreshFormatting(); 
                message.setText("\"" + inventoryTableView.getSelectionModel().getSelectedItem().getItemName() + "\" removed from your inventory.");
                System.out.println(inventoryTableView.getSelectionModel().getSelectedItem().getItemName() + " removed from the inventory for user account: " + userAccount.getUserEmail());            
                Item i = inventoryTableView.getSelectionModel().getSelectedItem(); // Set item equal to item selected in table
                model.Item mI = new model.Item(i.getItemCategory(), i.getItemName(), i.getItemID(), i.getItemLocation(), i.getUserEmail());

                for(model.Item item : sDC.getItemList().getItemArrayList()) { // Enhanced for loop to check for an email match
                    if (item.getItemName().equals(mI.getItemName()) && // If selected item in table matches item in serialized item list
                        item.getItemCategory().equals(mI.getItemCategory()) &&
                        item.getItemID().equals(mI.getItemID()) &&
                        item.getItemLocation().equals(mI.getItemLocation()) &&
                        item.getUserEmail().equals(mI.getUserEmail())) {
                        int j = sDC.getItemList().getItemArrayList().indexOf(item); // Find index of the category
                        sDC.getItemList().getItemArrayList().remove(j); // Use index to remove category from serialized category list
                        sDC.getItemList().writeItemListFile(); // Update serialized item list file
                        break;
                    }    
                }            
                userItemArrayList.remove(i); // Remove the item at index i from the user array list
                if(!filteredUserItemArrayList.isEmpty()){
                    filteredUserItemArrayList.remove(i);
                }
                inventoryTableView.getItems().remove(inventoryTableView.getSelectionModel().getSelectedItem()); // Remove the same item from the table            
                if(observableItemList.isEmpty()){ // If observable list is empty
                    if(!listDropDown.getSelectionModel().isEmpty()){
                        if(!listDropDown.getSelectionModel().getSelectedItem().equals("Full Inventory")) { // If the filtered list is empty
                            tableLabel.setText("There are no longer items listed in the category: " + listDropDown.getSelectionModel().getSelectedItem());
                        }                
                        else{ // Full inventory is being displayed but is empty
                            tableLabel.setText("Your inventory is empty! Let's add some items :)");
                        }                    
                    }
                }
                inventoryTableView.getSelectionModel().clearSelection(); // Don't highlight the next item in the table
                inventoryTableView.requestFocus(); // Focus on the table to display the item removal        
            }
        }
    }
    
    @FXML
    private void copyItemButtonClicked() { // Copy selected item from table to text fields
        refreshFormatting();
        System.out.println("Copy Item button selected");
        if(inventoryTableView.getSelectionModel().isEmpty()) { // No items are selected in the table
            message.setId("message2"); // Set CSS Id to message2, which displays red text for errors instead of dark blue
            inventoryTableView.getStyleClass().add("table-view-custom");            
            inventoryTableView.getSelectionModel().clearSelection();
            inventoryTableView.requestFocus();            
            message. setText("Please select an item in the table, and click Copy Item to copy the selected item.");
            System.out.println("No row selected.");
        }     
        else { // Item is selected in table
            Item i = inventoryTableView.getSelectionModel().getSelectedItem();
            addCategoryDropDown.setValue(i.getItemCategory());
            itemNameField.setText(i.getItemName());
            itemIDField.setText(i.getItemID());
            itemLocationField.setText(i.getItemLocation());
            message.setId("message"); // Set CSS Id to default dark blue font
            message.setText(i.getItemName() + " copied. Make modifications, and select Add Item to add to inventory.");
            itemNameField.requestFocus(); // Focus on/highlight item name text field for greater usability
        }
    }
    @FXML
    private void dashboardButtonClicked() { // If dashboard button is clicked
        System.out.println("Dashboard button clicked");
        loadDashboard();
        closeStage();
    }
    @FXML
    private void dropDownSelected() {        
        refreshFormatting();
        filteredUserItemArrayList = new ArrayList<>(); // Ensures filtered list is cleared before new filter is applied
        if(listDropDown.getSelectionModel().getSelectedItem().equals("Full Inventory")){ // If full inventory is selected
            observableItemList = FXCollections.observableArrayList(userItemArrayList); // Load user item array list into observable item list
            inventoryTableView.setItems(observableItemList); // Set table view to the value of observable item list
            if(userItemArrayList.isEmpty()){ // User's items list is empty
                tableLabel.setText("Your inventory is empty! Let's add some items :)");
                message.setText("Let's get organized!");
            }
            else { // List is not empty
                message.setText("Displaying full inventory :)");
            }
        }
        else { // Category is selected (not full inventory)
            filteredUserItemArrayList = new ArrayList();
            message.setText("Filtering inventory by category: " + listDropDown.getSelectionModel().getSelectedItem());
            // For loop to add items from selected category to the filtered array list
            for(int i = 0; i < userItemArrayList.size(); i++) {
                if(userItemArrayList.get(i).getItemCategory().equals(listDropDown.getSelectionModel().getSelectedItem())){
                    filteredUserItemArrayList.add(userItemArrayList.get(i)); 
                }
            }
            observableItemList = FXCollections.observableArrayList(filteredUserItemArrayList); // Set observable item list to value of filtered user item array list
            inventoryTableView.setItems(observableItemList); // Set inventory table view to the value of observable item list
            if(filteredUserItemArrayList.isEmpty()) { // If the filtered list is empty
                tableLabel.setText("There are no inventory items for the category: " + listDropDown.getValue());
            }
        }
    }
    
    @FXML
    private void addCategoryDropDownSelected() {
        System.out.println("Category drop down selected");        
        if(addCategoryDropDown.getValue().equals("Create New Category")){ // If user selects Create New Category, load the lists gui
            loadCategoriesGUI();
            closeStage();
        }
    }

    @FXML
    private void clearFieldsClicked(ActionEvent event) {
        refreshFormatting();
        addCategoryDropDown.setValue("Select Category");
        itemNameField.setText("");
        itemIDField.setText("");
        itemLocationField.setText("");
        addCategoryDropDown.requestFocus();
        message.setText("Fields cleared. Select a category to beginning adding an item.");
    }
    
    public static class Item { // Used to load observable list of items into table view
        final SimpleStringProperty itemCategory; // E.g., "Office supplies"        
        final SimpleStringProperty itemName; // E.g., "Bluetooth keyboard"
        final SimpleStringProperty itemID; // E.g., "1"        
        final SimpleStringProperty itemLocation; // E.g., "Office desk"
        final SimpleStringProperty userEmail; // E.g., "ffm5113@psu.edu"
        
        public Item (String itemCategory, String itemName, String itemID, String itemLocation, String userEmail) { // Constructor with all parameters
            this.itemCategory = new SimpleStringProperty(itemCategory);
            this.itemName = new SimpleStringProperty(itemName);
            this.itemID = new SimpleStringProperty(itemID);            
            this.itemLocation = new SimpleStringProperty(itemLocation);
            this.userEmail = new SimpleStringProperty(userEmail);            
        }

        public String getItemID() { // Accessor method
            return itemID.get();
        }

        public void setItemID(String itemID) { // Mutator method
            this.itemID.set(itemID);
        }

        public String getItemName() { // Accessor method
            return itemName.get();
        }

        public void setItemName(String itemName) { // Mutator method
            this.itemName.set(itemName);
        }

        public String getItemLocation() { // Accessor method
            return itemLocation.get();
        }

        public void setItemLocation(String itemLocation) { // Mutator method
            this.itemLocation.set(itemLocation);
        }

        public String getItemCategory() { // Accessor method
            return itemCategory.get();
        }

        public void setItemCategory(String itemCategory) { // Mutator method
            this.itemCategory.set(itemCategory);
        }
        
        public String getUserEmail() { // Accessor method
            return userEmail.get();
        }
    
        public void setUserEmail(String userEmail){ // Mutator method
            this.userEmail.set(userEmail);
        } 
    }
    
    public void clearTextFields(){
        itemNameField.setText("");
        addCategoryDropDown.setValue("Select Category");
        itemIDField.setText("");
        itemLocationField.setText("");
        // Set prompt text to give the user examples
        itemNameField.setPromptText("\"iPad Pro\"");
        itemIDField.setPromptText("\"1\"");
        itemLocationField.setPromptText("\"Office desk\"");
    }
    private void loadDashboard() {
        try {
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardGUI.fxml")); // Load FXML file
            AnchorPane dashboard = loader.load(); // AnchorPane is equal to FXML file load
            // Get controller of dashboardGUIController
            DashboardGUIController dashboardGUIController = loader.getController();
            dashboardGUIController.setUserAccount(userAccount); // Pass the user account to the dashboard
            // Show dashboardGUI in new window
            Stage stage = new Stage();
            Scene scene = new Scene(dashboard);
            scene.getStylesheets().add(getClass().getResource("/view/himaCSS.css").toExternalForm()); // Load css file to be applied to fxml file            
            stage.setScene(scene);
            stage.setTitle("HIMA: Dashboard");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignOnGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadCategoriesGUI() {
        try {
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CategoriesGUI.fxml")); // Load FXML file
            AnchorPane categoriesGUI = loader.load(); // AnchorPane is equal to FXML file load
            // Get control of fxml file
            CategoriesGUIController categoriesGUIController = loader.getController();
            categoriesGUIController.setUserAccount(userAccount); // Pass user account to categories GUI
            // Show lists GUI in new window
            Stage stage = new Stage();
            Scene scene = new Scene(categoriesGUI);
            scene.getStylesheets().add(getClass().getResource("/view/himaCSS.css").toExternalForm()); // Load css file to be applied to fxml file            
            stage.setScene(scene);
            stage.setTitle("HIMA: Lists Table");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignOnGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public UserAccount getUserAccount() { // Accessor method
        return userAccount;
    }
    
    public void setUserAccount(UserAccount userAccount) { // Mutator method
        this.userAccount = userAccount;
    }

    public ArrayList<UserAccount> getUserAccounts() { // Accessor method
        return userAccounts;
    }
    
    public void setUserAccounts(ArrayList<UserAccount> userAccounts) { // Mutator method
        this.userAccounts = userAccounts;
    }  

    public ArrayList<Item> getUserItemArrayList() { // Accessor method
        return userItemArrayList;
    }

    public void setUserItemArrayList(ArrayList<Item> userItemArrayList) { // Mutator method
        this.userItemArrayList = userItemArrayList;
    }
    
    public ArrayList<Item> getFilteredUserItemArrayList() { // Accessor method
        return filteredUserItemArrayList;
    }

    public void setFilteredUserItemArrayList(ArrayList<Item> filteredUserItemArrayList) { // Mutator method
        this.filteredUserItemArrayList = filteredUserItemArrayList;
    }
    
    private void closeStage(){
        // Get control of the sign on stage
        Stage stage = (Stage)this.dashboardButton.getScene().getWindow();
        // Close the lists gui stage
        stage.close();          
    }
    
    private void refreshFormatting(){
        while(inventoryTableView.getStyleClass().contains("table-view-custom")){
            inventoryTableView.getStyleClass().remove("table-view-custom");
        }
        while(addCategoryDropDown.getStyleClass().contains("combo-box-custom")){
            addCategoryDropDown.getStyleClass().remove("combo-box-custom");
        }
        while(itemNameField.getStyleClass().contains("text-field-error")){
            itemNameField.getStyleClass().remove("text-field-error");
        }
        while(itemIDField.getStyleClass().contains("text-field-error")){
            itemIDField.getStyleClass().remove("text-field-error");
        }
        while(itemLocationField.getStyleClass().contains("text-field-error")){
            itemLocationField.getStyleClass().remove("text-field-error");
        }
        while(addCategoryDropDown.getStyleClass().contains("text-field-error")){
            addCategoryDropDown.getStyleClass().remove("text-field-error");   
        }
        message.setId("message");        
    }
    
    private void allFieldsInvalid(){
        addCategoryDropDown.getStyleClass().add("combo-box-custom");
        itemNameField.getStyleClass().add("text-field-error");
        itemIDField.getStyleClass().add("text-field-error");
        itemLocationField.getStyleClass().add("text-field-error");
        message.setId("message2");
        itemNameField.requestFocus();
    }
}
