using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
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
            PageContentStackPanel.Children.Clear();
            foreach (var order in Middleware.MiddlewareConnections.GetOutstandingOrders())
                PageContentStackPanel.Children.Add(new OrderUserControl(order));
        }

        public void FulfilOrder(Order orderToFulfill)
        {
            Middleware.MiddlewareConnections.FulfillOrder(orderToFulfill, StaticData.currentUser);
            // Reload the UI
            OnNavigatedTo(null);
            // Show a notification
            StaticData.masterPage.ShowNotification("Order " + orderToFulfill.ID + " has been fulfilled");
        }

        private void HomeButtonClick(object sender, RoutedEventArgs e)
        {
            StaticData.masterPage.Navigate(typeof(Pages.OptionsPage));
        }
    }
}
