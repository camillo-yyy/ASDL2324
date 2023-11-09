package it.unicam.cs.asdl2324.es6;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
/**
 * Lista concatenata singola che non accetta valori null, ma permette elementi
 * duplicati. Le seguenti operazioni non sono supportate:
 * 
 * <ul>
 * <li>ListIterator<E> listIterator()</li>
 * <li>ListIterator<E> listIterator(int index)</li>
 * <li>List<E> subList(int fromIndex, int toIndex)</li>
 * <li>T[] toArray(T[] a)</li>
 * <li>boolean containsAll(Collection<?> c)</li>
 * <li>addAll(Collection<? extends E> c)</li>
 * <li>boolean addAll(int index, Collection<? extends E> c)</li>
 * <li>boolean removeAll(Collection<?> c)</li>
 * <li>boolean retainAll(Collection<?> c)</li>
 * </ul>
 * 
 * L'iteratore restituito dal metodo {@code Iterator<E> iterator()} è fail-fast,
 * cioè se c'è una modifica strutturale alla lista durante l'uso dell'iteratore
 * allora lancia una {@code ConcurrentMopdificationException} appena possibile,
 * cioè alla prima chiamata del metodo {@code next()}.
 * 
 * @author Luca Tesei
 *
 * @param <E>
 *                il tipo degli elementi della lista
 */
public class SingleLinkedList<E> implements List<E> {

    private int size;

    private Node<E> head;

    private Node<E> tail;

    private int numeroModifiche;

    /**
     * Crea una lista vuota.
     */
    public SingleLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
        this.numeroModifiche = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. E' dichiarata static perché
     * gli oggetti della classe Node<E> non hanno bisogno di accedere ai campi
     * della classe principale per funzionare.
     */
    private static class Node<E> {
        private E item;

