package com.example.HomeSaveHome.BulletinBoard.dto;

import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private String author;  // ✅ Ensure author is explicitly included
    private LocalDateTime date;

    public static ArticleDto fromEntity(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getAuthor(),  // ✅ Ensure author is set
                article.getDate()
        );
    }
}
