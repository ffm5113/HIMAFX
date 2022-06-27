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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.SerializedDataCollection;
import model.UserAccount;

/**
 *
 * @author forre
 */
public class CategoriesGUIController implements Initializable {
    
    UserAccount userAccount; // Current user account object
    ArrayList<Category> fullCategoryArrayList; // Array fullCategoryArrayList of categories (all users)
    ArrayList<Category> usersCategoryArrayList; // Array usersCategoryArrayList of categories (belonging to user)
    ObservableList<Category> observableCategoryList; // Observable list of categories
    // Table columns
    private TableColumn<Category, String> categoryNameColumn;
    private TableColumn<Category, String> categoryIDColumn;
    private TableColumn<Category, String> userNameColumn;
    Label tableLabel = new Label(); // Table lable to use as placeholder for table view message when table is null
    SerializedDataCollection sDC; // Provides access to stored serialized objects and allows new objects to be written to serializable file

    @FXML
    private Label header;
    @FXML
    private Label message;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label categoryIDLabel;
    @FXML
    private Button addCategoryButton;
    @FXML
    private Button deleteItemButton;
    @FXML
    private Button dashboardButton;
    @FXML
    private TableView<Category> categoryTableView;
    @FXML
    private TextField categoryNameField;
    @FXML
    private TextField categoryIDField;
    @FXML
    private Button inventoryButton;
    
