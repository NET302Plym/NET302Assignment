<<<<<<< HEAD
﻿using System.Collections.Generic;
using System.Linq;
using DWSS.Data;
using DWSS.Development;
using System.Threading.Tasks;
using System;
using Google.Apis.Customsearch.v1;
using Google.Apis.Customsearch.v1.Data;

namespace DWSS.Middleware
{
    /// <summary>
    /// Provides static accessors to the REST API service
    /// </summary>
    class MiddlewareConnections
    {
        /// <summary>
        /// Set to :
        ///     true to use dummy data
        /// or
        ///     false to use the REST API
        /// </summary>
        private static bool isDebug = false;
        /// <summary>
        /// Returns a list of outstanding orders (unfulfilled orders)
        /// </summary>
        /// <returns></returns>
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
                    string serverResponse = await MiddlewareHTTPClient.SendQuery("getUnfulfilled.jsp", new List<KeyValuePair<string, string>>());
                    orderList = serverResponse == null ? new List<Order>() : Newtonsoft.Json.JsonConvert.DeserializeObject<List<Order>>(serverResponse);
                } catch (Exception)
                {
                    throw new Exception("Error Communicating With The Server");
                }
                return orderList;
            }
        }
        /// <summary>
        /// Fulfills an order
        /// </summary>
        /// <param name="orderToFulfil">The order to fulfill</param>
        /// <returns></returns>
        public async static Task<bool> FulfillOrder(Order orderToFulfil)
        {
            if (isDebug)
            {
                return true;
            }
            else
            {
                try
                {
                    var listToSend = new List<KeyValuePair<string, string>>() {
                        new KeyValuePair<string, string>("ORDER",orderToFulfil.ID.ToString())
                    };
                    string serverResponse = await MiddlewareHTTPClient.SendQuery("fulfillOrder.jsp", listToSend);
                    return serverResponse.Contains("SUCCESS");
                } catch (Exception ex)
                {
                    return false;
                }
            }
        }
        /// <summary>
        /// Returns a User based on their username. This won't return any of their passwords or personal information
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        public async static Task<User> GetUser(string username) 
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
                var listToSend = new List<KeyValuePair<string, string>>()
                {
                    new KeyValuePair<string, string>("ID", username)
                };
                string serverResponse = await MiddlewareHTTPClient.SendQuery("getUser.jsp", listToSend);
                return Newtonsoft.Json.JsonConvert.DeserializeObject<User>(serverResponse);
            }
        }
        /// <summary>
        /// This searches the database for a particular item, based on their search terms
        /// </summary>
        /// <param name="searchTerms"></param>
        /// <returns></returns>
        public async static Task<List<Product>> SearchForProduct(string searchTerms) 
        {
            searchTerms = searchTerms.ToLower();
            if (isDebug)
            {
                return DummyData.GetProdutcs().Where(xProduct => xProduct.name.ToLower().Contains(searchTerms)).ToList();
            }
            else
            {
                var listToSend = new List<KeyValuePair<string, string>>()
                {
                    new KeyValuePair<string, string>("TERM", searchTerms)
                };
                string serverResponse = await MiddlewareHTTPClient.SendQuery("searchProducts.jsp", listToSend);
                return Newtonsoft.Json.JsonConvert.DeserializeObject<List<Product>>(serverResponse);
            }
        }
        /// <summary>
        /// This uploads a new quantity to the database
        /// </summary>
        /// <param name="product">The product to change</param>
        /// <param name="newQuantity">The new quantity</param>
        /// <returns></returns>
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
                    var listToSend = new List<KeyValuePair<string, string>>()
                    {
                        new KeyValuePair<string, string>("PRODUCT", product.ID.ToString()),
                        new KeyValuePair<string, string>("NEWQUANTITY", newQuantity.ToString())
                    };
                    string serverResponse = await MiddlewareHTTPClient.SendQuery("changeProductQuantity.jsp", listToSend);
                    return serverResponse.Contains("SUCCESS");
                }
                catch (Exception)
                {
                    return false;
                }
            }
        }
        /// <summary>
        /// This returns a URL for an image. 
        /// This uses Google and will return the first image result from a search term. 
        /// </summary>
        /// <param name="searchTerms"></param>
        /// <returns></returns>
        public static string DownloadImage(string searchTerms)
        {
            string apiKey = "AIzaSyAD5OsjTu6d-8xwOEkewvgA0JtNecMfoNo";
            string searchEngineId = "008801159646905401147:nqod7kqw_kc";
            CustomsearchService customSearchService = new CustomsearchService(new Google.Apis.Services.BaseClientService.Initializer { ApiKey = apiKey });
            Google.Apis.Customsearch.v1.CseResource.ListRequest listRequest = customSearchService.Cse.List(searchTerms);
            listRequest.Cx = searchEngineId;
            listRequest.SearchType = CseResource.ListRequest.SearchTypeEnum.Image;
            listRequest.Num = 1;
            listRequest.Start = 1;
            Search search = listRequest.Execute();
            if (search.Items.Count == 0) return string.Empty;
            return search.Items[0].Link;
        }
    }
}
=======
﻿using System.Collections.Generic;
using System.Linq;
using DWSS.Data;
using DWSS.Development;
using System.Threading.Tasks;
using System;
using Google.Apis.Customsearch.v1;
using Google.Apis.Customsearch.v1.Data;

