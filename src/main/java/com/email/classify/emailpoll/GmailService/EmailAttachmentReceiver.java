package com.email.classify.emailpoll.GmailService;
import com.email.classify.emailpoll.Utils.AttachmentHandler;
import com.email.classify.emailpoll.Utils.CommonConstants;
import org.springframework.stereotype.Service;

@Service
public class EmailAttachmentReceiver {

    public static String saveDirectory = "/home/content";

    private void setSaveDirectory(String dir) {
        EmailAttachmentReceiver.saveDirectory = dir;
    }

public void poll() {
        String host = CommonConstants.HOST_NAME;
        String port = CommonConstants.PORT_ID;
        String userName = CommonConstants.EMAIL_ID;
        String password =CommonConstants.PASSWORD;

    String saveDirectory = "/home/sdsf";

    EmailAttachmentReceiver receiver = new EmailAttachmentReceiver();
        receiver.setSaveDirectory(saveDirectory);
        AttachmentHandler.downloadEmailAttachments(host, port, userName, password);

        }
}