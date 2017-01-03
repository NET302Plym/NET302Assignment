package NET302JavaLibrary;
import java.sql.Time;
import com.google.gson.Gson;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Order {
    //************************************************************************//
    //  -   VARIABLES AND CONSTRUCTORS                                    -   //
    //************************************************************************//
    private final int ID;
    private int     quantity;
    private boolean fulfilled;
    private String  dateOrdered;
    private String  dateDelivered;
    private Time    timeDelivered;
    private User    staffOrdered;
    private User    staffFulfilled;
    private Product product;
    private GenericLookup location;
    private GenericLookup status;

    public Order(int ID, int quantity, boolean fulfilled, String dateOrdered, User staffOrdered, Product product, GenericLookup location, GenericLookup status) {
        this.ID = ID;
        this.quantity = quantity;
        this.fulfilled = fulfilled;
        this.dateOrdered = dateOrdered;
        this.staffOrdered = staffOrdered;
        this.product = product;
        this.location = location;
        this.status = status;
    }
    /**
     * DONT USE FOR ANYTHING OTHER THAN NEW ORDER
     * @param p
     * @param quantity
     * @param user
     * @param limitData 
     */
    public Order(Product p, int quantity, User user, Boolean limitData)
    {
        LocalDate localDate = LocalDate.now();
         if (limitData)
        {
            this.ID = p.getID();
            this.quantity = quantity;
            this.dateOrdered = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
            this.staffOrdered = user;
            this.product = p;
            this.location = new GenericLookup(1, "South West");
            this.status = new GenericLookup(0, "Open");
            this.product.setName("");
            this.product.setCategory(null);
            this.product.setCategory(null);
            this.product.setContainer(null);
            this.product.setName(null);
            this.product.setStockCount(40);
            this.product.setSubCategory(null);
            this.product.setUnitPrice(null);
       // addOrder.setInt(1, orderNew.getQuantity());
       // addOrder.setString(2, orderNew.getDateOrdered());
       // addOrder.setInt(3, orderNew.getStaffOrdered().getID());
       // addOrder.setInt(4, orderNew.getProduct().getID());
       // addOrder.setInt(5, orderNew.getLocation().getID());
       // addOrder.setInt(6, orderNew.getStatus().getID());
        }
        else
         {
            this.ID = p.getID();
            this.quantity = quantity;
            this.fulfilled = false;
            this.dateOrdered = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
            this.staffOrdered = user;
            this.product = p;
            this.location = new GenericLookup(1, "South West");
            this.status = new GenericLookup(0, "Open");
         }
    }
    
    //************************************************************************//
    //  -   GETTERS + SETTERS                                             -   //
    //************************************************************************//

    /**
     * Gets the ID.
     * @return int - being the ID.
     */
    public int getID() {
        return ID;
    }
    
    /**
     * Gets the Quantity.
     * @return int - being the Quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set the Quantity.
     * @param quantity int - being the new Quantity.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets whether or not the Order is fulfilled.
     * @return boolean - is the Order fulfilled?
     */
    public boolean isFulfilled() {
        return fulfilled;
    }

    /**
     * Sets whether or not the Order is fulfilled.
     * @param fulfilled boolean - is the Order fulfilled?
     */
    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    /**
     * Gets the date ordered.
     * @return String - the date ordered in String format.
     */
    public String getDateOrdered() {
        return dateOrdered;
    }

    /**
     * Sets the date ordered.
     * @param dateOrdered String - the ordered in String format.
     */
    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    /**
     * Gets the date delivered.
     * @return String - being the date delivered in String format.
     */
    public String getDateDelivered() {
        return dateDelivered;
    }

    /**
     * Sets the date delivered.
     * @param dateDelivered String - being the date delivered in String format.
     */
    public void setDateDelivered(String dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    /**
     * Gets the time delivered.
     * @return Time - being the time delivered.
     */
    public Time getTimeDelivered() {
        return timeDelivered;
    }

    /**
     * Sets the time delivered.
     * @param timeDelivered Time - being the time delivered.
     */
    public void setTimeDelivered(Time timeDelivered) {
        this.timeDelivered = timeDelivered;
    }

    /**
     * Gets the Staff details who ordered this.
     * @return User - being the Staff who ordered this.
     */
    public User getStaffOrdered() {
        return staffOrdered;
    }

    /**
     * Sets the Staff details who ordered this.
     * @param staffOrdered User - being the Staff who ordered this.
     */
    public void setStaffOrdered(User staffOrdered) {
        this.staffOrdered = staffOrdered;
    }

    /**
     * Gets the Staff details of who fulfilled the Order.
     * @return User - being the details of the Staff who fulfilled the Order.
     */
    public User getStaffFulfilled() {
        return staffFulfilled;
    }

    /**
     * Sets the Staff details of who fulfilled the Order.
     * @param staffFulfilled User - being the details of the Staff who fulfilled the Order.
     */
    public void setStaffFulfilled(User staffFulfilled) {
        this.staffFulfilled = staffFulfilled;
    }

    /**
     * Gets the Product details of the Order.
     * @return Product - being the details.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Sets the Product details of the Order.
     * @param product Product - being the details.
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Gets the location information.
     * @return GenericLookup - being the location information.
     */
    public GenericLookup getLocation() {
        return location;
    }

    /**
     * Sets the location information.
     * @param location GenericLookup - being the location information.
     */
    public void setLocation(GenericLookup location) {
        this.location = location;
    }

    /**
     * Gets the status information.
     * @return GenericLookup - being the status information.
     */
    public GenericLookup getStatus() {
        return status;
    }

    /**
     * Sets the status information.
     * @param status GenericLookup - being the status information.
     */
    public void setStatus(GenericLookup status) {
        this.status = status;
    }
    
    //************************************************************************//
    //  -   toString                                                      -   //
    //************************************************************************//

    /**
     * toString override to provide object information.
     * @return String - being the contents of the object.
     */
    @Override
    public String toString() {
        return "Order{" + "ID=" + ID + ", quantity=" + quantity + ", fulfilled=" + fulfilled 
                + ", dateOrdered=" + dateOrdered + ", dateDelivered=" 
                + dateDelivered + ", timeDelivered=" + timeDelivered 
                // Objects below use their toString method:
                + ", staffOrdered=" + staffOrdered.toString()
                + ", staffFulfilled=" + staffFulfilled.toString()
                + ", product=" + product.toString()
                + ", location=" + location.toString() 
                + ", status-" + status.toString() + '}';
    }
    
    //************************************************************************//
    //  -   GSON/JSON HELPER METHODS                                      -   //
    //************************************************************************//
    
    /**
     * Creates the object using a JSON string.
     * @param jsonString String - being the JSON string representing this object.
     */
    public Order(String jsonString){
        Gson gson = new Gson();
        Order order = gson.fromJson(jsonString, Order.class);
        this.ID             = order.getID();
        this.quantity       = order.getQuantity();
        this.fulfilled      = order.isFulfilled();
        this.dateDelivered  = order.getDateDelivered();
        this.dateOrdered    = order.getDateOrdered();
        this.timeDelivered  = order.getTimeDelivered();
        this.staffOrdered   = order.getStaffOrdered();
        this.staffFulfilled = order.getStaffFulfilled();
        this.product        = order.getProduct();
        this.location       = order.getLocation();
        this.status         = order.getStatus();
    }
    
    /**
     * Uses GSON to get the JSON string representing this object.
     * @return String - being the JSON string.
     */
    public String GetJSONString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
