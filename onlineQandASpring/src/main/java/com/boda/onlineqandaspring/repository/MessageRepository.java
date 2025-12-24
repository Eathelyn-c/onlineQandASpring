package com.boda.onlineqandaspring.repository;

import com.boda.onlineqandaspring.model.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class MessageRepository {

    private final List<Message> topics = new ArrayList<>();
    private final List<Message> discusses = new ArrayList<>();
    private final AtomicInteger nextTopicId = new AtomicInteger(1);

    public void addTopic(Message message) {
        topics.add(message);
    }

    public void addDiscuss(Message message) {
        discusses.add(message);
    }

    public int generateTopicId() {
        return nextTopicId.getAndIncrement();
    }

    public Message getTopicById(int id) {
        return topics.stream()
                .filter(msg -> msg.getTopicID() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Message> getDiscussById(int topicId) {
        return discusses.stream()
                .filter(msg -> msg.getTopicID() == topicId)
                .collect(Collectors.toList());
    }

    public List<Message> getAllTopics() {
        return new ArrayList<>(topics);
    }
}
