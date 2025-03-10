package com.example.HomeSaveHome.BulletinBoard.api;

import com.example.HomeSaveHome.BulletinBoard.dto.ArticleForm;
import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import com.example.HomeSaveHome.BulletinBoard.repository.ArticleRepository;
import com.example.HomeSaveHome.BulletinBoard.service.ArticleService;
import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j // For logging

@RestController
public class ArtitcleAPIController {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserRepository userRepository;

    // GET
    @GetMapping("/api/articles")
    public List<Article> index() {
        // TODO: findByBoardId() should have variable as parameter, not 1L
        return articleRepository.findByBoardId(1L);
    }
    @GetMapping("/api/articles/{id}")
    public Article show(@PathVariable Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    // POST
    @PostMapping("/boards/{boardId}/articles")
    public ResponseEntity<Article> create(@PathVariable Long boardId, @RequestBody ArticleForm form) {
        String username = getCurrentUsername();
        Article article = articleService.createArticle(form, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        return (user != null) ? user.getUsername() : email;
    }


    // PATCH
    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id, @RequestBody ArticleForm dto) {
        Article article = dto.toEntity(); // 1. Convert DTO to Entity
        log.info("id: {}, article: {}", id, article.toString());
        Article target = articleRepository.findById(id).orElse(null); // 2. Find the entity

        // 3. Handle the exception (bad request)
        if (target == null || id != article.getId()) {
            log.info("Bad Request! id: {}, article: {}", id, article.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 4. Update the entity and return 200 OK
        target.patch(article);
        Article updated = articleRepository.save(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    // DELETE
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id) {
        // 1. Find the entity
        Article target = articleRepository.findById(id).orElse(null);

        // 2. Handle the exception (bad request)
        if (target == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 3. Delete the entity and return 200 OK
        articleRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).body(target);
    }
}
