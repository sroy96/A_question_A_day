package com.email.classify.emailpoll.Repository;

import com.email.classify.emailpoll.DAO.GmailExtractDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends PagingAndSortingRepository<GmailExtractDAO,String> {
}
