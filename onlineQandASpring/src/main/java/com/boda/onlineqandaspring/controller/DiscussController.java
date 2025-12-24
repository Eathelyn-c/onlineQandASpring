package com.boda.onlineqandaspring.controller;

import com.boda.onlineqandaspring.service.MessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/discuss")
public class DiscussController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/create")
    public String createDiscuss(@RequestParam String title,
                                @RequestParam String content,
                                @RequestParam int topicId,
                                HttpSession session) {
        String sender = (String) session.getAttribute("loginUsername");
        messageService.releaseDiscuss(title, content, sender, topicId);
        return "redirect:/topic/" + topicId;
    }
}
