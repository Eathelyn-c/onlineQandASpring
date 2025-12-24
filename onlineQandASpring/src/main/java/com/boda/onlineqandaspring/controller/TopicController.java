package com.boda.onlineqandaspring.controller;

import com.boda.onlineqandaspring.model.Message;
import com.boda.onlineqandaspring.service.MessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        List<Message> topics = messageService.getAllTopics();
        model.addAttribute("messagesRelease", topics);
        model.addAttribute("loginUsername", session.getAttribute("loginUsername"));
        model.addAttribute("userID", session.getAttribute("userID"));
        return "online";
    }

    @PostMapping("/create")
    public String createTopic(@RequestParam String topic,
                              @RequestParam String content,
                              HttpSession session) {
        String sender = (String) session.getAttribute("loginUsername");
        messageService.releaseTopic(topic, content, sender);
        return "redirect:/topic/list";
    }

    @GetMapping("/{topicId}")
    public String topicDetail(@PathVariable int topicId, Model model) {
        Message topic = messageService.getTopicById(topicId);
        List<Message> replies = messageService.getDiscussById(topicId);

        model.addAttribute("topic", topic);
        model.addAttribute("messagesReply", replies);
        return "topicDetail";
    }
}
