package it.unicam.cs.asdl2324.mp1;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import java.util.ConcurrentModificationException;
import java.util.HashSet;

/**
 * Questa classe MyMultiset implementa tutte le funzionalità dell'interfaccia Multiset
 * attraverso l'utilizzo di una tabella hash con liste di collisioni implementata internamente
 *  (cioé senza l'utilizzo di strutture dati fornite dalla Java SE).
 * La tabella Hash con liste di collisioni è una struttura dati che soddisfa i requisiti di implementazione,
 * in quanto non garantisce una struttura in cui i siano dati ordinati né propiamente indicizzati.
 * Questa classe implementa pertanto tutte le operazioni base dall'interfaccia (add, remove, setCount, cointains, count) 
 * in un tempo Θ(1 + α), dove α è il fattore di carico della tabella, che è dato dal rapporto 
 * tra gli elementi effettivamente allocati e la grandezza della tabella.
 * 
 * La tabella Hash è implementata attraverso un array di una classe interna chiamata Element.
 * La classe interna Element rappresenta l'elemento nel multiset e contiene il puntatore 
 * all'oggetto E e un intero che rappresenta il numero di occorrenze di questo elemento nel multiinsieme. 
 * Inoltre dispone di un puntatore di tipo Element che è definito come il successivo della lista di collisione.
 * Nel caso due elementi diversi dovessero generare la stessa chiave hash verrebbero
 * concanenati nella rispettiva lista di collisione. 
 * La funzione di hashing (consulatare @computeHash) è implementata attraverso il metodo della 
 * moltiplicazione: floor(m * (k*A-floor(k*A))) dove k è la chiave cioé l'hashCode generato 
 * dall'elemento E, e A è un valore consigliato dalla letturatura per rendere la funzione di hashing
 * uniforme e indipendente, pari a (sqrt(5) - 1)/2; ed m è la dimensione della tabella.
 * 
 * La tabella è allocata di partenza con dimensione pari ad 8, 
 * ed è in grado di raddoppiare le sue dimensioni quando il fattore di carico 
 * supera un valore soglia pari a 0.75 (3/4 della tabella).
 * L'operazione di ampliamento è attuata dal metodo privato @extends:
 * e mantiene sempre un valore delle dimensioni pari a una
 * potenza di 2 come previsto dal metodo della moltiplicazione. 
 * L'operazione di rehashing e copia degli elementi nella nuova tabella ha 
 * pertanto un costo lineare pari a Θ(n).
 * 
 * Questa implementazione garantisce pertanto una notevole efficienza in termini di tempo 
 * per tutte le operazioni base dell'interfaccia.
 * Dal punto di vista dello spazio occupato in memoria occupa sempre uno spazio
 *  pari a Θ(K), dove K è la dimensione dell'array.
 * K è inizialmente pari ad 8 e la funzione per l'estensione ha un costo lineare.
 * 
 * 
 * 
 * @author Luca Tesei (template) CAMILLETTI SAMUELE samuele.camilletti@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi del multiset
 */
@SuppressWarnings({"unchecked"})
public class MyMultiset<E> implements Multiset<E> {

    // Fattore di carico indice del limite soglia da superare per estendere le dim. della tabella
    private static final double LOAD_FACTOR = 0.75;

    // Dimensione di partenza della tabella hash
    private static final int DEFAULT_DIM = 8;

    // Costante consigliata dalla letteratura per hash con metodo della moltiplicazione
    private static final double A_VALUE = (Math.sqrt(5) - 1)/2;

    // N. modifiche usato per l'iteratore fail-fast
    private int numeroModifiche; 

    // Array di elementi del multinsieme usato per implementare l'hash set
    private Element<E>[] hashTable; 

    // Numero di elementi "virtuali" cioè che prende conto anche delle occorrenze di ogni elemento
    private int nElementi;

    // Numero di elementi effettivamente allocati (necessario per calcolare il fattore di carico della tabella)
    private int nElementiEffettivo;

    // Dimensione attuale tabella
    private int msize; 

    private static class Element<E>{

