package com.example.votationsystem.core.person.application.domain;

public class InvalidTableType extends RuntimeException{
    public InvalidTableType(String tableTye){
        super("The table type "+tableTye+" is invalid");
    }
}
