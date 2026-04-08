package com.sb.quiz_service.controller;


import com.sb.quiz_service.model.QuestionWrapper;
import com.sb.quiz_service.model.QuizDto;
import com.sb.quiz_service.model.Response;
import com.sb.quiz_service.service.QuizService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    Environment environment;


    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizDto quizDto){

        return quizService.createQuiz(quizDto.getCategoryName(), quizDto.getNumQuestions(), quizDto.getTitle());
    }

    @GetMapping("get/{quizId}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer quizId){

        return quizService.getQuizQuestions(quizId);
    }

    @PostMapping("submit/{quizId}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Integer quizId, @RequestBody List<Response> responses){
        return quizService.calculateResult(quizId, responses);
    }
}
