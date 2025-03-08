package com.example.HomeSaveHome.BulletinBoard.controller;

import com.example.HomeSaveHome.BulletinBoard.dto.ArticleForm;
import com.example.HomeSaveHome.BulletinBoard.dto.CommentDto;
import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import com.example.HomeSaveHome.BulletinBoard.repository.ArticleRepository;
import com.example.HomeSaveHome.BulletinBoard.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentService commentService;
@GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }
@PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        log.info(form.toString());
        // System.out.println(form.toString());
        Article article = form.toEntity();
        log.info(article.toString());
        // System.out.println(article.toString());
        Article saved = articleRepository.save(article);
        log.info(saved.toString());
        // System.out.println(saved.toString());
        return "redirect:/articles/" + saved.getId(); // Redirect
    }
    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model) {
        log.info("id = " + id);
        Article articleEntity = articleRepository.findById(id).orElse(null); // check the id and bring the data

        List<CommentDto> commentDtos = commentService.comments(id);

        model.addAttribute("article", articleEntity); // register the data in the model
        model.addAttribute("commentDtos", commentDtos);
        return "articles/show"; // return the view page

    }
    @GetMapping("/articles")
    public String index(Model model) {
        // TODO: findByBoardId() should have variable as parameter, not 1L
        List<Article> articleEntityList = articleRepository.findByBoardId(1L); // 1. Bring all articles from DB


        model.addAttribute("articleList", articleEntityList);// 2. Save them in the model
        return "articles/index"; // 3. Return the view page

    }
@GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Article articleEntity = articleRepository.findById(id).orElse(null); // check the id and bring the data
        model.addAttribute("article", articleEntity); // register the data in the model
        return "articles/edit"; // set the view page
    }
    @PostMapping("/articles/update")
    public String update(ArticleForm form) {

        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        if (target != null) {
            articleRepository.save(articleEntity);
        }
        return "redirect:/articles/" + articleEntity.getId();
    }

    @GetMapping("articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        Article target = articleRepository.findById(id).orElse(null); // 1. Identify which article is to be deleted

        if (target != null) { // 2. Delete the article
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "Article Deleted.");
        }

        return "redirect:/articles"; // 3. Redirect to the result page

    }
}
