/**
 * 
 */
package it.unicam.cs.asdl2324.es12;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import java.util.HashSet;

/**
 * Implementazione della classe astratta {@code Graph<L>} che realizza un grafo
 * non orientato. Per la rappresentazione viene usata una variante della
 * rappresentazione a liste di adiacenza. A differenza della rappresentazione
 * standard si usano strutture dati più efficienti per quanto riguarda la
 * complessità in tempo della ricerca se un nodo è presente (pseudocostante, con
 * tabella hash) e se un arco è presente (pseudocostante, con tabella hash). Lo
 * spazio occupato per la rappresentazione risultà tuttavia più grande di quello
 * che servirebbe con la rappresentazione standard.
 * 
 * Le liste di adiacenza sono rappresentate con una mappa (implementata con
 * tabelle hash) che associa ad ogni nodo del grafo i nodi adiacenti. In questo
 * modo il dominio delle chiavi della mappa è il set dei nodi, su cui è
 * possibile chiamare il metodo contains per testare la presenza o meno di un
 * nodo. Ad ogni chiave della mappa, cioè ad ogni nodo del grafo, non è
 * associata una lista concatenata dei nodi collegati, ma un set di oggetti
 * della classe GraphEdge<L> che rappresentano gli archi connessi al nodo: in
 * questo modo la rappresentazione riesce a contenere anche l'eventuale peso
 * dell'arco (memorizzato nell'oggetto della classe GraphEdge<L>). Per
 * controllare se un arco è presente basta richiamare il metodo contains in
 * questo set. I test di presenza si basano sui metodi equals ridefiniti per
 * nodi e archi nelle classi GraphNode<L> e GraphEdge<L>.
 * 
 * Questa classe non supporta le operazioni di rimozione di nodi e archi e le
 * operazioni indicizzate di ricerca di nodi e archi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *                etichette dei nodi del grafo
 */
public class MapAdjacentListUndirectedGraph<L> extends Graph<L> {

    /*
     * Le liste di adiacenza sono rappresentate con una mappa. Ogni nodo viene
     * associato con l'insieme degli archi uscenti. Nel caso in cui un nodo non
     * abbia archi uscenti è associato con un insieme vuoto.
     */
    private final Map<GraphNode<L>, Set<GraphEdge<L>>> adjacentLists;

    /**
     * Crea un grafo vuoto.
     */
    public MapAdjacentListUndirectedGraph() {
        // Inizializza la mappa con la mappa vuota
        this.adjacentLists = new HashMap<GraphNode<L>, Set<GraphEdge<L>>>();
    }

    @Override
    public int nodeCount() {
        return adjacentLists.size();
    }

    @Override
    public int edgeCount() {

        int size = 0;

        for ( GraphNode<L> key : adjacentLists.keySet() ) {
            size += adjacentLists.get(key).size();
        }
        return size/2;
        
    }

    @Override
    public void clear() {
        this.adjacentLists.clear();
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa grafi non orientati
        return false;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return adjacentLists.keySet();
    }

    @Override
    public boolean addNode(GraphNode<L> node) {

        if (node == null)
            throw new NullPointerException("Tentativo di aggiungere un nodo null");

        if(!adjacentLists.containsKey(node))
            adjacentLists.put(node, new HashSet<GraphEdge<L>>());
        else 
            return false;

        return true;

    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException(
                    "Tentativo di rimuovere un nodo null");
        throw new UnsupportedOperationException(
                "Rimozione dei nodi non supportata");
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {

        if (node == null)
            throw new NullPointerException("Tentativo di cercare un nodo null");

        return adjacentLists.containsKey(node);

    }

    @Override
    public GraphNode<L> getNodeOf(L label) {

        if (label == null)
            throw new NullPointerException("Tentativo di cercare un nodo null");

        for ( GraphNode<L> key : adjacentLists.keySet() ) {
            if(label.equals(key.getLabel()))
                return key;
        }

        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di ricercare un nodo con etichetta null");
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {

        if (node == null)
            throw new NullPointerException("Tentativo di cercare un nodo null");
        if (!adjacentLists.containsKey(node))
            throw new IllegalArgumentException("Tentativo di aggiungere un nodo null"); 

        Set<GraphEdge<L>> egdesOfNode = adjacentLists.get(node);

        Set<GraphNode<L>> temp = new HashSet<GraphNode<L>>();

        for( GraphEdge<L> l : egdesOfNode) {
            if(!l.getNode1().equals(node))
                temp.add(l.getNode1());
            else
                temp.add(l.getNode2());
        }

        return temp;

    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                        "Predecessore non supportato in un grafico non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {

        Set<GraphEdge<L>> temp = new HashSet<GraphEdge<L>>();
        
        for ( GraphNode<L> key : adjacentLists.keySet() ) 
            temp.addAll(adjacentLists.get(key));

        return temp;

    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {

        if (edge == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");
        if (!adjacentLists.containsKey(edge.getNode1()) || !adjacentLists.containsKey(edge.getNode2()))
            throw new IllegalArgumentException("Tentativo di aggiungere un nodo null");        
        if (edge.isDirected() == true)
            throw new IllegalArgumentException("Tentativo di aggiungere un nodo null"); 

        if(!adjacentLists.get(edge.getNode1()).contains(edge)) {
            adjacentLists.get(edge.getNode1()).add(edge);
            adjacentLists.get(edge.getNode2()).add(edge);
        }
        else return false;

        return true;

    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException(
                "Rimozione degli archi non supportata");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {

        if (edge == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");
        if (!adjacentLists.containsKey(edge.getNode1()) || !adjacentLists.containsKey(edge.getNode2()))
            throw new IllegalArgumentException("Tentativo di aggiungere un nodo null");     

        return adjacentLists.get(edge.getNode1()).contains(edge);

    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {

        if (node == null)
            throw new NullPointerException("Tentativo di cercare un nodo null");
        if (!adjacentLists.containsKey(node))
            throw new IllegalArgumentException("Tentativo di aggiungere un nodo null");   

        return adjacentLists.get(node);

    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Archi entranti non significativi in un grafo non orientato");
    }

}
