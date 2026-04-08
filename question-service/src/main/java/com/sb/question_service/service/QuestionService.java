package com.sb.question_service.service;

import com.sb.question_service.dao.QuestionDao;
import com.sb.question_service.model.Question;
import com.sb.question_service.model.QuestionWrapper;
import com.sb.question_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions(){
        try {
            return  new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }
        catch (Exception e){
           e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category){
        try {
            return  new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        }

        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    public ResponseEntity<String> addQuestion(Question question){
        try {
            questionDao.save(question);
            return  new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Error adding question", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<String> updateQuestion(Integer id, Question question){

        try {
            // check if id exists in database or not
            Optional<Question> optional = questionDao.findById(id);

            if (!optional.isPresent()) {
                return new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
            }

            Question existingQuestion = optional.get();
            //update fields
            existingQuestion.setQuestionTitle(question.getQuestionTitle());
            existingQuestion.setCategory(question.getCategory());
            existingQuestion.setDifficultylevel(question.getDifficultylevel());
            existingQuestion.setRightAnswer(question.getRightAnswer());
            existingQuestion.setOption1(question.getOption1());
            existingQuestion.setOption2(question.getOption2());
            existingQuestion.setOption3(question.getOption3());
            existingQuestion.setOption4(question.getOption4());

            questionDao.save(existingQuestion);
            return new ResponseEntity<>("Question updated successfully", HttpStatus.OK);
        }

        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Error updating question", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<String> removeQuestion(Integer id) {
        try {
            if (!questionDao.existsById(id)) {
                return new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
            }
            questionDao.deleteById(id);
            return new ResponseEntity<>("Question deleted successfully", HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Error deleting question", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        try {
            List<Integer> questionIds = questionDao.findRandomQuestionsByCategory(categoryName, numQuestions);
           return new ResponseEntity<>(questionIds, HttpStatus.CREATED);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromIds(List<Integer> questionIds) {
        try {

            List<Question> questionsFromDB = new ArrayList<>();
            for(Integer questionId : questionIds){
                questionsFromDB.add(questionDao.findById(questionId).get());
            }

            List<QuestionWrapper> wrappers= new ArrayList<>();

            for(Question question : questionsFromDB) {
                QuestionWrapper wrapper = new QuestionWrapper();
                wrapper.setId(question.getId());
                wrapper.setQuestionTitle(question.getQuestionTitle());
                wrapper.setOption1(question.getOption1());
                wrapper.setOption2(question.getOption2());
                wrapper.setOption3(question.getOption3());
                wrapper.setOption4(question.getOption4());

               wrappers.add(wrapper);
            }


            return new ResponseEntity<>(wrappers, HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        try {
          int score = 0;
          for(Response response : responses){
              String rightAnswer = questionDao.findById(response.getId()).get().getRightAnswer();
              if(response.getResponse().equals(rightAnswer))
                  score++;
          }
            return new ResponseEntity<>(score, HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
