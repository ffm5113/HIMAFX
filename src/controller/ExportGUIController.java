/*
public class CheckBoxTableCell<S,​T>
extends TableCell<S,​T>
A class containing a TableCell implementation that draws a CheckBox node inside the cell, optionally with a label to indicate what the checkbox represents.
By default, the CheckBoxTableCell is rendered with a CheckBox centred in the TableColumn. If a label is required, it is necessary to provide a non-null StringConverter instance to the CheckBoxTableCell(Callback, StringConverter) constructor.
To construct an instance of this class, it is necessary to provide a Callback that, given an object of type T, will return an ObservableProperty<Boolean> that represents whether the given item is selected or not. This ObservableValue will be bound bidirectionally (meaning that the CheckBox in the cell will set/unset this property based on user interactions, and the CheckBox will reflect the state of the ObservableValue, if it changes externally).
Note that the CheckBoxTableCell renders the CheckBox 'live', meaning that the CheckBox is always interactive and can be directly toggled by the user. This means that it is not necessary that the cell enter its editing state (usually by the user double-clicking on the cell). A side-effect of this is that the usual editing callbacks (such as on edit commit) will not be called. If you want to be notified of changes, it is recommended to directly observe the boolean properties that are manipulated by the CheckBox.
 */
package controller;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Category;
import model.SerializedDataCollection;
import model.UserAccount;


/**
 * FXML Controller class
 *
 * @author maxrn
 */
public class ExportGUIController implements Initializable {
    
    public UserAccount userAccount;// UserAccount model class object.

    public ArrayList<UserAccount> userAccounts;// ArrayList containing UserAccount info. Email important.

    public ArrayList<Category> list;

    public ArrayList<Category> usersList;

    public ObservableList<Category> categoryList;// ObservableList containing Category details. Related to UserAccount info.

    public ArrayList<Category> userCategoryArrayList = new ArrayList<>();// new ArrayList to hold users categories
    
    private TableColumn<Category, String> categoryNameColumn;

    private TableColumn<Category, Boolean> pdfExportColumn;

    private TableColumn<Category, Boolean> txtExportColumn;

    private TableColumn<Category, String> userNameColumn;
    
    SerializedDataCollection sdc;// Serialized Data Collection object.
    

    @FXML
    private Label header;
    @FXML
    private Button dashboardButton;
    @FXML
    private TableView<Category> exportTableView;
    @FXML
    private Button exportButton;
    @FXML
    private Label message;

    
    public ExportGUIController(){
        Platform.runLater(() -> {
            try {
                sdc = new SerializedDataCollection();
            } catch (IOException ex) {
               Logger.getLogger(ExportGUIController.class.getName()).log(Level.SEVERE, null, ex);
            }       setUsersList(new ArrayList<>());
            // Add categories with username attribute to the user category array list
            if (!sdc.getCategoryList().getCategoryArrayList().isEmpty()) {
                for(int i = 0; i < sdc.getCategoryList().getCategoryArrayList().size(); i++) {
                    if(sdc.getCategoryList().getCategoryArrayList().get(i).getUserEmail().equals(userAccount.getUserEmail())) {
                        Category c = new Category(sdc.getCategoryList().getCategoryArrayList().get(i).getCategoryName(),
                            sdc.getCategoryList().getCategoryArrayList().get(i).getCategoryID(), sdc.getCategoryList().getCategoryArrayList().get(i).getUserEmail());
                        usersList.add(c); // Add this category list to the user's list of categories
                    }
                }
            }
            // Create table columns
            categoryNameColumn = new TableColumn<>("Category Name");
            categoryNameColumn.setMinWidth(540/3.01); // Used to set the column width without the scroll bar appearing
            pdfExportColumn = new TableColumn<>("PDF Export");
            pdfExportColumn.setMinWidth(540/3.01); // Used to set the column width without the scroll bar appearing
            txtExportColumn = new TableColumn<>(".TXT Export");
            txtExportColumn.setMinWidth(540/3.05); // Used to set the column width without the scroll bar appearing
            exportTableView.getColumns().addAll(categoryNameColumn, pdfExportColumn, txtExportColumn);
        });       
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
            
            pdfExportColumn.setCellValueFactory(new PropertyValueFactory<>("pdfExport"));
            pdfExportColumn.setCellFactory(CheckBoxTableCell.forTableColumn(pdfExportColumn));
            pdfExportColumn.setEditable(true);
            
            txtExportColumn.setCellValueFactory(new PropertyValueFactory<>("txtExport"));
            txtExportColumn.setCellFactory(CheckBoxTableCell.forTableColumn(txtExportColumn));
            txtExportColumn.setEditable(true);
            
            categoryList = FXCollections.observableArrayList(usersList);
            exportTableView.setEditable(true);
            exportTableView.setItems(categoryList);
            exportTableView.requestFocus(); // Initialize the window with the focus on the table view
            exportTableView.getSelectionModel().clearSelection(); // Clear the selection model in the table so the last item that was added to the table view is not selected when the table is initialized                        
        });
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
    
    private void setUserCategoryArrayList(ArrayList<Category> userCategoryArrayList) {
        this.userCategoryArrayList = userCategoryArrayList;
    }
    
    public ArrayList<Category> getList() {
        return list;
    }

    public void setList(ArrayList<Category> list) {
        this.list = list;
    }

    public ArrayList<Category> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<Category> usersList) {
        this.usersList = usersList;
    }

    @FXML
    private void dashboardButtonClicked(ActionEvent event) {
        loadDashboard();
        // Get control of the dashboard stage (via the dashboard button in this stage)          
        Stage stage = (Stage)dashboardButton.getScene().getWindow();
        // Close the export stage
        stage.close();
    }

    @FXML
    private void exportButtonClicked(ActionEvent event) {
        System.out.println("Export button is still being developed");
        message.setText("Export access is only available to premium users.\nContact your administrator to upgrade your account!");
//        for (Object row : exportTableView.getItems()) {
//            if(!exportTableView.getSelectionModel().isEmpty()) { // Row in the table is selected (selection model is not empty)
//                /*exportTableView.getRowFactory()
//                  exportTableView.getColumns()*/
//            }
//        }
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
            java.util.logging.Logger.getLogger(SignOnGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}

