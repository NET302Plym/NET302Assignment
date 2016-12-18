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
    public static ManagementBean GetManagementBean(){
        HttpSession session = session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        //if (FacesContext.getCurrentInstance().getExternalContext().getSession(false) == null)
       // {
        //    session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        //    session.setAttribute("mb", new ManagementBean());
        //    ((ManagementBean)session.getAttribute("mb")).f = "new string";
       // }
       // else {
       //     session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
       // }
        
        if (session.getAttribute("mb") == null)
        {
            // Set up vars
            session.setAttribute("mb", new ManagementBean());
            ((ManagementBean)session.getAttribute("mb")).f = "new string";
        }
        
        String s = ((ManagementBean)session.getAttribute("mb")).f;
        
        return (ManagementBean)session.getAttribute("mb");
    }
}
