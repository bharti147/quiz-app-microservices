package com.sb.quiz_service.model;

import jakarta.persistence.*;
import lombok.Data;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.util.List;

@Data
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @ManyToMany
    private List<TypePatternQuestions.Question> questions;
}
