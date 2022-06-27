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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
public class EditAccountGUIController implements Initializable {
    
    UserAccount userAccount; // Current user account object
    ArrayList<UserAccount> userAccounts; // Current user account list modeled after sdc user acount list    
    boolean userFound; // Boolean used to indicate whether user email is already in use
    SerializedDataCollection sDC; // Provides access to stored serialized objects and allows new objects to be written to serializable file
    
    @FXML
    private Label header;
    @FXML
    private TextField emailField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label emailLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button dashboardButton;
    @FXML
    private Label message;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField confirmPasswordTextField;
    @FXML
    private CheckBox showPasswordCheckBox;
    @FXML
    private Button saveButton;
    
    public EditAccountGUIController() throws IOException{ // Constructor
        System.out.println("AccoungGUIController launched"); // Debugging    
        setUserFound(false);
        sDC = new SerializedDataCollection();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> { // Avoid null values in text fields (allows parameter to be passed from DashboardController)
            // Lock text fields
            lockTextFields();
            passwordTextField.setVisible(false); // Initialize visible password as hidden
            confirmPasswordTextField.setVisible(false); // Initialize visible confirm password as hidden
            // Fill fields with user data
            emailField.setText(userAccount.getUserEmail());
            firstNameField.setText(userAccount.getFirstName());        
            lastNameField.setText(userAccount.getLastName());      
            passwordField.setText(userAccount.getPassword());      
            confirmPasswordField.setText(userAccount.getPassword());  
            editButton.requestFocus(); // Request focus on edit button
            saveButton.setDisable(true); // Initialize the save button as disabled until the edit button is clicked
        });      
    }    
    // Edit button logic
    @FXML
    private void editbuttonClicked() { 
        System.out.println("Edit button clicked"); 
        message.setText("Update your account information, and select Save All to confirm any changes.");
        unlockTextFields();
        emailField.requestFocus();
    }
    // Save all button logic
    @FXML
    private void saveButtonClicked() { 
        System.out.println("Save all button clicked"); 
        // If fields are locked, changes cannot be saved (must first select edit)
        if(emailField.isDisabled()) { 
            message.setText("Select Edit to update your account information.");
        }
        // Fields are unlocked, so changes can be saved and fields can be locked again
        else { 
            saveChangesLogic();
        }
    }

    @FXML
    private void dashboardButtonClicked() {
        System.out.println("Dashboard button clicked");
        loadDashboard(); // Load dashboard
        // Get control of the account GUI stage    
        Stage stage = (Stage)dashboardButton.getScene().getWindow();
        // Close the account GUI stage
        stage.close();        
    }

    void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @FXML
    private void showPasswordClicked() {
        if(showPasswordCheckBox.isSelected()){ // Show password check box is selected
            // Set visible passwords equal to hidden passwords
            passwordTextField.setText(passwordField.getText());
            confirmPasswordTextField.setText(confirmPasswordField.getText());   
            // Hide hidden password fields
            passwordField.setVisible(false);
            confirmPasswordField.setVisible(false);
            // Show visible password fields (actual text, not bullets)            
            passwordTextField.setVisible(true);
            confirmPasswordTextField.setVisible(true);
        }
        else { // Show password check box is deselected
            // Set hidden passwords equal to visible passwords            
            passwordField.setText(passwordTextField.getText());
            confirmPasswordField.setText(confirmPasswordTextField.getText());
            // Show hidden password fields            
            passwordField.setVisible(true);
            confirmPasswordField.setVisible(true);
            // Hide visible password fields (actual text, not bullets)                 
            passwordTextField.setVisible(false);
            confirmPasswordTextField.setVisible(false);           
        }
    }
    
    @FXML
    private void saveChanges() { // Enter key is pressed from any of the unlocked text fields
        System.out.println("Save entered (enter key pressed)");
        saveChangesLogic();
    }
    
    public UserAccount findUser(String emailAddress) {
    UserAccount u = new UserAccount();
    for(int i = 0; i < sDC.getUserAccounts().getUserAccountArrayList().size(); i++) { // 
        if(emailAddress.equals(sDC.getUserAccounts().getUserAccountArrayList().get(i).getUserEmail())) {
            u = sDC.getUserAccounts().getUserAccountArrayList().get(i); // Set u equal to the account at index i
            setUserFound(true);   
            System.out.println("User found " + u.toString()); // Print user to output for debugging            
            break;
        }
    }
    return u; // Return user with email address match (same value as account at index i)
    }

    public boolean isUserFound() {// Accessor method
        return userFound;
    }

    private void setUserFound(boolean userFound) {// Mutator method
        this.userFound = userFound;
    }
    
    public ArrayList<UserAccount> getUserAccounts() { // Accessor method
        return userAccounts;
    }
    
    public void setUserAccounts(ArrayList<UserAccount> userAccounts) { // Mutator method
        this.userAccounts = userAccounts;
    }
       
    public void lockTextFields(){
        // Lock text fields
        emailField.setDisable(true);        
        firstNameField.setDisable(true);
        lastNameField.setDisable(true);      
        passwordField.setDisable(true); 
        confirmPasswordField.setDisable(true);   
        // Disable special show password fields
        passwordTextField.setDisable(true);
        confirmPasswordTextField.setDisable(true);  
        saveButton.setDisable(true); // Lock the save button so the user cannot save account info without selecting Edit first
    }
    
    public void unlockTextFields(){
        // Unlock text fields
        emailField.setDisable(false);        
        firstNameField.setDisable(false);
        lastNameField.setDisable(false);      
        passwordField.setDisable(false); 
        confirmPasswordField.setDisable(false); 
        // Enable special show password fields
        passwordTextField.setDisable(false);
        confirmPasswordTextField.setDisable(false);  
        saveButton.setDisable(false); // Unlock the save button so the user can save account info        
    }
    
    public void authenticateEditAccount(){
        setUserFound(false); // Set user found to false to begin method
        UserAccount user = new UserAccount(firstNameField.getText(), lastNameField.getText(), emailField.getText(), "",  confirmPasswordField.getText());
        if(!passwordField.isVisible()){ // If bullet character password is hidden
            confirmPasswordField.setText(confirmPasswordTextField.getText()); // Set hidden password equal to visible password
            passwordField.setText(passwordTextField.getText());
            user.setPassword(confirmPasswordTextField.getText()); // Update this value in the new user instance in case the user changed the visible password
        }        
        else if(passwordField.isVisible()) { // If the bullet character password is visible
            confirmPasswordTextField.setText(confirmPasswordField.getText());
            passwordTextField.setText(confirmPasswordTextField.getText());            
            user.setPassword(confirmPasswordField.getText()); // Update this value in the new user instance in case the user changed the visible password
        }
        for(UserAccount u : sDC.getUserAccounts().getUserAccountArrayList()) { // Passwords match. Proceed with for loop to check for existing user 
            if (u.getUserEmail().equals(user.getUserEmail()) // If email address in text field matches email address for account in array list
                    && !userAccount.getUserEmail().equals(user.getUserEmail())) { // And if the current user email address (from sign on) is not one of the matches (since we are looking for matches with other accounts)    
                message.setId("message2");
                message.setText("User exists. Please choose a different email address.");
                emailField.requestFocus();
                emailField.getStyleClass().add("text-field-error");
                setUserFound(true); // Set user exists boolean to true 
                break; // Exit for loop if email address is registered to another account
            }
            else if(userAccount.getUserEmail().equals(user.getUserEmail()) && // If user confirms, but no changes were made
                    userAccount.getFirstName().equals(user.getFirstName()) &&
                            userAccount.getLastName().equals(user.getLastName()) &&
                    userAccount.getPassword().equals(user.getPassword())) {
                message.setText("Your account information was saved (No changes were made).");
                dashboardButton.requestFocus();
                lockTextFields(); // Lock text fields
                setUserFound(true); // Set authenticated boolean to true (since the user entered already exists)
            }
        }
    }
    private void loadDashboard() {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardGUI.fxml")); // Load FXML file
            AnchorPane dashboard = loader.load(); // AnchorPane is equal to FXML file load
            //Get controller of dashboardGUIController
            DashboardGUIController dashboardGUIController = loader.getController();
            System.out.println("User Account email: " + userAccount.getUserEmail());
            dashboardGUIController.setUserAccount(userAccount); // Pass the user account to dashbord
            //Show dashboardGUI in new window
            Scene scene = new Scene(dashboard);
            Stage stage = new Stage();
            scene.getStylesheets().add(getClass().getResource("/view/himaCSS.css").toExternalForm()); // Load css file to be applied to fxml file            
            stage.setScene(scene);
            stage.setTitle("HIMA: Dashboard");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignOnGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void saveChangesLogic(){
        refreshFormatting();               
        if(!passwordField.getText().equals(confirmPasswordField.getText()) || // If passwords do not match
                            !passwordTextField.getText().equals(confirmPasswordTextField.getText())) {
            message.setText("Passwords do not match. Please confirm the password."); // Display message
            passwordFieldError();
            System.out.println("Invalid password confirmation"); // Debugging
        }          
        else { // Passwords match. Proceed with authentication
            // Code to update hidden/displayed password fields to match when save all is selected
            if(passwordField.isVisible()){
                passwordTextField.setText(passwordField.getText());
                confirmPasswordTextField.setText(confirmPasswordField.getText());
            }
            if(!passwordField.isVisible()) {
                passwordField.setText(passwordTextField.getText());   
                confirmPasswordField.setText(confirmPasswordTextField.getText());            
            }             
            authenticateEditAccount();// Authenticate account edit request
            if(userFound == false) { // If user is not found in the serialized user account list
                if(passwordField.getText().equals("") || confirmPasswordField.getText().equals("") || // If any of fields are null
                        passwordTextField.getText().equals("") || confirmPasswordTextField.getText().equals("") ||
                        emailField.getText().equals("") || firstNameField.getText().equals("") || lastNameField.getText().equals("")) {
                    allFieldsInvalid();
                    message.setText("One of more fields are blank. Please fill all fields before saving.");
                }
                else { // Fields are filled. Proceed with updating the user account in the observable list
                    // Update account data here
                    // Update user's items with the new email address that was entered
                    for(int i = 0; i < sDC.getItemList().getItemArrayList().size(); i++) {
                        if(sDC.getItemList().getItemArrayList().get(i).getUserEmail().equals(userAccount.getUserEmail())) {
                            sDC.getItemList().getItemArrayList().get(i).setUserEmail(emailField.getText());
                        }
                    } 
                    // Update user's categories with the new email address that was entered
                    for(int i = 0; i < sDC.getCategoryList().getCategoryArrayList().size(); i++){
                        if(sDC.getCategoryList().getCategoryArrayList().get(i).getUserEmail().equals(userAccount.getUserEmail())) {
                            sDC.getCategoryList().getCategoryArrayList().get(i).setUserEmail(emailField.getText());                            
                        }
                    }
                    int index = sDC.getUserAccounts().getUserAccountArrayList().indexOf(findUser(userAccount.getUserEmail())); // Int index in serialized list of user accounts where the account equals userAccount
                    sDC.getUserAccounts().getUserAccountArrayList().get(index).setUserEmail(emailField.getText()); // Set the new email
                    sDC.getUserAccounts().getUserAccountArrayList().get(index).setFirstName(firstNameField.getText()); // Set the new first name       
                    sDC.getUserAccounts().getUserAccountArrayList().get(index).setLastName(lastNameField.getText()); // Set the new last name          
                    sDC.getUserAccounts().getUserAccountArrayList().get(index).setPassword(confirmPasswordField.getText()); // Set the new password name       
                    sDC.getUserAccounts().writeUserAccountListFile(); // Update the serialized user account list file
                    sDC.getCategoryList().writeCategoryListFile(); // Update the serialized category list file 
                    sDC.getItemList().writeItemListFile(); // Update the serialized item list file
                    userAccount = sDC.getUserAccounts().getUserAccountArrayList().get(index);
                    message.setText("Thank you, " + userAccount.getFirstName() + ". Your account changes have been saved."); // Display message
                    System.out.println("Account updated:\n" + userAccount.toString()); // Print new account data to output for debugging
                    // Lock text fields
                    lockTextFields();    
                    dashboardButton.requestFocus();
                }
            }
        }      
    }
    private void refreshFormatting(){ // Remove all error message styles
        message.setId("message");
        while(passwordField.getStyleClass().contains("text-field-error")) { // While the text field contains the CSS selector for text field errors
            passwordField.getStyleClass().remove("text-field-error"); // Remove text field error CSS selector that highlights text field in red
        }
        while(confirmPasswordTextField.getStyleClass().contains("text-field-error")) { // While the text field contains the CSS selector for text field errors
            confirmPasswordTextField.getStyleClass().remove("text-field-error"); // Remove text field error CSS selector that highlights text field in red
        }              
        while(passwordTextField.getStyleClass().contains("text-field-error")) { // While the text field contains the CSS selector for text field errors
            passwordTextField.getStyleClass().remove("text-field-error"); // Remove text field error CSS selector that highlights text field in red
        }   
        while(confirmPasswordField.getStyleClass().contains("text-field-error")) { // While the text field contains the CSS selector for text field errors
            confirmPasswordField.getStyleClass().remove("text-field-error"); // Remove text field error CSS selector that highlights text field in red
        }              
        while(emailField.getStyleClass().contains("text-field-error")){ // While the email field contains the CSS selector for text field errors
            emailField.getStyleClass().remove("text-field-error"); // Remove text field error CSS selector that highlights text field in red                            
        }
        while(firstNameField.getStyleClass().contains("text-field-error")){ // While the firstNameField contains the CSS selector for text field errors
            firstNameField.getStyleClass().remove("text-field-error"); // Remove text field error CSS selector that highlights firstNameField in red                            
        }
        while(lastNameField.getStyleClass().contains("text-field-error")){ // While the lastNameField contains the CSS selector for text field errors
            lastNameField.getStyleClass().remove("text-field-error"); // Remove text field error CSS selector that highlights lastNameField in red                            
        }  
    }
    private void passwordFieldError(){ // Add error styles for password error
        passwordField.getStyleClass().add("text-field-error");
        confirmPasswordField.getStyleClass().add("text-field-error");
        passwordTextField.getStyleClass().add("text-field-error");
        confirmPasswordTextField.getStyleClass().add("text-field-error");   
        if(passwordField.isVisible()){
            passwordField.requestFocus();            
        }
        else {
            passwordTextField.requestFocus();            
        }
        message.setId("message2");
    }        
    private void allFieldsInvalid(){ // Highlight all text fields with red borders and set message font to red
        passwordField.getStyleClass().add("text-field-error");
        confirmPasswordField.getStyleClass().add("text-field-error");
        passwordTextField.getStyleClass().add("text-field-error");   
        confirmPasswordTextField.getStyleClass().add("text-field-error");
        emailField.getStyleClass().add("text-field-error");
        firstNameField.getStyleClass().add("text-field-error");
        lastNameField.getStyleClass().add("text-field-error");
        message.setId("message2");
    }        
}
