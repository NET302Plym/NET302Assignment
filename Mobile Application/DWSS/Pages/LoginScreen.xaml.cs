using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;
using DWSS.Middleware;
using System;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace DWSS.Pages
{
    /// <summary>
    /// The login page
    /// </summary>
    public sealed partial class LoginScreen : Page
    {
        /// <summary>
        /// Default constructor
        /// </summary>
        public LoginScreen()
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
        /// This bypasses the user's login with the first user in the database
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private async void BypassLoginClick(object sender, RoutedEventArgs e)
        {
            // Hides the login button as clicking this multiple times can cause problems 
            await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
            {
                (sender as Button).IsEnabled = false;
            });
            StaticData.currentUser = await MiddlewareConnections.GetUser("1");
            await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
            {
                if (StaticData.currentUser != null)
                    (Window.Current.Content as Frame).Navigate(typeof(MasterPage));
                else
                {
                    // TODO: This!
                    //StaticData.masterPage.ShowNotification("Sorry, Authentication Failed.");
                }
            });
            // Shows the login button again, in case the login was unsuccesful 
            await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
            {
                (sender as Button).IsEnabled = true;
            });
        }
    }
}
