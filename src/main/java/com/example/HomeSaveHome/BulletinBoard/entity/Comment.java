package com.example.HomeSaveHome.BulletinBoard.entity;


import com.example.HomeSaveHome.BulletinBoard.dto.CommentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor


public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="article_id")
    private Article article;

    @Column
    private String nickname;

    @Column
    private String body;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public static Comment createComment(CommentDto dto, Article article) {
        // Exception handling
        // 1. If there's id in dto, IllegalArgumentException (there shouldn't be ID value before even creating a comment)
        if (dto.getId() != null) {
            throw new IllegalArgumentException("Can't create a comment! ID should be null.");
        }
        // 2. If the parent article from dto and the article from the parameter are different, IllegalArgumentException (ID is different)
        if (dto.getArticleId() != article.getId()) {
            throw new IllegalArgumentException("Can't create a comment! Wrong Article ID.");
        }
        // Create entity and convert
        return new Comment(dto.getId(), article, dto.getNickname(), dto.getBody(), article.getBoard());
    }

    public void patch(CommentDto dto) {
        // Exception handling
        if (this.id != dto.getId())
            throw new IllegalArgumentException("Can't update comment! Wrong Article ID.");
        // Patch the entity
        if (dto.getNickname() != null) {
            this.nickname = dto.getNickname();
        }
        if (dto.getBody() != null) {
            this.body = dto.getBody();
        }
    }
}
