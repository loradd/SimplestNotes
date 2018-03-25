package it.univaq.f4i.iw.simplestnote.view;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.simplestnote.controller.SecurityLayer;
import it.univaq.f4i.iw.simplestnote.controller.Validate;
import it.univaq.f4i.iw.simplestnote.controller.init;
import it.univaq.f4i.iw.simplestnote.data.model.Note;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.ALL_PERMISSIONS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.CONFIRMED_STATUS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.CREATOR_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.PENDING_STATUS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READONLY_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READWRITE_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READ_ACCESS;
import it.univaq.f4i.iw.simplestnote.data.model.Tag;
import it.univaq.f4i.iw.simplestnote.data.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Marco
 */
public class logged extends init {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {

        HttpSession s = SecurityLayer.checkSession(request);
        int userid = (int) s.getAttribute("userid");
        int noteId;
        int noteVersion;
        String inputName;
        String inputTag;

        String isAddTag = "0";
        String isAddTagError = "0";
        String isAddUser = "0";
        String isRemoveTag = "0";
        String isRemoveUser = "0";
        String isShowAcceptShared = "0";
        String isRestoreNote = "0";
        String isDeleteNote = "0";
        String isErrorShared = "0";
        String isTooManyNotes = "0";
        String isChangePermission = "0";
        String isInviteSimplestNote = "0";
        String isErrorWriting = "0";
        
        TemplateResult res = new TemplateResult(getServletContext());

        if (null == request.getParameter("note")) {
            if(null == getServletContext().getAttribute("flag")){
                getServletContext().setAttribute("isSearch", "0");
                getServletContext().setAttribute("isTagsList", "1");
                getServletContext().setAttribute("isUserList", "0");
                getServletContext().setAttribute("isNoteHistory", "0");
                getServletContext().setAttribute("flag", "1");
            }
        } else {
            switch (request.getParameter("note")) {
                case "UserList":
                    getServletContext().setAttribute("noteId", Integer.parseInt(request.getParameter("note-id")));
                    getServletContext().setAttribute("noteVersion", Integer.parseInt(request.getParameter("note-version")));
                    getServletContext().setAttribute("isUsersList", "1");
                    getServletContext().setAttribute("isTagsList", "0");
                    getServletContext().setAttribute("isNoteHistory", "0");
                    break;
                case "History":
                    getServletContext().setAttribute("noteId", Integer.parseInt(request.getParameter("note-id")));
                    getServletContext().setAttribute("noteVersion", Integer.parseInt(request.getParameter("note-version")));
                    getServletContext().setAttribute("isUsersList", "0");
                    getServletContext().setAttribute("isTagsList", "0");
                    getServletContext().setAttribute("isNoteHistory", "1");
                    break;
                case "TagList":
                    getServletContext().setAttribute("isUsersList", "0");
                    getServletContext().setAttribute("isTagsList", "1"); 
                    getServletContext().setAttribute("isNoteHistory", "0");
                    break;
                case "Search":
                    getServletContext().setAttribute("inputName", request.getParameter("NameInput"));
                    getServletContext().setAttribute("inputTag", request.getParameter("TagInput"));
                    getServletContext().setAttribute("isSearch", "1"); 
                    break;
                case "StopSearch":
                    getServletContext().setAttribute("isSearch", "0"); 
                    break;
                case "AddTag":
                    isAddTag = "1";
                    break;
                case "AddUser":
                    isAddUser = "1";
                    break;
                case "Delete" :
                    isDeleteNote = "1";
                    break;
                case  "RemoveTag":
                    isRemoveTag = "1";
                    break;
                case  "RemoveUser":
                    isRemoveUser = "1";
                    break;
                case "View":
                    isShowAcceptShared = "1";
                    break;
                case "RestoreNote":
                    isRestoreNote = "1";
                    break;
                case "errorShared":
                    isErrorShared = "1";
                    break;
                case "isAddTagError":
                    isAddTagError = "1";
                    break;
                case "tooManyNotes" :
                    isTooManyNotes = "1";
                    break;
                case "changePermission":
                    isChangePermission = "1";
                    break;
                case "inviteSimplestNote":
                    isInviteSimplestNote = "1";
                    break;     
                case "errorWriting":
                    isErrorWriting = "1";
                    break;                      
            }
        } 

