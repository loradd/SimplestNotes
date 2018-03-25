package it.univaq.f4i.iw.simplestnote.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.simplestnote.data.model.Note;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.LATEST_VERSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READ_ACCESS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.WRITE_ACCESS;
import it.univaq.f4i.iw.simplestnote.data.model.Tag;
import it.univaq.f4i.iw.simplestnote.view.CreateTemplate;
import it.univaq.f4i.iw.simplestnote.view.error;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Marco Di Natale, Lorenzo Addazi
 */
public class tag extends init {
    public final error error = new error();
    public final CreateTemplate template=new CreateTemplate();
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws it.univaq.f4i.iw.framework.data.DataLayerException
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
            HttpSession session = request.getSession(false);
            int userid =(int) session.getAttribute("userid");
            if(null != request.getParameter("action")){
                switch (request.getParameter("action")){
                        //Aggiungi tag ad una nota
                        case "AddTag":
                            action_AddTag(userid,request,response);
                            break;
                        //Rimuovi tag da una nota    
                        case "RemoveTag":
                            action_RemoveTagNote(userid,request,response);
                            break;   
                        //Elimina tag dalla lista dei tag    
                        case "Yes":
                            action_RemoveTagList(userid,request,response);
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
                
    }
    private void action_AddTag(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {

            boolean validateInput=false;
            String tagContent= request.getParameter("tag");
            validateInput=Validate.ValidateString(tagContent);
            String name= request.getParameter("name");

            int idNote = Integer.parseInt(request.getParameter("note-id"));

            if (validateInput==true){
                response.setContentType("text/html;charset=UTF-8");

                /* INSERIMENTO TAG NEL DATABASE */
                Tag tag = getDataLayer().createTag();
                boolean isExistingTag = false;
                Note note = getDataLayer().getNoteByKey(idNote, LATEST_VERSION, WRITE_ACCESS); // prendo la nota dal database in scrittura
                List<Tag> userTags = getDataLayer().getTagsFromUser(getDataLayer().getUserByKey(userid, READ_ACCESS), READ_ACCESS);
                for(Tag noteTag : userTags){ // per ogni tag della nota
                    if(noteTag.getContent().equals(tagContent)){ // se il contenuto coincide con quello del tag in inserimento
                        tag = noteTag;
                        isExistingTag = true; // il tag esiste 
                        break; // mi fermo
                    }
                }
                if(note.getAccessMode() == WRITE_ACCESS){
                    if(!isExistingTag){ // se il tag non esiste
                         // creo un nuovo tag
                        tag.setContent(tagContent); // imposto il contenuto inserito da utente
                        tag = getDataLayer().getTagByKey(getDataLayer().storeTag(tag), READ_ACCESS); // inserisco il tag nel database e prendo la chiave generata
                        note.addTag(tag); // aggiungo il tag alla nota 
                    } else {
                        note.addTag(tag);
                    }
                    getDataLayer().storeNote(note); // salvo la nota 
                    response.sendRedirect("logged");
                } else {
                    response.sendRedirect("logged?note=errorWriting");  
                }
            } else {
                response.sendRedirect("logged?note=isAddTagError&name="+ name +"&noteid=" + idNote);
            }
        }    
    private void action_RemoveTagNote(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {
            int idNote = Integer.parseInt(request.getParameter("note-id"));
            int idTag = Integer.parseInt(request.getParameter("Tag-id"));

            /* ELIMINAZIONE DEL TAG DAL DATABASE */
            Note note = getDataLayer().getNoteByKey(idNote, LATEST_VERSION, WRITE_ACCESS); // prendo la nota dal database
            Tag tag = getDataLayer().getTagByKey(idTag, READ_ACCESS);
            if(note.getAccessMode() == WRITE_ACCESS){
            note.deleteTag(tag); // cancello il tag relativo del database
            getDataLayer().storeNote(note); // salvo nota 
            response.sendRedirect("logged");
            } else {
               response.sendRedirect("logged?note=errorWriting");  
            }
        } 
    
    /* da controllare */
    private void action_RemoveTagList(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {   
            int idTag = Integer.parseInt(request.getParameter("Tag-id"));
            /* ELIMINAZIONE DEL TAG DA TUTTE LE NOTE */
            Tag tag = getDataLayer().getTagByKey(idTag, READ_ACCESS);
            List<Note> tagNotes = getDataLayer().getNotesFromTag(tag, WRITE_ACCESS);
            for(Note tagNote : tagNotes){ // per ogni nota associata al tag
                if(tagNote.getAccessMode() == WRITE_ACCESS){
                    tagNote.deleteTag(tag); // cancello il tag dalla nota
                } else {
                     response.sendRedirect("logged?note=errorWriting");
                }
            }
            for(Note tagNote : tagNotes){
                getDataLayer().storeNote(tagNote);
            }
            getDataLayer().deleteTag(tag); // cancello il tag
            response.sendRedirect("logged");
        } 
    
}
