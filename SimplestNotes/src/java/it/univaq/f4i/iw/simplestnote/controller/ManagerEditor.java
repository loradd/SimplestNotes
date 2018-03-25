package it.univaq.f4i.iw.simplestnote.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import static it.univaq.f4i.iw.simplestnote.data.impl.utilities.NoteUtilities.getDateTitle;
import it.univaq.f4i.iw.simplestnote.data.model.Note;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.CREATOR_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.LATEST_VERSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READWRITE_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READ_ACCESS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.WRITE_ACCESS;
import it.univaq.f4i.iw.simplestnote.data.model.User;
import it.univaq.f4i.iw.simplestnote.view.CreateTemplate;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Marco
 */
public class ManagerEditor extends note {

    private final CreateTemplate template;

    public ManagerEditor() {
        this.template = new CreateTemplate();
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        HttpSession session = request.getSession(false);
        int userid = (int) session.getAttribute("userid");

        //////////START EDITOR 1//////////
        getServletContext().setAttribute("state1", request.getParameter("state1"));
        getServletContext().setAttribute("id-note1", request.getParameter("id-note1"));
        getServletContext().setAttribute("title1", request.getParameter("title1"));
        getServletContext().setAttribute("editor1", request.getParameter("editor1"));
        String state1 = (String) getServletContext().getAttribute("state1");
        int idNote1 = Integer.parseInt((String) getServletContext().getAttribute("id-note1"));
        String title1 = (String) getServletContext().getAttribute("title1");
        String editor1 = (String) getServletContext().getAttribute("editor1");
        String acces1 = (String) getServletContext().getAttribute("acces1");
        //////////END EDITOR 1//////////            
        //////////START EDITOR 2//////////
        getServletContext().setAttribute("state2", request.getParameter("state2"));
        getServletContext().setAttribute("id-note2", request.getParameter("id-note2"));
        getServletContext().setAttribute("title2", request.getParameter("title2"));
        getServletContext().setAttribute("editor2", request.getParameter("editor2"));
        String state2 = (String) getServletContext().getAttribute("state2");
        int idNote2 = Integer.parseInt((String) getServletContext().getAttribute("id-note2"));
        String title2 = (String) getServletContext().getAttribute("title2");
        String editor2 = (String) getServletContext().getAttribute("editor2");
        String acces2 = (String) getServletContext().getAttribute("acces2");
        //////////END EDITOR 2//////////            

        
            switch (request.getParameter("note")) {
                case "Home":
                    response.sendRedirect("logged");
                    break;
                case "Profile":
                    response.sendRedirect("profile?profile=Profile");
                    break;                    
                case "New1":
                    action_NewEditor1(userid, idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response);
                    break;
                case "New2":
                    action_NewEditor2(userid, idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response);
                    break;
                case "Save1":
                    action_SaveEditor1(userid, idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response);
                    break;
                case "Save2":
                    action_SaveEditor2(userid, idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response);
                    break;
                case "Close1":
                    action_CloseEditor1(userid, idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response);
                    break;
                case "Close2":
                    action_CloseEditor2(userid, idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response);
                    break;
                default:
                    response.sendRedirect("core");
                    break;
            }
    }

    private void action_NewEditor1(int userid, int idNote1, int idNote2, String acces1, String acces2, String state1, String state2, String title1, String editor1, String title2, String editor2, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        getServletContext().setAttribute("state1", "open");
        getServletContext().setAttribute("id-note1", 0);
        getServletContext().setAttribute("title1", getDateTitle());
        getServletContext().setAttribute("editor1", "");
        getServletContext().setAttribute("acces1","readWrite");
        state1 = "open";
        title1 = getDateTitle();
        idNote1 = 0;
        editor1 = "";
        acces2="readWrite";        
        template.CreateEditor(idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response, getServletContext());
        
    }

    private void action_NewEditor2(int userid, int idNote1, int idNote2, String acces1, String acces2, String state1, String state2, String title1, String editor1, String title2, String editor2, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        getServletContext().setAttribute("state2", "open");
        getServletContext().setAttribute("id-note2", 0);
        getServletContext().setAttribute("title2", getDateTitle());
        getServletContext().setAttribute("editor2", "");
        getServletContext().setAttribute("acces2","readWrite");
        state2 = "open";
        title2 = getDateTitle();
        idNote2 = 0;
        editor2 = "";
        acces2="readWrite";
        template.CreateEditor(idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response, getServletContext());
        
    }

