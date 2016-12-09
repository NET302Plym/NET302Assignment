﻿using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;
using DWSS.Data;
using DWSS.UserControls;
using System;
using Windows.UI.Xaml.Media.Imaging;

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
        protected async override void OnNavigatedTo(NavigationEventArgs e)
        {
            StaticData.OrderFulfilmentPage = this; // Subscribe to the static data page
            await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
            {
                PageContentStackPanel.Children.Clear();
            });
            foreach (var order in await Middleware.MiddlewareConnections.GetOutstandingOrders())
                await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
                {
                    var orderUI = new OrderUserControl(order);
                    orderUI.click += (s, o) =>
                    {
                        // TODO: Search & show a full-screen UI for the image
                        string url = Middleware.MiddlewareConnections.DownloadImage((s as ProductUserControl).product.name);
                        if (!string.IsNullOrWhiteSpace(url))
                            ShowImage(url, (s as ProductUserControl).product.name);
                    };
                    PageContentStackPanel.Children.Add(orderUI);
                });
        }

        private void ShowImage(string url, string name)
        {
            Image.Source = new BitmapImage(new Uri(url, UriKind.Absolute));
            ImageText.Text = name;
            ImageViewer.Visibility = Visibility.Visible;
        }

        public async void FulfilOrder(Order orderToFulfill)
        {
            await Middleware.MiddlewareConnections.FulfillOrder(orderToFulfill, StaticData.currentUser);
            // Reload the UI
            await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
            {
                OnNavigatedTo(null);
                // Show a notification
                StaticData.masterPage.ShowNotification("Order " + orderToFulfill.ID + " has been fulfilled");
            });
        }

        private void HomeButtonClick(object sender, RoutedEventArgs e)
        {
            StaticData.masterPage.Navigate(typeof(Pages.OptionsPage));
        }

        private void ImageViewerButtonClick(object sender, RoutedEventArgs e)
        {
            ImageViewer.Visibility = Visibility.Collapsed;
        }
    }
}
