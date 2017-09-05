/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pmart;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import validation.TextFieldValidation;


/**
 * FXML Controller class
 *
 * @author LeakSmey
 */
public class UserRegisterFXMLController implements Initializable {

    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private ComboBox<Role> cboRole;
    @FXML
    private Label lblEmailError;
    @FXML
    private Button btnRegister;
    @FXML
    private Label lblPasswordError;

     private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<Role> role;
    private String roleId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         con = dba.DBConnection.pmartConnection();
         role = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("Select * from Role");
            rs = pst.executeQuery();
            while(rs.next()){
                role.add(new Role(rs.getString(1), rs.getString(2)));
            }
            cboRole.setItems(role);
        } catch (SQLException ex) {
            Logger.getLogger(UserRegisterFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        cboRole.setConverter(new StringConverter<Role>() {
             @Override
             public String toString(Role object) {
                 return object.getRoleName();
             }

             @Override
             public Role fromString(String string) {
                 return null;
             }
         });
        cboRole.valueProperty().addListener((obs, oldVaue, newValue) ->{
                if(newValue != null){
                    /*Alert alter = new Alert(Alert.AlertType.INFORMATION, newValue.getRoleID(), ButtonType.OK);
                    alter.show();*/
                    roleId = newValue.getRoleID();
                }
        });
         
    }    

    @FXML
    private void handleRegister(ActionEvent event) {
        boolean isValidEmail = TextFieldValidation.isValidEmail(txtEmail, lblEmailError, "invalid email! please try again!");
        boolean isPasswordMatched = TextFieldValidation.isPasswordMatched(txtPassword, txtConfirmPassword, lblPasswordError, "password is not matched, please try again!");
        if(isValidEmail && isPasswordMatched){
            try {
                /*lblEmailError.setText("Valid Email");
                lblPasswordError.setText("Password matched");*/
                String insert = "Insert into Users(Email, Password, FirstName, LastName, RoleId) Values(?,?,?,?,?)";
                pst = con.prepareStatement(insert);
                pst.setString(1, txtEmail.getText());
                pst.setString(2, txtPassword.getText());
                pst.setString(3, txtFirstName.getText());
                pst.setString(4, txtLastName.getText());
                pst.setString(5, roleId);
                
                int i = pst.executeUpdate();
                if(i == 1){
                     Alert alter = new Alert(Alert.AlertType.INFORMATION, "User register successfully!", ButtonType.OK);
                    alter.show();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserRegisterFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