    private void action_SaveEditor1(int userid, int idNote1, int idNote2, String acces1, String acces2, String state1, String state2, String title1, String editor1, String title2, String editor2, HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException, ServletException {
        boolean validateTitle;
        Note note;
        
        if (idNote1 == 0){
            User user = getDataLayer().getUserByKey(userid, READ_ACCESS);
            //Controllo terminato con valore 0 procediamo alla creazione di una nuova nota
            note = getDataLayer().createNote();
            //Verifico se il titolo è una stringa valida 
            //Se questa è valida inserisco il titolo immesso dall'utente
            //Altrimenti ne inserisco io uno di default
            if(Validate.ValidateString(title1)){
                note.setTitle(title1);
            } else {
                note.setTitle(getDateTitle());
            }
            //Fine controllo immissione titolo
            note.setContentPath(getServletContext().getRealPath("/") + "../../users/");
            note.setContentFile(editor1);
            note.setCreatorUser(user);
            getDataLayer().storeNote(note);
            getServletContext().setAttribute("id-note1", note.getId());
        } else {
        //Ho il permesso in scrittura Sono un utente ReadWrite reale 
            note = getDataLayer().getNoteByKey(idNote1, LATEST_VERSION, WRITE_ACCESS);
            if(note.getAccessMode() == WRITE_ACCESS){
                int userNotePermission = getDataLayer().getUserNotePermission(getDataLayer().getUserByKey(userid, READ_ACCESS), note);
                if(userNotePermission == READWRITE_PERMISSION || userNotePermission == CREATOR_PERMISSION){
                    if(Validate.ValidateString(title1)){
                        note.setTitle(title1);
                    } else {
                        note.setTitle(getDateTitle());
                    }
                    note.setContentFile(editor1);
                    getDataLayer().storeNote(note);
                    getServletContext().setAttribute("write", "save");
                }
            } else {
                getServletContext().setAttribute("write", "error");
            }
        }
        template.CreateEditor(idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response, getServletContext());
    }

    private void action_SaveEditor2(int userid, int idNote1, int idNote2, String acces1, String acces2, String state1, String state2, String title1, String editor1, String title2, String editor2, HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException, ServletException {
        boolean validateTitle;
        validateTitle = Validate.ValidateString(title2);
        Note note;
        if (idNote2 == 0) {
            User user = getDataLayer().getUserByKey(userid, READ_ACCESS);
            //Controllo terminato con valore 0 procediamo alla creazione di una nuova nota
            note = getDataLayer().createNote();
            //Verifico se il titolo è una stringa valida 
            //Se questa è valida inserisco il titolo immesso dall'utente
            //Altrimenti ne inserisco io uno di default            
            if(validateTitle == true){
                note.setTitle(title2);
            } else {
                note.setTitle(getDateTitle());
            }
            //Fine controllo immissione titolo
            note.setContentPath(getServletContext().getRealPath("/") + "../../users/");
            note.setContentFile(editor2);
            note.setCreatorUser(user);
            getDataLayer().storeNote(note);
            getServletContext().setAttribute("id-note2", note.getId());
        } else {
        //Ho il permesso in scrittura Sono un utente ReadWrite reale 
            note = getDataLayer().getNoteByKey(idNote2, LATEST_VERSION, WRITE_ACCESS);
            if(note.getAccessMode() == WRITE_ACCESS){
            int userNotePermission = getDataLayer().getUserNotePermission(getDataLayer().getUserByKey(userid, READ_ACCESS), note);
            if(userNotePermission == READWRITE_PERMISSION || userNotePermission == CREATOR_PERMISSION){
                if(Validate.ValidateString(title2)){
                    note.setTitle(title2);
                } else {
                    note.setTitle(getDateTitle());
                }
                note.setContentFile(editor2);
                getDataLayer().storeNote(note);
                getServletContext().setAttribute("write", "save");
            }
            } else {
                getServletContext().setAttribute("write", "error");
            }
        }
        template.CreateEditor(idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response, getServletContext());
    }

    private void action_CloseEditor1(int userid, Object idNote1, Object idNote2, String acces1, String acces2, String state1, String state2, String title1, String editor1, String title2, String editor2, HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException, ServletException {

        //////////START CLOSE EDITOR 1//////////
        getServletContext().setAttribute("state1", "close");
        getServletContext().setAttribute("id-note1", 0);
        getServletContext().setAttribute("title1", "");
        getServletContext().setAttribute("editor1", "");
        getServletContext().setAttribute("acces1", "readWrite");
        state1 = "close";
        idNote1 = 0;
        title1 = "";
        editor1 = "";
        acces1 = "readWrite";
        //////////END CLOSE EDITOR 1//////////
        if (state2.equals("close")) {
            response.sendRedirect("logged");
        } else {
            template.CreateEditor(idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response, getServletContext());
        }

    }

    private void action_CloseEditor2(int userid, Object idNote1, Object idNote2, String acces1, String acces2, String state1, String state2, String title1, String editor1, String title2, String editor2, HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException, ServletException {

        //////////START CLOSE EDITOR 2//////////
        getServletContext().setAttribute("state2", "close");
        getServletContext().setAttribute("id-note2", 0);
        getServletContext().setAttribute("title2", "");
        getServletContext().setAttribute("editor2", "");
        getServletContext().setAttribute("acces2", "readWrite");
        state2 = "close";
        idNote2 = 0;
        title2 = "";
        editor2 = "";
        acces2 = "readWrite";
        //////////END CLOSE EDITOR 2//////////     
        if (state1.equals("close")) {
            response.sendRedirect("logged");
        } else {
            template.CreateEditor(idNote1, idNote2, acces1, acces2, state1, state2, title1, editor1, title2, editor2, request, response, getServletContext());
        }

    }

}