    public CategoriesGUIController(){
        Platform.runLater(() -> {
            try {
                sDC = new SerializedDataCollection();
            } catch (IOException ex) {
                Logger.getLogger(InventoryGUIController.class.getName()).log(Level.SEVERE, null, ex);
            }            
            setUserCategoryArrayList(new ArrayList<>());
            if(!sDC.getCategoryList().getCategoryArrayList().isEmpty()){
                for(int i = 0; i < sDC.getCategoryList().getCategoryArrayList().size(); i++) { // For loop to check full fullCategoryArrayList of categories
                    if(sDC.getCategoryList().getCategoryArrayList().get(i).getUserEmail().equals(userAccount.getUserEmail())) { // If the email address in the fullCategoryArrayList matches the current user account email address
                        Category c = new Category(sDC.getCategoryList().getCategoryArrayList().get(i).getCategoryName(), 
                                sDC.getCategoryList().getCategoryArrayList().get(i).getCategoryID(), sDC.getCategoryList().getCategoryArrayList().get(i).getUserEmail());
                        usersCategoryArrayList.add(c); // Add this category list to the user's fullCategoryArrayList of categories
                    }
                }                
            }
            // Instantiate table columns
            categoryNameColumn = new TableColumn<>("Category Name");
            categoryNameColumn.setMinWidth(219);           
            categoryIDColumn = new TableColumn<>("Category ID");
            categoryIDColumn.setMinWidth(219);     
            categoryTableView.getColumns().addAll(categoryNameColumn, categoryIDColumn);
            // This column needs to be created for observable list to pull data into table properly, but it is hidden since it is not needed by user
            userNameColumn = new TableColumn<>("User Name");
            userNameColumn.setVisible(false);
        }); 
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {      
            // Assicn prorperty values to table columns
            categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
            categoryIDColumn.setCellValueFactory(new PropertyValueFactory<>("categoryID")); 
            userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName")); 
            // Set prompt text for text fields
            categoryNameField.setPromptText("\"Sports\"");    
            categoryIDField.setPromptText("\"2\"");    
            observableCategoryList = FXCollections.observableArrayList(usersCategoryArrayList); // Load user's fullCategoryArrayList of categories into the observable list
            categoryTableView.setEditable(true);
            categoryTableView.setItems(observableCategoryList); // Load observable list into the category table view
            tableLabel.setTextFill(Color.web("#1e4994")); // Blue font color for HIMA
            tableLabel.setFont(new Font("System", 16)); // Set table label font
            categoryTableView.setPlaceholder(tableLabel); // Add table label as placeholder for table view
            tableLabel.setText("There are currently no categories.");
        }); 
    }

    @FXML
    private void addButtonClicked(ActionEvent event) {
        refreshFormatting();
        System.out.println("Add Button clicked or enter key pressed");
        message.setId("message2");        
        if(categoryNameField.getText().equals("") && 
                categoryIDField.getText().equals("")){
            message.setText("Both fields are empty. Please fill the fields and select Add Category.");
            categoryNameField.getStyleClass().add("text-field-error");
            categoryIDField.getStyleClass().add("text-field-error");
            categoryNameField.requestFocus(); // Request focus on category name field so user can fill it 
        }
        else if(categoryNameField.getText().equals("")){ // If only category name field is null 
            message.setText("Category name is blank. Please fill the field and select Add Category.");
            categoryNameField.getStyleClass().add("text-field-error");
            categoryNameField.requestFocus(); // Request focus on category name field so user can fill it    
            }
        else if(categoryIDField.getText().equals("")){ // If only category ID field is null 
            message.setText("Category ID is blank. Please fill the field and select Add Category.");            
            categoryIDField.getStyleClass().add("text-field-error");
            categoryIDField.requestFocus(); // Request focus on category ID field so user can change the string value            
        }
        else if(listExists() == true) { // Category name is already listed
            message.setText("Category already exists. Please choose a different category name.");    
            categoryNameField.getStyleClass().add("text-field-error");
            categoryNameField.requestFocus(); // Request focus on category name field so user can change the string value            
        }
        else {
            Category l = new Category(categoryNameField.getText(), categoryIDField.getText(), userAccount.getUserEmail());
            model.Category c = new model.Category(categoryNameField.getText(), categoryIDField.getText(), userAccount.getUserEmail());
            sDC.getCategoryList().getCategoryArrayList().add(c); // Add the new category to the serialized data file
            sDC.getCategoryList().writeCategoryListFile(); // Update the serialized category list file
            usersCategoryArrayList.add(l);
            message.setText("\"" + l.getCategoryName() + "\" added to the category list.");
            message.setId("message");
            System.out.println(l.getCategoryName() + " added as a category for user account: " + userAccount.getUserEmail());
            observableCategoryList = FXCollections.observableArrayList(usersCategoryArrayList); // Observable list is set to the value of the user's array fullCategoryArrayList
            categoryTableView.setItems(observableCategoryList); // Load updated observable list into the table view 
            clearTextFields();
            categoryNameField.requestFocus();
        }
    }

    @FXML
    private void deleteButtonClicked(ActionEvent event) {
        System.out.println("Delete button clicked");    
        refreshFormatting();           
        if(observableCategoryList.isEmpty()) { // If the observable fullCategoryArrayList is empty
            message.setId("message2");  // Update error message formatting
            categoryTableView.getStyleClass().add("table-view-custom"); // Update table error formatting
            categoryTableView.requestFocus();
            categoryTableView.getSelectionModel().clearSelection(); // Clear table selection when table is focused
            message.setText("There are no categories to remove from the list.");
            System.out.println("There are categories to remove from the list.");
        }  
        else if(categoryTableView.getSelectionModel().isEmpty()) { // No lists are selected in the table
            message.setId("message2"); // Update error message formatting
            categoryTableView.getStyleClass().add("table-view-custom"); // Update table error formatting
            categoryTableView.requestFocus();  
            categoryTableView.getSelectionModel().clearSelection();
            message.setText("Please select a category row in the table,\nand click Delete to remove it from your category list.");
            System.out.println("No row selected");
        }         
        else {         
            if(!message.getText().equals("Are you sure you would like to delete the selected category?\n" // If the current message text is not the confirmation message
                    + "Click Delete again to confirm.")){
                message.setText("Are you sure you would like to delete the selected category?\n" // Then set the message text and format for the confirmation message
                    + "Click Delete again to confirm.");
                message.setId("message2");
                categoryTableView.requestFocus(); // Focus on the table to highlight that an item is selected for deletion and to avoid the user accidentally clicked the delete button a second time without reading message               
            }  
            else { // Category can be deleted from the observable fullCategoryArrayList/table view
                message.setText("\"" + categoryTableView.getSelectionModel().getSelectedItem().getCategoryName() + "\" removed from your category list.");            
                System.out.println(categoryTableView.getSelectionModel().getSelectedItem().getCategoryName() + " removed from the category list for user account: " + userAccount.getUserEmail());
                Category l = categoryTableView.getSelectionModel().getSelectedItem(); // Set category list equal to category selected in table
                for(model.Category cat : sDC.getCategoryList().getCategoryArrayList()) { // Enhanced for loop to check for an email match
                    if (cat.getCategoryName().equals(l.getCategoryName())) { // If category name matches
                        int i = sDC.getCategoryList().getCategoryArrayList().indexOf(cat); // Find index of the category
                        sDC.getCategoryList().getCategoryArrayList().remove(i); // Use index to remove category from serialized category list
                        sDC.getCategoryList().writeCategoryListFile();
                        break;
                    } 
                }
                usersCategoryArrayList.remove(l); // Remove the fullCategoryArrayList at index l from the users array list
                categoryTableView.getItems().remove(categoryTableView.getSelectionModel().getSelectedItem()); // Remove the same item from the table              
                categoryTableView.getSelectionModel().clearSelection();
                categoryTableView.requestFocus(); // Focus on the table to display the category removal
            }            
        }
    }

    @FXML
    private void dashboardButtonClicked(ActionEvent event) {
        System.out.println("Dashboard button clicked");  
        loadDashboard();
        closeStage();
    }
    
    @FXML
    private void inventoryButtonClicked(ActionEvent event) {
        System.out.println("Inventory button clicked");
        loadInventoryGUI();
        closeStage();
    }
    
    private void loadDashboard() {
        try {
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardGUI.fxml")); // Load FXML file
            AnchorPane dashboard = loader.load(); // AnchorPane is equal to FXML file load
            // Get controller of dashboardGUIController
            DashboardGUIController dashboardGUIController = loader.getController();
            dashboardGUIController.setUserAccount(userAccount); // Pass user account to dashboard
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
    private void loadInventoryGUI(){ // Load InventoryGUI.fxml file
        try {
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InventoryGUI.fxml")); // Load FXML file
            AnchorPane signOn = loader.load(); // AnchorPane is equal to FXML file load
            // Get controller of fxml file  
            InventoryGUIController inventoryGUIController = loader.getController();
            inventoryGUIController.setUserAccount(userAccount); // Pass user account back to inventory gui
            inventoryGUIController.setFilteredUserItemArrayList(new ArrayList<>()); // Tells inventory GUI controller that there is no search fullCategoryArrayList to be filtered
            Scene scene = new Scene(signOn);  
            Stage stage = new Stage();
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/view/himaCSS.css").toExternalForm());
            stage.setTitle("HIMA: Inventory Table"); // Set title for categories Table stage (window)
            stage.show();        
        } catch (IOException ex) {
            Logger.getLogger(InventoryGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }        
        
    private void clearTextFields(){
        categoryNameField.setText("");
        categoryIDField.setText("");
        // Set prompt text to give the user examples
        categoryNameField.setPromptText("\"Sports\"");    
        categoryIDField.setPromptText("\"2\"");     
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public ArrayList<Category> getUserCategoryArrayList() {
        return (ArrayList<Category>) usersCategoryArrayList;
    }

    public void setUserCategoryArrayList(ArrayList<Category> usersCategoryArrayList) {
        this.usersCategoryArrayList = usersCategoryArrayList;
    }    
    
    public static class Category{
        final SimpleStringProperty categoryName; // E.g., "Office supplies"
        final SimpleStringProperty categoryID; // E.g., "1"    
        final SimpleStringProperty userEmail; // E.g. "ffm5113@psu.edu" - The email associated with this item   

        public Category() {
            this.categoryName = new SimpleStringProperty("");
            this.categoryID = new SimpleStringProperty("");        
            this.userEmail = new SimpleStringProperty("");        
        }

        public Category(String categoryName, String categoryID, // Constructor with all parameters
                String userEmail) {
            this.categoryName = new SimpleStringProperty(categoryName);
            this.categoryID = new SimpleStringProperty(categoryID);
            this.userEmail = new SimpleStringProperty(userEmail);          
        }

        public String getCategoryID() { // Accessor method
            return categoryID.get();
        }

        public void setCategoryID(String categoryID) { // Mutator method
            this.categoryID.set(categoryID);
        }

        public String getCategoryName() { // Accessor method
            return categoryName.get();
        }

        public void setCategoryName(String categoryName) { // Mutator method
            this.categoryName.set(categoryName);
        }

        public String getUserEmail() { // Accessor method
            return userEmail.get();
        }

        public void setUserEmail(String userEmail) { // Mutator method
            this.userEmail.set(userEmail);
        }
    }
    
    private boolean listExists(){
        boolean listExists = false;
        for (int i = 0; i < usersCategoryArrayList.size(); i++) {
            if(categoryNameField.getText().equals(usersCategoryArrayList.get(i).getCategoryName())) { // Category name already exists
                listExists = true;
                break;
            }
        }
        return listExists;
    }
    
    private void closeStage(){
        // Get control of the categories GUI stage        
        Stage stage = (Stage)dashboardButton.getScene().getWindow();
        // Close the categories GUI stage
        stage.close();         
    }
    
    private void refreshFormatting(){ // Reset to default formatting
        while(categoryNameField.getStyleClass().contains("text-field-error")){
            categoryNameField.getStyleClass().remove("text-field-error");            
        }
        while(categoryIDField.getStyleClass().contains("text-field-error")){
            categoryIDField.getStyleClass().remove("text-field-error");            
        }  
        while(categoryTableView.getStyleClass().contains("table-view-custom")){
            categoryTableView.getStyleClass().remove("table-view-custom");
        }
        message.setId("message");
    }
}
