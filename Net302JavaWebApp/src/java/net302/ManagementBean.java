/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net302;

import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Mellor
 */
@Named(value = "managementBean")
@Dependent
public class ManagementBean
{
    private  ArrayList<ProductBean> productList;
    private ArrayList<OrderBean> orderItems;
    /**
     * Creates a new instance of ManagementBean
     */
    public ManagementBean()
    {
        retrieveStock();
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
        OrderBean orderItem = new OrderBean(ID,quantity);
        orderItems.add(orderItem);
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
