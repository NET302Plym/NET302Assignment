using Newtonsoft.Json;

namespace DWSS.Data
{
    /// <summary>
    /// Used to represent database id - value data pairs 
    /// </summary>
    public class GenericLookup
    {
        /// <summary>
        /// ID 
        /// </summary>
        public int ID { set; get; }
        /// <summary>
        /// Value 
        /// </summary>
        public string value { set; get; }
        /// <summary>
        /// Blank JSON constructor
        /// </summary>
        [JsonConstructor]
        public GenericLookup() { }
        /// <summary>
        /// ID & value constructor 
        /// </summary>
        /// <param name="id"></param>
        /// <param name="value"></param>
        public GenericLookup(int id, string value)
        {
            this.ID = id;
            this.value = value;
        }
    }
}
