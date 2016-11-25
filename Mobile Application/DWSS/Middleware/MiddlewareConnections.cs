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

        public async static Task<List<Order>> GetOutstandingOrders()
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
                    string serverResponse = await MiddlewareHTTPClient.SendQuery("getOrder.jsp");
                    orderList = Newtonsoft.Json.JsonConvert.DeserializeObject<List<Order>>(serverResponse);
                } catch (Exception)
                {
                    throw new Exception("Error Communicating With The Server");
                }
                return orderList;
            }
        }

        public async static Task<bool> FulfillOrder(Order orderToFulfil, User currentUser)
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

        public async static Task<User> GetUser(string username)
        {
            if (true) // TODO: Remove
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
                return null; // TODO Be implemented within the middleware
            }
        }

        public async static Task<List<Product>> SearchForProduct(string searchTerms)
        {
            searchTerms = searchTerms.ToLower();
            if (isDebug)
            {
                return DummyData.GetProdutcs().Where(xProduct => xProduct.name.ToLower().Contains(searchTerms)).ToList();
            }
            else
            {
                // TODO this.
                return null;
            }
        }

        public async static Task<bool> UploadChanges(Product product, int newQuantity)
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
                return true;
            }
        }
    }
}
