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

// TODO: Full testing of methods.
// TODO: Be careful of the GSON conversion on ArrayLists, this is the most
//       uncertain part of the class!

/**
 * Connector class provides a client-side functionality for the REST Middleware
 * for the NET302 project. It effectively provides methods which build the URL
 * query and connect to the page, returning the result as the objects in the
 * NET302JavaLibrary.
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
     * Helper method to send a query off to the given URL.
     * It will open a HTTP GET Request and returns the first non-blank line 
     * (uses another method to do so)that is found in the response page.
     * Returns the resulting line.
     * @param queryURL String - being the full URL to connect to.
     * @return String - being the first non-blank line from the URL.
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
            
            // Get the resulting JSP page as a whole:
            BufferedReader in = new BufferedReader(
                new InputStreamReader(connect.getInputStream(), "UTF-8"));
            
            // Get the actual data using helper method (skips non-blank):
            result = getFirstLine(in);
            
        } catch (MalformedURLException ex) {
            // URL Error - Straightforward
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            result = "ERROR: URL error. Connector failed to reach specified URL."
                    + "\n" + ex.getMessage();
        } catch (IOException ex) {
            // Reader error - Poor error message so may need to be corrected!
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
        String result = "";     // Initialised for return.
        
        do {
            result = reader.readLine();
        } while (result.length() < 1);
        return result;
    }
    
    //************************************************************************//
    //  -   AUTHENTICATE USER + OTHER ASSORTED METHOD                     -   //
    //************************************************************************//
    
    /**
     * This method authenticates a user based on the hashed password value given.
     * Returns true or false is successful.
     * Will log any result to System.err.
     * @param user User - being the User object representing the user to authenticate.
     * @param hash String - being the hashed string to authenticate user against.
     * @return boolean - being whether or not the authentication is successful.
     */
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
    
    /**
     * Returns a list of ALL Products from the database.
     * @return ArrayList<Product> - being the list of all Products.
     */
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
    
    /**
     * Returns a list of Products where their name is similar to the searchTerm.
     * @param searchTerm String - being the term to search for.
     * @return ArrayList<Product> - being the list of returned Products.
     */
    public ArrayList<Product> searchProduct(String searchTerm) {
        urlEnd = "searchProducts.jsp?TERM=" + searchTerm;
        
        String q = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(q);
        
        // TEST: Convert result using GSON:
        Gson gson = new Gson();
        Type token = new TypeToken<ArrayList<Product>>() {}.getType();
        ArrayList<Product> list = gson.fromJson(q, token);
        
        return list;
    }
    
    /**
     * Returns a list of ALL Orders from the database. 
     * @return ArrayList<Order> - being the list of all Orders.
     */
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
    
    /**
     * Returns a list of unfulfilled Orders from the database.
     * @return ArrayList<Order> - being the list of all Orders.
     */
    public ArrayList<Order> getUnfulfilled() {
        urlEnd = "getFulfilled.jsp";
        
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
    
    /**
     * Returns a list of ALL Users from the database.
     * @return ArrayList<User> - being the list of all Users.
     */
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
    
    /**
     * Returns a single Product of the given ID from the database.
     * @param id int - being the ID of the Product to return.
     * @return Product - being the requested Product.
     */
    public Product getProduct(int id) {
        urlEnd = "getProduct.jsp?ID=" + id;
        
        // Pass query to URL:
        String q = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(q);
        
        return new Product(q);
    }
    
    /**
     * Returns a single Order of the given ID from the database.
     * @param id int - being the ID of the Order to return.
     * @return Order - being the requested Order.
     */
    public Order getOrder(int id) {
        urlEnd = "getOrder.jsp?ID=" + id;
        
        // Pass query to URL:
        String o = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(o);
        
        return new Order(o);
    }
    
    /**
     * Returns a specified User of the given ID from the database.
     * @param id int - being the ID of the User to return.
     * @return User - being the requested User.
     */
    public User getUser(int id) {
        urlEnd = "getUser.jsp?ID=" + id;
        
        // Pass query to URL:
        String u = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(u);
        
        return new User(u);
    }
    
    /**
     * Returns a specified User of the given username from the database.
     * @param username String - being the username of the User to return.
     * @return User - being the requested User.
     */
    public User getUser(String username) {
        urlEnd = "getUser.jsp?ID=0&UN=" + username;
        
        // Pass query to URL:
        String u = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(u);
        
        return new User(u);
    }
    
    /**
     * Sends the given Product through the Middleware to be inserted into the database.
     * @param p Product - being the set of details to insert.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean addProduct(Product p) {
        urlEnd = "addProduct.jsp?PRODUCT=" + p.GetJSONString() + "&NEW=TRUE";
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    /**
     * Sends the given Order through the Middleware to be inserted into the database.
     * @param o Order - being the set of details to insert.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean addOrder(Order o) {
        urlEnd = "addOrder.jsp?ORDER=" + o.GetJSONString() + "&NEW=TRUE";
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    /**
     * Sends the given User through the Middleware to be inserted into the database.
     * @param u User - being the set of details to insert.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean addUser(User u) {
        urlEnd = "addUser.jsp?USER=" + u.GetJSONString() + "&NEW=TRUE";
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    /**
     * Sends the given Product through the Middleware to be updated on the database.
     * @param p Product - being the set of details to update (on current ID value).
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean updateProduct(Product p) {
        urlEnd = "addProduct.jsp?PRODUCT=" + p.GetJSONString() + "&NEW=FALSE";
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    /**
     * Updates the quantity of a given Product using it's ID.
     * @param productID int - being the ID of the Product to update.
     * @param quantity int - being the quantity to set.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean setQuantity(int productID, int quantity) {
        urlEnd = "changeProductQuantity.jsp?PRODUCT=" + productID 
                + "?NEWQUANTITY=" + quantity;
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    /**
     * Sends the given Order through the Middleware to be updated on the database.
     * @param o Order - being the set of details to update (on current ID value).
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean updateOrder(Order o) {
        urlEnd = "addOrder.jsp?ORDER=" + o.GetJSONString() + "&NEW=FALSE";
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    } 
    
    /**
     * Sets the given Order to be fulfilled (status change).
     * @param orderID int - being the ID of the Order to change.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean fulfillOrder(int orderID) {
        urlEnd = "fulfillOrder.jsp?ORDER=" + orderID;
        
        // Pass query to URL:
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    /**
     * Sends the User through the Middleware to be updated on the database.
     * @param u User - being the set of details to update (on current ID value).
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean updateUser(User u) {
        urlEnd = "addUser.jsp?USER=" + u.GetJSONString() + "&NEW=FALSE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }

    //************************************************************************//
    //  -   MANAGE LOOKUP ITEMS                                           -   //
    //************************************************************************//
    
    /**
     * Returns ALL lookup objects from a specified lookup table.
     * @param identifier String - being the identifier for the lookup table.
     * SELECT ONE: category, subcategory, container, location, status.
     * @return ArrayList<GenericLookup> - being the list of all lookups from the
     * specified table.
     */
    public ArrayList<GenericLookup> getAllLookups(String identifier) {
       
        if (identifier.equals("category")
                    || identifier.equals("subcategory")
                    || identifier.equals("container")
                    || identifier.equals("location")
                    || identifier.equals("status")) {
            // Construct the URL:
            urlEnd = "getLookups.jsp?IDENTIFIER=" + identifier;
            
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
            // Error with parameters:
            System.err.println("ERROR: Identifier given to the method getAllLookups"
                    + "is incorrect! Please review the JavaDoc.");
            return null; 
        }
    }
    
    /**
     * 
     * @param l
     * @param identifier String - being the identifier for the lookup table.
     * SELECT ONE: category, subcategory, container, location, status.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean updateLookup(GenericLookup l, String identifier) {
        if (identifier.equals("category")
                    || identifier.equals("subcategory")
                    || identifier.equals("container")
                    || identifier.equals("location")
                    || identifier.equals("status")) {
            // Construct the URL:
            urlEnd = "addLookup.jsp?IDENTIFIER=" + identifier 
                    + "?LOOKUP=" + l.GetJSONString()
                    + "?NEW=FALSE";
            
            // Pass query to URL:
            String result = SendQuery(SERVER + urlEnd);
            
            // Log the result in case of error message:
            System.err.println(result);
            
            return result.startsWith("SUCCESS");
        } else {
            // Error with parameters:
            System.err.println("ERROR: Identifier given to the method getAllLookups"
                    + "is incorrect! Please review the JavaDoc.");
            return false;
        }
    }
    
    /**
     * Sends a new Location value to be added to the database.
     * @param location String - being the value to add.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean addLocation(String location) {
        // Create lookup with ID of 1, it won't be used but just encapsulates the data.
        GenericLookup l = new GenericLookup(1, location);
        urlEnd = "addLookup.jsp?IDENTIFIER=location?LOOKUP=" + l.GetJSONString() + "?NEW=TRUE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    /**
     * Sends a new Category value to be added to the database.
     * @param category String - being the value to add.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean addCategory(String category) {
        // Create lookup with ID of 1, it won't be used but just encapsulates the data.
        GenericLookup l = new GenericLookup(1, category);
        urlEnd = "addLookup.jsp?IDENTIFIER=location?LOOKUP=" + l.GetJSONString() + "?NEW=TRUE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    /**
     * Sends a new SubCategory value to be added to the database.
     * @param subcat String - being the value to add.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean addSubCat(String subcat) {
        // Create lookup with ID of 1, it won't be used but just encapsulates the data.
        GenericLookup l = new GenericLookup(1, subcat);
        urlEnd = "addLookup.jsp?IDENTIFIER=location?LOOKUP=" + l.GetJSONString() + "?NEW=TRUE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    /**
     * Adds a new OrderStatus value to be added to the database.
     * @param status String - being the value to add.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean addOrderStatus(String status) {
        // Create lookup with ID of 1, it won't be used but just encapsulates the data.
        GenericLookup l = new GenericLookup(1, status);
        urlEnd = "addLookup.jsp?IDENTIFIER=location?LOOKUP=" + l.GetJSONString() + "?NEW=TRUE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
    
    /**
     * Adds a new Container value to be added to the database.
     * @param container String - being the value to add.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean addContainer(String container) {
        // Create lookup with ID of 1, it won't be used but just encapsulates the data.
        GenericLookup l = new GenericLookup(1, container);
        urlEnd = "addLookup.jsp?IDENTIFIER=location?LOOKUP=" + l.GetJSONString() + "?NEW=TRUE";
        
        String result = SendQuery(SERVER + urlEnd);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
}