namespace DWSS.Middleware
{
    /// <summary>
    /// Provides static accessors to the REST API service
    /// </summary>
    class MiddlewareConnections
    {
        /// <summary>
        /// Set to :
        ///     true to use dummy data
        /// or
        ///     false to use the REST API
        /// </summary>
        private static bool isDebug = false;
        /// <summary>
        /// Returns a list of outstanding orders (unfulfilled orders)
        /// </summary>
        /// <returns></returns>
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
                    string serverResponse = await MiddlewareHTTPClient.SendQuery("getUnfulfilled.jsp", new List<KeyValuePair<string, string>>());
                    orderList = serverResponse == null ? new List<Order>() : Newtonsoft.Json.JsonConvert.DeserializeObject<List<Order>>(serverResponse);
                } catch (Exception)
                {
                    throw new Exception("Error Communicating With The Server");
                }
                return orderList;
            }
        }
        /// <summary>
        /// Fulfills an order
        /// </summary>
        /// <param name="orderToFulfil">The order to fulfill</param>
        /// <returns></returns>
        public async static Task<bool> FulfillOrder(Order orderToFulfil)
        {
            if (isDebug)
            {
                return true;
            }
            else
            {
                try
                {
                    var listToSend = new List<KeyValuePair<string, string>>() {
                        new KeyValuePair<string, string>("ORDER",orderToFulfil.ID.ToString())
                    };
                    string serverResponse = await MiddlewareHTTPClient.SendQuery("fulfillOrder.jsp", listToSend);
                    return serverResponse.Contains("SUCCESS");
                } catch (Exception ex)
                {
                    return false;
                }
            }
        }
        /// <summary>
        /// Returns a User based on their username. This won't return any of their passwords or personal information
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        public async static Task<User> GetUser(string username) 
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
                var listToSend = new List<KeyValuePair<string, string>>()
                {
                    new KeyValuePair<string, string>("ID", username)
                };
                string serverResponse = await MiddlewareHTTPClient.SendQuery("getUser.jsp", listToSend);
                return Newtonsoft.Json.JsonConvert.DeserializeObject<User>(serverResponse);
            }
        }
        /// <summary>
        /// This searches the database for a particular item, based on their search terms
        /// </summary>
        /// <param name="searchTerms"></param>
        /// <returns></returns>
        public async static Task<List<Product>> SearchForProduct(string searchTerms) 
        {
            searchTerms = searchTerms.ToLower();
            if (isDebug)
            {
                return DummyData.GetProdutcs().Where(xProduct => xProduct.name.ToLower().Contains(searchTerms)).ToList();
            }
            else
            {
                var listToSend = new List<KeyValuePair<string, string>>()
                {
                    new KeyValuePair<string, string>("TERM", searchTerms)
                };
                string serverResponse = await MiddlewareHTTPClient.SendQuery("searchProducts.jsp", listToSend);
                return Newtonsoft.Json.JsonConvert.DeserializeObject<List<Product>>(serverResponse);
            }
        }
        /// <summary>
        /// This uploads a new quantity to the database
        /// </summary>
        /// <param name="product">The product to change</param>
        /// <param name="newQuantity">The new quantity</param>
        /// <returns></returns>
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
                    var listToSend = new List<KeyValuePair<string, string>>()
                    {
                        new KeyValuePair<string, string>("PRODUCT", product.ID.ToString()),
                        new KeyValuePair<string, string>("NEWQUANTITY", newQuantity.ToString())
                    };
                    string serverResponse = await MiddlewareHTTPClient.SendQuery("changeProductQuantity.jsp", listToSend);
                    return serverResponse.Contains("SUCCESS");
                }
                catch (Exception)
                {
                    return false;
                }
            }
        }
        /// <summary>
        /// This returns a URL for an image. 
        /// This uses Google and will return the first image result from a search term. 
        /// </summary>
        /// <param name="searchTerms"></param>
        /// <returns></returns>
        public static string DownloadImage(string searchTerms)
        {
            string apiKey = "AIzaSyAD5OsjTu6d-8xwOEkewvgA0JtNecMfoNo";
            string searchEngineId = "008801159646905401147:nqod7kqw_kc";
            CustomsearchService customSearchService = new CustomsearchService(new Google.Apis.Services.BaseClientService.Initializer { ApiKey = apiKey });
            Google.Apis.Customsearch.v1.CseResource.ListRequest listRequest = customSearchService.Cse.List(searchTerms);
            listRequest.Cx = searchEngineId;
            listRequest.SearchType = CseResource.ListRequest.SearchTypeEnum.Image;
            listRequest.Num = 1;
            listRequest.Start = 1;
            Search search = listRequest.Execute();
            if (search.Items.Count == 0) return string.Empty;
            return search.Items[0].Link;
        }
    }
}
>>>>>>> b3e01a43bf7f7639b946fd47057a843a6e4185a6
