using System.Collections.Generic;
using System.Linq;
using DWSS.Data;
using DWSS.Development;
using System.Threading.Tasks;
using System;

namespace DWSS.Middleware
{
    class MiddlewareConnections
    {
        private static bool isDebug = true;

        public async static Task<List<Order>> GetOutstandingOrders() // This could be working, everything is talking OK just no orders in the database to test this on. 
        {
            if (isDebug)
            {
                // Return dummy data
                return DummyData.GetOrders();
            }
            else
            {
                // Connect to the middleware
                List<Order> orderList;
                try
                {
                    string serverResponse = await MiddlewareHTTPClient.SendQuery("getUnfulfilled.jsp");
                    orderList = serverResponse == null ? new List<Order>() : Newtonsoft.Json.JsonConvert.DeserializeObject<List<Order>>(serverResponse);
                } catch (Exception)
                {
                    throw new Exception("Error Communicating With The Server");
                }
                return orderList;
            }
        }

        public async static Task<bool> FulfillOrder(Order orderToFulfil, User currentUser) // Untested but the syntax looks ok
        {
            if (isDebug)
            {
                return true;
            }
            else
            {
                try
                {
                    string serverResponse = await MiddlewareHTTPClient.SendQuery("addOrder.jsp?ORDER=" + Newtonsoft.Json.JsonConvert.SerializeObject(orderToFulfil) + "&NEW=FALSE");
                    return serverResponse.StartsWith("SUCCESS");
                } catch (Exception)
                {
                    return false;
                }
            }
        }

        public async static Task<User> GetUser(string username) // Untested but the syntax looks ok
        {
            if (isDebug) 
            {
                return new User()
                {
                    ID = 1, 
                    authenticated = true,
                    contact = "5",
                    password = "",
                    username = "Dan"
                };
            }
            else
            {
                string serverResponse = await MiddlewareHTTPClient.SendQuery("getUser.jsp?ID=0&UN=" + username);
                return Newtonsoft.Json.JsonConvert.DeserializeObject<User>(serverResponse);
            }
        }

        public async static Task<List<Product>> SearchForProduct(string searchTerms) // This is working 
        {
            searchTerms = searchTerms.ToLower();
            if (isDebug)
            {
                return DummyData.GetProdutcs().Where(xProduct => xProduct.name.ToLower().Contains(searchTerms)).ToList();
            }
            else
            {
                string serverResponse = await MiddlewareHTTPClient.SendQuery("searchProducts.jsp?TERM=" + searchTerms);
                return Newtonsoft.Json.JsonConvert.DeserializeObject<List<Product>>(serverResponse);
            }
        }

        public async static Task<bool> UploadChanges(Product product, int newQuantity) // This works but isn't working on the server correctly (it just calls add new again)
        {
            product.stockCount = newQuantity;
            if (isDebug)
            {
                return true;
            }
            else
            {
                try
                {
                    string serverResponse = await MiddlewareHTTPClient.SendQuery("addProduct.jsp?PRODUCT=" + Newtonsoft.Json.JsonConvert.SerializeObject(product) + "&NEW=FALSE");
                    return serverResponse.StartsWith("SUCCESS");
                }
                catch (Exception)
                {
                    return false;
                }
            }
        }
    }
}
