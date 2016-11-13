package NET302_Handlers;

import NET302JavaLibrary.GenericLookup;
import NET302JavaLibrary.Order;
import NET302JavaLibrary.Product;
import NET302JavaLibrary.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: JavaDoc comments throughout.

/**
 * Handles connections from the JSP pages (who accept connections from clients)
 * to them provide data from the AWS connection.
 * @author Samuel Bewick
 */
public class DB_Handler {
    //************************************************************************//
    //  -   SET-UP OF CLASS                                               -   //
    //************************************************************************//
    
    // DATABASE CONNECTION DETAILS:
    private final String    db_url      = "net302.cli7vsmzzdht.us-west-2.rds.amazonaws.com:3306/";
    private final String    db_name     = "MyDatabase";
    private final String    user_id     = "root";
    private final String    password    = "password1";
    private final String    driver      = "com.mysql.jdbc.Driver";
    
    // SHORT VARIABLES FOR LONG TABLE NAMES:
    private final String    p_sub       = "NET302.Product_Subcategory";
    private final String    p_cat       = "NET302.Product_Category";
    private final String    p_con       = "NET302.Product_Container";
    private final String    p_loc       = "NET302.Location";
    private final String    o_sta       = "NET302.Order_Status";
    private final String    s_typ       = "NET302.Staff_Type";
    
    // BASE VARIABLES FOR THE CLASS TO USE:
    private Connection  connection;
    private ResultSet   resultSet;
    
    // PREPARED STATEMENTS:
    // Laid out as demonstrated in https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
    private PreparedStatement   getProduct      = null;
    private PreparedStatement   getOrder        = null;
    private PreparedStatement   getUser         = null;
    private PreparedStatement   updateOrder     = null;
    private PreparedStatement   updateProduct   = null;
    private PreparedStatement   updateUser      = null;
    private PreparedStatement   addProduct      = null;
    private PreparedStatement   addOrder        = null;
    private PreparedStatement   addUser         = null;
    private PreparedStatement   allProducts     = null;
    private PreparedStatement   allOrders       = null;
    private PreparedStatement   allUsers        = null;
    private PreparedStatement   findID          = null;
    private PreparedStatement   getLookups      = null;
    private PreparedStatement   addLookup       = null;
    private PreparedStatement   updateLookup    = null; 
    private PreparedStatement   authUser        = null;
    
    // SQL QUERY STRINGS:
    // ';' is included in the strings, 
    // but may be included in the prepared statement anyway.
    
    private final String        getProductQ     =
            "SELECT (Products.ID) as ID, "
            + "stockCount, "
            + "prodName as name, "
            + "available, "
            + "unitPrice, "
            + "(category.categoryVal) as category, "
            + "(category.ID) as category_id, "
            + "(subcat.subcatVal) as subCategory, "
            + "(subcat.ID) as subCategory_id, "
            + "(container.containerVal) as container, "
            + "(container.ID) as container_id, "
            
            // FROM / JOIN / WHERE:
            + "FROM NET302.Products "
            + "JOIN " + p_cat + " ON Products.categoryID = " + p_cat + ".ID "
            + "JOIN " + p_sub + " ON Products.subcatID = " + p_sub + ".ID "
            + "JOIN " + p_con + " ON Products.containerID = " + p_con + ".ID "
            + "WHERE NET302.Products.ID = ?;"; // ID last.
    
    private final String        getOrderQ       =
            "SELECT (Orders.ID) as ID, "
            + "quantity, "
            + "TO_CHAR(dateOrdered, 'DD/MM/YYYY') as dateOrdered, "
            + "(staff.ID) as staffOrdered, "
            + "(Products.ID) as productID, "
            + "(Location.ID) as loc_id, "
            + "(Location.locationVal) as loc_value, "
            + "(" + o_sta + ".ID) as status_id, "
            + "(" + o_sta + ".statusName) as status_value, "
            + "TO_CHAR(dateDelivered, 'DD/MM/YYYY') as dateDelivered, "
            //+ "timeDelivered, " // MISSING FROM TABLE.
            // TODO: Time may need a mask, but wait to see how it returns first.
            
