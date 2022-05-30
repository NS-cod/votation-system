package com.example.votationsystem.core.table.domain;

import com.example.votationsystem.core.person.domain.Voter;
import org.javatuples.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public abstract class Table {
    private final int codeID;
    private static int codCount = 0;
    private Voter tablePresident;
    private final HashMap<Integer,Integer> timeZones; // (franja-cupo)

    public Table(Voter tablePresident) {
        if(tablePresident == null) {
            throw new RuntimeException();
        }
        this.codeID = codCount++;
        this.timeZones= new HashMap<>();
        createTimeSlots();
        asingPresident(tablePresident);
    }

    public abstract void createTimeSlots();

    public void asingPresident(Voter voter){
        if(voter == null || voter.isTablePresident() || existTablePresident()) {
            throw new RuntimeException();
        }

        this.tablePresident=voter;
        Pair<Integer, Integer> turn = giveMeATurn();
        tablePresident.asingAsTablePresident();
        tablePresident.asingTurn(turn.getValue0(), turn.getValue1());
    }

    public boolean existTablePresident() {
        return this.tablePresident!=null ;
    }


    public int getTableCode() {
        return this.codeID;
    }

    public void updateQuota(int timeZone) {
        this.timeZones.replace(timeZone, this.timeZones.get(timeZone)+1);
    }

    public abstract int searchTimeZone();

    public Pair<Integer,Integer> giveMeATurn() {
        int avaibleTimeSlot = searchTimeZone();
        if (avaibleTimeSlot!=-1) {
            Pair<Integer,Integer> turn= new Pair<>(getTableCode(),avaibleTimeSlot);
            updateQuota(avaibleTimeSlot);
            return turn;
        }
        else
            return null;
    }

    public HashMap<Integer, Integer> getTimeZones() {
        return timeZones;
    }

    public int amountOfVoters() {
        int voterQuantity=0;
        Collection<Integer> slots = this.timeZones.values();
        for (Integer slot : slots) {
            voterQuantity += slot;
        }
        return voterQuantity;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Table m = (Table) obj;
        return codeID == m.codeID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------------------------------------------\n");
        sb.append("Mesa N° ").append(this.codeID).append("\n");
        sb.append(this.getClass().getName()).append("\n");
        sb.append("President de mesa :").append(this.tablePresident.getName()).append(" DNI: ");
        sb.append(this.tablePresident.getDni()).append("\n");

        Set<Integer> schedules = this.timeZones.keySet();
        for (Integer schedule : schedules) {
            sb.append("Schedule: ").append(schedule).append(" \n" +
                    "Assigned quotas: ");
            sb.append(this.timeZones.get(schedule)).append("\n");
        }

        return sb.toString();
    }
}
