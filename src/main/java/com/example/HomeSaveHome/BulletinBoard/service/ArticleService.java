package com.example.HomeSaveHome.BulletinBoard.service;

import com.example.HomeSaveHome.BulletinBoard.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;


}
