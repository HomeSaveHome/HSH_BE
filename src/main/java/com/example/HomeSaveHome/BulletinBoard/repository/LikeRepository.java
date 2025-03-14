package com.example.HomeSaveHome.BulletinBoard.repository;

import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import com.example.HomeSaveHome.BulletinBoard.entity.Like;
import com.example.HomeSaveHome.BulletinBoard.entity.Comment;
import com.example.HomeSaveHome.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndArticle(User user, Article article);
    Optional<Like> findByUserAndComment(User user, Comment comment);

    List<Like> findByArticle(Article article);
    List<Like> findByComment(Comment comment);
}
