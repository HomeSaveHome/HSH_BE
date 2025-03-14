package com.example.HomeSaveHome.BulletinBoard.repository;

import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByBoardId(Long boardId);
    Page<Article> findByBoardId(Long boardId, Pageable pageable);
}
