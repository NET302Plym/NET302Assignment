package NET302JavaLibrary;
import com.google.gson.Gson;

// TODO: JavaDoc comments throughout.

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
     * @param ID
     * @param stockCount
     * @param name
     * @param available
     * @param unitPrice
     * @param category
     * @param subCategory
     * @param container 
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

    public int getID() {
        return ID;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public GenericLookup getCategory() {
        return category;
    }

    public void setCategory(GenericLookup category) {
        this.category = category;
    }

    public GenericLookup getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(GenericLookup subCategory) {
        this.subCategory = subCategory;
    }

    public GenericLookup getContainer() {
        return container;
    }

    public void setContainer(GenericLookup container) {
        this.container = container;
    }
    
    //************************************************************************//
    //  -   toString                                                      -   //
    //************************************************************************//

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
    
    public String GetJSONString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
