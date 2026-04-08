package com.sb.quiz_service.service;


import com.sb.quiz_service.dao.QuizDao;
import com.sb.quiz_service.feign.QuizInterface;
import com.sb.quiz_service.model.QuestionWrapper;
import com.sb.quiz_service.model.Quiz;
import com.sb.quiz_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;



    public ResponseEntity<String> createQuiz(String category, int numQ, String title){

        try {
            List<Integer> questionIds = quizInterface.getQuestionsForQuiz(category, numQ).getBody();

            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestionIds(questionIds);
            quizDao.save(quiz);
            return new ResponseEntity<>("Quiz created successfully", HttpStatus.CREATED);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Error creating quiz", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int quizId){
        try {
            Quiz quiz = quizDao.findById(quizId).get();
            List<Integer> quizQuestionIds = quiz.getQuestionIds();
          ResponseEntity<List<QuestionWrapper>> questionsForUser = quizInterface.getQuestionsFromIds(quizQuestionIds);
            return questionsForUser;
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<Integer> calculateResult(Integer quizId, List<Response> responses) {

        try {
            ResponseEntity<Integer> score = quizInterface.getScore(responses);
            return score;
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
