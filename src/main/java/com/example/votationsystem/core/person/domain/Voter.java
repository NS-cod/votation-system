package com.example.votationsystem.core.person.domain;

import org.javatuples.Pair;

public class Voter extends Person {
    private final boolean workCertificate;
    private boolean tablePresident;
    private boolean vote;
    private Pair<Integer, Integer> turn;

    public Voter(String name, int dni, int age, boolean disease, boolean workCertificate) {
        super(name, dni, age, disease);

        if (age < 16) {
            throw new InvalidVoter(age);
        }

        this.vote = false;
        this.turn = null;
        this.workCertificate = workCertificate;
        this.tablePresident = false;
    }

    public boolean isTablePresident() {
        return this.tablePresident;
    }

    public void asingAsTablePresident() {
        if (!haveTurn()) {
            this.tablePresident = true;
        }
    }

    public boolean haveWorkCertifiate() {
        return this.workCertificate;
    }

    public void confirmVote() {
        if (haveTurn()) {
            this.vote = true;
        }
    }

    public boolean haveTurn() {
        return turn != null;
    }

    public boolean consultVote() {
        return this.vote;
    }

    public Pair<Integer, Integer> asingTurn(int tableCode, int schedule) {
        if (!haveTurn())
            this.turn = new Pair<>(tableCode, schedule);
        return consultTurn();
    }

    public Pair<Integer, Integer> consultTurn() {
        return this.turn;
    }

    public Integer consultSchedule() {
        if(!haveTurn())
            return null;
        return consultTurn().getValue1();
    }

    public boolean isOnTable(Integer cod) {
        if(!haveTurn())
            return false;
        return consultTurn().getValue0().equals(cod);
    }

    @Override
    public String toString() {
        return "----------- voter -----------" +
                super.toString() +
                "\n" +
                "work certificate :" + workCertificate +
                "\n" +
                "vote : " + this.vote +
                "\n" +
                "Is the president of the electoral table : " + this.tablePresident +
                "\n" +
                "Turn electoral Table-schedule : " + this.turn +
                "\n";
    }
}