            // FROM + JOINS + WHERE:
            + "FROM NET302.Orders "
            + "JOIN " + o_sta + " ON Orders.statusID = " + o_sta + ".ID "
            + "JOIN NET302.Staff ON Orders.staffID = Staff.staffID "
            + "JOIN NET302.Products ON Orders.productID = Products.ID "
            + "WHERE NET302.Orders.ID = ?;";   // ID last.
    
    private final String        getUserQ        =
            "SELECT Staff.ID as ID, "
            + "username, "
            + "staffContact as contact, "
            + "staffName as name, "
            
            // Get the Staff_Type details:
            + "Staff.staffType as type_id, "
            + "staffType as type_value, "
            
            // FROM + WHERE:
            + "FROM NET302.Staff "
            + "JOIN " + s_typ + " ON Staff.staffType = " + s_typ + ".ID "
            + "WHERE ID = ?;";                  // ID last.
    
    private final String        updateProductQ  =
            "UPDATE NET302.Products SET "
            + "stockCount = ?, "
            + "prodName = ?, "
            + "available = ?, "
            + "unitPrice = ?, "
            + "categoryID = ?, "
            + "subCatID = ?, "
            + "containerID = ?, "
            + "WHERE NET302.Products.ID = ?;"; // ID last.
    
    private final String        updateOrderQ    =
            "UPDATE NET302.Orders SET "
            + "quantity = ?, "
            + "dateOrdered = ?, "
            + "staffID = ?, "
            + "productID = ?, "
            + "locationID = ?, "
            + "statusID = ?, "
            + "dateDelivered = ?, "
            //+ "timeDelivered = ?, " // MISSING FROM TABLE?
            + "WHERE NET302.Orders.ID = ?;";   // ID last.
    
    private final String        updateUserQ     =
            "UPDATE NET302.Staff SET "
            + "username = ?, "                  // TODO: is this changable?
            + "staffContact = ? "
            + "staffName = ?, "
            + "staffType = ?, "
            + "WHERE NET302.Staff.ID = ?;";    // ID last.
    
    private final String        addProductQ     =
            "INSERT INTO Products ("
            + "stockCount, prodName, available, unitPrice, categoryID, subCatID, containerID) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?);";

    private final String        addOrderQ       =
            "INSERT INTO orders ("
            + "quantity, dateOrdered, staffID, productID, locationID, statusID)"
            + " VALUES (?, GETDATE(), ?, ?, ?, ?);";
    
    private final String        addUserQ        =
            "INSERT INTO staff ("
            + "username, staffContact, staffName, staffType, password) "
            + "VALUES (?, ?, ?, ?, ?);";
    
    
    private final String        getAllProductsQ =
            "SELECT (Products.ID) as ID, "
            + "stockCount, "
            + "prodName as name, "
            + "available, "
            + "unitPrice, "
            + "(category.categoryVal) as category, "
            + "(category.ID) as category_id, "
            + "(subcat.subcatVal) as subCategory, "
            + "(subcat.ID) as subCategory_id, "
            + "(container.containerVal) as container, "
            + "(container.ID) as container_id, "
            
            // FROM / JOIN / WHERE:
            + "FROM NET302.Products "
            + "JOIN " + p_cat + " ON Products.categoryID = " + p_cat + ".ID "
            + "JOIN " + p_sub + " ON Products.subcatID = " + p_sub + ".ID "
            + "JOIN " + p_con + " ON Products.containerID = " + p_con + ".ID "
            + "ORDER BY ID;";
    
    private final String        getAllOrdersQ   =
            "SELECT (Orders.ID) as ID, "
            + "quantity, "
            + "TO_CHAR(dateOrdered, 'DD/MM/YYYY') as dateOrdered, "
            + "(staff.ID) as staffOrdered, "
            + "(Products.ID) as productID, "
            + "(Location.ID) as loc_id, "
            + "(Location.locationVal) as loc_value, "
            + "(" + o_sta + ".ID) as status_id, "
            + "(" + o_sta + ".statusName) as status_value, "
            + "TO_CHAR(dateDelivered, 'DD/MM/YYYY') as dateDelivered, "
            //+ "timeDelivered, " // MISSING FROM TABLE.
            // TODO: Time may need a mask, but wait to see how it returns first.
            
            // FROM + JOINS + WHERE:
            + "FROM NET302.Orders "
            + "JOIN " + o_sta + " ON Orders.statusID = " + o_sta + ".ID "
            + "JOIN NET302.Staff ON Orders.staffID = Staff.staffID "
            + "JOIN NET302.Products ON Orders.productID = Products.ID "
            + "ORDER BY ID;";
    
