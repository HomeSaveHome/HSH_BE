package com.example.HomeSaveHome.BulletinBoard.dto;

import com.example.HomeSaveHome.BulletinBoard.entity.Comment;
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
    private String nickname;
    private String body;

    public static CommentDto createCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(), // Comment entity's ID
                comment.getArticle().getId(), // Parent Article entity's ID
                comment.getNickname(), // Comment entity's nickname
                comment.getBody() // Comment entity's body
        );
    }
}
