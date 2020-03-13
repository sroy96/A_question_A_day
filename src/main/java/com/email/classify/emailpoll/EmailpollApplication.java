package com.email.classify.emailpoll;


import com.email.classify.emailpoll.GmailService.DataFetcherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class EmailpollApplication implements Runnable {

	public static void main(String[] args) {
		SpringApplication.run(EmailpollApplication.class, args);
	}

	@Autowired
	DataFetcherService dataFetcherService;

	@Override
	@Scheduled(cron = "0 0 0 * * ?")
	public void run() {
		Calendar calendar = Calendar.getInstance();
		try{
		dataFetcherService.dataFetcher();
		log.info(calendar.toString()+": CRON Job Running");
		}
		catch (IOException e){
			log.error(e.getMessage()+" IOException");
		}
		catch (GeneralSecurityException ex){
			log.error(ex.getMessage()+"Authentication Security Error");
		}

	}
}
