package com.example.HomeSaveHome.BulletinBoard.dto;

import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import com.example.HomeSaveHome.BulletinBoard.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
public class ArticleForm {
    @Getter
    private Long id;
    @Getter
    private String title;
    @Getter
    private String content;
    @Getter
    private String author;
    @Getter
    private LocalDateTime date;
    @Getter
    private Long BoardId;
    @Getter
    private Board board;

    public Article toEntity() {
        return new Article(id, title, content, author, LocalDateTime.now(), board);
    }


}
