/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pmart;

import Dialog.AlertDialog;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author LeakSmey
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button btn_addProduct;
    @FXML
    private TextField txt_barcode;
    @FXML
    private TextField txt_priceIn;
    @FXML
    private TextField txt_product;
    @FXML
    private TextField txt_priceOut;
    
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<ProductList> data;
    
    @FXML
    private TableView<ProductList> tableProduct;
    @FXML
    private TableColumn<ProductList, String> columnBarcode;
    @FXML
    private TableColumn<ProductList, String> columnProductName;
    @FXML
    private TableColumn<ProductList, String> columnPriceIn;
    @FXML
    private TableColumn<ProductList, String> columnPriceOut;
    @FXML
    private TableColumn<ProductList, String> columnQty;
    @FXML
    private TableColumn<ProductList, String> columnDateIn;
    @FXML
    private Label error_barcode;
    @FXML
    private Label error_product;
    @FXML
    private Label error_priceIn;
    @FXML
    private Label error_priceOut;
    @FXML
    private Button btn_UpdateProduct;
    @FXML
    private Button btn_DeleteProduct;
    @FXML
    private TextField txt_search;
    @FXML
    private Button btn_clear;
    @FXML
    private TextField txt_qty;
    @FXML
    private Label error_qty;
    @FXML
    private DatePicker dateIn_Picker;
   
   
    private int pid;
    @FXML
    private Button btn_order;
    @FXML
    private Button btnBrowser;
    private FileChooser fileChooser;
    private File file;
    private Stage stage;
    @FXML
    private AnchorPane anchorPane;
    private final Desktop deskTop = Desktop.getDesktop();
    @FXML
    private ImageView imageView;
    private Image image;
    private FileInputStream fis;
  
   


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = dba.DBConnection.pmartConnection();
        data = FXCollections.observableArrayList();
        setCellTable();
        try {
            loadDataFromDatabase();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        setCellValueFromTableToTextField();
        searchProduct();
        
        
       fileChooser = new FileChooser();
       fileChooser.getExtensionFilters().addAll(
               new FileChooser.ExtensionFilter("All files", "*.*"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif"),
                 new FileChooser.ExtensionFilter("Text File", "*.txt")
       );
        
   
    } 
   
    private void setCellTable(){
        columnBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        columnProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        columnPriceIn.setCellValueFactory(new PropertyValueFactory<>("priceIn"));
        columnPriceOut.setCellValueFactory(new PropertyValueFactory<>("priceOut"));
        columnQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        columnDateIn.setCellValueFactory(new PropertyValueFactory<>("dateIn"));
            
        
    }
    
    private void loadDataFromDatabase() throws SQLException{
        data.clear();
      
            pst = con.prepareStatement("Select  p.*, s.Qty, s.DateIn   from products p Inner join stock s on p.pid = s.pid");
            rs = pst.executeQuery();
            while(rs.next()){
                data.add(new ProductList(rs.getInt(1),rs.getString(2), rs.getString(3), ""+rs.getDouble(4), ""+rs.getDouble(5), ""+rs.getInt(7), ""+rs.getDate(8)));
            }
       
        tableProduct.setItems(data);
        
    }
    private void setCellValueFromTableToTextField(){
        tableProduct.setOnMouseClicked(e -> {
            ProductList pl = tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
            txt_barcode.setText(pl.getBarcode());
            txt_product.setText(pl.getProductName());
            txt_priceIn.setText(pl.getPriceIn());
            txt_priceOut.setText(pl.getPriceOut());
            txt_qty.setText(pl.getQty());
            dateIn_Picker.setValue(LocalDate.parse(pl.getDateIn()));
            pid = pl.getPid(); 
            showProductImage(pl.getBarcode());
          
        });
        
        
    }
    private void showProductImage(String barcode){
       
        try {
             pst = con.prepareStatement("Select pimage from products where barcode = ?");
            pst.setString(1, barcode);
            rs = pst.executeQuery();
            if(rs.next()){
                InputStream is = rs.getBinaryStream(1);
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] contents = new byte[1024];
                int size = 0;
                while( (size = is.read(contents)) != -1){
                    os.write(contents, 0, size);
                }
                
                image = new Image("file:photo.jpg", imageView.getFitWidth(), imageView.getFitHeight(), true, true);
                imageView.setImage(image);
                imageView.setPreserveRatio(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    public void handleAddProduct(ActionEvent event) throws SQLException {
        boolean isBarcodeEmpty = validation.TextFieldValidation.istextFieldTypeNumber(txt_barcode, error_barcode, "Barcode must be number");
        boolean isProductEmpty = validation.TextFieldValidation.isTextFieldNotEmpty(txt_product, error_product, "Product Name is require");
        boolean isPriceInEmpty = validation.TextFieldValidation.istextFieldTypeNumber(txt_priceIn, error_priceIn, "PriceIn must be number");
        boolean isPriceOutEmpty = validation.TextFieldValidation.istextFieldTypeNumber(txt_priceOut, error_priceOut, "PriceOut must be number");
        boolean isQtyEmpty = validation.TextFieldValidation.istextFieldTypeNumber(txt_qty, error_qty, "qty is required");
        
        if(isBarcodeEmpty && isProductEmpty && isPriceInEmpty && isPriceOutEmpty && isQtyEmpty){    
            String sql = "Insert into products(barcode,productname,priceIn, priceOut, pimage) Values(?,?,?,?,?)";
            String barcode = txt_barcode.getText();
            String pname = txt_product.getText();
            double priceIn = Double.valueOf(txt_priceIn.getText());
            double priceOut = Double.valueOf(txt_priceOut.getText());

            try {
                pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, barcode);
                pst.setString(2, pname);
                pst.setDouble(3, priceIn);
                pst.setDouble(4, priceOut);   
                fis = new FileInputStream(file);
                pst.setBinaryStream(5, fis, file.length());
                
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                rs.next();
                Object key = rs.getObject(1);
                sql = "Insert into stock(pid, qty, dateIn) values(?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(String.valueOf(key)));
                pst.setInt(2, Integer.parseInt(txt_qty.getText()));
                pst.setDate(3, java.sql.Date.valueOf(dateIn_Picker.getValue()));
                
                int i = pst.executeUpdate();
            /*try{
            
                CallableStatement cstm = con.prepareCall("{call insertProduct(?,?,?,?,?,?)}");
                cstm.setString(1, barcode);
                cstm.setString(2, pname);
                cstm.setDouble(3, priceIn);
                cstm.setDouble(4, priceOut);
                cstm.setInt(5, Integer.parseInt(txt_qty.getText()));
                cstm.setDate(6, java.sql.Date.valueOf(dateIn_Picker.getValue()));
                int i = cstm.executeUpdate();*/
                
                if(i == 1){
                    //System.out.println("Data Insert Successfully");
                    AlertDialog.display("Info", "Data Insert Successfully");
                     setCellTable();
                    loadDataFromDatabase();
                    clearTextField();
                }

            } catch (SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally{
                pst.close();
            }
        }
        
        
    }

    @FXML
    private void handleUpdateProduct(ActionEvent event) {
        boolean isBarcodeEmpty = validation.TextFieldValidation.isTextFieldNotEmpty(txt_barcode, error_barcode, "Barcode is required to update");
        boolean isProductEmpty = validation.TextFieldValidation.isTextFieldNotEmpty(txt_product,error_product, "Product Name is required to update");
        boolean isPriceInEmpty = validation.TextFieldValidation.isTextFieldNotEmpty(txt_priceIn, error_priceIn, "PriceIn is required to update");
        boolean isPriceOutEmpty = validation.TextFieldValidation.isTextFieldNotEmpty(txt_priceOut, error_priceOut, "PriceOut is required to update");
        boolean isQtyEmpty = validation.TextFieldValidation.istextFieldTypeNumber(txt_qty, error_qty, "qty is required to update");
        if(isBarcodeEmpty && isProductEmpty && isPriceInEmpty && isPriceOutEmpty && isQtyEmpty){
            String sql = "Update products set productname = ?, priceIn = ?, priceOut = ?, pimage = ? where barcode =?";
            try {
                String barcode = txt_barcode.getText();
                String pname = txt_product.getText();
                double priceIn = Double.valueOf(txt_priceIn.getText());
                double priceOut = Double.valueOf(txt_priceOut.getText());
                pst = con.prepareStatement(sql);
                pst.setString(1, pname);
                pst.setDouble(2, priceIn);
                pst.setDouble(3, priceOut);
                 fis = new FileInputStream(file);
                pst.setBinaryStream(4, fis, file.length());
                pst.setString(5, barcode);
                int i = pst.executeUpdate();
                
                sql = "Update Stock Set qty = ?, dateIn = ? where pid = ?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txt_qty.getText()));
                pst.setDate(2, java.sql.Date.valueOf(dateIn_Picker.getValue()));
                pst.setInt(3, pid);
                
                int j = pst.executeUpdate();

                if(i== 1 && j == 1){
                    AlertDialog.display("Info", "Product Update Successfully");
                    loadDataFromDatabase();
                    clearTextField();
                }

            } catch (SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void clearTextField(){
        txt_barcode.clear();
        txt_product.clear();
        txt_priceIn.clear();
        txt_priceOut.clear();
        txt_qty.clear();
        dateIn_Picker.setValue(LocalDate.now());
    }

    @FXML
    private void handleDeleteProduct(ActionEvent event) {
        boolean isBarcodeEmpty = validation.TextFieldValidation.isTextFieldNotEmpty(txt_barcode, error_barcode, "Barcode is require to delete");
        if(isBarcodeEmpty){
            String sql = "delete from products where barcode = ?";
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, txt_barcode.getText());
                int i = pst.executeUpdate();
                if(i == 1){
                    AlertDialog.display("Info", "Product Delete Successfully");
                    loadDataFromDatabase();
                    clearTextField();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    private void searchProduct(){
        
        txt_search.setOnKeyReleased(e->{
            if(txt_search.getText().equals("")){
                try {
                    loadDataFromDatabase();
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                data.clear();
                String sql = "Select  p.*, s.Qty, s.DateIn  from products p Inner join stock s on p.pid = s.pid and p.barcode LIKE '%"+txt_search.getText()+"%'"
                        + " UNION Select  p.*, s.Qty, s.DateIn   from products p Inner join stock s on p.pid = s.pid and p.productName LIKE '%"+txt_search.getText()+"%'"
                + " UNION Select  p.*, s.Qty, s.DateIn   from products p Inner join stock s on p.pid = s.pid and p.priceIn LIKE '%"+txt_search.getText()+"%'"
                        + " UNION Select  p.*, s.Qty, s.DateIn   from products p Inner join stock s on p.pid = s.pid and p.priceOut LIKE '%"+txt_search.getText()+"%'";
                try {
                    pst = con.prepareStatement(sql);
                    rs = pst.executeQuery();
                    while(rs.next()){
                        System.out.println(""+rs.getString(2));
                        data.add(new ProductList(rs.getInt(1),rs.getString(2), rs.getString(3), ""+rs.getDouble(4), ""+rs.getDouble(5),""+rs.getInt(6),""+rs.getDate(7)));
                    }
                    tableProduct.setItems(data);
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            });
    }

    @FXML
    private void handleClear(ActionEvent event) {
        clearTextField();
    }

    @FXML
    private void handleOrder(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("OrderProductFXML.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        
    }

    @FXML
    private void handleBrowser(ActionEvent event) {
        stage = (Stage) anchorPane.getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);
        /*try {
            deskTop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        if(file != null){
            System.out.println(""+file.getAbsolutePath());
            image = new Image(file.getAbsoluteFile().toURI().toString(), imageView.getFitWidth(), imageView.getFitHeight(), true, true);
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
        }
        
        
    }

    
    
}
