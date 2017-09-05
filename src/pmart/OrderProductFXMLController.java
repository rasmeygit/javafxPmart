/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pmart;

import Dialog.AlertDialog;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import report.Products;


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
    @FXML
    private TextField txt_invoiceNo;
    @FXML
    private DatePicker OrderDate;
    
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    @FXML
    private Button btn_enter;
    @FXML
    private TableView<OrderList> table_order;
    @FXML
    private TableColumn<OrderList, Integer> column_no;
    @FXML
    private TableColumn<OrderList, Integer> column_pid;
    @FXML
    private TableColumn<OrderList, String> column_barcode;
    @FXML
    private TableColumn<OrderList, String> column_product;
    @FXML
    private TableColumn<OrderList, Double> column_price;
    @FXML
    private TableColumn<OrderList, Integer> column_qty;
    @FXML
    private TableColumn<OrderList, Integer> cokumn_amount;
    
    int no = 0;
    int productId = 0;
    String barcode = "";
    String productName = "";
    double priceOut = 0.0;
    int qty = 0;
    double amount = 0.0;
    
    private ObservableList<OrderList> orderData;
    @FXML
    private Label lbl_grandTotal;
    private double grandTotal = 0.0;
    @FXML
    private Button btn_PrintInvoice;
    
    

    /**
     * Initializes the controller class.
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = dba.DBConnection.pmartConnection();
        txt_invoiceNo.setText(autoOrderID());
        OrderDate.setValue(LocalDate.now());
        
        column_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        column_pid.setCellValueFactory(new PropertyValueFactory<>("productId"));
        column_barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        column_product.setCellValueFactory(new PropertyValueFactory<>("productName"));
        column_price.setCellValueFactory(new PropertyValueFactory<>("priceOut"));
        column_qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        cokumn_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        orderData = FXCollections.observableArrayList();
        
    }    
    private String autoOrderID(){
        String orderID = "IVN00000";
        try {
            pst = con.prepareStatement("Select max(orderID) from orderProduct");
            rs = pst.executeQuery();
            if(rs.next()){
                orderID = rs.getString(1);
                int n = Integer.parseInt(orderID.substring(3)) + 1;
                int x = String.valueOf(n).length();
                orderID = orderID.substring(0, 8-x) + String.valueOf(n);
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(OrderProductFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orderID;
    }
     
    @FXML
    private void handleBarcodeScan(KeyEvent event) {
        scannBarcode();
    }

    @FXML
    private void handleEnter(ActionEvent event) {
        scannBarcode();
    }
    private void scannBarcode(){
        try {
                
                pst = con.prepareStatement("Select * from products where barcode = ?");
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
            } catch (SQLException ex) {
                Logger.getLogger(OrderProductFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @FXML
    private void handleOrder(ActionEvent event) {
        qty = Integer.parseInt(txt_qty.getText());
        if(qty != 0){
            amount = priceOut * qty;
            grandTotal += amount;
           
            for (OrderList item : orderData) {
                if(item.getProductId() == productId){
                    int table_qty = item.getQty() + qty;
                    double table_amount = item.getAmount() + amount;
                    item.setQty(table_qty);
                    item.setAmount(table_amount);
                    lbl_grandTotal.setText(""+grandTotal);
                    table_order.getItems().set(table_order.getItems().indexOf(item), item);
                    clearText();
                    return;
                    
                }
            }
            
            orderData.add(new OrderList(++no, productId, barcode, productName, priceOut, qty, amount));
            table_order.setItems(orderData);
            lbl_grandTotal.setText(""+grandTotal);
            clearText();
        }
        else{
            AlertDialog.display("Info", "Qty can not be zero");
        }
    }
    private void clearText(){
        txt_barcode.clear();
        txt_barcode.requestFocus();
        txt_productName.clear();
        txt_priceOut.clear();
        txt_qty.clear();
        
    }

    @FXML
    private void handlePrintInvoice(ActionEvent event) {
        String sql = "Insert into orderProduct(orderId, OrderDate) values(?,?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, txt_invoiceNo.getText());
            pst.setDate(2, java.sql.Date.valueOf(OrderDate.getValue()));
            int i = pst.executeUpdate();
            if(i == 1){
                sql = "Insert into orderDetails(orderId, pid, qty, price) values(?,?,?,?)";
                for(OrderList ol : orderData){
                    pst = con.prepareStatement(sql);
                    pst.setString(1, txt_invoiceNo.getText());
                    pst.setInt(2, ol.getProductId());
                    pst.setInt(3, ol.getQty());
                    pst.setDouble(4, ol.getPriceOut());
                    
                    int j = pst.executeUpdate();
                    if(j == 1){
                        System.out.println("Complete Order");
                        
                    }
                }
                printInvoice();
                txt_invoiceNo.setText(autoOrderID());
            }  
        } catch (SQLException ex) {
            Logger.getLogger(OrderProductFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void printInvoice(){
        String souceFile = "D:\\Java2016\\NetbeansProjects\\Pmart\\src\\report\\Invoice.jrxml";
        try {
            JasperReport jr = JasperCompileManager.compileReport(souceFile);
            HashMap<String, Object> para = new HashMap<>();
            para.put("invoiceNo","Invoice No : "+txt_invoiceNo.getText());
            para.put("cashier", "username") ;// user get from login
            para.put("grandTotal", lbl_grandTotal.getText());
            
            ArrayList<Products> plist = new ArrayList<>();
            
            for(OrderList ol  : orderData){
                plist.add(new Products(ol.getProductName(),""+ ol.getPriceOut(),""+ ol.getQty(),""+ ol.getAmount()));
                
            }
            JRBeanCollectionDataSource jcs = new JRBeanCollectionDataSource(plist);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, jcs);
            JasperViewer.viewReport(jp);
            
            
        } catch (JRException ex) {
            Logger.getLogger(OrderProductFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
        
    }
   

    
    

