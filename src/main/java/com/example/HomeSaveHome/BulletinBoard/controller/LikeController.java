package com.example.HomeSaveHome.BulletinBoard.controller;

import com.example.HomeSaveHome.BulletinBoard.entity.Comment;
import com.example.HomeSaveHome.BulletinBoard.repository.ArticleRepository;
import com.example.HomeSaveHome.BulletinBoard.repository.CommentRepository;
import com.example.HomeSaveHome.BulletinBoard.service.LikeService;
import com.example.HomeSaveHome.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/articles/{articleId}")
    public String likeArticle(@PathVariable Long articleId, @RequestParam String username, RedirectAttributes redirectAttributes) {
        String message = likeService.likeArticle(articleId, username);
        Long boardId = articleRepository.findById(articleId).get().getBoard().getId();

        redirectAttributes.addFlashAttribute("likeMessage", message); // ✅ Send message to frontend
        return "redirect:/boards/" + boardId + "/articles/" + articleId;
    }



    @PostMapping("/comments/{commentId}")
    public String likeComment(@PathVariable Long commentId, @RequestParam String username, RedirectAttributes redirectAttributes) {
        Comment comment = commentRepository.findById(commentId).get();
        Long articleId = comment.getArticle().getId();
        Long boardId = comment.getArticle().getBoard().getId();

        String message = likeService.likeComment(commentId, username);
        redirectAttributes.addFlashAttribute("likeMessage", message); // ✅ Send message to frontend

        return "redirect:/boards/" + boardId + "/articles/" + articleId;
    }




    @GetMapping("/articles/{articleId}")
    public ResponseEntity<List<User>> getUsersWhoLikedArticle(@PathVariable Long articleId) {
        return ResponseEntity.ok(likeService.getUsersWhoLikedArticle(articleId));
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<List<User>> getUsersWhoLikedComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(likeService.getUsersWhoLikedComment(commentId));
    }
}
