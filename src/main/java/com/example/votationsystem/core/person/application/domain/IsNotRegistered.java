package com.example.votationsystem.core.person.application.domain;

public class IsNotRegistered extends RuntimeException{
    public IsNotRegistered(int dni){
        super("The person with dni "+dni+" is not registered");
    }
}
