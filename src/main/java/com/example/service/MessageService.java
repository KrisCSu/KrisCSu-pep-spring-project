package com.example.service;

import com.example.entity.*;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }
    
    
    public Message createMessage(Message message){
        if (message.getMessageText() == null || message.getMessageText().isEmpty()){
            throw new IllegalArgumentException("Empty message content!");
        }
        if (message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }
        if (message.getPostedBy() == null || !messageRepository.existsById(message.getPostedBy())) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer id){
        return messageRepository.findById(id);
    }

    public Integer deleteMessage(Integer id){
        if (messageRepository.existsById(id)){
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public Integer updateMessage(Integer id, Message text){
        Optional<Message> updatedMessage = messageRepository.findById(id);
        if (!updatedMessage.isPresent() || text.getMessageText().length() > 255 || text.getMessageText().length() <= 0) {
            return 0;
        } else {
            updatedMessage.get().setMessageText(text.getMessageText());
            messageRepository.save(updatedMessage.get());
            return 1;
        }
    }

    public List<Message> getMessagesByAccountId(Integer accountId){
        return messageRepository.findByPostedBy(accountId);
    }
}
