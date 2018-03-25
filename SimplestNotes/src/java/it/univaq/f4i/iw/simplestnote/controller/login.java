/*
 * login.java
 *
 */
package it.univaq.f4i.iw.simplestnote.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READ_ACCESS;
import it.univaq.f4i.iw.simplestnote.data.model.User;
import it.univaq.f4i.iw.simplestnote.view.error;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 *
 * @author Marco Di Natale
 * 
 */
public class login extends init {
    public error error=new error();
   
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {

        try {
            boolean cMail=false;
            boolean cPass=false;
            String email = request.getParameter("email");
            String password = request.getParameter("pass");
            
            cMail= Validate.ValidateMail(email);
            cPass=Validate.ValidatePassword(password);
            
            String md5=null;
            MessageDigest mdEnc = MessageDigest.getInstance("MD5"); // Encryption algorithm
            mdEnc.update(password.getBytes(), 0, password.length());
            md5 = new BigInteger(1, mdEnc.digest()).toString(16);
            
            //se dal controllo risulta pulito l'imput proseguo altrimenti do errore
            if ((cMail==true)&&(cPass==true)){
                //Verificata la correttezza sintattica dell'email e della password 
                //Posso verificare la correttezza dei dati nel db
                
                boolean isCorrectPassword = false;
                List<User> emailUsers = getDataLayer().getUsersFromEmailAddress(email, READ_ACCESS);
                User myuser = getDataLayer().createUser();
                for(User user : emailUsers){
                    if(user.getPassword().equals(md5)){
                        myuser.copyFrom(user);
                        isCorrectPassword = true;
                        break;
                    }
                }
                if(isCorrectPassword){
                    //Verificata la corretta identit� dell'utente
                    //Mi faccio restituire lo UserId dell'utente loggato e lo assegno alla variabile sottostante
                    SecurityLayer.createSession(request, myuser.getEmailAddress(), myuser.getId());
                    User user = getDataLayer().getUserByKey(myuser.getId(), READ_ACCESS);
                    String name = user.getFirstName();
                    String surname = user.getLastName();
                    getServletContext().setAttribute("user",surname+" "+ name);
                    //Verificata l'identit� dell'utente accedo all'area privata
                    response.sendRedirect("core");
                } else {
                    error.ErrorUsernamePassword(email,request, response, getServletContext());          
                }
            }else{
                //Controllo terminato con una sintassi non consentita
                //l'email non ha una conformità reale
                //la password non contiene almeno un carattere grande o un numero
                //segnalo l'errore all'utente
                error.ErrorUsernamePassword(email,request, response, getServletContext());
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
