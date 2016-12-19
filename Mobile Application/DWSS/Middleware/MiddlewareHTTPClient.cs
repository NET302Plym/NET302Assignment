<<<<<<< HEAD
﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace DWSS.Middleware
{
    /// <summary>
    /// Provides access to the REST API for the MiddlewareConnections
    /// </summary>
    public class MiddlewareHTTPClient
    {
        /// <summary>
        /// This is the location of the REST server
        /// </summary>
        private const string serverAddress = "http://localhost:8080/NET302_REST/"; // TODO: Specify server. 
        /// <summary>
        /// A local HttpClient to handle REST connections 
        /// </summary>
        private static HttpClient httpClient;

        /// <summary>
        /// Used to form a POST query construction 
        /// </summary>
        /// <param name="queryURL">The URL to post the query to</param>
        /// <param name="expectResponse">Will only post-process the results if they are required</param>
        /// <returns></returns>
        public static async Task<string> SendQuery(string queryURL, List<KeyValuePair<string, string>> values, bool expectResponse = true)
        {
            //// Encrypt the data to send
            //List<KeyValuePair<string, string>> newValues = new List<KeyValuePair<string, string>>();
            //for (int i = 0; i < values.Count(); i++)
            //    newValues.Add(new KeyValuePair<string, string>(values[i].Key, Encryption.Encrypter.Encrypt(values[i].Value)));

            //httpClient = new HttpClient();
            //httpClient.DefaultRequestHeaders.IfModifiedSince = DateTime.Now;
            //// var responseMessage = await httpClient.GetAsync(serverAddress + queryURL);

            //var content = new FormUrlEncodedContent(newValues);
            var content = new FormUrlEncodedContent(values);

            var responseMessage = await httpClient.PostAsync(serverAddress + queryURL, content);
            httpClient.Dispose();
            if (responseMessage.StatusCode != System.Net.HttpStatusCode.OK)
            {
                throw new Exception("Connection Failure");
            }
            if (expectResponse)
                return StripResponseString(await responseMessage.Content.ReadAsStringAsync());
            return null;
        }
        /// <summary>
        /// Some C#-Java/PHP seems to add a few characters either side of the string returned and so this removes those extra characters. 
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        private static string StripResponseString(string input)
        {
            // A string returned will either be wrapped in '['...']' or '{'...'}' - Accomodate for both of these cases. 
            if (input.Contains("[") && input.Contains("]"))
            {
                int firstCounter = 0;
                while (input[firstCounter] != '[')
                    firstCounter++;
                int secondCounter = input.Length - 1;
                while (input[secondCounter] != ']')
                    secondCounter--;
                return input.Substring(firstCounter, input.Length - firstCounter - (input.Length - secondCounter) + 1);
            }
            else if (input.Contains("{") && input.Contains("}"))
            {
                int firstCounter = 0;
                while (input[firstCounter] != '{')
                    firstCounter++;
                int secondCounter = input.Length - 1;
                while (input[secondCounter] != '}')
                    secondCounter--;
                return input.Substring(firstCounter, input.Length - firstCounter - (input.Length - secondCounter) + 1);
            }
            else return input; // The returned string is not a JSON string, so return the input string. This will usually be a success or error string.  
        }
    }
}
=======
﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace DWSS.Middleware
{
    /// <summary>
    /// Provides access to the REST API for the MiddlewareConnections
    /// </summary>
    public class MiddlewareHTTPClient
    {
        /// <summary>
        /// This is the location of the REST server
        /// </summary>
        private const string serverAddress = "http://localhost:8080/NET302_REST/"; // TODO: Specify server. 
        /// <summary>
        /// A local HttpClient to handle REST connections 
        /// </summary>
        private static HttpClient httpClient;

        /// <summary>
        /// Used to form a POST query construction 
        /// </summary>
        /// <param name="queryURL">The URL to post the query to</param>
        /// <param name="expectResponse">Will only post-process the results if they are required</param>
        /// <returns></returns>
        public static async Task<string> SendQuery(string queryURL, List<KeyValuePair<string, string>> values, bool expectResponse = true)
        {
            //// Encrypt the data to send
            //List<KeyValuePair<string, string>> newValues = new List<KeyValuePair<string, string>>();
            //for (int i = 0; i < values.Count(); i++)
            //    newValues.Add(new KeyValuePair<string, string>(values[i].Key, Encryption.Encrypter.Encrypt(values[i].Value)));

            //httpClient = new HttpClient();
            //httpClient.DefaultRequestHeaders.IfModifiedSince = DateTime.Now;
            //// var responseMessage = await httpClient.GetAsync(serverAddress + queryURL);

            //var content = new FormUrlEncodedContent(newValues);
            var content = new FormUrlEncodedContent(values);

            var responseMessage = await httpClient.PostAsync(serverAddress + queryURL, content);
            httpClient.Dispose();
            if (responseMessage.StatusCode != System.Net.HttpStatusCode.OK)
            {
                throw new Exception("Connection Failure");
            }
            if (expectResponse)
                return StripResponseString(await responseMessage.Content.ReadAsStringAsync());
            return null;
        }
        /// <summary>
        /// Some C#-Java/PHP seems to add a few characters either side of the string returned and so this removes those extra characters. 
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        private static string StripResponseString(string input)
        {
            // A string returned will either be wrapped in '['...']' or '{'...'}' - Accomodate for both of these cases. 
            if (input.Contains("[") && input.Contains("]"))
            {
                int firstCounter = 0;
                while (input[firstCounter] != '[')
                    firstCounter++;
                int secondCounter = input.Length - 1;
                while (input[secondCounter] != ']')
                    secondCounter--;
                return input.Substring(firstCounter, input.Length - firstCounter - (input.Length - secondCounter) + 1);
            }
            else if (input.Contains("{") && input.Contains("}"))
            {
                int firstCounter = 0;
                while (input[firstCounter] != '{')
                    firstCounter++;
                int secondCounter = input.Length - 1;
                while (input[secondCounter] != '}')
                    secondCounter--;
                return input.Substring(firstCounter, input.Length - firstCounter - (input.Length - secondCounter) + 1);
            }
            else return input; // The returned string is not a JSON string, so return the input string. This will usually be a success or error string.  
        }
    }
}
>>>>>>> b3e01a43bf7f7639b946fd47057a843a6e4185a6
