package com.example.votationsystem.core.table.domain;

import com.example.votationsystem.core.person.domain.Voter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PreExistingDiseaseTable extends Table{
    public PreExistingDiseaseTable(Voter tablePresident) {
        super(tablePresident);
    }

    @Override
    public void createTimeSlots() {
        HashMap<Integer, Integer> timeZones = getTimeZones();
        for (int i = 8; i < 18; i++) {
            timeZones.put(i, 0);
        }
    }

    @Override
    public int searchTimeZone() {
        Iterator<Map.Entry<Integer, Integer>> it = this.getTimeZones().entrySet().iterator();
        int avaibleZones=-1;
        while (it.hasNext() && avaibleZones==-1) {
            Map.Entry<Integer, Integer> timeZone = it.next();
            if (timeZone.getValue()<20) {
                avaibleZones= timeZone.getKey();
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