        private E item;         // Elemento
        private int occorrenze; // N. di occorrenze presenti dell'elemento
        private Element<E> next;// Puntatore al successivo elemento della lista di collisioni
    

        Element(E item, int occorrenze, Element<E> next) {

            this.item = item;
            this.occorrenze = occorrenze;
            this.next = next;

        }

    }

    private class Itr implements Iterator<E> {
        
        private Element<E> lastRet; // ultimo elemento ritornato
        private Element<E> temp;    // sentinella
        private Element<E> next;    // successivo della sentinella

        // intero per tenere traccia della posizione nella tabella
        private int index; 
        // intero per tenere traccia di quante occorrenze manchino all'elemento su cui si sta iterando
        private int occorrenza; 

        // var di appoggio per le modifiche attese 
        private int numeroModificheAtteso; 

        private Itr() {
            // Salvo le modifiche
            this.numeroModificheAtteso = MyMultiset.this.numeroModifiche;

            // se il n. di elementi è maggiore di 0
            if(nElementi > 0) {
                // trovo  la prima occorrenza nella tabella 
                while(MyMultiset.this.hashTable[this.index] == null && this.index < msize) this.index++;

                this.temp = MyMultiset.this.hashTable[this.index];
                this.occorrenza = this.temp.occorrenze;
                if(temp.next == null) { // se non ha collisioni
                    // trovo  la prima occorrenza nella tabella 
                    while(++this.index < MyMultiset.this.msize && MyMultiset.this.hashTable[this.index] == null);
                    if(this.index != MyMultiset.this.msize) next = MyMultiset.this.hashTable[this.index]; // se è l'indice non ha superato la dim. della tabella
                    else next = null; // altrimenti non ha un successore ed è l'unico elemento
                } 
                else next = temp.next; // se ha collisioni è l'elemento successivo nella lista di collisioni
            }
            // altrimenti i puntatori non sono inizializzati: questa assegnazione è possibile in quanto l'iteratore è failfast
            else {
                this.temp = null;
                this.next = null;
            }

        }

        @Override
        public boolean hasNext() {

            // Se il puntatore non è null o se le occorrenze non sono terminate
            return this.next != null || this.occorrenza > 0;

        }

        @Override
        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != MyMultiset.this.numeroModifiche) {
                throw new ConcurrentModificationException("Lista modificata durante l'iterazione");
            }

            // controllo hasNext()
            if (!hasNext()) throw new NoSuchElementException("Richiesta di next quando hasNext è falso");

            if(this.occorrenza > 1) {
                this.occorrenza--; // se ci sono ancora più di 1 occorrenza, diminuisco
                return this.temp.item; // e ritorno
            }            

            lastRet = this.temp; // salvo il valore che verrà ritornato
            this.occorrenza--; // diminuisco le occorrenze rimaste

            // se ha un successivo
            if(this.next != null) {

                // prendo il successivo per la prossima iterazione
                this.temp = this.next;

                // aggiorno le occorrenze
                this.occorrenza = this.temp.occorrenze;

                // ha un successivo nella lista di collisioni
                if(next.next != null) 
                    this.next = this.next.next;
                // non ha un successivo nella lista di collisioni
                else {
                    // trovo  la prima occorrenza nella tabella 
                    while(++this.index < MyMultiset.this.msize && MyMultiset.this.hashTable[this.index] == null);
                    if(this.index != MyMultiset.this.msize) next = MyMultiset.this.hashTable[this.index]; // se è l'indice non ha superato la dim. della tabella
                    else next = null; // altrimenti non ha un successore ed è l'unico elemento rimasto
                } 
                
            }

