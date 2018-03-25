package it.univaq.f4i.iw.simplestnote.view;

import it.univaq.f4i.iw.simplestnote.controller.init;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marco
 */
public class loginView extends init {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        
        TemplateResult res = new TemplateResult(getServletContext());
        res.PublicActivate("header-login.ftl.html", "body-login.ftl.html", request, response);
    
    }

}
