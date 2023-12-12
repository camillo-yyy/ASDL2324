package it.unicam.cs.asdl2324.es9;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che implementa uno heap binario che può contenere elementi non nulli
 * possibilmente ripetuti.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 * @param <E>
 *                il tipo degli elementi dello heap, che devono avere un
 *                ordinamento naturale.
 */
public class MaxHeap<E extends Comparable<E>> {

    /*
     * L'array che serve come base per lo heap
     */
    private ArrayList<E> heap;

    /**
     * Costruisce uno heap vuoto.
     */
    public MaxHeap() {
        this.heap = new ArrayList<E>();
    }

    /**
     * Restituisce il numero di elementi nello heap.
     * 
     * @return il numero di elementi nello heap
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Determina se lo heap è vuoto.
     * 
     * @return true se lo heap è vuoto.
     */
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    /**
     * Costruisce uno heap a partire da una lista di elementi.
     * 
     * @param list
     *                 lista di elementi
     * @throws NullPointerException
     *                                  se la lista è nulla
     */
    public MaxHeap(List<E> list) {
        
        if(list == null) throw new NullPointerException("Puntatore null");

        this.heap = new ArrayList<E>();
        // scorro ogni elemento e lo aggiungo all'array list
        for (E e : list) {
           this.heap.add(e);
        }
        // dalla prima foglia interna col figlio buildo il max heap
        for(int i = (this.size()/2)-1; i >= 0; i--) {
            heapify(i);
        }
        
    }

    /**
     * Inserisce un elemento nello heap
     * 
     * @param el
     *               l'elemento da inserire
     * @throws NullPointerException
     *                                  se l'elemento è null
     * 
     */
    public void insert(E el) {

        if(el == null) throw new NullPointerException("Puntatore null");

        this.heap.add(el);
        E app = null;
        int i = this.size() - 1;
        // fino alla radice se il parent è pià piccolo
        while(i != 0 && this.heap.get(i).compareTo(this.heap.get(this.parentIndex(i))) > 0) {
            // scambio e prendo l'indice del padre in analisi
            app = this.heap.get(this.parentIndex(i));
            this.heap.set(this.parentIndex(i), this.heap.get(i));
            this.heap.set(i, app);
            i = this.parentIndex(i);
        }

            
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int leftIndex(int i) {
        return (2*i)+1;
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio destro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int rightIndex(int i) {
        return (2*i)+2;
    }

    /*
     * Funzione di comodo per calcolare l'indice del genitore del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int parentIndex(int i) {
        return (i-1)/2;
    }

    /**
     * Ritorna l'elemento massimo senza toglierlo.
     * 
     * @return l'elemento massimo dello heap oppure null se lo heap è vuoto
     */
    public E getMax() {
        if(this.size() == 0) return null;
        else return this.heap.get(0);
    }

    /**
     * Estrae l'elemento massimo dallo heap. Dopo la chiamata tale elemento non
     * è più presente nello heap.
     * 
     * @return l'elemento massimo di questo heap oppure null se lo heap è vuoto
     */
    public E extractMax() {

        if(this.size() == 0) return null;

        E app;
        // prendo radice, metto l'ultima foglia alla radice e 
        // rimuovo l'ultimo elemento
        // poi faccio heapify sulla radice
        app = this.heap.get(0);
        this.heap.set(0, this.heap.get(this.heap.size()-1));
        this.heap.remove(this.heap.size()-1);

        this.heapify(0);

        return app;
    }

    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(int i) {
        int max = i;
        E app;
        
        // se la foglia è compresa nella size vedo se è più grande, se sì prendo l'indice   
        if(this.leftIndex(i) <= this.size()-1)
            if(this.heap.get(i).compareTo(this.heap.get(this.leftIndex(i))) < 0)
                max = this.leftIndex(i);
        // se la foglia è compresa nella size vedo se è più grande, se sì prendo l'indice   
        if(this.rightIndex(i) <= this.size()-1)
            if(this.heap.get(max).compareTo(this.heap.get(this.rightIndex(i))) < 0)
                max = this.rightIndex(i);    
        // se il max non è l'indice iniziale scambio e chiamo ricorsivamente
        if(max != i) {
            app = this.heap.get(i);
            this.heap.set(i, this.heap.get(max));
            this.heap.set(max, app);
            
            heapify(max);
        }

    }
    
    /**
     * Only for JUnit testing purposes.
     * 
     * @return the arraylist representing this max heap
     */
    protected ArrayList<E> getHeap() {
        return this.heap;
    }
}
