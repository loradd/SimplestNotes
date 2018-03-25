/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.simplestnote.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import static it.univaq.f4i.iw.simplestnote.data.impl.utilities.FileSystemManagement.delFileRecursively;
import it.univaq.f4i.iw.simplestnote.data.model.Note;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.ALL_PERMISSIONS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.CONFIRMED_STATUS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READ_ACCESS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.WRITE_ACCESS;
import it.univaq.f4i.iw.simplestnote.data.model.User;
import it.univaq.f4i.iw.simplestnote.mail.MailUtility;
import it.univaq.f4i.iw.simplestnote.view.CreateTemplate;
import it.univaq.f4i.iw.simplestnote.view.error;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Marco
 */
public class profile extends init {

    public final CreateTemplate template = new CreateTemplate();
    public final error error = new error();
    private boolean cName = false;
    private boolean cCognome = false;
    private boolean cMail = false;
    private boolean cPass1 = false;
    private boolean cPass2 = false;
    private int userid = 0;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DataLayerException {

        HttpSession s = SecurityLayer.checkSession(request);
        if (s != null) {
            HttpSession session = request.getSession(false);
            userid = (int) session.getAttribute("userid");
        }
        
        if(null != request.getParameter("profile")){
            switch (request.getParameter("profile")) {
            case "Profile":
                action_ShowProfile(userid, request, response);
                break;
            case "Remove":
                action_RemoveProfile(userid, request, response);
                break;
            case "New":
                action_registered(request, response);
                break;
            case "Edit":
                action_EditProfile(userid, request, response);
                break;
            case "Change":
                action_EditPassword(userid, request, response);
                break;
            case "Recovery":
                    try {
                        action_passRecovery(request, response);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                break;
            case "Back":
                action_ShowProfile(userid, request, response);
                break;
            case "back":
                response.sendRedirect("core");
                break;
            default:
                response.sendRedirect("core");
                break;
            }
        } else {
            response.sendRedirect("core");
        }
        
        

        
    }

    private void action_registered(HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException {

        String mex = null;
        String password1, password2;
        password1 = request.getParameter("password1");
        password2 = request.getParameter("password2");
        cName = Validate.ValidateString(request.getParameter("nome"));
        cCognome = Validate.ValidateString(request.getParameter("cognome"));
        cMail = Validate.ValidateMail(request.getParameter("email"));
        cPass1 = Validate.ValidatePassword(password1);
        cPass2 = Validate.ValidatePassword(password2);

        if (cName == true && cCognome == true && cMail == true && cPass1 == true && cPass2 == true) {
            if (password1.equals(password2)) {
                if (getDataLayer().getUsersFromEmailAddress(request.getParameter("email"), READ_ACCESS).isEmpty()) { // VER
                    try {
                        //Procedo con la registrazione a SimplestNote
                        String md5 = null;
                        MessageDigest mdEnc = MessageDigest.getInstance("MD5"); // Encryption algorithm
                        mdEnc.update(password1.getBytes(), 0, password1.length());
                        md5 = new BigInteger(1, mdEnc.digest()).toString(16);

                        User nuovoUtente = getDataLayer().createUser();
                        nuovoUtente.setFirstName(request.getParameter("nome"));
                        nuovoUtente.setLastName(request.getParameter("cognome"));
                        nuovoUtente.setEmailAddress(request.getParameter("email"));
                        nuovoUtente.setPassword(md5);
                        getDataLayer().storeUser(nuovoUtente);
                        
                        File userFolder = new File(getServletContext().getRealPath("/") + "../../users/" + nuovoUtente.getId());
                        userFolder.mkdir();
                        
                        try {
                            //stampa se inviato con successo
                            String mail = request.getParameter("email");
                            String message =    "Hi, <br>welcome to <a href=\"#\" target=\"_blank\">SimplestNotes.it</a><br>\n" +
                                                "<br><br>Login and start editing your notes!!! Your data:<br><br>\n" +
                                                "Username: "+mail+"<br>\n" +
                                                "Password: "+password1+"<br>\n" +
                                                "<br><br><br>\n" +
                                                "See you there!<br>SimplestNotes Team";
                           
                            MailUtility.sendMail(request.getParameter("email"), message);
                            mex = "An email containing your registration data has been sent to you!";
                        } catch (MessagingException exc) {
                            //stampa se non inviato con successo
                            mex = "Ooops! An unexpected error occurred while sending you an email message containing your registration data!";
                        }

                        template.CreateWelcome(mex, request.getParameter("nome"), request.getParameter("cognome"), request.getParameter("email"), request, response, getServletContext());
                    } catch (ServletException ex) {
                        Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    try {
                        error.ErrorEmailPresente(request.getParameter("nome"), request.getParameter("cognome"), request.getParameter("email"), request, response, getServletContext());
                        /* INDIRIZZO EMAIL GIA PRESENTE NEL SISTEMA, DEVE CAMBIARLO !!! */
                    } catch (ServletException ex) {
                        Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else {
                try {
                    //Segnalo la diversit� delle due password immesse
                    error.ErrorPassPass(request.getParameter("nome"), request.getParameter("cognome"), request.getParameter("email"), request, response, getServletContext());
                } catch (ServletException ex) {
                    Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            try {
                //Segnalo che si � verificato un errore nell'immissione dei dati nella form
                error.ErrorValidate(request.getParameter("nome"), request.getParameter("cognome"), request.getParameter("email"), request, response, getServletContext());
            } catch (ServletException ex) {
                Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void action_passRecovery(HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException, NoSuchAlgorithmException {
        String mail;
        cMail = Validate.ValidateMail(request.getParameter("email"));
        mail = request.getParameter("email");

        if (cMail == true) {
            //Email Immessa Corretta 
            //Verifico l'esistenza della meil
            //Invio l'email se superata la verifica dell'esistenza
            List<User> usersFromEmailAddress = getDataLayer().getUsersFromEmailAddress(mail, READ_ACCESS);
            if (usersFromEmailAddress.size() == 1) {
                /* POSSO PROCEDERE INVIO */

                String password = usersFromEmailAddress.get(0).getPassword();
                
                char[] startingCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                char[] bodyCharacters = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
                StringBuilder stringBuilder = new StringBuilder();
                Random random = new Random();
                stringBuilder.append(startingCharacters[random.nextInt(startingCharacters.length)]);
                stringBuilder.append(random.nextInt(9));
                for(int i = 0; i < 13; i++){
                    char character = bodyCharacters[random.nextInt(bodyCharacters.length)];
                    stringBuilder.append(character);
                }
                String newPassword = stringBuilder.toString();
                newPassword = newPassword.toUpperCase().charAt(0) + newPassword.substring(1);
                MessageDigest messageDigestEnconding = MessageDigest.getInstance("md5");
                messageDigestEnconding.update(newPassword.getBytes(), 0, newPassword.length());
                User user = getDataLayer().getUserByKey(usersFromEmailAddress.get(0).getId(), WRITE_ACCESS);
                user.setPassword(new BigInteger(1, messageDigestEnconding.digest()).toString(16)); 
                getDataLayer().storeUser(user);
                /* Il recupero della password consiste nel cancellare la pass vecchia dal db ed inserirne una nuova auto generata di lunghezza X 
                 ed inviarla via mail all'utente */
               // String message = "Le tue credenziali di accesso sono " + mail + " Password " + newPassword;
                String message = "Hi, <br>as requested, these are your login credentials:\n" +
                                 "<br><br>We suggest you to change your password next time you log in,<br><br>\n" +
                                 "Username: "+mail+"<br>\n" +
                                 "Password: "+newPassword+"<br>\n" +
                                 "<br><br><br>\n" +
                                 "Cheers<br>SimplestNotes Team";
                
                try {//stampa se inviato con successo
                    MailUtility.sendMail(mail, message);
                    String mex = "Email successfully sent!";
                    try {
                        template.CreateSendMailPass(mail, mex, request, response, getServletContext());
                    } catch (ServletException ex) {
                        Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (MessagingException exc) {
                    try {
                        //stampa se non inviato con successo
                        String mexError = "An unexpected error occurred! Try again!";
                        template.CreateSendMailPass(mail, mexError, request, response, getServletContext());
                    } catch (ServletException ex) {
                        Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else {
                try {
                    /* ERRORE EMAIL NON PRESENTE O MULTIPLA */
                    error.ErrorEmailRecovery(mail, request, response, getServletContext());
                } catch (ServletException ex) {
                    Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            try {
                //Errore Immissione Email segnalo l'errore
                error.ErrorEmailRecovery(mail, request, response, getServletContext());
            } catch (ServletException ex) {
                Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void action_ShowProfile(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException {
        //Recupero i dati dal database
        User user = getDataLayer().getUserByKey(userid, READ_ACCESS);
        String name = user.getFirstName();
        String surname = user.getLastName();
        String email = user.getEmailAddress();
        try {
            List<User> contactList = new ArrayList<User>();
            List<Note> userNotes = getDataLayer().getNotesFromUser(user, ALL_PERMISSIONS, CONFIRMED_STATUS, READ_ACCESS); // tutte le note non pending del nostro utente
            for(Note userNote : userNotes){ // considero ogni nota
                List<User> userNoteUsers = getDataLayer().getUsersFromNote(userNote, ALL_PERMISSIONS, CONFIRMED_STATUS, READ_ACCESS); // tutti gli utenti della nota in considerazione
                /* filtro la lista per eliminare il nostro utente */
                Iterator<User> userNoteUsersIterator = userNoteUsers.iterator();
                while(userNoteUsersIterator.hasNext()){
                    User currentUser = userNoteUsersIterator.next();
                    /* elimino il nostro utente */
                    if(currentUser.getId() == user.getId()){
                        userNoteUsersIterator.remove();
                    }
                    /* elimino ogni utente presente nella lista contatti */
                    for(User contact : contactList){
                        if(contact.getId() == currentUser.getId()){
                            userNoteUsersIterator.remove();
                            break;
                        }
                    }
                }
                contactList.addAll(userNoteUsers);
            }
            getServletContext().setAttribute("contactList", contactList);
            template.CreateProfile(userid, name, surname, email, request, response, getServletContext());
        } catch (ServletException ex) {
            Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void action_RemoveProfile(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException {

        User user = getDataLayer().getUserByKey(userid, READ_ACCESS);
        String name = user.getFirstName();
        String surname = user.getLastName();
        String email = user.getEmailAddress();

        switch (request.getParameter("action")) {
            case "show":
                try {
                    template.CreateRemoveProfile(name, surname, email, request, response, getServletContext());
                } catch (ServletException ex) {
                    Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "Remove":
                //Procedo con l'eliminazione dell'utente
                user = getDataLayer().getUserByKey(userid, WRITE_ACCESS);
                if(user.getAccessMode() == WRITE_ACCESS){
                    user.setFirstName(getServletContext().getRealPath("/"));
                    getDataLayer().deleteUser(user);
                    SecurityLayer.disposeSession(request);
                    try {
                        template.CreateAddio(name, surname, request, response, getServletContext());
                    } catch (ServletException ex) {
                        Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                        try {
                            template.ErrorWtiting(name, surname, email, request, response, getServletContext());
                        } catch (ServletException ex) {
                            Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                break;
        }

    }

    private void action_EditProfile(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException {
        String name, surname, email;
        name = request.getParameter("name");
        surname = request.getParameter("surname");
        email = request.getParameter("email");

        cName = Validate.ValidateString(name);
        cCognome = Validate.ValidateString(surname);
        cMail = Validate.ValidateMail(email);

        if (cName == true && cCognome == true && cMail == true) {
            try {
                //controllo correttezza immissione finito
                //salvo le modifiche
                User user = getDataLayer().getUserByKey(userid, WRITE_ACCESS);
                if(user.getAccessMode() == WRITE_ACCESS){
                    user.setFirstName(name);
                    user.setLastName(surname);
                    user.setEmailAddress(email);
                    getDataLayer().storeUser(user);
                    template.SaveAlert(name, surname, email, request, response, getServletContext());
                } else {
                        try {
                            template.ErrorWtiting(name, surname, email, request, response, getServletContext());
                        } catch (ServletException ex) {
                            Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
            } catch (ServletException ex) {
                Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                //Controllo correttezza non superato
                error.ErrorValidateEditProfile(name, surname, email, request, response, getServletContext());
            } catch (ServletException ex) {
                Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void action_EditPassword(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException {
        try {
            boolean cPassOld = false;
            String oldpass, pass1, pass2, ricPass, name, surname, email;
            pass1 = request.getParameter("password1");
            pass2 = request.getParameter("password2");
            oldpass = request.getParameter("oldpass");
            name = request.getParameter("name");
            surname = request.getParameter("surname");
            email = request.getParameter("email");
            cPass1 = Validate.ValidatePassword(pass1);
            cPass2 = Validate.ValidatePassword(pass2);
            cPassOld = Validate.ValidatePassword(oldpass);

            //Converto la password in ingresso in md5 per poi poterla confrontare con quella sul db
            String md5OldPass = null;
            MessageDigest mdEnc = MessageDigest.getInstance("MD5"); // Encryption algorithm
            mdEnc.update(oldpass.getBytes(), 0, oldpass.length());
            md5OldPass = new BigInteger(1, mdEnc.digest()).toString(16);

            //Converto la password in md5 per poi poterla salvare sul db
            String md5NewPass = null;
            MessageDigest New = MessageDigest.getInstance("MD5"); // Encryption algorithm
            New.update(pass1.getBytes(), 0, pass1.length());
            md5NewPass = new BigInteger(1, New.digest()).toString(16);

            //Prelevo la password dal db
            User user = getDataLayer().getUserByKey(userid, WRITE_ACCESS);
            ricPass = user.getPassword();
            if (cPass1 == true && cPass2 == true && cPassOld == true) {
                if (md5OldPass.equals(ricPass)) {
                    if (pass1.equals(pass2)) {
                        if(user.getAccessMode() == WRITE_ACCESS){
                        try {
                            //controllo terminato con successo
                            //salvo le modifiche
                            user.setPassword(md5NewPass);
                            getDataLayer().storeUser(user);
                            template.SaveAlert(name, surname, email, request, response, getServletContext());
                        } catch (ServletException ex) {
                            Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        } else {
                            try {
                                template.ErrorWtiting(name, surname, email, request, response, getServletContext());
                            } catch (ServletException ex) {
                                Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        //controllo delle due password irregolare
                        try {
                            //errore immissione password non coincidenti
                            error.ErrorValidateEditPassPass(name, surname, email, request, response, getServletContext());
                        } catch (ServletException ex) {
                            Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    try {
                        //errore immissione vecchia password errata
                        error.ErrorValidateEditOldPass(name, surname, email, request, response, getServletContext());
                    } catch (ServletException ex) {
                        Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else {
                //Immissione dati non corretta
                try {
                    //errore immissione dati
                    error.ErrorValidateEditPass(name, surname, email, request, response, getServletContext());
                } catch (ServletException ex) {
                    Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(profile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
