/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NET302JavaLibrary;

import java.util.ArrayList;

/**
 *
 * @author Dan
 */
public class DummyData {
    
    private final ArrayList<Product>          products;
    private final ArrayList<Order>            orders;
    private final ArrayList<User>             users;
    private final ArrayList<GenericLookup>    categories;
    private final ArrayList<GenericLookup>    subCategories;
    private final ArrayList<GenericLookup>    orderStatus;
    private final ArrayList<GenericLookup>    containers;
    private final ArrayList<GenericLookup>    staffTypes;
    private final ArrayList<GenericLookup>    locations;

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
