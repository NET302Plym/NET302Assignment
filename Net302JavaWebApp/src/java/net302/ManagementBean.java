/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net302;


import Connector.Connector;
import NET302JavaLibrary.*;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import java.lang.Object;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author Mellor
 */
@Named(value = "managementBean")
@Dependent

public class ManagementBean
{
    private  ArrayList<ProductBean> productList;
    private ArrayList<OrderBean> orderItems = new ArrayList<OrderBean>();
    
    private ArrayList<Product>          products;
    private ArrayList<Order>            orders;
    private ArrayList<User>             users;
    private ArrayList<GenericLookup>    categories;
    private ArrayList<GenericLookup>    subCategories;
    private ArrayList<GenericLookup>    orderStatus;
    private ArrayList<GenericLookup>    containers;
    private ArrayList<GenericLookup>    staffTypes;
    private ArrayList<GenericLookup>    locations;
    /**
     * Creates a new instance of ManagementBean
     */
    public ManagementBean()
    {
        Connector c = new Connector();
        
        products = c.getAllProducts();
    }
    
    public void retrieveStock()
    {
        // populate productList from server when we can
        productList = DummyData.GetProdutcs();
    }

    public ArrayList<ProductBean> getProductList()
    {
        return productList;
    }
    
    public void submitOrder()
    {
        // for each orderItem -> to Json -> send to server 
    }
    
    public void addToTestOrder(int ID, int quantity)
    {
          String str = "";
        //get quantity from front end tabl
      
        System.out.println("ID: " + ID + ", Q: " + quantity);

        Order orderItem = new Order(ID,quantity);
        orderItems.add(orderItem);
        System.out.println("DERP ** Test Order Func");
        System.out.println(orderItems);
        
        con.addOrder(orderItem);
        
    }
    
    public void addToOrder(int quantity, UserBean user, ProductBean product)
    {
        OrderBean orderItem = new OrderBean(quantity,user,product);
        orderItems.add(orderItem);
        
        
        
    }

    public ArrayList<OrderBean> getOrderItems()
    {
        return orderItems;
    }
    
    
    
}
