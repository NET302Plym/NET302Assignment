using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NET302_DataImporter
{
    class Program
    {
        const int rowId = 0,
            orderId = 1,
            orderDate = 2, 
            orderPriority = 3,
            orderQuantity = 4,
            sales = 5,
            discount = 6,
            shipMode = 7,
            profit = 8,
            unitPrice = 9,
            shippingCost = 10,
            customerName = 11,
            province = 12,
            region = 13,
            customerSegment = 14,
            productCategory = 15,
            productSubCategory = 16,
            productName = 17,
            productContainer = 18,
            productBaseMargin = 19,
            shipDate = 20;
        static void Main(string[] args)
        {
            //// tmp Users csv management 
            //string loc = @"C:\Users\Dan\Desktop\Users.csv";
            //List<string[]> uFileContents = new List<string[]>();
            //using (StreamReader sr = new StreamReader(loc))
            //    uFileContents.AddRange(sr.ReadToEnd().Split('\n').Select(s => s.Split(',')));
            //for(int i = 1; i < uFileContents.Count; i++)
            //{
            //    //uFileContents[i][3] = uFileContents[i][1].Split(' ').Aggregate((workingString, newVar) => workingString + "." + newVar).ToLower();
            //    uFileContents[i][5] = "1";
            //}
            //using (StreamWriter sw = new StreamWriter(loc, false))
            //    foreach (var s in uFileContents)
            //        sw.WriteLine(s.Aggregate((workingString, newVar) => workingString + "," + newVar));

            //return;

            string filePath = @"C:\Users\Dan\Desktop\Sample - Superstore Sales (Excel).csv";
            List<string[]> fileContents = new List<string[]>();
            using (StreamReader sr = new StreamReader(filePath))
                fileContents.AddRange(sr.ReadToEnd().Split('\n').Select(s => s.Split(',')));
            fileContents.RemoveAll(xMatch => xMatch.Length != 21);
            List<string> tmpList;
            // Product Category List
            tmpList = fileContents.Select(content => content[productCategory]).ToList();
            tmpList = tmpList.Distinct().ToList();
            List<string[]> productCategoryList = new List<string[]>();
            int counter = 0;
            foreach (string s in tmpList)
            {
                productCategoryList.Add(new []{ counter.ToString(), s });
                counter++;
            }

            // Product Sub Category List
            tmpList = fileContents.Select(content => content[productSubCategory]).ToList();
            tmpList = tmpList.Distinct().ToList();
            List<string[]> productSubCategoryList = new List<string[]>();
            counter = 0;
            foreach (string s in tmpList)
            {
                productSubCategoryList.Add(new[] { counter.ToString(), s });
                counter++;
            }

            // Product Container List
            tmpList = fileContents.Select(content => content[productContainer]).ToList();
            tmpList = tmpList.Distinct().ToList();
            List<string[]> productContainerList = new List<string[]>();
            counter = 0;
            foreach (string s in tmpList)
            {
                productContainerList.Add(new[] { counter.ToString(), s });
                counter++;
            }

            // Products
            List<string[]> productList = new List<string[]>();
            counter = 0;
            foreach (string[] record in fileContents)
            {
                var res = productList.Find(xRow => xRow[1] == record[productName]);
                if (res != null)
                {
                    res[2] = (Convert.ToInt32(res[2]) + Convert.ToInt32(record[orderQuantity])).ToString();
                }
                else
                {
                    var rand = Random().ToString();
                    try
                    {
                        string[] pCMatch = productCategoryList.Find(xTerm => xTerm[1] == record[productCategory]);
                        string[] pSCMatch = productSubCategoryList.Find(xTerm => xTerm[1] == record[productSubCategory]);
                        string[] pContainerMatch = productContainerList.Find(xTerm => xTerm[1] == record[productContainer]);
                        productList.Add(new string[]
                        {
                            counter.ToString(),
                            record[productName],
                            rand,
                            record[unitPrice],
                            rand == "0" ? "NO" : "YES",
                            pCMatch[0],
                            pSCMatch[0],
                            pContainerMatch[0]
                        });
                        counter++;
                    } catch(Exception ex) { }
                }
            }
            //foreach (var prod in productList)
            //{
            //    foreach(string s in prod)
            //        Console.Write(s + ",");
            //    Console.WriteLine();
            //}

            // Dump the files into CSV files
            using (StreamWriter sw = new StreamWriter("Product Categories.csv", false))
                foreach (string[] s in productCategoryList)
                    sw.WriteLine(s.Aggregate((workingSentance, next) => workingSentance + "," + next));
            using (StreamWriter sw = new StreamWriter("Product Sub Categories.csv", false))
                foreach (string[] s in productSubCategoryList)
                    sw.WriteLine(s.Aggregate((workingSentance, next) => workingSentance + "," + next));
            using (StreamWriter sw = new StreamWriter("Product Containers.csv", false))
                foreach (string[] s in productContainerList)
                    sw.WriteLine(s.Aggregate((workingSentance, next) => workingSentance + "," + next));
            using (StreamWriter sw = new StreamWriter("Products.csv", false))
                foreach (string[] s in productList)
                    sw.WriteLine(s.Aggregate((workingSentance, next) => workingSentance + "," + next));
            Console.WriteLine("Finished Conversion");
            Console.ReadLine();
        }
        static Random random = new Random();
        static int Random()
        {
            return random.Next(0, 10000);
        }
    }
}
