package net302;

import Connector.Connector;
import NET302JavaLibrary.*;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.Dependent;


/**
 * ManagementBean, provides methods and data access for the web session.
 * @author Mellor
 */
@Named(value = "managementBean")
@Dependent
public class ManagementBean {
    // DEBUG: If true, then no commands will be sent to the REST server!
    private final boolean debug = true;
    
    // TODO LISR:
    // How often to update the data? Perhaps only when needed, using the pages?
    // It should be cached per session thanks to the @Depenedant tag.
    // Should fetching all data even be used? It works well for DummyData to
    // display and populate the pages but perhaps it will be querying too much!
    
    private final Connector             client_connector;
    private final DummyData             dummy_data;
    
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
        // Get the connector:
        client_connector = new Connector();
        dummy_data = new DummyData(10);
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
    public void orderProduct(Product p, int quantity, User staff) {
        // TODO: Assign these values, not sure if default or extracted from the
        // webpage. For the latter, the method requires additional parameters!
        
        GenericLookup location  = null;
        GenericLookup status    = null;
        
        // Generate the Order:
        Order o = new Order(0,  // ID of Order, only used to encapsulate the data here.
                quantity,       // Quantity of p to order.
                false,          // Fulfilled is false by default.
                "",             // Date Ordered
                staff,          // Staff who ordered.
                p,              // Product to order.
                location,       // Location
                status          // Status ?
        );
        
        // Action the generated Order:
        if (debug) { 
            System.out.println("[DEBUG] ORDER GENERATED FOR " + quantity + " OF " + p.getName());
            orders.add(o);
            dummy_data.setOrders(orders);
        } else  { client_connector.addOrder(o); }
    }
    
    //************************************************************************//
    //  -   ARRAYLIST LOADERS                                             -   //
    //************************************************************************//
    // These methods fetch/update the local ArrayLists with data either from  //
    // the DummyData class or from the middleware connection (depending on    //
    // debug). They also return the ArrayList for use in the webpages.        //
    //************************************************************************//
    
    /**
     * Gets the up-to-date ArrayList of Products from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<Product> - being the list of Products.
     */
    public ArrayList<Product> loadProducts() {
        if (debug) {
            products = dummy_data.getProducts();
        } else { products = client_connector.getAllProducts(); }
        return products;
    }
    
    /**
     * Gets the up-to-date ArrayList of Orders from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<Order> - being the list of Orders.
     */
    public ArrayList<Order> loadOrders() {
        if (debug) {
            orders = dummy_data.getOrders();
        } else { orders = client_connector.getAllOrders(); }
        return orders;
    }
    
    /**
     * Gets the up-to-date ArrayList of Users from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<User> - being the list of Users.
     */
    public ArrayList<User> loadUsers() {
        if (debug) {
            users = dummy_data.getUsers();
        } else { users = client_connector.getAllUsers(); }
        return users;
    }
    
    /**
     * Gets the up-to-date ArrayList of Product categories from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<GenericLookup> - being the list of categories.
     */
    public ArrayList<GenericLookup> loadCategories() {
        if (debug) {
            categories = dummy_data.getCategories();
        } else { categories = client_connector.getAllLookups("category"); }
        return categories;
    }
    
    /**
     * Gets the up-to-date ArrayList of sub-categories from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<GenericLookup> - being the list of sub-categories.
     */
    public ArrayList<GenericLookup> loadSubCategories() {
        if (debug) {
            subCategories = dummy_data.getSubCategories();
        } else { subCategories = client_connector.getAllLookups("subcategory"); }
        return subCategories;
    }
    
    /**
     * Gets the up-to-date ArrayList of the Product containers from the REST
     * server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<GenericLookup> - being the list of Product containers.
     */
    public ArrayList<GenericLookup> loadContainers() {
        if (debug) {
            containers = dummy_data.getContainers();
        } else { containers = client_connector.getAllLookups("container"); }
        return containers;
    }
    
    /**
     * Gets the up-to-date ArrayList of Order statuses from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<GenericLookup> - being the list of Order statuses.
     */
    public ArrayList<GenericLookup> loadStatuses() {
        if (debug) {
            orderStatus = dummy_data.getOrderStatus();
        } else { orderStatus = client_connector.getAllLookups("status"); }
        return orderStatus;
    }
    
    /**
     * Gets the up-to-date ArrayList of Locations from the REST server.
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<GenericLookup> - being the list of Locations.
     */
    public ArrayList<GenericLookup> loadLocations() {
        if (debug) {
            locations = dummy_data.getLocations();
        } else { locations = client_connector.getAllLookups("location"); }
        return locations;
    }
    
    /**
     * Gets the up-to-date ArrayList of Orders which are unfulfilled, from the 
     * REST server. 
     * Note: debug mode will refer to DummyData instead!
     * @return ArrayList<Order> - being the list of unfulfilled Orders.
     */
    public ArrayList<Order> loadUnfulfilled() {
        if (debug) {
            // Not a supported method in DummyData, will manually remove here:
            unfulfilled = dummy_data.getOrders();
            for (Order u: unfulfilled) {
                if (u.isFulfilled()) {
                    unfulfilled.remove(u);
                }
            }
        } else { unfulfilled = client_connector.getUnfulfilled(); }
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
        return products;
    }

    /**
     * Gets the local copy of the Orders ArrayList.
     * @return ArrayList<Order>
     */
    public ArrayList<Order> getOrders() {
        return orders;
    }

    /**
     * Gets the local copy of the Users ArrayList.
     * @return ArrayList<User>
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Gets the local copy of the categories ArrayList.
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<GenericLookup> getCategories() {
        return categories;
    }

    /**
     * Gets the local copy of the subcategories ArrayList.
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<GenericLookup> getSubCategories() {
        return subCategories;
    }

    /**
     * Gets the local copy of the Order statuses ArrayList
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<GenericLookup> getOrderStatus() {
        return orderStatus;
    }

    /**
     * Gets the local copy of the containers ArrayList.
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<GenericLookup> getContainers() {
        return containers;
    }

    /**
     * Gets the local copy of the locations ArrayList.
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<GenericLookup> getLocations() {
        return locations;
    }

    /**
     * Gets the local copy of the unfulfilled Orders ArrayList.
     * @return ArrayList<GenericLookup>
     */
    public ArrayList<Order> getUnfulfilled() {
        return unfulfilled;
    }
}
