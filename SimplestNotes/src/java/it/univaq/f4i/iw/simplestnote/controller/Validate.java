/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.f4i.iw.simplestnote.controller;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marco
 */
public class Validate extends init {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    
    protected static boolean ValidateMail(String mail) throws IOException {

        Pattern p = Pattern.compile(".+@.+\\.[a-z]+", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mail);
        boolean matchFound = m.matches();

        //Condizioni pi� restrittive rispetto alle precedenti
        String  expressionPlus="[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        Pattern pPlus = Pattern.compile(expressionPlus, Pattern.CASE_INSENSITIVE);
        Matcher mPlus = pPlus.matcher(mail);
        boolean matchFoundPlus = mPlus.matches();

        return matchFound && matchFoundPlus;   
        
    }        
        
              
        protected static boolean ValidatePassword(String m) throws IOException {
            //Espressione regolare che determina i caratteri leciti nelle form Password
            //La Password drov� contenere almeno un numero e un carattere maiuscolo
            if (m == null){
            return false;
            }
            
            Pattern ControlPass = Pattern.compile("[a-zA-Z0-9]{0,}[A-Z]{1,}[a-zA-Z0-9]{0,}[0-9]{1,}[a-zA-Z]{0,}");  
            Matcher ParPassControl = ControlPass.matcher(m);
            boolean controlPass = ParPassControl.matches();   
          
            return (controlPass);             
            
    } 
        
        public static boolean ValidateString(String m) throws IOException {
            
            if (m == null){
                return false;
            }
            boolean result=true;
            //Espressione regolare che determina i caratteri leciti nelle form di Stringhe
            Pattern ControlPass = Pattern.compile("^[a-z A-Z 0-9 á Á à À â Â é É è È ê Ê í Í ì Ì î Î ó Ó ò Ò ô Ô ú Ú ù Ù û Û ´ ` ‘ ’ ']{3,40}$", Pattern.CASE_INSENSITIVE);    
            Matcher ParStringControl = ControlPass.matcher(m);
            boolean controlString = ParStringControl.matches();
            return (controlString);
    }   
 
        public static boolean ValidateSearch(String m) throws IOException {
            
            if (m == null){
                return false;
            }
            boolean result=true;
            //Espressione regolare che determina i caratteri leciti nelle form di Stringhe
            Pattern ControlPass = Pattern.compile("^[a-z A-Z 0-9 á Á à À â Â é É è È ê Ê í Í ì Ì î Î ó Ó ò Ò ô Ô ú Ú ù Ù û Û ´ ` ‘ ’ ']{1,40}$", Pattern.CASE_INSENSITIVE);    
            Matcher ParStringControl = ControlPass.matcher(m);
            boolean controlString = ParStringControl.matches();
            return (controlString);
    }          
    
}
