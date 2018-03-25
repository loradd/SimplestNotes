package it.univaq.f4i.iw.simplestnote.mail;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class MailUtility
{
  public static void sendMail (String mail, String mex)
      throws MessagingException
  {
    // Creazione di una mail session configurata con i dati necessari
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.user", "simplestnote@gmail.com");
    //per i parametri SSL
    props.put("mail.smtp.socketFactory.port", "465"); //porta ssl
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");
    
    //creo sessione mail
    Session session;
      session = Session.getDefaultInstance(props, 
              new javax.mail.Authenticator(){
                  @Override
                  protected PasswordAuthentication getPasswordAuthentication() {
                      return new PasswordAuthentication(
                              "simplestnote@gmail.com", "simplestnote14");//username e password qui
                  }
              });

    //Creazione del messaggio da inviare
    MimeMessage message = new MimeMessage(session);
    //oggetto
    message.setSubject("SimplestNote");
    //testo
    message.setContent(mex,"text/html");

    //mittente
    InternetAddress fromAddress = new InternetAddress("simplestnote@gmail.com");
    //destinatario
    InternetAddress toAddress = new InternetAddress(mail);
    message.setFrom(fromAddress);
    message.setRecipient(Message.RecipientType.TO, toAddress);

    //Invio del messaggio
    Transport.send(message);
  }
}
