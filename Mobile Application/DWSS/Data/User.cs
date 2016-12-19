using Newtonsoft.Json;

namespace DWSS.Data
{
    /// <summary>
    /// Used to represent a user, usually a member of staff
    /// </summary>
    public class User
    {
        /// <summary>
        /// ID for the user
        /// </summary>
        public int ID { set; get; }
        /// <summary>
        /// Username of the user
        /// </summary>
        public string username { set; get; }
        /// <summary>
        /// Password of the user. This will be removed after login 
        /// </summary>
        public string password { set; get; }
        /// <summary>
        /// The contact for the user
        /// </summary>
        public string contact { set; get; }
        /// <summary>
        /// The name of the user
        /// </summary>
        public string name { set; get; }
        /// <summary>
        /// Whether or not the user has bee authenticated 
        /// </summary>
        public bool authenticated { set; get; }
        /// <summary>
        /// The type of staff member (can be warehouse, managemenent etc) 
        /// </summary>
        public GenericLookup staffType { set; get; }
        /// <summary>
        /// Blank JSON constructor 
        /// </summary>
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
