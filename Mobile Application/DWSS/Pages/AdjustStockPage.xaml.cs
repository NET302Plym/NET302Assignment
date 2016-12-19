using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Navigation;
using DWSS.Data;
using DWSS.UserControls;
using System;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace DWSS.Pages
{
    /// <summary>
    /// This is the page shown when searching for a product to modify 
    /// </summary>
    public sealed partial class AdjustStockPage : Page
    {
        /// <summary>
        /// Default constructor
        /// </summary>
        public AdjustStockPage()
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
        /// Cache the search terms to prevent unnessesary stress on the server by constantly re-searching the same terms 
        /// </summary>
        private string searchTermsCache = "";
        
        /// <summary>
        /// Used when clicking a result, loads the searched item and navigates to the product change page 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="tappedRoutedEventArgs"></param>
        private void VisualProductOnTapped(object sender, TappedRoutedEventArgs tappedRoutedEventArgs)
        {
            Product product = (sender as ProductUserControl).product;
            if (product == null) return;

            StaticData.masterPage.Navigate(typeof(Pages.AdjustProductPage));
            StaticData.adjustProductPage.SetData(product);
        }

        /// <summary>
        /// Searches using the given search terms and outputs to the page all the results that match that criteria. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private async void FindButtonClick(object sender, RoutedEventArgs e)
        {
            StaticData.masterPage.ShowProgressRing();
            string searchTerms = SearchTermsTextBox.Text;
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
                foreach (var product in await Middleware.MiddlewareConnections.SearchForProduct(searchTerms))
                {
                    await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
                    {
                        var visualProduct = new ProductUserControl();
                        visualProduct.SetData(product);
                        visualProduct.Tapped += VisualProductOnTapped;
                        visualProduct.Margin = new Thickness(10, 10, 10, 50);
                        PageContentStackPanel.Children.Add(visualProduct);
                    });
                }
            }
            searchTermsCache = searchTerms;
            StaticData.masterPage.HideProgressRing();
        }
    }
}
