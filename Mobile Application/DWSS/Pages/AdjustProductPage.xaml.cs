using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;
using DWSS.Data;
using DWSS.UserControls;
using System;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace DWSS.Pages
{
    /// <summary>
    /// This is the page shown when changing a product quantity
    /// </summary>
    public sealed partial class AdjustProductPage : Page
    {
        /// <summary>
        /// The internal product
        /// </summary>
        private Product product;

        /// <summary>
        /// Default constructor
        /// </summary>
        public AdjustProductPage()
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
            StaticData.adjustProductPage = this; // Register to the static data
        }

        /// <summary>
        /// Externally load a product into this page and display on the screen
        /// </summary>
        /// <param name="product"></param>
        public void SetData(Product product)
        {
            this.product = product;
            var userControl = new ProductUserControl();
            userControl.SetData(product);
            ProductGrid.Children.Add(userControl);
            NewQuantityTextBox.Text = product.stockCount.ToString();
        }

        /// <summary>
        /// Submits the new quantity
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private async void SubmitChangesButtonClick(object sender, RoutedEventArgs e) 
        {
            // Submit the changes
            int x;
            if (int.TryParse(NewQuantityTextBox.Text, out x))
            {
                bool success = await Middleware.MiddlewareConnections.UploadChanges(product, x);
                // Show a message
                await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
                {
                    if (success)
                    {
                        StaticData.masterPage.ShowNotification("Quantity has been changed to " + x.ToString());
                        StaticData.masterPage.GoBack();
                    }
                    else
                        StaticData.masterPage.ShowNotification("Error! Internal Server Error");
                });
            }
            else
            {
                await Dispatcher.RunAsync(Windows.UI.Core.CoreDispatcherPriority.Normal, () =>
                {
                    // Inform the user of the incorrect quantity 
                    StaticData.masterPage.ShowNotification("Incorrect quantity has been entered!");
                });
            }
        }
    }
}
