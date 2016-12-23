package Connector;

import Encrypter.Encrypter;
import Encrypter.SymmetricEncrypter;
import NET302JavaLibrary.GenericLookup;
import NET302JavaLibrary.Order;
import NET302JavaLibrary.Product;
import NET302JavaLibrary.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.Type;
import java.net.ProtocolException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
    
    private final   String KEY    = "NET_302_Plym_KEY";
    private final   String SERVER = "http://localhost:8080/NET302_REST/";
    private         String urlEnd;
    private         String urlEndPost;
    private         String urlEndEncrypted;
    private         Encrypter enc;
    
    /**
     * Empty constructor for referencing this class to use.
     */
    public Connector() { /* I lied, not empty */ }
    
    //************************************************************************//
    //  -   UNIVERSAL / KEY METHODS                                       -   //
    //************************************************************************//
    
    /**
     * This method utilises the SymmetricEncrytper() class to send encrypted data
     * via POST, to the REST server. This would need to be changed in the future
     * if Asymmetric encryption could be supported (in progress).
     * @param input List<SimpleEntry> - being the list of 
     * @return 
     */
    private String postEncryptedData(List<SimpleEntry> input){
        String postParams = "?";
        SymmetricEncrypter e = new SymmetricEncrypter();
        String result;
          for (int i = 0; i < input.size(); i++)
          {
              try {
                  String prefix = "";
                  if (i != 0) prefix = "&";
                  postParams += prefix + input.get(i).getKey().toString() + "=" + e.EncryptString(input.get(i).getValue().toString());
              } catch (Exception ex){}
          }
        try {
            
            URL url = new URL( SERVER + urlEndEncrypted + postParams);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod( "POST" );

          

            conn.setDoOutput( true );
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + postParams);
            System.out.println("Response Code : " + responseCode);

            //BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // Get the resulting JSP page as a whole:
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
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
    
    // TODO: JavaDoc here.
    private String postQuery(String[] postParamsArr) throws ProtocolException, IOException
    {
        String postParams = "?";
        String result;
          for(int i = 0; i < postParamsArr.length;i++)
            {
                postParams += postParamsArr[i];
                if(i < postParamsArr.length -1)
                {
                    postParams += "&";
                }
            }
        try {
            
            URL url = new URL( SERVER + urlEndPost + postParams);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod( "POST" );

          

            conn.setDoOutput( true );
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + postParams);
            System.out.println("Response Code : " + responseCode);

            //BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // Get the resulting JSP page as a whole:
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
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
     * OLD: Previous method of communication, does not function with
     * encrypted data due to the nature of the =/& sign usages.
     * Instead, this method is used where no data is sent up!
     * 
     * Helper method to send a query off to the given URL.
     * It will open a HTTP GET Request and returns the first non-blank line 
     * (uses another method to do so)that is found in the response page.
     * Returns the resulting line.
     * @param queryURL String - being the full URL to connect to.
     * @return String - being the first non-blank line from the URL.
     */
    private String SendQuery() {
        String result;
        
        try {
            // Encrypt the queryURL:
            enc = new Encrypter();
            //queryURL = enc.encryptString(queryURL); // commented out for testing.
            String q = enc.encryptString(urlEnd); // left in for testing.
            
            // Connect to the URL:
            URL url = new URL(SERVER + urlEnd);
            HttpURLConnection connect;
            connect = (HttpURLConnection) url.openConnection();
            connect.setReadTimeout(10000);
            connect.setConnectTimeout(15000);
            connect.setRequestMethod("GET");
            connect.setDoOutput(true);
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
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            result = "ERROR: Encrypter Error. Encrypter failed when generating keys or encrypting."
                    + "\n" + ex.getMessage();
        }
        return result;
    }

    //************************************************************************//
    //  -   AUTHENTICATEPOST USER                                         -   //
    //************************************************************************//
    
    /**
     * This method authenticates a user based on the hashed password value given.
     * Returns the User if successful, otherwise NULL - therefore you MUST check
     * for NULL values when using this method!
     * Will log any result to System.err.
     * @param username String - being the username to authenticate with.
     * @param passwordHash String - being the hashed password to authenticate with.
     * @return User - being the User (if correct) else null.
     */
    public User authenticatePost(String username, String passwordHash) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        urlEndEncrypted = "authUser.jsp";

        //String[] postParams = new String[] { "ID="+ username, "HASH="+passwordHash };
        
        // Pass query to URL:
        //String q = postQuery(postParams);
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("ID",username));
        ar.add(new SimpleEntry("HASH",passwordHash));
        String q = postEncryptedData(ar);
        
        // Log the result in case of error message
        System.err.println(q);
        
        if (q.startsWith("ERROR")) { return null; }
        else { return new User(q); }
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
        SymmetricEncrypter e = new SymmetricEncrypter();
        do {
            result = reader.readLine();
                        
        } while (result.length() < 1);
        //return result;
        //Here we decrypt the String:
        try {
            //result = enc.decryptString(result); // commented out for testing.
            result = e.DecryptString(result); // left in for testing.
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex){
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    //************************************************************************//
    //  -   GET + TEST SESSION KEY                                        -   //
    //************************************************************************//
    
    /**
     * Debugging method for Asymmetric encryption. This is now empty as work on
     * this feature was stopped due to limited project time.
     * @return boolean - for testing.
     */
    public boolean testSessionKey() {
        // TODO: Write the test case into the middleware server.
        // TODO: Freshness needs to be addressed fully.
       return false;
    }

    /**
     * This is the initial work on Asymmetric communication. The method is not
     * currently used due to support for this encryption not being completed.
     * The purpose here is to retrieve a session key (in the form of IV), so that
     * the REST server and a client can communicate securely.
     */
    public void getIV() {
        // TODO: Test the method for errors / unexpected results or usages.
        boolean     loaded;
        PrivateKey  client_priv = null;
        PublicKey   client_publ = null;
        
        String      client_code = "";
        
        // Try loading any existing keys:
        try {
            client_priv = Encrypter.loadPrivateKey("client_private.txt");
            client_publ = Encrypter.loadPublicKey("client_public.txt");
            
            // Extra security check for successful load:
            loaded = !(client_priv == null | client_publ == null);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            // ERROR: Issue with stored key here.
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            loaded = false;
        }
        
        // If not loaded, then we will have to generate the keys.
        // Expected usage would be the first time the system is used, or
        // as a security policy these could be remade every 24 hours.
        if (!loaded) {
            try {
                enc.genPPKeys();
                
                client_priv = enc.getPrivKey();
                client_publ = enc.getPublKey();
                
                // Save the keys for next time:
                if (client_priv != null & client_publ != null) {
                    Encrypter.savePrivateKey("client_private.txt", client_priv);
                    Encrypter.savePublicKey("client_public.txt", client_publ);
                }
            } catch (NoSuchAlgorithmException ex) {
                // ERROR: issue with the Encrypter!
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // We now have a set of keys, we can use them to query the REST server:
        if (loaded) {
            urlEnd = "requestPublicKey.jsp?CODE=" + client_code
                    + "&KEY=" + new String(client_publ.getEncoded());

            // Here the result will be the server's public key, encrypted with our public key:
            String result = SendQuery();

            // Log the result in case of error message:
            System.err.println(result);

            try {
                // Decrypt and decode the given public key:
                byte[] resultData = enc.decrypt(client_priv, result.getBytes());

                // Log the result after decryption:
                System.err.println(new String(resultData));
                PublicKey server_publ = Encrypter.decodePublicKey(resultData);
                
                // Query the second page to get the AES IV:
                byte[] codeBytes = enc.encrypt(server_publ, client_code.getBytes());
                urlEnd = "requestSessionKey.jsp?CODE=" +new String(codeBytes);
                result = SendQuery();
                
                // Log the result in case of error message:
                System.err.println(result);
                
                // Now decrypt this result with our private key and get the IV:
                resultData = enc.decrypt(client_priv, result.getBytes());
                
                // Log the resultData:
                System.err.println(new String(resultData));
                
                enc.setIv(resultData);
                enc.saveIV("client_iv.txt");

            } catch (NoSuchAlgorithmException | NoSuchPaddingException | 
                    InvalidKeyException | IllegalBlockSizeException | 
                    BadPaddingException | InvalidKeySpecException ex) {
                
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
        String q = SendQuery();
        
        // Log the result in case of error message:
        System.err.println(q);
        
        // TEST: Convert result using GSON, making sure it appears to be JSON:
        if (!q.startsWith("SUCCESS") & !q.startsWith("ERROR")) {
            Gson gson = new Gson();
            Type token = new TypeToken<ArrayList<Product>>() {}.getType();
            ArrayList<Product> list = gson.fromJson(q, token);
            return list;
        } else { return null; }
    }
    
    /**
     * Returns a list of Products where their name is similar to the searchTerm.
     * @param searchTerm String - being the term to search for.
     * @return ArrayList<Product> - being the list of returned Products.
     */
    public ArrayList<Product> searchProduct(String searchTerm) {
        urlEndEncrypted = "searchProducts.jsp";//?TERM=" + searchTerm;
        
        //String q = SendQuery();
        
        ArrayList<SimpleEntry> ar = new ArrayList<>();
        ar.add(new SimpleEntry("TERM",searchTerm));
        String q = postEncryptedData(ar);
        
        // Log the result in case of error message:
        System.err.println(q);
        
        // TEST: Convert result using GSON, making sure it appears to be JSON:
        if (!q.startsWith("SUCCESS") & !q.startsWith("ERROR")) {
            Gson gson = new Gson();
            Type token = new TypeToken<ArrayList<Product>>() {}.getType();
            ArrayList<Product> list = gson.fromJson(q, token);
            return list;
        } else { return null; }
    }
    
    /**
     * Returns a list of ALL Orders from the database. 
     * @return ArrayList<Order> - being the list of all Orders.
     */
    public ArrayList<Order> getAllOrders() {
        urlEnd = "getOrder.jsp";
        
        // Pass query to URL:
        String o = SendQuery();
        
        // Log the result in case of error message:
        System.err.println(o);
        
        // TEST: Convert result using GSON, making sure it appears to be JSON:
        if (!o.startsWith("SUCCESS") & !o.startsWith("ERROR")) {
            Gson gson = new Gson();
            Type token = new TypeToken<ArrayList<Order>>() {}.getType();
            ArrayList<Order> list = gson.fromJson(o, token);
            return list;
        } else { return null; }
    }
    
    // TODO: THE FOLLOWING METHOD DOES NOT USE ENCRYPTION
    /**
     * Returns a list of unfulfilled Orders from the database.
     * @return ArrayList<Order> - being the list of all Orders.
     */
    public ArrayList<Order> getUnfulfilled() {
        urlEnd = "getFulfilled.jsp";
        
        // Pass query to URL:
        String o = SendQuery();
        
        // Log the result in case of error message:
        System.err.println(o);
        
        // TEST: Convert result using GSON, making sure it appears to be JSON:
        if (!o.startsWith("SUCCESS") & !o.startsWith("ERROR")) {
            Gson gson = new Gson();
            Type token = new TypeToken<ArrayList<Order>>() {}.getType();
            ArrayList<Order> list = gson.fromJson(o, token);
            return list;
        } else { return null; }
    }
    
    /**
     * DEPRECIATED: Support on REST is commented out due to security concerns.
     * Returns a list of ALL Users from the database.
     * @return ArrayList<User> - being the list of all Users.
     */
    public ArrayList<User> getAllUsers() {
        urlEnd = "getUser.jsp";
        
        // Pass query to URL:
        String u = SendQuery();
        
        // Log the result in case of error message:
        System.err.println(u);
        
        // TEST: Convert result using GSON, making sure it appears to be JSON:
        if (!u.startsWith("SUCCESS") & !u.startsWith("ERROR")) {
            Gson gson = new Gson();
            Type token = new TypeToken<ArrayList<User>>() {}.getType();
            ArrayList<User> list = gson.fromJson(u, token);
            return list;
        } else { return null; }
    }
    
    /**
     * Returns a single Product of the given ID from the database.
     * @param id int - being the ID of the Product to return.
     * @return Product - being the requested Product.
     */
    public Product getProduct(int id) {
        urlEndEncrypted = "getProduct.jsp";//?ID=" + id;
        
        // Pass query to URL:
        //String q = SendQuery();
        
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("ID",id));
        String q = postEncryptedData(ar);

        // Log the result in case of error message:
        System.err.println(q);
        
        // TEST: Convert result using GSON, making sure it appears to be JSON:
        if (!q.startsWith("SUCCESS") & !q.startsWith("ERROR")) {
            return new Product(q);
        } else { return null; }
    }
    
    /**
     * Returns a single Order of the given ID from the database.
     * @param id int - being the ID of the Order to return.
     * @return Order - being the requested Order.
     */
    public Order getOrder(int id) {
        urlEndEncrypted = "getOrder.jsp";//?ID=" + id;
        
        // Pass query to URL:
        //String o = SendQuery();
        
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("ID",id));
        String o = postEncryptedData(ar);
        
        // Log the result in case of error message:
        System.err.println(o);
        
        // TEST: Convert result using GSON, making sure it appears to be JSON:
        if (!o.startsWith("SUCCESS") & !o.startsWith("ERROR")) {
            return new Order(o);
        } else { return null; }
    }
    
    /**
     * Returns a specified User of the given ID from the database.
     * @param id int - being the ID of the User to return.
     * @return User - being the requested User.
     */
    public User getUser(int id) {
        urlEndEncrypted = "getUser.jsp";//?ID=" + id;
        
        // Pass query to URL:
        //String u = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("ID",id));
        String u = postEncryptedData(ar);
        
        // Log the result in case of error message:
        System.err.println(u);
        
        // TEST: Convert result using GSON, making sure it appears to be JSON:
        if (!u.startsWith("SUCCESS") & !u.startsWith("ERROR")) {
            return new User(u);
        } else { return null; }
    }

    
    /**
     * Returns a specified User of the given username from the database.
     * @param username String - being the username of the User to return.
     * @return User - being the requested User.
     */
    public User getUser(String username) {
        urlEndEncrypted = "getUser.jsp";//?ID=0&UN=" + username;
        
        // Pass query to URL:
        //String u = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("ID","0"));
        ar.add(new SimpleEntry("UN",username));
        String u = postEncryptedData(ar);
        
        // Log the result in case of error message:
        System.err.println(u);
        
        // TEST: Convert result using GSON, making sure it appears to be JSON:
        if (!u.startsWith("SUCCESS") & !u.startsWith("ERROR")) {
            return new User(u);
        } else { return null; }
    }
    
    /**
     * Sends the given Product through the Middleware to be inserted into the database.
     * @param p Product - being the set of details to insert.
     * @return boolean - being whether or not the operation was successful.
     */
    public boolean addProduct(Product p) {
        urlEndEncrypted = "addProduct.jsp";//?PRODUCT=" + p.GetJSONString() + "&NEW=TRUE";
        
        // Pass query to URL:
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("PRODUCT",p.GetJSONString()));
        ar.add(new SimpleEntry("NEW","TRUE"));
        String result = postEncryptedData(ar);
        
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
        urlEndEncrypted = "addOrder.jsp";//?ORDER=" + o.GetJSONString() + "&NEW=TRUE";
        
        // Pass query to URL:
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("ORDER",o.GetJSONString()));
        ar.add(new SimpleEntry("NEW","TRUE"));
        String result = postEncryptedData(ar);
        
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
        urlEndEncrypted = "addUser.jsp";//?USER=" + u.GetJSONString() + "&NEW=TRUE";
        
        // Pass query to URL:
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("USER",u.GetJSONString()));
        ar.add(new SimpleEntry("NEW","TRUE"));
        String result = postEncryptedData(ar);
        
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
        urlEndEncrypted = "addProduct.jsp";//?PRODUCT=" + p.GetJSONString() + "&NEW=FALSE";
        
        // Pass query to URL:
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("PRODUCT",p.GetJSONString()));
        ar.add(new SimpleEntry("NEW","FALSE"));
        String result = postEncryptedData(ar);
        
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
        urlEndEncrypted = "changeProductQuantity.jsp";//?PRODUCT=" + productID + "?NEWQUANTITY=" + quantity;
        
        // Pass query to URL:
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("PRODUCT",productID));
        ar.add(new SimpleEntry("NEWQUANTITY",String.valueOf(quantity)));
        String result = postEncryptedData(ar);
        
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
        urlEndEncrypted = "addOrder.jsp";//?ORDER=" + o.GetJSONString() + "&NEW=FALSE";
        
        // Pass query to URL:
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("ORDER",o.GetJSONString()));
        ar.add(new SimpleEntry("NEW","FALSE"));
        String result = postEncryptedData(ar);
        
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
        urlEndEncrypted = "fulfillOrder.jsp";//?ORDER=" + orderID;
        
        // Pass query to URL:
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("ORDER",String.valueOf(orderID)));
        String result = postEncryptedData(ar);
        
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
        urlEndEncrypted = "addUser.jsp";//?USER=" + u.GetJSONString() + "&NEW=FALSE";
        
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("USER",u.GetJSONString()));
        ar.add(new SimpleEntry("NEW","FALSE"));
        String result = postEncryptedData(ar);
        
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
            urlEndEncrypted = "getLookups.jsp";//?IDENTIFIER=" + identifier;
            
            // Pass query to URL:
            //String l = SendQuery();
            ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
            ar.add(new SimpleEntry("IDENTIFIER",identifier));
            String l = postEncryptedData(ar);
            
            // Log the result in case of error message:
            System.err.println(l);

            // If NOT starting with ERROR (or SUCCESS to be thorough) then...
            if (!l.startsWith("ERROR") & !l.startsWith("SUCCESS")) {
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
            urlEndEncrypted = "addLookup.jsp";//?IDENTIFIER=" + identifier + "&LOOKUP=" + l.GetJSONString() + "&NEW=FALSE";
            
            // Pass query to URL:
            //String result = SendQuery();
            ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
            ar.add(new SimpleEntry("IDENTIFIER",identifier));
            ar.add(new SimpleEntry("LOOKUP",l.GetJSONString()));
            ar.add(new SimpleEntry("NEW","FALSE"));
            String result = postEncryptedData(ar);
            
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
        urlEndEncrypted = "addLookup.jsp";//?IDENTIFIER=location&LOOKUP=" + l.GetJSONString() + "?NEW=TRUE";
        
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("IDENTIFIER",location));
        ar.add(new SimpleEntry("LOOKUP",l.GetJSONString()));
        ar.add(new SimpleEntry("NEW","TRUE"));
        String result = postEncryptedData(ar);        
        
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
        urlEndEncrypted = "addLookup.jsp";//?IDENTIFIER=location&LOOKUP=" + l.GetJSONString() + "?NEW=TRUE";
       
        //String result = SendQuery();
        
        // Log the result in case of error message:
        //System.err.println(result);
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("IDENTIFIER","location"));
        ar.add(new SimpleEntry("LOOKUP",l.GetJSONString()));
        ar.add(new SimpleEntry("NEW","FALSE"));
        String result = postEncryptedData(ar);
        
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
        urlEndEncrypted = "addLookup.jsp";//?IDENTIFIER=location&LOOKUP=" + l.GetJSONString() + "?NEW=TRUE";
        
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("IDENTIFIER","location"));
        ar.add(new SimpleEntry("LOOKUP",l.GetJSONString()));
        ar.add(new SimpleEntry("NEW","TRUE"));
        String result = postEncryptedData(ar);
        
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
        urlEndEncrypted = "addLookup.jsp";//?IDENTIFIER=location&LOOKUP=" + l.GetJSONString() + "?NEW=TRUE";
        
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("IDENTIFIER","location"));
        ar.add(new SimpleEntry("LOOKUP",l.GetJSONString()));
        ar.add(new SimpleEntry("NEW","TRUE"));
        String result = postEncryptedData(ar);
        
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
        urlEndEncrypted = "addLookup.jsp";//?IDENTIFIER=location&LOOKUP=" + l.GetJSONString() + "?NEW=TRUE";
        
        //String result = SendQuery();
        ArrayList<SimpleEntry> ar = new ArrayList<SimpleEntry>();
        ar.add(new SimpleEntry("IDENTIFIER","location"));
        ar.add(new SimpleEntry("LOOKUP",l.GetJSONString()));
        ar.add(new SimpleEntry("NEW","TRUE"));
        String result = postEncryptedData(ar);
        
        // Log the result in case of error message:
        System.err.println(result);
        
        return result.startsWith("SUCCESS");
    }
}
