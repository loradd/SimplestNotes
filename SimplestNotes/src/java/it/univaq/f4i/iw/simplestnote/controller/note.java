package it.univaq.f4i.iw.simplestnote.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import static it.univaq.f4i.iw.simplestnote.data.impl.utilities.NoteUtilities.getDateTitle;
import it.univaq.f4i.iw.simplestnote.data.model.Note;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.ALL_PERMISSIONS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.ALL_STATUS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.CREATOR_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.LATEST_VERSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READONLY_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READWRITE_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READ_ACCESS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.WRITE_ACCESS;
import it.univaq.f4i.iw.simplestnote.data.model.User;
import it.univaq.f4i.iw.simplestnote.mail.MailUtility;
import it.univaq.f4i.iw.simplestnote.view.CreateTemplate;
import it.univaq.f4i.iw.simplestnote.view.error;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Marco
 */
public class note extends init {
    
    protected ServletContext context;
    private final error error = new error();
    private final CreateTemplate template = new CreateTemplate();
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        int userid=0;
        HttpSession s = SecurityLayer.checkSession(request);
        if (s != null) {
            HttpSession session = request.getSession(false);
            userid = (int) session.getAttribute("userid");
       
        String name=null;
        if((request.getParameter("name"))!=null){
            name = request.getParameter("name");
        }
        //Id nota presente nella lista delle note
        //da non confondere con id-note1 e id-note2 che fanno parte degli editor
        int idNote = 0;
        if(request.getParameter("note-id") != null){
            idNote = Integer.parseInt(request.getParameter("note-id"));
        }
        
        if(null != request.getParameter("note")){
        switch (request.getParameter("note")){    
            case "ShowNote":
                action_ShowEditor(request, response);
                break;  
            case "ShowHistory":
                action_Edit(userid,request, response);
                break;                 
            case "Restore":
                action_RestoreNote(userid,request, response);
                break;                
            case "RemoveUser":
                action_RemoveUser(userid, request, response);
                break;                
            case "Invite":
                action_Invite(userid, request, response);
                break;                  
            case "Edit":
                action_Edit(userid,request,response);
                break;  
            case "View":
                action_Edit(userid,request,response);
                break;                 
            case "Confirm":
                action_RemoveNote(userid,request,response);
                break;                       
            case "back":
                response.sendRedirect("core");
                break;                      
            default:
                response.sendRedirect("core");
                break;
        }   
        }else{
            response.sendRedirect("core");
        }
        
    }else{
        response.sendRedirect("core");
        }
    } 
    
private void action_ShowEditor(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

    String state1, state2, title1, title2, editor1, editor2,acces1,acces2;
    state1 = (String) getServletContext().getAttribute("state1");
    state2 = (String) getServletContext().getAttribute("state2");
    Object idNote1 =  getServletContext().getAttribute("id-note1");
    Object idNote2 =  getServletContext().getAttribute("id-note2");
    title1 = (String) getServletContext().getAttribute("title1");
    title2 = (String) getServletContext().getAttribute("title2");
    editor1 = (String) getServletContext().getAttribute("editor1"); 
    editor2 = (String) getServletContext().getAttribute("editor2"); 
    acces1 = (String) getServletContext().getAttribute("acces1"); 
    acces2 = (String) getServletContext().getAttribute("acces2");     
    template.CreateEditor(idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response, getServletContext());

}    

