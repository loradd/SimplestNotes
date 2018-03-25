package it.univaq.f4i.iw.simplestnote.controller;

import it.univaq.f4i.iw.simplestnote.mail.MailUtility;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marco
 */
@WebServlet(name = "InviteSimplestNote", urlPatterns = {"/InviteSimplestNote"})
public class InviteSimplestNote extends init {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getParameter("action")){    
            case "Invite":
                action_Invite(request, response);
                break;  
            case "back":
                response.sendRedirect("logged");
                break;                  
        }        
    }

    private void action_Invite(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

            try {
                String mail= request.getParameter("email");
                String by=request.getParameter("by");

                String message ="Hello ,<br>"+by+" invited you to subscribe to SimplestNotes! Visit our website <a href=\"#\" target=\"_blank\">SimplestNotes.it</a><br>\n" +
                                "<br><br><br>\n" +
                                "Cheers<br>SimplestNotes Team";
                
                MailUtility.sendMail(mail,message);
            } catch (MessagingException ex) {
                Logger.getLogger(InviteSimplestNote.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect("logged");
    }    
    
}
