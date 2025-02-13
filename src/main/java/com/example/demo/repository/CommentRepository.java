package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Show all comments on a certain article
    @Query(value = "SELECT * FROM COMMENT WHERE article_id = :articleId", nativeQuery = true)
    List<Comment> findByArticleId(Long articleId);
    // Show all comments by a certain user (a certain nickname)
    List<Comment> findByNickname(String nickname);
}
