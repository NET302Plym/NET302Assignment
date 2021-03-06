﻿using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;
using DWSS.Data;
using DWSS.UserControls;
using System;
using Windows.UI.Xaml.Media.Imaging;
using System.Linq;

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
            LoadUI(); // Loads all unfulfilled orders from the REST API into the GUI
        }

        /// <summary>
        /// This shows an image for a clicked product
        /// </summary>
        /// <param name="url"></param>
        /// <param name="name"></param>
        private void ShowImage(string url, string name)
        {
            Image.Source = new BitmapImage(new Uri(url, UriKind.Absolute));
            ImageText.Text = name;
            ImageViewer.Visibility = Visibility.Visible;
        }

        /// <summary>
        /// Calls the Middleware fulfillment page for a particular order
        /// </summary>
        /// <param name="orderToFulfill"></param>
        public async void FulfilOrder(Order orderToFulfill)
        {
            StaticData.masterPage.ShowProgressRing();
            bool success = await Middleware.MiddlewareConnections.FulfillOrder(orderToFulfill);
            // Reload the UI
            if (success)
            {
                // Show a message & clear the children
                await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
                {
                    StaticData.masterPage.ShowNotification("Order " + orderToFulfill.ID + " has been fulfilled");
                });
                // Reload the data 
                LoadUI();
            }
            else
            {
                await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
                {
                    StaticData.masterPage.ShowNotification("Error! Internal server error");
                });
            }
            StaticData.masterPage.HideProgressRing();
        }
        
        /// <summary>
        /// Loads orders from the Middleware REST API into the GUI
        /// </summary>
        private async void LoadUI()
        {
            StaticData.masterPage.ShowProgressRing();
            await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
            {
                PageContentStackPanel.Children.Clear();
            });

            foreach (var order in (await Middleware.MiddlewareConnections.GetOutstandingOrders()).Where(xOrder => !xOrder.fulfilled))
            {
                await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
                {
                    var orderUI = new OrderUserControl(order);
                    orderUI.click += (s, o) =>
                    {
                        StaticData.masterPage.ShowProgressRing();
                        string url = Middleware.MiddlewareConnections.DownloadImage((s as ProductUserControl).product.name);
                        if (!string.IsNullOrWhiteSpace(url))
                            ShowImage(url, (s as ProductUserControl).product.name);
                        StaticData.masterPage.HideProgressRing();
                    };
                    
                    PageContentStackPanel.Children.Add(orderUI);
                });
            }
            StaticData.masterPage.HideProgressRing();
        }

        /// <summary>
        /// Returns the user to the home page 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void HomeButtonClick(object sender, RoutedEventArgs e)
        {
            StaticData.masterPage.Navigate(typeof(Pages.OptionsPage));
        }

        /// <summary>
        /// Closes the image viewer
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ImageViewerButtonClick(object sender, RoutedEventArgs e)
        {
            ImageViewer.Visibility = Visibility.Collapsed;
        }
    }
}
