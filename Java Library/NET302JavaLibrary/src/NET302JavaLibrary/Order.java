package NET302JavaLibrary;
import java.sql.Time;
import com.google.gson.Gson;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.TemporalQueries.localDate;

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
