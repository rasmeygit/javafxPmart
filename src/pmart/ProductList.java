/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pmart;

/**
 *
 * @author LeakSmey
 */
public class ProductList {

    private int pid;
    private String barcode;
    private String productName;
    private String priceIn;
    private String priceOut;
    private String qty;
    private String dateIn;
    
   
    public ProductList(String barcode, String productName, String priceIn, String priceOut) {
        this.barcode = barcode;
        this.productName = productName;
        this.priceIn = priceIn;
        this.priceOut = priceOut;
        
    }   
     public ProductList(int pid,String barcode, String productName, String priceIn, String priceOut, String qty, String dateIn) {
        this.pid = pid;
        this.barcode = barcode;
        this.productName = productName;
        this.priceIn = priceIn;
        this.priceOut = priceOut;
        this.qty = qty;
        this.dateIn = dateIn;
        
    }   
   

    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the priceIn
     */
    public String getPriceIn() {
        return priceIn;
    }

    /**
     * @param priceIn the priceIn to set
     */
    public void setPriceIn(String priceIn) {
        this.priceIn = priceIn;
    }

    /**
     * @return the priceOut
     */
    public String getPriceOut() {
        return priceOut;
    }

    /**
     * @param priceOut the priceOut to set
     */
    public void setPriceOut(String priceOut) {
        this.priceOut = priceOut;
    }

    /**
     * @return the qty
     */
    public String getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * @return the dateIn
     */
    public String getDateIn() {
        return dateIn;
    }

    /**
     * @param dateIn the dateIn to set
     */
    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

    /**
     * @return the pid
     */
    public int getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(int pid) {
        this.pid = pid;
    }
    
   
  
    
}
