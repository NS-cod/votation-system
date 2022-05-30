package com.example.votationsystem.core.person.application.domain;

import com.example.votationsystem.core.person.domain.Voter;
import com.example.votationsystem.core.table.domain.*;
import com.example.votationsystem.shared.TableType;
import org.javatuples.Pair;

import java.util.*;

public class VotationSystemDomain {
    private final String systemName;
    private final HashMap<Integer, Voter> registeredPersons;
    private final Set<Table> tables;

    public VotationSystemDomain(String systemName) {
        if(systemName == null) {
            throw new RuntimeException();
        }
        this.systemName = systemName;
        this.registeredPersons = new HashMap<Integer, Voter>();
        this.tables = new HashSet<Table>();
    }

    public boolean thePersonIsRegistered(int dni) {
        return this.registeredPersons.containsKey(dni);
    }
    public String testRequest(String name) {
        return "funciona votation System service"+name;
    }
    public void voterRegister(int dni, String nombre, int edad, boolean enfPrevia, boolean trabaja) {
        //Verificacion edad y nombre contemplado en Contructor de Votante
        if(thePersonIsRegistered(dni))
            throw new IsRegistered(dni);

        registeredPersons.put(dni, new Voter(nombre,dni,edad,enfPrevia,trabaja));
    }

    private boolean verifyTableType(String tableType)   {
        return tableType.equals(TableType.Pre_existing_disease.toString()) ||
                tableType.equals(TableType.OlderThan65.toString()) ||
                tableType.equals(TableType.Worker.toString()) ||
                tableType.equals(TableType.General.toString());
    }

    private boolean verifyTablePresident(int dni) {
        if(!thePersonIsRegistered(dni))
            return false;
        return !this.registeredPersons.get(dni).haveTurn();
    }

    public int addTable(String tableType, int dni){
        if(!verifyTableType(tableType) || !verifyTablePresident(dni)) {
            throw new InvalidTableCreationAttempt(tableType,dni);
        }
        Voter president = this.registeredPersons.get(dni);
        Table new_table = null;

        if(tableType.equals(TableType.Pre_existing_disease.toString())) {
            new_table = new PreExistingDiseaseTable(president);
            this.tables.add(new_table);
        }else if(tableType.equals(TableType.OlderThan65.toString())){
            new_table = new OlderTable(president);
            this.tables.add(new_table);
        }else if(tableType.equals(TableType.Worker.toString())) {
            new_table = new WorkersTable(president);
            this.tables.add(new_table);
        }else {
            new_table = new GenericTable(president);
            this.tables.add(new_table);
        }

        return new_table.getTableCode();

    }

    private Pair<Integer, Integer> searchTurnOnTable(String tableType){
        if(!verifyTableType(tableType)) {
            throw new RuntimeException();
        }

        Pair<Integer,Integer> turn = null;
        for (Table mesa : tables) {
            if(tableType.equals(TableType.OlderThan65.toString())) {
                if(mesa instanceof OlderTable)
                    turn = mesa.giveMeATurn();
            }else if(tableType.equals(TableType.Pre_existing_disease.toString())) {
                if(mesa instanceof PreExistingDiseaseTable)
                    turn = mesa.giveMeATurn();
            }else if(tableType.equals(TableType.Worker.toString())) {
                if(mesa instanceof WorkersTable)
                    turn = mesa.giveMeATurn();
            }else if(tableType.equals(TableType.General.toString())) {
                if(mesa instanceof GenericTable)
                    turn = mesa.giveMeATurn();
            }
        }
        return turn;
    }

    /**
     * -Si es trabajador vota en mesa trabajadores
     * -Si es mayor y enfermedadPrex, en cualquier mesa con disponibilidad
     * -Sino, generica
     */
    public Pair<Integer,Integer> asignTurn(int dni){
        if(!thePersonIsRegistered(dni)) {
            throw new IsNotRegistered(dni);
        }

        Voter voter = this.registeredPersons.get(dni);
        if(voter.haveTurn()) {
            return voter.consultTurn();
        }

        Pair<Integer, Integer> turn = null;
        if(voter.haveWorkCertifiate()) {
            turn = searchTurnOnTable(TableType.Worker.toString());
        }else if(voter.over65() && voter.preExistingDisease()) {
            turn = searchTurnOnTable(TableType.OlderThan65.toString());
            if(turn == null)
                turn = searchTurnOnTable(TableType.Pre_existing_disease.toString());
        }else if(voter.over65()) {
            turn = searchTurnOnTable(TableType.OlderThan65.toString());
        }else if(voter.preExistingDisease()) {
            turn = searchTurnOnTable(TableType.Pre_existing_disease.toString());
        }else {
            turn = searchTurnOnTable(TableType.General.toString());
        }

        if(turn != null) {
            voter.asingTurn(turn.getValue0(), turn.getValue1());
        }
        return turn;
    }

    public int asignTurns() {
        int amountOfAssignedTurns = 0;
        Set<Integer> dniVoters = this.registeredPersons.keySet();
        for (Integer dniVoter : dniVoters) {
            if(consultTurn(dniVoter)==null && asignTurn(dniVoter)!=null) { //Si el votante no tiene turno //Si se asignó un turno al votante
                    amountOfAssignedTurns++;
            }
        }
        return amountOfAssignedTurns;
    }


