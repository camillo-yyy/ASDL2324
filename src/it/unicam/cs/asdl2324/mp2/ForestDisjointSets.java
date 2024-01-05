package it.unicam.cs.asdl2324.mp2;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Luca Tesei (template) CAMILLETTI SAMUELE samuele.camilletti@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;
    
    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }

    }

    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() {

        this.currentElements = new HashMap<E, Node<E>>();

    }

    @Override
    public boolean isPresent(E e) {

        if(e == null)
            throw new NullPointerException("Puntatore null");

        // Uso l'API di HashMap per verificare la presenza o meno del nodo data la chiave
        return this.currentElements.containsKey(e);

    }

    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {
        
        if(e == null)
            throw new NullPointerException("Puntatore null");
        if(this.isPresent(e))
            throw new IllegalArgumentException("Elemento presente in uno degli insiemi");
        
        // creo un nuovo nodo e lo mappo nella tabella hash con la relativa chiave
        this.currentElements.put(e, new Node<E>(e));

    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {
        
        if(e == null)
            throw new NullPointerException("Puntatore null");
        if(!this.isPresent(e))
            return null;

        // ottengo il nodo corrispondente
        Node<E> node = this.currentElements.get(e);
        
        // se il parent è diverso da sé stesso (cioè non è il rappresentante)
        if(node.parent != node)
            // richiamo ricorsivamente sul padre
            node.parent = this.currentElements.get(this.findSet(node.parent.item));
            // quando la funzione ricorisiva ritorna, il parent diventa il nodo radice
            // effettuando così la compressione del cammino
        
        // ritorno la label del padre
        return node.parent.item;
    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {

        if(e1 == null || e2 == null)
            throw new NullPointerException("Puntatore null");
        if(!this.isPresent(e1) || !this.isPresent(e2))
            throw new IllegalArgumentException("Uno dei due elementi non presenti");
        if(this.findSet(e1) == this.findSet(e2))
            return;

        // ottengo i rispettivi nodi rappresentanti delle etichette passate
        Node<E> node1 = this.currentElements.get(this.findSet(e1));
        Node<E> node2 = this.currentElements.get(this.findSet(e2));

        // se il rank del nodo 1 è più alto allora è lui il rappresentante
        if(node1.rank > node2.rank)
            node2.parent = node1;
        else {
        // altrimenti è il nodo2
            node1.parent = node2;
            // se dovessero essere uguali è sempre il nodo 2 a cui aumenta anche il rango
            if(node1.rank == node2.rank) 
                node2.rank+=1;
        }
    }

    @Override
    public Set<E> getCurrentRepresentatives() {
        
        Set<E> rep = new HashSet<E>();

        // aggiungo tutte le etichette dei nodi il cui parent è sé stesso
        for ( E temp : this.currentElements.keySet() )
            if(this.currentElements.get(temp).parent == this.currentElements.get(temp))
                rep.add(temp);

        return rep;

    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {

        if(e == null)
            throw new NullPointerException("Puntatore null");
        if(!this.isPresent(e))
            throw new IllegalArgumentException("Elemento non appartenente a nessun insieme");

        Set<E> rep = new HashSet<E>();

        // aggiungo tutte le etichette dei nodi il cui parent è lo stesso di quello passato
        for ( E temp : this.currentElements.keySet() )
            if(this.findSet(temp) == this.findSet(e))
                rep.add(temp);

        return rep;
    }

    @Override
    public void clear() {
        this.currentElements = new HashMap<E, Node<E>>();
    }

}
