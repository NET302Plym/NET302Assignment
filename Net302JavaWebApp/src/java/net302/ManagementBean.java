package net302;

import Connector.Connector;
import Encrypter.Encrypter;
import NET302JavaLibrary.*;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigInteger;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Date;
import javax.faces.context.FacesContext;
import java.security.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Arrays;




/**
 * ManagementBean, provides methods and data access for the web session.
 * @author Mellor
 */
@Named(value = "managementBean")
@Dependent
public class ManagementBean {
    // DEBUG: If true, then no commands will be sent to the REST server!
    private final boolean debug = false;
    
    private final Connector             client_connector;
    private final DummyData             dummy_data;
    
    private User loggedUser;
    private Boolean authenticated = false;
    
    public String orderID;
    public String productName;
    public String orderQuantity;
    
    public String errorMessage = "";



    
    
            
    private Encrypter encrypter;
    
    private ArrayList<Product>          products            = null;
    private ArrayList<Order>            orders              = null;
    private ArrayList<User>             users               = null;
    private ArrayList<GenericLookup>    categories          = null;
    private ArrayList<GenericLookup>    subCategories       = null;
    private ArrayList<GenericLookup>    orderStatus         = null;
    private ArrayList<GenericLookup>    containers          = null;
    private ArrayList<GenericLookup>    locations           = null;
    private ArrayList<Order>            unfulfilled         = null;
    
    /**
     * Creates a new instance of ManagementBean.
     */
    public ManagementBean()
    {        
        System.out.println("**** ManagementBean Loaded ****");
        client_connector = new Connector();
        dummy_data = new DummyData(0);        
       //products = client_connector.getAllProducts();
    }
    
    
    
    //************************************************************************//
    //  -   ADDING OR EDITING PRODUCTS/ORDERS/USERS                       -   //
    //************************************************************************//
    // This should be any method which needs to send data to the middleware   //
    // server. Most of the work here will be building an object and then using//
    // the Connector library to send the data. Refer to Connector for help.   //
    //************************************************************************//
    
    /**
     * 
     * @param p
     * @param quantity
     * @param staff 
     */

    public String GetProductName(){
        if(this.productName == "")
        {
            return "all products";
        }
        return this.productName;
    }
    public String GetOrderQuantity(){
        return this.orderQuantity;
    }
    
//    public String orderID;
//    public String productName;
//    public String orderQuantity;
    
    public String orderProduct(Product p, String quantity) {
        if(authenticated == false)return "index.html";
        int quantityInt;
        try
        {
           quantityInt = Integer.parseInt( quantity );
           errorMessage = "";
        }
        catch( NumberFormatException e )
        {
           errorMessage = "Error: Quantity must be a number";
           return "productList.xhtml";
        }
        if(quantityInt > p.getStockCount() || quantityInt < 1)
        {
           errorMessage = "Error: Quantity must less than the stock count and greater than 0";
           return "productList.xhtml";
        }
            
        
        
        
        System.out.println("Product ID = " + p.getID() + " and Qualtity = " + quantity);
        
        productName = p.getName();
        orderQuantity = String.valueOf(quantity);

        if (debug)
        { 
            System.out.println("[DEBUG] ORDER GENERATED FOR " + quantity + " OF " + p.getName());
            //orders.add(order);
            //dummy_data.setOrders(orders);
        } 
        else
        {
            if(client_connector.addOrder(quantityInt, this.loggedUser.getID(), p.getID())) //int quantity, int staffID, int productID
            {
                System.out.println("***** ADDING TO ORDER ******");
                filterProducts("");
                return "displayOrder.xhtml";
            }
        } 
        return null;
    }

    public String Logout(){
        this.loggedUser = null;
        return "index.xhtml";
    }
    
    public String getUsername()
    {
        return this.loggedUser.getName();
    }
    
    public String getErrorMessage() {
            return errorMessage;
    }
    
//    public Order getOrder()
//    {
//        return this.order;
//    }
    
