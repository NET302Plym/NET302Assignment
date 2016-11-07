using System;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
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
            this.CategoryTextBlock.Text = product.category;
            this.SubCategoryTextBlock.Text = product.category;
            this.ContainerTextBlock.Text = product.container;
        }
    }
}
