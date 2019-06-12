package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Map;

@Service
@Transactional
public class AchatService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager entityManager;

    public void acheter(Long idClient, Map<Long, Integer> articlesQuantites) {
        Client client = clientRepository.findById(idClient).get();
        Facture facture = new Facture();
        facture.setClient(client);
        entityManager.persist(facture);
        articlesQuantites
                .entrySet()
                .stream()
                .forEach(e -> {
                    LigneFacture ligneFacture = new LigneFacture();
                    ligneFacture.setFacture(facture);
                    Article article = articleRepository.getOne(e.getKey());
                    ligneFacture.setArticle(article);
                    ligneFacture.setQuantite(e.getValue());
                    entityManager.persist(ligneFacture);
                });
    }
}
