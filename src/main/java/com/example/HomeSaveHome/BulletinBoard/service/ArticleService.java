package com.example.HomeSaveHome.BulletinBoard.service;

import com.example.HomeSaveHome.BulletinBoard.dto.ArticleForm;
import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import com.example.HomeSaveHome.BulletinBoard.entity.Board;
import com.example.HomeSaveHome.BulletinBoard.repository.ArticleRepository;
import com.example.HomeSaveHome.BulletinBoard.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BoardRepository boardRepository;

    public Article createArticle(ArticleForm form) {
        Board board = boardRepository.findById(form.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));
        return articleRepository.save(form.toEntity());
    }
}
