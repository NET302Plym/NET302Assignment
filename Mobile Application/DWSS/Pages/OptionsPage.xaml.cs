using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;

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
            StaticData.masterPage.Navigate(typeof(Pages.OrderFulfilmentPage));
        }

        private void LogoutButtonClick(object sender, RoutedEventArgs e)
        {
            StaticData.currentUser = null;
            (Window.Current.Content as Frame).Navigate(typeof(Pages.LoginScreen));
        }

        private void AdjustStockButtonClick(object sender, RoutedEventArgs e)
        {
            StaticData.masterPage.Navigate(typeof(Pages.AdjustStockPage));
        }
    }
}
