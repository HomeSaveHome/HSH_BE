package com.example.HomeSaveHome.BulletinBoard.repository;

import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    ArrayList<Article> findByBoardId(Long boardId); // Iterable --> ArrayList type casting.
}
