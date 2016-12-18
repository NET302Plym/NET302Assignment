using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace DWSS.Pages
{
    /// <summary>
    /// This is the page simply listing options to the user and redirects to certain pages
    /// </summary>
    public sealed partial class OptionsPage : Page
    {
        /// <summary>
        /// Default constructor
        /// </summary>
        public OptionsPage()
        {
            this.InitializeComponent();
        }

        ///// <summary>
        ///// Invoked when this page is about to be displayed in a Frame.
        ///// </summary>
        ///// <param name="e">Event data that describes how this page was reached.
        ///// This parameter is typically used to configure the page.</param>
        //protected override void OnNavigatedTo(NavigationEventArgs e)
        //{
        //}

        /// <summary>
        /// Navigates the user to the fulfull orders page
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void FulfillOrdersButtonClick(object sender, RoutedEventArgs e)
        {
            StaticData.masterPage.Navigate(typeof(Pages.OrderFulfilmentPage));
        }

        /// <summary>
        /// Navigates the user to the login page
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void LogoutButtonClick(object sender, RoutedEventArgs e)
        {
            StaticData.currentUser = null;
            (Window.Current.Content as Frame).Navigate(typeof(Pages.LoginScreen));
        }

        /// <summary>
        /// Navigates the user to the stock adjustment page 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void AdjustStockButtonClick(object sender, RoutedEventArgs e)
        {
            StaticData.masterPage.Navigate(typeof(Pages.AdjustStockPage));
        }
    }
}
