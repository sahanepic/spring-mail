package com.sahanbcs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Controller
public class MailController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/sahan")
    public String Hello() {
        return "Sahan";
    }


    @GetMapping("/contact")
    public String contactForm() {
        return "contact_form";
    }

    @PostMapping("/contact")
    public String sendMail(HttpServletRequest  request, @RequestParam("attachement") MultipartFile  multipartFile) throws MessagingException, UnsupportedEncodingException {
        String  fullname = request.getParameter("fullname");
        String  email = request.getParameter("email");
        String  Subject = request.getParameter("Subject");
        String  content = request.getParameter("content");



//        SimpleMailMessage message = new SimpleMailMessage();

//
//        String mailSubject = fullname +  "  has Send A Message : " + Subject ;
//        String mailContent = " Sender Name  : " + fullname + "\n";
//        mailContent += "Send Email : " + email + "\n" ;
//        mailContent += "Subject : " + Subject + "\n";
//        mailContent += "Content: " + content + "\n";

//        message.setTo("sahanepic@gmail.com");

//        message.setTo("sahanephelperl.com");
//        message.setSubject(mailSubject);
//        message.setText(mailContent);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);





        String mailSubject = fullname +  "  has Send A Message : " + Subject ;
        String mailContent = "<p> <b>Sender Name  : " + fullname + "<b></p>";
        mailContent += "<p>Send Email : " + email + "</p>" ;
        mailContent += "<p>Subject : " + Subject + "</p>";
        mailContent += "<p>Content: " + content + "</p>";
        mailContent += "<hr><hr> <img src='cid:logoImage' />";


        helper.setFrom("sahansprin@gmail.com","sahan Bcs");
        helper.setTo(email);
        helper.setSubject(mailSubject);
        helper.setText(mailContent,true);
          Resource resource = new PathResource("E:\\Acadamic\\java\\spring-mail-new\\src\\main\\resources\\static\\ww.png");
        helper.addInline("logoImage" ,resource);


        if(!multipartFile.isEmpty()){
            String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            InputStreamSource source = new InputStreamSource() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return multipartFile.getInputStream();
                }
            };

            helper.addAttachment(filename,source);

        }

        mailSender.send(message);


        return "message";
    }

}