    private final String        getAllUsersQ    =
            "SELECT Staff.ID as ID, "
            + "username, "
            + "staffContact as contact, "
            + "staffName as name, "
            
            // Get the Staff_Type details:
            + "Staff.staffType as type_id, "
            + "staffType as type_value, "
            
            // FROM + WHERE:
            + "FROM NET302.Staff "
            + "JOIN " + s_typ + " ON Staff.staffType = " + s_typ + ".ID "
            + "ORDER BY ID;";
    
    private final String        getAllLookupsQ  =
            "SELECT ID as ID,"
            + "? as VALUE"
            + "FROM ?"
            + "ORDER BY ID;";
    
    private final String        addLookupQ      =
            "INSERT INTO ? (?) VALUES (?);";
    
    private final String        updateLookupQ   =
            "UPDATE ? SET ? = ? WHERE ID = ?;";
    
    // TODO: write this query.
    private final String        authUserQ       =
            "";
    
    // Empty Constructor - allows instantiating for multiple connections.
    public DB_Handler() { /* empty */ }
    
    //************************************************************************//
    //  -   CONNECTION AND DATABASE DRIVER METHODS                        -   //
    //************************************************************************//
    
    /**
     * Gets a new connection to the database, must be closed when done with!
     * @return boolean - used for testing if connection was successful or not.
     */
    public boolean GetConnection() {
        // Presume true here, as the catch's alter this value.
        boolean successful = true;
        
        try { 
            // Prepare URL by combining elements:
            String url = "jdbc:mysql://" + db_url + db_name;

            // Load the driver and establish a connection:
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(url, user_id, password);

            // This is the suggested way but cannot get it to work :(
            //MysqlDataSource dSource = new MysqlDataSource();
            //dSource.setUser(user_id);
            //dSource.setPassword(password);
            //dSource.setServerName(db_url + db_name);
            //connection = dSource.getConnection();
            
            // Write something to the log to state successful:
            if (successful) {
                System.err.println("INFO: Connection established successfully.");
            }
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            // Set successful to false and log the errors:
            successful = false;
            Logger.getLogger(DB_Handler.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("ERROR: " + ex.getLocalizedMessage());
        }
        // Note the return, we need to test to make sure a connection success:
        return successful;
    }
    
    /**
     * Close any active database connection. 
     * @return boolean - used to confirm if an open connection was closed or not.
     */
    public boolean CloseConnection() {
        // Presume false here as we will set to true in the following lines:
        boolean successful = false;
        
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                // Set successful to false and log the errors:
                successful = false;
                Logger.getLogger(DB_Handler.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("ERROR: " + ex.getLocalizedMessage());
            }
        }
        // Note the return, we need to test to see if this was closed correctly:
        return successful;
    }
    
    //************************************************************************//
    //  -   PROCESSING STATEMENTS + RESULTSETS                            -   //
    //************************************************************************//
    
    private User constructUser(ResultSet set) throws SQLException {
        // User details, ordered by constructor usage:
        int     id          = set.getInt("ID");
        String  username    = set.getString("username");
        String  contact     = set.getString("contact");
        // PASSWORD: Not to be fetched from database.
        String  name        = set.getString("name");
        
        // Construct lookup for Staff Type:
        int type_id         = set.getInt("type_id");
        String type_value   = set.getString("type_value");
        GenericLookup staffType = new GenericLookup(type_id, type_value);
        
        // Note that we are not specifying the password, and we are setting authenticated to false.
        User user           = new User(id, username, contact, "", name, false, staffType);
        
        return user;
    }
    
    private Product constructProduct(ResultSet set) throws SQLException {      
        // Product details, ordered by constructor usage:
        int     id          = set.getInt("ID");
        int     sc          = set.getInt("stockCount");
        String  name        = set.getString("name");
        boolean available   = set.getBoolean("available");
        Double  unitPrice   = set.getDouble("unitPrice");
        
        int     cat_id      = set.getInt("category_id");
        String  category    = set.getString("category");
        GenericLookup cat   = new GenericLookup(cat_id, category);
        
        int     sub_id      = set.getInt("subCategory_id");
        String subCategory  = set.getString("subCategory");
        GenericLookup sub   = new GenericLookup(sub_id, subCategory);
        
        int     con_id      = set.getInt("container_id");
        String container    = set.getString("container");
        GenericLookup con   = new GenericLookup(con_id, container);
        
        Product product     = new Product(
                id, sc, name, available,unitPrice, cat, sub, con);
        
        return product;
    }
    
