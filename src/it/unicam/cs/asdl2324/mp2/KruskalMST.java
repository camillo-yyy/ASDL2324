package it.unicam.cs.asdl2324.mp2;

import java.util.List;
import java.util.Set;

import java.util.HashSet;
import java.util.ArrayList;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author Luca Tesei (template) CAMILLETTI SAMUELE samuele.camilletti@studenti.unicam.it (implementazione)
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMST<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;

    /* Attributo di una classe interna per implementazione quicksort */
    private QuickSort<L> sortEdge;

    /* Classe interna che implementa un quicksort per una lista di 
     * archi di tipo GraphEdge con label L
     */
    public static class QuickSort<L>{

        /* Metodo pubblico per eseguire il quicksort
         * per una lista di archi di tipo GraphEdge con label L
         * sull'attributo weight in quanto GraphEdge non implementa compareTo per confrontare
         * gli archi tra di loro.
         * Il quicksort è eseguito in loco, infatti il puntatore
         * restituito che punta alla lista ordinata
         * è lo stesso della lista fornita in input
         * Il quicksort rispetta gli standard di performance per algoritmi su confronti
         * in quanto nel caso medio impiega O(nlogn)
         * 
         * @param List<GraphEdge<L>> Lista di archi 
         * 
         * @return la stessa lista l fornita input ma ordinata per peso
         * 
         * @throw NullPointerException se il puntatore ad l è null
         */
        public List<GraphEdge<L>> sort(List<GraphEdge<L>> l) {
    
            if (l == null) 
                throw new NullPointerException("Tentativo di ordinare una lista null");

            // per ordinare la lista vuota o con un solo elemento non faccio niente
            if (l.size() <= 1) return l;
    
            quickSort(l, 0, l.size() - 1);
    
            return l;
    
        }
    
        private void quickSort(List<GraphEdge<L>> l, int p, int r) {
    
            // condizione di stop quando p = r
            if(p < r) {
    
                int m = partition(l, p, r); // partizione
                quickSort(l, p, m-1); // chiamata ricorsiva 1
                quickSort(l, m+1, r); // chiamata ricorsiva 2
    
            }
    
        }
    
        private int partition(List<GraphEdge<L>> l, int p, int r) {
    
            GraphEdge<L> app;
            GraphEdge<L> pivot = l.get(r); // il pivot è l'ultimo elemento della lista
            int i = (p - 1);
    
            // pongo gli elementi < del pivot a sinistra, e gli elementi >= a destra della lista
            for(int j = p; j < r; j++) { // il contatore j conta i maggiori uguali e scorre a prescindere
    
                if(l.get(j).getWeight() < pivot.getWeight()) {
    
                    i++; // Il contatore i scorre se trovo un elemento minore del pivot 
                    // che scambio opportunamente con l'elemento j, in quanto deve stare a sinistra
                    app = l.get(i);
                    l.set(i, l.get(j));
                    l.set(j, app);  
                }
    
            }
    
            // Aggiusto il pivot
            app = l.get(i+1);
            l.set(i+1, pivot);
            l.set(r, app);  
    
            return (i + 1); // ritorno la posizione del pivot
        }

    }

    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMST() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
        this.sortEdge = new QuickSort<L>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     *         copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        
        // controlli 
        if(g == null)
            throw new NullPointerException("Puntatore a grafo null");
        if(g.isDirected())
            throw new IllegalArgumentException("Grafo orientato");
        for( GraphEdge<L> edge : g.getEdges() ) 
            if(!edge.hasWeight() || edge.getWeight() < 0)
                throw new IllegalArgumentException("Almeno un arco non pesato o con peso negativo");

        // mi assicuro che la foresta di alberi sia vuota ad ogni computazione
        this.disjointSets.clear();
        // alloco un set per l'insieme che costituirò l'insieme di archi del MST
        Set<GraphEdge<L>> minSpanTree = new HashSet<GraphEdge<L>>();
        // alloco una lista per contenere gli archi da ordinare
        // necessito di una lista per ordinare i nodi in quanto il metodo getEdges() fornisce
        // un set, che pertanto non e' una struttura dati indicizzata
        // Un arraylist in particolare ha dei costi per le 
        // operazioni di add, get e set (effettuate nel qsort pari ad O(1)
        List<GraphEdge<L>> edgesList = new ArrayList<GraphEdge<L>>();

        // creo un insieme singoletto per ogni nodo del grafo
        for( GraphNode<L> node : g.getNodes() )
            this.disjointSets.makeSet(node);
        
        // appendo in una lista tutti gli archi del grafo
        for( GraphEdge<L> temp : g.getEdges())
            edgesList.add(temp);

        // ordino la lista per peso come definito dall'algoritmo di Kruskal
        List<GraphEdge<L>> sortedEdges = this.sortEdge.sort(edgesList);

        // per ogni arco preso in ordine dalla lista
        for(GraphEdge<L> edge : sortedEdges ) {
            // se i rappresentanti dei due insiemi disgiunti sono diversi,
            // ergo, se i due nodi che costituiscono l'arco rappresentano un arco leggero,
            // ovvero un arco i cui due nodi fanno parte di insiemi diversi con il costo minore,
            // allora l'arco è sicuro per l'insieme minSpanTree e i due insiemi si uniscono
            if(this.disjointSets.findSet(edge.getNode1()) != this.disjointSets.findSet(edge.getNode2())) {
                this.disjointSets.union(edge.getNode1(), edge.getNode2());
                minSpanTree.add(edge);
            }
        }

        return minSpanTree;

    }
}
