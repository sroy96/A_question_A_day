package com.email.classify.emailpoll;

import com.email.classify.emailpoll.DAO.GmailExtractDAO;
import com.email.classify.emailpoll.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailpollApplication implements Runnable {

	public static void main(String[] args) {
		SpringApplication.run(EmailpollApplication.class, args);
	}


	@Override
	public void run(){

	}
}
