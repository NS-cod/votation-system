package com.example.votationsystem.core.person.domain;

public class Person {
    private final String name;
    private final int dni;
    private final int age;
    private final boolean disease;

    public Person(String name, int dni, int age, boolean disease) {
        if(dni < 0 || name == null || age < 0 || age >= 120) {
            throw new RuntimeException();
        }
        this.name = name;
        this.dni = dni;
        this.age = age;
        this.disease = disease;
    }

    public boolean over65() {
        return this.age > 65;
    }

    public boolean preExistingDisease() {
        return this.disease;
    }

    public String getName() {
        return name;
    }

    public int getDni() {
        return dni;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "\n" +
                "nombre= " + name +
                "\n" +
                "dni= " + dni +
                "\n" +
                "edad= " + age +
                "\n" +
                "enfermedad= " + disease;
    }

}