    public String auth(String username, String password) throws Exception
    {  
        errorMessage = "";
        authenticated = false;    
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes(),0,password.length());
            String md5Pass = new BigInteger(1, md.digest()).toString(16);
            User user = client_connector.authenticatePost(username, md5Pass);
           /* String encUsername = encrypter.encryptString(username);
            String encMD5Pass = encrypter.encryptString(password);
            User user = client_connector.authenticatePost(encUsername, encMD5Pass);
            System.out.println("encrypt usename " + encUsername);*/
            if (user != null)
            {
                loggedUser = user;
                authenticated = true;
                return "productList.xhtml";
            }                        
        } catch (Exception ex){
            authenticated = false;
        }
        return "index.xhtml";        
    }

    //************************************************************************//
    //  -   ARRAYLIST LOADERS                                             -   //
    //************************************************************************//
    // These methods fetch/update the local ArrayLists with data either from  //
    // the DummyData class or from the middleware connection (depending on    //
    // debug). They also return the ArrayList for use in the webpages.        //
    //************************************************************************//
        
    public String filterProducts(String filter)
    {
        errorMessage = "";
        if(authenticated == false) return "index.xhtml";
        System.out.println("*** filtered products based on filter: " + filter);
        products = client_connector.searchProduct(filter);
        return "productList.xhtml";
    }
    
    
    /**
     * Gets the up-to-date ArrayList of Products from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<Product> - being the list of Products.
     */
    public ArrayList<Product> loadProducts() {
        if(authenticated != false)
        {
             if (debug) {
                products = dummy_data.getProducts();
            } else { products = client_connector.getAllProducts(); }
        return products;
        }
        return new ArrayList<>();
    }
    
    /**
     * Gets the up-to-date ArrayList of Orders from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<Order> - being the list of Orders.
     */
    public ArrayList<Order> loadOrders() {
        if(authenticated != false)
        {
            if (debug) {
               orders = dummy_data.getOrders();
            } else { orders = client_connector.getAllOrders(); }
        }
    return orders;
    }
    
    /**
     * Gets the up-to-date ArrayList of Users from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<User> - being the list of Users.
     */
    public ArrayList<User> loadUsers() {
        if(authenticated != false)
        {
            if (debug) {
                users = dummy_data.getUsers();
            } else { users = client_connector.getAllUsers(); }
        }
        return users;
    }
    
    /**
     * Gets the up-to-date ArrayList of Product categories from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<GenericLookup> - being the list of categories.
     */
    public ArrayList<GenericLookup> loadCategories() {
        if(authenticated != false)
        {
            if (debug) {
                categories = dummy_data.getCategories();
            } else { categories = client_connector.getAllLookups("category"); }              
        }
        return categories;
    }
    
    /**
     * Gets the up-to-date ArrayList of sub-categories from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<GenericLookup> - being the list of sub-categories.
     */
    public ArrayList<GenericLookup> loadSubCategories() {
        if(authenticated != false)
        {
            if (debug) {
                subCategories = dummy_data.getSubCategories();
            } else { subCategories = client_connector.getAllLookups("subcategory"); }
        } 
        return subCategories;
    }
    
    /**
     * Gets the up-to-date ArrayList of the Product containers from the REST
     * server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<GenericLookup> - being the list of Product containers.
     */
    public ArrayList<GenericLookup> loadContainers() {
        if(authenticated != false)
        {
            if (debug) {
                containers = dummy_data.getContainers();
            } else { containers = client_connector.getAllLookups("container"); }
        }
        return containers;
    }
    
    /**
     * Gets the up-to-date ArrayList of Order statuses from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<GenericLookup> - being the list of Order statuses.
     */
    public ArrayList<GenericLookup> loadStatuses() {
        if(authenticated != false)
        {
        if (debug) {
            orderStatus = dummy_data.getOrderStatus();
        } else { orderStatus = client_connector.getAllLookups("status"); }
        }
        return orderStatus;
    }
    
    /**
     * Gets the up-to-date ArrayList of Locations from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<GenericLookup> - being the list of Locations.
     */
    public ArrayList<GenericLookup> loadLocations() {
        if(authenticated != false)
        {
        if (debug) {
            locations = dummy_data.getLocations();
        } else { locations = client_connector.getAllLookups("location"); }
        }
        return locations;
    }
    
    /**
     * Gets the up-to-date ArrayList of Orders which are unfulfilled, from the 
     * REST server. 
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<Order> - being the list of unfulfilled Orders.
     */
    public ArrayList<Order> loadUnfulfilled() {
        if(authenticated != false)
        {
        if (debug) {
            // Not a supported method in DummyData, will manually remove here:
            unfulfilled = dummy_data.getOrders();
            for (Order u: unfulfilled) {
                if (u.isFulfilled()) {
                    unfulfilled.remove(u);
                }
            }
        } else { unfulfilled = client_connector.getUnfulfilled(); }
        }
        return unfulfilled;
    }

    //************************************************************************//
    //  -   ARRAYLIST GETTERS                                             -   //
    //************************************************************************//
    
    /**
     * Gets the local copy of the Products ArrayList.
     * @return ArrayList<Product>
     */
    public ArrayList<Product> getProducts() {
        if(authenticated != false)return products;
        return new ArrayList<>();
    }
    

    /**
     * Gets the local copy of the Orders ArrayList.
     * @return ArrayList<Order>
     */
    public ArrayList<Order> getOrders() {
        if(authenticated != false)return orders;
        return new ArrayList<>();
    }

    /**
     * Gets the local copy of the Users ArrayList.
     * @return ArrayList<User>
     */
    public ArrayList<User> getUsers() {
        if(authenticated != false)return users;
        return new ArrayList<>();
    }

    /**
     * Gets the local copy of the categories ArrayList.
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<GenericLookup> getCategories() {
        if(authenticated != false)return categories;
        return new ArrayList<>();
    }

    /**
     * Gets the local copy of the subcategories ArrayList.
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<GenericLookup> getSubCategories() {
        if(authenticated != false)return subCategories;
        return new ArrayList<>();
    }

    /**
     * Gets the local copy of the Order statuses ArrayList
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<GenericLookup> getOrderStatus() {
        if(authenticated != false)return orderStatus;
        return new ArrayList<>();
    }

    /**
     * Gets the local copy of the containers ArrayList.
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<GenericLookup> getContainers() {
        if(authenticated != false)return containers;
        return new ArrayList<>();
    }

    /**
     * Gets the local copy of the locations ArrayList.
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<GenericLookup> getLocations() {
        if(authenticated != false)return locations;
        return new ArrayList<>();
    }

    /**
     * Gets the local copy of the unfulfilled Orders ArrayList.
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<Order> getUnfulfilled() {
        if(authenticated != false)return unfulfilled;
        return new ArrayList<>();
    }
}
