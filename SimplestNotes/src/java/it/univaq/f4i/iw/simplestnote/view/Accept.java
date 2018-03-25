package it.univaq.f4i.iw.simplestnote.view;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.simplestnote.controller.init;
import it.univaq.f4i.iw.simplestnote.data.model.Note;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.LATEST_VERSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READONLY_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READWRITE_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READ_ACCESS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.WRITE_ACCESS;
import it.univaq.f4i.iw.simplestnote.data.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Marco
 */
public class Accept extends init {
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        
        HttpSession session = request.getSession(false);
        int userid =(int) session.getAttribute("userid");
        switch (request.getParameter("action")){           
            case "Accept":
                action_Accept(userid, request, response);
                break;
            case "Declined":
                action_Declined(userid, request, response);
                break;
        }
    }
    
private void action_Accept(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException {

        int idNote = Integer.parseInt(request.getParameter("note-id"));
        Note note = getDataLayer().getNoteByKey(idNote, LATEST_VERSION, WRITE_ACCESS);
        if(note.getAccessMode() == WRITE_ACCESS){
            User user = getDataLayer().getUserByKey(userid, READ_ACCESS);
            int userPermission = getDataLayer().getUserNotePermission(user, note);
            if(userPermission == READONLY_PERMISSION){
                note.addReadOnlyUser(user);
            } else if(userPermission == READWRITE_PERMISSION){
                note.addReadWriteUser(user);
            }
            getDataLayer().storeNote(note);
            response.sendRedirect("logged");
        } else {
             response.sendRedirect("logged?note=errorWriting");
        }
    }   

private void action_Declined(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException {

        int idNote = Integer.parseInt(request.getParameter("note-id"));
        Note note = getDataLayer().getNoteByKey(idNote, LATEST_VERSION, WRITE_ACCESS);
        if(note.getAccessMode() == WRITE_ACCESS){
            User user = getDataLayer().getUserByKey(userid, READ_ACCESS);
            note.deletePendingUser(user);
            getDataLayer().storeNote(note);
            response.sendRedirect("logged");
        } else {
             response.sendRedirect("logged?note=errorWriting");
        }
    } 
}
