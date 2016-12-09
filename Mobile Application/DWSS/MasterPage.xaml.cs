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

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace DWSS
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class MasterPage : Page
    {
        public MasterPage()
        {
            this.InitializeComponent();
            StaticData.masterPage = this;
            Navigate(typeof(Pages.OptionsPage));
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

        public void Navigate(System.Type page)
        {
            pageViewer.Navigate(page);
        }

        public void GoBack()
        {
            pageViewer.GoBack();
        }

        private void HomeButtonClick(object sender, RoutedEventArgs e)
        {
            Navigate(typeof(Pages.OptionsPage));
        }

        public void ShowNotification(string notificationText)
        {
            NotificationText.Text = notificationText;
            NotificationGrid.Visibility = Visibility.Visible;
        }

        private void NotificationOkayClick(object sender, RoutedEventArgs e)
        {
            NotificationGrid.Visibility = Visibility.Collapsed;
        }
    }
}
