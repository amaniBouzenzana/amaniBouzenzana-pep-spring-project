package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.service.MessageService;


@RestController
public class MessageController {
   
    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountRepository accountRepository;


    public MessageController(MessageService themessageService, AccountRepository theaccountRepository) {
		messageService= themessageService;
        accountRepository = theaccountRepository;
        
	}
       
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
       
        if (message.getMessageText().trim().isEmpty() || message.getMessageText().length() > 254) {
            return ResponseEntity.badRequest().build();
        }

  
        if (!accountRepository.existsById(message.getPostedBy())) {
            return ResponseEntity.badRequest().build();
        }

        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.ok(createdMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return ResponseEntity.ok().build(); 
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        int rowsDeleted = messageService.deleteMessageById(messageId);

        if (rowsDeleted > 0) {
            return ResponseEntity.ok(rowsDeleted); // Return number of rows updated (1)
        }
        return ResponseEntity.ok().build(); // Return empty body if no rows were updated
    
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody Message messageUpdate) {
        try {
           
            if (messageUpdate.getMessageText().trim().isEmpty() || messageUpdate.getMessageText().length() > 255) {
                return ResponseEntity.badRequest().body("Message text is either empty or exceeds the allowable limit.");
            }

            Message updatedMessage = messageService.updateMessage(messageId, messageUpdate);

            if (updatedMessage == null) {
                return ResponseEntity.badRequest().body("Message with ID " + messageId + " not found.");
            }

            return ResponseEntity.ok(1); 

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
  

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesForUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getAllMessagesForUser(accountId);
        return ResponseEntity.ok(messages); // Respond with an empty list if no messages are found
    }
    
}
