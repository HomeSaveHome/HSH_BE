package com.example.demo.api;

import com.example.demo.dto.CommentDto;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentAPIController {
    @Autowired
    private CommentService commentService;

    // 1. Show comments
    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId) {
        // Use commentService to get comments
        List<CommentDto> dtos = commentService.comments(articleId);
        // Return result
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
    // 2. Add comments
    @PostMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentDto> create(@PathVariable Long articleId, @RequestBody CommentDto dto) {
        // Use commentService to add comments
        CommentDto commentDto = commentService.create(articleId, dto);
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
}
