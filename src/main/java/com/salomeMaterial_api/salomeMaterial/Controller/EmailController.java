package com.salomeMaterial_api.salomeMaterial.Controller;

import com.salomeMaterial_api.salomeMaterial.Entity.User;
import com.salomeMaterial_api.salomeMaterial.Service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("api/email")
public class EmailController {
    @Autowired
    private MailService service;

    @PostMapping()
    public ResponseEntity sendEmail(@RequestBody User user) throws MessagingException, UnsupportedEncodingException {
        service.sendMaterialEmail(user);
        return ResponseEntity.ok().build();
    }
}