    private Order constructOrder(ResultSet set) throws SQLException {
        // Order details, ordered by constructr usage:
        int     id              = set.getInt("ID");
        int     quantity        = set.getInt("quantity");
        boolean fulfilled       = set.getBoolean("");
        String  dateOrdered     = set.getString("dateOrdered");
        
        // Staff Ordered    [use existing method]:
        int     staffO          = set.getInt("staffOrdered");
        User    staffOrdered    = getUser(staffO);
        
        // Product          [use existing method]:
        int     productID       = set.getInt("productID");
        Product product         = getProduct(productID);
        
        // Location
        int     loc_id          = set.getInt("loc_id");
        String  loc_value       = set.getString("loc_value");
        GenericLookup location  = new GenericLookup(loc_id, loc_value);
        
        // Status:
        int     status_id       = set.getInt("status_id");
        String  status_value    = set.getString("status_value");
        GenericLookup status    = new GenericLookup(status_id, status_value);
       
        // Construct object:
        Order order = new Order(id, quantity, fulfilled, dateOrdered, staffOrdered, product, location, status);
        
        // Get non-constructor data:
        String  dateDelivered   = set.getString("dateDelivered");
        Time    timeDelivered   = set.getTime("timeDelivered");
        
        int     staffF          = set.getInt("staffFulfilled");
        User    staffFulfilled  = getUser(staffF);

        order.setDateDelivered(dateDelivered);
        order.setTimeDelivered(timeDelivered);
        order.setStaffFulfilled(staffFulfilled);
        
        return order; 
    }
    
    private GenericLookup constructLookup(ResultSet set) throws SQLException {
        int id          = set.getInt("ID");
        String value    = set.getString("VALUE");
        
        return new GenericLookup(id, value);
    }
    
    // OLD METHOD - MAY NOT BE NEEDED BUT DO NOT DELETE JUST IN CASE
    private int FindIDForTable(String table, String value) {
        try {
            String valueCol;
            
            switch(table){
                case p_cat:
                    valueCol = "categoryVal";
                    break;
                    
                case p_con:
                    valueCol = "containerVal";
                    break;
                    
                case p_sub:
                    valueCol = "subcatVal";
                    break;
                    
                case p_loc:
                    valueCol = "locationVal";
                    break;
                    
                case o_sta:
                    valueCol = "statusName";
                    break;
                    
                default:
                    return 0;
            }
            int id_val;
            // Set up the query to get the ID we want:
            String query = "SELECT ID FROM " + table + " WHERE "
                    + valueCol + " = " + value + ";";
            
            findID = connection.prepareStatement(query);
            
            resultSet = findID.executeQuery();
            resultSet.next();
            
            id_val = resultSet.getInt(1);
            
            if (id_val > 0) {
                return id_val;
            } else { return 0; }
        } catch (SQLException ex) {
            Logger.getLogger(DB_Handler.class.getName()).log(Level.SEVERE, null, ex);
            
            // 0 return must be handled outside of this method really:
            return 0;
        }
    }
    
    public ArrayList<Product> getAllProducts() throws SQLException {
        allProducts     = connection.prepareStatement(getAllProductsQ);
        resultSet       = allProducts.executeQuery();
        
        // Prepare list to store data in:
        ArrayList<Product> list = new ArrayList<>();
        
        // Get the size of the resultSet:
        resultSet.last();
        int count = resultSet.getRow();
        resultSet.beforeFirst();
        
        // Construct list using helper method:
        for(int a=0; a<count; a++) {
            resultSet.next();
            list.add(constructProduct(resultSet));
        }
        
        // Return:
        return list;
    }
    
    public ArrayList<Order> getAllOrders() throws SQLException {        
        allOrders = connection.prepareStatement(getAllOrdersQ);
        resultSet = allOrders.executeQuery();
        
        // Prepare list to store data in:
        ArrayList<Order> list = new ArrayList<>();
        
        // Get the size of the resultSet:
        resultSet.last();
        int count = resultSet.getRow();
        resultSet.beforeFirst();
        
        // Construct list using helper method:
        for(int a=0; a<count; a++) {
            resultSet.next();
            list.add(constructOrder(resultSet));
        }
        
        // Return:
        return list;
    }
    
