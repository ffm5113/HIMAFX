/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.InventoryGUIController.Item;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.SerializedDataCollection;
import model.UserAccount;

/**
 * FXML Controller class
 *
 * @author forre
 */


public class DashboardGUIController implements Initializable {

    UserAccount userAccount; // Current user account object  
    SerializedDataCollection sDC; // Provides access to stored serialized objects and allows new objects to be written to serializable file
    ArrayList<Item> userItemArrayList; // User's full list of items
    ArrayList<Item> searchResultsArrayList; // User's list of items that match dashboard search results
    
    @FXML
    private Label header;
    @FXML
    private Button categoriesButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button exportButton;
    @FXML
    private Button accountButton;
    @FXML
    private Button signOutButton;
    @FXML
    private TextField searchField;
    @FXML
    private Label message;
    @FXML
    private Button inventoryButton;

    /**
     * Initializes the controller class.
     */
    
    public DashboardGUIController(){ 
        Platform.runLater(() -> { // Avoid null exception
            System.out.println("DashboardGUIController launched"); // Debugging
            try {
                sDC = new SerializedDataCollection();
            } catch (IOException ex) {
                Logger.getLogger(DashboardGUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
            userItemArrayList = new ArrayList();
            // Add items with username attribute to the user item array list
            for(int i = 0; i < sDC.getItemList().getItemArrayList().size(); i++) {
                if(sDC.getItemList().getItemArrayList().get(i).getUserEmail().equals(userAccount.getUserEmail())) {
                    Item item = new Item(sDC.getItemList().getItemArrayList().get(i).getItemCategory(), 
                            sDC.getItemList().getItemArrayList().get(i).getItemName(), sDC.getItemList().getItemArrayList().get(i).getItemID(), 
                            sDC.getItemList().getItemArrayList().get(i).getItemLocation(), sDC.getItemList().getItemArrayList().get(i).getUserEmail());
                    userItemArrayList.add(item);
                }
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            Platform.runLater(() -> { // Avoid null email (allows parameter to be passed from SignOnGUIController)
                message.setText("Welcome, " + userAccount.getFirstName());
//                searchField.requestFocus(); // Request focus on search field (highlights the text for the user)
                header.requestFocus();
            });
    }  

    @FXML
    private void categoriesClicked() {
        System.out.println("Lists button clicked");  
        loadCategoriesGUI();
        closeStage();
    }

    @FXML
    private void searchClicked() {
        System.out.println("Search button clicked");    
        searchLogic();
    }

    @FXML
    private void exportClicked() {
        System.out.println("Export button clicked");   
        message.setText("Export feature is still being developed.");     
        System.out.println("Export button clicked");
        loadExportGUI(); //Load and display export GUI fxml file
        closeStage();
    }

    @FXML
    private void accountClicked() { // Account button logic
        System.out.println("Account button clicked");
        loadAccountGUI(); // Load and display account gui fxml file
        closeStage();
    }

    @FXML
    private void signOutClicked() { // Sign out button logic
        System.out.println("Sign out button clicked");
        loadSignOnGUI(); // Load and display sign on fxml file
        closeStage();
    }         

    @FXML
    private void searchTextEntered() { // Search text field logic (enter key)
        System.out.println("Search text entered (enter key)");        
        searchLogic();
    }
    
    @FXML
    private void inventoryClicked() {
        System.out.println("Inventory button clicked");  
        loadInventoryGUI();
        closeStage();
    }
    
    private void loadAccountGUI() { // Load AccountGUI.fxml file
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditAccountGUI.fxml"));
            Parent root = loader.load();
            // Get controller for the fxml file
            EditAccountGUIController accountGUIController = loader.getController();
            accountGUIController.setUserAccount(userAccount); // Pass the user account to the account gui
            //Show accountGUI in new window
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/view/himaCSS.css").toExternalForm()); // Load css file to be applied to fxml file            
            stage.setScene(scene);
            stage.setTitle("HIMA: Account Info");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EditAccountGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadSignOnGUI(){ // Load SignOnGUI.fxml file
        try {
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignOnGUI.fxml")); // Load FXML file
            AnchorPane signOn = loader.load(); // AnchorPane is equal to FXML file load
            // Get controller of fxml file  
            SignOnGUIController signOnController = loader.getController();
            signOnController.setUserAccount(userAccount); // Pass the user account to the sign on
            // Create new stage and scene
            Scene scene = new Scene(signOn);  
            scene.getStylesheets().add(getClass().getResource("/view/himaCSS.css").toExternalForm()); // Load CSS file to be applied to fxml file
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("HIMA: Sign On"); // Set title for Sign On stage (window)
            stage.show();        
        } catch (IOException ex) {
            Logger.getLogger(SignOnGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void loadInventoryGUI(){ // Load InventoryGUI.fxml file
        try {
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InventoryGUI.fxml")); // Load FXML file
            AnchorPane signOn = loader.load(); // AnchorPane is equal to FXML file load
            // Get controller of fxml file  
            InventoryGUIController inventoryGUIController = loader.getController();
            inventoryGUIController.setUserAccount(userAccount); // Pass the user account to the inventory gui
            searchResultsArrayList = new ArrayList(); // Set to null since not needed (used) in loadInventoryGUI method that requires this parameter
            inventoryGUIController.setFilteredUserItemArrayList(searchResultsArrayList); // Tells inventory GUI controller that there is no search list to be filtered since array list is empty (search not clicked)      
            Scene scene = new Scene(signOn);  
            Stage stage = new Stage();
            scene.getStylesheets().add(getClass().getResource("/view/himaCSS.css").toExternalForm()); // Load css file to be applied to fxml file            
            stage.setScene(scene);
            stage.setTitle("HIMA: Inventory Table"); // Set title for Lists Table stage (window) 
            stage.show();        
        } catch (IOException ex) {
            Logger.getLogger(InventoryGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }  
    
        public void loadInventoryGUI(ArrayList<Item> searchResultsArrayList){ // Load InventoryGUI.fxml file and only display items from search  
        try {
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InventoryGUI.fxml")); // Load FXML file
            AnchorPane signOn = loader.load(); // AnchorPane is equal to FXML file load
            // Get controller of fxml file  
            InventoryGUIController inventoryGUIController = loader.getController();
            inventoryGUIController.setUserAccount(userAccount); // Pass the user account to the inventory gui
            inventoryGUIController.setFilteredUserItemArrayList(searchResultsArrayList); // Tells inventory conroller to filter by the search results array list of items
            Scene scene = new Scene(signOn);  
            Stage stage = new Stage();
            scene.getStylesheets().add(getClass().getResource("/view/himaCSS.css").toExternalForm()); // Load css file to be applied to fxml file            
            stage.setScene(scene);
            stage.setTitle("HIMA: Inventory Table"); // Set title for Lists Table stage (window) 
            stage.show();        
        } catch (IOException ex) {
            Logger.getLogger(InventoryGUIController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private void loadExportGUI(){
        try{
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ExportGUI.fxml"));
            AnchorPane exportGUI = loader.load();
            // Get control of fxml file
            ExportGUIController exportGUIController = loader.getController();
            exportGUIController.setUserAccount(userAccount);
            // Show export GUI in new window
            Stage stage = new Stage();
            Scene scene = new Scene(exportGUI);
            scene.getStylesheets().add(getClass().getResource("/view/himaCSS.css").toExternalForm()); // Load css file to be applied to fxml file                        
            stage.setScene(scene);
            stage.setTitle("HIMA: Export");
            stage.show();            
        } catch(IOException ex){
            Logger.getLogger(ExportGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    public void searchLogic(){ // Logic used for search button and search entered (enter key)     
        searchResultsArrayList = new ArrayList();        
        if(searchField.getText().equals("Search Here :)") || searchField.getText().equals("")){ // If search field has default text (pseudo prompt text) or is blank
            message.setText("Please enter a keyword or item ID in the search field."); // Set message and take no action
            errorFormatting();
            searchField.requestFocus();
        }
        else { // User has entered text in the search text field
            for(int i = 0; i < userItemArrayList.size(); i++){ // For the array list of the user's items
                if(userItemArrayList.get(i).getItemCategory().contains(searchField.getText()) || // If item category or...
                        userItemArrayList.get(i).getItemName().contains(searchField.getText()) || // Item name or...
                        userItemArrayList.get(i).getItemID().contains(searchField.getText()) || // Item ID or...
                        userItemArrayList.get(i).getItemLocation().contains(searchField.getText())) { // Item location contains the string entered in the search field            
                    searchResultsArrayList.add(userItemArrayList.get(i)); // Add item that contains match to the search results array list of items    
                }
            }
            if(searchResultsArrayList.size() == 0) {
                errorFormatting();
                message.setText("No inventory search results found for \"" + searchField.getText() + "\".");
                System.out.println("No inventory search results found for \"" + searchField.getText() + "\".");
            }
            else if(searchResultsArrayList.size() == 1) {// One result found
//                message.setText("1 inventory search result found for \"" + searchField.getText() + "\".");
                System.out.println("1 inventory search result found for \"" + searchField.getText() + "\".");             
                loadInventoryGUI(searchResultsArrayList);
                closeStage();
            }
            else{ // More than one result found
//                message.setText(searchResultsArrayList.size() + " inventory search results found for \"" + searchField.getText() + "\".");                
                System.out.println(searchResultsArrayList.size() + " inventory search results found for \"" + searchField.getText() + "\".");
                loadInventoryGUI(searchResultsArrayList);
                closeStage();
            }
        }  
    }
    
    private void closeStage(){
        // Get control of the dashboard stage        
        Stage stage = (Stage)accountButton.getScene().getWindow();
        // Close the dashboard stage
        stage.close();           
    }

    public UserAccount getUserAccount() { // Accessor method
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) { // Mutator method
        this.userAccount = userAccount;
    }   
    
    private void refreshFormatting(){
        while(searchField.getStyleClass().contains("text-field-error")){
            searchField.getStyleClass().remove("text-field-error");            
        }
        message.setId("message");
    }
    
    private void errorFormatting(){
        searchField.getStyleClass().add("text-field-error");
        searchField.requestFocus(); // Highlight the search field so user can type in the text field
        message.setId("message2");
    }
}
