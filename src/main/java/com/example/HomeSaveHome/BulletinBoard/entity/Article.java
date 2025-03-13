package com.example.HomeSaveHome.BulletinBoard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@ToString

@Entity
@Getter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Setter
    private String title;
    @Column
    @Setter
    private String content;
    @Setter
    @Column
    private String author;

    @CreationTimestamp
    @Column
    private LocalDateTime date;

    @Setter
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Setter
    @Transient
    private String formattedDate;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public void patch(Article article) {
        if (article.title != null) {
            this.title = article.title;
        }
        if (article.content != null) {
            this.content = article.content;
        }
    }

    public Article(Long id, String title, String content, String author, LocalDateTime date, Board board) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
        this.board = board;
    }

    public int getCommentCount() {
        return (comments != null) ? comments.size() : 0;
    }

}