package it.univaq.f4i.iw.simplestnote.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.simplestnote.data.model.Note;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READ_ACCESS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.WRITE_ACCESS;
import it.univaq.f4i.iw.simplestnote.data.model.Tag;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lorenzo Addazi (addazi.lorenzo@gmail.com)
 */
public class TagMysqlImpl implements Tag{
    
    private int id;
    private String content;
    private int access_mode;
    private List<Note> notes;
    protected SimplestNoteDataLayerMysqlImpl ownerdatalayer;
    protected boolean dirty;
    
    TagMysqlImpl(SimplestNoteDataLayerMysqlImpl ownerdatalayer) throws DataLayerException{
        if(null != ownerdatalayer){
            this.ownerdatalayer = ownerdatalayer;
            this.id = 0;
            this.content = "";
            this.access_mode = WRITE_ACCESS;
            this.notes = new ArrayList<Note>();
            this.dirty = false;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Null OwnerDataLayer - TagMysqlImpl.TagMysqlImpl(SimplestNoteDataLayerMysqlImpl ownerdatalayer)"); */
        }
    }
    
    TagMysqlImpl(SimplestNoteDataLayerMysqlImpl ownerdatalayer, ResultSet queryResultSet, int access_mode) throws SQLException, DataLayerException {
        this(ownerdatalayer);
        if(null != queryResultSet && (access_mode == READ_ACCESS || access_mode == WRITE_ACCESS)){
            this.id = queryResultSet.getInt("id");
            this.content = queryResultSet.getString("content");
            this.access_mode = access_mode;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("TagMysqlImpl.TagMysqlImpl(SimplestNoteDataLayerMysqlImpl ownerdatalayer, ResultSet queryResultSet, int access_mode)"); */
        }
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    @Override
    public String getContent() {
        return this.content;
    }
    @Override
    public void setContent(String content) {
        if(this.access_mode == WRITE_ACCESS && null != content){
            this.content = content;
            this.dirty = true;
        }
    }
    @Override
    public int getAccessMode(){
        return this.access_mode;
    }
    @Override
    public void setAccessMode(int access_mode) {
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            this.access_mode = access_mode;
        }
    }
    
    @Override
    public List<Note> getNotes(int access_mode) throws DataLayerException{
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            if(this.notes.isEmpty()){
                this.notes = ownerdatalayer.getNotesFromTag(this, access_mode);
            } else {
                for(Note note : notes){
                    if(note.getAccessMode() != access_mode){
                        this.notes.add(ownerdatalayer.getNoteByKey(note.getId(), note.getVersion(), access_mode));
                        this.notes.remove(note);
                    }   
                }
            }
            return this.notes;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - SimplestNoteDataLayerMysqlImpl.getNotes(int access_mode)); */
        }
        
    }
    
    @Override
    public boolean isDirty() {
        return this.dirty;
    }
    
    @Override
    public void setDirty(boolean dirty) throws DataLayerException {
        if(this.access_mode == WRITE_ACCESS){
            this.dirty = dirty;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - TagMysqlImpl.setDirty(boolean dirty)"); */
        }
    }
    
    @Override
    public void copyFrom(Tag tag) throws DataLayerException{
        if(null != tag && this.access_mode == WRITE_ACCESS){
            this.id = tag.getId();
            this.content = tag.getContent();
            this.notes = new ArrayList<Note>();
            this.dirty = true;
        }
    }

}
