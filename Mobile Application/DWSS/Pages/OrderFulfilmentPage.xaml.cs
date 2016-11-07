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
using DWSS.UserControls;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace DWSS.Pages
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class OrderFulfilmentPage : Page
    {
        public OrderFulfilmentPage()
        {
            this.InitializeComponent();
        }

        /// <summary>
        /// Invoked when this page is about to be displayed in a Frame.
        /// </summary>
        /// <param name="e">Event data that describes how this page was reached.
        /// This parameter is typically used to configure the page.</param>
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            StaticData.OrderFulfilmentPage = this; // Subscribe to the static data page
            this.UsernameTextBlock.Text = StaticData.currentUser.username; // Push the username out to the screen
            PageContentStackPanel.Children.Clear();
            foreach (var order in Middleware.MiddlewareConnections.GetOutstandingOrders())
                PageContentStackPanel.Children.Add(new OrderUserControl(order));
        }

        public void FulfilOrder(Order orderToFulfill)
        {
            Middleware.MiddlewareConnections.FulfillOrder(orderToFulfill, StaticData.currentUser);
            // Reload the UI
            OnNavigatedTo(null);
        }

        private void HomeButtonClick(object sender, RoutedEventArgs e)
        {
            (Window.Current.Content as Frame).Navigate(typeof(Pages.OptionsPage));
        }
    }
}
