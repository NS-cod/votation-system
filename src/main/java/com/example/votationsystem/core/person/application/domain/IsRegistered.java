package com.example.votationsystem.core.person.application.domain;

public class IsRegistered extends RuntimeException{
    public IsRegistered(int dni){
        super("The person is already registered with dni: "+dni);
    }
}
