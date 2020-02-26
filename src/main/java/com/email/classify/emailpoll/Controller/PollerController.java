package com.email.classify.emailpoll.Controller;

import com.email.classify.emailpoll.GmailService.EmailAttachmentReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RestController
public class PollerController {

    @Autowired
    private EmailAttachmentReceiver pollingService;

    @GetMapping("/")
    public String success() throws IOException, GeneralSecurityException {

        try{
            pollingService.poll();
            return "success";
        }
        catch(Exception e){
            e.getMessage();
        }
        return "done";
    }
}
