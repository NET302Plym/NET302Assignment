﻿

#pragma checksum "E:\NET302 Start\DWSS\DWSS\Pages\AdjustStockPage.xaml" "{406ea660-64cf-4c82-b6f0-42d48172a799}" "269C725A2F32079A70FB9C76673C5DDF"
//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace DWSS.Pages
{
    partial class AdjustStockPage : global::Windows.UI.Xaml.Controls.Page, global::Windows.UI.Xaml.Markup.IComponentConnector
    {
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
 
        public void Connect(int connectionId, object target)
        {
            switch(connectionId)
            {
            case 1:
                #line 16 "..\..\Pages\AdjustStockPage.xaml"
                ((global::Windows.UI.Xaml.Controls.Primitives.ButtonBase)(target)).Click += this.HomeButtonClick;
                 #line default
                 #line hidden
                break;
            case 2:
                #line 21 "..\..\Pages\AdjustStockPage.xaml"
                ((global::Windows.UI.Xaml.UIElement)(target)).KeyUp += this.SearchTermTextBoxChange;
                 #line default
                 #line hidden
                break;
            }
            this._contentLoaded = true;
        }
    }
}


