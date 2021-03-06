package NET302_Handlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import NET302JavaLibrary.GenericLookup;
import NET302JavaLibrary.Order;
import NET302JavaLibrary.Product;
import NET302JavaLibrary.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Testing throughout.
// TODO: Ensure things are up to standards (security, etc.)

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
    private final String    db_url      = "net302cw.cye4yuizcdgc.us-west-2.rds.amazonaws.com:3306/";
    private final String    db_name     = "NET302";
    private final String    user_id     = "root";
    private final String    password    = "net302rootuser";
    private final String    driver      = "com.mysql.jdbc.Driver";
    
    // Alternative Details for early testing:
    /*
    private final String    db_url      = "net302.cli7vsmzzdht.us-west-2.rds.amazonaws.com:3306/";
    private final String    db_name     = "MyDatabase";
    private final String    user_id     = "root";
    */
    
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
    private PreparedStatement   getProduct          = null;
    private PreparedStatement   getOrder            = null;
    private PreparedStatement   getUser             = null;
    private PreparedStatement   updateOrder         = null;
    private PreparedStatement   updateProduct       = null;
    private PreparedStatement   updateOrderProduct  = null;
    private PreparedStatement   updateUser          = null;
    private PreparedStatement   addProduct          = null;
    private PreparedStatement   addOrder            = null;
    private PreparedStatement   addOrder2           = null;
    private PreparedStatement   addUser             = null;
    private PreparedStatement   allProducts         = null;
    private PreparedStatement   allOrders           = null;
    private PreparedStatement   allUsers            = null;
    private PreparedStatement   findID              = null;
    private PreparedStatement   getLookups          = null;
    private PreparedStatement   addLookup           = null;
    private PreparedStatement   updateLookup        = null; 
    private PreparedStatement   authUser            = null;
    private PreparedStatement   checkUsername       = null;
    private PreparedStatement   searchProduct       = null;
    private PreparedStatement   updateQuantity      = null;
    
    // SQL QUERY STRINGS:
    // ';' is included in the strings, 
    // but may be included in the prepared statement anyway.
    
    private final String        getProductQ     =
            "SELECT (Products.ID) as ID, "
            + "stockCount, "
            + "prodName as name, "
            + "available, "
            + "unitPrice, "
            + "(" + p_cat + ".categoryVal) as category, "
            + "(" + p_cat + ".ID) as category_id, "
            + "(" + p_sub + ".subcatVal) as subCategory, "
            + "(" + p_sub + ".ID) as subCategory_id, "
            + "(" + p_con + ".containerVal) as container, "
            + "(" + p_con + ".ID) as container_id "
            
            // FROM / JOIN / WHERE:
            + "FROM NET302.Products "
            + "JOIN " + p_cat + " ON Products.categoryID = " + p_cat + ".ID "
            + "JOIN " + p_sub + " ON Products.subcatID = " + p_sub + ".ID "
            + "JOIN " + p_con + " ON Products.containerID = " + p_con + ".ID "
            + "WHERE NET302.Products.ID = ?;"; // ID last.
    
    private final String        getOrderQ       =
            "SELECT "
            + "Orders.ID, "
            + "Orders.quantity, "
            + "Orders.dateOrdered, "
            + "Staff.staffName as staffOrdered, "
            + "Staff.username as staffUsername, " 
            + "Staff.ID as staffID, "
            + "Products.ID as productID, "
            + "Products.stockCount as productStockCount, "
            + "Products.available as productAvailable, "
            + "Products.prodName as productName, "
            + "Products.unitPrice as productUnitPrice, "
            //+ "Products.categoryID as productCategoryID, " // duplicate
            + "(" + p_cat + ".ID) as productCategoryID, "
            + "(" + p_sub + ".ID) as productSubCategoryID, "
            + "(" + p_con + ").ID as productContainerID, "
            + "(" + p_cat + ".categoryVal) as productCategory, "
            + "(" + p_sub + ".subcatVal) as productSubcategory, "
            + "(" + p_con + ".containerVal) as productContainer, "
            + "(" + p_loc + ".ID) as loc_id, "
            + "(" + p_loc + ".locationVal) as loc_value, "
            + "(" + o_sta + ".ID) as status_id, "
            + "(" + o_sta + ".statusName) as status_value, "
            + "Orders.dateDelivered as dateDelivered "
            
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
            + "Staff.staffName as name,"
            
            // Get the Staff_Type details:
            + "Staff.staffType as type_id, " 
            + "(" + s_typ + ".staffType) as type_value "
            
            // FROM + WHERE:
            + "FROM NET302.Staff "
            + "JOIN Staff_Type ON Staff.staffType = Staff_Type.ID "
            + "WHERE Staff.ID = ?;"; // ID last.       
    
    private final String        getUserQ_2      =
            "SELECT Staff.ID as ID, "
            + "username, "
            + "staffContact as contact, "
            + "staffName as name, "
            
            // Get the Staff_Type details:
            + "Staff.staffType as type_id, "
            + "(" + s_typ + ".staffType) as type_value "
            
            // FROM + WHERE:
            + "FROM NET302.Staff "
            + "JOIN " + s_typ + " ON Staff.staffType = " + s_typ + ".ID "
            + "WHERE username = ?;";            // Username last.
    
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
    
    private final String        fulfillOrderQ   =
            "UPDATE NET302.Orders SET "
            + " statusID = ?, "
            + " dateDelivered = ? "
            + " WHERE NET302.Orders.ID = ?;";   // ID last.
    
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
            "UPDATE NET302.Staff SET ("
            + "quantity, dateOrdered, staffID, productID, locationID, statusID)"
            + " VALUES (?, GETDATE(), ?, ?, ?, ?);";
    
    private final String        addOrderQ2       =
            "INSERT INTO NET302.Orders  ("
            + "quantity, dateOrdered, staffID, productID, locationID, statusID)"
            + " VALUES (?, ?, ?, ?, ?, ?);";
    
    private final String    updateOrderProductQ2       =
            "UPDATE NET302.Products SET  stockCount = stockCount - ? " 
            + "WHERE NET302.Products.ID = ?;";
    
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
            + "(" + p_cat + ".categoryVal) as category, "
            + "(" + p_cat + ".ID) as category_id, "
            + "(" + p_sub + ".subcatVal) as subCategory, "
            + "(" + p_sub + ".ID) as subCategory_id, "
            + "(" + p_con + ".containerVal) as container, "
            + "(" + p_con + ".ID) as container_id "
            
            // FROM / JOIN / WHERE:
            + "FROM NET302.Products "
            + "JOIN " + p_cat + " ON Products.categoryID = " + p_cat + ".ID "
            + "JOIN " + p_sub + " ON Products.subcatID = " + p_sub + ".ID "
            + "JOIN " + p_con + " ON Products.containerID = " + p_con + ".ID "
            + "ORDER BY ID;";
    
    private final String        getAllOrdersQ   =
            "SELECT "
            + "Orders.ID, "
            + "Orders.quantity, "
            + "Orders.dateOrdered, "
            + "Staff.staffName as staffOrdered, "
            + "Staff.username as staffUsername, " 
            + "Staff.ID as staffID, "
            + "Products.ID as productID, "
            + "Products.stockCount as productStockCount, "
            + "Products.available as productAvailable, "
            + "Products.prodName as productName, "
            + "Products.unitPrice as productUnitPrice, "
            //+ "Products.categoryID as productCategoryID, " // duplicate
            + "(" + p_cat + ".ID) as productCategoryID, "
            + "(" + p_sub + ".ID) as productSubCategoryID, "
            + "(" + p_con + ").ID as productContainerID, "
            + "(" + p_cat + ".categoryVal) as productCategory, "
            + "(" + p_sub + ".subcatVal) as productSubcategory, "
            + "(" + p_con + ".containerVal) as productContainer, "
            + "(" + p_loc + ".ID) as loc_id, "
            + "(" + p_loc + ".locationVal) as loc_value, "
            + "(" + o_sta + ".ID) as status_id, "
            + "(" + o_sta + ".statusName) as status_value, "
            + "Orders.dateDelivered as dateDelivered "
            
            // FROM + JOINS + WHERE:
            + "FROM NET302.Orders "
            + "JOIN " + o_sta + " ON Orders.statusID = " + o_sta + ".ID "
            + "JOIN NET302.Staff ON Orders.staffID = Staff.staffID "
            + "JOIN NET302.Products ON Orders.productID = Products.ID "
            + "ORDER BY ID;";
    
    private final String        getUnfulfilledQ = 
            "SELECT "
            + "Orders.ID, "
            + "Orders.quantity, "
            + "Orders.dateOrdered, "
            + "Staff.staffName as staffOrdered, "
            + "Staff.username as staffUsername, " 
            + "Staff.ID as staffID, "
            + "Products.ID as productID, "
            + "Products.stockCount as productStockCount, "
            + "Products.available as productAvailable, "
            + "Products.prodName as productName, "
            + "Products.unitPrice as productUnitPrice, "
            //+ "Products.categoryID as productCategoryID, " // duplicate
            + "(" + p_cat + ".ID) as productCategoryID, "
            + "(" + p_sub + ".ID) as productSubCategoryID, "
            + "(" + p_con + ".ID) as productContainerID, "
            + "(" + p_cat + ".categoryVal) as productCategory, "
            + "(" + p_sub + ".subcatVal) as productSubcategory, "
            + "(" + p_con + ".containerVal) as productContainer, "
            + "(" + p_loc + ".ID) as loc_id, "
            + "(" + p_loc + ".locationVal) as loc_value, "
            + "(" + o_sta + ".ID) as status_id, "
            + "(" + o_sta + ".statusName) as status_value, "
            + "Orders.dateDelivered as dateDelivered "
            + "FROM NET302.Orders "
            + "JOIN NET302.Staff ON Staff.ID = Orders.staffID "
            + "JOIN " + o_sta + " ON " + o_sta + ".ID = Orders.statusID "
            + "JOIN NET302.Products ON Products.ID = Orders.productID "
            + "JOIN NET302.Location ON Location.ID = Orders.locationID "
            + "JOIN NET302.Product_Category ON Products.categoryID = Product_Category.ID "
            + "JOIN " + p_sub + " ON Products.subcatID = " + p_sub + ".ID "
            + "JOIN " + p_con + " ON Products.containerID = " + p_con + ".ID "
            + "WHERE Orders.statusID = 1 "
            + "ORDER BY Orders.ID;";
            
            // old
