﻿

#pragma checksum "D:\My Files\Documents\University\University Year 3\NET302 - Distributed Systems\Assignment\Sourcecode\Mobile Application\DWSS\UserControls\OrderUserControl.xaml" "{406ea660-64cf-4c82-b6f0-42d48172a799}" "E49187576F3EFE5B2E950C357039A22B"
//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace DWSS.UserControls
{
    partial class OrderUserControl : global::Windows.UI.Xaml.Controls.UserControl
    {
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private global::DWSS.UserControls.ProductUserControl ProductUserControl; 
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private global::Windows.UI.Xaml.Controls.Button FulfillmentButton; 
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private global::Windows.UI.Xaml.Controls.TextBlock CustomerNameTextBlock; 
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private global::Windows.UI.Xaml.Controls.TextBlock QuantityTextBlock; 
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private global::Windows.UI.Xaml.Controls.TextBlock LocationTextBlock; 
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private bool _contentLoaded;

        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
        public void InitializeComponent()
        {
            if (_contentLoaded)
                return;

            _contentLoaded = true;
            global::Windows.UI.Xaml.Application.LoadComponent(this, new global::System.Uri("ms-appx:///UserControls/OrderUserControl.xaml"), global::Windows.UI.Xaml.Controls.Primitives.ComponentResourceLocation.Application);
 
            ProductUserControl = (global::DWSS.UserControls.ProductUserControl)this.FindName("ProductUserControl");
            FulfillmentButton = (global::Windows.UI.Xaml.Controls.Button)this.FindName("FulfillmentButton");
            CustomerNameTextBlock = (global::Windows.UI.Xaml.Controls.TextBlock)this.FindName("CustomerNameTextBlock");
            QuantityTextBlock = (global::Windows.UI.Xaml.Controls.TextBlock)this.FindName("QuantityTextBlock");
            LocationTextBlock = (global::Windows.UI.Xaml.Controls.TextBlock)this.FindName("LocationTextBlock");
        }
    }
}



