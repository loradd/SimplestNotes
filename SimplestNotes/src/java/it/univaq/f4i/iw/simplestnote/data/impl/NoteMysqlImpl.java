package it.univaq.f4i.iw.simplestnote.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.simplestnote.data.model.Note;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.ALL_PERMISSIONS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.CONFIRMED_STATUS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.CREATOR_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.LATEST_VERSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.PENDING_STATUS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.PREVIOUS_VERSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READONLY_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READWRITE_PERMISSION;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.READ_ACCESS;
import static it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer.WRITE_ACCESS;
import it.univaq.f4i.iw.simplestnote.data.model.Tag;
import it.univaq.f4i.iw.simplestnote.data.model.User;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Lorenzo Addazi (addazi.lorenzo@gmail.com)
 */

public class NoteMysqlImpl implements Note{
    
    private int id;
    private int version;
    private String title;
    private String contentPath;
    private String contentFile;
    private Date last_update;
    private int access_mode;
    private User creatorUser;
    private List<User> readOnlyUsers;
    private boolean dirtyReadOnlyUsers;
    private List<User> readWriteUsers;
    private boolean dirtyReadWriteUsers;
    private Map<String, Integer> pendingUsersPermission;
    private List<User> pendingUsers;
    private boolean dirtyPendingUsers;
    private List<Tag> noteTags;
    private boolean dirtyTags;
    private List<Note> noteHistory;
    protected SimplestNoteDataLayerMysqlImpl ownerdatalayer;
    protected boolean dirty;
    
