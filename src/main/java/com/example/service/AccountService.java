package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.example.entity.Account;
import com.example.exception.AuthenticationFailedException;
import com.example.exception.DuplicateUsernameException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    /*
     - The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its accountId. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
     - If the registration is not successful due to a duplicate username, the response status should be 409. (Conflict)
     - If the registration is not successful for some other reason, the response status should be 400. (Client error)
     */

    public Account addUser(Account account){

        if (account.getUsername() == null || account.getUsername().isBlank() || account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Invalid input"); // Will translate to 400 in controller
        }

        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username already exists"); // Custom exception
        }

        return accountRepository.save(account);

    }

    /*
     ## 2: Our API should be able to process User logins.

As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. The request body will contain a JSON representation of an Account.

- The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database. If successful, the response body should contain a JSON of the account in the response body, including its accountId. The response status should be 200 OK, which is the default.
- If the login is not successful, the response status should be 401. (Unauthorized)
     */

     public Account login(String username, String password){
        Optional<Account> existing = accountRepository.findByUsername(username);

        if(existing.isPresent() && existing.get().getPassword().equals(password)){
           return existing.get();
        }else {
            throw new AuthenticationFailedException("Invalid input!");
        }

    }



}
