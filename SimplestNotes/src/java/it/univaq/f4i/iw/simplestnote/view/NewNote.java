package it.univaq.f4i.iw.simplestnote.view;

import it.univaq.f4i.iw.simplestnote.controller.init;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marco
 */
public class NewNote extends init {
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
                TemplateResult res = new TemplateResult(getServletContext());
                request.setAttribute("idnote1", "0");
                request.setAttribute("idnote2", "0");
                request.setAttribute("state1", "open");
                request.setAttribute("title1", "New Note");
                request.setAttribute("state2", "close");
                res.PrivateActivate("editor1.ftl.html","editor2.ftl.html","editor3.ftl.html", request, response);
        
    }
}
