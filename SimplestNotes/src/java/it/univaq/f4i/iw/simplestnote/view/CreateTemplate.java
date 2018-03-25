package it.univaq.f4i.iw.simplestnote.view;



import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.simplestnote.controller.init;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 *
 * @author Marco
 */
public class CreateTemplate extends init {

public void CreateSendMailPass(String mail,String mex,HttpServletRequest request, HttpServletResponse response,ServletContext context) throws IOException, ServletException {
        
     TemplateResult res = new TemplateResult(context);
        request.setAttribute("email", mail);
        request.setAttribute("mex", mex);
        res.PublicActivate("header-login.ftl.html","body-login.ftl.html","sendMailPass.ftl.html", request, response);
    }      
    
  public void CreateRemoveProfile(String name,String surname,String email,HttpServletRequest request, HttpServletResponse response,ServletContext context) throws IOException, ServletException {
            
            TemplateResult res = new TemplateResult(context);
            request.setAttribute("name", name);
            request.setAttribute("surname", surname);
            request.setAttribute("email", email);
            res.PrivateActivate("showRemoveProfile.ftl.html", "", "profile.ftl.html", "", request, response);
        
    }   
    
 public void CreateWelcome(String mex,String nome,String cognome,String email,HttpServletRequest request, HttpServletResponse response,ServletContext context) throws IOException, ServletException {
        
     TemplateResult res = new TemplateResult(context);
        request.setAttribute("nome", nome);
        request.setAttribute("cognome", cognome);
        request.setAttribute("email", email);
        request.setAttribute("mex", mex);
        res.PublicActivate(null,"welcome.ftl.html", request, response);
    }      

 public void CreateAddio(String nome,String cognome,HttpServletRequest request, HttpServletResponse response,ServletContext context) throws IOException, ServletException {
        
     TemplateResult res = new TemplateResult(context);
        request.setAttribute("name", nome);
        request.setAttribute("surname", cognome);
        res.PublicActivate(null,"addio.ftl.html", request, response);
    }      
 
public void CreateEditor(Object idNote1,Object idNote2,String acces1,String acces2,String state1,String state2,String title1,String editor1,String title2,String editor2,HttpServletRequest request, HttpServletResponse response,ServletContext context) throws IOException, ServletException {
               
 
            TemplateResult res = new TemplateResult(context);
            request.setAttribute("startForm", "<form method=\"post\" action=\"ManagerEditor\">");
            request.setAttribute("homeHidden", "hidden");
            request.setAttribute("homeShow", "show");   
            request.setAttribute("profileHidden", "hidden");
            request.setAttribute("profileShow", "show");            
            request.setAttribute("DisplaySave1", "Ciao Amore Mio");
            request.setAttribute("DisplaySave2", "Ciao Amore Mio");
            
            if(state1.equals("close")){
                request.setAttribute("disabled1", "disabled");
                request.setAttribute("DisplaySave1", null);
                request.setAttribute("DisplayClose1", "display: none;");
                request.setAttribute("DisplayNew1", "display: block;");                
            }
            
            if(state2.equals("close")){
                request.setAttribute("disabled2", "disabled");
                request.setAttribute("DisplaySave2", null);
                request.setAttribute("DisplayClose2", "display: none;");
                request.setAttribute("DisplayNew2", "display: block;");                
            }
            
            if(acces1.equals("readOnly")){
                request.setAttribute("DisplaySave1", null);             
            }    
            
            if(acces2.equals("readOnly")){
                request.setAttribute("DisplaySave2", null);             
            }              
            
            
            
            if(((String)context.getAttribute("write")) != null){
                if(((String)context.getAttribute("write")).equals("error")){
                request.setAttribute("classMessage", "red");  
                request.setAttribute("message", "Saving data failed! Try again!"); 
                }
                if(((String)context.getAttribute("write")).equals("save")){
                request.setAttribute("classMessage", "green");  
                request.setAttribute("message", "Data successfully saved!");                
                }
                context.setAttribute("write", null);
            }              
            
            
            request.setAttribute("acces1", acces1);
            request.setAttribute("state1", state1);
            request.setAttribute("title1", title1);
            request.setAttribute("idnote1", idNote1);
            request.setAttribute("editor1", editor1);
            request.setAttribute("acces2", acces2);
            request.setAttribute("state2", state2);
            request.setAttribute("title2", title2);
            request.setAttribute("idnote2", idNote2);
            request.setAttribute("editor2", editor2);  
   
            request.setAttribute("endForm", "</form>");
            res.PrivateActivate("editor1.ftl.html","editor2.ftl.html", request, response);
     
    }    
  
  public void CreateProfile(int userId, String name,String surname,String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException, DataLayerException {
            
            TemplateResult res = new TemplateResult(context);
            request.setAttribute("name", name);
            request.setAttribute("surname", surname);
            request.setAttribute("email", email);
            request.setAttribute("contactList", context.getAttribute("contactList"));
            res.PrivateActivate("profile.ftl.html", request, response);
        
    }   
   
public void SaveAlert(String name, String surname, String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {
            
                    TemplateResult res = new TemplateResult(context);
                    request.setAttribute("AlertView", "display: block;");
                    request.setAttribute("messageProfile", "Data successfully saved!");
                    request.setAttribute("name", name);
                    request.setAttribute("surname",surname);
                    request.setAttribute("email", email);
                    request.setAttribute("contactList", context.getAttribute("contactList"));
                    res.PrivateActivate("profile.ftl.html", request, response);

       
    }  

public void ErrorWtiting(String name, String surname, String email,HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException {
            
                    TemplateResult res = new TemplateResult(context);
                    request.setAttribute("AlertView", "display: block;");
                    request.setAttribute("messageProfile", "Saving data failed! Try again!");
                    request.setAttribute("name", name);
                    request.setAttribute("surname",surname);
                    request.setAttribute("email", email);
                    request.setAttribute("contactList", context.getAttribute("contactList"));
                    res.PrivateActivate("profile.ftl.html", request, response);

       
    } 
  
}
