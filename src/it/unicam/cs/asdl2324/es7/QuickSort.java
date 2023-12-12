/**
 * 
 */
package it.unicam.cs.asdl2324.es7;

import java.util.List;

// TODO completare import

/**
 * Implementazione del QuickSort con scelta della posizione del pivot fissa.
 * L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    private int countCompare;

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {

        countCompare = 0;

        if (l == null) throw new NullPointerException("Tentativo di ordinare una lista null");
        // per ordinare la lista vuota o con un solo elemento non faccio niente
        if (l.size() <= 1) return new SortingAlgorithmResult<E>(l, 0);

        quickSort(l, 0, l.size() - 1);

        return new SortingAlgorithmResult<E>(l, countCompare);

    }

    private void quickSort(List<E> l, int p, int r) {

        // condizione di stop quando p = r
        if(p < r) {

            int m = partition(l, p, r); // partizione
            quickSort(l, p, m-1); // chiamata ricorsiva 1
            quickSort(l, m+1, r); // chiamata ricorsiva 2

        }

    }

    private int partition(List<E> l, int p, int r) {

        E app;
        E pivot = l.get(r);
        int i = (p - 1);

        // pongo gli elementi < del pivot a sinistra, e gli elementi >= a destra della lista
        for(int j = p; j < r; j++) { // il contatore j conta i maggiori uguali e scorre a prescindere

            countCompare++;
            if(l.get(j).compareTo(pivot) < 0) {

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

    @Override
    public String getName() {
        return "QuickSort";
    }

}
