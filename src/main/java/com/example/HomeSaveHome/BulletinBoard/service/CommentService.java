package com.example.HomeSaveHome.BulletinBoard.service;

import com.example.HomeSaveHome.BulletinBoard.dto.CommentDto;
import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import com.example.HomeSaveHome.BulletinBoard.entity.Comment;
import com.example.HomeSaveHome.BulletinBoard.repository.ArticleRepository;
import com.example.HomeSaveHome.BulletinBoard.repository.CommentRepository;
import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    public List<CommentDto> comments(Long articleId) {

        return commentRepository.findByArticleId(articleId)
                .stream()
                .map(comment -> CommentDto.createCommentDto(comment, comment.getBoard().getId())).collect(Collectors.toList());
    }
    @Transactional
    public CommentDto create(Long articleId, CommentDto dto, String username) {
        // Debugging logs
        System.out.println("Creating comment for article ID: " + articleId);
        System.out.println("Received DTO: " + dto);

        // 1. Find the article and handle the exception
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Can't create comment! Article does not exist"));


        // Debugging log: Check retrieved username
        System.out.println("Retrieved username: " + username);

        // 3. Create the comment entity with author
        Comment comment = Comment.createComment(dto, article, username);

        // Debugging log: Check if board is correctly assigned
        System.out.println("Comment Board ID: " + (comment.getBoard() != null ? comment.getBoard().getId() : "NULL"));

        // 4. Save to DB
        Comment created = commentRepository.save(comment);

        // Update user's point (add 5 points when a user writes a new comment)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found")); // ✅ Handle Optional
        user.setPoint(user.getPoint() + 5);
        userRepository.save(user);

        // 5. Convert to DTO and return
        return CommentDto.createCommentDto(created, comment.getBoard().getId());
    }



    @Transactional
    public CommentDto update(Long id, CommentDto dto) {
        // ✅ 1. Find the comment entity and handle the exception
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't update comment! Comment does not exist"));

        // ✅ 2. Update the entity
        target.patch(dto);

        // ✅ 3. Save the updated entity
        Comment updated = commentRepository.save(target);

        // ✅ 4. Retrieve article & boardId (Fixing the issue)
        Article article = updated.getArticle();
        if (article == null) {
            throw new IllegalStateException("Updated comment does not have a valid article.");
        }
        Long boardId = article.getBoard().getId(); // ✅ Extract boardId from the related Article

        // ✅ 5. Return the updated entity as DTO (ensure it includes articleId & boardId)
        return CommentDto.createCommentDto(updated, boardId);
    }



    @Transactional
    public CommentDto delete(Long id) {
        // 1. Find the comment entity and handle the exception
        Comment target = commentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Can't delete comment! Comment does not exist"));
        // 2. Delete the entity
        commentRepository.delete(target);

        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found")); // ✅ Handle Optional
        user.setPoint(user.getPoint() - 5);
        userRepository.save(user);

        // 3. Return the entity as DTO (by converting it)
        return CommentDto.createCommentDto(target, target.getBoard().getId());
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        return (user != null) ? user.getUsername() : email;
    }
}