private void action_Edit(int userid,HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {
   
    String state1, state2, acces1, acces2;
    int idNote = 0;
    
    if((getServletContext().getAttribute("state1"))!=null){
        state1= (String) getServletContext().getAttribute("state1");
    } else {
        getServletContext().setAttribute("state1","close");  
        state1= (String) getServletContext().getAttribute("state1");
    }
    
    if((getServletContext().getAttribute("acces1"))!=null){
        acces1= (String) getServletContext().getAttribute("acces1");
    } else {
        getServletContext().setAttribute("acces1","readOnly");  
        acces1 = (String) getServletContext().getAttribute("acces1");
    }    
    
    if((getServletContext().getAttribute("state2"))!=null){
        state2= (String) getServletContext().getAttribute("state2");
    } else {
        getServletContext().setAttribute("state2","close");  
        state2= (String) getServletContext().getAttribute("state2");
    }
    
    if((getServletContext().getAttribute("acces2"))!=null){
        acces1= (String) getServletContext().getAttribute("acces2");
    } else {
        getServletContext().setAttribute("acces2","readOnly");  
        acces1= (String) getServletContext().getAttribute("acces2");
    }      
    
    //prelevo l'id della nota che voglio editare
    if(request.getParameter("note-id") != null){
        idNote = Integer.parseInt(request.getParameter("note-id"));
    }else{
        idNote=0;
    }
    /* RIEMPIMENTO TEXTAREA DA CONTENUTO FILE SYSTEM */
    String editor = "";
    String title = request.getParameter("name");
    
    /*Richiamo i permessi dell'utene e setto la variabile access*/
    String acces = request.getParameter("access");
    if(idNote != 0){ // id nota esistente nel database 
        //Mi chiedo se sto leggendo un elemento dalla cronologia di una nota
        Note note;
        if(request.getParameter("version") != null){
            //Devo richiamare la nota in lettura della versione specificata
            note = getDataLayer().getNoteByKey(idNote, Integer.parseInt(request.getParameter("version")), READ_ACCESS);
            acces = "readOnly";
        } else {
            //Devo richiamare la nota più aggiornata
            note = getDataLayer().getNoteByKey(idNote, LATEST_VERSION, READ_ACCESS);
            User user = getDataLayer().getUserByKey(userid, READ_ACCESS);
            int userNotePermission = getDataLayer().getUserNotePermission(user, note);
            if(userNotePermission == READWRITE_PERMISSION || userNotePermission == CREATOR_PERMISSION){
                acces = "readWrite";
            } else {
                acces = "readOnly";
            }             
        }
        

        editor = note.getContentFile();
    }
    Object idN,id1,id2; 
    idN=0;
    if(request.getParameter("note-id")!=null){
        idN= request.getParameter("note-id");
    }
    if(idNote==0){
        idN=-1;
    }
    id1= getServletContext().getAttribute("id-note1");
    id2= getServletContext().getAttribute("id-note2");

    //Verifico se una nota è già aperta nell'editor
    if(idN.equals(id1) || idN.equals(id2)){
        action_ShowEditor(request, response);
    } else {
       
        
    //L'editor 1  libero lo uso
    if( state1.equals("close")) {
        if(request.getParameter("note-id")!=null){
            if(acces.equals("readOnly")){
                getServletContext().setAttribute("acces1","readOnly");
            } else {
                getServletContext().setAttribute("acces1","readWrite");
            }
            getServletContext().setAttribute("state1","open");
            getServletContext().setAttribute("id-note1",idNote);
            getServletContext().setAttribute("title1",title);
            getServletContext().setAttribute("editor1", editor);

        } else {
            getServletContext().setAttribute("state1","open");
            getServletContext().setAttribute("id-note1", 0);
            getServletContext().setAttribute("title1", getDateTitle());
            getServletContext().setAttribute("editor1", "");  
            getServletContext().setAttribute("acces1","readWrite");
        }
        
        Object idNote1= getServletContext().getAttribute("id-note1");
        String title1= (String) getServletContext().getAttribute("title1");
        String editor1 =(String) getServletContext().getAttribute("editor1");
        state1 =(String) getServletContext().getAttribute("state1");
        acces1 = (String) getServletContext().getAttribute("acces1");

        Object idNote2= getServletContext().getAttribute("id-note2");
        String title2= (String) getServletContext().getAttribute("title2");
        String editor2 =(String) getServletContext().getAttribute("editor2");
        state2 =(String) getServletContext().getAttribute("state2"); 
        acces2 = (String) getServletContext().getAttribute("acces2");      
        template.CreateEditor(idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response, getServletContext());
        
    } else {
        
           if(state2.equals("close")){
               if(request.getParameter("note-id")!=null){
                  
                    if(acces.equals("readOnly")){
                        getServletContext().setAttribute("acces2","readOnly");
                    }else{
                        getServletContext().setAttribute("acces2","readWrite");
                    }                   
                   
                 getServletContext().setAttribute("state2","open");
                 getServletContext().setAttribute("id-note2",idNote);
                 getServletContext().setAttribute("title2",title);
                 getServletContext().setAttribute("editor2",editor);
                    
               } else {
                 getServletContext().setAttribute("state2","open");
                 getServletContext().setAttribute("id-note2",0);
                 getServletContext().setAttribute("title2", getDateTitle());
                 getServletContext().setAttribute("editor2",""); 
                 getServletContext().setAttribute("acces2","readWrite");
               }
               
                 Object idNote2 = getServletContext().getAttribute("id-note2");
                 String title2= (String) getServletContext().getAttribute("title2");
                 String editor2 =(String) getServletContext().getAttribute("editor2");
                 state2 =(String) getServletContext().getAttribute("state2");
                 acces2 = (String) getServletContext().getAttribute("acces2");
                 
                 Object idNote1=  getServletContext().getAttribute("id-note1");
                 String title1= (String) getServletContext().getAttribute("title1");
                 String editor1 =(String) getServletContext().getAttribute("editor1");
                 state1 =(String) getServletContext().getAttribute("state1"); 
                 acces1 = (String) getServletContext().getAttribute("acces1");
                 
                 template.CreateEditor(idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response, getServletContext());                     
           } else {
                    //Seganalo all'utente che non piu editare piu di due testi
                    response.sendRedirect("logged?note=tooManyNotes");
                 }
         }
    }
        
}

private void action_RestoreNote(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {
            
        String name = request.getParameter("name");
        int noteId = Integer.parseInt(request.getParameter("note-id"));
        int noteVersion = Integer.parseInt(request.getParameter("version"));
        User user = getDataLayer().getUserByKey(userid, READ_ACCESS);
        Note note = getDataLayer().getNoteByKey(noteId, LATEST_VERSION, WRITE_ACCESS);
        if(note.getAccessMode() == WRITE_ACCESS){
            int userNotePermission = getDataLayer().getUserNotePermission(user, note);
            if(userNotePermission == READWRITE_PERMISSION || userNotePermission == CREATOR_PERMISSION){
                note.rollbackToPreviousVersion(noteVersion);
                Note deleteNote = getDataLayer().getNoteByKey(noteId, noteVersion, WRITE_ACCESS); 
                getDataLayer().deleteNoteVersion(deleteNote); 
                note.setContentPath(note.getContentPath());
                getDataLayer().storeNote(note);  
            } 
            response.sendRedirect("logged?note=History&note-id="+ note.getId() +"&note-version=" + note.getVersion());
        } else {
            response.sendRedirect("logged?note=errorWriting");  
        }
       
    }


private void action_RemoveUser(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {
           
        int idNote = Integer.parseInt(request.getParameter("note-id"));
        String email = request.getParameter("email");
        //Posso procedere con l'eliminazione dell'utente dalla nota
        Note note = getDataLayer().getNoteByKey(idNote, LATEST_VERSION, WRITE_ACCESS);
        if(note.getAccessMode() == WRITE_ACCESS){
            List<User> usersFromEmailAddress;
            User user = ((usersFromEmailAddress = getDataLayer().getUsersFromEmailAddress(email, READ_ACCESS)).isEmpty())? null : usersFromEmailAddress.get(0);
            int userPermission = getDataLayer().getUserNotePermission(user, note);
            if(getDataLayer().getUserNotePermission(getDataLayer().getUserByKey(userid, READ_ACCESS), note) == CREATOR_PERMISSION){
               if(userPermission == READONLY_PERMISSION){
                if(!note.getReadOnlyUsers(READ_ACCESS).isEmpty()){
                    note.deleteReadOnlyUser(user);
                }
                getDataLayer().storeNote(note);
                } else if(userPermission == READWRITE_PERMISSION){
                    if(!note.getReadWriteUsers(READ_ACCESS).isEmpty()){
                        note.deleteReadWriteUser(user);
                    }
                    getDataLayer().storeNote(note);
                } else if(userPermission == CREATOR_PERMISSION){
                    getDataLayer().deleteNote(note);
                } 
            }
            //Aggiorno la pagina per mostrare l'eliminazione dell'utente
            response.sendRedirect("logged?note=UserList&note-id=" + idNote + "&note-version=" + note.getVersion());
        } else {
           response.sendRedirect("logged?note=errorWriting");  
        }
     
    }


    private void action_RemoveNote(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {

            int idNote = Integer.parseInt(request.getParameter("note-id"));
            Note note = getDataLayer().getNoteByKey(idNote, LATEST_VERSION, WRITE_ACCESS);
            int userPermission = getDataLayer().getUserNotePermission(getDataLayer().getUserByKey(userid, READ_ACCESS), note);
            if(note.getAccessMode() == WRITE_ACCESS){
                if(userPermission == CREATOR_PERMISSION){
                    getDataLayer().deleteNote(note);
                    response.sendRedirect("logged");
                } else if(userPermission == READWRITE_PERMISSION){
                    User user = getDataLayer().getUserByKey(userid, READ_ACCESS);
                    note.deleteReadWriteUser(user);
                    getDataLayer().storeNote(note);
                    response.sendRedirect("logged");
                } else if(userPermission == READONLY_PERMISSION){
                    User user = getDataLayer().getUserByKey(userid, READ_ACCESS);
                    note.deleteReadOnlyUser(user);
                    getDataLayer().storeNote(note);
                    response.sendRedirect("logged");
                }
            } else {
               response.sendRedirect("logged?note=errorWriting");  
            }

    }

    private void action_Invite(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {
        
        String userInvited = request.getParameter("Email");
        String name = request.getParameter("name");
        String permission = request.getParameter("permission");
        int idNote = Integer.parseInt(request.getParameter("note-id"));
        Note note = getDataLayer().getNoteByKey(idNote, LATEST_VERSION, READ_ACCESS);
        if(getDataLayer().getUserNotePermission(getDataLayer().getUserByKey(userid, READ_ACCESS), note) != READONLY_PERMISSION ){
            List<User> usersFromEmailAddress = getDataLayer().getUsersFromEmailAddress(userInvited, READ_ACCESS);
            List<User> joiningUsers = getDataLayer().getUsersFromNote(note, ALL_PERMISSIONS, ALL_STATUS, READ_ACCESS);
            boolean invalidEmailAddress = false;
            if(!usersFromEmailAddress.isEmpty()){
                if(userInvited.equals(getDataLayer().getUserByKey(userid, READ_ACCESS).getEmailAddress())){
                    invalidEmailAddress = true;
                } else {
                    for(User joiningUser : joiningUsers){
                        if(joiningUser.getEmailAddress().equals(userInvited) && (getDataLayer().getUserNotePermission(joiningUser, note) == CREATOR_PERMISSION)){ 
                            invalidEmailAddress = true;                    
                        }
                    }
                }
                if(Validate.ValidateMail(userInvited) && !invalidEmailAddress){
                    response.setContentType("text/html;charset=UTF-8");
                    note = getDataLayer().getNoteByKey(idNote, LATEST_VERSION, WRITE_ACCESS);
                    if(note.getAccessMode() == WRITE_ACCESS){
                        List<User> users = getDataLayer().getUsersFromEmailAddress(userInvited, READ_ACCESS);
                        for(User userFromEmailAddress : users){
                            if(userFromEmailAddress.getEmailAddress().equals(userInvited)){
                                switch (permission) {
                                    case "readwrite":
                                        note.addPendingUser(getDataLayer().getUserByKey(userFromEmailAddress.getId(), READ_ACCESS), READWRITE_PERMISSION);
                                        break;
                                    case "readonly":
                                        note.addPendingUser(getDataLayer().getUserByKey(userFromEmailAddress.getId(), READ_ACCESS), READONLY_PERMISSION);
                                        break;
                                }
                            }
                        }
                        getDataLayer().storeNote(note);
                        /*Procedo all'invio della notifica all'utente*/
                            String mail = request.getParameter("email");
                            String message = "Hello ,<br>"+(String) getServletContext().getAttribute("user")+" shared a note with you!!! Log into <a href=\"http://simplenotes.it\" target=\"_blank\">SimpleNotes.it</a><br>\n" +
                                             "<br><br><br>\n" +
                                             "Cheers<br>SimplestNotes Team ;)";
                        try {
                            MailUtility.sendMail(userInvited, message);
                        } catch (MessagingException ex) {
                            Logger.getLogger(note.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        response.sendRedirect("logged");
                    } else {
                       response.sendRedirect("logged?note=errorWriting");  
                    }
                } else {
                    response.sendRedirect("logged?note=errorShared&name="+name+"&note-id="+idNote);
                }
            } else {
                response.sendRedirect("logged?note=inviteSimplestNote&email="+userInvited+"&by="+ getServletContext().getAttribute("user"));
            }
        } else {
            response.sendRedirect("logged");
        }
    }
}
