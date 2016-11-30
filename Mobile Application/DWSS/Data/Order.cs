﻿using System;
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
        
        //public Order(string jsonString)
        //{
        //    var order = Newtonsoft.Json.JsonConvert.DeserializeObject<Order>(jsonString);
        //    this.ID = order.ID;
        //    this.dateDelivered = order.dateDelivered;
        //    this.location = order.location;
        //    this.staffFulfilled = order.staffFulfilled;
        //    this.product = order.product;
        //    this.timeDelivered = order.timeDelivered;
        //    this.fulfilled = order.fulfilled;
        //}
    }
}