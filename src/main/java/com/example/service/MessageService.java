package com.example.service;

import com.example.entity.Message;

import java.util.List;

public interface MessageService {
    Message createMessage(Message message);
    List<Message> getAllMessages();
    Message getMessageById(Integer id);
    int deleteMessageById(Integer messageId);
    Message updateMessage(Integer id, Message message);
    List<Message> getAllMessagesForUser(Integer userId);
   
   
}