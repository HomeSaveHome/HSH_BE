package com.example.HomeSaveHome.BulletinBoard.service;

import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import com.example.HomeSaveHome.BulletinBoard.entity.Comment;
import com.example.HomeSaveHome.BulletinBoard.entity.Like;
import com.example.HomeSaveHome.BulletinBoard.repository.ArticleRepository;
import com.example.HomeSaveHome.BulletinBoard.repository.CommentRepository;
import com.example.HomeSaveHome.BulletinBoard.repository.LikeRepository;
import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ArticleRepository articleRepository;
    @Autowired private CommentRepository commentRepository;

    public String likeArticle(Long articleId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found!"));

        Optional<Like> existingLike = likeRepository.findByUserAndArticle(user, article);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get()); // Unlike if already liked
            return "Like removed!";
        }

        Like like = new Like(user, article);
        likeRepository.save(like);
        return "Liked successfully!";
    }

    public String likeComment(Long commentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));

        Optional<Like> existingLike = likeRepository.findByUserAndComment(user, comment);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get()); // Unlike if already liked
            return "Like removed!";
        }

        Like like = new Like(user, comment);
        likeRepository.save(like);
        return "Liked successfully!";
    }

    public List<String> getUsersWhoLikedArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found!"));
        return likeRepository.findByArticle(article).stream().map(like -> like.getUser().getUsername()).toList();
    }

    public List<String> getUsersWhoLikedComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));
        return likeRepository.findByComment(comment).stream().map(like -> like.getUser().getUsername()).toList();
    }

}
