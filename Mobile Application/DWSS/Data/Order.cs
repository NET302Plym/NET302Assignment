using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
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
        /// Date the order was created
        /// </summary>
        public string dateDelivered { get; set; } = "";
        /// <summary>
        /// Name of the customer
        /// </summary>
        public string custName { get; set; } = "";
        /// <summary>
        /// The customer contact info
        /// </summary>
        public string custContact { get; set; } = "";
        /// <summary>
        /// The location of the customer
        /// </summary>
        public string location { get; set; } = "";
        /// <summary>
        /// The staff member who fulfilled the order
        /// </summary>
        public string staffFulfilled { get; set; }
        /// <summary>
        /// The product being ordered
        /// </summary>
        public Product product { get; set; }
        /// <summary>
        /// The time the order was delivered
        /// </summary>
        public DateTime timeDelivered { get; set; }
        /// <summary>
        /// Whether or not the order has been fulfilled
        /// </summary>
        public bool fulfilled { get; set; }
        /// <summary>
        /// This is the required constructor, and all elements are required to constructing
        /// </summary>
        [JsonConstructor]
        public Order() { }
        public Order(int id, string date, string custName, string custContact, string location, string staffFulfilled, Product product, DateTime timeDelivered, bool fulfilled, int quantity)
        {
            this.ID = id;
            this.dateDelivered = date;
            this.custName = custName;
            this.custContact = custContact;
            this.location = location;
            this.staffFulfilled = staffFulfilled;
            this.product = product;
            this.timeDelivered = timeDelivered;
            this.fulfilled = fulfilled;
            this.quantity = quantity;
        }
        public Order(string jsonString)
        {
            var order = Newtonsoft.Json.JsonConvert.DeserializeObject<Order>(jsonString);
            this.ID = order.ID;
            this.dateDelivered = order.dateDelivered;
            this.custName = order.custName;
            this.custContact = order.custContact;
            this.location = order.location;
            this.staffFulfilled = order.staffFulfilled;
            this.product = order.product;
            this.timeDelivered = order.timeDelivered;
            this.fulfilled = order.fulfilled;
        }
    }
}