//            "SELECT (Orders.ID) as ID, "
//            + "quantity, "
//            + "TO_CHAR(dateOrdered, 'DD/MM/YYYY') as dateOrdered, "
//            + "(staff.ID) as staffOrdered, "
//            + "(Products.ID) as productID, "
//            + "(" + p_loc + ".ID) as loc_id, "
//            + "(" + p_loc + ".locationVal) as loc_value, "
//            + "(" + o_sta + ".ID) as status_id, "
//            + "(" + o_sta + ".statusName) as status_value, "
//            + "TO_CHAR(dateDelivered, 'DD/MM/YYYY') as dateDelivered "
//            //+ "timeDelivered, " // MISSING FROM TABLE.
//            // TODO: Time may need a mask, but wait to see how it returns first.
//            
//            // FROM + JOINS + WHERE:
//            + "FROM NET302.Orders "
//            + "JOIN " + o_sta + " ON Orders.statusID = " + o_sta + ".ID "
//            + "JOIN NET302.Staff ON Orders.staffID = Staff.staffID "
//            + "JOIN NET302.Products ON Orders.productID = Products.ID "
//            + "WHERE status_value = " // TODO: Enter statusvalue!!! 
//            + "ORDER BY ID;";
    
    private final String        getAllUsersQ    =
            "SELECT Staff.ID as ID, "
            + "username, "
            + "staffContact as contact, "
            + "staffName as name, "
            
            // Get the Staff_Type details:
            + "Staff.staffType as type_id, "
            + "staffType as type_value "
            
            // FROM + WHERE:
            + "FROM NET302.Staff "
            + "JOIN " + s_typ + " ON Staff.staffType = " + s_typ + ".ID "
            + "ORDER BY ID;";
    
    private final String        getAllLookupsQ  =
            "SELECT ID as ID,"
            + "? as VALUE "
            + "FROM ?"
            + "ORDER BY ID;";
    
    private final String        addLookupQ      =
            "INSERT INTO ? (?) VALUES (?);";
    
    private final String        updateLookupQ   =
            "UPDATE ? SET ? = ? WHERE ID = ?;";
    
    private final String        authUserQ       =
            "SELECT Staff.ID, Staff.staffName, Staff.staffContact, Staff.username, Staff_Type.staffType, Staff_Type.ID FROM NET302.Staff "
            + "LEFT OUTER JOIN Staff_Type ON Staff.staffType = Staff_Type.ID WHERE Staff.username = ? AND Staff.userPassword = ?;";
    
    /* TODO: Possible alternative to the above to support code re-use.
    private final String        authUserQ       =
            "SELECT Staff.ID as ID, "
            + "Staff.staffName as name, "
            + "Staff.staffContact as contact, "
            + "Staff.username as username, "
            + "Staff_Type.staffType as type_value, "
            + "Staff_Type.ID as type_id "
            + "FROM NET302.Staff LEFT OUTER JOIN Staff_Type ON Staff.staffType = Staff_Type.ID WHERE Staff.username = ? AND Staff.userPassword = ?;";  
    */
    
    private final String        checkUsernameQ  =
            "SELECT ID, username FROM NET302.Staff WHERE Staff.Username = ?;";
            
    private final String        updateQuantityQ = 
            "UPDATE NET302.Products SET stockCount = ? WHERE ID = ?";
    
    private final String        searchProductQ  =
            "SELECT (Products.ID) as ID, "
            + "stockCount, "
            + "prodName as name, "
            + "available, "
            + "unitPrice, "
            + "(" + p_cat + ".categoryVal) as category, "
            + "(" + p_cat + ".ID) as category_id, "
            + "(" + p_sub + ".subcatVal) as subCategory, "
            + "(" + p_sub + ".ID) as subCategory_id, "
            + "(" + p_con + ".containerVal) as container, "
            + "(" + p_con + ".ID) as container_id "
            
            // FROM / JOIN / WHERE:
            + "FROM NET302.Products "
            + "JOIN " + p_cat + " ON Products.categoryID = " + p_cat + ".ID "
            + "JOIN " + p_sub + " ON Products.subcatID = " + p_sub + ".ID "
            + "JOIN " + p_con + " ON Products.containerID = " + p_con + ".ID "
            + "WHERE lower(prodName) LIKE ? "
            + "ORDER BY ID;";
    
    /**
     * Empty Constructor.
     * Used so that the middleware pages can reference the class and use
     * the methods below.
     */
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
    
    /*
    // USED FOR DEBUGGING:
    public String getAllProductsQ() {
        return getAllProductsQ;
    }
    */
    
    // USED FOR DEBUGGING:
    /*
    private int GetResultSetCount(ResultSet set) {
        int size = 0;
        try {
            set.last();
            size = set.getRow();
            set.beforeFirst();
        } catch (Exception ex) {

        }
        return size;
    }
    */
    
    //************************************************************************//
    //  -   PROCESSING STATEMENTS + RESULTSETS                            -   //
    //************************************************************************//
    
    /**
     * This method constructs a User from the current position of a ResultSet.
     * @param set ResultSet - being the set which we are working with.
     * @return User - being the User extracted from the current position of the
     * ResultSet.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery.
     */
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
        User user           = new User(id, username, "", contact, name, false, staffType);
        user.setAuthenticated(true);
        return user;
    }
    
    /**
     * This method constructs a Product from the current position of a ResultSet.
     * @param set ResultSet - being the set which we are working with.
     * @return Product - being the Product extracted from the current position 
     * of the ResultSet.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery.
     */
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
    
    /**
     * This method constructs an Order from the current position of the ResultSet.
     * @param set ResultSet - being the set we are working with.
     * @return Order - being the Order extracted from the current position of 
     * the ResultSet.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery.
     */
    private Order constructOrder(ResultSet set) throws SQLException {
        int id = set.getInt("ID");
        int quantity = set.getInt("quantity");
        String dateOrdered = set.getString("dateOrdered");
        
        String staff = set.getString("staffOrdered");
        String staffUsername = set.getString("staffUsername");
        int staffID = set.getInt("staffID");
        
        int productID = set.getInt("productID");
        int stockCount = set.getInt("productStockCount");
        boolean available = set.getBoolean("productAvailable");
        String productName = set.getString("productName");
        double unitPrice = set.getDouble("productUnitPrice");
        
        int categoryID = set.getInt("productCategoryID");
        String category = set.getString("productCategory");
        
        int subCategoryID = set.getInt("productSubCategoryID");
        String subCategory = set.getString("productSubcategory");
        
        int containerID = set.getInt("productContainerID");
        String container = set.getString("productContainer");
        
        int locationID = set.getInt("loc_id");
        String location = set.getString("loc_value");
        
        int statusID = set.getInt("status_id");
        String status = set.getString("status_value");
        
        return new Order(
                id, 
                quantity, 
                !(statusID == 1),  
                dateOrdered, 
                new User(
                        staffID, 
                        staffUsername, 
                        staff), 
                new Product(
                        productID,
                        stockCount,
                        productName,
                        available,
                        unitPrice,
                        new GenericLookup(categoryID, category),
                        new GenericLookup(subCategoryID, subCategory),
                        new GenericLookup(containerID, container)
                ),
                new GenericLookup(locationID, location),
                new GenericLookup(statusID, status)
        );
        
        // OLD QUERY FOR CONSTRUCTING THE ORDER:
        /*
        int     id              = set.getInt("ID");
        int quantity = set.getInt("quantity");

        String dateOrdered = set.getString("dateOrdered");

        // Staff Ordered    [use existing method]:
        int staffO = set.getInt("staffOrdered");
        //User    staffOrdered    = getUser(staffO);

        // Product          [use existing method]:
        int productID = set.getInt("productID");
        //Product product         = getProduct(productID);

        // Location
        int loc_id = set.getInt("loc_id");
        String loc_value = set.getString("loc_value");
        GenericLookup location = new GenericLookup(loc_id, loc_value);

        // Status:
        int status_id = set.getInt("status_id");
        String status_value = set.getString("status_value");
        GenericLookup status = new GenericLookup(status_id, status_value);
        //boolean fulfilled       = set.getBoolean("");

        // Get non-constructor data:
        // This is only called from unfulfilled orders and so this probably won't even be used. Causing errors. 
        String dateDelivered = "";// new SimpleDateFormat("dd-mm-yyyy").format(set.getDate("dateDelivered"));
        //Time    timeDelivered   = set.getTime("timeDelivered");

        //int     staffF          = set.getInt("staffFulfilled");
        //User    staffFulfilled  = getUser(staffF);
        // Construct object:
        Order order = new Order(id, quantity, !(status_id == 1), dateOrdered, staffOrdered, null, location, status);

        order.setDateDelivered(dateDelivered);
        order.setTimeDelivered(timeDelivered);
        order.setStaffFulfilled(staffFulfilled);

        return order;
        */
    }
    
    /**
     * This method constructs a GenericLookup from the current position of the ResultSet.
     * @param set ResultSet - being the set we are working with.
     * @return GenericLookup - being the GenericLookup we have extracted from the
     * current position of the ResultSet.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery.
     */
    private GenericLookup constructLookup(ResultSet set) throws SQLException {
        int id          = set.getInt("ID");
        String value    = set.getString("VALUE");
        
        return new GenericLookup(id, value);
    }
    
    /**
     * DEPRECIATED METHOD.
     * This method was written before the object library included GenericLookup
     * and therefore the SQL queries had to fetch the ID for a lookup value,
     * so this method took the table and value to search in/for.
     * @param table String - being the identifier of the table.
     * @param value String - being the value we are looking for.
     * @return int - being the ID for the given value in the given table.
     * A 0 return MUST be handled outside of this method to pass back the error!
     */
    private int FindIDForTable(String table, String value) {
        try {
            // Column name to query:
            String valueCol; 
            
            // Switch through the tables and set our column.
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
            // Set up the query to get the ID we want:
            int id_val;
            String query = "SELECT ID FROM " + table + " WHERE "
                    + valueCol + " = " + value + ";";
            
            // Prepare Statement:
            findID = connection.prepareStatement(query);
            
            resultSet = findID.executeQuery();
            resultSet.next();
            
            id_val = resultSet.getInt(1);
            
            // Some error handling:
            if (id_val > 0) {
                return id_val;
            } else { return 0; }
        } catch (SQLException ex) {
            Logger.getLogger(DB_Handler.class.getName()).log(Level.SEVERE, null, ex);
            
            // 0 return must be handled outside of this method!
            return 0;
        }
    }
    
    /**
     * This method queries for ALL Products and creates them from the ResultSet.
     * These are stored in an ArrayList and returned.
     * @return ArrayList<Product> - being the list of Products.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
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
    
    public ArrayList<Product> getProducts(String searchTerm) throws SQLException {
        searchProduct = connection.prepareStatement(searchProductQ);
        searchProduct.setString(1, "%" + searchTerm + "%");
        resultSet = searchProduct.executeQuery();
        
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
    
    /**
     * This method queries for ALL Orders and creates them from the ResultSet.
     * These are stored in an ArrayList and returned.
     * @return ArrayList<Order> - being the list of Orders.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
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
    
    /**
     * This method queries for Orders where the status is unfulfilled,
     * and it creates the objects from the ResultSet. 
     * These are stored in an ArrayList and returned.
     * @return ArrayList<Order> - being the list of unfulfilled Orders.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
    public ArrayList<Order> getUnfulfilled() throws SQLException {
        allOrders = connection.prepareStatement(getUnfulfilledQ);
        resultSet = allOrders.executeQuery();
        
        // Prepare list to store data in:
        ArrayList<Order> list = new ArrayList<>();
        
        while (resultSet.next())
        {
            //list.add(null);
            list.add(constructOrder(resultSet));
        }
        
        // Return:
        return list;
    }
    
    /**
     * 
     * @return
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
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
    
    /**
     * 
     * @param identifier
     * @return
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
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
    
    /**
     * 
     * @param identifier
     * @param lookup
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     * @throws Exception - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the identifier passed.
     */
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
    
    /**
     * 
     * @param identifier
     * @param lookup
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     * @throws Exception - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the identifier.
     */
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
            updateLookup.setString(1, table);               // TABLENAME
            updateLookup.setString(2, value);               // VALUE COLUMN
            updateLookup.setString(3, lookup.getValue());   // VALUE TO SET
            updateLookup.setInt(4, lookup.getID());         // ID TO CHANGE.
            
            updateLookup.executeQuery();
        }
    }
    
    /**
     * This method queries for a given Product (using ID).
     * @param id int - being the ID to query.
     * @return Product - being the constructed result of the query.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
    public Product getProduct(int id) throws SQLException {
        getProduct = connection.prepareStatement(getProductQ);
        getProduct.setInt(1, id); // Set up the ID part of the query.
        
        resultSet = getProduct.executeQuery();
        resultSet.next();
        
        return constructProduct(resultSet);
    }
    
    /**
     * This method queries for a given Order (using ID).
     * @param id int - being the ID to query.
     * @return Order - being the constructed result of the query.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
    public Order getOrder(int id) throws SQLException {
        getOrder = connection.prepareStatement(getOrderQ);
        getOrder.setInt(1, id); // Set up the ID part of the query.
        
        resultSet = getOrder.executeQuery();
        resultSet.next();
        
        return constructOrder(resultSet);
    }
    
    /**
     * This method queries for a given User (using ID).
     * @param id int - being the ID to query.
     * @return User - being the constructed result of the query.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
    public User getUser(int id) throws SQLException {
        getUser = connection.prepareStatement(getUserQ);
        getUser.setInt(1, id); // Set up the ID part of the query.
        
        resultSet = getUser.executeQuery();
        resultSet.next();
        
        return constructUser(resultSet);
    }
    
    /**
     * This method queries for a given User (using Username).
     * @param username String - being the Username to query.
     * @return User - being the constructed result of the query.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
    public User getUser(String username) throws SQLException {
        getUser = connection.prepareStatement(getUserQ_2);
        getUser.setString(1, username);
        
        resultSet = getUser.executeQuery();
        resultSet.next();
        
        return constructUser(resultSet);
    }
    
    /**
     * This method performs an update query on the database using a given Product.
     * @param productUpdate Product - being the details to update.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
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
    
    /**
     * This method updates the quantity of a specific Product.
     * @param productID int - being the ID of the Product.
     * @param newQuantity int - being the new quantity to set.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.  
     */
    public void updateQuantity(int productID, int newQuantity) throws SQLException {
        updateQuantity = connection.prepareStatement(updateQuantityQ);
        updateQuantity.setInt(1, newQuantity);
        updateQuantity.setInt(2, productID);
        updateQuantity.executeUpdate();
    }
    
    /**
     * This method performs an update on the database using a given Order.
     * @param orderUpdate Order - being the details to update.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
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
    
    /**
     * This simply fulfils a given order
     * @param orderToFulfill
     * @throws SQLException 
     */
    public void fulfillOrder(int orderToFulfill) throws SQLException {
        updateOrder = connection.prepareStatement(fulfillOrderQ);
        
        updateOrder.setInt(1, 2); 
        updateOrder.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        updateOrder.setInt(3, orderToFulfill);
        
        updateOrder.executeUpdate();
    }
    
    /**
     * This method performs an update on the database using a given User.
     * @param userUpdate User - being the details to update.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
    public void updateUser(User userUpdate) throws SQLException {
        updateUser = connection.prepareStatement(updateUserQ);
        
        updateUser.setString(1, userUpdate.getUsername());
        updateUser.setString(2, userUpdate.getContact());
        updateUser.setString(3, userUpdate.getName());
        updateUser.setInt(4, userUpdate.getStaffType().getID());
        
        updateUser.executeUpdate();
    }
    
    /**
     *  This method performs an insert on the database using a given Product.
     * @param productNew Product - being the details to add to the database.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
    public void newProduct(Product productNew) throws SQLException {
        addProduct = connection.prepareStatement(addProductQ);

        // Populate query - we do not need to use ID:
        addProduct.setInt(1, productNew.getStockCount());
        addProduct.setString(2, productNew.getName());
        addProduct.setBoolean(3, productNew.isAvailable());
        addProduct.setDouble(4, productNew.getUnitPrice());
        addProduct.setInt(5, productNew.getCategory().getID());
        addProduct.setInt(6, productNew.getSubCategory().getID());
        addProduct.setInt(7, productNew.getContainer().getID());

        addProduct.executeUpdate();
    }
    
    public void newOrderQ2(int quantity, int staffOrderedID, int productID, int locationID) throws SQLException {
        addOrder = connection.prepareStatement(addOrderQ2);
        // Populate query - we do not need to use ID:
        addOrder.setInt(1, quantity);
        addOrder.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(new Date())); 
        addOrder.setInt(3, staffOrderedID);
        addOrder.setInt(4, productID);
        addOrder.setInt(5, locationID);
        addOrder.setInt(6, 1);
        
        addOrder.executeUpdate();
        
        
                
        updateOrderProduct = connection.prepareStatement(updateOrderProductQ2);
        // Populate query - we do not need to use ID:
        updateOrderProduct.setInt(1, quantity);
        updateOrderProduct.setInt(2, productID);
        
        System.out.println("update Order PRoduct ******" + updateOrderProduct);
                
        updateOrderProduct.executeUpdate();     
      /*  private final String    updateOrderQ2       =
            "UPDATE NET302.Products SET  stockCount = stockCount - ?
            WHERE NET302.Products.ID = ?";*/
