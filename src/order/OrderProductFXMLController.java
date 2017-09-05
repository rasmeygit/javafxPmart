/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author LeakSmey
 */
public class OrderProductFXMLController implements Initializable {

    @FXML
    private TextField txt_barcode;
    @FXML
    private TextField txt_productName;
    @FXML
    private TextField txt_priceOut;
    @FXML
    private TextField txt_qty;
    
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    @FXML
    private TableView<OrderList> table_order;
    @FXML
    private TableColumn<OrderList, Integer> column_no;
    @FXML
    private TableColumn<OrderList, Integer> column_pid;
    @FXML
    private TableColumn<OrderList, String> column_barcode;
    @FXML
    private TableColumn<OrderList, String> column_productName;
    @FXML
    private TableColumn<OrderList, Double> colum_price;
    @FXML
    private TableColumn<OrderList, Integer> column_qty;
    @FXML
    private TableColumn<OrderList, Double> column_amount;
    
    private ObservableList<OrderList> orderData;
    int no = 0;
    int productId;
     String barcode;
     String productName;
     double priceOut;
     int qty = 0;
     double amount = 0.0;
    @FXML
    private TextField txt_invoiceNo;
    @FXML
    private DatePicker orderdate;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = dba.DBConnection.pmartConnection();
        
        txt_invoiceNo.setText(autoOrderId());
        
        column_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        column_pid.setCellValueFactory(new PropertyValueFactory<>("productId"));
        column_barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        column_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colum_price.setCellValueFactory(new PropertyValueFactory<>("priceOut"));
        column_qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        column_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        orderData = FXCollections.observableArrayList();
    }    

    @FXML
    private void handleScanProduct(KeyEvent event) throws SQLException {
        pst = con.prepareStatement("Select * from products where barcode = ? ");
        pst.setString(1, txt_barcode.getText());
        rs = pst.executeQuery();
        if(rs.next()){
            
            productId = rs.getInt(1);
            barcode = rs.getString(2);
            productName = rs.getString(3);
            priceOut = rs.getDouble(5);
            txt_productName.setText(productName);
            txt_priceOut.setText(""+priceOut);
            txt_qty.requestFocus();
            
        }
        rs.close();
        
    }

    @FXML
    private void hanldeOrder(ActionEvent event) {
        
        int qty = Integer.parseInt(txt_qty.getText());
        if(qty != 0){
            amount = priceOut * qty;
            for(OrderList item : orderData){
                if(item.getProductId() == productId){
                    int table_qty = item.getQty() + qty;
                    double table_amount = item.getAmount() + amount;
                    item.setQty(table_qty);
                    item.setAmount(table_amount);
                    table_order.getItems().set(table_order.getItems().indexOf(item), item);
                    clearText();
                    return;
                    
                }
            }
            orderData.add(new OrderList(++no, productId,barcode , productName, priceOut, qty, amount));
            table_order.setItems(orderData);
            clearText();
        }
        
    }
    private void clearText(){
        txt_barcode.clear();
        txt_barcode.requestFocus();
        txt_productName.clear();
        txt_priceOut.clear();
        txt_qty.clear();
    }
    private String autoOrderId(){
        String orderId = "ivn00000";
        try {
            pst = con.prepareStatement("select max(orderid) from orderProduct");
            rs = pst.executeQuery();
            if(rs.next()){
                int n = Integer.parseInt(orderId.substring(3)) + 1;
                int x = String.valueOf(n).length();
                orderId = orderId.substring(0, 8-x) + String.valueOf(n);
                
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(OrderProductFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orderId;
    }
    
}
