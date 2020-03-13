package com.email.classify.emailpoll.GmailService;

import com.email.classify.emailpoll.DAO.ProblemSet;
import com.email.classify.emailpoll.Repository.ProblemRepository;
import com.email.classify.emailpoll.Repository.QuestionRepository;
import com.email.classify.emailpoll.Utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class AppService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    EmailAttachmentReceiver emailAttachmentReceiver;

    @Autowired
    DataFetcherService dataFetcherService;

    @Autowired
    ProblemRepository problemRepository;

    public Iterable<ProblemSet> add()
    {
        emailAttachmentReceiver.poll();
        return  problemRepository.findAll();
    }
//    public Iterable<ProblemSet> add() throws IOException, GeneralSecurityException
//    {
//        dataFetcherService.dataFetcher();
//        return  problemRepository.findAll();
//    }

    /**
     *
     * @return Iterable<ProblemSet>
     */
    public Iterable<ProblemSet> allQues() {
     return problemRepository.findAll();
    }

    /**
     * @return List
     */
    public List<ProblemSet> easy() {
        return  problemRepository.findAllByDifficulty(CommonConstants.EASY);
    }

    /**
     *
     * @return List
     */
    public List<ProblemSet> medium() {
        return  problemRepository.findAllByDifficulty(CommonConstants.MEDIUM);
    }


    /**
     *
     * @return List
     */
    public List<ProblemSet> hard() {
        return  problemRepository.findAllByDifficulty(CommonConstants.HARD);
    }


}
