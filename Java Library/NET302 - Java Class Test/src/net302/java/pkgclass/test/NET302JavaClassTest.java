package net302.java.pkgclass.test;
import java.sql.*;
public class NET302JavaClassTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Test the JSON outputs of the classes here 
//        Order order = new Order(5, new User("Dan", "Password"), new Product(1, 1, "Cat", "Cat2", "Name", "Container", 5.0, true));
//        System.out.println("Order:");
//        System.out.println(order.GetJSONString());
//        System.out.println("Product:");
//        System.out.println(order.getProduct().GetJSONString());
//        User user = new User("Dan", "Password");
//        System.out.println("User:");
//        System.out.println(user.GetJSONString());
//        
//        // Check the deserialization
//        Order orderD = new Order(order.GetJSONString());
//        User userD = new User(user.GetJSONString());
//        System.out.println(orderD.toString());
//        System.out.println(orderD.getProduct().toString());
//        System.out.println(userD.toString());
//        
//        for (int i = 0; i < DummyData.GetProdutcs().size(); i++)
//            System.out.println(DummyData.GetProdutcs().get(i));

//       String url = "jdbc:mysql://mysqlinstance.ccamcguf5lrd.us-west-2.rds.amazonaws.com/NET302";
//       String user = "root";
//       String password = "net302rootuser";
       String url = "jdbc:mysql://net302.cli7vsmzzdht.us-west-2.rds.amazonaws.com:3306";
       String user = "root";
       String password = "password1";
       try {
       Class.forName("com.mysql.jdbc.Driver").newInstance();
       Connection connection = DriverManager.getConnection(url, user, password);
       //Statement statement = connection.createStatement();
       //statement.execute("INSERT INTO new_table VALUES (2,'test')");
       //statement.close();
       
       connection.close();
       } catch (Exception ex)
       {
           System.out.println("Exception: " + ex.getMessage());
       }
    }
}
