package com.example.HomeSaveHome.BulletinBoard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Getter
public class Article {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@Column
    private String title;
@Column
    private String content;
@Column
    private String author;
@Column
    private String date;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;


    public void patch(Article article) {
        if (article.title != null) {
            this.title = article.title;
        }
        if (article.content != null) {
            this.content = article.content;
        }
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
