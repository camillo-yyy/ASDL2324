/**
 * 
 */
package it.unicam.cs.asdl2324.es7;

import java.util.List;
import java.util.ArrayList;

/**
 * Implementazione dell'algoritmo di Merge Sort integrata nel framework di
 * valutazione numerica. Non è richiesta l'implementazione in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    private int countCompare;

    public SortingAlgorithmResult<E> sort(List<E> l) {
        
        countCompare = 0;

        if (l == null) throw new NullPointerException("Tentativo di ordinare una lista null");
        // per ordinare la lista vuota o con un solo elemento non faccio niente
        if (l.size() <= 1) return new SortingAlgorithmResult<E>(l, 0);

        mergeSort(l, 0, l.size() - 1);

        return new SortingAlgorithmResult<E>(l, countCompare);
    }
    
    private void mergeSort(List<E> l, int p, int r) {

        if(r <= p) return;  // condizione di stop quando r <= p

        int q = (p+r)/2;    // Trovo la meta

        mergeSort(l, p, q);  // chiamata ricorsiva 1
        mergeSort(l, q+1, r); // chiamata ricorsiva 2

        merge(l, p, q, r);  // Merging

    }

    private void merge(List<E> l, int s, int m, int e) {

        List<E> left = new ArrayList<E>();
        List<E> right = new ArrayList<E>();

        // riempio le liste di appoggio
        for(int i = s; i <= m; i++) left.add(l.get(i));
        for(int i = m + 1; i <= e; i++) right.add(l.get(i));

        int i = 0; // Scorre su left
        int j = 0; // Scorre su right
        int k = s; // Scorre sulla lista principale

        // Finchè una delle due semiliste non terminano
        while(i < left.size() && j < right.size()) {
            countCompare++;
            // Confronto e riempio con l'elemento più piccolo
            if(left.get(i).compareTo(right.get(j)) < 0) { 
                l.set(k, left.get(i));
                i++;
            }
            else {
                l.set(k, right.get(j));
                j++;
            }  
            k++;
        }

        // Completo il resto della lista con la semilista non terminata
        while(i<left.size())  {
            l.set(k, left.get(i));
            i++;
            k++;
        }
        while(j<right.size())  {
            l.set(k, right.get(j));
            j++;
            k++;
        }
    }

    public String getName() {
        return "MergeSort";
    }
}
