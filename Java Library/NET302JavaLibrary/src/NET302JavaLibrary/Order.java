package NET302JavaLibrary;
import java.sql.Time;
import com.google.gson.Gson;

// TODO: JavaDoc comments throughout.

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
    
    //************************************************************************//
    //  -   GETTERS + SETTERS                                             -   //
    //************************************************************************//

    public int getID() {
        return ID;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public String getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public String getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(String dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public Time getTimeDelivered() {
        return timeDelivered;
    }

    public void setTimeDelivered(Time timeDelivered) {
        this.timeDelivered = timeDelivered;
    }

    public User getStaffOrdered() {
        return staffOrdered;
    }

    public void setStaffOrdered(User staffOrdered) {
        this.staffOrdered = staffOrdered;
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

    public GenericLookup getLocation() {
        return location;
    }

    public void setLocation(GenericLookup location) {
        this.location = location;
    }

    public GenericLookup getStatus() {
        return status;
    }

    public void setStatus(GenericLookup status) {
        this.status = status;
    }
    
    //************************************************************************//
    //  -   toString                                                      -   //
    //************************************************************************//

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
    
    public String GetJSONString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