//        
//        
//        addOrder = connection.prepareStatement(addOrderQ2);
//        /*private final String        addOrderQ2       =
//            "INSERT INTO orders ("
//            + "quantity, dateOrdered, staffID, productID, locationID, statusID)"
//            + " VALUES (?, ?, ?, ?, ?, ?);";*/
//
//        
//        // Populate query - we do not need to use ID:
//        addOrder.setInt(1, orderNew.getQuantity());
//        addOrder.setString(2, orderNew.getDateOrdered());
//        addOrder.setInt(3, orderNew.getStaffOrdered().getID());
//        addOrder.setInt(4, orderNew.getProduct().getID());
//        addOrder.setInt(5, orderNew.getLocation().getID());
//        addOrder.setInt(6, orderNew.getStatus().getID());
//        
//        addOrder.executeUpdate();
    }
    
    /**
     * This method performs an insert on the database using a given Order.
     * @param orderNew Order - being the details to add to the database.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
    public void newOrder(Order orderNew) throws SQLException {
        addOrder = connection.prepareStatement(addOrderQ);
        /*private final String        addOrderQ2       =
            "INSERT INTO orders ("
            + "quantity, dateOrdered, staffID, productID, locationID, statusID)"
            + " VALUES (?, GETDATE(), ?, ?, ?, ?);";*/
        
        //SHOULD GETDATE() be ? - need to test but i feel like this is against the
        //system as we are specifying date and even implementing it below. will this not
        //cause an SQL error? - See newOrderQ2 above

        
        // Populate query - we do not need to use ID:
        addOrder.setInt(1, orderNew.getQuantity());
        addOrder.setString(2, orderNew.getDateOrdered());
        addOrder.setInt(3, orderNew.getStaffOrdered().getID());
        addOrder.setInt(4, orderNew.getProduct().getID());
        addOrder.setInt(5, orderNew.getLocation().getID());
        addOrder.setInt(6, orderNew.getStatus().getID());
        
        addOrder.executeUpdate();
    }
    
    /**
     * This method performs an insert on the database using a given User and
     * the hashed password (not contained in the User object).
     * @param newUser User - being the details to add to the database.
     * @param password String - being the hashed password string.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
    public void newUser(User newUser, String password) throws SQLException {
        addUser = connection.prepareStatement(addUserQ);
        
        // Populate query:
        addUser.setString(1, newUser.getUsername());
        addUser.setString(2, newUser.getContact());
        addUser.setString(3, newUser.getName());
        addUser.setInt(4, newUser.getStaffType().getID());
        addUser.setString(5, password);
        
        addUser.executeQuery();
    }
    
    /**
     *  This method queries the password for a given ID and password to compare.
     * If the password given is equal to the one stored then it returns true, 
     * else false.
     * @param username String - being the username to check for.
     * @param password String - being the hashed password to check for.
     * @return User - being the User object if successful, else NULL. Checks
     * should be made when using this method to ensure the object is not NULL!
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it.
     */
    public User authUser(String username, String password) throws SQLException {
        authUser = connection.prepareStatement(authUserQ);
        authUser.setString(1, username);
        authUser.setString(2, password);
        resultSet = authUser.executeQuery();
        
        resultSet.first();
        int i = resultSet.getRow();
        if (i == 0) { return null; }
        else { 
            // TODO: May be worth utilising the below, better code re-use practise and this is part of the grade.
            // The SQL query must also be swapped over (see way up above).
            //return this.constructUser(resultSet);
            return new User(resultSet.getInt(1), resultSet.getString(4), "", resultSet.getString(3), resultSet.getString(2), true, new GenericLookup(resultSet.getInt(6), resultSet.getString(5))); 
        }
    }
    
    /**
     * This method queries the database for a cetain username existing, so that
     * the mobile application can use it to log in with security passes.
     * @param username String - being the username to search for.
     * @return boolean - being whether or not the username was found.
     * @throws SQLException - Thrown as errors will be passed back to JSP pages.
     * Error here indicates a problem with the SQLQuery or the execution of it. 
     */
    public boolean checkUsername(String username) throws SQLException {
        checkUsername = connection.prepareStatement(checkUsernameQ);
        checkUsername.setString(1, username);
        resultSet = checkUsername.executeQuery();
        
        return (resultSet.getString("username").length() > 0);
    }
}
