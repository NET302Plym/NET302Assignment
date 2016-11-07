using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;
using DWSS.Data;
using Newtonsoft.Json;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace DWSS.Pages
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class OptionsPage : Page
    {
        public OptionsPage()
        {
            this.InitializeComponent();

            //Order:
            //{ "ID":1,"quantity":100,"dateDelivered":"20/10/2016","custName":"dan","custContact":"88","location":"southLoc","staffFulfilled":"","product":{ "ID":1,"stockCount":5,"category":"stationary","subCategory":"subStationary","name":"pen","container":"5?","unitPrice":5.0,"available":true},"timeDelivered":"01:00:00 AM","fulfilled":false}
            //Product:
            //{ "ID":1,"stockCount":5,"category":"stationary","subCategory":"subStationary","name":"pen","container":"5?","unitPrice":5.0,"available":true}
            //Order order = new Order(1, 100, "20/10/2016", "dan", "88", "southLoc", "", // new Product(1, 5, "stationary", "subStationary", "pen", "5?", 5.0, true), DateTime, false);
            //Order order = Newtonsoft.Json.JsonConvert.DeserializeObject<Order>()
            //string jsonProduct = "{\"ID\":1,\"stockCount\":5,\"category\":\"stationary\",\"subCategory\":\"subStationary\",\"name\":\"pen\",\"container\":\"5?\",\"unitPrice\":5.0,\"available\":true}";
            //Product product = JsonConvert.DeserializeObject<Product>(jsonProduct);
            //string jsonOrder = "{\"ID\":0,\"quantity\":5,\"customer\":{\"ID\":0,\"username\":\"Dan\",\"password\":\"Password\"},\"product\":{\"ID\":1,\"stockCount\":1,\"category\":\"Cat\",\"subCategory\":\"Cat2\",\"name\":\"Name\",\"container\":\"Container\",\"unitPrice\":5.0,\"available\":true}}";
            //Order order = JsonConvert.DeserializeObject<Order>(jsonOrder);
            this.UsernameTextBlock.Text = StaticData.currentUser.username;
        }

        /// <summary>
        /// Invoked when this page is about to be displayed in a Frame.
        /// </summary>
        /// <param name="e">Event data that describes how this page was reached.
        /// This parameter is typically used to configure the page.</param>
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
        }

        private void FulfillOrdersButtonClick(object sender, RoutedEventArgs e)
        {
            (Window.Current.Content as Frame).Navigate(typeof(Pages.OrderFulfilmentPage));
        }

        private void LogoutButtonClick(object sender, RoutedEventArgs e)
        {
            StaticData.currentUser = null;
            (Window.Current.Content as Frame).Navigate(typeof(Pages.LoginScreen));
        }

        private void AdjustStockButtonClick(object sender, RoutedEventArgs e)
        {
            (Window.Current.Content as Frame).Navigate(typeof(Pages.AdjustStockPage));
        }

        private void MoveStockLocationButtonClick(object sender, RoutedEventArgs e)
        {
            (Window.Current.Content as Frame).Navigate(typeof(Pages.AdjustStockPage));
        }
    }
}
