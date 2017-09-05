/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pmart;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * FXML Controller class
 *
 * @author LeakSmey
 */
public class FXMLMainController implements Initializable {

    @FXML
    private ListView<String> listForm;
    private ObservableList<String> subListForm;
    @FXML
    private TabPane main_tab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        generateSubList();
        selectListForm();
    }    
    private void generateSubList(){
        subListForm = FXCollections.observableArrayList();
        subListForm.add("Product");
        subListForm.add("Order");
        listForm.setItems(subListForm);
        
    }
    private void selectListForm(){
        listForm.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int i = listForm.getSelectionModel().getSelectedIndex();
                if(i == 0){
                    try {
                        Node productForm = FXMLLoader.load(getClass().getResource("/pmart/FXMLDocument.fxml"));
                        Tab tab = new Tab("Products", productForm);
                        main_tab.getTabs().add(tab);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if(i == 1){
                    try {
                        Node orderForm = FXMLLoader.load(getClass().getResource("/pmart/OrderProductFXML.fxml"));
                        Tab tab = new Tab("Order", orderForm);
                        main_tab.getTabs().add(tab);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        });
    }
    
}
