/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

/**
 *
 * @author LeakSmey
 */
public class Products {
    private String pname;
    private String price;
    private String pqty;
    private String amount;

    public Products(String pname, String price, String pqty, String amount) {
        this.pname = pname;
        this.price = price;
        this.pqty = pqty;
        this.amount = amount;
    }

    /**
     * @return the pname
     */
    public String getPname() {
        return pname;
    }

    /**
     * @param pname the pname to set
     */
    public void setPname(String pname) {
        this.pname = pname;
    }

    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return the pqty
     */
    public String getPqty() {
        return pqty;
    }

    /**
     * @param pqty the pqty to set
     */
    public void setPqty(String pqty) {
        this.pqty = pqty;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    
    
}
