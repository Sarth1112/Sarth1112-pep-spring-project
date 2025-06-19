package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.exception.AuthenticationFailedException;
import com.example.exception.DuplicateUsernameException;
import com.example.service.AccountService;

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

}
