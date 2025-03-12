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

    @PostMapping("api/comments/{id}/update") // ✅ Handles form-based comment updates
    public ResponseEntity<?> updateCommentForm(@PathVariable Long id, @ModelAttribute CommentDto dto) {
        try {
            CommentDto updatedDto = commentService.update(id, dto);
            return ResponseEntity.ok(updatedDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Comment not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update comment.");
        }
    }

    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CommentDto dto) {
        // ✅ Check if request body is empty or invalid
        if (dto == null || dto.getBody() == null || dto.getBody().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comment body cannot be empty.");
        }

        try {
            // ✅ Use commentService to update the comment
            CommentDto updatedDto = commentService.update(id, dto);

            // ✅ Check if the update was successful
            if (updatedDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found.");
            }

            return ResponseEntity.ok(updatedDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update comment.");
        }
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
