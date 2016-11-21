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

// TODO: in theory everything pretty much works? needs to be tested really.

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
    //  -   AUTHENTICATE USER + OTHER ASSORTED METHOD                     -   //
    //************************************************************************//
    
    public boolean Authenticate(User user, String hash) {
        urlEnd = "authUser.jsp?ID=" + user.getID() + "?HASH=" + hash;
        
        // Pass query to URL:
        String q = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(q);
        
        // Check for the result being SUCCESS or not, and return boolean:
        return q.startsWith("SUCCESS");
    }
    
    //************************************************************************//
    //  -   GET / ADD / UPDATE ITEMS METHODS                              -   //
    //************************************************************************//
    
    public ArrayList<Product> getAllProducts() {
        urlEnd = "getProduct.jsp";
        
        // Pass query to URL:
        String q = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(q);
        
        // TEST: Convert result using GSON:
        Gson gson = new Gson();
        Type token = new TypeToken<ArrayList<Product>>() {}.getType();
        ArrayList<Product> list = gson.fromJson(q, token);
        
        return list;
    }
    
    public ArrayList<Order> getAllOrders() {
        urlEnd = "getOrder.jsp";
        
        // Pass query to URL:
        String o = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(o);
        
        // TEST: Convert result using GSON:
        Gson gson = new Gson();
        Type token = new TypeToken<ArrayList<Order>>() {}.getType();
        ArrayList<Order> list = gson.fromJson(o, token);
        
        return list;
    }
    
    public ArrayList<User> getAllUsers() {
        urlEnd = "getUser.jsp";
        
        // Pass query to URL:
        String u = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(u);
        
        // TEST: Convert result using GSON:
        Gson gson = new Gson();
        Type token = new TypeToken<ArrayList<User>>() {}.getType();
        ArrayList<User> list = gson.fromJson(u, token);
        
        return list;
    }
    
    // TEST: This should work, but not sure if need to do one more step?
    public Product getProduct(int id) {
        urlEnd = "getProduct?ID=" + id;
        
        // Pass query to URL:
        String q = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(q);
        
        return new Product(q);
    }
    
    // TEST: This should work, but not sure if need to do one more step?
    public Order getOrder(int id) {
        urlEnd = "getOrder.jsp?ID=" + id;
        
        // Pass query to URL:
        String o = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(o);
        
        return new Order(o);
    }
    
    // TEST: This should work, but not sure if need to do one more step?
    public User getUser(int id) {
        urlEnd = "getUser.jsp?ID=" + id;
        
        // Pass query to URL:
        String u = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(u);
        
        return new User(u);
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean addProduct(Product p) {
        urlEnd = "addProduct?PRODUCT=" + p.GetJSONString() + "&NEW=TRUE";
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean addOrder(Order o) {
        urlEnd = "addOrder?ORDER=" + o.GetJSONString() + "&NEW=TRUE";
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean addUser(User u) {
        urlEnd = "addUser?USER=" + u.GetJSONString() + "&NEW=TRUE";
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean updateProduct(Product p) {
        urlEnd = "addProduct?PRODUCT=" + p.GetJSONString() + "&NEW=FALSE";
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        return result.startsWith("SUCCESS");
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean updateOrder(Order o) {
        urlEnd = "addOrder?ORDER=" + o.GetJSONString() + "&NEW=FALSE";
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    // TEST: This should work, but not sure about the resulting boolean.
    public boolean updateUser(User u) {
        urlEnd = "addUser?USER=" + u.GetJSONString() + "&NEW=FALSE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
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
            
            // Pass query to URL:
            String l = SendQuery(SERVER + urlEnd);
            
            // Log the result in case of error message:
            System.err.println(l);

            // If NOT starting with ERROR then continue...
            if (!l.startsWith("ERROR")) {
                // TEST: Convert result using GSON:
                Gson gson = new Gson();
                Type token = new TypeToken<ArrayList<User>>() {}.getType();
                ArrayList<GenericLookup> list = gson.fromJson(l, token);
                
                return list;
                
            } else  {
                // Log the result in case of error message:
                System.err.println(l);
                return null;
            }
        } else { 
            // Eroor
            System.err.println("ERROR: Identifier given to the method getAllLookups"
                    + "is incorrect! Please review the JavaDoc.");
            return null; 
        }
    }
    
    public boolean addLocation(String location) {
        // Create lookup with ID of 1, it won't be used but just encapsulates the data.
        GenericLookup l = new GenericLookup(1, location);
        urlEnd = "addLookup.jsp?IDENTIFIER=location?LOOKUP=" + l.GetJSONString() + "?NEW=FALSE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    public boolean addCategory(String category) {
        // Create lookup with ID of 1, it won't be used but just encapsulates the data.
        GenericLookup l = new GenericLookup(1, category);
        urlEnd = "addLookup.jsp?IDENTIFIER=location?LOOKUP=" + l.GetJSONString() + "?NEW=FALSE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    public boolean addSubCat(String subcat) {
        // Create lookup with ID of 1, it won't be used but just encapsulates the data.
        GenericLookup l = new GenericLookup(1, subcat);
        urlEnd = "addLookup.jsp?IDENTIFIER=location?LOOKUP=" + l.GetJSONString() + "?NEW=FALSE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    public boolean addOrderStatus(String status) {
        // Create lookup with ID of 1, it won't be used but just encapsulates the data.
        GenericLookup l = new GenericLookup(1, status);
        urlEnd = "addLookup.jsp?IDENTIFIER=location?LOOKUP=" + l.GetJSONString() + "?NEW=FALSE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    public boolean addContainer(String container) {
        // Create lookup with ID of 1, it won't be used but just encapsulates the data.
        GenericLookup l = new GenericLookup(1, container);
        urlEnd = "addLookup.jsp?IDENTIFIER=location?LOOKUP=" + l.GetJSONString() + "?NEW=FALSE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
}
