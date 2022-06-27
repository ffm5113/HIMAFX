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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.SerializedDataCollection;
import model.UserAccount;

/**
 *
 * @author forre
 */
public class SignOnGUIController implements Initializable {
    
    public boolean userAuthenticated; // Boolean for user authentication method
    public UserAccount userAccount = new UserAccount(); // Current user account object
    public ArrayList<UserAccount> userAccounts = new ArrayList<>(); // Current user account list modeled after sdc user acount list   
    ObservableList<InventoryGUIController.Item> observableItemList = FXCollections.observableArrayList(); // Initialize item Category before passing to item controller.
    SerializedDataCollection sDC; // Provides access to stored serialized objects and allows new objects to be written to serializable file
    
    @FXML
    private Label header;
    @FXML
    private Label emailLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button signOnButton;
    @FXML
    private Button forgotPasswordButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private CheckBox passwordCheckBox;
    @FXML
    private PasswordField passwordField;
    @FXML 
    private TextField emailField;
    @FXML
    private Label message;
    @FXML
    private AnchorPane window;
    @FXML
    private ImageView himaImage;
    
    public SignOnGUIController(){
        Platform.runLater(() -> {
            System.out.println("SignOnController launched"); // Debugging
            userAuthenticated = false; // Set authentication to false        
            try {
                sDC = new SerializedDataCollection();
            } catch (IOException ex) {
                Logger.getLogger(SignOnGUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
            userAccounts = sDC.getUserAccounts().getUserAccountArrayList(); // User Accounts is set to serialized user accounts list
        });
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            passwordTextField.setVisible(false); // Hide password text field (part of show password function)             
        });
    }    
   
    @FXML
    private void signOnButtonHandled(){ // Sign on button is selected          
        System.out.println("Sign on button selected");
        signOnLogic();
    }
    
    @FXML
    public void textFieldEntered(ActionEvent event){ // Enter key is pressed from password text field (shown), password field (hidden), or email field
        System.out.println("One of the text fields was entered entered (enter key)");
        if(event.getSource().equals(emailField)){
        System.out.println("Text field identified: email field.");            
        }
        else if(event.getSource().equals(passwordField)){
        System.out.println("Text field identified: password field.");            
        }     
        else {
        System.out.println("Text field identified: password text field.");            
        }           
        signOnLogic();
    }
    
