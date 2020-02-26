package com.email.classify.emailpoll.Controller;

import com.email.classify.emailpoll.GmailService.EmailAttachmentReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/problems")
public class PollerController {

    @Autowired
    private EmailAttachmentReceiver pollingService;

    @GetMapping("/refresh")
    public String success()  {
        try{
            pollingService.poll();
            return "success";
//            return new ResponseEntity(HttpStatus.OK);
        }
        catch(Exception e){
            e.getMessage();
        }
        return "fail";
        //return new ResponseEntity(HttpStatus.BAD_GATEWAY);
    }


}
