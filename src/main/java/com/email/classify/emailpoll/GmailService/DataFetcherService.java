package com.email.classify.emailpoll.GmailService;

import com.email.classify.emailpoll.DAO.ProblemSet;
import com.email.classify.emailpoll.Repository.ProblemRepository;
import com.email.classify.emailpoll.Utils.AccessTokenGeneratorUtils;
import com.email.classify.emailpoll.Utils.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.api.services.gmail.model.MessagePartHeader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

@Slf4j
@Component
public class DataFetcherService {

    @Autowired
    AccessTokenGeneratorUtils accessTokenGeneratorUtils;

    @Autowired
    ProblemRepository problemRepository;


    private static final String APPLICATION_NAME = CommonConstants.APPLICATION_NAME;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String user = CommonConstants.USER_ME;
    static Gmail service = null;
    private static File filePath = new File(System.getProperty("user.dir")+"/credentials.json");

    public static List<Message> listMessagesWithLabels(Gmail service, String userId,
                                                       List<String> labelIds) throws IOException {
        ListMessagesResponse response = service.users().messages().list(userId)
                .setLabelIds(labelIds).execute();

        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(userId).setLabelIds(labelIds)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }


        return messages;
    }

    public void dataFetcher() throws IOException, GeneralSecurityException{


        InputStream in = new FileInputStream(filePath);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        Credential authorize = new GoogleCredential.Builder().setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientSecrets.getDetails().getClientId().toString(),
                        clientSecrets.getDetails().getClientSecret().toString())
                .build()
                .setAccessToken(accessTokenGeneratorUtils.getAccessToken())
                .setRefreshToken(CommonConstants.REFRESH_TOKEN);

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize).setApplicationName(DataFetcherService.APPLICATION_NAME).build();

        Gmail.Users.Messages.List request = service.users().messages().list(user).setQ("to: " + CommonConstants.EMAIL_ID);

        ListMessagesResponse messagesResponse = request.execute();
        request.setPageToken(messagesResponse.getNextPageToken());
        List<String> labelList = new ArrayList<String>();
        labelList.add(CommonConstants.FOLDER_NAME);
        List<Message> output = DataFetcherService.listMessagesWithLabels(service, CommonConstants.EMAIL_ID, labelList);
        for (int i = 0; i < output.size(); i++) {
            try {
                    log.info("Attempt to Fetch Question");
                String messageId = messagesResponse.getMessages().get(i).getId();
                Message message = service.users().messages().get(user, messageId).execute();
                List<MessagePartHeader>headers= message.getPayload().getHeaders();
                String sender="";
                String subject="";
                for (MessagePartHeader head: headers) {
                    if("From".equalsIgnoreCase(head.getName()) || "from".equalsIgnoreCase(head.getName())){
                        sender=head.getValue();
                    }
                    if("Subject".equalsIgnoreCase(head.getName()) || "subject".equalsIgnoreCase(head.getName()) ){
                        subject=head.getValue();
                    }
                }
                if(CommonConstants.FROM_FILTER.equalsIgnoreCase(sender)) {
                    log.info("FILTER ARGUMENT MATCHED");
                    String emailBody = StringUtils
                            .newStringUtf8(Base64.decodeBase64(message.getPayload().getParts().get(0).getBody().getData()));
                    int start =  emailBody.indexOf("today.");
                    int end= emailBody.indexOf("Upgrade.");
                    String question = emailBody.substring(start+5,end);
                    String difficultyLevel = subject.substring(subject.indexOf("[")+1,subject.indexOf("]"));
                    System.out.println(difficultyLevel);
                    System.out.println(question);
                    try{
                        ProblemSet problemSet = new ProblemSet();
                        problemSet.setQuestion(question);
                        problemSet.setDifficulty(difficultyLevel);
                        problemRepository.save(problemSet);
                        log.info("Question Successfully Logged");
                    }
                    catch (Exception ex){
                        log.error("Repository Failed to Connect");
                    }

                }
            } catch (NullPointerException gmailNullPtrEx) {
                log.error("No Filter Matched");
                gmailNullPtrEx.printStackTrace();
            }
            catch(IndexOutOfBoundsException gmailOutOfBoundEx){
                log.error("Stack OverFlowed");
                gmailOutOfBoundEx.getMessage();
            }

        }
    }
}

