﻿

#pragma checksum "C:\Users\Daniel\Documents\NET302Assignment\Mobile Application\DWSS\Pages\OptionsPage.xaml" "{406ea660-64cf-4c82-b6f0-42d48172a799}" "2EE643CB3C4CEF737633AEB43EDEF7A0"
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
    partial class OptionsPage : global::Windows.UI.Xaml.Controls.Page, global::Windows.UI.Xaml.Markup.IComponentConnector
    {
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
 
        public void Connect(int connectionId, object target)
        {
            switch(connectionId)
            {
            case 1:
                #line 17 "..\..\Pages\OptionsPage.xaml"
                ((global::Windows.UI.Xaml.Controls.Primitives.ButtonBase)(target)).Click += this.FulfillOrdersButtonClick;
                 #line default
                 #line hidden
                break;
            case 2:
                #line 26 "..\..\Pages\OptionsPage.xaml"
                ((global::Windows.UI.Xaml.Controls.Primitives.ButtonBase)(target)).Click += this.AdjustStockButtonClick;
                 #line default
                 #line hidden
                break;
            case 3:
                #line 35 "..\..\Pages\OptionsPage.xaml"
                ((global::Windows.UI.Xaml.Controls.Primitives.ButtonBase)(target)).Click += this.LogoutButtonClick;
                 #line default
                 #line hidden
                break;
            }
            this._contentLoaded = true;
        }
    }
}


