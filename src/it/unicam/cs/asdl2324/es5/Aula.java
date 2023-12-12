package it.unicam.cs.asdl2324.es5;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 */
public class Aula implements Comparable<Aula> {
    
    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    // Insieme delle facilities di quest'aula
    private final Set<Facility> facilities;

    // Insieme delle prenotazioni per quest'aula, segue l'ordinamento naturale
    // delle prenotazioni
    private final SortedSet<Prenotazione> prenotazioni;

    /**
     * Costruisce una certa aula con nome e location. Il set delle facilities è
     * vuoto. L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                     il nome dell'aula
     * @param location
     *                     la location dell'aula
     * 
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location) {
        // Controllo se i parametri passati sono null
        if(nome == null || location == null) throw new NullPointerException("Oggetti non esistenti");

        this.nome = nome;
        this.location = location;
        this.facilities = new HashSet<Facility>();
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /**
     * Costruisce una certa aula con nome, location e insieme delle facilities.
     * L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                       il nome dell'aula
     * @param location
     *                       la location dell'aula
     * @param facilities
     *                       l'insieme delle facilities dell'aula
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location, Set<Facility> facilities) {
        // Controllo se i parametri passati sono null
        if(nome == null || location == null || facilities == null) throw new NullPointerException("Oggetti non esistenti");

        this.nome = nome;
        this.location = location;
        this.facilities = facilities;
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /*
     * Ridefinire in accordo con equals
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;

        // si usa il valore intero corrispondente alla rappresentazione del
        // double bit a bit (64 bit, cio� un long)
        temp = Double.doubleToLongBits(this.nome.hashCode());
        // si fa il bitwise XOR tra i 64 bit originali e il loro shift a destra
        // di 32 bit, poi si fa il cast a int
        result = prime * result + (int) (temp ^ (temp >>> 32));

        return result;
    }

    /* Due aule sono uguali se e solo se hanno lo stesso nome */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(obj == null) return false;     // Se è null
        if(!(obj instanceof Aula)) return false; // Se non è un Aula

        Aula a = (Aula) obj; // Cast Obj -> aula

