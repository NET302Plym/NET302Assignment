using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DWSS.Data;

namespace DWSS.Development
{
    class DummyData
    {
        public static List<Order> GetOrders()
        {
            return new List<Order>()
            {
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[0], DateTime.MinValue, false, 1),
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[1], DateTime.MinValue, false, 1),
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[2], DateTime.MinValue, false, 1),
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[3], DateTime.MinValue, false, 1),
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[4], DateTime.MinValue, false, 1),
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[5], DateTime.MinValue, false, 1),
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[6], DateTime.MinValue, false, 1),
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[7], DateTime.MinValue, false, 1),
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[8], DateTime.MinValue, false, 1),
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[9], DateTime.MinValue, false, 1),
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[10], DateTime.MinValue, false, 1),
                new Order(1, "15/10/1992", "Dan", "6", "Lower Shelf", "", GetProdutcs()[11], DateTime.MinValue, false, 1),
            };
        }

        public static List<Product> GetProdutcs()
        {
            return new List<Product>()
            {
                new Product(1, 500, "Stationary", "St", "Blue Pen", "1", 5, true),
                new Product(2, 500, "Stationary", "St", "Red Pen", "1", 5, true),
                new Product(3, 500, "Stationary", "St", "Black Pen", "1", 5, true),
                new Product(4, 500, "Stationary", "St", "Yellow Pen", "1", 5, true),
                new Product(5, 500, "Stationary", "St", "Purple Pen", "1", 5, true),
                new Product(6, 500, "Stationary", "St", "Multicoloured Pen", "1", 5, true),
                new Product(7, 500, "Stationary", "St", "Turquise Pen", "1", 5, true),
                new Product(1, 500, "Components", "HDD", "500GB", "1", 5, true),
                new Product(1, 500, "Components", "HDD", "1000GB", "1", 5, true),
                new Product(1, 500, "Components", "HDD", "2000GB", "1", 5, true),
                new Product(1, 500, "Components", "HDD", "4000GB", "1", 5, true),
                new Product(1, 500, "Components", "HDD", "8000GB", "1", 5, true)
            };
        } 
    }
}
