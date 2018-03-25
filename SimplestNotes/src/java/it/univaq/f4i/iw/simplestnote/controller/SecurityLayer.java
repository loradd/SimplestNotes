package it.univaq.f4i.iw.simplestnote.controller;

import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SecurityLayer {

    //questa funzione esegue una serie di controlli di sicurezza
    //sulla sessione corrente. Se la sessione non � valida, la cancella
    //e ritorna null, altrimenti la aggiorna e la restituisce
    //this method executed a set of standard chacks on the current session.
    //If the session exists and is valid, it is rerutned, otherwise
    //the session is invalidated and the method returns null
    public static HttpSession checkSession(HttpServletRequest r) {
        boolean check = true;

        HttpSession s = r.getSession(false);
        //per prima cosa vediamo se la sessione � attiva
        //first, let's see is the sessione is active
        if (s == null) {
            return null;
        }

        //check sulla validit� della sessione
        //second, check is the session contains valid data
        //nota: oltre a controllare se la sessione contiene un userid, 
        //dovremmo anche controllere che lo userid sia valido, probabilmente 
        //consultando il database utenti
        //note: besides checking if the session contains an userid, we should 
        //check if the userid is valid, possibly querying the user database
        if (s.getAttribute("userid") == null) {
            check = false;
            //check sull'ip del client
            //check if the client ip changed
        } else if ((s.getAttribute("ip") == null) || !((String) s.getAttribute("ip")).equals(r.getRemoteHost())) {
            check = false;
            //check sulle date
            //check if the session is timed out
        } else {
            //inizio sessione
            //session start timestamp
            Calendar begin = (Calendar) s.getAttribute("inizio-sessione");
            //ultima azione
            //last action timestamp
            Calendar last = (Calendar) s.getAttribute("ultima-azione");
            //data/ora correnti
            //current timestamp
            Calendar now = Calendar.getInstance();
            if (begin == null) {
                check = false;
            } else {

                //tempo trascorso dall'inizio della sessione
                long secondsfrombegin = (now.getTimeInMillis() - begin.getTimeInMillis()) / 1000;

                //dopo cinque dall'apertura della sessione questa scade automaticamente
                if (secondsfrombegin > 5 * 60 * 60) {
                    check = false;
                } else if (last != null) {
                    //secondi trascorsi dall'ultima azione
                    //seconds from the last valid action
                    long secondsfromlast = (now.getTimeInMillis() - last.getTimeInMillis()) / 1000;

                    //dopo trenta minuti dall'ultima operazione la sessione scade                 
                    if (secondsfromlast > 30 * 60) {
                        check = false;
                    }
                }
            }
        }
        if (!check) {
            s.invalidate();
            return null;
        } else {
            //reimpostiamo la data/ora dell'ultima azione
            //if che checks are ok, update the last action timestamp
            s.setAttribute("ultima-azione", Calendar.getInstance());
            return s;
        }
    }

    public static HttpSession createSession(HttpServletRequest request, String email, int userid) {
        //se una sessione � gi� attiva, rimuoviamola e creiamone una nuova
        //if a session already exists, remove it and recreate a new one
        disposeSession(request);
        HttpSession s = request.getSession(true);
        s.setAttribute("email", email);
        s.setAttribute("ip", request.getRemoteHost());
        s.setAttribute("inizio-sessione", Calendar.getInstance());
        s.setAttribute("userid", userid);
        return s;
    }

    public static int getSession(HttpServletRequest request) {

        HttpSession s = request.getSession(false);
        int userid = (int) s.getAttribute("userid");
        //String email = (String) s.getAttribute("email");
        //String id = (String) s.getAttribute("ip");
        //String inizio_sessione = (String) s.getAttribute("inizio_sessione");
        return userid;
    }

    public static void disposeSession(HttpServletRequest request) {
        HttpSession s = request.getSession(false);
        if (s != null) {
            s.invalidate();
        }
    }

    public static void createSession(HttpServletRequest request, String email, String password, int userid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
