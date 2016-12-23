package NET302JavaLibrary;

import java.util.ArrayList;

/**
 * DummyData to represent the Middleware without having the query and use up
 * the AWS allowances. This creates a series of numbers in place of any real
 * information, simply for testing object manipulation and table display.
 * 
 * MUST BE INITIALISED IN THE CONSTRUCTOR.
 * 
 * @author Sam
 */
public class DummyData {
    
    private ArrayList<Product>          products;
    private ArrayList<Order>            orders;
    private ArrayList<User>             users;
    private ArrayList<GenericLookup>    categories;
    private ArrayList<GenericLookup>    subCategories;
    private ArrayList<GenericLookup>    orderStatus;
    private ArrayList<GenericLookup>    containers;
    private ArrayList<GenericLookup>    staffTypes;
    private ArrayList<GenericLookup>    locations;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<GenericLookup> getCategories() {
        return categories;
    }

    public ArrayList<GenericLookup> getSubCategories() {
        return subCategories;
    }

    public ArrayList<GenericLookup> getOrderStatus() {
        return orderStatus;
    }

    public ArrayList<GenericLookup> getContainers() {
        return containers;
    }

    public ArrayList<GenericLookup> getStaffTypes() {
        return staffTypes;
    }

    public ArrayList<GenericLookup> getLocations() {
        return locations;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setCategories(ArrayList<GenericLookup> categories) {
        this.categories = categories;
    }

    public void setSubCategories(ArrayList<GenericLookup> subCategories) {
        this.subCategories = subCategories;
    }

    public void setOrderStatus(ArrayList<GenericLookup> orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setContainers(ArrayList<GenericLookup> containers) {
        this.containers = containers;
    }

    public void setStaffTypes(ArrayList<GenericLookup> staffTypes) {
        this.staffTypes = staffTypes;
    }

    public void setLocations(ArrayList<GenericLookup> locations) {
        this.locations = locations;
    }

    public DummyData(int dataSize) {
        products = new ArrayList<>();
        orders = new ArrayList<>();
        users = new ArrayList<>();
        categories = new ArrayList<>();
        subCategories = new ArrayList<>();
        orderStatus = new ArrayList<>();
        containers = new ArrayList<>();
        staffTypes = new ArrayList<>();
        locations = new ArrayList<>();
        
        for (int i = 0; i < dataSize; i++) {
            GenLookups(i);
        }
        
        for (int j = 0; j < dataSize; j++) {
            GenUser(j);
            GenProduct(j);
        }
        
        for (int i = 0; i < dataSize; i++) {
            GenOrders(i);
        }
    }
    
    private void GenLookups(int i) {
        GenericLookup con = new GenericLookup(i, "container" + i);
        GenericLookup cat = new GenericLookup(i, "category" + i);
        GenericLookup sub = new GenericLookup(i, "subcategory" + i);
        GenericLookup sta = new GenericLookup(i, "status" + i);
        GenericLookup typ = new GenericLookup(i, "staff-type" + i);
        GenericLookup loc = new GenericLookup(i, "location" + i);
        
        categories.add(cat);
        subCategories.add(sub);
        orderStatus.add(sta);
        containers.add(con);
        locations.add(loc);
    }
    
    private void GenUser(int i) {
        User u = new User(i,
                "username" + i, 
                "password" + i, 
                "contact" + i, 
                "name" + i, 
                false,
                staffTypes.get(i)
                
        );
        users.add(u);
    }
    
    private void GenProduct(int i) {
        Product p = new Product(i,
                i*3,
                "name" + i,
                (i % 4) != 0,
                (double)i*3.14,
                categories.get(i),
                subCategories.get(i),
                containers.get(i)
        );
        products.add(p);
    }
    
    private void GenOrders(int i) {
        Order o = new Order(i, 
                (int) (1.5*i),
                (i % 5) != 0,
                "dateOrdered",
                users.get(i),
                products.get(i),
                locations.get(i),
                orderStatus.get(i)
        );
    }
}
