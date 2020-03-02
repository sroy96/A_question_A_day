package com.email.classify.emailpoll.GmailService;

import com.email.classify.emailpoll.DAO.ProblemSet;
import com.email.classify.emailpoll.Repository.ProblemRepository;
import com.email.classify.emailpoll.Repository.QuestionRepository;
import com.email.classify.emailpoll.Utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    EmailAttachmentReceiver emailAttachmentReceiver;

    @Autowired
    ProblemRepository problemRepository;


    public void add()
    {
        emailAttachmentReceiver.poll();
    }

    public Iterable<ProblemSet> allQues() {
     return    problemRepository.findAll();
    }

    public List<ProblemSet> easy() {
        return  problemRepository.findAllByDifficulty(CommonConstants.EASY);
    }

    public List<ProblemSet> medium() {
        return  problemRepository.findAllByDifficulty(CommonConstants.MEDIUM);
    }



    public List<ProblemSet> hard() {
        return  problemRepository.findAllByDifficulty(CommonConstants.HARD);
    }


}
