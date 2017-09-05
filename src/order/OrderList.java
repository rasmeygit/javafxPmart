/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order;

/**
 *
 * @author LeakSmey
 */
public class OrderList {
    private int no;
    private int productId;
    private String barcode;
    private String productName;
    private double priceOut;
    private int qty;
    private double amount;

    public OrderList(int no, int productId, String barcode, String productName, double priceOut, int qty, double amount) {
        this.no = no;
        this.productId = productId;
        this.barcode = barcode;
        this.productName = productName;
        this.priceOut = priceOut;
        this.qty = qty;
        this.amount = amount;
    }

    /**
     * @return the no
     */
    public int getNo() {
        return no;
    }

    /**
     * @param no the no to set
     */
    public void setNo(int no) {
        this.no = no;
    }

    /**
     * @return the productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
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
     * @return the priceOut
     */
    public double getPriceOut() {
        return priceOut;
    }

    /**
     * @param priceOut the priceOut to set
     */
    public void setPriceOut(double priceOut) {
        this.priceOut = priceOut;
    }

    /**
     * @return the qty
     */
    public int getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(int qty) {
        this.qty = qty;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    
    
}
