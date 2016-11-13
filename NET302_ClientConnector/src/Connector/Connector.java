/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connector;

import NET302JavaLibrary.GenericLookup;
import NET302JavaLibrary.Order;
import NET302JavaLibrary.Product;
import NET302JavaLibrary.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.Type;

/**
 *
 * @author Sam
 */
public class Connector {
    //************************************************************************//
    //  -   SET-UP OF CLASS                                               -   //
    //************************************************************************//
    
    private static final    String SERVER = "TODO: Specify the server";
    private                 String urlEnd;

    /**
     * Empty constructor for referencing this class to use.
     */
    public Connector() { /* I lied, not empty */ }
    
    /**
     * 
     * @param queryURL
     * @return 
     */
    private String SendQuery(String queryURL) {
        String result;
        
        try {
            // Connect to the URL:
            URL url = new URL(queryURL);
            HttpURLConnection connect;
            connect = (HttpURLConnection) url.openConnection();
            connect.setReadTimeout(10000);
            connect.setConnectTimeout(15000);
            connect.setRequestMethod("GET");
            connect.setChunkedStreamingMode(0);
            connect.setDoInput(true);
            connect.connect();
            
            // Get the resulting JSP page:
            BufferedReader in = new BufferedReader(
                new InputStreamReader(connect.getInputStream(), "UTF-8"));
            
            // Get the actual data using helper method:
            result = getFirstLine(in);
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            result = "ERROR: URL error. Connector failed to reach specified URL."
                    + "\n" + ex.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            result = "ERROR: Reader Error. Connector failed to use BufferedReader on resulting query."
                    + "\n" + ex.getMessage();
        }
        return result;
    }
    
    /**
     * Helper method to return the first non-blank line of a BufferedReader.
     * This removes white-spacing that will otherwise appear at the start of 
     * the JSP page.
     * @param reader - The reader to find the non-blank line in.
     * @return - String being the line it found. This will be GSON or server error.
     * @throws IOException - IOException can be handled wherever this is used as
     * a buffered reader is still required.
     */
    private String getFirstLine(BufferedReader reader) throws IOException {
        String result = "";
        
        do {
            result = reader.readLine();
        } while (result.length() < 1);
        return result;
    }
    
    //************************************************************************//
    //  -   GET / ADD / UPDATE ITEMS METHODS                              -   //
    //************************************************************************//
    
    // TODO: On all of the below, make a connection and convert the result to
    // what we expect. USE TOKENS (?).
    
    public ArrayList<Product> getAllProducts() {
        urlEnd = "getProduct.jsp";
        
        String q = SendQuery(SERVER + urlEnd);
        
        // TEST: unsure if correct, guidance say this is how but
        // it doesn't use the toGson method in the generic lookup.
        Gson gson = new Gson();
        Type token = new TypeToken<ArrayList<Product>>() {}.getType();
        ArrayList<Product> list = gson.fromJson(q, token);
        
        return list;
    }
    
    public ArrayList<Order> getAllOrders() {
        urlEnd = "getOrder.jsp";
        
        String o = SendQuery(SERVER + urlEnd);
        
        // TEST: unsure if correct, guidance say this is how but
        // it doesn't use the toGson method in the generic lookup.
        Gson gson = new Gson();
        Type token = new TypeToken<ArrayList<Order>>() {}.getType();
        ArrayList<Order> list = gson.fromJson(o, token);
        
        return list;
    }
    
    public ArrayList<User> getAllUsers() {
        urlEnd = "getUser.jsp";
        
        String u = SendQuery(SERVER + urlEnd);
        
        // TEST: unsure if correct, guidance say this is how but
        // it doesn't use the toGson method in the generic lookup.
        Gson gson = new Gson();
        Type token = new TypeToken<ArrayList<User>>() {}.getType();
        ArrayList<User> list = gson.fromJson(u, token);
        
        return list;
    }
    
    // TEST: This should work, but not sure if need to do one more step?
    public Product getProduct(int id) {
        urlEnd = "getProduct?ID=" + id;
        
        String q = SendQuery(SERVER + urlEnd);
        
        return new Product(q);
    }
    
    // TEST: This should work, but not sure if need to do one more step?
    public Order getOrder(int id) {
        urlEnd = "getOrder.jsp?ID=" + id;
        
        String o = SendQuery(SERVER + urlEnd);
        
        return new Order(o);
    }
    
    // TEST: This should work, but not sure if need to do one more step?
    public User getUser(int id) {
        urlEnd = "getUser.jsp?ID=" + id;
        
        String u = SendQuery(SERVER + urlEnd);
        
        return new User(u);
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean addProduct(Product p) {
        urlEnd = "addProduct?PRODUCT=" + p.GetJSONString() + "&NEW=TRUE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        return result.equals("SUCCESS");
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean addOrder(Order o) {
        urlEnd = "addOrder?ORDER=" + o.GetJSONString() + "&NEW=TRUE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        return result.equals("SUCCESS");
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean addUser(User u) {
        urlEnd = "addUser?USER=" + u.GetJSONString() + "&NEW=TRUE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        return result.equals("SUCCESS");
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean updateProduct(Product p) {
        urlEnd = "addProduct?PRODUCT=" + p.GetJSONString() + "&NEW=FALSE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        return result.equals("SUCCESS");
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean updateOrder(Order o) {
        urlEnd = "addOrder?ORDER=" + o.GetJSONString() + "&NEW=FALSE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        return result.equals("SUCCESS");
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean updateUser(User u) {
        urlEnd = "addUser?USER=" + u.GetJSONString() + "&NEW=FALSE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        return result.equals("SUCCESS");
    }
    
    //************************************************************************//
    //  -   MANAGE LOOKUP ITEMS                                           -   //
    //************************************************************************//
    
    public ArrayList<GenericLookup> getAllLookups(String identifier) {
       
        if (identifier.equals("category")
                    || identifier.equals("subcategory")
                    || identifier.equals("container")
                    || identifier.equals("location")
                    || identifier.equals("status")) {
            urlEnd = "getLookups?IDENTIFIER=" + identifier;
            
            // TODO: connect to server, fetch GSON and convert it to an object.
            
            // TODO: handle input error, return properly.
            
        } else { return null; }
        return null;
    }
    
    public boolean addLocation(String location) {
        // TODO: add middleware function then this is possible.
        return false;
    }
    
    public boolean addCategory(String category) {
        // TODO: add middleware function then this is possible.
        return false;
    }
    
    public boolean addSubCat(String category) {
        // TODO: add middleware function then this is possible.
        return false;
    }
    
    public boolean addOrderStatus(String status) {
        // TODO: add middleware function then this is possible.
        return false;
    }
}
