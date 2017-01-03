package NET302JavaLibrary;
import com.google.gson.Gson;

public class Product {
    //************************************************************************//
    //  -   VARIABLES AND CONSTRUCTORS                                    -   //
    //************************************************************************//
    private final int ID;
    private int     stockCount;
    private boolean available;
    private String  name;
    private Double  unitPrice;
    private GenericLookup   category;
    private GenericLookup   subCategory;
    private GenericLookup   container;

    /**
     * Constructor for creating a new Product object.
     * @param ID int - being the ID
     * @param stockCount int - being the number of stock.
     * @param name String - being the Name.
     * @param available boolean - being if the Product is available or not.
     * @param unitPrice Double - being the price per unit.
     * @param category GenericLookup - being the Category information.
     * @param subCategory GenericLookup - being the SubCategory information.
     * @param container GenericLookup - being the Container information.
     */
    public Product(int ID, int stockCount, String name, boolean available, Double unitPrice, GenericLookup category, GenericLookup subCategory, GenericLookup container) {
        this.ID = ID;
        this.stockCount = stockCount;
        this.name = name;
        this.available = available;
        this.unitPrice = unitPrice;
        this.category = category;
        this.subCategory = subCategory;
        this.container = container;
    }

    //************************************************************************//
    //  -   GETTERS + SETTERS                                             -   //
    //************************************************************************//

    /**
     * Gets the ID of the Product.
     * @return int - being the ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Gets the stock count of the Product .
     * @return int - being the stock count.
     */
    public int getStockCount() {
        return stockCount;
    }

    /**
     * Sets the stock count of the Product.
     * @param stockCount int - being the new stock count.
     */
    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    /**
     * Gets the Name of the Product.
     * @return String - being the Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Name of the Product.
     * @param name String - being the new Name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets whether or not the Product is available.
     * @return boolean - is the Product available?
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets the availability of the Product
     * @param available boolean - is the Product now available?
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    
    /**
     * Gets the Unit Price.
     * @return Double - being the Unit Price.
     */
    public Double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the Unit Price.
     * @param unitPrice Double - being the new Unit Price.
     */
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * Gets the Category information.
     * @return GenericLookup - being the Category information.
     */
    public GenericLookup getCategory() {
        return category;
    }

    /**
     * Sets the Category information.
     * @param category GenericLookup - being the new Category information.
     */
    public void setCategory(GenericLookup category) {
        this.category = category;
    }

    /**
     * Gets the SubCategory information.
     * @return GenericLookup - being the SubCategory information.
     */
    public GenericLookup getSubCategory() {
        return subCategory;
    }

    /**
     * Sets the SubCategory information.
     * @param subCategory GenericLookup - being the new SubCategory information.
     */
    public void setSubCategory(GenericLookup subCategory) {
        this.subCategory = subCategory;
    }

    /**
     * Gets the Container information.
     * @return GenericLookup - being the Container information.
     */
    public GenericLookup getContainer() {
        return container;
    }

    /**
     * Sets the Container information.
     * @param container GenericLookup - sets the Container information.
     */
    public void setContainer(GenericLookup container) {
        this.container = container;
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
        return "Product{" + "ID=" + ID + ", stockCount=" + stockCount + ", name=" + name 
                + ", available=" + available + ", unitPrice=" + unitPrice 
                // Objects below use their toString method:
                + ", category=" + category.toString() 
                + ", subCategory=" + subCategory.toString() 
                + ", container=" + container.toString() + '}';
    }
    
    //************************************************************************//
    //  -   GSON/JSON HELPER METHODS                                      -   //
    //************************************************************************//
    
    /**
     * Creates the object using a JSON string.
     * @param jsonString String - being the JSON string representing this object.
     */
    public Product(String jsonString){
        Gson gson = new Gson();
        Product product = gson.fromJson(jsonString, Product.class);
        this.ID             = product.getID();
        this.stockCount     = product.getStockCount();
        this.name           = product.getName();
        this.available      = product.isAvailable();
        this.unitPrice      = product.getUnitPrice();
        this.category       = product.getCategory();
        this.subCategory    = product.getSubCategory();
        this.container      = product.getSubCategory();
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
