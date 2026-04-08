package com.sb.question_service.dao;

import com.sb.question_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {

    //JPQL query (works on Java objects)
    @Query("Select q from Question q where lower(q.category) = lower(:category)")
    List<Question> findByCategory(@Param("category") String category);

    //Native query (works on database tables)
    @Query(value="SELECT q.id FROM Question q WHERE LOWER(category) = LOWER(:category) ORDER BY RANDOM() LIMIT :numQ ", nativeQuery = true)
    List<Integer> findRandomQuestionsByCategory(String category, int numQ);


}
