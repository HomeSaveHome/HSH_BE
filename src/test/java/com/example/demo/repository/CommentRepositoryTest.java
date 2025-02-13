package com.example.demo.repository;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("Showing all comments on a certain article")
    void findByArticleId() {
        // Case 1. Show all comments on 4th article
        {
            // 1. Prepare an input data
            Long articleId = 4L;
            // 2. Real data
            List<Comment> comments = commentRepository.findByArticleId(articleId);
            // 3. Expected data
            Article article = new Article(4L, "What is your favorite movie?", "Please leave a comment below 1");
            Comment a = new Comment(1L, article, "Park", "The Shawshank Redemption");
            Comment b = new Comment(2L, article, "Kim", "The Godfather");
            Comment c = new Comment(3L, article, "Choi", "The Great Gatsby");
            List<Comment> expected = Arrays.asList(a, b, c);
            // 4. Compare and evaluate the real data with the expected data
            assertEquals(expected.toString(), comments.toString(), "Printing all the comments on 4th article.");
        }

        // Case 2. Show all the comments on 1st article (should be none)
        {
            // 1. Prepare an input data
            Long articleId = 1L;
            // 2. Real data
            List<Comment> comments = commentRepository.findByArticleId(articleId);
            // 3. Expected data
            Article article = new Article(1L, "Article 1", "Content of article 1");

            List<Comment> expected = Arrays.asList();
            // 4. Compare and evaluate the real data with the expected data
            assertEquals(expected.toString(), comments.toString(), "There's no comment on 1st article.");
        }
    }

    @Test
    @DisplayName("Showing all comments by a certain user (a certain nickname)")
    void findByNickname() {

        // Case 1. Showing all the comments by "Park"
        String nickname = "Park";
        List<Comment> comments = commentRepository.findByNickname(nickname);
        Comment a = new Comment(1L, new Article(4L, "What is your favorite movie?", "Please leave a comment below 1"), "Park", "The Shawshank Redemption");
        Comment b = new Comment(4L, new Article(5L, "What is your favorite food?", "Please leave a comment below 2"), "Park", "Pizza");
        Comment c =  new Comment(8L, new Article (6L, "What is your hobby?", "Please leave a comment below 3"), "Park", "Basketball");

        List<Comment> expected = Arrays.asList(a, b, c);

        assertEquals(expected.toString(), comments.toString(), "Printing all the comments by Park.");
    }
}