package it.univaq.f4i.iw.simplestnote.data.model;

import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.List;

/**
 * @author lorenzoaddazi
 */
public interface SimplestNoteDataLayer extends DataLayer {
    
    /* DATABASE ACCESS MODE */
    public final static int READ_ACCESS = -1; // READ ACCESS - no table row lock, always possible
    public final static int WRITE_ACCESS = -2; // WRITE_ACCESS - table row lock, only if there is not any other user currently writing on the involved instance 
    /* NOTE VERSION SHORTCUTS */
    public final static int LATEST_VERSION = -3; // LATEST_VERSION - maximum version number for the involved note, if it exists
    public final static int PREVIOUS_VERSION = -4; // PREVIOUS_VERSION - LATEST_VERSION - 1, for N > 1, and LATEST_VERSION, for N <= 1; where N is the total versions number
    /* USER PARTICIPATION TO NOTE STATUS */
    public final static int ALL_STATUS = -5; // ALL_STATUS = PENDING_STATUS U CONFIRMED_STATUS
    public final static int PENDING_STATUS = -6; // PENDING_STATUS - user note participations without confirmation executed yet
    public final static int CONFIRMED_STATUS = -7; // CONFIRMED_STATUS - confirmed user note participations
    /* USER PARTICIPATION TO NOTE PERMISSION */
    public final static int ALL_PERMISSIONS = -8; // ALL_PERMISSIONS = CREATOR_PERMISSION U READONLY_PERMISSION U READWRITE_PERMISSION
    public final static int CREATOR_PERMISSION = -9; // note creator user permission, all operations permitted
    public final static int READONLY_PERMISSION = -10; // note users with read permission only
    public final static int READWRITE_PERMISSION = -11; // note users with read/write permissions only, note deletion and complete management forbidden
    
    /* USER */
    User createUser() throws DataLayerException; // New User Instance 
    User getUserByKey(int key, int access_mode) throws DataLayerException; // Retrieves User Instance from its Primary Key
    List<User> getUsersFromEmailAddress(String email_address, int access_mode) throws DataLayerException; // Retrieves User Instances from an Email Address String 
    List<User> getUsersFromNote(Note note, int permission, int status, int access_mode) throws DataLayerException; // Retrieves User Instances from a linked Note Instance
    void storeUser(User user) throws DataLayerException; // Confirms/Creates and Saves a given User Instance
    void deleteUser(User user) throws DataLayerException; // Deletes a given User Instance
    /* NOTE */
    Note createNote() throws DataLayerException; // New Note Instance
    Note getNoteByKey(int key, int version, int access_mode) throws DataLayerException; // Retrieves Note Instance from its Primary Keys
    List<Note> getNotesFromTitle(String title, int access_mode) throws DataLayerException; // Retrieves Note Instance from a Title String
    List<Note> getNotesFromUser(User user, int permission, int status, int access_mode) throws DataLayerException; // Retrieves Note Instances from a linked User Instance
    List<Note> getNotesFromTag(Tag tag, int access_mode) throws DataLayerException; // Retrieves Note Instances from a linked Tag Instance
    List<Note> getNoteHistory(Note note, int access_mode) throws DataLayerException; // Retrieves the given Note Instance's History (e.g. Non Deleted Previous Versions)
    void storeNote(Note note) throws DataLayerException; // Confirms/Creates and Saves a given Note Instance
    void deleteNote(Note note) throws DataLayerException; // Deletes a given Note Instance
    void deleteNoteVersion(Note note) throws DataLayerException; // Deletes a given Version af a given Note
    /* USER NOTE - Participation Relationships among User and Note_Static Instances */
    int getUserNotePermission(User user, Note note) throws DataLayerException; // Retrieves User Participation Permission to Note
    /* TAG */
    Tag createTag() throws DataLayerException; // New Tag Instance
    Tag getTagByKey(int key, int access_mode) throws DataLayerException; // Retrieves Tag Instance from its Primary Keys
    List<Tag> getTagsFromContent(String content, int access_mode) throws DataLayerException; // Retrieves Tag Instances from a given Content String
    List<Tag> getTagsFromUser(User user, int access_mode) throws DataLayerException; // Retrieves Tag Instances from a linked User Instance
    List<Tag> getTagsFromNote(Note note,int access_modes) throws DataLayerException; // Retrieves Tag Instances from a linked Note Instance
    int getTagInclusionsNumber(Tag tag) throws DataLayerException; // Retrieves the given Tag Instance's usage factor
    int storeTag(Tag tag)throws DataLayerException; // Confirms/Creates and Saves a given Tag Instance
    void deleteTag(Tag tag) throws DataLayerException; // Deletes a given Tag Instance

}
