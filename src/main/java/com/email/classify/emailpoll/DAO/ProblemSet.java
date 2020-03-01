package com.email.classify.emailpoll.DAO;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class ProblemSet {
    @Id
    private String id;
    private String question;
    private String difficulty;

    public ProblemSet() {
    }

    public ProblemSet(String question, String difficulty) {
        this.question = question;
        this.difficulty = difficulty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "ProblemSet{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
