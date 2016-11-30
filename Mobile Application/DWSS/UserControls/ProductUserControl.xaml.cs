using Windows.UI.Xaml.Controls;
using DWSS.Data;

// The User Control item template is documented at http://go.microsoft.com/fwlink/?LinkId=234236

namespace DWSS.UserControls
{
    public sealed partial class ProductUserControl : UserControl
    {
        public Product product { get; private set; }

        public ProductUserControl()
        {
            this.InitializeComponent();
        }

        public void SetData(Product product)
        {
            this.product = product;
            this.NameTextBlock.Text = product.name;
            this.CategoryTextBlock.Text = product.category.value;
            this.SubCategoryTextBlock.Text = product.subCategory.value;
            this.ContainerTextBlock.Text = product.container.value;
        }
    }
}
