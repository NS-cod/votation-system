package com.example.votationsystem.core.person.application.domain;

public class InvalidTableCreationAttempt extends RuntimeException{
    public InvalidTableCreationAttempt(String tableType, int dni){
        super("The table type does not exist: "+tableType+" or the person with dni "+dni+" cant be the president of the electoral table");
    }
}
