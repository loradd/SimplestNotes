package it.univaq.f4i.iw.simplestnote.view;


import it.univaq.f4i.iw.simplestnote.controller.init;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marco Di Natale
 */
public class error extends init {

    public void ErrorUsernamePassword(String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {

        TemplateResult res = new TemplateResult(context);
        request.setAttribute("validateForm", "validateForm");
        request.setAttribute("emailLogin", email);
        res.PublicActivate("header-login.ftl.html", "body-login.ftl.html", request, response);

    }    

    public void ErrorPassPass(String nome,String cognome,String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {

        TemplateResult res = new TemplateResult(context);
        request.setAttribute("box_message", "box_message");
        request.setAttribute("message", "Passwords mismatch!");
        request.setAttribute("errorPassPass", "validateForm");
        request.setAttribute("nome", nome);
        request.setAttribute("cognome", cognome);
        request.setAttribute("email", email);
        res.PublicActivate("header-login.ftl.html", "body-login.ftl.html", request, response);

    }  

    public void ErrorEmailPresente(String nome,String cognome,String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {

        TemplateResult res = new TemplateResult(context);
        request.setAttribute("box_message", "box_message");
        request.setAttribute("message", "Email Address is already in our system!");
        request.setAttribute("nome", nome);
        request.setAttribute("cognome", cognome);
        request.setAttribute("email", email);
        res.PublicActivate("header-login.ftl.html", "body-login.ftl.html", request, response);

    }  

    public void ErrorValidate(String nome, String cognome,String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {

        TemplateResult res = new TemplateResult(context);
        request.setAttribute("box_message", "box_message");
        request.setAttribute("message", "Wrong inserted data!");
        request.setAttribute("nome", nome);
        request.setAttribute("cognome", cognome);
        request.setAttribute("email", email);                    
        res.PublicActivate("header-login.ftl.html", "body-login.ftl.html", request, response);

    }  

    public void ErrorValidateEditProfile(String name, String surname, String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {

        TemplateResult res = new TemplateResult(context);
        request.setAttribute("error", "validateForm");
        request.setAttribute("name", name);
        request.setAttribute("surname",surname);
        request.setAttribute("email", email);
        request.setAttribute("contactList", context.getAttribute("contactList"));
        res.PrivateActivate("profile.ftl.html", request, response);

    }  

    public void ErrorValidateEditPass(String name, String surname, String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {

        TemplateResult res = new TemplateResult(context);
        request.setAttribute("errorPass", "validateForm");
        request.setAttribute("name", name);
        request.setAttribute("surname", surname);
        request.setAttribute("email", email);  
        request.setAttribute("contactList", context.getAttribute("contactList"));
        res.PrivateActivate("profile.ftl.html", request, response);

    }

    public void ErrorValidateEditOldPass(String name, String surname, String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {

        TemplateResult res = new TemplateResult(context);
        request.setAttribute("errorOldPass", "validateForm");
        request.setAttribute("name", name);
        request.setAttribute("surname", surname);
        request.setAttribute("email", email);  
        request.setAttribute("contactList", context.getAttribute("contactList"));
        res.PrivateActivate("profile.ftl.html", request, response);

    }

    public void ErrorValidateEditPassPass(String name,String surname,String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {
                     try {
                        TemplateResult res = new TemplateResult(context);
                        request.setAttribute("errorPassPass", "validateForm");
                        request.setAttribute("name", name);
                        request.setAttribute("surname", surname);
                        request.setAttribute("email", email);
                        request.setAttribute("contactList", context.getAttribute("contactList"));
                        res.PrivateActivate("profile.ftl.html", request, response);
                    } catch (ServletException ex) {
                        Logger.getLogger(error.class.getName()).log(Level.SEVERE, null, ex);
                    } 

        }

    public void ErrorEmailRecovery(String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {

        TemplateResult res = new TemplateResult(context);
        request.setAttribute("box_message", "box_message");
        request.setAttribute("message", "Invalid email address");
        request.setAttribute("email", email);
        res.PublicActivate("header-login.ftl.html","body-login.ftl.html","showRecoveryPass.ftl.html", request, response);

    } 


    public void ErrorShared(int idNote,String name,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {

        TemplateResult res = new TemplateResult(context);
        request.setAttribute("box_message", "box_message");
        request.setAttribute("message", "Invalid email address");
        request.setAttribute("name", name);
        request.setAttribute("noteid", idNote);
        res.PrivateActivate("showAddTag.ftl.html","filter.ftl.html", "notes-list.ftl.html","right-sidebar-list.ftl.html", request, response);

    } 
    
}
