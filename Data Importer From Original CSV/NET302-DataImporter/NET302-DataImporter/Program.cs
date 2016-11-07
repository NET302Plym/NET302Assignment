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
        static void Main(string[] args)
        {
            string filePath = @"D:\My Files\Documents\University\University Year 3\NET302 - Distributed Systems\Sample - Superstore Sales (Excel).csv";
            List<string[]> fileContents = new List<string[]>();
            using (StreamReader sr = new StreamReader(filePath))
                fileContents.AddRange(sr.ReadToEnd().Split('\n').Select(s => s.Split(',')));

        }
    }
}