    NoteMysqlImpl(SimplestNoteDataLayerMysqlImpl ownerdatalayer) throws DataLayerException {
        if(null != ownerdatalayer){
            this.ownerdatalayer = ownerdatalayer;
            this.id = 0;
            this.version = 0;
            this.title = "";
            this.contentPath = "";
            this.contentFile = null;
            this.last_update = null;
            this.access_mode = WRITE_ACCESS;
            this.creatorUser = ownerdatalayer.createUser();
            this.readOnlyUsers = new ArrayList<User>();
            this.readWriteUsers = new ArrayList<User>();
            this.pendingUsersPermission = new HashMap<String, Integer>(); 
            this.pendingUsers = new ArrayList<User>(); 
            this.noteHistory = new ArrayList<Note>(); 
            this.noteTags = new ArrayList<Tag>();
            this.dirty = false;
        } else {
            throw new DataLayerException("Unable to create note instance!");
        }
        
    }
    NoteMysqlImpl(SimplestNoteDataLayerMysqlImpl ownerdatalayer, ResultSet queryResultSet, int access_mode) throws SQLException, DataLayerException {
        this(ownerdatalayer);
        if(null != queryResultSet && (access_mode == READ_ACCESS || access_mode == WRITE_ACCESS)){
            this.id = queryResultSet.getInt("id");
            this.version = queryResultSet.getInt("version");
            this.title = queryResultSet.getString("title");
            this.contentPath = queryResultSet.getString("content");
            this.last_update = queryResultSet.getDate("last_update");
            this.access_mode = access_mode;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Parameters Error - NoteMysqlImpl.NoteMySqlImpl(SimplestNoteDataLayerMysqlImpl ownerdatalayer, ResultSet queryResultSet, int access_mode)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    @Override
    public int getVersion() {
        return this.version;
    }
    @Override 
    public void setVersion(int version) throws DataLayerException{
        if(version > 0){
            if(access_mode == WRITE_ACCESS){
                this.version = version;
                this.dirty = true;
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Version Error - NoteMysqlImpl.setVersion(int version)"); - DEBUG ONLY */
        }
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) throws DataLayerException{
        if(null != title){
            if(access_mode == WRITE_ACCESS){
                this.title = title;
                this.dirty = true;
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Null Title Error - NoteMysqlImpl.setTitle(String title)"); - DEBUG ONLY */
        }
    }

    @Override
    public String getContentPath() {
        return this.contentPath;
    }

    @Override
    public void setContentPath(String content) throws DataLayerException{
        if(null != content){
            if(access_mode == WRITE_ACCESS){
                this.contentPath = content;
                this.dirty = true;
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Null Content Error - NoteMysqlImpl.setContent(String content)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public String getContentFile() throws FileNotFoundException, IOException{
        if(null == this.contentFile){ 
            byte[] fileContent;
            try(FileInputStream contentFileStream = new FileInputStream(this.contentPath)){
                fileContent = new byte[contentFileStream.available()];
                contentFileStream.read(fileContent);
            }
            this.contentFile = new String(fileContent, "ISO-8859-1");
        }
        return this.contentFile;
    }
    
    @Override
    public void setContentFile(String content) throws DataLayerException{
        if(null != content){
            this.contentFile = content;
            this.dirty = true;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Null Content Error - NoteMysqlImpl.setContentFile(String content)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public Date getLastUpdate() {
        return this.last_update;
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
            /* throw new DataLayerException("Access Mode Error - NoteMysqlImpl.setAccessMode(int access_mode)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public User getCreatorUser(int access_mode) throws DataLayerException{
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            if(creatorUser.getId() == 0){ 
                List<User> storedCreator = ownerdatalayer.getUsersFromNote(this, CREATOR_PERMISSION, CONFIRMED_STATUS, access_mode);
                if(!storedCreator.isEmpty()){
                    this.creatorUser = storedCreator.get(0);
                }
            }
            return this.creatorUser;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - NoteMysqlImpl.getCreatorUser(int access_mode)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public void setCreatorUser(User creator_user) throws DataLayerException{
        if(null != creator_user && access_mode == WRITE_ACCESS){
            this.creatorUser = creator_user;
            this.dirty = true;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Paramaters Error - NoteMysqlImpl.setCreatorUser(User creator_user)"); */
        }
    }

    @Override
    public List<User> getReadOnlyUsers(int access_mode) throws DataLayerException{
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            if(readOnlyUsers.isEmpty()){
                if(!this.dirtyReadOnlyUsers){
                    this.readOnlyUsers = ownerdatalayer.getUsersFromNote(this, READONLY_PERMISSION, CONFIRMED_STATUS, access_mode);
                    this.dirtyReadOnlyUsers = true;
                }
            } else {
                Iterator<User> readOnlyUsersIterator = this.readOnlyUsers.iterator();
                while(readOnlyUsersIterator.hasNext()){
                    User readOnlyUser = readOnlyUsersIterator.next();
                    if(readOnlyUser.getAccessMode() != access_mode){
                        readOnlyUser.setAccessMode(access_mode);
                    }
                }
            }
            return this.readOnlyUsers;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - NoteMysqlImpl.getReadOnlyUsers(int access_mode)"); - DEBUG ONLY */
        }
    }

    @Override
    public void addReadOnlyUser(User read_only_user) throws DataLayerException{
        if(access_mode == WRITE_ACCESS && null != read_only_user){
            boolean isNotContained = true;
            for(User readOnlyUser : this.readOnlyUsers){
                if(readOnlyUser.getId() == read_only_user.getId()){
                    isNotContained = false;
                    break;
                }
            }
            if(isNotContained){
               this.readOnlyUsers.add(read_only_user);
               this.dirtyReadOnlyUsers = true;
               this.dirty = true;
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - NoteMysqlImpl.addReadOnlyUser(User user)"); - DEBUG ONLY */
        }
    }
    @Override
    public void deleteReadOnlyUser(User read_only_user) throws DataLayerException {
        if(access_mode == WRITE_ACCESS && null != read_only_user){
            Iterator<User> readOnlyUsersIterator = this.readOnlyUsers.iterator();
            while(readOnlyUsersIterator.hasNext()){
                if(readOnlyUsersIterator.next().getId() == read_only_user.getId()){
                    readOnlyUsersIterator.remove();
                    this.dirtyReadOnlyUsers = true;
                    this.dirty = true;
                }
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - NoteMysqlImpl.deleteReadOnlyUser(User read_only_user)"); - DEBUG ONLY */
        }
    }
    @Override
    public List<User> getReadWriteUsers(int access_mode) throws DataLayerException{
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            if(readWriteUsers.isEmpty()){
                if(!this.dirtyReadWriteUsers){
                    this.readWriteUsers = ownerdatalayer.getUsersFromNote(this, READWRITE_PERMISSION, CONFIRMED_STATUS, access_mode);
                    this.dirtyReadWriteUsers = true;
                }
            } else {
                for(User readWriteUser : this.readWriteUsers){
                    if(readWriteUser.getAccessMode() != access_mode){
                        this.readWriteUsers.add(ownerdatalayer.getUserByKey(readWriteUser.getId(), access_mode));
                        this.readWriteUsers.remove(readWriteUser);
                    }
                }
            }
            return this.readWriteUsers;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - NoteMysqlImpl.getReadWriteUsers(int access_mode)"); - DEBUG ONLY */
        }
    }

    @Override
    public void addReadWriteUser(User read_write_user) throws DataLayerException {
        if(null != read_write_user && access_mode == WRITE_ACCESS){
            boolean isNotContained = true;
            for(User readWriteUser : this.readWriteUsers){
                if(readWriteUser.getId() == read_write_user.getId()){
                    isNotContained = false;
                    break;
                }
            }
            if(isNotContained){
                this.readWriteUsers.add(read_write_user);
                this.dirtyReadWriteUsers = true;
                this.dirty = true;
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Parameters Error - NoteMysqlImpl.addReadWriteUser(User read_write_user)"); - DEBUG ONLY */
        }
    }
    @Override
    public void deleteReadWriteUser(User read_write_user) throws DataLayerException {
        if(null != read_write_user && access_mode == WRITE_ACCESS){
            Iterator<User> readWriteUsersIterator = this.readWriteUsers.iterator();
            while(readWriteUsersIterator.hasNext()){
                if(readWriteUsersIterator.next().getId() == read_write_user.getId()){
                    readWriteUsersIterator.remove();
                    this.dirtyReadWriteUsers = true;
                    this.dirty = true;
                }
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Parameters Error - NoteMysqlImpl.deleteReadWriteUser(User read_write_user)"); - DEBUG ONLY */
        }    
    }

    @Override
    public List<User> getPendingUsers(int access_mode) throws DataLayerException{
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            if(pendingUsers.isEmpty()){
                if(!this.dirtyPendingUsers){
                    this.pendingUsers = ownerdatalayer.getUsersFromNote(this, ALL_PERMISSIONS, PENDING_STATUS, access_mode);
                    for(User pendingUser : this.pendingUsers){
                        pendingUsersPermission.put(pendingUser.getEmailAddress(), ownerdatalayer.getUserNotePermission(pendingUser, this));
                    }
                    this.dirtyPendingUsers = true;
                }
            } else {
                for(User pendingUser : this.pendingUsers){
                    if(pendingUser.getAccessMode() != access_mode){
                        this.pendingUsers.remove(pendingUser);
                        this.pendingUsers.add(ownerdatalayer.getUserByKey(pendingUser.getId(), access_mode));
                    }
                }
            }
            return this.pendingUsers;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - NoteMysqlImpl.getPendingUsers(int access_mode)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public int getPendingUserPermission(String userEmailAddress) throws DataLayerException{
        if(null != userEmailAddress){
            if(this.pendingUsersPermission.isEmpty()){
                return 0;
            } else {
                return this.pendingUsersPermission.get(userEmailAddress);
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Email Address Error - NoteMysqlImpl.getPendingUserPermission(String userEmailAddress)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public void addPendingUser(User pending_user, int permission) throws DataLayerException {
        if(null != pending_user && (permission == READONLY_PERMISSION || permission == READWRITE_PERMISSION || permission == CREATOR_PERMISSION) && access_mode == WRITE_ACCESS){
            boolean isNotContained = true;
            Iterator<User> pendingUsersIterator = this.pendingUsers.iterator();
            while(pendingUsersIterator.hasNext()){
                User pendingUser = pendingUsersIterator.next();
                if(pendingUser.getId() == pending_user.getId()){
                    if(pendingUser.getPendingNotePermission(this) != permission){
                        System.out.println(permission + " " + pendingUser.getPendingNotePermission(this));
                        this.pendingUsersPermission.put(pendingUser.getEmailAddress(), permission);
                    }
                    isNotContained = false;
                    break;
                } 
            }
            if(isNotContained){
                this.pendingUsers.add(pending_user);
                this.pendingUsersPermission.put(pending_user.getEmailAddress(), permission);
                this.dirtyPendingUsers = true;
                this.dirty = true;
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Parameters Error - NoteMysqlImpl.addPendingUsers(User pending_user, int permission)"); - DEBUG ONLY */
        }
    }

    @Override
    public void deletePendingUser(User pending_user) throws DataLayerException{
        if(access_mode == WRITE_ACCESS && null != pending_user){
            Iterator<User> pendingUsersIterator = this.pendingUsers.iterator();
            while(pendingUsersIterator.hasNext()){
                if(pendingUsersIterator.next().getId() == pending_user.getId()){
                    pendingUsersIterator.remove();
                    this.pendingUsersPermission.remove(pending_user.getEmailAddress());
                    this.dirtyPendingUsers = true;
                    this.dirty = true;
                }
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Parameters Error - NoteMysqlImpl.deletePendingUser(User pending_user)"); - DEBUG ONLY */
        }
        
    }
    
    @Override
    public List<Tag> getNoteTags(int access_mode) throws DataLayerException{
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            if(noteTags.isEmpty()){
                if(!this.dirtyTags){
                    this.noteTags = ownerdatalayer.getTagsFromNote(this, access_mode);
                    this.dirtyTags = true;
                }
            } else {
                for(Tag tag : this.noteTags){
                    if(tag.getAccessMode() != access_mode){
                        this.noteTags.add(ownerdatalayer.getTagByKey(tag.getId(), access_mode));
                        this.noteTags.remove(tag);
                    }
                }
            }
            return this.noteTags;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - NoteMysqlImpl.getNoteTags(int access_mode)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public void addTag(Tag tag) throws DataLayerException{
        if(null != tag && access_mode == WRITE_ACCESS){
            Iterator<Tag> tagsIterator = this.noteTags.iterator();
            boolean isNotContained = true;
            while(tagsIterator.hasNext()){
                Tag currentTag = tagsIterator.next();
                if(currentTag.getId() == tag.getId()){
                    if(currentTag.getContent().equals(tag.getContent())){
                       isNotContained = false; 
                    } else {
                        tagsIterator.remove();
                    }
                    break;
                }
            }
            if(isNotContained){
                this.noteTags.add(tag);
                this.dirtyTags = true;
                this.dirty = true;
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Parameters Error - NoteMysqlImpl.addTag(Tag tag)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public void deleteTag(Tag tag) throws DataLayerException{
        if(null != tag && this.access_mode == WRITE_ACCESS){
            Iterator<Tag> tagsIterator = this.noteTags.iterator();
            while(tagsIterator.hasNext()){
                if(tag.getId() == tagsIterator.next().getId()){
                    tagsIterator.remove();
                    this.dirtyTags = true;
                    this.dirty = true;
                    break;
                }
            }
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Parameters Error - NoteMysqlImpl.deleteTag(Tag tag)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public List<Note> getNoteHistory(int access_mode) throws DataLayerException {
        if(access_mode == READ_ACCESS || access_mode == WRITE_ACCESS){
            if(this.noteHistory.isEmpty()){
                this.noteHistory = ownerdatalayer.getNoteHistory(this, access_mode);
            } else {
                for(Note oldNote : this.noteHistory){
                    if(oldNote.getAccessMode() != access_mode){
                        this.noteHistory.add(ownerdatalayer.getNoteByKey(oldNote.getId(), oldNote.getVersion(), access_mode));
                        this.noteHistory.remove(oldNote);
                    }
                }
            }
            return this.noteHistory;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - NoteMysqlImpl.getNoteHistory(int access_mode)"); - DEBUG ONLY */
        }
        
    }
    
    @Override
    public void rollbackToPreviousVersion(int version) throws DataLayerException {
        if(access_mode == WRITE_ACCESS && (version > 0 || version == LATEST_VERSION || version == PREVIOUS_VERSION) && this.version != 0 && this.version != 1){
            int currentVersion = this.version;
            Note oldNote = ownerdatalayer.getNoteByKey(this.id, version, READ_ACCESS);
            copyFrom(oldNote);
            this.contentPath = (this.contentPath.substring(0, this.contentPath.lastIndexOf("/"))).substring(0, (this.contentPath.substring(0, this.contentPath.lastIndexOf("/"))).lastIndexOf("/")) + "/" + currentVersion + "/" + this.title.replaceAll(" ", "") + ".txt";
            this.version = currentVersion;
            this.dirty = true;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Parameters Error - NoteMysqlImpl.rollbackToPreviousVersion(int version)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public boolean isDirty() {
        return this.dirty;
    }
    
    @Override
    public void setDirty(boolean dirty) throws DataLayerException {
        if(access_mode == WRITE_ACCESS){
            this.dirty = dirty;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - NoteMysqlImpl.setDirty(boolean dirty)"); - DEBUG ONLY */
        }
    }
    
    @Override
    public void copyFrom(Note note) throws DataLayerException {
        if(null != note && access_mode == WRITE_ACCESS){
            this.id = note.getId();
            this.version = note.getVersion();
            this.title = note.getTitle();
            this.contentPath = note.getContentPath();
            try {
                this.contentFile = note.getContentFile();
            } catch (IOException ex) {
                this.contentFile = null;
            }
            this.last_update = note.getLastUpdate();
            this.creatorUser = ownerdatalayer.createUser();
            this.readOnlyUsers = new ArrayList<User>();
            this.dirtyReadOnlyUsers = false;
            this.readWriteUsers = new ArrayList<User>();
            this.dirtyReadWriteUsers = false;
            this.pendingUsers = new ArrayList<User>();
            this.dirtyPendingUsers = false;
            this.noteHistory = new ArrayList<Note>();
            this.noteTags = new ArrayList<Tag>();
            this.dirtyTags = false;
            this.dirty = true;
        } else {
            throw new DataLayerException("An Unexpected Error Occurred! :(");
            /* throw new DataLayerException("Access Mode Error - NoteMysqlImpl.copyFrom(Note note)"); - DEBUG ONLY */
        }
    } 
}
