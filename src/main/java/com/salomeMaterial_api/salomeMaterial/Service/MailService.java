package com.salomeMaterial_api.salomeMaterial.Service;


import com.salomeMaterial_api.salomeMaterial.Entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MailService {

    @Autowired
    JavaMailSender emailSender;


    public void sendMaterialEmail(User user) throws MessagingException, UnsupportedEncodingException {
    String toAdress = user.getEmail();
    String fromAdress = "${EMAIL.SENDER}";
    String senderName = "SalomeStudies";
    String subject = "Material da Salome Studies";

    String content = "PAGINA HTML COM O MATERIAL";

    MimeMessage mimeMessage = emailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
    messageHelper.setFrom(fromAdress,senderName);
    messageHelper.setTo(toAdress);
    messageHelper.setSubject(subject);

    messageHelper.setText(content);
    emailSender.send(mimeMessage);
    }
}
