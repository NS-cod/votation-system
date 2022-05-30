package com.example.votationsystem.core.person.domain;

public class InvalidVoter extends RuntimeException {
    public InvalidVoter (int age){
        super("the voter`s age must be greater than 16, the ingressed age is"+age);
    }
}
