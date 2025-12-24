package com.boda.onlineqandaspring.service;

import com.boda.onlineqandaspring.model.Message;
import com.boda.onlineqandaspring.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void releaseTopic(String title, String content, String sender) {
        int topicId = messageRepository.generateTopicId();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Message message = new Message(title, content, time, sender, topicId);
        messageRepository.addTopic(message);
    }

    public Message getTopicById(int id) {
        return messageRepository.getTopicById(id);
    }

    public void releaseDiscuss(String title, String content, String sender, int topicId) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Message message = new Message(title, content, time, sender, topicId);
        messageRepository.addDiscuss(message);
    }

    public List<Message> getDiscussById(int id) {
        return messageRepository.getDiscussById(id);
    }

    public List<Message> getAllTopics() {
        return messageRepository.getAllTopics();
    }
}
