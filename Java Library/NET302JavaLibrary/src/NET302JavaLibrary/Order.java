package NET302JavaLibrary;
import java.sql.Time;
import com.google.gson.Gson;

public class Order {
    // Local Variables
    int     ID;
    int     quantity;
    String  dateDelivered;
    User  customer;
    String  location;
    User  staffFulfilled;
    Product product;
    Time    timeDelivered;
    Boolean fulfilled;
    
    // Constructors
    /**
     * This should be used for the construction from the Database on the Middleware side (as is requires all information)
     * @param ID
     * @param quantity
     * @param dateDelivered
     * @param customer
     * @param location
     * @param staffFulfilled
     * @param product
     * @param timeDelivered
     * @param fulfilled 
     */
    public Order(int ID, int quantity, String dateDelivered, User customer, String location, User staffFulfilled, Product product, Time timeDelivered, Boolean fulfilled) {
        this.ID = ID;
        this.quantity = quantity;
        this.dateDelivered = dateDelivered;
        this.customer = customer;
        this.location = location;
        this.staffFulfilled = staffFulfilled;
        this.product = product;
        this.timeDelivered = timeDelivered;
        this.fulfilled = fulfilled;
    }
    /**
     * Creates a new order (client side)
     * @param quantity Quantity requested
     * @param user The user logged in
     * @param product The product requested
     */
    public Order(int quantity, User user, Product product){
        this.quantity = quantity;
        this.customer = user;
        this.product = product;
    }
    
    // Set / Gets
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(String dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getStaffFulfilled() {
        return staffFulfilled;
    }

    public void setStaffFulfilled(User staffFulfilled) {
        this.staffFulfilled = staffFulfilled;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Time getTimeDelivered() {
        return timeDelivered;
    }

    public void setTimeDelivered(Time timeDelivered) {
        this.timeDelivered = timeDelivered;
    }

    public Boolean getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(Boolean fulfilled) {
        this.fulfilled = fulfilled;
    }
        
    // ToString Override

    @Override
    public String toString() {
        return "Order{" + "ID=" + ID + ", quantity=" + quantity + ", dateDelivered=" + dateDelivered + ", customer=" + customer + ", location=" + location + ", staffFulfilled=" + staffFulfilled + ", product=" + product + ", timeDelivered=" + timeDelivered + ", fulfilled=" + fulfilled + '}';
    }
        
    // JSON Conversion
    public Order(String jsonString){
        Gson gson = new Gson();
        Order order = gson.fromJson(jsonString, Order.class);
        this.ID = order.ID;
        this.quantity = order.quantity;
        this.dateDelivered = order.dateDelivered;
        this.customer = order.customer;
        this.location = order.location;
        this.staffFulfilled = order.staffFulfilled;
        this.product = order.product;
        this.timeDelivered = order.timeDelivered;
        this.fulfilled = order.fulfilled;
    }
    
    public String GetJSONString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
