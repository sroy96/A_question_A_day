package com.email.classify.emailpoll.GmailService;
import com.email.classify.emailpoll.Utils.AttachmentHandler;
import com.email.classify.emailpoll.Utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class EmailAttachmentReceiver {

    @Autowired
    AttachmentHandler attachmentHandler;

public void poll() {
        String host = CommonConstants.HOST_NAME;
        String port = CommonConstants.PORT_ID;
        String userName = CommonConstants.EMAIL_ID;
        String password =CommonConstants.PASSWORD;


    attachmentHandler.downloadEmailAttachments(host, port, userName, password);

        }
}