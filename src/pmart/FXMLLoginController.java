/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pmart;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author LeakSmey
 */
public class FXMLLoginController implements Initializable {

    @FXML
    private TextField txt_email;
    @FXML
    private PasswordField txt_password;
    @FXML
    private Button btn_Login;
    @FXML
    private Button btn_cancel;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = dba.DBConnection.pmartConnection();
    }    

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        if(!txt_email.getText().isEmpty() || !txt_password.getText().isEmpty()){
                if(txt_email.getText().equals(getEmail()) && txt_password.getText().equals(getPassword())){
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                stage.close();
                Parent root = FXMLLoader.load(getClass().getResource("/pmart/FXMLMain.fxml"));
               Scene scene = new Scene(root);
               stage.setScene(scene);
                //stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.NONE,"Invalid Email or Password", ButtonType.OK);
                alert.setTitle("Invalid");
                alert.showAndWait();
            }
        }
        else{
                Alert alert = new Alert(Alert.AlertType.NONE,"Invalid Email or Password", ButtonType.OK);
                alert.setTitle("Invalid");
                alert.showAndWait();
            }
        
    }
    private String getEmail(){
        String email = "";
        try {
            
            pst = con.prepareStatement("Select email from users where email = ?");
            pst.setString(1,txt_email.getText());
            rs = pst.executeQuery();
            if(rs.next())
                email = rs.getString(1);
            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return email;
    }
    private String getPassword(){
        String password = "";
        try {
            
            pst = con.prepareStatement("Select password from users where email = ?");
            pst.setString(1,txt_email.getText());
            rs = pst.executeQuery();
            if(rs.next())
                password = rs.getString(1);
            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return password;
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
    
}
