package com.example.votationsystem.core.table.domain;

import com.example.votationsystem.core.person.domain.Voter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenericTable extends Table {
    public GenericTable(Voter tablePresident){
        super(tablePresident);
    }

    @Override
    public void createTimeSlots() {
        HashMap<Integer, Integer> zones = getTimeZones();
        for (int i = 8; i < 18; i++) {
            zones.put(i, 0);
        }
    }

    @Override
    public int searchTimeZone() {
        Iterator<Map.Entry<Integer, Integer>> it = this.getTimeZones().entrySet().iterator();
        int avaibleZones=-1;
        while (it.hasNext() && avaibleZones==-1) {
            Map.Entry<Integer, Integer> franja = it.next();
            if (franja.getValue()<30) {
                avaibleZones= franja.getKey();
            }
        }

        return avaibleZones;
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
