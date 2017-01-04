/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net302;

import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Dan
 */
@Named(value = "SessionHandler")
@Dependent
public class SessionHandler {
    //get an instance of management bean and store in the users session
    public static ManagementBean GetManagementBean(){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        if (session.getAttribute("mb") == null)
        {
            // Set up vars
            session.setAttribute("mb", new ManagementBean());
        }
        
        return (ManagementBean)session.getAttribute("mb");
    }
}
