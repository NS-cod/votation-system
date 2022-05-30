package com.example.votationsystem.core.person.application.domain;

public class TableNotExist extends RuntimeException{
    public TableNotExist(int tableNumber){
        super("the table number "+tableNumber+" does not exist");
    }
}
