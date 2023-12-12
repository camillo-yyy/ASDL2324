/**
 * 
 */
package it.unicam.cs.asdl2324.es9;

import java.util.List;

/**
 * Classe che implementa un algoritmo di ordinamento basato su heap.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 */
public class HeapSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    private int countCompare;

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {

        countCompare = 0;

        if (l == null) throw new NullPointerException("Tentativo di ordinare una lista null");
        // per ordinare la lista vuota o con un solo elemento non faccio niente
        if (l.size() <= 1) return new SortingAlgorithmResult<E>(l, 0);

        // build max heap
        MaxHeap(l);
        // chiamo la procedura heapsort
        List<E> sorted = heapSort(l);

        return new SortingAlgorithmResult<E>(sorted, countCompare);
    }

    public List<E> heapSort(List<E> l) {

        int heapsize = l.size()-1;
        E app;

        // per ogni elemento dell'heap dalla foglia alla radice
        for(int i=heapsize; i>0; i--) {

            // estraggo il massimo
            app = l.get(0);
            l.set(0, l.get(i));
            l.set(i, app);
            heapify(l, 0, i); // metto la foglia sulla radice e richiamo heapify sulla radice ma con dim-1

        }

        return l;
    }

    public void MaxHeap(List<E> list) {
        
        if(list == null) throw new NullPointerException("Puntatore null");
        // parto dalla primo nodo interno con almeno 1 foglia e maxheapifico
        for(int i = (list.size()/2)-1; i >= 0; i--) {
            heapify(list, i, list.size());
        }
        
    }


    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(List<E> list, int i, int size) {
        int max = i;
        E app;
        
        // se la foglia è compresa nella size vedo se è più grande, se sì prendo l'indice
        if(this.leftIndex(i) < size)
            if(list.get(i).compareTo(list.get(this.leftIndex(i))) < 0) {
                max = this.leftIndex(i);
                countCompare++;
            }
        // se la foglia è compresa nella size vedo se è più grande, se sì prendo l'indice   
        if(this.rightIndex(i) < size)
            if(list.get(max).compareTo(list.get(this.rightIndex(i))) < 0){
                max = this.rightIndex(i);    
                countCompare++;
            }
        // se il max non è l'indice iniziale scambio e chiamo ricorsivamente
        if(max != i) {
            app = list.get(i);
            list.set(i, list.get(max));
            list.set(max, app);

            heapify(list, max, size);
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


    @Override
    public String getName() {
        return "HeapSort";
    }

}
