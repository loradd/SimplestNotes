package it.univaq.f4i.iw.simplestnote.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.simplestnote.data.model.Note;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.ALL_PERMISSIONS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.CONFIRMED_STATUS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.CREATOR_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.PENDING_STATUS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READONLY_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READWRITE_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READ_ACCESS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.WRITE_ACCESS;
import it.univaq.f4i.iw.simplestnote.data.model.Tag;
import it.univaq.f4i.iw.simplestnote.data.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lorenzo Addazi (addazi.lorenzo@gmail.com)
 */

public class UserMysqlImpl implements User {
    
    private int id;
    private String first_name;
    private String last_name;
    private String email_address;
    private String password;
    private List<Note> createdNotes;
    private List<Note> readOnlyNotes;
    private List<Note> readWriteNotes;
    private List<Note> pendingNotes;
    private Map<Note, Integer> pendingNotesPermission;
    private List<Tag> tags;
    private int access_mode;
    protected boolean dirty;
    protected SimplestNoteDataLayerMysqlImpl ownerdatalayer;
    
    UserMysqlImpl(SimplestNoteDataLayerMysqlImpl ownerdatalayer) throws DataLayerException{
        if(null != ownerdatalayer){
            this.ownerdatalayer = ownerdatalayer;
            this.id = 0;
            this.first_name = "";
            this.last_name = "";
            this.email_address = "";
            this.password = "";
            this.access_mode = WRITE_ACCESS;
            this.createdNotes = new ArrayList<Note>();
            this.readOnlyNotes = new ArrayList<Note>();
            this.readWriteNotes = new ArrayList<Note>();
            this.pendingNotes = new ArrayList<Note>();
            this.pendingNotesPermission = new HashMap<Note, Integer>();
            this.tags = new ArrayList<Tag>();
            this.dirty = false;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Null ownerdatalayer - UserMysqlImpl.UserMysqlImpl(SimplestNoteDataLayerMysqlImpl ownerdatalayer)"); */
        }
        
    }
    UserMysqlImpl(SimplestNoteDataLayerMysqlImpl ownerdatalayer, ResultSet queryResultSet, int access_mode) throws SQLException, DataLayerException{
        this(ownerdatalayer);
        if(null != queryResultSet && (access_mode == READ_ACCESS || access_mode == WRITE_ACCESS)){
            this.id = queryResultSet.getInt("id");
            this.first_name = queryResultSet.getString("first_name");
            this.last_name = queryResultSet.getString("last_name");
            this.email_address = queryResultSet.getString("email_address");
            this.password = queryResultSet.getString("password");
            this.access_mode = access_mode;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Null ownerdatalayer - UserMysqlImpl.UserMysqlImpl(SimplestNoteDataLayerMysqlImpl ownerdatalayer, ResultSet queryResultSet, int access_mode)"); */
        }
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    @Override
    public String getFirstName() {
        return this.first_name;
    }
    @Override 
    public void setFirstName(String first_name) throws DataLayerException{
        if(null != first_name && this.access_mode == WRITE_ACCESS){
            this.first_name = first_name;
            this.dirty = true;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /*throw new DataLayerException("Parameters Error - UserMysqlImpl.setFirstName(String first_name)"); */
        }
    }
    
    @Override
    public String getLastName() {
        return this.last_name;
    }
    @Override
    public void setLastName(String last_name) throws DataLayerException {
        if(null != last_name && this.access_mode == WRITE_ACCESS){
            this.last_name = last_name;
            this.dirty = true;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /*throw new DataLayerException("Parameters Error - UserMysqlImpl.setLastName(String last_name)"); */
        }
    }
    
    @Override
    public String getEmailAddress() {
        return this.email_address;
    }
    
    @Override
    public void setEmailAddress(String email_address) throws DataLayerException{
        if(null != email_address && this.access_mode == WRITE_ACCESS){
            this.email_address = email_address;
            this.dirty = true;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /*throw new DataLayerException("Parameters Error - UserMysqlImpl.setEmailAddress(String email_address)"); */
        }
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }
    
    @Override
    public void setPassword(String password) throws DataLayerException {
        if(null != password && this.access_mode == WRITE_ACCESS){
            this.password = password;
            this.dirty = true;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /*throw new DataLayerException("Parameters Error - UserMysqlImpl.setPassword(String password)"); */
        }
    }
    @Override 
    public int getAccessMode(){
        return this.access_mode;
    }
    @Override
    public void setAccessMode(int access_mode) throws DataLayerException{
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            this.access_mode = access_mode;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /*throw new DataLayerException("Access Mode Error - UserMysqlImpl.setAccessMode(int access_mode)"); */
        }
    }
    
