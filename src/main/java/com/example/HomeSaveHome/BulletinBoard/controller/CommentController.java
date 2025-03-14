package com.example.HomeSaveHome.BulletinBoard.controller;

import com.example.HomeSaveHome.BulletinBoard.dto.CommentDto;
import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import com.example.HomeSaveHome.BulletinBoard.entity.Comment;
import com.example.HomeSaveHome.BulletinBoard.repository.ArticleRepository;
import com.example.HomeSaveHome.BulletinBoard.repository.CommentRepository;
import com.example.HomeSaveHome.BulletinBoard.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    @Autowired
    public CommentController(CommentService commentService, CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    // ✅ Load the comment edit page
    @GetMapping("/{commentId}/edit")
    public String showEditCommentPage(@PathVariable Long commentId, Model model) {
        Comment comment = commentRepository.findById(commentId).orElse(null);

        if (comment == null) {
            return "redirect:/error"; // Handle missing comment
        }

        model.addAttribute("comment", comment);
        return "comments/_edit"; // ✅ Redirect to edit.html in comments folder
    }

    @PostMapping("/{commentId}/update")
    public String updateComment(@PathVariable Long commentId, @ModelAttribute CommentDto dto) {
        CommentDto updatedCommentDto = commentService.update(commentId, dto);

        if (updatedCommentDto == null) {
            return "redirect:/error"; // Handle error case
        }

        // ✅ Get boardId and articleId directly from the updated DTO
        Long boardId = updatedCommentDto.getBoardId();
        Long articleId = updatedCommentDto.getArticleId();

        return "redirect:/boards/" + boardId + "/articles/" + articleId;
    }




    // ✅ Handle comment delete
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);

        if (comment != null) {
            Long articleId = comment.getArticle().getId();
            Long boardId = comment.getArticle().getBoard().getId();
            commentRepository.deleteById(commentId);
            return "redirect:/boards/" + boardId + "/articles/" + articleId;
        }

        return "redirect:/error"; // Handle missing comment case
    }
}
