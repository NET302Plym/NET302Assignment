using System.Collections.Generic;
using System.Linq;
using DWSS.Data;
using DWSS.Development;

namespace DWSS.Middleware
{
    class MiddlewareConnections
    {
        private static bool isDebug = true;
        public static List<Order> GetOutstandingOrders()
        {
            if (isDebug)
            {
                // Return dummy data
                return DummyData.GetOrders();
            }
            else
            {
                // Connect to the middleware and handle those
                return null;
            }
        }

        public static bool FulfillOrder(Order orderToFulfil, User currentUser)
        {
            // Merge the two together

            if (isDebug)
            {
                return true;
            }
            else
            {
                // Connect to the middleware and upload the new information 
                return false;
                // TODO this
            }
        }

        public static User GetUser(string username)
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
                return null;
            }
        }

        public static List<Product> SearchForProduct(string searchTerms)
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

        public static bool UploadChanges(Product product, int newQuantity)
        {
            if (isDebug)
            {
                return true;
            }
            else
            {
                // TODO;
                return true;
            }
        }
    }
}
