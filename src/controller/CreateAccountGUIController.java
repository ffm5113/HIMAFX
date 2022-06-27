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
import java.util.regex.Pattern;
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
 *
 * @author forre
 */
public class CreateAccountGUIController implements Initializable {

    UserAccount userAccount; // Used to pass user account data to another controller
    ArrayList<UserAccount> userAccounts; // Used to pass user accounts data to another constructor
    boolean userExists; // Used to determine whether user exists when creating an account    
    SerializedDataCollection sDC; // Serialized data collection used to add new account to persistent data storage
    
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
    private TextField passwordTextField;
    @FXML
    private TextField confirmPasswordTextField;
    @FXML
    private CheckBox showPasswordCheckBox;
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
    private Button addButton;
    @FXML
    private Button confirmButton;
    @FXML
    private Label message;    
    @FXML
    private Button signOn;
    
    public CreateAccountGUIController() throws IOException{
        System.out.println("CreateAccountGUIController launched");
        sDC = new SerializedDataCollection();
        userAccounts = sDC.getUserAccounts().getUserAccountArrayList(); // User accounts set to serialized user account list
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            passwordTextField.setVisible(false); // Initialize visible password as hidden
            confirmPasswordTextField.setVisible(false); // Initialize visible confirm password as hidden   
            confirmButton.setDisable(true); // Initialize confirm button as locked
        });
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
    private void addButtonClicked() {
        message.setText("Select Confirm to finalize account creation.");
        refreshFormatting();
        unlockTextFields();
    }

    @FXML
    private void confirmButtonClicked() {
        System.out.println("Confirm button clicked");
        createAccount();
    }

    @FXML
    private void signOnButtonClicked() {
        System.out.println("Sign on button clicked");
        loadSignOnGUI();             
        // Get control of the create account stage        
        Stage stage = (Stage)addButton.getScene().getWindow();
        // Close the create account stage
        stage.close();     
    }
    
    @FXML
    private void confirmEntered(ActionEvent event) {
        if(confirmButton.isDisabled() == false){ // If confirm button is enabled, complete create account action when enter key is pressed in the text fields
            System.out.println("Confirm account entered (enter key)");
            createAccount();            
        }
    } // Else do nothing since confirm button is not visible (user needs to select Add first)
    
    public void lockTextFields(){
        // Lock text fields
        emailField.setDisable(true);        
        firstNameField.setDisable(true);
        lastNameField.setDisable(true);      
        passwordField.setDisable(true); 
        confirmPasswordField.setDisable(true);   
        // Special show password fields
        passwordTextField.setDisable(true);
        confirmPasswordTextField.setDisable(true); 
        confirmButton.setDisable(true); // Lock the confirm button whe the text fields are locked
    }
    
    public void unlockTextFields(){
        // Unlock text fields
        emailField.setDisable(false);        
        firstNameField.setDisable(false);
        lastNameField.setDisable(false);      
        passwordField.setDisable(false); 
        confirmPasswordField.setDisable(false); 
        // Special show password fields
        passwordTextField.setDisable(false);
        confirmPasswordTextField.setDisable(false);  
        confirmButton.setDisable(false); // Unlock the confirm button when the text fields are unlocked
    }
    private void loadSignOnGUI(){ // Load SignOnGUI.fxml file
        try {
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignOnGUI.fxml")); // Load FXML file
            AnchorPane signOn = loader.load(); // AnchorPane is equal to FXML file load
            // Get controller of fxml file  
            SignOnGUIController signOnController = loader.getController();
            signOnController.setUserAccount(userAccount); // Pass the user account to sign on 
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

    public boolean isUserExists() { // Accessor method
        return userExists;
    }

    public void setUserExists(boolean userExists) { // Mutator method
        this.userExists = userExists;
    }

    private void createAccount(){
        setUserExists(false);
        refreshFormatting();
        userAccount = new UserAccount(firstNameField.getText(), 
                lastNameField.getText(), emailField.getText(), 
                "Unassigned", confirmPasswordField.getText());        
        for(UserAccount u : userAccounts) { // Enhanced for loop to check for an email match
            if (u.getUserEmail().equals(userAccount.getUserEmail())) { // If email matches
                setUserExists(true); // Set exists boolean to true
                System.out.println("Account creation rejected. User email exists.");
                unlockTextFields(); // Unlock text fields so user can correct error
                message.setId("message2");
                message.setText("User account exists. Please choose a different email address.");                
                emailField.requestFocus();
                emailField.getStyleClass().add("text-field-error");
                break;
            }
        }        
        if(userExists == false) {
            // Code to update hidden/displayed password fields to match when confirm is selected                
            if(passwordField.isVisible()){
                passwordTextField.setText(passwordField.getText());
                confirmPasswordTextField.setText(confirmPasswordField.getText());
            }
            // Code to update hidden/displayed password fields to match when confirm is selected
            if(!passwordField.isVisible()) {
                passwordField.setText(passwordTextField.getText());   
                confirmPasswordField.setText(confirmPasswordTextField.getText());   
                userAccount.setPassword(confirmPasswordField.getText());
            } 
            // If passwords do not match
            if(!passwordField.getText().equals(confirmPasswordField.getText()) || 
                    !passwordTextField.getText().equals(confirmPasswordTextField.getText())) {
                passwordFieldError(); // Display error in password fields
                unlockTextFields(); // Unlock text fields so user can correct error
                message.setText("Passwords do not match. Please confirm the password."); // Display message
                System.out.println("Invalid password confirmation"); // Debugging
            }
            // If any of the fields are null
            else if(passwordField.getText().equals("") || confirmPasswordField.getText().equals("") || 
                            passwordTextField.getText().equals("") || confirmPasswordTextField.getText().equals("") ||
                            emailField.getText().equals("") || firstNameField.getText().equals("") || lastNameField.getText().equals("")) {
                allFieldsInvalid(); // Highlight all fields with red borders
                unlockTextFields(); // Unlock text fields so user can correct error
                message.setText("One or more fields are blank. Please fill all fields before saving.");
            }
            // If the email field is invalid
            else if(isValid(emailField.getText()) == false) { 
                System.out.println("Invalid email");
                unlockTextFields(); // Unlock text fields so user can correct error
                message.setId("message2");
                emailField.getStyleClass().add("text-field-error");          
                emailField.requestFocus(); // Focus on the email address field so the user can enter a valid address          
                message.setText("Please enter a valid email address such as \"Hello@HIMA.com\"");
            }
            // Email field is valid. Proceed with account creation
            else { 
                sDC.getUserAccounts().getUserAccountArrayList().add(userAccount); // Add user account to serialized user account list
                sDC.getUserAccounts().writeUserAccountListFile(); // Write the updated user account list to the serializable file
                userAccounts.add(userAccount); // Make the new user the current user account
                message.setText("Thank you, " + firstNameField.getText() + ". Your "
                + "account was successfully created.\n"
                + "Please select Sign On and log in with your new credentials.");
                signOn.requestFocus(); // Request focus on sign on button for user to easily navigate to sign on
                lockTextFields();
            }
        }
    }
    private static boolean isValid(String email) { // Check for valid email address entry
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$";                 
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
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
}
