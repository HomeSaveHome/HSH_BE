package com.example.HomeSaveHome.BulletinBoard.dto;

import com.example.HomeSaveHome.BulletinBoard.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString

public class CommentDto {
    private Long id;
    private Long articleId;

    @NotBlank(message = "Please enter a comment.")
    private String body;
    private String author;
    private Long boardId;
    private int likeCount;

    public static CommentDto createCommentDto(Comment comment, Long boardId) {
        return new CommentDto(
                comment.getId(),
                comment.getArticle().getId(),
                comment.getBody(),
                comment.getAuthor(), // Ensure this is mapped correctly
                comment.getArticle().getBoard().getId(),
                comment.getLikeCount()
        );
    }

}
