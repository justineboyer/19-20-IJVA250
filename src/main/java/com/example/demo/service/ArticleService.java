package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> find(String query) {
        return articleRepository.findByQuery(query);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(Long id) {
        return articleRepository.findById(id).get();
    }

    public void modify(Long id, String libelle, double prix, int quantite) {
        Article article = articleRepository.findById(id).get();
        article.setLibelle(libelle);
        article.setPrix(prix);
        article.setQuantite(quantite);
    }

    public void create(String libelle, double prix, int quantite) {
        Article article = new Article();
        article.setLibelle(libelle);
        article.setPrix(prix);
        article.setQuantite(quantite);
        articleRepository.save(article);
    }
}
