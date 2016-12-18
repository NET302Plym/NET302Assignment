using DWSS.Data;
using DWSS.Pages;

namespace DWSS
{
    /// <summary>
    /// Used to store a set of runtime session variables 
    /// </summary>
    class StaticData
    {
        /// <summary>
        /// The currently logged in user
        /// </summary>
        public static User currentUser;
        /// <summary>
        /// The current order fulfillment page 
        /// </summary>
        public static OrderFulfilmentPage OrderFulfilmentPage;
        /// <summary>
        /// The current adjust product page 
        /// </summary>
        public static AdjustProductPage adjustProductPage;
        /// <summary>
        /// The current master page 
        /// </summary>
        public static MasterPage masterPage;
    }
}
