/**
 * 
 */
package it.unicam.cs.asdl2324.es10;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Realizza un insieme tramite una tabella hash con indirizzamento primario (la
 * funzione di hash primario deve essere passata come parametro nel costruttore
 * e deve implementare l'interface PrimaryHashFunction) e liste di collisione.
 * 
 * La tabella, poiché implementa l'interfaccia Set<E> non accetta elementi
 * duplicati (individuati tramite il metodo equals() che si assume sia
 * opportunamente ridefinito nella classe E) e non accetta elementi null.
 * 
 * La tabella ha una dimensione iniziale di default (16) e un fattore di
 * caricamento di defaut (0.75). Quando il fattore di bilanciamento effettivo
 * eccede quello di default la tabella viene raddoppiata e viene fatto un
 * riposizionamento di tutti gli elementi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
@SuppressWarnings({"unchecked"})
public class CollisionListResizableHashTable<E> implements Set<E> {

    /*
     * La capacità iniziale. E' una potenza di due e quindi la capacità sarà
     * sempre una potenza di due, in quanto ogni resize raddoppia la tabella.
     */
    private static final int INITIAL_CAPACITY = 16;

    /*
     * Fattore di bilanciamento di default. Tipico valore.
     */
    private static final double LOAD_FACTOR = 0.75;

    /*
     * Numero di elementi effettivamente presenti nella hash table in questo
     * momento. ATTENZIONE: questo valore è diverso dalla capacity, che è la
     * lunghezza attuale dell'array di Object che rappresenta la tabella.
     */
    private int size;

    /*
     * L'idea è che l'elemento in posizione i della tabella hash è un bucket che
     * contiene null oppure il puntatore al primo nodo di una lista concatenata
     * di elementi. Si può riprendere e adattare il proprio codice della
     * Esercitazione 6 che realizzava una lista concatenata di elementi
     * generici. La classe interna Node<E> è ripresa proprio da lì.
     * 
     * ATTENZIONE: la tabella hash vera e propria può essere solo un generico
     * array di Object e non di Node<E> per una impossibilità del compilatore di
     * accettare di creare array a runtime con un tipo generics. Ciò infatti
     * comporterebbe dei problemi nel sistema di check dei tipi Java che, a
     * run-time, potrebbe eseguire degli assegnamenti in violazione del tipo
     * effettivo della variabile. Quindi usiamo un array di Object che
     * riempiremo sempre con null o con puntatori a oggetti di tipo Node<E>.
     * 
     * Per inserire un elemento nella tabella possiamo usare il polimorfismo di
     * Object:
     * 
     * this.table[i] = new Node<E>(item, next);
     * 
     * ma quando dobbiamo prendere un elemento dalla tabella saremo costretti a
     * fare un cast esplicito:
     * 
     * Node<E> myNode = (Node<E>) this.table[i];
     * 
     * Ci sarà dato un warning di cast non controllato, ma possiamo eliminarlo
     * con un tag @SuppressWarning,
     */
    private Object[] table;

    /*
     * Funzion di hash primaria usata da questa hash table. Va inizializzata nel
     * costruttore all'atto di creazione dell'oggetto.
     */
    private final PrimaryHashFunction phf;

    /*
     * Contatore del numero di modifiche. Serve per rendere l'iterator
     * fail-fast.
     */
    private int modCount;

    // I due metodi seguenti sono di comodo per gestire la capacity e la soglia
    // oltre la quale bisogna fare il resize.

    /* Numero di elementi della tabella corrente */
    private int getCurrentCapacity() {
        return this.table.length;
    };

    /*
     * Valore corrente soglia oltre la quale si deve fare la resize,
     * getCurrentCapacity * LOAD_FACTOR
     */
    private int getCurrentThreshold() {
        return (int) (getCurrentCapacity() * LOAD_FACTOR);
    }

    /**
     * Costruisce una Hash Table con capacità iniziale di default e fattore di
     * caricamento di default.
     */
    public CollisionListResizableHashTable(PrimaryHashFunction phf) {
        this.phf = phf;
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        // TODO implementare
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente
         * 
         */
        if(o == null) 
            throw new NullPointerException("Elemento inesistente.");

        Node<E> temp; // sentinella
        int index = this.phf.hash(o.hashCode(),this.getCurrentCapacity());
        if((temp = (Node<E>) this.table[index]) != null) {

            // scorro lista 
            while(!(o.equals(temp.item)) && temp.next != null) temp = temp.next;
            
            // se non lo trovo
            if(!(o.equals(temp.item))) 
                return false;
            // se lo trovo
            else 
                return true;
        }
        else 
            return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean add(E e) {
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui inserire
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve inserire l'elemento o
         * nella lista concatenata lì presente. Se vuota, si crea la lista
         * concatenata e si inserisce l'elemento, che sarà l'unico.
         * 
         */
        // ATTENZIONE, si inserisca prima il nuovo elemento e poi si controlli
        // se bisogna fare resize(), cioè se this.size >
        // this.getCurrentThreshold()
        if(e == null) 
            throw new NullPointerException("Elemento inesistente.");

        // controllo se non presente
        if(!(this.contains(e))) {

            int index = this.phf.hash(e.hashCode(),this.getCurrentCapacity());
            Node<E> temp = (Node<E>) this.table[index];

            if(temp != null) {

                Node<E> newElement = new Node<E>(e, temp.next);
                temp.next = newElement;

            }
            else
                this.table[index] = new Node<E>(e, null);


        }
        // se  presente
        else {
            return false;
        }

        this.size++;
        this.modCount++;

        // se la tabella ha superato la soglia del fattore di carico, estendo
        if(this.size > this.getCurrentThreshold())
            this.resize();

        return true;
    }

    /*
     * Raddoppia la tabella corrente e riposiziona tutti gli elementi. Da
     * chiamare quando this.size diventa maggiore di getCurrentThreshold()
     */
    private void resize() {

        Object[] oldHash = this.table;
        int oldSize = this.getCurrentCapacity(); // salvo le vecchie dimensioni
        
        this.table = new Object[this.getCurrentCapacity()*2]; // alloco una nuova tabella dalla dimensione doppia
        Node<E> temp, next;


        for(int i=0; i<oldSize; i++) { // scorro l'hash table

            temp = (Node<E>) oldHash[i]; // prendo l'elemento di indice
            next = (Node<E>) oldHash[i];
            
            // scorro dal primo eventuale elemento l'intera lista di collisioni
            while(temp != null){ 
                
                int hash = this.phf.hash(temp.item.hashCode(),this.getCurrentCapacity());
                next = temp.next;
                temp.next = null;

                Node<E> bucket = (Node<E>) this.table[hash];

                if(bucket != null) { // se l'indice non è libero

                    // attacco dopo la testa
                    temp.next = bucket.next;
                    bucket.next = temp;

                }
                else   // altrimenti va nella posizione corrispondente al risultato della funzione hash
                    this.table[hash] = temp;
                
                temp = next;
            }

        }        

    }

    @Override
    public boolean remove(Object o) {
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente. Se presente, l'elemento deve essere
         * eliminato dalla lista concatenata
         * 
         */
        // ATTENZIONE: la rimozione, in questa implementazione, **non** comporta
        // mai una resize "al ribasso", cioè un dimezzamento della tabella se si
        // scende sotto il fattore di bilanciamento desiderato.

        if(o == null) 
            throw new NullPointerException("Elemento inesistente.");
        
        Node<E> temp; // sentinella
        Node<E> prec; // precedente di temp

        // controllo se è presente
        if(this.contains(o)) {

            // salvo il valore con quell'hash (testa della lista di collisione)
            int index = this.phf.hash(o.hashCode(),this.getCurrentCapacity());
            temp = (Node<E>) this.table[index];
            prec = temp; // il precedente coincide con la testa

            // scorro lista collisioni, sapendo che prima o poi lo troverò
            while(!(temp.item.equals(o))) {
                prec = temp;
                temp = temp.next;
            }

            if(this.table[index] == temp) { // è la testa della lista di collisione
                if(temp.next == null) 
                    this.table[index] = null; // cella hash libera
                // primo elemento della lista delle collisioni diventa testa della cella hash
                else 
                    this.table[index] = temp.next; 
            }
            // è un elemento della lista di collisioni
            else {
                prec.next = temp.next;
            }

            this.size--;
            this.modCount++;
            return true;
        }
        else 
            return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        boolean flag = true;
        Iterator<?> thisIterator = c.iterator();
        // uso l'iterator di questa classe
        while (thisIterator.hasNext() && flag) 
            if(!(this.contains(thisIterator.next()))) flag = false;

        return flag;

    }

    @Override
    public boolean addAll(Collection<? extends E> c) {

        boolean flag = false;
        Iterator<?> thisIterator = c.iterator();
        // uso l'iterator di questa classe
        while (thisIterator.hasNext()) 
            if((this.add((E) thisIterator.next()))) flag = true;

        return flag;

    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        boolean flag = false;
        Iterator<?> thisIterator = c.iterator();
        // uso l'iterator di questa classe
        while (thisIterator.hasNext()) 
            if((this.remove((E) thisIterator.next()))) flag = true;

        return flag;

    }

    @Override
    public void clear() {
        // Ritorno alla situazione iniziale
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. Lo specificatore è protected
     * solo per permettere i test JUnit.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    /*
     * Classe che realizza un iteratore per questa hash table. L'ordine in cui
     * vengono restituiti gli oggetti presenti non è rilevante, ma ogni oggetto
     * presente deve essere restituito dall'iteratore una e una sola volta.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la tabella è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastRet; // ultimo elemento ritornato
        private Node<E> temp;    // sentinella

        // intero per tenere traccia della posizione nella tabella
        private int index; 

        // var di appoggio per le modifiche attese 
        private int numeroModificheAtteso; 

        private Itr() {
            // Salvo le modifiche
            this.numeroModificheAtteso = CollisionListResizableHashTable.this.modCount;

            // se il n. di elementi è maggiore di 0
            if(CollisionListResizableHashTable.this.size > 0) {
                // trovo  la prima occorrenza nella tabella 
                while(CollisionListResizableHashTable.this.table[this.index] == null 
                    && this.index < CollisionListResizableHashTable.this.getCurrentCapacity()) this.index++;

                this.temp = (Node <E>) CollisionListResizableHashTable.this.table[this.index];
            }
            // altrimenti i puntatori non sono inizializzati: questa assegnazione è possibile in quanto l'iteratore è failfast
            else {
                this.temp = null;
                //this.next = null;
            }

        }

        @Override
        public boolean hasNext() {

            // Se il puntatore non è null o se le occorrenze non sono terminate
            return this.temp != null;

        }

        @Override
        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != CollisionListResizableHashTable.this.modCount) {
                throw new ConcurrentModificationException("Lista modificata durante l'iterazione");
            }

            // controllo hasNext()
            if (!hasNext()) throw new NoSuchElementException("Richiesta di next quando hasNext è falso");
                
            lastRet = this.temp; // salvo il valore che verrà ritornato


            // ha un successivo nella lista di collisioni
            if(temp.next != null) 
                this.temp = this.temp.next;
            // non ha un successivo nella lista di collisioni
            else {
                // trovo  la prima occorrenza nella tabella 
                while(++this.index < CollisionListResizableHashTable.this.getCurrentCapacity()
                && CollisionListResizableHashTable.this.table[this.index] == null);
                if(this.index < CollisionListResizableHashTable.this.getCurrentCapacity()) 
                    temp = (Node <E>) CollisionListResizableHashTable.this.table[this.index]; // se è l'indice non ha superato la dim. della tabella
                else temp = null; // altrimenti non ha un successore ed è l'unico elemento rimasto
            } 
                
            // ritorno il valore che inizialmente aveva la sentinella temp
            return this.lastRet.item; 

        }

    }

    /*
     * Only for JUnit testing purposes.
     */
    protected Object[] getTable() {
        return this.table;
    }

    /*
     * Only for JUnit testing purposes.
     */
    protected PrimaryHashFunction getPhf() {
        return this.phf;
    }

}
