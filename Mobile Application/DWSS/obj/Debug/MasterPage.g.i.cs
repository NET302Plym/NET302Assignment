﻿

#pragma checksum "D:\My Files\Documents\University\University Year 3\NET302 - Distributed Systems\Assignment\Sourcecode\Mobile Application\DWSS\MasterPage.xaml" "{406ea660-64cf-4c82-b6f0-42d48172a799}" "E17CA63CD6CB97C1CDDBD97D52E9B8E5"
//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace DWSS
{
    partial class MasterPage : global::Windows.UI.Xaml.Controls.Page
    {
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private global::DWSS.UserControls.ProgressRing ProgressRingUserControl; 
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private global::Windows.UI.Xaml.Controls.TextBlock UsernameTextBlock; 
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private global::Windows.UI.Xaml.Controls.Grid NotificationGrid; 
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private global::Windows.UI.Xaml.Controls.TextBlock NotificationText; 
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private global::Windows.UI.Xaml.Controls.Frame pageViewer; 
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        private bool _contentLoaded;

        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 4.0.0.0")]
        [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
        public void InitializeComponent()
        {
            if (_contentLoaded)
                return;

            _contentLoaded = true;
            global::Windows.UI.Xaml.Application.LoadComponent(this, new global::System.Uri("ms-appx:///MasterPage.xaml"), global::Windows.UI.Xaml.Controls.Primitives.ComponentResourceLocation.Application);
 
            ProgressRingUserControl = (global::DWSS.UserControls.ProgressRing)this.FindName("ProgressRingUserControl");
            UsernameTextBlock = (global::Windows.UI.Xaml.Controls.TextBlock)this.FindName("UsernameTextBlock");
            NotificationGrid = (global::Windows.UI.Xaml.Controls.Grid)this.FindName("NotificationGrid");
            NotificationText = (global::Windows.UI.Xaml.Controls.TextBlock)this.FindName("NotificationText");
            pageViewer = (global::Windows.UI.Xaml.Controls.Frame)this.FindName("pageViewer");
        }
    }
}



