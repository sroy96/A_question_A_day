package com.email.classify.emailpoll.Utils;

import com.email.classify.emailpoll.DAO.GmailExtractDAO;
import com.email.classify.emailpoll.DAO.ProblemSet;
import com.email.classify.emailpoll.GmailService.EmailAttachmentReceiver;
import com.email.classify.emailpoll.Repository.ProblemRepository;
import com.email.classify.emailpoll.Repository.QuestionRepository;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;

import java.io.IOException;
import java.util.Properties;

@Component
public class AttachmentHandler {
    @Autowired
   private QuestionRepository questionRepository;

@Autowired
private ProblemRepository problemRepository;
    public  void downloadEmailAttachments(String host, String port, String userName, String password) {

        Properties properties = new Properties();

        // server setting
        properties.put(CommonConstants.POP3_HOST, host);
        properties.put(CommonConstants.POP3_PORT, port);

        // SSL setting
        properties.setProperty(CommonConstants.POP3_CLASS,CommonConstants.JAVAX_SOCKET_FACTORY);
        properties.setProperty(CommonConstants.POP3_FALLBACK,CommonConstants.FALSE );
        properties.setProperty(CommonConstants.POP3_socketFactory, String.valueOf(port));

        Session session = Session.getDefaultInstance(properties);

        try {
            // connects to the message store
            Store store = session.getStore(CommonConstants.PROTOCOL);
            store.connect(userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder(CommonConstants.FOLDER_NAME);
            folderInbox.open(Folder.READ_ONLY);

            // fetches new messages from server
            Message[] arrayMessages = folderInbox.getMessages();

            for (int i = 0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                Address[] fromAddress = message.getFrom();
                String from = fromAddress[0].toString();

               if (CommonConstants.FROM_FILTER.equalsIgnoreCase(from)) {

                    String subject = message.getSubject();
                    String sentDate = message.getSentDate().toString();

                    String contentType = message.getContentType();
                    String messageContent = "";

                    StringBuilder attachFiles = new StringBuilder();

                    if (contentType.contains("multipart")) {
                        // content may contain attachments
                        Multipart multiPart = (Multipart) message.getContent();
                        int numberOfParts = multiPart.getCount();
                        for (int partCount = 0; partCount < numberOfParts; partCount++) {
                            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                                // this part is attachment
                                String fileName = part.getFileName();
                                attachFiles.append(fileName).append(", ");

                            } else {
                                // this part may be the message content
                                messageContent = part.getContent().toString();
                            }
                        }


                    } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                        Object content = message.getContent();
                        if (content != null) {
                            messageContent = content.toString();
                        }
                    }
                    String newMessageContent = Jsoup.parse(messageContent).wholeText();
                    int start =  newMessageContent.indexOf("today.");
                    int end= newMessageContent.indexOf("Upgrade");

                    String resultMessage = newMessageContent.substring(start+5,end);
                    GmailExtractDAO gmailExtractDAO = new GmailExtractDAO();
                    gmailExtractDAO.setFrom(from);
                    gmailExtractDAO.setMessage(resultMessage);
                    gmailExtractDAO.setSubject(subject);
                    gmailExtractDAO.setSentDate(sentDate);
                    questionRepository.save(gmailExtractDAO);

                   ProblemSet problemSet= new ProblemSet();
                 problemSet.setDifficulty(subject.substring(subject.indexOf("[")+1,subject.indexOf("]")));
                problemSet.setQuestion(resultMessage);
                problemRepository.save(problemSet);

                }
                else {
                    throw new AppException("Filter not matched");
                }
            }

            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for pop3.");
            ex.printStackTrace();
         //
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
           // log.error(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
          //  log.error(ex);
        }catch (AppException ex){
            ex.printStackTrace();
          //  log.error("Filter not Matched during search");
        }
    }


}