    public Pair<Integer, Integer> consultTurn(int dni) {
        if(!thePersonIsRegistered(dni)) {
            throw new IsNotRegistered(dni);
        }
        return this.registeredPersons.get(dni).consultTurn();
    }

    public boolean vote(int dni){
        if (!thePersonIsRegistered(dni))
            throw new IsNotRegistered(dni);

        Voter voter= this.registeredPersons.get(dni);
        if (voter.consultVote())
            return false;

        voter.confirmVote();
        return true;
    }

    public int votersWithTurn(String tableType) {
        if (!verifyTableType(tableType))
            throw new InvalidTableType(tableType);

        int totalAmountOfVoterOnTable = 0;

        for (Table mesa : tables) {
            if (tableType.equals(TableType.OlderThan65.toString())) {
                if (mesa instanceof OlderTable)
                    totalAmountOfVoterOnTable += mesa.amountOfVoters();
            } else if (tableType.equals(TableType.Pre_existing_disease.toString())) {
                if (mesa instanceof PreExistingDiseaseTable)
                    totalAmountOfVoterOnTable += mesa.amountOfVoters();
            } else if (tableType.equals(TableType.Worker.toString())) {
                if (mesa instanceof WorkersTable)
                    totalAmountOfVoterOnTable += mesa.amountOfVoters();
            } else if (mesa instanceof GenericTable) {
                totalAmountOfVoterOnTable += mesa.amountOfVoters();
            }
        }
        return totalAmountOfVoterOnTable;
    }

    private Table searchTable(int tableNumber) {
        Table m = null;
        for (Table table : tables) {
            if(table.getTableCode()==tableNumber)
                m = table;
        }
        return m;
    }

    /**
     * Llena cada clave del Map con cada franja horaria de una mesa
     *
     */
    private void fillTimeSlots(Map<Integer, List<Integer>> list, Table table) {
        Set<Integer> timeSlots = table.getTimeZones().keySet();
        for (Integer timeSlot : timeSlots) {
            list.put(timeSlot, new LinkedList<Integer>());
        }
    }

    /**
     * Llena la lista con votantes pertenecientes a la mesa numMesa, dependiendo de que franja
     * horaria tenga asignada el votante
     */
    private void fillListWithVotersOnTable(Map<Integer,List<Integer>> list, int tableNumber) {
        Collection<Voter> voters = this.registeredPersons.values();
        for(Voter voter : voters) {
            if(voter.isOnTable(tableNumber))
                list.get(voter.consultSchedule()).add(voter.getDni());
        }
    }

    public Map<Integer,List<Integer>> asignedToTable(int tableNumber){
        Table m = searchTable(tableNumber);

        if(m==null)
            throw new TableNotExist(tableNumber);

        Map<Integer,List<Integer>> lista = new HashMap<Integer,List<Integer>>();
        fillTimeSlots(lista, m);
        fillListWithVotersOnTable(lista, tableNumber);

        return lista;
    }


    public List<Pair<String, Integer>> personsWithoutTurnsByTypeOfTable(){
        int amountOfOlders = 0;
        int amountOfGeneral = 0;
        int amountOfPreExistingDisease = 0;
        int amountOfWorkers = 0;

        Set<Integer> dniVoters = this.registeredPersons.keySet();
        Voter voter = null;
        List<Pair<String,Integer>> list = new LinkedList<Pair<String,Integer>>();

        for (Integer dniVoter : dniVoters) {
            voter = this.registeredPersons.get(dniVoter);
            if(!voter.haveTurn()) {
                if(voter.haveWorkCertifiate()) {
                    amountOfWorkers++;
                }else if(voter.preExistingDisease()) {
                    amountOfPreExistingDisease++;
                }else if(voter.over65()) {
                    amountOfOlders++;
                }else {
                    amountOfGeneral++;
                }
            }
        }

        list.add(new Pair<String,Integer>(TableType.Worker.toString(), amountOfWorkers));
        list.add(new Pair<String,Integer>(TableType.General.toString(), amountOfGeneral));
        list.add(new Pair<String,Integer>(TableType.OlderThan65.toString(), amountOfOlders));
        list.add(new Pair<String,Integer>(TableType.Pre_existing_disease.toString(), amountOfPreExistingDisease));

        return list;
    }
    public HashMap<Integer, Voter> getRegisteredPersons() {
        return registeredPersons;
    }

    public Set<Table> getTables() {
        return tables;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Pair<String, Integer>> withoutTurn= personsWithoutTurnsByTypeOfTable();
        Set<Integer> dniVoters= this.registeredPersons.keySet();
        Voter voter = null;

        sb.append(this.systemName).append("\n");
        sb.append("Voters without turn:  \n");

        for (Pair<String,Integer> tableType : withoutTurn) {
            sb.append(tableType).append("\n");
        }

        sb.append("Voters with turn: \n");
        for (Integer dni : dniVoters) {
            voter = this.registeredPersons.get(dni);
            if (voter.haveTurn()) {
                sb.append("DNI: ").append(dni).append(", Turn: ").append(voter.consultTurn());
                sb.append("  Voted: ").append(voter.consultTurn()).append("\n");
            }
        }

        for (Table mesa : this.tables) {
            sb.append(mesa);
        }
        return sb.toString();

    }

}
