package com.example.HomeSaveHome.BulletinBoard.api;

import com.example.HomeSaveHome.BulletinBoard.dto.CommentDto;
import com.example.HomeSaveHome.BulletinBoard.service.CommentService;
import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CommentAPIController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepository userRepository;
    // 1. Show comments
    @GetMapping("/api/boards/{boardId}/articles/{articleId}/comments")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId) {
        // Use commentService to get comments
        List<CommentDto> dtos = commentService.comments(articleId);
        // Return result
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
    // 2. Add comments
    @PostMapping("/api/boards/{boardId}/articles/{articleId}/comments")
    public ResponseEntity<CommentDto> create(@PathVariable Long articleId, @RequestBody CommentDto dto) {

        String username = getCurrentUsername();

        // Use commentService to add comments
        CommentDto commentDto = commentService.create(articleId, dto, username);
        // Return result
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }
    // 3. Update comments
    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable Long id, @RequestBody CommentDto dto) {
        // Use commentService to update comments
        CommentDto updatedDto = commentService.update(id, dto);
        // Return result
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }
    // 4. Delete comments
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> delete(@PathVariable Long id) {
        // Use commentService to delete comments
        CommentDto deletedDto = commentService.delete(id);
        // Return result
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deletedDto);
    }




    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the authenticated email

        // Fetch user from database by email (not Optional, since findByEmail() returns User directly)
        User user = userRepository.findByEmail(email);

        // Return username if found, otherwise return email as fallback
        return (user != null) ? user.getUsername() : email;
    }


}
