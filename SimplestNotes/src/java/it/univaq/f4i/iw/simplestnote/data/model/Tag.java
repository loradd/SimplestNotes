package it.univaq.f4i.iw.simplestnote.data.model;

import java.util.List;
import it.univaq.f4i.iw.framework.data.DataLayerException;

/**
 * @author Lorenzo Addazi (addazi.lorenzo@gmail.com)
 */

public interface Tag {
    
    /* TAG ID ( Unique Integer Code ) - Database Auto Increment */
    public int getId();
    /* TAG CONTENT ( String ) */
    public String getContent(); // Retrieves the Tag Instance's Content String
    public void setContent(String content); // Sets the Tag Instance's Content from a String
    /* TAG ACCESS MODE ( READ_ACCESS or WRITE_ACCESS ) */
    public int getAccessMode(); // Retrieves the Tag Instance's Current Access Mode 
    public void setAccessMode(int access_mode); // Sets the Tag Instance's Access Mode ( READ_ACCESS to WRITE_ACCESS forbidden )
    /* TAG RELATED NOTES */
    public List<Note> getNotes(int access_mode) throws DataLayerException; // Retrieves all Note Instances using the Tag 
    /* INSTANCE UTILITIES */
    public boolean isDirty();
    public void setDirty(boolean dirty) throws DataLayerException;
    public void copyFrom(Tag tag) throws DataLayerException;
    
}
