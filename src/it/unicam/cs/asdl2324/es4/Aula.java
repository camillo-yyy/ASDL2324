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
        if(nome == null || location == null) throw new NullPointerException("Oggetti non esistenti");
        this.nome = nome;
        this.location = location;
        
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
        if(obj == null) return false;
        if(!(obj instanceof Aula)) return false;

        Aula a = (Aula) obj;

        if(this.nome.equals(a.getNome())) return true;

        else return false;
    }

    /* L'ordinamento naturale si basa sul nome dell'aula */
    @Override
    public int compareTo(Aula o) {
        return this.nome.compareTo(o.getNome());
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
        if(f == null) throw new NullPointerException("Facility inesistente.");

        if(this.numFacilities == this.facilities.length) this.extendFacilities();

        if(!this.isPresent(f)) { 
            this.facilities[this.numFacilities++] = f; 
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

         if(ts == null) throw new NullPointerException("Timeslot inesistente.");

        for(int i=0; i<this.numPrenotazioni; i++) {

            if(this.prenotazioni[i].getTimeSlot().getMinutesOfOverlappingWith(ts) > 0) return false;

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
        if(requestedFacilities == null) throw new NullPointerException("Array inesistente.");

        boolean flag;

        for(int i=0; i<requestedFacilities.length; i++) {

            flag = false;

            if(requestedFacilities[i] == null) flag = true;

            for(int j=0; (j<numFacilities) && (flag != true); j++) {

                if(this.facilities[j].satisfies(requestedFacilities[i])) flag = true;
            }
            
            if(flag == false) return false;

        }
        return true;
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
        if(ts == null || docente == null || motivo == null)  throw new NullPointerException("Facility inesistente.");
        
        // Controlla se l'aula è libera in quel timeslot
        if(!this.isFree(ts)) throw new IllegalArgumentException("Aula occupata in quel timeslot");
        
        if(this.numPrenotazioni == this.prenotazioni.length) this.extendPrenotazioni();

        this.prenotazioni[this.numPrenotazioni++] = new Prenotazione(this, ts, docente, motivo); 
    }

    // TODO inserire eventuali metodi privati per questioni di organizzazione

    /**
     * PControlla se la facility è esistente
     * 
     * @param f
     * @return true
     *                              se la facility passata è presente nell'array dell'oggetto aula
     *         false                        
     *                              altrimenti
     */
    public boolean isPresent(Facility f)  {

        for(int i=0; i<this.numFacilities; i++) {

            if(this.facilities[i].equals(f)) return true;

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

        if(this.numFacilities != this.facilities.length) throw new IllegalStateException("stato illegale");

        Facility[] temp = new Facility[this.facilities.length*2];

        for(int i=0; i<this.numFacilities; i++) {

            temp[i] = facilities[i];

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

        if(this.numPrenotazioni != this.prenotazioni.length) throw new IllegalStateException("stato illegale");

        Prenotazione[] temp = new Prenotazione[this.prenotazioni.length*2];

        for(int i=0; i<this.numPrenotazioni; i++) {

            temp[i] = prenotazioni[i];

        }

        prenotazioni = temp;

    }

}
