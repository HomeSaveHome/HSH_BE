package com.example.HomeSaveHome.BulletinBoard.dto;

import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import com.example.HomeSaveHome.BulletinBoard.entity.Board;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ArticleForm {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String date;
    private Long BoardId;
    private Board board;

    public Article toEntity() {
        return new Article(id, title, content, author, date, board);
    }

    public Long getBoardId() {
        return BoardId;
    }
}
