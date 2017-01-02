using System;
using Newtonsoft.Json;

namespace DWSS.Data
{
    public class Order
    {
        /// <summary>
        /// A rigid ID for the order (unique in the db)
        /// </summary>
        public int ID { get; set; }
        /// <summary>
        /// Quantity of the product required
        /// </summary>
        public int quantity { get; set; }
        /// <summary>
        /// Whether or not the order has been fulfilled
        /// </summary>
        public bool fulfilled { get; set; }
        /// <summary>
        /// The date the order was ordered
        /// </summary>
        public string dateOrdered { set; get; }
        /// <summary>
        /// Date the order was created
        /// </summary>
        public string dateDelivered { get; set; } = "";
        /// <summary>
        /// The time the order was delivered
        /// </summary>
        public DateTime timeDelivered { get; set; }
        /// <summary>
        /// The staff of which ordered this order
        /// </summary>
        public User staffOrdered;
        /// <summary>
        /// The staff member who fulfilled the order
        /// </summary>
        public User staffFulfilled { get; set; }
        /// <summary>
        /// The product being ordered
        /// </summary>
        public Product product { get; set; }
        /// <summary>
        /// The location of the customer
        /// </summary>
        public GenericLookup location { get; set; }
        /// <summary>
        /// The current status of this order
        /// </summary>
        public GenericLookup status { set; get; }      
                
        /// <summary>
        /// This is the required constructor, and all elements are required to constructing
        /// </summary>
        [JsonConstructor]
        public Order() { }
        
        /// <summary>
        /// Returns an order with only the ID. This is used in the JSON -> JAVA upload as some of the other data isn't being passed correctly, mainly strings containing illegal chars
        /// </summary>
        /// <returns></returns>
        public Order StripExcessData()
        {
            Order o = new Order();
            o.ID = ID;
            return o;
        }
    }
}
