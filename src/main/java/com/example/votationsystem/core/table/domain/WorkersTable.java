package com.example.votationsystem.core.table.domain;

import com.example.votationsystem.core.person.domain.Voter;

import java.util.HashMap;
import java.util.Iterator;

public class WorkersTable extends Table{
    public WorkersTable(Voter tablePresident) {
        super(tablePresident);
    }

    @Override
    public void createTimeSlots() {
        HashMap<Integer, Integer> zones = getTimeZones();
        zones.put(8, 0);
    }

    @Override
    public int searchTimeZone() {
        Iterator<Integer> it = getTimeZones().keySet().iterator();
        return it.next();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
