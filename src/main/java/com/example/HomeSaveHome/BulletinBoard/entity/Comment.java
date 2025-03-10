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
    @JoinColumn(name = "article_id")
    private Article article;


    @Column
    private String body;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private String author; // New field for author

    public static Comment createComment(CommentDto dto, Article article, String author) {
        // Exception handling
        if (dto.getId() != null) {
            throw new IllegalArgumentException("Can't create a comment! ID should be null.");
        }
        if (dto.getArticleId() != article.getId()) {
            throw new IllegalArgumentException("Can't create a comment! Wrong Article ID.");
        }
        // Create comment entity with the author's username
        return new Comment(dto.getId(), article, dto.getBody(), article.getBoard(), author);
    }


    public void patch(CommentDto dto) {
        if (this.id != dto.getId()) {
            throw new IllegalArgumentException("Can't update comment! Wrong Comment ID.");
        }

        if (dto.getBody() != null) {
            this.body = dto.getBody();
        }
    }
}
