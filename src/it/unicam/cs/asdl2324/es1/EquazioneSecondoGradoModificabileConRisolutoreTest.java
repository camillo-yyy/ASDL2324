/**
 * 
 */
package it.unicam.cs.asdl2324.es1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Template: Luca Tesei, Implementation: Samuele Camilletti
 *
 */
class EquazioneSecondoGradoModificabileConRisolutoreTest {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    static final double EPSILON = 1.0E-15;

    @Test
    final void testEquazioneSecondoGradoModificabileConRisolutore() {
        // controllo che il valore 0 su a lanci l'eccezione
        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(0, 1,
                        1));
        // devo controllare che comunque nel caso normale il costruttore
        // funziona
        EquazioneSecondoGradoModificabileConRisolutore eq = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        // Controllo che all'inizio l'equazione non sia risolta
        assertFalse(eq.isSolved());
    }

    @Test
    final void testGetA() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                x, 1, 1);
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getA());
        // in generale si dovrebbe usare assertTrue(Math.abs(x -
        // e1.getA())<EPSILON) ma in
        // questo caso il valore che testiamo non ha subito manipolazioni quindi
        // la sua rappresentazione sarà la stessa di quella inserita nel
        // costruttore senza errori di approssimazione

    }

    @Test
    final void testSetA() {
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                10, 1, 1);

        e1.solve();

        assertTrue(e1.isSolved());

        assertThrows(IllegalArgumentException.class,
                () -> e1.setA(0));

        e1.setA(20);
        // controllo che il valore sia stato settato
        assertTrue(20 == e1.getA());
        // Controllo che lo stato sia stato resettato
        assertFalse(e1.isSolved());
    }

    @Test
    final void testGetB() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                2, x, 1);

        assertTrue(x == e1.getB());
    }

    @Test
    final void testSetB() {
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                10, 1, 1);

        e1.solve();

        assertTrue(e1.isSolved());

        e1.setB(20);
        // controllo che il valore sia stato settato
        assertTrue(20 == e1.getB());
        // Controllo che lo stato sia stato resettato
        assertFalse(e1.isSolved());
    }

    @Test
    final void testGetC() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                2, 1, x);

        assertTrue(x == e1.getC());
    }

    @Test
    final void testSetC() {
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                10, 1, 1);

        e1.solve();

        assertTrue(e1.isSolved());

        e1.setC(20);
        // controllo che il valore sia stato settato
        assertTrue(20 == e1.getC());
        // Controllo che lo stato sia stato resettato
        assertFalse(e1.isSolved());
    }

    @Test
    final void testIsSolved() {
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                10, 1, 1);

        assertFalse(e1.isSolved());
        
        e1.solve();

        assertTrue(e1.isSolved());
    }

    @Test
    final void testSolve() {
        EquazioneSecondoGradoModificabileConRisolutore e3 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 3);
        // controllo semplicemente che la chiamata a solve() non generi errori
        e3.solve();
        // i test con i valori delle soluzioni vanno fatti nel test del metodo
        // getSolution()
    }

    @Test
    final void testGetSolution() {

        // Test a 2 soluzioni
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                2, 3, 1);

        e1.solve();

        SoluzioneEquazioneSecondoGrado s1 = e1.getSolution();

        assertTrue(-0.5 + s1.getS1() < EPSILON);

        assertTrue(-1.0 + s1.getS2() < EPSILON);
        
        // Test a 1 soluzione
        EquazioneSecondoGradoModificabileConRisolutore e2 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 2, 1);

        e2.solve();

        SoluzioneEquazioneSecondoGrado s2 = e2.getSolution();

        assertTrue(s2.isOneSolution());

        assertTrue(-1.0 + s1.getS1() < EPSILON);
        
        // Test a 0 soluzioni
        EquazioneSecondoGradoModificabileConRisolutore e3 = new EquazioneSecondoGradoModificabileConRisolutore(
                8, 1, 1);

        e3.solve();

        SoluzioneEquazioneSecondoGrado s3 = e3.getSolution();

        assertTrue(s3.isEmptySolution());
       
    }

}
