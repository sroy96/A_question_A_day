package com.email.classify.emailpoll.DAO;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@Document
public class GmailExtractDAO {
    @Id
    private String Id;
    private String from;
    private String subject;
    private String sentDate;
    private String message;
    private StringBuilder attachment;

    public GmailExtractDAO() {
    }

    public GmailExtractDAO(String from, String subject, String sentDate, String message, StringBuilder attachment) {

        this.from = from;
        this.subject = subject;
        this.sentDate = sentDate;
        this.message = message;
        this.attachment = attachment;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StringBuilder getAttachment() {
        return attachment;
    }

    public void setAttachment(StringBuilder attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "GmailExtractDAO{" +
                "Id='" + Id + '\'' +
                ", from='" + from + '\'' +
                ", subject='" + subject + '\'' +
                ", sentDate='" + sentDate + '\'' +
                ", message='" + message + '\'' +
                ", attachment='" + attachment + '\'' +
                '}';
    }
}
