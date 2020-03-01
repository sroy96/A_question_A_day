package com.email.classify.emailpoll.Repository;

import com.email.classify.emailpoll.DAO.ProblemSet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProblemRepository extends MongoRepository<ProblemSet,String> {
    List<ProblemSet>findAllByDifficulty(String difficulty);
}
