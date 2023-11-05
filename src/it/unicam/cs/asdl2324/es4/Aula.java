package it.unicam.cs.asdl2324.es4;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class Aula implements Comparable<Aula> {

    /*
     * numero iniziale delle posizioni dell'array facilities. Se viene richiesto
     * di inserire una facility e l'array è pieno questo viene raddoppiato. La
     * costante è protected solo per consentirne l'accesso ai test JUnit
     */
    protected static final int INIT_NUM_FACILITIES = 5;

    /*
     * numero iniziale delle posizioni dell'array prenotazioni. Se viene
     * richiesto di inserire una prenotazione e l'array è pieno questo viene
     * raddoppiato. La costante è protected solo per consentirne l'accesso ai
     * test JUnit.
     */
    protected static final int INIT_NUM_PRENOTAZIONI = 100;

    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    /*
     * Insieme delle facilities di quest'aula. L'array viene creato all'inizio
     * della dimensione specificata nella costante INIT_NUM_FACILITIES. Il
     * metodo addFacility(Facility) raddoppia l'array qualora non ci sia più
     * spazio per inserire la facility.
     */
    private Facility[] facilities;

    // numero corrente di facilities inserite
    private int numFacilities;

    /*
     * Insieme delle prenotazioni per quest'aula. L'array viene creato
     * all'inizio della dimensione specificata nella costante
     * INIT_NUM_PRENOTAZIONI. Il metodo addPrenotazione(TimeSlot, String,
     * String) raddoppia l'array qualora non ci sia più spazio per inserire la
     * prenotazione.
     */
    private Prenotazione[] prenotazioni;

    // numero corrente di prenotazioni inserite
    private int numPrenotazioni;

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
        
        // Alloco due array statici di dimensione fissata e setto i counter a 0
        this.numPrenotazioni = 0;
        this.prenotazioni = new Prenotazione[INIT_NUM_PRENOTAZIONI]; 
        this.numFacilities = 0;
        this.facilities = new Facility[INIT_NUM_FACILITIES];        

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
    public Facility[] getFacilities() {
        return this.facilities;
    }

    /**
     * @return il numero corrente di facilities
     */
    public int getNumeroFacilities() {
        return this.numFacilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @return the prenotazioni
     */
    public Prenotazione[] getPrenotazioni() {
        return this.prenotazioni;
    }

    /**
     * @return il numero corrente di prenotazioni
     */
    public int getNumeroPrenotazioni() {
        return this.numPrenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula. Controlla se la facility è già
     * presente, nel qual caso non la inserisce.
     * 
     * @param f
     *              la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     *         aggiunta, false altrimenti
     * @throws NullPointerException
     *                                  se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        /*
         * Nota: attenzione! Per controllare se una facility è già presente
         * bisogna usare il metodo equals della classe Facility.
         * 
         * Nota: attenzione bis! Si noti che per le sottoclassi di Facility non
         * è richiesto di ridefinire ulteriormente il metodo equals...
         */
        if(f == null) throw new NullPointerException("Facility inesistente."); // Se la facilty non esiste

        if(this.numFacilities == this.facilities.length) this.extendFacilities(); // Se è stata raggiunta la dim. massima estendo l'array

        // Controllo se è già presente
        if(!this.isPresent(f)) { 
            this.facilities[this.numFacilities++] = f; // Assegno il puntatore e scorro il counter con post-incremento
            return true; 
        }
        else return false;
    }


    /**
     * Determina se l'aula è libera in un certo time slot.
     * 
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

        if(ts == null) throw new NullPointerException("Timeslot inesistente."); // Se il timeslot non esiste

        // Verifico tra tutte le prenotazioni se ne esiste una con un TimeSlot che si sovrappone con il TimeSlot passato
        for(int i=0; i<this.numPrenotazioni; i++) {

            if(this.prenotazioni[i].getTimeSlot().getMinutesOfOverlappingWith(ts) > 0) return false; // Se esiste ritorno false

        }

        return true;
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare, sono da considerare solo le
     *                                posizioni diverse da null
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Facility[] requestedFacilities) {
        if(requestedFacilities == null) throw new NullPointerException("Array inesistente."); // Se l'array non esiste

        boolean flag;

        for(int i=0; i<requestedFacilities.length; i++) { // Scorro sull'array di facility

            // Resetto il flag per la facility da analizzare
            flag = false;

            if(requestedFacilities[i] == null) flag = true; // Se la facility passata non esiste è automaticamente soddisfatta dall'Aula

            for(int j=0; (j<numFacilities) && (flag != true); j++) { // Scorro l'array di facility dell'aula finché non trovo la facility richiesta

                // Controllo se ne esiste almeno una che soddisfa la facility richiesta
                if(this.facilities[j].satisfies(requestedFacilities[i])) flag = true; 

            }
            
            if(flag == false) return false; // Se il flag non viene mai messo a true per una determinata facility 
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
        
        if(this.numPrenotazioni == this.prenotazioni.length) this.extendPrenotazioni(); // Se è stata raggiunta la dim. massima estendo l'array

        this.prenotazioni[this.numPrenotazioni++] = new Prenotazione(this, ts, docente, motivo);  // Assegno il puntatore e scorro il counter con post-incremento
    }
    /**
     * Controlla se la facility è esistente
     * 
     * @param f
     * @return true se la facility passata è presente nell'array dell'oggetto aula
     *         false altrimenti
     */
    public boolean isPresent(Facility f)  {

        for(int i=0; i<this.numFacilities; i++) {

            if(this.facilities[i].equals(f)) return true; // Controllo se esiste una facility uguale nell'array rispetto a quella passata

        }

        return false;
    }

    /**
     * Duplica la lunghezza dell'array Facilities
     * 
     * @throws IllegalStateException
     *                                      se l'array non è pieno
     */
    public void extendFacilities() {
        // Verifico che la dimensione massima è stata raggiunta
        if(this.numFacilities != this.facilities.length) throw new IllegalStateException("stato illegale"); 

        Facility[] temp = new Facility[this.facilities.length*2]; // Rialloco un array di dimensione maggiorataa

        for(int i=0; i<this.numFacilities; i++) {

            temp[i] = facilities[i];    // Riassegno le facility al nuovo array

        }

        facilities = temp;

    }

        /**
     * Duplica la lunghezza dell'array Prenotazioni
     * 
     * @throws IllegalStateException
     *                                      se l'array non è pieno
     */
    public void extendPrenotazioni() {
        // Verifico che la dimensione massima è stata raggiunta
        if(this.numPrenotazioni != this.prenotazioni.length) throw new IllegalStateException("stato illegale");

        Prenotazione[] temp = new Prenotazione[this.prenotazioni.length*2];// Rialloco un array di dimensione maggiorataa

        for(int i=0; i<this.numPrenotazioni; i++) {

            temp[i] = prenotazioni[i]; // Riassegno le prenotazioni al nuovo array
 
        }

        prenotazioni = temp;

    }

}