        String isSearch = (String) ((getServletContext().getAttribute("isSearch") == null) ? "0" : getServletContext().getAttribute("isSearch"));
        String isTagsList = (String) ((getServletContext().getAttribute("isTagsList") == null) ? "0" : getServletContext().getAttribute("isTagsList"));
        String isUsersList = (String) ((getServletContext().getAttribute("isUsersList") == null) ? "0" : getServletContext().getAttribute("isUsersList"));
        String isNoteHistory = (String) ((getServletContext().getAttribute("isNoteHistory") == null) ? "0" : getServletContext().getAttribute("isNoteHistory"));

        User user = getDataLayer().getUserByKey(userid, READ_ACCESS);
        List<Note> createdNotes = new ArrayList<Note>();
        List<Note> readWriteNotes = new ArrayList<Note>();
        List<Note> readOnlyNotes = new ArrayList<Note>();
        List<Note> pendingNotes = new ArrayList<Note>();
        
        if ("1".equals(isSearch)) {
            inputName = (String) getServletContext().getAttribute("inputName");
            inputTag = (String) getServletContext().getAttribute("inputTag");
            boolean inN = Validate.ValidateSearch(inputName);
            boolean inT = Validate.ValidateSearch(inputTag);
            List<Note> result = new ArrayList<Note>();
            List<Note> notesFromTitle = new ArrayList<Note>();
            List<Tag> tagsFromContent = new ArrayList<Tag>();
            List<Note> notesFromSingleTag = new ArrayList<Note>();
            List<Note> notesFromTags = new ArrayList<Note>();
              
            if (inN != false) {
                if (inT != false) {
                    notesFromTitle = getDataLayer().getNotesFromTitle(inputName, READ_ACCESS);
                    if (!notesFromTitle.isEmpty()) {
                        tagsFromContent = getDataLayer().getTagsFromContent(inputTag, READ_ACCESS);
                        for (Tag tag : tagsFromContent) {
                            notesFromSingleTag = getDataLayer().getNotesFromTag(tag, READ_ACCESS);
                            if (!notesFromSingleTag.isEmpty()) {
                                notesFromTags.addAll(notesFromSingleTag);
                            }
                        }
                        for (Note note : notesFromTags) {
                            for (Note anote : notesFromTitle) {
                                if (note.getId() == anote.getId()) {
                                    if (!result.contains(note)) {
                                        result.add(note);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    notesFromTitle = getDataLayer().getNotesFromTitle(inputName, READ_ACCESS);
                    for (Note note : notesFromTitle) {
                        if (!result.contains(note)) {
                            result.add(note);
                        }
                    }
                }
            } else {
                if (inT != false) {
                    tagsFromContent = getDataLayer().getTagsFromContent(inputTag, READ_ACCESS);
                    for (Tag tag : tagsFromContent) { 
                        notesFromSingleTag = getDataLayer().getNotesFromTag(tag, READ_ACCESS);
                        if (!notesFromSingleTag.isEmpty()) {
                            notesFromTags.addAll(notesFromSingleTag);
                        }
                    }
                    for (Note note : notesFromTags) {
                        if (!result.contains(note)) {
                            result.add(note);
                        }
                    }
                } else {
                    response.sendRedirect("logged?note=StopSearch");
                }
            }
            for (Note note : result) {
                int notepermission = getDataLayer().getUserNotePermission(user, note);
                if (notepermission == CREATOR_PERMISSION) {
                    createdNotes.add(note);
                } else if (notepermission == READONLY_PERMISSION) {
                    readOnlyNotes.add(note);
                } else if (notepermission == READWRITE_PERMISSION) {
                    readWriteNotes.add(note);
                }
            }
            
        } else {
            createdNotes = getDataLayer().getNotesFromUser(user, CREATOR_PERMISSION, CONFIRMED_STATUS, READ_ACCESS); // note create
            readOnlyNotes = getDataLayer().getNotesFromUser(user, READONLY_PERMISSION, CONFIRMED_STATUS, READ_ACCESS); // note sola lettura
            readWriteNotes = getDataLayer().getNotesFromUser(user, READWRITE_PERMISSION, CONFIRMED_STATUS, READ_ACCESS); // note scrittura/lettura
        }
        
        if("1".equals(isTagsList)){ 
            List<Tag> userTags = getDataLayer().getTagsFromUser(user, READ_ACCESS); 
            request.setAttribute("userTags", userTags);
        } else if("1".equals(isUsersList)){ 
            noteId = (int) getServletContext().getAttribute("noteId");
            noteVersion = (int) getServletContext().getAttribute("noteVersion");
            Note note = getDataLayer().getNoteByKey(noteId, noteVersion, READ_ACCESS);
            List<User> creatorUser = getDataLayer().getUsersFromNote(note, CREATOR_PERMISSION, CONFIRMED_STATUS, READ_ACCESS); 
            List<User> readOnlyUsers = getDataLayer().getUsersFromNote(note, READONLY_PERMISSION, CONFIRMED_STATUS, READ_ACCESS);
            List<User> readWriteUsers = getDataLayer().getUsersFromNote(note, READWRITE_PERMISSION, CONFIRMED_STATUS, READ_ACCESS);
            int userPermission = getDataLayer().getUserNotePermission(user, note);
            if(userPermission == CREATOR_PERMISSION ){
                creatorUser = new ArrayList<User>();
                request.setAttribute("deleteUser", "show");
            } else if(userPermission == READWRITE_PERMISSION){
                Iterator<User> readWriteUsersIterator = readWriteUsers.iterator();
                while(readWriteUsersIterator.hasNext()){
                    if(readWriteUsersIterator.next().getId() == user.getId()){
                        readWriteUsersIterator.remove();
                    }
                }
            } else if(userPermission == READONLY_PERMISSION){
                Iterator<User> readOnlyUsersIterator = readOnlyUsers.iterator();
                while(readOnlyUsersIterator.hasNext()){
                    if(readOnlyUsersIterator.next().getId() == user.getId()){
                        readOnlyUsersIterator.remove();
                    }
                }
            }
            request.setAttribute("noteId", noteId);
            request.setAttribute("creatorUser", creatorUser);
            request.setAttribute("readOnlyNoteUsers", readOnlyUsers);
            request.setAttribute("readWriteNotesUsers", readWriteUsers);
        } else if("1".equals(isNoteHistory)){
            noteId = (int) getServletContext().getAttribute("noteId");
            noteVersion = (int) getServletContext().getAttribute("noteVersion");
            Note note = getDataLayer().getNoteByKey(noteId, noteVersion, READ_ACCESS);
            List<Note> noteHistory = getDataLayer().getNoteHistory(note, READ_ACCESS);
            int userPermission = getDataLayer().getUserNotePermission(user, note);
            if(userPermission == CREATOR_PERMISSION || userPermission == READWRITE_PERMISSION){
                request.setAttribute("previousVersion", "show");
            } 
            request.setAttribute("history", noteHistory);
        }
        pendingNotes = getDataLayer().getNotesFromUser(user, ALL_PERMISSIONS, PENDING_STATUS, READ_ACCESS);
        request.setAttribute("pendingNotes", pendingNotes); 
        request.setAttribute("createdNotes", createdNotes);
        request.setAttribute("readOnlyNotes", readOnlyNotes);
        request.setAttribute("readWriteNotes", readWriteNotes);
        if("1".equals(isAddTag)){
            request.setAttribute("noteid", Integer.parseInt(request.getParameter("note-id")));
            request.setAttribute("name", request.getParameter("name"));
            res.PrivateActivate("showAddTag.ftl.html", "filter.ftl.html", "notes-list.ftl.html", "right-sidebar-list.ftl.html", request, response);
        } else if("1".equals(isAddUser)){
            request.setAttribute("name", request.getParameter("name"));
            request.setAttribute("noteid", Integer.parseInt(request.getParameter("note-id")));
            res.PrivateActivate("showSharedNote.ftl.html", "filter.ftl.html", "notes-list.ftl.html", "right-sidebar-list.ftl.html", request, response);
        } else if("1".equals(isDeleteNote)){ 
            request.setAttribute("name", request.getParameter("name"));
            request.setAttribute("noteid", Integer.parseInt(request.getParameter("note-id")));
            getServletContext().setAttribute("isNoteHistory", "0");
            getServletContext().setAttribute("isTagsList", "1");
            res.PrivateActivate("showDelete.ftl.html","filter.ftl.html", "notes-list.ftl.html","right-sidebar-list.ftl.html", request, response);
        } else if("1".equals(isRemoveTag)){
            request.setAttribute("name", request.getParameter("name"));
            request.setAttribute("tagid", Integer.parseInt(request.getParameter("tag-id")));
            request.setAttribute("notetag", Integer.parseInt(request.getParameter("tag-id")));
            res.PrivateActivate("showRemoveTag.ftl.html", "filter.ftl.html", "notes-list.ftl.html", "right-sidebar-list.ftl.html", request, response);
        } else if("1".equals(isRemoveUser)){
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("noteid", Integer.parseInt(request.getParameter("note-id")));
            res.PrivateActivate("showRemoveUser.ftl.html", "filter.ftl.html", "notes-list.ftl.html", "right-sidebar-list.ftl.html", request, response);
        } else if("1".equals(isShowAcceptShared)){
            request.setAttribute("noteid", Integer.parseInt(request.getParameter("note-id")));
            request.setAttribute("name", request.getParameter("name"));
            request.setAttribute("userShared", request.getParameter("userShared"));
            res.PrivateActivate("showAcceptShared.ftl.html", "filter.ftl.html", "notes-list.ftl.html", "right-sidebar-list.ftl.html", request, response);
        } else if("1".equals(isRestoreNote)){
            request.setAttribute("name", request.getParameter("name"));
            request.setAttribute("noteid", Integer.parseInt(request.getParameter("note-id")));
            request.setAttribute("version", Integer.parseInt(request.getParameter("version")));
            res.PrivateActivate("showRestoresVersion.ftl.html", "filter.ftl.html", "notes-list.ftl.html", "right-sidebar-list.ftl.html", request, response);
        } else if("1".equals(isTooManyNotes)){
            request.setAttribute("message", "Attenzione!!! Ci sono due note aperte...");
            res.PrivateActivate("AlertNote.ftl.html","filter.ftl.html", "notes-list.ftl.html","right-sidebar-list.ftl.html", request, response);
        } else if("1".equals(isAddTagError)){
            request.setAttribute("box_message", "box_message");
            request.setAttribute("message", "Tag non valido!");
            request.setAttribute("name", request.getParameter("name"));
            request.setAttribute("noteid", request.getParameter("noteid"));
            res.PrivateActivate("showAddTag.ftl.html","filter.ftl.html", "notes-list.ftl.html","right-sidebar-list.ftl.html", request, response);
        } else if("1".equals(isErrorShared)){
            request.setAttribute("box_message", "box_message");
            request.setAttribute("message", "Email non valida !");
            request.setAttribute("name", request.getParameter("name"));
            request.setAttribute("noteid", request.getParameter("note-id"));
            res.PrivateActivate("showSharedNote.ftl.html","filter.ftl.html", "notes-list.ftl.html","right-sidebar-list.ftl.html", request, response);
        } else if("1".equals(isChangePermission)){
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("noteid", request.getParameter("note-id"));
            request.setAttribute("name", "Change Permission");
            if( (request.getParameter("permission")).equals("readonly") ){
                request.setAttribute("readonly", "checked=\"true\"");
            }else{
                request.setAttribute("readwrite", "checked=\"true\"");
            }
            res.PrivateActivate("showSharedNote.ftl.html","filter.ftl.html", "notes-list.ftl.html","right-sidebar-list.ftl.html", request, response);       
        }else if("1".equals(isInviteSimplestNote)){
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("by", request.getParameter("by"));
            res.PrivateActivate("showInviteSimplestNote.ftl.html", "filter.ftl.html", "notes-list.ftl.html", "right-sidebar-list.ftl.html", request, response);        
            }else if("1".equals(isErrorWriting)){
            res.PrivateActivate("errorWriting.ftl.html", "filter.ftl.html", "notes-list.ftl.html", "right-sidebar-list.ftl.html", request, response);        
            }else{
            res.PrivateActivate("filter.ftl.html", "notes-list.ftl.html", "right-sidebar-list.ftl.html", request, response);
        }
    }
}