            // ritorno il valore che inizialmente aveva la sentinella temp
            return this.lastRet.item; 

        }

    }

    /**
     * Crea un multiset vuoto.
     */
    public MyMultiset() {
        
        this.hashTable = (Element<E>[])new Element[DEFAULT_DIM]; // creo un array di dimensione standard 8
        this.nElementi = 0;
        this.nElementiEffettivo = 0;
        this.numeroModifiche = 0;
        this.msize = DEFAULT_DIM;

    }

    @Override
    public int size() {
        return this.nElementi;
    }

    @Override
    public int count(Object element) {

        if(element == null) 
            throw new NullPointerException("Elemento inesistente.");

        Element<E> temp; // sentinella

        // controllo se esiste almeno un elemento con quell'hash
        if((temp = this.hashTable[computeHash((E) element)]) != null) {

            // scorro lista colllisioni dalla testa fino all'ultimo o finché non lo trovo 
            while(!(temp.item.equals(element)) && temp.next != null) temp = temp.next;
            
            // se non l'ho trovato
            if(!(temp.item.equals(element))) 
                return 0;
            // se l'ho trovato
            else 
                return temp.occorrenze;
        }
        else 
            return 0;
    }

    @Override
    public int add(E element, int occurrences) {

        if(element == null) 
            throw new NullPointerException("Elemento inesistente.");
        if(occurrences < 0) 
            throw new IllegalArgumentException("Numero di occorrenze negativo non consentito.");

        // se la tabella ha superato la soglia del fattore di carico, estendo
        if((this.nElementiEffettivo/this.msize) > LOAD_FACTOR)
            this.extend();

        Element<E> temp; // sentinella
        int oldOccorrenze = 0; // var di appoggio per le occorrenze precedenti
        
        // controllo se presente
        if(this.contains(element)) {

            // se lo contiene so già che lo troverò
            temp = this.hashTable[computeHash(element)];

            // scorro lista collisioni
            while(!(temp.item.equals(element)) && temp.next != null) temp = temp.next;

            // verifico che la dimensione non sia superiore alla soglia massima per un intero
            if(!checkMax((long)temp.occorrenze + occurrences)) { 
                oldOccorrenze = temp.occorrenze;
                temp.occorrenze += occurrences; // aumento di occurences il n. occorrenze dell'elemento
            }
            else throw new IllegalArgumentException("Limite fisico per un intero di occorrenze raggiunto.");

            if(oldOccorrenze != temp.occorrenze) 
                this.numeroModifiche++; // se le occorrenze sono cambiate

        }
        // se non presente
        else {

            if(occurrences == 0) return 0; // se le occorrenze da aggiungere sono 0 non aggiungo il nuovo elemento
            if(this.hashTable[computeHash(element)] != null) {
            
                // se non supera il limite di occorrenze lo aggiungo immediatamente come slot dopo "la testa" nella lista di collisioni
                if(!checkMax((long) occurrences)) {
                    Element<E> newElement = new Element<E>(element, occurrences, this.hashTable[computeHash(element)].next);
                    this.hashTable[computeHash(element)].next = newElement;
                }
                else throw new IllegalArgumentException("Limite fisico per un intero di occorrenze raggiunto.");

            }
            else {

                // verifico che la dimensione non sia superiore alla soglia massima per un intero
                if(!checkMax(occurrences))
                    this.hashTable[computeHash(element)] = new Element<E>(element, occurrences, null);
                else throw new IllegalArgumentException("Limite fisico per un intero di occorrenze raggiunto.");

            }
            this.numeroModifiche++;
            this.nElementiEffettivo++;
        }
            
        this.nElementi += occurrences;
        return oldOccorrenze;
    }

    @Override
    public void add(E element) {
        this.add(element, 1); 
    }

    @Override
    public int remove(Object element, int occurrences) {

        if(element == null) 
            throw new NullPointerException("Elemento inesistente.");
        if(occurrences < 0) 
            throw new IllegalArgumentException("Numero di occorrenze negativo non consentito.");
        
        Element<E> temp; // sentinella
        Element<E> prec; // precedente di temp
        int oldOccorrenze = 0;  // var di appoggio per le occorrenze precedenti

        // controllo se è presente
        if(this.contains(element)) {
            
            // salvo il valore con quell'hash (testa della lista di collisione)
            temp = this.hashTable[computeHash((E) element)];
            prec = temp; // il precedente coincide con la testa

            // scorro lista collisioni, sapendo che prima o poi lo troverò
            while(!(temp.item.equals(element))) {
                prec = temp;
                temp = temp.next;
            }
            
            oldOccorrenze = temp.occorrenze;
            // se va rimosso
            if(occurrences >= temp.occorrenze) {
                // se è la testa della lista di collisione
                if(this.hashTable[computeHash((E) element)] == temp) { 
                    if(temp.next == null) 
                        this.hashTable[computeHash((E) element)] = null; // cella tabella hash libera
                    // altrimenti, primo elemento della lista delle collisioni diventa testa della cella hash
                    else 
                        this.hashTable[computeHash((E) element)] = temp.next; 
                }
                // è un elemento della lista di collisioni
                else {
                    prec.next = temp.next;
                }
                this.numeroModifiche++;
            }
            // se vanno solo diminuite le occorrenze
            else {
                temp.occorrenze -= occurrences;
                if(oldOccorrenze != temp.occorrenze) this.numeroModifiche++;
            }

            // Se l'elemento è stato rimosso rimuovo solo le "vecchie" occorrenze
            if(occurrences > oldOccorrenze) 
                this.nElementi -= oldOccorrenze;
            else 
                this.nElementi -= occurrences; // altrimenti se sono diminuite tolgo quelle rimosse
            
        }

        return oldOccorrenze;
    }

    @Override
    public boolean remove(Object element) {

        if(element == null) 
            throw new NullPointerException("Elemento inesistente.");
        
        Element<E> temp; // sentinella
        Element<E> prec; // precedente di temp

        // controllo se è presente
        if(this.contains(element)) {

            // salvo il valore con quell'hash (testa della lista di collisione)
            temp = this.hashTable[computeHash((E) element)];
            prec = temp; // il precedente coincide con la testa

            // scorro lista collisioni, sapendo che prima o poi lo troverò
            while(!(temp.item.equals(element))) {
                prec = temp;
                temp = temp.next;
            }
            
            // se va rimosso
            if(temp.occorrenze == 1) {
                if(this.hashTable[computeHash((E) element)] == temp) { // è la testa della lista di collisione
                    if(temp.next == null) 
                        this.hashTable[computeHash((E) element)] = null; // cella hash libera
                    // primo elemento della lista delle collisioni diventa testa della cella hash
                    else 
                        this.hashTable[computeHash((E) element)] = temp.next; 
                }
                // è un elemento della lista di collisioni
                else {
                    prec.next = temp.next;
                }

            }
            // se vanno diminuite le occorrenze
            else {
                temp.occorrenze -= 1;
            }

            this.nElementi--;
            this.numeroModifiche++;
            return true;
        }
        else 
            return false;

    }

    @Override
    public int setCount(E element, int count) {

        if(element == null) 
            throw new NullPointerException("Elemento inesistente.");
        if(count < 0) 
            throw new IllegalArgumentException("Numero di occorrenze negativo non consentito.");
        
        Element<E> temp; // sentinella
        int oldOccorrenze = 0;

        // controllo se presente
        if(this.contains(element)) {

            // se lo contiene so già che lo troverò
            temp = this.hashTable[computeHash(element)];

            // scorro lista collisioni
            while(!(temp.item.equals(element)) && temp.next != null) temp = temp.next;

            if(!checkMax((long)temp.occorrenze + count)) {
                oldOccorrenze = temp.occorrenze;
                temp.occorrenze = count; // aumento di 1 il n. occorrenze dell'elemento
            }
            else throw new IllegalArgumentException("Limite fisico per un intero di occorrenze raggiunto.");

            if(oldOccorrenze != temp.occorrenze) 
                this.numeroModifiche++; // se le occorrenze sono cambiate      
            
        }
        else {
            this.add(element, count);
        }
        
        this.nElementi -= oldOccorrenze;
        this.nElementi += count;
        return oldOccorrenze;

    }

    @Override
    public Set<E> elementSet() {
        
        Set<E> retSet = new HashSet<E>();   // set di appoggio
        Element<E> temp;        // sentinella

        for(int i=0; i<this.msize; i++) { // scorro l'hash table

            temp = this.hashTable[i]; // prendo l'elemento di indice

            // scorro dal primo eventuale l'elemento l'intera lista di collisioni
            while(temp != null){ 

                retSet.add(temp.item);
                temp = temp.next;

            }

        }

        return retSet;

    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public boolean contains(Object element) {

        if(element == null) 
            throw new NullPointerException("Elemento inesistente.");

        Element<E> temp; // sentinella

        if((temp = this.hashTable[computeHash((E) element)]) != null) {

            // scorro lista 
            while(!(element.equals(temp.item)) && temp.next != null) temp = temp.next;
            
            // se non lo trovo
            if(!(element.equals(temp.item))) 
                return false;
            // se lo trovo
            else 
                return true;
        }
        else 
            return false;
    }

    @Override
    public void clear() {

        // resetto i dati inizializzati dal costruttore
        this.hashTable = (Element<E>[])new Element[DEFAULT_DIM];
        this.nElementi = 0;
        this.nElementiEffettivo = 0;
        this.numeroModifiche++;
        this.msize = DEFAULT_DIM;

    }

    @Override
    public boolean isEmpty() {
        return this.nElementi == 0;
    }

    /*
     * Due multinsiemi sono uguali se e solo se contengono esattamente gli
     * stessi elementi (utilizzando l'equals della classe E) con le stesse
     * molteplicità.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof MyMultiset))
            return false;
        MyMultiset<?> other = (MyMultiset<?>) obj;
        // Controllo se entrambe liste vuote
        if (this.nElementi == 0) {
            if (other.nElementi != 0)
                return false;
            else
                return true;
        }
        // Liste non vuote, scorro gli elementi di entrambe
        Iterator<E> thisIterator = this.iterator();
        Iterator<?> otherIterator = other.iterator();
        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            E o1 = thisIterator.next();
            // uso il polimorfismo di Object perché non conosco il tipo ?
            Object o2 = otherIterator.next();
            // il metodo equals che si usa è quello della classe E
            if (!o1.equals(o2))
                return false;
        }
        // Controllo che entrambe le liste siano terminate
        return !(thisIterator.hasNext() || otherIterator.hasNext());
    }

    /*
     * Da ridefinire in accordo con la ridefinizione di equals.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hashCode = 1;

        Iterator<E> thisIterator = this.iterator();
        // uso l'iterator di questa classe
        while (thisIterator.hasNext()) {
            E e = thisIterator.next();
            hashCode = 31 * hashCode + e.hashCode();
        }
    
        return hashCode;
    }

    /*
     * Metodo per il calcolo della chiave
     * 
     * 
     */
    private int computeHash(E element) {

        // metodo della moltiplicazione floor(m * partefrazionaria(key * A))
        return (int)Math.floor(this.msize * (element.hashCode()*A_VALUE - Math.floor((element.hashCode()*A_VALUE))));
    }

    /*
     * Metodo per check delle occorrenze
     * Utilizzo un cast a long per verificare che il valore superi la soglia costante massima per 
     * un intero.
     * 
     */
    private boolean checkMax(long occurrences) {

        return (long) occurrences > Integer.MAX_VALUE;
    }

    /*
     * Metodo per estensione dimensione array
     * 
     * 
     */
    private void extend() {

        Element<E>[] oldHash = this.hashTable;
        this.hashTable = (Element<E>[])new Element[this.msize*2]; // alloco una nuova tabella dalla dimensione doppia
        Element<E> temp, next;

        int oldSize = this.msize; // salvo le vecchie dimensioni
        this.msize = this.msize * 2; // radoppio le dim della tabella

        for(int i=0; i<oldSize; i++) { // scorro l'hash table

            temp = oldHash[i]; // prendo l'elemento di indice
            next = oldHash[i];
            
            // scorro dal primo eventuale elemento l'intera lista di collisioni
            while(temp != null){ 
                
                int hash = computeHash(temp.item);
                next = temp.next;
                temp.next = null;

                if(this.hashTable[hash] != null) { // se l'indice non è libero

                    // attacco dopo la testa
                    temp.next = this.hashTable[hash].next;
                    this.hashTable[hash].next = temp;

                }
                else   // altrimenti va nella posizione corrispondente al risultato della funzione hash
                    this.hashTable[hash] = temp;
                
                temp = next;
            }

        }        

    }

}
