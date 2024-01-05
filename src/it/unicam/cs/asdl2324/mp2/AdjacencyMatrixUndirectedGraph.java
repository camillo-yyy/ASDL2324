/**
 * 
 */
package it.unicam.cs.asdl2324.mp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * 
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 * 
 * @author Luca Tesei (template) CAMILLETTI SAMUELE samuele.camilletti@studenti.unicam.it (implementazione)
 *
 * 
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {   
        return this.nodesIndex.size();
    }

    @Override
    public int edgeCount() {

        int size = 0;
        // controllo per ogni riga della matrice associata ai nodi
        // quanti edge effettivi non null sono presenti
        for ( ArrayList<GraphEdge<L>> item : this.matrix ) {
            for ( GraphEdge<L> edge : item ) 
                if(edge != null) size++;
        }

        // divido il contatore per 2 in quanto il grafo non è orientato
        return size/2;

    }

    @Override
    public void clear() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {

        if(node == null)
            throw new NullPointerException("Puntatore a nodo null");
        if(this.nodesIndex.containsKey(node))
            return false; 

        // aggiungo un nuovo indice
        this.nodesIndex.put(node, this.nodeCount());

        // aggiorno la dimensione in "larghezza" della matrice (+1 in ogni riga)
        for ( ArrayList<GraphEdge<L>> item : this.matrix ) {
            item.add(null);
        }

        // aggiungo una nuova riga alla matrice
        this.matrix.add(new ArrayList<GraphEdge<L>>());
        // e la riempio di puntatori a null 
        for ( int i=0; i<this.nodeCount(); i++ ) {
            this.matrix.get(this.nodeCount()-1).add(null);
        }

        return true;

    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {

        if(label == null)
            throw new NullPointerException("Puntatore a etichetta null");

        // creo il nuovo nodo e faccio il controllo
        GraphNode<L> node = new GraphNode<L>(label);

        // Posso utilizzare la funzione implementata precedentemente
        return this.addNode(node);

    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {

        if(node == null)
            throw new NullPointerException("Puntatore a nodo null");
        if(!this.nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Nodo inesistente"); 
        
        // indice del nodo ricercare e rimuovere
        int index = this.nodesIndex.get(node);

        // rimuovo la riga con gli archi del nodo
        this.matrix.remove(index);
        
        // rimuovo il nodo e indice dalla hash table
        this.nodesIndex.remove(node);

        // controllo i nodi con indice superiore a quello rimosso
        // e lo diminuisco di 1 riutilizzando cosi' l'indice rimosso
        for ( GraphNode<L> temp : this.nodesIndex.keySet())
            if(this.nodesIndex.get(temp) > index) // se indice > indicerimosso
                this.nodesIndex.put(temp, this.nodesIndex.get(temp)-1);
        
        // aggiorno la dimensione in "larghezza" della matrice
        // lascio il lavoro di "restringimento" all'ArrayList
        for ( ArrayList<GraphEdge<L>> item : this.matrix ) {
            item.remove(index);
        }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {

        if(label == null)
            throw new NullPointerException("Puntatore a etichetta null");

        // creo un nuovo nodo con la label passata
        GraphNode<L> node = new GraphNode<L>(label);

        // e posso utilizzare la funzione implementata precedentemente
        this.removeNode(node);

    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {

        // Utilizzo il metodo getNode che recupera il nodo
        // ed esegue i controlli necessari
        GraphNode<L> node = this.getNode(i);

        // rimuovo la riga di indice corrispondente
        this.matrix.remove(i);

        // rimuovo il nodo e indice dalla hash table
        this.nodesIndex.remove(node);

        // controllo i nodi con indice superiore a quello rimosso
        // e lo diminuisco di 1 riutilizzando cosi' l'indice rimosso
        for ( GraphNode<L> temp : this.nodesIndex.keySet())
            if(this.nodesIndex.get(temp) > i) // se indice > indicerimosso
                this.nodesIndex.put(temp, this.nodesIndex.get(temp)-1);
        
        // aggiorno la dimensione in "larghezza" della matrice
        for ( ArrayList<GraphEdge<L>> item : this.matrix ) {
            item.remove(i);
        }

    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {

        if(node == null)
            throw new NullPointerException("Puntatore a nodo null");
        
        // scorro tra tutte le chiavi e il trovo nodo uguale
        for ( GraphNode<L> temp : this.nodesIndex.keySet())
            if(node.equals(temp)) 
                return temp;

        // se non lo trovo ritorno null
        return null;

    }

    @Override
    public GraphNode<L> getNode(L label) {

        if(label == null)
            throw new NullPointerException("Puntatore a etichetta null");

        //  creo un nuovo nodo con la label passata
        GraphNode<L> node = new GraphNode<L>(label);

        return this.getNode(node);

    }

    @Override
    public GraphNode<L> getNode(int i) {

        if(i < 0 || i > this.nodeCount() - 1)
            throw new IndexOutOfBoundsException("Indice non compreso nella dimensione");

        // scorro tra tutte le chiavi e il trovo quella con il valore intero uguale
        for ( GraphNode<L> temp : this.nodesIndex.keySet())
            if(this.nodesIndex.get(temp) == i) 
                return temp;

        // se non lo trovo ritorno null        
        return null;

    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {

        if(node == null)
            throw new NullPointerException("Puntatore a nodo null");
        if(!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Nodo inesistente"); 

        // ritorno l'indice associato alla chiave
        return this.nodesIndex.get(node);

    }

    @Override
    public int getNodeIndexOf(L label) {

        if(label == null)
            throw new NullPointerException("Puntatore a nodo null");

        //  creo un nuovo nodo con la label passata
        GraphNode<L> node = new GraphNode<L>(label);

        if(!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Nodo inesistente"); 

        // ritorno l'indice associato alla chiave
        return this.nodesIndex.get(node);

    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {

        if (edge == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()))
            throw new IllegalArgumentException("Almeno un nodo inesistente nel grafo");        
        if (edge.isDirected())
            throw new IllegalArgumentException("Arco orientato"); 

        // ottengo gli indici dei nodi che costituiscono l'arco
        int indexNodo1 = this.getNodeIndexOf(edge.getNode1());
        int indexNodo2 = this.getNodeIndexOf(edge.getNode2());

        // essendo il grafo non orientato è sufficiente 
        // controllare che almeno in una riga non sia giò presente
        if(!this.matrix.get(indexNodo1).contains(edge)) {
            // setto gli edge nella posizione della matrice corrispondente
            this.matrix.get(indexNodo1).set(indexNodo2, edge);
            this.matrix.get(indexNodo2).set(indexNodo1, edge);
        }
        else return false;

        return true;

    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {

        if (node1 == null || node2 == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2))
            throw new IllegalArgumentException("Almeno un nodo inesistente nel grafo");        
        
        //  creo un nuovo edge 
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);

        return this.addEdge(edge);

    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2,
            double weight) {

        if (node1 == null || node2 == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2))
            throw new IllegalArgumentException("Tentativo di aggiungere un nodo null");  

        //  creo un nuovo edge 
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false, weight);

        return this.addEdge(edge);

    }

    @Override
    public boolean addEdge(L label1, L label2) {

        if (label1 == null || label2 == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");

        GraphNode<L> node1, node2;

        if ((node1 = this.getNode(label1)) == null || (node2 = this.getNode(label2)) == null)
            throw new IllegalArgumentException("Almeno un nodo inesistente nel grafo");        

        //  creo un nuovo edge 
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);

        return this.addEdge(edge);

    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {

        if (label1 == null || label2 == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");

        GraphNode<L> node1, node2;

        if ((node1 = this.getNode(label1)) == null || (node2 = this.getNode(label2)) == null)
            throw new IllegalArgumentException("Almeno un nodo inesistente nel grafo");  

        //  creo un nuovo edge 
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false, weight);

        return this.addEdge(edge);

    }

    @Override
    public boolean addEdge(int i, int j) {

        if (i < 0 || i > this.nodeCount() - 1 || j < 0 || j > this.nodeCount() - 1)
            throw new IndexOutOfBoundsException("Tentativo di aggiungere un arco null");

        GraphNode<L> node1, node2;

        if ((node1 = this.getNode(i)) == null || (node2 = this.getNode(j)) == null)
            throw new NullPointerException("Almeno un nodo inesistente nel grafo");   

        //  creo un nuovo edge     
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);

        return this.addEdge(edge);
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {

        if (i < 0 || i > this.nodeCount() - 1 || j < 0 || j > this.nodeCount() - 1)
            throw new IndexOutOfBoundsException("Tentativo di aggiungere un arco null");

        GraphNode<L> node1, node2;

        if ((node1 = this.getNode(i)) == null || (node2 = this.getNode(j)) == null)
            throw new NullPointerException("Almeno un nodo inesistente nel grafo");   

        //  creo un nuovo edge  
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false, weight);
        
        return this.addEdge(edge);

    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {

        if (edge == null)
            throw new NullPointerException("Tentativo di modificare un arco null");
        if (this.getEdge(edge) == null)
            throw new IllegalArgumentException("Arco non esistente nel grafo");
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()))
            throw new IllegalArgumentException("Almeno un nodo non esistente nel grafo");

        // ottengo l'indice dei due nodi
        int indexNodo1 = this.getNodeIndexOf(edge.getNode1());
        int indexNodo2 = this.getNodeIndexOf(edge.getNode2());

        // ottengo l'indice dell'edge nella rispettiva riga della matrice
        int indexEdge1 = this.matrix.get(indexNodo1).indexOf(edge);
        int indexEdge2 = this.matrix.get(indexNodo2).indexOf(edge);

        // setto a null la posizioni corrispondenti
        this.matrix.get(indexNodo1).set(indexEdge1, null);
        this.matrix.get(indexNodo2).set(indexEdge2, null);
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {

        if (node1 == null || node2 == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");
            
        this.removeEdge(new GraphEdge<L>(node1, node2, false));

    }

    @Override
    public void removeEdge(L label1, L label2) {

        if (label1 == null || label2 == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");

        GraphNode<L> node1, node2;

        if ((node1 = this.getNode(label1)) == null || (node2 = this.getNode(label2)) == null)
            throw new IllegalArgumentException("Almeno un nodo non esistente nel grafo"); 
        
        this.removeEdge(new GraphEdge<L>(node1, node2, false));

    }

    @Override
    public void removeEdge(int i, int j) {

        if (i < 0 || i > this.nodeCount() - 1 || j < 0 || j > this.nodeCount() - 1)
            throw new IndexOutOfBoundsException("Tentativo di aggiungere un arco null");

        GraphNode<L> node1, node2;

        if ((node1 = this.getNode(i)) == null || (node2 = this.getNode(j)) == null)
            throw new NullPointerException("Almeno un nodo non esistente nel grafo");     
        
        this.removeEdge(new GraphEdge<L>(node1, node2, false));

    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {

        if (edge == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()))
            throw new IllegalArgumentException("Almeno un nodo non esistente nel grafo");
            
        // ottengo l'indice di un nodo    
        int indexNodo = this.getNodeIndexOf(edge.getNode1());
        int indexEdge;
        
        // se l'indice associato all'edge nella riga della matrice corrispondente al nodo
        // è maggiore di -1 e pertanto esiste, ritorno quel nodo della matrice
        if((indexEdge = this.matrix.get(indexNodo).indexOf(edge)) > -1)
            return this.matrix.get(indexNodo).get(indexEdge);
        else
            return null;

    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {

        if (node1 == null || node2 == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2))
            throw new IllegalArgumentException("Almeno un nodo non esistente nel grafo");
        
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);

        return this.getEdge(edge);

    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {

        if (label1 == null || label2 == null)
            throw new NullPointerException("Tentativo di aggiungere un arco null");

        GraphNode<L> node1, node2;

        if ((node1 = this.getNode(label1)) == null || (node2 = this.getNode(label2)) == null)
            throw new IllegalArgumentException("Almeno un nodo non esistente nel grafo");        
        
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);

        return this.getEdge(edge);

    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {

        if (i < 0 || i > this.nodeCount() - 1 || j < 0 || j > this.nodeCount() - 1)
            throw new IndexOutOfBoundsException("Tentativo di aggiungere un arco null");

        GraphNode<L> node1, node2;

        if ((node1 = this.getNode(i)) == null || (node2 = this.getNode(j)) == null)
            throw new NullPointerException("Almeno un nodo non esistente nel grafo");        
        
        GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);

        return this.getEdge(edge);

    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {

        if (node == null)
            throw new NullPointerException("Puntatore a nodo null");
        if(!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Nodo inesistente"); 

        // set contenitore dei nodi adiacenti
        Set<GraphNode<L>> adjNodes = new HashSet<GraphNode<L>>();

        // ottengo l'indice del nodo
        int indexNodo = this.getNodeIndexOf(node);

        // scorro la riga corrispondente nella matrice 
        for( GraphEdge<L> edge : this.matrix.get(indexNodo)) {
            // se l'arco non è null
            if(edge != null) {
                // aggiungo al set il nodo dell'arco non uguale a 
                // quello che sto cercando 
                // (questa condizione supporta anche un eventuale cappio)
                if(!edge.getNode1().equals(node))
                    adjNodes.add(edge.getNode1());
                else
                    adjNodes.add(edge.getNode2());
            }
        }
        
        return adjNodes;

    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {

        if(label == null)
            throw new NullPointerException("Puntatore a etichetta null");

        //  creo un nuovo nodo con la label passata
        GraphNode<L> node = new GraphNode<L>(label);

        return this.getAdjacentNodesOf(node);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        // lascio che getNode lanci le dovute eccezioni
        return this.getAdjacentNodesOf(this.getNode(i));
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {

        if (node == null)
            throw new NullPointerException("Puntatore a nodo null");
        if(!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Nodo inesistente"); 

        Set<GraphEdge<L>> edges = new HashSet<GraphEdge<L>>();
        // scorro ed aggiungo al set tutti gli edge non null
        // contenuti nella rispettiva riga della matrice
        for( GraphEdge<L> edge : this.matrix.get(this.getNodeIndexOf(node)) ) {
            if(edge != null)
                edges.add(edge);
        }

        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {

        if(label == null)
            throw new NullPointerException("Puntatore a etichetta null");

        //  creo un nuovo nodo con la label passata
        GraphNode<L> node = new GraphNode<L>(label);

        return this.getEdgesOf(node);

    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        // lascio che getNode lanci le dovute eccezioni
        return this.getEdgesOf(this.getNode(i));
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {

        Set<GraphEdge<L>> edges = new HashSet<GraphEdge<L>>();
        
        // scorro l'intera matrice aggiungendo al set
        // gli edge not null
        for( ArrayList<GraphEdge<L>> adjRow : this.matrix ) {
            for( GraphEdge<L> edge : adjRow ) {
                if(edge != null)
                    edges.add(edge);
            }
        }

        return edges;

    }
}
