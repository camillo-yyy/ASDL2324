package it.unicam.cs.asdl2324.es12;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 * 
 * L'algoritmo richiede l'uso di una coda di min priorità tra i nodi che può
 * essere realizzata con una semplice ArrayList (non c'è bisogno di ottimizzare
 * le operazioni di inserimento, di estrazione del minimo, o di decremento della
 * priorità).
 * 
 * Si possono usare i colori dei nodi per registrare la scoperta e la visita
 * effettuata dei nodi.
 * 
 * @author @author Template: Luca Tesei, Implementazione: collettiva
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class PrimMST<L> {

    List<GraphNode<L>> priorityQueue; // array per il min heap

    /*
     * In particolare: si deve usare una coda con priorità che può semplicemente
     * essere realizzata con una List<GraphNode<L>> e si deve mantenere un
     * insieme dei nodi già visitati
     */

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda
     * vuota.
     */
    public PrimMST() {

        priorityQueue = new ArrayList<GraphNode<L>>();

    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. Dopo l'esecuzione del metodo nei nodi del grafo il campo
     * previous deve contenere un puntatore a un nodo in accordo all'albero di
     * copertura minimo calcolato, la cui radice è il nodo sorgente passato.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @param s
     *              il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *              dell'albero di copertura minimo. Tale nodo sarà la radice
     *              dell'albero di copertura trovato
     * 
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public void computeMSP(Graph<L> g, GraphNode<L> s) {

        // controlli
        if (g == null || s == null) 
            throw new NullPointerException("Grafo o nodo sorgente nulli");
        
        if(!g.containsNode(s))
            throw new IllegalArgumentException();
        if (g.isDirected())
            throw new IllegalArgumentException("Valore non valido"); 
        for ( GraphEdge<L> edge : g.getEdges() ) 
            if(!edge.hasWeight() || edge.getWeight() < 0)
                throw new IllegalArgumentException("Valore non valido");
        
        double weight = 0; // var locale

        // pongo il peso di tutti i nodi ad infinito
        for(GraphNode<L> l : g.getNodes()) {
            l.setColor(0);
            l.setFloatingPointDistance(Double.POSITIVE_INFINITY);
            l.setPrevious(null);
            this.priorityQueue.add(l);
        }        

        // la radice reale da 0 in modo che venga estratta subito dall'array
        GraphNode<L> realSource = g.getNodeOf(s.getLabel());
        this.priorityQueue.add(realSource);
        realSource.setColor(1);
        realSource.setFloatingPointDistance(0);
        realSource.setPrevious(null);  
        
        // finchè non estraggo tutti gli elementi
        while(!priorityQueue.isEmpty()) {

            GraphNode<L> node = this.getMin(); // estraggo il minimo
            priorityQueue.remove(node); // rimuovo dal minheap
            node.setColor(2); // il nodo diventa visitato

            // per ogni adiacenza del nodo scoperto
            for(GraphNode<L> adj : g.getAdjacentNodesOf(node)) {

                // estraggo il peso dell arco tra node e adj 
                for(GraphEdge<L> x : g.getEdgesOf(node)) {
                    if((x.getNode1()==node && x.getNode2()==adj) || (x.getNode2()==node && x.getNode1()==adj))
                        weight = x.getWeight();
                }

                // se il nodo non è stato visitato e 
                // il suo peso è minore rispetto a quello registrato in passato
                // lo aggiorno e pongo il suo stato a scoperto
                if(priorityQueue.contains(adj) && weight < adj.getFloatingPointDistance()) {
                    adj.setColor(1);
                    adj.setFloatingPointDistance(weight);
                    adj.setPrevious(node);
                }
            }
        }


    }

    private GraphNode<L> getMin() {
        GraphNode<L> min = this.priorityQueue.get(0);

        for(GraphNode<L> l : this.priorityQueue) 
            if(l.getFloatingPointDistance() < min.getFloatingPointDistance()) min = l;

        return min;
    }
        
}
