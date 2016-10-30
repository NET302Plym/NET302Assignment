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
    public static ArrayList<Product> GetProdutcs(){
        ArrayList<Product> list = new ArrayList<Product>();
        list.add(new Product(1, 500, "Stationary", "Pens", "Blue Pen", "Container1", 5.0, true));
        list.add(new Product(2, 500, "Stationary", "Pens", "Red Pen", "Container1", 5.0, true));
        list.add(new Product(3, 500, "Stationary", "Pens", "Black Pen", "Container1", 5.0, true));
        list.add(new Product(4, 500, "Stationary", "Pens", "Purple Pen", "Container1", 5.0, true));
        list.add(new Product(5, 500, "Stationary", "Pens", "Green Pen", "Container1", 5.0, true));
        list.add(new Product(6, 500, "Stationary", "Pens", "Orange Pen", "Container1", 5.0, true));
        list.add(new Product(7, 500, "Stationary", "Pens", "Yellow Pen", "Container1", 5.0, true));
        list.add(new Product(8, 500, "Stationary", "Pens", "Green-Red Pen", "Container1", 5.0, true));
        list.add(new Product(9, 500, "Stationary", "Pens", "Multicolour Pen", "Container1", 5.0, true));
        list.add(new Product(10, 500, "Stationary", "Pens", "Multi Pen", "Container1", 5.0, true));
        return list;
    }
}