        if(this.nome.equals(a.getNome())) return true; // Se hanno lo stesso nome
        else return false;
    }

    /* L'ordinamento naturale si basa sul nome dell'aula */
    @Override
    public int compareTo(Aula o) {
        return this.nome.compareTo(o.getNome()); // Richiamo il compareTo tra stringhe
    }

    /**
     * @return the facilities
     */
    public Set<Facility> getFacilities() {
        return facilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the prenotazioni
     */
    public SortedSet<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula.
     * 
     * @param f
     *              la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     *         aggiunta, false altrimenti
     * @throws NullPointerException
     *                                  se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        if(f == null) throw new NullPointerException("Oggetto nullo");

        if(this.facilities.add(f)) return true; // Se non esiste nell'insieme aggiunge e ritorna true
        else return false;
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
     * 
     * @param ts
     *               il time slot da controllare
     * 
     * @return true se l'aula risulta libera per tutto il periodo del time slot
     *         specificato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean isFree(TimeSlot ts) {

        /*
         * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in
         * maniera efficiente: poiché le prenotazioni sono in ordine crescente
         * di time slot se arrivo a una prenotazione che segue il time slot
         * specificato posso concludere che l'aula è libera nel time slot
         * desiderato e posso interrompere la ricerca
         */
        if(ts == null) throw new NullPointerException("Timeslot inesistente."); // Se il timeslot non esiste

        // Verifico tra tutte le prenotazioni se ne esiste una con un TimeSlot che si sovrappone con il TimeSlot passato
        for (Prenotazione p : prenotazioni){

            if(p.getTimeSlot().getStart().after(ts.getStop())) return true;
            if(p.getTimeSlot().overlapsWith(ts)) return false; // Se esiste ritorno false

        }

        return true;
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Set<Facility> requestedFacilities) {
        if(requestedFacilities == null) throw new NullPointerException("Set inesistente."); // Se il set non esiste

        boolean flag;

        for(Facility rF : requestedFacilities) { // Scorro sul set di facility

            // Resetto il flag per la facility da analizzare
            flag = false;

            if(rF == null) flag = true; // Se la facility passata non esiste è automaticamente soddisfatta dall'Aula

            Iterator<Facility> fIterator = this.facilities.iterator();
            Facility f;

            // Scorro il set di facility dell'aula finché non trovo la facility richiesta, utilizzo  l'iteratore per la doppia condizione
            while(fIterator.hasNext() && (flag != true)) { 

                f = fIterator.next();
                // Controllo se ne esiste almeno una che soddisfa la facility richiesta
                if(f.satisfies(rF)) flag = true; 

            }
            
            if(!flag) return false;         // Se il flag non viene mai messo a true per una determinata facility 
                                            // significa che non viene soddisfatta e pertanto ritorno false 

        }
        return true; // Se tutte le facility sono soddisfatte
    }

    /**
     * Prenota l'aula controllando eventuali sovrapposizioni.
     * 
     * @param ts
     * @param docente
     * @param motivo
     * @throws IllegalArgumentException
     *                                      se la prenotazione comporta una
     *                                      sovrapposizione con un'altra
     *                                      prenotazione nella stessa aula.
     * @throws NullPointerException
     *                                      se una qualsiasi delle informazioni
     *                                      richieste è nulla.
     */
    public void addPrenotazione(TimeSlot ts, String docente, String motivo) {
        if(ts == null || docente == null || motivo == null)  throw new NullPointerException("Facility inesistente."); // Se i parametri non esistono

        // Controlla se l'aula è libera in quel timeslot
        if(!this.isFree(ts)) throw new IllegalArgumentException("Aula occupata in quel timeslot");

        this.prenotazioni.add(new Prenotazione(this, ts, docente, motivo));
    }

    /**
     * Cancella una prenotazione di questa aula.
     * 
     * @param p
     *              la prenotazione da cancellare
     * @return true se la prenotazione è stata cancellata, false se non era
     *         presente.
     * @throws NullPointerException
     *                                  se la prenotazione passata è null
     */
    public boolean removePrenotazione(Prenotazione p) {
        if(p == null) throw new NullPointerException("Prenotazione inesistente."); // Se il set non esiste

        if(this.prenotazioni.contains(p)) { //  Se lo contiene lo rimuovo
            this.prenotazioni.remove(p);
            return true;
        }
        else return false; // Altrimenti
    }

    /**
     * Rimuove tutte le prenotazioni di questa aula che iniziano prima (o
     * esattamente in) di un punto nel tempo specificato.
     * 
     * @param timePoint
     *                      un certo punto nel tempo
     * @return true se almeno una prenotazione è stata cancellata, false
     *         altrimenti.
     * @throws NullPointerException
     *                                  se il punto nel tempo passato è nullo.
     */
    public boolean removePrenotazioniBefore(GregorianCalendar timePoint) {
        /*
         * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in
         * maniera efficiente: poiché le prenotazioni sono in ordine crescente
         * di time slot se ho raggiunto una prenotazione con tempo di inizio
         * maggiore del tempo indicato posso smettere la procedura
         */
        if(timePoint == null) throw new NullPointerException("timePoint inesistente."); // Se il set non esiste

        boolean flag = false;
        Iterator<Prenotazione> pIterator = this.prenotazioni.iterator();
        Prenotazione p;

        // Scorro il set di prenotazioni
        while(pIterator.hasNext()) { 
            p = pIterator.next();
            
            if(p.getTimeSlot().getStart().compareTo(timePoint) <= 0) { // Se ho superato temporalmente il punto posso concludere
                pIterator.remove(); 
                flag = true; 
            } 
            else {
                return flag;
            }

        }
        
        return flag;
    }
}
