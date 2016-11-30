using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using DWSS.Data;

// The User Control item template is documented at http://go.microsoft.com/fwlink/?LinkId=234236

namespace DWSS.UserControls
{
    public sealed partial class OrderUserControl : UserControl
    {
        public OrderUserControl()
        {
            this.InitializeComponent();
        }

        public OrderUserControl(Order order)
        {
            this.InitializeComponent();
            SetData(order);
        }

        public void SetData(Order order)
        {
            this.CustomerNameTextBlock.Text = order.staffOrdered.name;
            this.QuantityTextBlock.Text = order.quantity + "x";
            this.LocationTextBlock.Text = order.location.value;
            this.ProductUserControl.SetData(order.product);
            this.FulfillmentButton.Tag = order;
        }

        private void FulfillButtonClick(object sender, RoutedEventArgs e)
        {
            if ((sender as Button).Tag is Order)
            {
                // Fulfill the order
                StaticData.OrderFulfilmentPage.FulfilOrder((sender as Button).Tag as Order);
            }
            // Else do nothing this wasn't called from the right button 
        }
    }
}
