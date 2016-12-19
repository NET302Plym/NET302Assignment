using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using DWSS.Data;
using System;

// The User Control item template is documented at http://go.microsoft.com/fwlink/?LinkId=234236

namespace DWSS.UserControls
{
    public sealed partial class OrderUserControl : UserControl
    {
        /// <summary>
        /// External handler for the internal click event 
        /// </summary>
        public event EventHandler<EventArgs> click;

        /// <summary>
        /// Default constructor
        /// </summary>
        public OrderUserControl()
        {
            this.InitializeComponent();
        }

        /// <summary>
        /// Constructs and loads an order
        /// </summary>
        /// <param name="order"></param>
        public OrderUserControl(Order order)
        {
            this.InitializeComponent();
            SetData(order);
        }

        /// <summary>
        /// Loads an order into the GUI
        /// </summary>
        /// <param name="order"></param>
        public void SetData(Order order)
        {
            this.CustomerNameTextBlock.Text = order.staffOrdered.name;
            this.QuantityTextBlock.Text = order.quantity + "x";
            this.LocationTextBlock.Text = order.location.value;
            this.ProductUserControl.SetData(order.product, true);
            this.ProductUserControl.click += (s, o) => { click?.Invoke(s, o); };
            this.FulfillmentButton.Tag = order;
        }

        /// <summary>
        /// Fulfills the current order 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void FulfillButtonClick(object sender, RoutedEventArgs e)
        {
            if (sender is Button)
                if ((sender as Button).Tag is Order)
                    // Fulfill the order
                    StaticData.OrderFulfilmentPage.FulfilOrder((sender as Button).Tag as Order);
        }
    }
}