    @FXML
    private void forgotPasswordButtonHandled() { // Forgot password button is selected
        System.out.println("Forgot password selected");
        loadForgotPassword();
        closeStage();
    }
    @FXML
    private void createAccountButtonHandled() { // Create account button is selected
            System.out.println("Create account button selected");            
            loadCreateAccount();
            closeStage();
    }
    @FXML
    private void showPasswordHandled() { 
        if(passwordCheckBox.isSelected()) { // Show password check box selected    
            System.out.println("Show password box selected");      
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordTextField.setVisible(true);            
        }
        else { // Show password check box is deselected
            System.out.println("Show password box deselected");   
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordTextField.setVisible(false);             
        }
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
    private void loadCreateAccount() {
        try {
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateAccountGUI.fxml")); // Load FXML file
            AnchorPane createAccount = loader.load(); // AnchorPane is equal to FXML file load
            // Get controller of createAccountGUIController
            CreateAccountGUIController createAccountGUIController = loader.getController();
            createAccountGUIController.setUserAccount(userAccount); // Pass user account to create account controller
            // Show dashboardGUI in new window
            Stage stage = new Stage();
            Scene scene = new Scene(createAccount);
            scene.getStylesheets().add(getClass().getResource("/view/himaCSS.css").toExternalForm()); // Load css file to be applied to fxml file            
            stage.setScene(scene);
            stage.setTitle("HIMA: Create Account");
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(SignOnGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    private void loadForgotPassword() {
        try {
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ForgotPasswordGUI.fxml")); // Load FXML file
            AnchorPane createAccount = loader.load(); // AnchorPane is equal to FXML file load
            // Get controller of forgotPasswordController
            ForgotPasswordGUIController forgotPasswordController = loader.getController();
            forgotPasswordController.setUserAccount(userAccount);// Pass user account to forgot password controller
            // Show ForgotPasswordGUI in new window
            Stage stage = new Stage();
            Scene scene = new Scene(createAccount);
            scene.getStylesheets().add(getClass().getResource("/view/himaCSS.css").toExternalForm()); // Load css file to be applied to fxml file            
            stage.setScene(scene);
            stage.setTitle("HIMA: Forgot Password");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignOnGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void signOnLogic(){
        if(passwordField.isVisible()) { // Password text is hidden
            setUserAccount(new UserAccount("", "", emailField.getText(), "",  passwordField.getText()));  
            passwordTextField.setText(passwordField.getText()); // Set hidden password equal to displayed password
        }
        else { // Password text is visible
            setUserAccount(new UserAccount("", "", emailField.getText(), "", passwordTextField.getText()));
            passwordField.setText(passwordTextField.getText()); // Set hidden password equal to displayed password
        }        
        if(emailField.getText().equals("") && !passwordField.getText().equals("")){ // Only email is null
            refreshFormatting(); // Refresh formatting 
            message.setId("message2"); // Set CSS Id to message2, which displays red text for errors instead of dark blue
            message.setText("Email field is empty. Please fill this field and try again.");
            emailField.getStyleClass().add("text-field-error"); // Add text field error CSS selector that highlights email field in red
            emailField.requestFocus(); // Request focus on email field for ease of use
        }
        else if(passwordField.getText().equals("") && !emailField.getText().equals("")) { // Only password is null
            refreshFormatting(); // Refresh formatting             
            message.setId("message2"); // Set CSS Id to message2, which displays red text for errors instead of dark blue
            message.setText("Password field is empty. Please fill this field and try again.");
            passwordField.getStyleClass().add("text-field-error"); // Add text field error CSS selector that highlights text field in red
            passwordTextField.getStyleClass().add("text-field-error"); // Add text field error CSS selector that highlights text field in red                
            if(passwordField.isVisible()){ // If password characters are hidden
                passwordField.requestFocus(); // Request focus on password field for ease of use
            }
            else{ // Password characters are not hidden 
                passwordTextField.requestFocus(); // Request focus on password text field (characters not hidden) for ease of use
            }
        }
        else if (emailField.getText().equals("") && passwordField.getText().equals("")){ // Both email and password fields are null
            allFieldsInvalid(); // See method below            
            message.setText("Both email and password fields are empty. Please fill these fields and try again.");
        }
        else{ // Both email and password fields are filled. Proceed with authentication
            authenticateUser(); // See method above
            if(isUserAuthenticated() == false) { // User is not authenticated
                System.out.println("User not authenticated");
                allFieldsInvalid(); // See method below
                message.setText("Invalid email/password combination. Please try again.");                
            }
            else{ // User is authenticated             
                loadDashboard(); // Load the dashboard FXML file    
                closeStage(); // See method below
            }
        }        
    }
    
        public void authenticateUser(){
        setUserAuthenticated(false);
        for(UserAccount u : userAccounts) { // Enhanced for loop to check for a password and email match
            if ((u.getUserEmail().equals(userAccount.getUserEmail()) // If email and password match
                && u.getPassword().equals(userAccount.getPassword()))) {
                setUserAuthenticated(true); // Set authenticated boolean to true
                setUserAccount(u);
                System.out.println(userAccount.toString());
                System.out.println(userAccount.getUserEmail()+ " authenticated :)\n");
                break;
            }
        }
    }  
    
    private void closeStage(){
        // Get control of the sign on stage
        Stage stage = (Stage)signOnButton.getScene().getWindow();
        // Close the sign on stage
        stage.close();          
    }
    
    private void allFieldsInvalid(){
        passwordField.getStyleClass().add("text-field-error"); // Add text field error CSS selector that highlights text field in red
        passwordTextField.getStyleClass().add("text-field-error"); // Add text field error CSS selector that highlights text field in red                            
        emailField.getStyleClass().add("text-field-error"); // Add text field error CSS selector that highlights email field in red            
        emailField.requestFocus(); // Request focus in email field for ease of use
        message.setId("message2");
    }
    
    private void refreshFormatting(){
        while(passwordField.getStyleClass().contains("text-field-error")) { // While the text field contains the CSS selector for text field errors
            passwordField.getStyleClass().remove("text-field-error"); // Remove text field error CSS selector that highlights text field in red
        }
        while(passwordTextField.getStyleClass().contains("text-field-error")) { // While the text field contains the CSS selector for text field errors
            passwordTextField.getStyleClass().remove("text-field-error"); // Remove text field error CSS selector that highlights text field in red
        }                        
        while(emailField.getStyleClass().contains("text-field-error")){ // While the email field contains the CSS selector for text field errors
            emailField.getStyleClass().remove("text-field-error"); // Remove text field error CSS selector that highlights text field in red                            
        }
        message.setId("message"); // Set message CSS style to message Id selector
    }
        
    public boolean isUserAuthenticated() { // Accessor method
        return userAuthenticated;
    }
    
    public void setUserAuthenticated(boolean userExists) { // Mutator method
        this.userAuthenticated = userExists;
    } 
    
    public ArrayList<UserAccount> getUserAccounts() { // Accessor method
        return userAccounts;
    }
    
    public void setUserAccounts(ArrayList<UserAccount> userAccounts) { // Mutator method
        this.userAccounts = userAccounts;
    }

    public ObservableList<InventoryGUIController.Item> getItemList() { // Accessor method
        return observableItemList;
    }

    public void setItemList(ObservableList<InventoryGUIController.Item> itemList) { // Mutator method
        this.observableItemList = itemList;
    }

    public UserAccount getUserAccount() { // Accessor method
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) { // Mutator method
        this.userAccount = userAccount;
    }
}
