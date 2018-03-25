package it.univaq.f4i.iw.simplestnote;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

/**
 * Web application lifecycle listener.
 *
 * @author Lorenzo Addazi(addazi.lorenzo@gmail.com)
 */
public class ContextInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup(sce.getServletContext().getInitParameter("data.source"));
            sce.getServletContext().setAttribute("datasource", dataSource);
        } catch (NamingException ex) {
            Logger.getLogger(ContextInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Enumeration<?> contextAttributes = sce.getServletContext().getAttributeNames();
        while(contextAttributes.hasMoreElements()){
            String attribute = (String) contextAttributes.nextElement();
            sce.getServletContext().removeAttribute(attribute);
            System.err.println(attribute);
        }
        
    }
}
