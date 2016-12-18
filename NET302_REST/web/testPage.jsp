<%@page import="NET302_Handlers.DB_Handler"%>
<%  // test page for certain debugging methods
    // debugging methods are commented out in the handler code!
    
    DB_Handler handler = new DB_Handler();
    
    if (handler.GetConnection()) {
        out.print(
            //handler.getAllProductsQ()
        );
    }
    
    out.flush();
%>