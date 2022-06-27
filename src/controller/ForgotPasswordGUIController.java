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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.SerializedDataCollection;
import model.UserAccount;

/**
 *
 * @author forre
 */
public class ForgotPasswordGUIController implements Initializable {

    UserAccount userAccount; // Current user account object
    ArrayList<UserAccount> userAccounts; // Current user account list modeled after sdc user acount list    
    SerializedDataCollection sDC; // Provides access to stored serialized objects and allows new objects to be written to serializable file
    
    @FXML
    private Label header;
    @FXML
    private TextField emailField;
    @FXML
    private Button resetPasswordButton;
    @FXML
    private Label message;
    @FXML
    private Button signOnButton;

    public ForgotPasswordGUIController() throws IOException{ // Debugging
        System.out.println("ForgotPasswordController launched");
        sDC = new SerializedDataCollection(); // Instantiate new serialized data collection used to update password in persistent data storage
    }    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            Platform.runLater(() -> { // Avoid null exception (allows parameter to be passed from another controller)
                emailField.requestFocus();
            });
    }      
    
    @FXML
    private void resetPasswordButtonClicked() { // Reset password button used instead of text field
        System.out.println("Reset Password button clicked");
        findUser();
    }
    
    @FXML
    private void resetEntered() { // Enter key used in text field instead of using the button
        System.out.println("Reset Password entered (enter key)");
        findUser();
    }

    @FXML
    private void signOnButtonClicked() {
        System.out.println("Sign On button clicked");
        loadSignOnGUI();
        // Get control of the forgot password stage
        Stage stage = (Stage) resetPasswordButton.getScene().getWindow();
        // Close the forgot password stage
        stage.close();        
    }

    public UserAccount getUserAccount() { // Accessor method
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) { // Mutator method
        this.userAccount = userAccount;
    }
    
    public void loadSignOnGUI(){ // Load SignOnGUI.fxml file
        try {
            // Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignOnGUI.fxml")); // Load FXML file
            AnchorPane signOn = loader.load(); // AnchorPane is equal to FXML file load
            // Get controller of fxml file  
            SignOnGUIController signOnController = loader.getController();
            signOnController.setUserAccount(userAccount); // Pass user account to sign on
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
    
    public void findUser(){
        if(emailField.getText().equals("")) { // If email address is blank
            message.setText("Email field is blank. Please enter a valid email address.");
            message.setId("message2");
            emailField.getStyleClass().add("text-field-error");
            emailField.requestFocus(); // Reuest focus on the email field to allow the user to fill the field easily
            System.out.println("Email field is blank. Unable to reset password");
        }
        else { // Email address is not blank
            for(UserAccount u : sDC.getUserAccounts().getUserAccountArrayList()) { // Enhanced for loop to check for an email match
                if (u.getUserEmail().equals(emailField.getText())) { // If email matches
                    System.out.println("User found for password reset");
                    u.setPassword("password"); // Reset user password to "password"
                    sDC.getUserAccounts().writeUserAccountListFile(); // Write the updated serialized user account list file
                    message.setId("message");
                    while(emailField.getStyleClass().contains("text-field-error")){
                        emailField.getStyleClass().remove("text-field-error");                        
                    }
                    message.setText("Your password has been reset. Please check your email inbox.");                    
                    signOnButton.requestFocus(); // Request focus on sign on button to allow user to easily return to sign on window
                    break;
                }
                else { // Email not found. Unable to reset password
                    System.out.println("User not found for password reset");
                    message.setId("message2");
                    emailField.getStyleClass().add("text-field-error");                        
                    emailField.requestFocus(); // Reuest focus on the email field to allow the user to fill the field easily                    
                    message.setText("The email address entered was not found. "
                            + "Please try again.");
                }
            }
        }
    } 
}
