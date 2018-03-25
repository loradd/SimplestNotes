/*
 * init.java
 *
 */
package it.univaq.f4i.iw.simplestnote.controller;
import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.servlet.*;
import javax.servlet.ServletContext;
import javax.servlet.http.*;


/**
 *
 * @author Marco Di Natale
 * @version
 */
public class init extends SimpleNoteBaseController {

    protected ServletContext context;
    SimpleDateFormat date = new SimpleDateFormat();
    
    //Verifico all'accesso sulla web application se ci sono utenti loggati
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DataLayerException {
        HttpSession s = SecurityLayer.checkSession(request);
        if (s == null) {
            //Utente non loggato mostra la pagina principale della web application
            action_login(request, response);
        } else {
            action_logged(request, response);
        }
    }    
    
    //Eseguo action_login solo se non sono autenticato
    private void action_login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Delega la costruzione della pagina alla Servlet View loginView 
        getServletContext().setAttribute("state1","close");
        getServletContext().setAttribute("state2","close");
        getServletContext().setAttribute("id-note1","0");
        getServletContext().setAttribute("id-note2","0");
        response.sendRedirect("loginView");
    }
    
    
//Eseguo action_logged solo se autenticato    

    void action_logged(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Delega la costruzione della pagina alla Servlet View Logged 
        response.sendRedirect("logged");
    }
}
