using Newtonsoft.Json;

namespace DWSS.Data
{
    public class GenericLookup
    {
        public int ID { set; get; }
        public string value { set; get; }
        [JsonConstructor]
        public GenericLookup() { }
        public GenericLookup(int id, string value)
        {
            this.ID = id;
            this.value = value;
        }
    }
}
