package com.example.HomeSaveHome.BulletinBoard.entity;

import com.example.HomeSaveHome.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user; // The user who liked

    @ManyToOne
    @JoinColumn(name = "article_id")
    @Getter
    @Setter
    private Article article; // Can be linked to an article

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @Getter
    @Setter
    private Comment comment; // Or linked to a comment

    // Constructors
    public Like() {}

    public Like(User user, Article article) {
        this.user = user;
        this.article = article;
    }

    public Like(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

}
