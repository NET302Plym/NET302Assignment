using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;
using DWSS.Data;
using DWSS.UserControls;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace DWSS.Pages
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class AdjustProductPage : Page
    {
        private Product product;

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

        public void SetData(Product product)
        {
            this.product = product;
            var userControl = new ProductUserControl();
            userControl.SetData(product);
            ProductGrid.Children.Add(userControl);
            NewQuantityTextBox.Text = product.stockCount.ToString();
        }

        private void SubmitChangesButtonClick(object sender, RoutedEventArgs e)
        {
            // Submit the changes
            int x;
            if (int.TryParse(NewQuantityTextBox.Text, out x))
            {
                Middleware.MiddlewareConnections.UploadChanges(product, x);
                // Show a message
                StaticData.masterPage.ShowNotification("Quantity has been changed to " + x.ToString());
                StaticData.masterPage.GoBack();
            }
            else
            {
                // Inform the user of the incorrect quantity 
                StaticData.masterPage.ShowNotification("Incorrect quantity has been entered!");
            }
        }
    }
}
