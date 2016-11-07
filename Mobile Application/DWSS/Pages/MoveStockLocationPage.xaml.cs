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
    public sealed partial class MoveStockLocationPage : Page
    {
        public MoveStockLocationPage()
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
            this.UsernameTextBlock.Text = StaticData.currentUser.username; // Push the username out to the screen
        }

        private void HomeButtonClick(object sender, RoutedEventArgs e)
        {
            (Window.Current.Content as Frame).Navigate(typeof(Pages.OptionsPage));
        }
        private string searchTermsCache = "";
        private void SearchTermTextBoxChange(object sender, KeyRoutedEventArgs e)
        {
            string searchTerms = (sender as TextBox).Text;
            if (searchTerms == searchTermsCache) return;
            if (string.IsNullOrEmpty(searchTerms))
            {
                // Clear the results
                this.PageContentStackPanel.Children.Clear();
            }
            else
            {
                this.PageContentStackPanel.Children.Clear();
                // Perform a search
                foreach (var product in Middleware.MiddlewareConnections.SearchForProduct(searchTerms))
                {
                    var visualProduct = new ProductUserControl();
                    visualProduct.SetData(product);
                    visualProduct.Tapped += VisualProductOnTapped;
                    visualProduct.Margin = new Thickness(10, 10, 10, 50);
                    this.PageContentStackPanel.Children.Add(visualProduct);
                }
            }
            searchTermsCache = searchTerms;
        }

        private void VisualProductOnTapped(object sender, TappedRoutedEventArgs tappedRoutedEventArgs)
        {
            Product product = (sender as ProductUserControl).product;
            if (product == null) return;

            (Window.Current.Content as Frame).Navigate(typeof(Pages.AdjustProductPage));
            StaticData.adjustProductPage.SetData(product);
        }
    }
}