        private Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }

    }

    /*
     * Classe che realizza un iteratore per SingleLinkedList.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la lista è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     * 
     * La classe è non-static perché l'oggetto iteratore, per funzionare
     * correttamente, ha bisogno di accedere ai campi dell'oggetto della classe
     * principale presso cui è stato creato.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned;

        private int numeroModificheAtteso;

        private Itr() {
            // All'inizio non è stato fatto nessun next
            this.lastReturned = null;
            this.numeroModificheAtteso = SingleLinkedList.this.numeroModifiche;
        }

        @Override
        public boolean hasNext() {
            if (this.lastReturned == null)
                // sono all'inizio dell'iterazione
                return SingleLinkedList.this.head != null;
            else
                // almeno un next è stato fatto
                return lastReturned.next != null;

        }

        @Override
        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != SingleLinkedList.this.numeroModifiche) {
                throw new ConcurrentModificationException(
                        "Lista modificata durante l'iterazione");
            }
            // controllo hasNext()
            if (!hasNext())
                throw new NoSuchElementException(
                        "Richiesta di next quando hasNext è falso");
            // c'è sicuramente un elemento di cui fare next
            // aggiorno lastReturned e restituisco l'elemento next
            if (this.lastReturned == null) {
                // sono all’inizio e la lista non è vuota
                this.lastReturned = SingleLinkedList.this.head;
                return SingleLinkedList.this.head.item;
            } else {
                // non sono all’inizio, ma c’è ancora qualcuno
                lastReturned = lastReturned.next;
                return lastReturned.item;
            }

        }

    }

    /*
     * Una lista concatenata è uguale a un'altra lista se questa è una lista
     * concatenata e contiene gli stessi elementi nello stesso ordine.
     * 
     * Si noti che si poteva anche ridefinire il metodo equals in modo da
     * accettare qualsiasi oggetto che implementi List<E> senza richiedere che
     * sia un oggetto di questa classe:
     * 
     * obj instanceof List
     * 
     * In quel caso si può fare il cast a List<?>:
     * 
     * List<?> other = (List<?>) obj;
     * 
     * e usando l'iteratore si possono tranquillamente controllare tutti gli
     * elementi (come è stato fatto anche qui):
     * 
     * Iterator<E> thisIterator = this.iterator();
     * 
     * Iterator<?> otherIterator = other.iterator();
     * 
     * ...
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof SingleLinkedList))
            return false;
        SingleLinkedList<?> other = (SingleLinkedList<?>) obj;
        // Controllo se entrambe liste vuote
        if (head == null) {
            if (other.head != null)
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
     * L'hashcode è calcolato usando gli hashcode di tutti gli elementi della
     * lista.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        // implicitamente, col for-each, uso l'iterator di questa classe
        for (E e : this)
            hashCode = 31 * hashCode + e.hashCode();
        return hashCode;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /* Ritorna true se questa lista contiene l'elemento passato cioè se viene rispettata  
    una delle seguenti condizioni(o==null ? e==null : o.equals(e)). 
    
     @param o   Oggetto da cercare     
     
     @returns true      Se l'oggetto viene trovato   
              false     Se l'oggetto non esiste
        
     @throws NullPointerException - se l'oggetto passato è nullo
    */
    @Override
    public boolean contains(Object o) {

         // Uso il valore del valore di ritorno di indexOf per determinare la presenza dell'elemento
        if(this.indexOf(o) != -1) return true; 
        else return false;
    
    }

    /*
     * Aggiunge l'elemento specificato in coda.
        Non sono permessi elementi null. Sono permessi elementi duplicati.
     * 
     * @param <E> e Oggetto da aggiungere 
     * 
     * @returns true se viene aggiunto
     *          false altrimenti
     * 
     * @throws NullPointerException - Se e è null
     */
    @Override
    public boolean add(E e) {
        if(e == null) throw new NullPointerException("Oggetto passato nullo");

        Node<E> node = new Node<E>(e, null);

        // se la lista è vuota
        if(this.tail == null) {
            this.head = node;       // creo la testa
            this.tail = this.head;  // che coincide con la coda
            ++this.size;
            ++this.numeroModifiche;
            return true;
        }
        // altrimenti 
        else {
            this.tail.next = node;   // Aggiungo in coda
            this.tail = node;        // Assegno la nuova coda
            ++this.size;
            ++this.numeroModifiche;
            return true;
        }
    }
    /*
     * Rimuove la prima occorrenza dell'elemento passato se presente. 
     * Rimuove l'elemento con l'indice minore tale che (o==null ? get(i)==null : o.equals(get(i))). 
     * Ritorna true se la lista cambia a seguito della chiamata.
     * 
     * @param Object o - Elemento da rimuovere
     * 
     * @returns true - se l'elemento viene rimosso
     *          false - se l'elemento non viene rimosso
     * 
     * @throws NullPointerException - se o è null
     */
    @Override
    public boolean remove(Object o) {
        if(o == null) throw new NullPointerException("Oggetto passato nullo");
        if(!this.contains(o)) return false;

        Node<E> temp; // Sentinella
        Node<E> prec; // Nodo precedente

        // Se la lista non è vuota
        if(this.head != null) {
            temp = this.head;
            prec = this.head;
        }
        else return false;

        while (temp != null) {

            // Se trovo una occorrenza
            if(o.equals(temp.item)) {

                // controllo se è la testa e poi se è anche la coda
                if(temp == this.head) {
                    if(temp.next == null) { // Se è anche la coda rimuovo tutto
                        this.head = null;
                        this.tail = null;
                    }
                    else {
                        this.head = temp.next; // Altrimenti la nuova testa è il successivo della vecchia testa
                    }
                }
                // se è la coda
                else if(temp == this.tail) {
                    prec.next = null;
                    this.tail = prec;   // Il nodo precdente diventa la nuova coda
                }
                // altrimenti sgancio il nodo da rimuovere dal suo precedento 
                //e riaggancio con il successivo dell'elemento da rimuovere
                else {
                    prec.next = temp.next;
                }
                // Aggiorno dimensioni
                --this.size;
                ++this.numeroModifiche;
                return true;

            }

            prec = temp;        // Salvataggio precedente
            temp = temp.next;   // Scorro
        }

        return false; // Se non lo trovo
    }

    /*
     * Rimuove tutti gli elementi dalla lista.
     */
    @Override
    public void clear() {

        // Svuoto testa/coda/dimensioni e lascio il resto al garbage collector
        this.head = null;
        this.tail = null;
        this.size = 0;
        ++this.numeroModifiche;

    }

    /*
     * Ritorna l'elemento all'indice specificato
     * 
     * @param index
     * 
     * @returns E
     * 
     * @throws IndexOutOfBoundException - se l'indice è fuori scala
     */
    @Override
    public E get(int index) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();
        if(this.head == null) throw new IndexOutOfBoundsException();
        
        // Sentinelle
        Iterator<E> thisIterator = this.iterator();
        int i=0;

        // Scorro finché non trovo l'elemento 
        while(i < index) {
            thisIterator.next();
            ++i;
        }

        return thisIterator.next(); // Ritorno il successivo
    }

    /*
     * Rimpiazza l'elemento a posizione indice con quello passato
     * 
     * @param int index
     * @param E element
     * 
     * @return l'elemento sostituito
     * 
     * @throws  NullPointerException - se E è null
     * @throws IndexOutOfBoundException - se l'indice è fuori scala
     */
    @Override
    public E set(int index, E element) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();
        if(this.head == null) throw new IndexOutOfBoundsException();
        if(element == null) throw new NullPointerException("Oggetto passato nullo");

        // Sentinelle
        Node<E> temp = this.head;
        E prev;
        int i=0;

        // Scorro finché non trovo l'elemento 
        while(i < index) {
            temp = temp.next;
            ++i;
        }
        
        prev = temp.item;   // Salvo l'elemento precedetemente contenuto
        temp.item = element;    // Inserisco il nuovo
        ++this.numeroModifiche; // Aumento il n. modifiche

        return prev;
    }
    
    /*
     * Inserisce l'elemento passato alla posizione passata.
     * GLi altri elementi scorrono a destra (se presentI).
     * 
     * @param int index
     * @param E element
     *  
     * @throws  NullPointerException - se E è null
     * @throws IndexOutOfBoundException - se l'indice è fuori scala
     */
    @Override
    public void add(int index, E element) {
        if(index < 0 || index > this.size) throw new IndexOutOfBoundsException();
        if(element == null) throw new NullPointerException("Oggetto passato nullo");

        Node<E> newNode = new Node<E>(element, null);

        // Se il nodo da aggiungere è in testa
        if(index == 0) {
            if(this.head == null) this.head = newNode; // Se la lista è vuota
            else { 
                newNode.next = this.head; // Altrimenti aggancio la vecchia testa
                this.head = newNode; 
            }
        } // Se l'elemento da aggiungere è in coda 
        else if(index == this.size) {
            this.tail.next = newNode;  // Il successivo della vecchia coda è il nuovo nodo
            this.tail = newNode;    // Il nuovo nodo diventa la nuova coda
        }
        else{
            // Sentinelle
            Node<E> temp = this.head;
            int i=0;

            // Scorro finché non trovo l'elemento precedente all'indice richiesto
            while(i < index - 1) {
                temp = temp.next;
                ++i;
            }
            // Il successivo dell'elemento precedente all'indice diventa il successivo del nuovo nodo
            newNode.next = temp.next;
            // Il successivo del nodo precedente all'indice è il nuovo nodo
            temp.next = newNode; 
        }

        // Aumento dimensioni e il n. mod
        ++this.size;
        ++this.numeroModifiche; 

    }

    /*
     * Rimuove l'elemento all'indice specificato
     * Sposta tutti gli elementi a sinistra (se presenti)
     * 
     * @param int index
     * 
     * @returns l'oggetto rimosso
     * 
     * @throws IndexOutOfBoundException - se l'indice è fuori scala
     */
    @Override
    public E remove(int index) {
        if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

        E ret;

        if(index == 0) {
            ret = this.head.item;  // Salvo l'elemento da rimuovere
            this.head = this.head.next; // La testa diventa il successivo della testa

            // Diminuisco dimensioni e aumento il n. mod
            --this.size;
            ++this.numeroModifiche; 

            return ret;
        }
        else{
            // Sentinelle
            Node<E> temp = this.head;
            int i=0;

            // Scorro finché non trovo l'elemento precedente all'indice dell'elemento da rimuovere
            while(i < index - 1) {
                temp = temp.next;
                ++i;
            }
             // Salvo l'elemento da rimuovere (cioè l'elemento contenuto nel nodo successivo)
            ret = temp.next.item;
            // Aggancio al precedente il successivo del nodo che verrà rimosso
            temp.next = temp.next.next; 

            // Diminuisco dimensioni e aumento il n. mod
            --this.size;
            ++this.numeroModifiche; 

            return ret;
        }

        
    }

    /*
     * Ritorna l'indice della prima occurrenza dell'elemento passato. -1 se invece non è contenuto.
     * Più formalmente ritorna il primo elemento tale che (o==null ? get(i)==null : o.equals(get(i))) o -1 se non trovato.
     * 
     * @param Object o
     * 
     * @return indice dell'elemento se presente (-1 se non presente)
     * 
     * @throws  NullPointerException - se o è null
     */
    @Override
    public int indexOf(Object o) {
        if(o == null) throw new NullPointerException("Oggetto passato nullo");
        if(this.head == null) return -1; // lista vuota, non contenuto

        // Sentinelle
        Node<E> temp = this.head;
        int i = 0;

         // Liste non vuota, scorro gli elementi fino a trovarlo
        while (temp != null) {
            if((o.equals(temp.item))) return i; // se lo trovo ritorno l'indice
            temp = temp.next;
            ++i;
        }

        return -1; // ritorno -1 se non trovato
    }

    /*
     * Ritorna l'indice dell'ultima occurrenza dell'elemento passato. -1 se invece non è contenuto.
     * Più formalmente ritorna l'ultimo elemento tale che (o==null ? get(i)==null : o.equals(get(i))) o -1 se non trovato.
     * 
     * @param Object o
     * 
     * @return indice dell'elemento se presente (-1 se non presente)
     * 
     * @throws  NullPointerException - se o è null
     */
    @Override
    public int lastIndexOf(Object o) {
        if(o == null) throw new NullPointerException("Oggetto passato nullo");
        if(this.head == null) return -1; // lista vuota, non contenuto

        // Sentinelle
        Node<E> temp = this.head;
        int i = 0;
        int index = -1;

         // Liste non vuota, scorro gli elementi 
        while (temp != null) {
            if((o.equals(temp.item))) index = i; // se lo trovo aggiorno index
            temp = temp.next;
            ++i;
        }

        return index; // ritorno index, se non trovato sarà -1
    }

    /*
     * Ritorna un array contenente gli stessi elementi della lista nello stesso ordine.
     *
     * @returns Array di E con tutti gli elementi della lista
     */
    @Override
    public Object[] toArray() {
        if(this.head == null) return new Object[0]; // se la lista è vuota torno un array vuoto

        Object[] temp = new Object[this.size]; // Allocco un nuovo array di dimensione size
        int i = 0;

        // Uso l'iteratore implicitamente con il for
        for(E e : this) {   

            temp[i++] = e; // Assegno ad ogni elemento dell'array l'elemento e
            // post-incremento l'indice

        }

        return temp;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }
}