    public ArrayList<User> getAllUsers() throws SQLException {
        allUsers        = connection.prepareStatement(getAllUsersQ);
        resultSet       = allUsers.executeQuery();
        
        // Prepare list to store data in:
        ArrayList<User> list = new ArrayList<>();
        
        // Get the size of the resultSet:
        resultSet.last();
        int count = resultSet.getRow();
        resultSet.beforeFirst();
        
        // Construct list using helper method:
        for(int a=0; a<count; a++) {
            resultSet.next();
            list.add(constructUser(resultSet));
        }
        
        // Return:
        return list;
    }
    
    public ArrayList<GenericLookup> getAllLookups(String identifier) throws SQLException {
        String tablename;
        String valuename;
        
        switch(identifier) {
            case "category":
                tablename = this.p_cat;
                valuename = "";
                break;
                
            case "subcategory":
                tablename = this.p_sub;
                valuename = "";
                break;
                
            case "location":
                tablename = this.p_loc;
                valuename = "";
                break;
                
            case "container":
                tablename = this.p_con;
                valuename = "";
                break;
                
            case "status":
                tablename = this.o_sta;
                valuename = "";
                break;
                
            default:
                return null;
        }
        getLookups = connection.prepareStatement(getAllLookupsQ);
        getLookups.setString(1, valuename);
        getLookups.setString(2, tablename);

        resultSet = getLookups.executeQuery();
        
        // Get the size of the resultSet:
        resultSet.last();
        int count = resultSet.getRow();
        resultSet.beforeFirst();
        
        // Create a lookup object here.
        ArrayList<GenericLookup> returnArray = new ArrayList<>();
        
        for (int a = 0; a < count; a++) {
            returnArray.add(constructLookup(resultSet));
            resultSet.next();
        }
        return returnArray;
    }
    
    public void newLookup(String identifier, GenericLookup lookup) throws SQLException, Exception {
        addLookup = connection.prepareStatement(addLookupQ);
        
        String table, value;
        
        switch (identifier) {
            case "category":
                table = this.p_cat;
                value = "categoryVal";
                break;
                
            case "subcategory":
                table = this.p_sub;
                value = "subcatVal";
                break;
                
            case "location":
                table = this.p_loc;
                value = "locationVal";
                break;
                
            case "container":
                table = this.p_con;
                value = "containerVal";
                break;
                
            case "status":
                table = this.o_sta;
                value = "statusName";
                break;
                
            default:
                table = "";
                value = "";
                throw new Exception();
        }
        if (table.length() > 1) {
            addLookup.setString(1, table);              // TABLENAME
            addLookup.setString(2, value);              // VALUE COLUMN
            addLookup.setString(3, lookup.getValue());  // VALUE TO ADD
            
            addLookup.executeQuery();
        }
    }
    
    public void updateLookup(String identifier, GenericLookup lookup) throws SQLException, Exception {
        updateLookup = connection.prepareStatement(addLookupQ);
        
        String table, value;
        
        switch (identifier) {
            case "category":
                table = this.p_cat;
                value = "categoryVal";
                break;
                
            case "subcategory":
                table = this.p_sub;
                value = "subcatVal";
                break;
                
            case "location":
                table = this.p_loc;
                value = "locationVal";
                break;
                
            case "container":
                table = this.p_con;
                value = "containerVal";
                break;
                
            case "status":
                table = this.o_sta;
                value = "statusName";
                break;
                
            default:
                table = "";
                value = "";
                throw new Exception();
        }
        if (table.length() > 1) {
            updateLookup.setString(1, table);               // TABLENAMEp
            updateLookup.setString(2, value);               // VALUE COLUMN
            updateLookup.setString(3, lookup.getValue());   // VALUE TO SET
            updateLookup.setInt(4, lookup.getID());         // ID TO CHANGE.
            
            updateLookup.executeQuery();
        }
    }
    
    public Product getProduct(int id) throws SQLException {
        getProduct = connection.prepareStatement(getProductQ);
        getProduct.setInt(1, id); // Set up the ID part of the query.
        
        resultSet = getProduct.executeQuery();
        resultSet.next();
        
        return constructProduct(resultSet);
    }
    
