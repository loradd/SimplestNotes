package it.univaq.f4i.iw.simplestnote.data.model;

import java.util.List;
import it.univaq.f4i.iw.framework.data.DataLayerException;

/**
 * @author lorenzoaddazi
 */

public interface User {
    
    /* USER ID CODE (Integer) */
    public int getId(); // Retrieve User Instance's Identification Code 
    /* USER FIRST NAME (String) */
    public String getFirstName(); // Retrieve User Instance's First Name 
    public void setFirstName(String first_name) throws DataLayerException; // Set User Instance's First Name 
    /* USER LAST NAME (String) */
    public String getLastName(); // Retrieve User Instance's Last Name 
    public void setLastName(String last_name) throws DataLayerException; // Set User Instance's Last Name 
    /* USER EMAIL ADDRESS (String) */
    public String getEmailAddress(); // Retrieve User Instance's Email Address
    public void setEmailAddress(String email_address) throws DataLayerException; // Set User Instance's Email Address
    /* USER PASSWORD */
    public String getPassword(); // Retrieve User Instance's Password 
    public void setPassword(String password) throws DataLayerException; // Set User Instance's Password 
    /* USER INSTANCE ACCESS MODE ( READ_ACCESS or WRITE_ACCESS ) */
    public int getAccessMode(); // Retrieve User Instance's Current Access Mode
    public void setAccessMode(int READ_ACCESS) throws DataLayerException; // Set User Instance's Current Access Mode
    /* USER'S CREATED NOTES */
    public List<Note> getCreatedNotes(int access_mode) throws DataLayerException; // Retrieve Note Instances related to this User Instance - as the Creator User
    /* USER'S READ ONLY NOTES */
    public List<Note> getReadOnlyNotes(int access_mode) throws DataLayerException; // Retrieve Note Instances related to this User Instance - as a Read Only User 
    /* USER'S READ WRITE NOTES */
    public List<Note> getReadWriteNotes(int access_mode) throws DataLayerException; // Retrieve Note Instances related to this User Instance - as a Read Write User 
    /* USER'S PENDING NOTES ( ... Waiting for a confirmation ... ) */
    public List<Note> getPendingNotes(int access_mode) throws DataLayerException; // Retrieve Note Instances related to this User Instance - waiting for a confirmation 
    /* USER'S PERMISSION FOR A PENDING NOTE */
    public int getPendingNotePermission(Note note) throws DataLayerException; // Retrieve expected User Instance's Permission regarding a given Note Instance 
    /* USER'S TAG */
    public List<Tag> getTags() throws DataLayerException; // Retrieve Tag Instances related to this User Instance - used in its related Note Instances 
    /* INSTANCE UTILITIES */
    public boolean isDirty(); // Check if uncommited changes have been made on this Instance 
    public void setDirty(boolean dirty) throws DataLayerException; // Signal uncommited changes on this Instance 
    public void copyFrom(User user) throws DataLayerException; // Copy Instance content from another User Instance

}
