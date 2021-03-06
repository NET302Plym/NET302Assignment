package NET302JavaLibrary;
import com.google.gson.Gson;

public class Product {
    // Local variables
    int     ID;
    int     stockCount;
    String  category;
    String  subCategory;
    String  name;
    String  container;
    Double  unitPrice;
    Boolean available;
    
    // Constructors
    /**
     * In theory this should only ever be called from the middleware, as the end clients won't be generating new products
     * @param ID
     * @param stockCount
     * @param category
     * @param subCategory
     * @param name
     * @param container
     * @param unitPrice
     * @param available 
     */
    public Product(int ID, int stockCount, String category, String subCategory, String name, String container, Double unitPrice, Boolean available) {
        this.ID = ID;
        this.stockCount = stockCount;
        this.category = category;
        this.subCategory = subCategory;
        this.name = name;
        this.container = container;
        this.unitPrice = unitPrice;
        this.available = available;
    }
    
    // Set / Gets
    public int getID() {
        return ID;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
    
    // ToString Override
    @Override
    public String toString() {
        return "Product{" + "ID=" + ID + ", stockCount=" + stockCount + ", category=" + category + ", subCategory=" + subCategory + ", name=" + name + ", container=" + container + ", unitPrice=" + unitPrice + ", available=" + available + '}';
    }
    
    // JSON Conversion
    public Product(String jsonString){
        Gson gson = new Gson();
        Product product = gson.fromJson(jsonString, Product.class);
        this.ID = product.ID;
        this.stockCount = product.stockCount;
        this.category = product.category;
        this.subCategory = product.subCategory;
        this.name = product.name;
        this.container = product.container;
        this.unitPrice = product.unitPrice;
        this.available = product.available;
    }
    
    public String GetJSONString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
