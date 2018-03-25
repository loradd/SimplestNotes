package it.univaq.f4i.iw.simplestnote.data.impl.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author lorenzoaddazi
 */
public class NoteUtilities {
    
    public static String getDateTitle(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
        return dateFormat.format(new Date());
    }
    
}
