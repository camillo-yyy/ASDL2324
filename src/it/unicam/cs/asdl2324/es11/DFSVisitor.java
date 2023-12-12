package it.unicam.cs.asdl2324.es11;
/**
 * Classe singoletto che fornisce lo schema generico di visita Depth-First di
 * un grafo rappresentato da un oggetto di tipo Graph<L>.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L> le etichette dei nodi del grafo
 */
public class DFSVisitor<L> {

    // Variabile "globale" per far andare avanti il tempo durante la DFS e
    // assegnare i relativi tempi di scoperta e di uscita dei nodi
    // E' protected per permettere il test JUnit
    protected int time;

    /**
     * Esegue la visita in profondità di un certo grafo. Setta i valori seguenti
     * valori associati ai nodi: tempo di scoperta, tempo di fine visita,
     * predecessore. Ogni volta che un nodo viene visitato viene eseguito il
     * metodo visitNode sul nodo. In questa classe il metodo non fa niente,
     * basta creare una sottoclasse e ridefinire il metodo per eseguire azioni
     * particolari.
     * 
     * @param g
     *              il grafo da visitare.
     * @throws NullPointerException
     *                                  se il grafo passato è null
     */
    public void DFSVisit(Graph<L> g) {
        // NOTA: inizializza il grafo e chiama la recDFS sui nodi in un ordine
        // qualsiasi per calcolare la "foresta" DFS
        if (g == null) 
            throw new NullPointerException("Grafo o nodo sorgente nulli");

        for(GraphNode<L> l : g.getNodes()) {
            l.setColor(0);
            l.setEnteringTime(Integer.MAX_VALUE);
            l.setExitingTime(Integer.MAX_VALUE);
            l.setPrevious(null);
        }
        this.time = 0;

        for(GraphNode<L> l : g.getNodes()) {
            if(l.getColor() == GraphNode.COLOR_WHITE) {
                recDFS(g, l);
            }
        }
    }

    /*
     * Esegue la DFS ricorsivamente sul nodo passato.
     * 
     * @param g il grafo
     * 
     * @param u il nodo su cui parte la DFS
     */
    protected void recDFS(Graph<L> g, GraphNode<L> u) {
        // NOTA: chiamare il metodo visitNode alla "scoperta" di un nuovo nodo
        u.setColor(1);
        this.time+=1;
        u.setEnteringTime(this.time);
        this.visitNode(u);
        
        for(GraphNode<L> l : g.getAdjacentNodesOf(u)) {

            if(l.getColor() == GraphNode.COLOR_WHITE) {
                l.setPrevious(u);
                this.recDFS(g, l);
            }
            
        }
        u.setColor(2);
        this.time+=1;
        u.setExitingTime(this.time);
    }

    /**
     * Questo metodo, che di default non fa niente, viene chiamato su tutti i
     * nodi visitati durante la DFS nel momento in cui il colore passa da grigio
     * a nero. Ridefinire il metodo in una sottoclasse per effettuare azioni
     * specifiche.
     * 
     * @param n
     *              il nodo visitato
     */
    public void visitNode(GraphNode<L> n) {
        /*
         * In questa classe questo metodo non fa niente. Esso può essere
         * ridefinito in una sottoclasse per fare azioni particolari.
         */
    }

}
