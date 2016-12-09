using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace DWSS.Middleware
{
    public class MiddlewareHTTPClient
    {
        private const string serverAddress = "http://localhost:8080/NET302_REST/"; // TODO: Specify server. 
        private static HttpClient httpClient;

        public static async Task<string> SendQuery(string queryURL, bool expectResponse = true)
        {
            httpClient = new HttpClient();
            var responseMessage = await httpClient.GetAsync(serverAddress + queryURL);
            httpClient.Dispose();
            if (responseMessage.StatusCode != System.Net.HttpStatusCode.OK)
            {
                throw new Exception("Connection Failure");
            }
            if (expectResponse)
                return StripResponseString(await responseMessage.Content.ReadAsStringAsync());
            return null;
        }

        private static string StripResponseString(string input)
        {
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
            else return string.Empty;
        }
    }
}
