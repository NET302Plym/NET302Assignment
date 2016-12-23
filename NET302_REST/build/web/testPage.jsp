<%@page import="NET302_Handlers.DB_Handler"%>
<%  // test page for certain debugging methods
    // debugging methods are commented out in the handler code!
    
    DB_Handler handler = new DB_Handler();
    
    if (handler.GetConnection()) {
        // TODO: Test support for the Encrypter object.
        // This will not be done as the mobile application is not at the same point.
        out.print(""
            //handler.getAllProductsQ()
        );
    }
    
    out.flush();
%>