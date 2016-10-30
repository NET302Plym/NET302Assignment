/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net302;

import com.google.gson.Gson;
import java.sql.Time;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Mellor
 */
@Named(value = "orderBean")
@Dependent
public class OrderBean
{

    /**
     * Creates a new instance of OrderBean
     */
    int     ID;
    int     quantity;
    String  dateDelivered;
    UserBean  customer;
    String  location;
    UserBean  staffFulfilled;
    ProductBean product;
    Time    timeDelivered;
    Boolean fulfilled;
    
    // Constructors
    /**
     * This should be used for the construction from the Database on the Middleware side (as is requires all information)
     * @param ID
     * @param quantity
     * @param dateDelivered
     * @param customer
     * @param location
     * @param staffFulfilled
     * @param product
     * @param timeDelivered
     * @param fulfilled 
     */
    
    public OrderBean(int ID, int quantity)
    {
        this.ID = ID;
        this.quantity = quantity;
    }
    
    public OrderBean(int ID, int quantity, String dateDelivered, UserBean customer, String location, UserBean staffFulfilled, ProductBean product, Time timeDelivered, Boolean fulfilled) {
        this.ID = ID;
        this.quantity = quantity;
        this.dateDelivered = dateDelivered;
        this.customer = customer;
        this.location = location;
        this.staffFulfilled = staffFulfilled;
        this.product = product;
        this.timeDelivered = timeDelivered;
        this.fulfilled = fulfilled;
    }
    /**
     * Creates a new order (client side)
     * @param quantity Quantity requested
     * @param user The user logged in
     * @param product The product requested
     */
    public OrderBean(int quantity, UserBean user, ProductBean product){
        this.quantity = quantity;
        this.customer = user;
        this.product = product;
    }
    
    // Set / Gets
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(String dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public UserBean getCustomer() {
        return customer;
    }

    public void setCustomer(UserBean customer) {
        this.customer = customer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserBean getStaffFulfilled() {
        return staffFulfilled;
    }

    public void setStaffFulfilled(UserBean staffFulfilled) {
        this.staffFulfilled = staffFulfilled;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public Time getTimeDelivered() {
        return timeDelivered;
    }

    public void setTimeDelivered(Time timeDelivered) {
        this.timeDelivered = timeDelivered;
    }

    public Boolean getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(Boolean fulfilled) {
        this.fulfilled = fulfilled;
    }
        
    // ToString Override

    @Override
    public String toString() {
        return "Order{" + "ID=" + ID + ", quantity=" + quantity + ", dateDelivered=" + dateDelivered + ", customer=" + customer + ", location=" + location + ", staffFulfilled=" + staffFulfilled + ", product=" + product + ", timeDelivered=" + timeDelivered + ", fulfilled=" + fulfilled + '}';
    }
        
    // JSON Conversion
    public OrderBean(String jsonString){
        Gson gson = new Gson();
        OrderBean order = gson.fromJson(jsonString, OrderBean.class);
        this.ID = order.ID;
        this.quantity = order.quantity;
        this.dateDelivered = order.dateDelivered;
        this.customer = order.customer;
        this.location = order.location;
        this.staffFulfilled = order.staffFulfilled;
        this.product = order.product;
        this.timeDelivered = order.timeDelivered;
        this.fulfilled = order.fulfilled;
    }
    
    public String GetJSONString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
