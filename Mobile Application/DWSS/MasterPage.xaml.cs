using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.Phone.UI.Input;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace DWSS
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class MasterPage : Page
    {
        /// <summary>
        /// Constructor
        ///     - Sets this to static data
        ///     - Loads the option page
        ///     - Overrides the back button 
        /// </summary>
        public MasterPage()
        {
            this.InitializeComponent();
            StaticData.masterPage = this;
            Navigate(typeof(Pages.OptionsPage));

            HardwareButtons.BackPressed += (s, o) =>
            {
                if (!pageViewer.CanGoBack) return;
                pageViewer.GoBack();
                o.Handled = true;
            };
        }

        /// <summary>
        /// Invoked when this page is about to be displayed in a Frame.
        /// </summary>
        /// <param name="e">Event data that describes how this page was reached.
        /// This parameter is typically used to configure the page.</param>
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            this.UsernameTextBlock.Text = StaticData.currentUser.name; // Push the username out to the screen
        }
        
        /// <summary>
        /// Used to externally direct the navigation to a particular page
        /// </summary>
        /// <param name="page"></param>
        public void Navigate(System.Type page)
        {
            pageViewer.Navigate(page);
        }

        /// <summary>
        /// Moves back a page, externally
        /// </summary>
        public void GoBack()
        {
            pageViewer.GoBack();
        }

        /// <summary>
        /// Handler for the button at the top of a page to go back to home 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void HomeButtonClick(object sender, RoutedEventArgs e)
        {
            Navigate(typeof(Pages.OptionsPage));
        }

        /// <summary>
        /// Externally used to show a notification over the application 
        /// </summary>
        /// <param name="notificationText"></param>
        public void ShowNotification(string notificationText)
        {
            NotificationText.Text = notificationText;
            NotificationGrid.Visibility = Visibility.Visible;
        }

        /// <summary>
        /// Closes the notification 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void NotificationOkayClick(object sender, RoutedEventArgs e)
        {
            NotificationGrid.Visibility = Visibility.Collapsed;
        }

        /// <summary>
        /// Can be called externally to start the "waiting" progress ring of the application 
        /// </summary>
        public async void ShowProgressRing()
        {
            await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
            {
                ProgressRingUserControl.Visibility = Visibility.Visible;
            });
        }

        /// <summary>
        /// Can be called externally to stop the "waiting" progress ring of the application 
        /// </summary>
        public async void HideProgressRing()
        {
            await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
            {
                ProgressRingUserControl.Visibility = Visibility.Collapsed;
            });
        }
    }
}
