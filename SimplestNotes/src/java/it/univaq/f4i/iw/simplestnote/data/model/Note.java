package it.univaq.f4i.iw.simplestnote.data.model;

import java.sql.Date;
import java.util.List;
import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Lorenzo Addazi (addazi.lorenzo@gmail.com)
 */

public interface Note {
    
    /* NOTE ID ( Unique Integer Code - Database Auto Increment ) */
    public int getId(); 
    /* NOTE VERSION ( Integer ) */
    public int getVersion(); // Retrieves the Current Note Instance's Version 
    public void setVersion(int version) throws DataLayerException; // Sets the Current Note Instance's Version
    /* NOTE TITLE ( String ) */
    public String getTitle(); // Retrieves the Note Instance's Title
    public void setTitle(String title) throws DataLayerException; // Sets the Note Instance's Title
    /* NOTE CONTENT PATH ( String ) */
    public String getContentPath(); // Retrieves the Note Instance's File System Path
    public void setContentPath(String content) throws DataLayerException; // Sets the Note Instance's File System Path
    /* NOTE CONTENT FILE (String - Underlying File )*/
    public String getContentFile() throws FileNotFoundException, IOException; // Retrieves the Note Instance's File Content as a String
    public void setContentFile(String content) throws DataLayerException; // Sets the Note Instance's File Content from a String
    /* NOTE LAST UP( Date ) */
    public Date getLastUpdate(); // Retrieves the Note Instance's Last Up(Date) - Database Auto Update
    /* NOTE ACCESS MODE ( Integer ) */
    public int getAccessMode(); // Retrieves the Note Instance's Current Access Mode ( READ_ACCESS or WRITE_ACCESS )
    public void setAccessMode(int access_mode) throws DataLayerException; // READ_ACCESS to WRITE_ACCESS FORBIDDEN!!!
    /* NOTE'S CREATOR USER ( User Instance )*/
    public User getCreatorUser(int access_mode) throws DataLayerException; // Retrieves the Note Instance's Creator User Instance
    public void setCreatorUser(User user) throws DataLayerException; // Sets the Note Instance's Creator User Instance
    /* NOTE'S READ ONLY USERS ( User Instances List )*/
    public List<User> getReadOnlyUsers(int access_mode) throws DataLayerException; // Retrieves the Note Instance's Read Only Users Instances
    public void addReadOnlyUser(User read_only_user) throws DataLayerException; // Add a Read Only User Instance
    public void deleteReadOnlyUser(User read_only_user) throws DataLayerException; // Delete a Read Only User Instance 
    /* NOTE'S READ WRITE USERS ( User Instances List )*/
    public List<User> getReadWriteUsers(int access_mode) throws DataLayerException; // Retrieves the Note Instance's Read Write Users Instances
    public void addReadWriteUser(User read_write_user) throws DataLayerException; // Add a Read Only Write Instance
    public void deleteReadWriteUser(User read_write_user) throws DataLayerException; // Delete a Read Write User Instance 
    /* NOTE'S PENDING USERS ( e.g. User's participation to Note not confirmed yet ) ( User Instances List ) */
    public List<User> getPendingUsers(int access_mode) throws DataLayerException; // Retrieves the Note Instance's Pending Users Instances
    public int getPendingUserPermission(String userEmailAddress) throws DataLayerException; // Retrieves a Pending User's Permission from an Email Address String
    public void addPendingUser(User pending_user, int permission) throws DataLayerException; // Add a Pending User Instance 
    public void deletePendingUser(User pending_user) throws DataLayerException; // Delete a Pending User Instance
    /* NOTE'S TAGS ( Tag Instances List )*/
    public List<Tag> getNoteTags(int access_mode) throws DataLayerException; // Retrieve all Note's Tags
    public void addTag(Tag tag) throws DataLayerException; // Add new or existing Tag to the Note
    public void deleteTag(Tag tag) throws DataLayerException; // Delete a Tag from the Note
    /* NOTE'S HISTORY ( Note Instances List ) */
    public List<Note> getNoteHistory(int access_mode) throws DataLayerException; // Retrieves all Note Instance's Previous Versions ( as Note Instances as well )
    public void rollbackToPreviousVersion(int version) throws DataLayerException; // Rollback to the immediatelly previous Note Instance's Version 
    /* NOTE INSTANCE UTILITIES */
    public boolean isDirty(); // Check if related instance has uncommited modifications
    public void setDirty(boolean dirty) throws DataLayerException; // Set uncommited modifications Flag 
    public void copyFrom(Note note) throws DataLayerException; // Copies another Note Instance content
    
}
