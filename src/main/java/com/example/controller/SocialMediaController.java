package com.example.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AuthenticationFailedException;
import com.example.exception.DuplicateUsernameException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody Account account){
        try {
            Account createdAccount = accountService.addUser(account);
            return ResponseEntity.ok(createdAccount);
            
        } catch (DuplicateUsernameException e) {
            return ResponseEntity.status(409).body("Username already exists.");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Registration Failed.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAccount(@RequestBody Account account){

        try{
            Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(loggedInAccount);
        }catch(AuthenticationFailedException e){
            return ResponseEntity.status(401).body("Unauthorized");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message){
        Message createdMessage = messageService.createMessage(message);
        if(createdMessage == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid message!");
        }
        return ResponseEntity.ok(createdMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable int messageId){
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/messages/{messageId}")
     public ResponseEntity<?> deleteById(@PathVariable int messageId){
        int rowsDeleted =  messageService.deleteById(messageId);
       
        if(rowsDeleted == 1){
            return ResponseEntity.ok(rowsDeleted);
        }

        return ResponseEntity.ok().build();
    }

    //Using Key Value, to get the newtext.
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessageText(@PathVariable int messageId, @RequestBody Map<String, String> body) {
    String newText = body.get("messageText");

    int rowsUpdated = messageService.updateMessageText(messageId, newText);

    if (rowsUpdated == 1) {
        return ResponseEntity.ok(rowsUpdated); 
    }

    return ResponseEntity.badRequest().build(); 
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<?> getAllMsgByUser(@PathVariable int accountId){
        
        List<Message> messages = messageService.getAllMsgByUser(accountId);
        return ResponseEntity.ok(messages);
    }



}
