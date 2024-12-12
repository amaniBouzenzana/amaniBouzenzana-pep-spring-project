package com.example.service;

import com.example.entity.Account;



public interface AccountService {





    Account saveAccount(Account account);


    Account getAccountByUsername(String username);

    Account authenticate(String username, String password);
    

}