package it.unicam.cs.asdl2324.es7;

import java.util.List;

// TODO completare import

/**
 * Implementazione dell'algoritmo di Insertion Sort integrata nel framework di
 * valutazione numerica. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 * @param <E>
 *                Una classe su cui sia definito un ordinamento naturale.
 */
public class InsertionSort<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        if (l == null) throw new NullPointerException("Tentativo di ordinare una lista null");
        // per ordinare la lista vuota o con un solo elemento non faccio niente
        if (l.size() <= 1) return new SortingAlgorithmResult<E>(l, 0);

        // Dichiaro var locali
        int countCompare = 0;
        E key;
        int j;

        for(int i = 1; i < l.size(); i++) {

            key = l.get(i); // Per ogni elemento della lista trovo la sua posizione 
            j = i - 1;

            // Controllo tutti gli elementi prima, se ne trovo uno minore ho terminato
            while(j >= 0 && key.compareTo(l.get(j)) < 0) {

                // Scorro se minore
                ++countCompare;
                l.set(j+1, l.get(j));
                --j;

            }
            // Posiziono l'elemento analizzato nell'indice corretto
            l.set(j+1, key);

        }

        return new SortingAlgorithmResult<E>(l, countCompare);
    }

    public String getName() {
        return "InsertionSort";
    }
}
