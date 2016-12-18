using Windows.UI.Xaml.Controls;
using DWSS.Data;
using System;

// The User Control item template is documented at http://go.microsoft.com/fwlink/?LinkId=234236

namespace DWSS.UserControls
{
    public sealed partial class ProductUserControl : UserControl
    {
        /// <summary>
        /// The product this user control is focusing on 
        /// </summary>
        public Product product { get; private set; }
        /// <summary>
        /// An external handler for the internal click event 
        /// </summary>
        public event EventHandler<EventArgs> click;

        /// <summary>
        /// A default constructor
        /// </summary>
        public ProductUserControl()
        {
            this.InitializeComponent();
        }

        /// <summary>
        /// Externally used to load a product into the UI as well as an option to show the "find on google" button 
        /// </summary>
        /// <param name="product"></param>
        /// <param name="showFind"></param>
        public void SetData(Product product, bool showFind = false)
        {
            this.product = product;
            this.NameTextBlock.Text = product.name;
            this.CategoryTextBlock.Text = product.category.value;
            this.SubCategoryTextBlock.Text = product.subCategory.value;
            this.ContainerTextBlock.Text = product.container.value;
            if (showFind)
                FindGoogleImageButton.Visibility = Windows.UI.Xaml.Visibility.Visible;
        }

        /// <summary>
        /// Clicks the externally handled click from the internally handled click
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Button_Click(object sender, Windows.UI.Xaml.RoutedEventArgs e)
        {
            click?.Invoke(this, null);
        }
    }
}
