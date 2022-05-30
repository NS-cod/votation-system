package com.example.votationsystem.core.person.application.app;

import com.example.votationsystem.core.person.application.domain.VotationSystemDomain;
import org.javatuples.Pair;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class VotationService {
    private static final VotationSystemDomain system = new VotationSystemDomain("My Votation System");

    public VotationService(String votation_test) {
    }


    @RequestMapping(value="/consultTurn", method= RequestMethod.GET)
    public String getTurn(Integer dni){
        return system.consultTurn(dni).toString();
    }

    @RequestMapping(value="/registrado", method=RequestMethod.GET)
    public boolean isRegistered(Integer dni){
        return system.thePersonIsRegistered(dni);
    }

    @RequestMapping(value="/testRequest", method=RequestMethod.GET)
    public String getTest(String name){
        return system.testRequest(name);
    }

    @RequestMapping(value="/asignedToTable", method=RequestMethod.GET)
    public Map<Integer, List<Integer>> asignedToTable(Integer tableId){
        return system.asignedToTable(tableId);
    }

    @RequestMapping(value="/asingTurn", method=RequestMethod.PUT)
    public String asignTurn(Integer dni){
        return system.asignTurn(dni).toString();
    }

    @RequestMapping(value="/agregateTable", method=RequestMethod.POST)
    public int asignTable(String tableType, Integer amount){

        return system.addTable(tableType,amount);
    }
}