    @Override
    public List<Note> getCreatedNotes(int access_mode) throws DataLayerException{ // posso scegliere con che tipo di permesso voglio ottenere le note 
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            if(this.createdNotes.isEmpty()){ 
                this.createdNotes = ownerdatalayer.getNotesFromUser(this, CREATOR_PERMISSION, CONFIRMED_STATUS, access_mode);
            } else {
                for(Note createdNote : createdNotes){
                    if(createdNote.getAccessMode() != access_mode){
                        this.createdNotes.add(ownerdatalayer.getNoteByKey(createdNote.getId(), createdNote.getVersion(), access_mode));
                        this.createdNotes.remove(createdNote);
                    }
                }
            }   
            return this.createdNotes;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /*throw new DataLayerException("Access Mode Error - UserMysqlImpl.getCreatedNotes(int access_mode)"); */
        }
        
    }
    @Override
    public List<Note> getReadOnlyNotes(int access_mode) throws DataLayerException {
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            if(this.readOnlyNotes.isEmpty()){
                this.readOnlyNotes = ownerdatalayer.getNotesFromUser(this, READONLY_PERMISSION, CONFIRMED_STATUS, access_mode);
            } else { 
                for(Note readOnlyNote : this.readOnlyNotes){
                    if(readOnlyNote.getAccessMode() != access_mode){
                        this.readOnlyNotes.add(ownerdatalayer.getNoteByKey(readOnlyNote.getId(), readOnlyNote.getVersion(), access_mode));
                        this.readOnlyNotes.remove(readOnlyNote);
                    }
                }
            }   
            return this.readOnlyNotes;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - UserMysqlImpl.getReadOnlyNotes(int access_mode)"); */
        }
        
    }
    @Override
    public List<Note> getReadWriteNotes(int access_mode) throws DataLayerException{
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            if(this.readWriteNotes.isEmpty()){
                this.readWriteNotes = ownerdatalayer.getNotesFromUser(this, READWRITE_PERMISSION, CONFIRMED_STATUS, access_mode);
            } else {
                for(Note readWriteNote : this.readWriteNotes){
                    if(readWriteNote.getAccessMode() != access_mode){
                        this.readWriteNotes.add(ownerdatalayer.getNoteByKey(readWriteNote.getId(), readWriteNote.getVersion(), access_mode));
                        this.readWriteNotes.remove(readWriteNote);
                    }
                }
            }
            return this.readWriteNotes;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - UserMysqlImpl.getReadWriteNotes(int access_mode)"); */
        }
        
    }
    @Override
    public List<Note> getPendingNotes(int access_mode) throws DataLayerException{
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){  
            if(this.pendingNotes.isEmpty()){
                this.pendingNotes = ownerdatalayer.getNotesFromUser(this, ALL_PERMISSIONS, PENDING_STATUS, access_mode);
                for(Note pendingNote : this.pendingNotes){
                    this.pendingNotesPermission.put(pendingNote, ownerdatalayer.getUserNotePermission(this, pendingNote));
                }
            } else {
                for(Note pendingNote : this.pendingNotes){
                    if(pendingNote.getAccessMode() != access_mode){
                        this.pendingNotes.add(ownerdatalayer.getNoteByKey(pendingNote.getId(), pendingNote.getVersion(), access_mode));
                        this.pendingNotes.remove(pendingNote);
                    }
                }
            }
            return this.pendingNotes;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - UserMysqlImpl.getPendingNotes(int access_mode)"); */
        }
        
    }
    
    @Override 
    public int getPendingNotePermission(Note note) throws DataLayerException {
        if(null != note){
            if(this.pendingNotesPermission.isEmpty()){
               return 0;
            } else {
                return this.pendingNotesPermission.get(note);
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - UserMysqlImpl.getPendingNotePermission(Note note)"); */
        }
    }
    
    @Override
    public List<Tag> getTags() throws DataLayerException{
        if(this.tags.isEmpty()){
            this.tags = ownerdatalayer.getTagsFromUser(this, READ_ACCESS);
        }
        return this.tags;
    }
    
    @Override
    public boolean isDirty() {
        return this.dirty;
    }
    
    @Override
    public void setDirty(boolean dirty) throws DataLayerException{
        if(this.access_mode == WRITE_ACCESS){
            this.dirty = dirty;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - UserMysqlImpl.setDirty(boolean dirty)"); */
        }
    }
    
    @Override
    public void copyFrom(User user) throws DataLayerException{
        if(null != user && this.access_mode == WRITE_ACCESS){
            this.id = user.getId();
            this.first_name = user.getFirstName();
            this.last_name = user.getLastName();
            this.email_address = user.getEmailAddress();
            this.password = user.getPassword();
            this.createdNotes = new ArrayList<Note>();
            this.readOnlyNotes = new ArrayList<Note>();
            this.readWriteNotes = new ArrayList<Note>();
            this.pendingNotes = new ArrayList<Note>();
            this.tags = new ArrayList<Tag>();
            this.dirty = true;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Parameters Error - UserMysqlImpl.copyFrom(User user)"); */
        }
    }
}
