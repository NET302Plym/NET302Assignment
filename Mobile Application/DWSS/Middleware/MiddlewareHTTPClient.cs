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
        private const string serverAddress = "LOCATIONOFSEVER"; // TODO: Specify server. 
        private static HttpClient httpClient;
        
        public static async Task<string> SendQuery(string queryURL, bool expectResponse = true)
        {
            httpClient = new HttpClient();
            var responseMessage = await httpClient.GetAsync(queryURL);
            httpClient.Dispose();
            if (responseMessage.StatusCode != System.Net.HttpStatusCode.OK) {
                throw new Exception("Connection Failure");
            }
            if (expectResponse)
                return await responseMessage.Content.ReadAsStringAsync();
            return null;
        }
    }
}
