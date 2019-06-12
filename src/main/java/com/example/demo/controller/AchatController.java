package com.example.demo.controller;

import com.example.demo.controller.form.AchatDto;
import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.service.AchatService;
import com.example.demo.service.ArticleService;
import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AchatController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AchatService achatService;

    @GetMapping("/acheter")
    public ModelAndView get() {
        ModelAndView modelAndView = new ModelAndView("acheter");
        List<Article> articles = articleService.findAll();
        modelAndView.addObject("articles", articles);
        return modelAndView;
    }

    @GetMapping("/acheter/post")
    public String post(@ModelAttribute AchatDto achatDto) {
        List<Client> allClients = clientService.findAllClients();
        Client client = allClients.get(0);
        achatService.acheter(client.getId(), achatDto.getAchat());
        return "redirect:/";
    }

}
