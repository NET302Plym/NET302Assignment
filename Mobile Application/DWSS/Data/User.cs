using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DWSS.Data
{
    public class User
    {
        public int ID;
        public string username;
        public string password;
        public string custContact;
        public bool authenticated;

        public User() { }

        public User(string username, string password)
        {
            this.username = username;
            this.password = password;
        }

        public User(string jsonString)
        {
            User user = Newtonsoft.Json.JsonConvert.DeserializeObject<User>(jsonString);
            this.ID = user.ID;
            this.username = user.username;
            this.password = user.password;
            this.custContact = user.custContact;
            this.authenticated = user.authenticated;
        }
    }
}
