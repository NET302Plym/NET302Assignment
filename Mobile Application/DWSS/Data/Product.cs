using Newtonsoft.Json;

namespace DWSS.Data
{
    public class Product
    {
        /// <summary>
        /// The ID for the product
        /// </summary>
        public int ID { get; set; }
        /// <summary>
        /// The current stock count of the item (how many there are available)
        /// </summary>
        public int stockCount { get; set; }
        /// <summary>
        /// Whether or not the item is currently available
        /// </summary>
        public bool available { get; set; }
        /// <summary>
        /// The name for the product
        /// </summary>
        public string name { get; set; }
        /// <summary>
        /// The unit price for the product
        /// </summary>
        public double unitPrice { get; set; }
        /// <summary>
        /// The category of the product
        /// </summary>
        public GenericLookup category { get; set; }
        /// <summary>
        /// The sub category for the item
        /// </summary>
        public GenericLookup subCategory { get; set; }
        /// <summary>
        /// The container info for the product
        /// </summary>
        public GenericLookup container { get; set; }
                
        /// <summary>
        /// A blank JSON constructor (required for deserialization)
        /// </summary>
        [JsonConstructor]
        public Product() { }
    }
}
