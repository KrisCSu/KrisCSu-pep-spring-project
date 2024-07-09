package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.*;
import com.example.service.AccountService;
import com.example.service.MessageService;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
@RequestMapping()
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account newAccount){
        try{
            Account account = accountService.register(newAccount);
            return ResponseEntity.ok(account);
        } catch (IllegalStateException e){
            if (e.getMessage() == "Account already exists"){
                return ResponseEntity.status(409).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> getMethodName(@RequestBody Account account){
        try {
            Account loggedAccount = accountService.login(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(loggedAccount);
        } catch (IllegalStateException e){
            return ResponseEntity.status(401).build();
        }
    }
    
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message){
        try{
            Message newMessage = messageService.createMessage(message);
            return ResponseEntity.ok(newMessage);
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        java.util.Optional<Message> messageOptional = messageService.getMessageById(messageId);
        if (messageOptional.isPresent()) {
            return ResponseEntity.ok(messageOptional.get());
        } else {
            // ResponseEntity.ok() automatically returns a status code of 200, and .body(null) will give an empty response body.
            return ResponseEntity.ok().body(null);
        }
    }
    
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Integer messageId) {
        try{
            Integer deleted = messageService.deleteMessage(messageId);
            if (deleted == 1){
                return ResponseEntity.ok(deleted.toString());
            }
            return ResponseEntity.ok("");
        } catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).build();
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody Message updateMessage) {
        if (messageService.updateMessage(messageId, updateMessage) == 1) {
            return new ResponseEntity<Integer>(1, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer account_id){
        try{
            List<Message> messages = messageService.getMessagesByAccountId(account_id);
            return ResponseEntity.ok(messages);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

}