    public Order getOrder(int id) throws SQLException {
        getOrder = connection.prepareStatement(getOrderQ);
        getOrder.setInt(1, id); // Set up the ID part of the query.
        
        resultSet = getOrder.executeQuery();
        resultSet.next();
        
        return constructOrder(resultSet);
    }
    
    public User getUser(int id) throws SQLException {
        getUser = connection.prepareStatement(getUserQ);
        getUser.setInt(1, id); // Set up the ID part of the query.
        
        resultSet = getUser.executeQuery();
        resultSet.next();
        
        return constructUser(resultSet);
    }
    
    public void updateProduct(Product productUpdate) throws SQLException {
        updateProduct = connection.prepareStatement(updateProductQ);

        // Populate SET part:
        updateProduct.setInt(1, productUpdate.getStockCount());
        updateProduct.setString(2, productUpdate.getName());
        updateProduct.setBoolean(3, productUpdate.isAvailable());
        updateProduct.setDouble(4, productUpdate.getUnitPrice());
        updateProduct.setInt(5, productUpdate.getCategory().getID());
        updateProduct.setInt(6, productUpdate.getSubCategory().getID());
        updateProduct.setInt(7, productUpdate.getContainer().getID());

        // Set the WHERE part:
        updateProduct.setInt(8, productUpdate.getID());
        
        updateProduct.executeUpdate();
    }
    
    public void updateOrder(Order orderUpdate) throws SQLException {
        updateOrder = connection.prepareStatement(updateOrderQ);
        
        // Populate SET part:
        updateOrder.setInt(1, orderUpdate.getQuantity());
        updateOrder.setString(2, orderUpdate.getDateOrdered());
        updateOrder.setInt(3, orderUpdate.getStaffOrdered().getID());
        updateOrder.setInt(4, orderUpdate.getProduct().getID());
        updateOrder.setInt(5, orderUpdate.getLocation().getID());
        updateOrder.setInt(6, orderUpdate.getStatus().getID());
        updateOrder.setString(8, orderUpdate.getDateDelivered());
        
        updateOrder.executeQuery();
    }
    
    public void updateUser(User userUpdate) throws SQLException {
        updateUser = connection.prepareStatement(updateUserQ);
        
        updateUser.setString(1, userUpdate.getUsername());
        updateUser.setString(2, userUpdate.getContact());
        updateUser.setString(3, userUpdate.getName());
        updateUser.setInt(4, userUpdate.getStaffType().getID());
        
        updateUser.executeUpdate();
    }
    
    public void newProduct(Product productNew) throws SQLException {
        addProduct = connection.prepareStatement(addProductQ);

        addProduct.setInt(1, productNew.getStockCount());
        addProduct.setString(2, productNew.getName());
        addProduct.setBoolean(3, productNew.isAvailable());
        addProduct.setDouble(4, productNew.getUnitPrice());
        addProduct.setInt(5, productNew.getCategory().getID());
        addProduct.setInt(6, productNew.getSubCategory().getID());
        addProduct.setInt(7, productNew.getContainer().getID());

        addProduct.executeUpdate();
    }
    
    public void newOrder(Order orderNew) throws SQLException {
        addOrder = connection.prepareStatement(addOrderQ);
        
        addOrder.setInt(1, orderNew.getQuantity());
        addOrder.setString(2, orderNew.getDateOrdered());
        addOrder.setInt(3, orderNew.getStaffOrdered().getID());
        addOrder.setInt(4, orderNew.getProduct().getID());
        addOrder.setInt(5, orderNew.getLocation().getID());
        addOrder.setInt(6, orderNew.getStatus().getID());
        
        addOrder.executeUpdate();
    }
    
    public void newUser(User newUser, String password) throws SQLException {
        addUser = connection.prepareStatement(addUserQ);
        
        addUser.setString(1, newUser.getUsername());
        addUser.setString(2, newUser.getContact());
        addUser.setString(3, newUser.getName());
        addUser.setInt(4, newUser.getStaffType().getID());
        addUser.setString(5, password);
        
        addUser.executeQuery();
    }
    
    public boolean authUser(User u, String password) {
        // TODO: this.
        // authUser = preparedStatement
        // authUserQ = Query.
        return false;
    }
}
