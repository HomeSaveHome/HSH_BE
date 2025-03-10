package com.example.HomeSaveHome.BulletinBoard.service;

import com.example.HomeSaveHome.BulletinBoard.dto.CommentDto;
import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import com.example.HomeSaveHome.BulletinBoard.entity.Comment;
import com.example.HomeSaveHome.BulletinBoard.repository.ArticleRepository;
import com.example.HomeSaveHome.BulletinBoard.repository.CommentRepository;
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
//        // 1. Show all comments
//        List<Comment> comments = commentRepository.findByArticleId(articleId);
//        // 2. Entity --> DTO transformation
//        List<CommentDto> dtos = new ArrayList<CommentDto>();
//        for (int i=0;i<comments.size();i++) { // for each comment
//            Comment c = comments.get(i); // get the comment
//            CommentDto dto = CommentDto.createCommentDto(c); // create a DTO object (converting entity to DTO)
//            dtos.add(dto); // add the DTO object to the list (dto)
//        }
        // 3. Return
        return commentRepository.findByArticleId(articleId)
                .stream()
                .map(comment -> CommentDto.createCommentDto(comment)).collect(Collectors.toList());
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

        // 5. Convert to DTO and return
        return CommentDto.createCommentDto(created);
    }


    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Transactional
    public CommentDto update(Long id, CommentDto dto) {
        // 1. Find the comment entity and handle the exception
        Comment target = commentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Can't update comment! Comment does not exist"));
        // 2. Update the entity
        target.patch(dto);
        // 3. Save the entity
        Comment updated = commentRepository.save(target);
        // 4. Return the entity as DTO (by converting it)
        return CommentDto.createCommentDto(updated);
    }
@Transactional
    public CommentDto delete(Long id) {
        // 1. Find the comment entity and handle the exception
        Comment target = commentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Can't delete comment! Comment does not exist"));
        // 2. Delete the entity
        commentRepository.delete(target);
        // 3. Return the entity as DTO (by converting it)
        return CommentDto.createCommentDto(target);
    }
}
