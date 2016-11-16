using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;
using DWSS.Middleware;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace DWSS.Pages
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class LoginScreen : Page
    {
        public LoginScreen()
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

        private void BypassLoginClick(object sender, RoutedEventArgs e)
        {
            StaticData.currentUser = MiddlewareConnections.GetUser("Scanned User Badge");
            (Window.Current.Content as Frame).Navigate(typeof (MasterPage));
        }
    }
}
