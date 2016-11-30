using Newtonsoft.Json;

namespace DWSS.Data
{
    public class User
    {
        public int ID { set; get; }
        public string username { set; get; }
        public string password { set; get; }
        public string contact { set; get; }
        public string name { set; get; }
        public bool authenticated { set; get; }
        public GenericLookup staffType { set; get; }
        [JsonConstructor]
        public User() { }

        //public User(string username, string password)
        //{
        //    this.username = username;
        //    this.password = password;
        //}

        //public User(string jsonString)
        //{
        //    User user = Newtonsoft.Json.JsonConvert.DeserializeObject<User>(jsonString);
        //    this.ID = user.ID;
        //    this.username = user.username;
        //    this.password = user.password;
        //    this.custContact = user.custContact;
        //    this.authenticated = user.authenticated;
        //}
    }
}
