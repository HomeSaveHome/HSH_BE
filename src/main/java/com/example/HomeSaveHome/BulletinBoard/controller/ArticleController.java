package com.example.HomeSaveHome.BulletinBoard.controller;

import com.example.HomeSaveHome.BulletinBoard.dto.ArticleForm;
import com.example.HomeSaveHome.BulletinBoard.dto.CommentDto;
import com.example.HomeSaveHome.BulletinBoard.entity.Article;
import com.example.HomeSaveHome.BulletinBoard.entity.Board;
import com.example.HomeSaveHome.BulletinBoard.repository.ArticleRepository;
import com.example.HomeSaveHome.BulletinBoard.repository.BoardRepository;
import com.example.HomeSaveHome.BulletinBoard.service.CommentService;
import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository, BoardRepository boardRepository) {
        this.articleRepository = articleRepository;
        this.boardRepository = boardRepository;
    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;
    @GetMapping(("/boards/{boardId}/articles/new"))
    public String newArticleForm(@PathVariable Long boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "articles/new";
    }

    @PostMapping("/boards/{boardId}/articles/create")
    public String createArticle(ArticleForm form, @PathVariable Long boardId, Model model) {

        String username = getCurrentUsername();  // ✅ Fetch logged-in username

        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            return "redirect:/boards"; // Redirect if board doesn't exist
        }

        // ✅ Ensure the article has the correct author
        Article article = form.toEntity();
        article.setBoard(board);
        article.setAuthor(username);  // ✅ Set the author field

        Article saved = articleRepository.save(article);

        model.addAttribute("boardId", boardId);
        return "redirect:/boards/{boardId}/articles/" + saved.getId(); // Redirect to article view
    }


    @GetMapping("/boards/{boardId}/articles/{id}")
    public String show(@PathVariable Long id, @PathVariable Long boardId, Model model) {
        log.info("Fetching article with ID: " + id);

        Article articleEntity = articleRepository.findById(id).orElse(null);

        if (articleEntity == null) {
            log.error("Article not found!");
            return "redirect:/boards/" + boardId + "/articles"; // Redirect if article doesn't exist
        }

        List<CommentDto> commentDtos = commentService.comments(id);

        model.addAttribute("article", articleEntity);
        model.addAttribute("boardId", boardId);
        model.addAttribute("author", getCurrentUsername());
        model.addAttribute("commentDtos", commentDtos);


        return "articles/show";  // ✅ Mustache file name
    }




    @GetMapping("/boards/{boardId}/articles")
    public String index(@PathVariable Long boardId, Model model) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            return "redirect:/boards"; // Redirect if board doesn't exist
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // ✅ Convert articles and add a formatted date before sending them to Mustache
        List<Article> formattedArticles = articleRepository.findByBoardId(boardId).stream()
                .peek(article -> {
                    if (article.getDate() != null) {
                        article.setFormattedDate(article.getDate().format(formatter)); // ✅ Format date
                    }
                })
                .collect(Collectors.toList());

        model.addAttribute("articleList", formattedArticles);
        model.addAttribute("boardId", boardId);

        return "articles/index";
    }




    @GetMapping(("/boards/{boardId}/articles/{id}/edit"))
    public String edit(@PathVariable Long id, @PathVariable Long boardId, Model model) {
        Article articleEntity = articleRepository.findById(id).orElse(null); // check the id and bring the data
        model.addAttribute("article", articleEntity); // register the data in the model
        model.addAttribute("boardId", boardId);
        return "articles/edit"; // set the view page
    }
    @PostMapping("/boards/{boardId}/articles/update")
    public String update(ArticleForm form, @PathVariable Long boardId) {
        Article articleEntity = form.toEntity();

        // Retrieve the existing article from the database
        Article existingArticle = articleRepository.findById(articleEntity.getId()).orElse(null);

        if (existingArticle != null) {
            // ✅ Preserve the existing board reference
            articleEntity.setBoard(existingArticle.getBoard());

            log.info("Updating article: " + articleEntity);
            articleRepository.save(articleEntity);
        }

        return "redirect:/boards/" + boardId + "/articles/" + articleEntity.getId();
    }


    @GetMapping("/boards/{boardId}/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        Article target = articleRepository.findById(id).orElse(null); // 1. Identify which article is to be deleted

        if (target != null) { // 2. Delete the article
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "Article Deleted.");
        }

        return "redirect:/boards/{boardId}/articles"; // 3. Redirect to the result page

    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        return (user != null) ? user.getUsername() : email;
    }
}
