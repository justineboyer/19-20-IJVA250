package com.example.demo.controller;

import com.example.demo.controller.form.FormArticle;
import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/articles")
    public ModelAndView geListArticles() {
        ModelAndView modelAndView = new ModelAndView("articles");
        List<Article> articles = articleService.findAll();
        modelAndView.addObject("articles", articles);
        return modelAndView;
    }

    @GetMapping("/articles/{id}")
    public ModelAndView getDetailArticle(
            @PathVariable Long id
    ) {
        ModelAndView modelAndView = new ModelAndView("article");
        Article article = articleService.findById(id);
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @PostMapping("/articles/{id}")
    public String modifierArticle(
            @PathVariable("id") Long id,
            @ModelAttribute FormArticle formArticle) {
        articleService.modify(
                id,
                formArticle.getLibelle(),
                formArticle.getPrix(),
                formArticle.getQuantite());
        return "redirect:/articles";
    }

    @GetMapping("/articles/create")
    public ModelAndView getCreate(
    ) {
        ModelAndView modelAndView = new ModelAndView("article");
        Article article = new Article();
        article.setLibelle("");
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @PostMapping("/articles/create")
    public String post(@ModelAttribute FormArticle formArticle) {
        articleService.create(
                formArticle.getLibelle(),
                formArticle.getPrix(),
                formArticle.getQuantite());
        return "redirect:/articles";
    }

}
