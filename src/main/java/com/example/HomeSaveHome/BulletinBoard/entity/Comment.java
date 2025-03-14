package com.example.HomeSaveHome.BulletinBoard.entity;

import com.example.HomeSaveHome.BulletinBoard.dto.CommentDto;
import com.example.HomeSaveHome.user.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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
    @NotBlank(message = "Please enter a comment.")

    private String body;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private String author; // New field for author

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();



    public Comment(Long id, Article article, String body, Board board, String author) {
        this.id = id;
        this.article = article;
        this.body = body;
        this.board = board;
        this.author = author;
    }


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

    public int getLikeCount() {
        return likes.size();
    }
}
