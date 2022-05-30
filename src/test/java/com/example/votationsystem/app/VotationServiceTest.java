package com.example.votationsystem.app;

import com.example.votationsystem.core.person.application.domain.VotationSystemDomain;
import com.example.votationsystem.shared.Fixture;
import com.example.votationsystem.shared.TableType;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VotationServiceTest {
    private final VotationSystemDomain system = new VotationSystemDomain("Voting Test");
    public static final Fixture F = Fixture.INSTANCE;

    @BeforeEach
    public void preSet(){
        system.voterRegister(
                F.dniFrodo,
                "Frodo",
                23,
                !F.havePreExistingDisease,
                !F.work
        );
        system.voterRegister(
                F.dniEowyn,
                "Eowyn",
                25,
                F.havePreExistingDisease,
                !F.work
        );
        system.voterRegister(
                F.dniBilbo,
                "Bilbo",
                65,
                F.havePreExistingDisease,
                !F.work
        );
        system.voterRegister(
                F.dniGandalf,
                "Gandalf",
                70,
                !F.havePreExistingDisease,
                F.work
        );
        system.voterRegister(
                F.dniLegolas,
                "Legolas",
                80,
                !F.havePreExistingDisease,
                F.work
        );
        system.voterRegister(
                F.dniGaladriel,
                "Galadriel",
                81,
                !F.havePreExistingDisease,
                F.work
        );
        system.voterRegister(
                F.dniArwen,
                "Arwen",
                50,
                !F.havePreExistingDisease,
                F.work
        );
        // # Votantes = 7
        // Mayores de 65 = 4
        // Trabajadores = 4
        // EnfPrexistente = 2
        system.addTable(F.preExistingDisease,F.dniFrodo);
        system.addTable(F.olderThan65,F.dniBilbo);
        system.addTable(F.general,F.dniGaladriel);
        system.addTable(F.worker,F.dniGandalf);
    }
    @Test
    public void tableAsignation() {
        assertNotNull(system.getTables());
        assertEquals(system.getTables().size(),4);

    }
    @Test
    public void consultTurn(){
        assertNotNull(system.consultTurn(F.dniFrodo));
        assertNotNull(system.consultTurn(F.dniBilbo));
        assertNotNull(system.consultTurn(F.dniGaladriel));
        assertNotNull(system.consultTurn(F.dniGandalf));
    }

    /*
     * Al querer crear una mesa sin un votante
     * registrado deberia devolver una excepcion
     */
    @Test
    public void asingUnregisterdDniToTable() {
        try {
            system.addTable(F.worker,F.notRegisteredDni);;
            //Si llego hasta aca esta mal! deberia haber fallado
            fail();
        }catch(Exception e) {
            //No hay mesa para el Dni sin registrar
            assertNotNull(e);
        }
    }

    /*
    /*
     * Al querer crear una mesa con un tipo de mesa invalido
     * deberia devolver una excepcion
     */
    @Test
    public void asignInvalidTableType() {
        try {
            system.addTable(F.invalidTableType,F.dniLegolas);
            //Si llego hasta aca esta mal! deberia haber fallado
            fail();
        } catch(Exception e) {
            //La mesa es invalida
            assertNotNull(e);
        }
    }

    /*
     * Se deberia asignar un turno para cada votante,
     * dado que no superan la capacidad total de las mesas
     * No valida que los votantes se hayan asignado a su mesa correspondiente
     */
    @Test
    public void asignTurnTest() {
        final int votantesEsperados = 3;

        assertEquals(votantesEsperados, system.asignTurns());
    }

    /*
     * Agrego una mesa solo para votantes trabajadores
     * Se espera que solo se asignen los votantes trabajadores + presidente mesa:
     * En este caso no se valida el IREP de la franja horaria en si
     * Ya que cada algoritmo puede asignar a los votantes en distintas franjas
     */
    @Test
    public void numberOfVoterWithTurnOnTable() {
       Integer numberOFVotersOnTable= system.votersWithTurn(TableType.Pre_existing_disease.toString());
       assertEquals(1,numberOFVotersOnTable);

    }

    /*
     * No se deberian asignar turnos a trabajadores (Ya que no hay una mesa)
     */
    @Test
    public void personsAsignedWithoutTurn() {
        int amountOfPersons=0;
      List<Pair<String,Integer>> personswitoutTurn = system.personsWithoutTurnsByTypeOfTable();
        for (Pair<String,Integer> persons: personswitoutTurn) {
            amountOfPersons+=persons.getValue1();
        }
        assertEquals(3,amountOfPersons);
    }

    /*
     * Agrego una mesa general, como Frodo esta en el padron,
     * deberia asignarse un Turno
     */
    @Test
    public void asignTurn() {
        Pair<Integer,Integer> turn=system.asignTurn(F.dniArwen);
        assertNotNull(turn);
    }

    /*
     * Agrego una mesa general, como el dniSinVotante no esta en el padron,
     * no deberia asignarse un Turno y lanza una excepcion
     */
    @Test
    public void asignarTurnoDniInvalidoTest() {
        try {
            system.asignTurn(F.notRegisteredDni);
            fail();
        }catch(Exception e) {
            assertNotNull(e);
        }
    }

    /*
     * Agrego una mesa General y va a votar Frodo
     * Luego, intenta votar otra vez y devuelve False
     */
    @Test
    public void voteTest() {
        system.asignTurn(F.dniArwen);
        //Pudo votar
        assertTrue(system.vote(F.dniArwen));
        //No puede votar al intentar votar nuevamente
        assertFalse(system.vote(F.dniArwen));

    }

}
