using System;
using System.Collections.Generic;
using DWSS.Data;

namespace DWSS.Development
{
    /// <summary>
    /// You can ignore this. 
    /// This was initially used to provide some data to use within the application before the REST service was up and running 
    /// </summary>
    class DummyData
    {
        /// <summary>
        /// Returns a few fake orders
        /// </summary>
        /// <returns></returns>
        public static List<Order> GetOrders()
        {
            var orderList = new List<Order>();
            foreach(var product in GetProdutcs())
            {
                orderList.Add(new Order
                {
                    ID = 1,
                    quantity = 5,
                    fulfilled = false,
                    dateOrdered = "15/10/1992",
                    dateDelivered = "",
                    timeDelivered = new DateTime(),
                    staffOrdered = new User
                    {
                        ID = 1,
                        username = "bobshot",
                        password = "",
                        contact = "",
                        name = "Bob Shot",
                        authenticated = true,
                        staffType = new GenericLookup
                        {
                            ID = 1,
                            value = "Boss"
                        }
                    },
                    staffFulfilled = null,
                    product = product,
                    location = new GenericLookup
                    {
                        ID = 1,
                        value = "Default Location"
                    },
                    status = new GenericLookup
                    {
                        ID = 1,
                        value = "Unfulfilled?"
                    }
                }
                );
            }
            return orderList;
        }

        /// <summary>
        /// Returns a few fake products
        /// </summary>
        /// <returns></returns>
        public static List<Product> GetProdutcs()
        {
            var productList = new List<Product>();
            foreach(string s in new string[] { "Blue Pen", "Red Pen", "Black Pen", "Yellow Pen", "Purple Pen", "Multicoloured Pen", "Turquise Pen", "500GB", "1000GB", "2000GB", "4000GB", "8000GB" })
            {
                productList.Add(new Product
                {
                    ID = 1,
                    stockCount = 5,
                    available = true,
                    name = s,
                    unitPrice = 5.0,
                    category = new GenericLookup
                    {
                        ID = 1,
                        value = "Stationary"
                    },
                    subCategory = new GenericLookup
                    {
                        ID = 1,
                        value = "Pens"
                    },
                    container = new GenericLookup
                    {
                        ID = 1,
                        value = "Small"
                    }
                });
            }
            return productList;
        } 
    }
}
