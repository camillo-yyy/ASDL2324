package it.unicam.cs.asdl2324.mp1;

import java.util.Set;
import java.util.HashSet;

/**
 * la classe LinkedListDisjointSets implementa una collezione di insiemi disgiunti
 * basata sulle API definite dall'interfaccia DisjointSets.
 * 
 * La collezione di insiemi disgiunti è implementata attraverso un Set che 
 * che tiene traccia di tutti i puntatori dei rappresentanti degli insiemi.
 * Le operazioni di makeSet e findSet hanno un costo di Θ(1) come da requisito:
 * makeSet funge da costruttore per l'elemento singoletto mentre findSet si limita a ritornare
 * l'elemento rappresentante puntato dal puntatore Ref1.
 * 
 * L'operazione di union controlla se i due elementi fanno parte di set diversi,
 * poi memorizza quale delle due liste è più piccola, attaccando alla testa della lista più
 * grande la testa della lista più piccola. A questo punto è possibile aggiornare tutti i riferimenti
 * al rappresentante ed attaccare alla coda della lista più piccola il resto della lista più grande, scorrendoù
 * la lista più piccola, pertanto l'intera operazione avrà costo Θ(min(x, y)).
 * 
 * 
 * @author Luca Tesei (template) CAMILLETTI SAMUELE samuele.camilletti@studenti.unicam.it (implementazione)
 *
 */
public class LinkedListDisjointSets implements DisjointSets {

    // Set utilizzato per la memorizzazione di tutti i puntatori ai rappresentanti
    HashSet<DisjointSetElement> disjointSetsCollection;


    /**
     * Crea una collezione vuota di insiemi disgiunti.
     */
    public LinkedListDisjointSets() {
        this.disjointSetsCollection = new HashSet<DisjointSetElement>();
    }

    /*
     * Nella rappresentazione con liste concatenate un elemento è presente in
     * qualche insieme disgiunto se il puntatore al suo elemento rappresentante
     * (ref1) non è null.
     */
    @Override
    public boolean isPresent(DisjointSetElement e) {
        return e.getRef1() != null;
    }

    /*
     * Nella rappresentazione con liste concatenate un nuovo insieme disgiunto è
     * rappresentato da una lista concatenata che contiene l'unico elemento. Il
     * rappresentante deve essere l'elemento stesso e la cardinalità deve essere
     * 1.
     */
    @Override
    public void makeSet(DisjointSetElement e) {
        
        if(e == null) 
            throw new NullPointerException("Elemento inesistente.");

        // se è già presente
        if(isPresent(e))
            throw new IllegalArgumentException("Elemento già presente in uno degli insiemi disgiunti");

        // l'elemento singoletto è il rappresentante
        e.setRef1(e);
        e.setRef2(null); // non ha un successivo
        e.setNumber(1);  // la cardinalità è 1

        // aggiungo il rappresentante alla collezione
        this.disjointSetsCollection.add(e);

    }

    /*
     * Nella rappresentazione con liste concatenate per trovare il
     * rappresentante di un elemento basta far riferimento al suo puntatore
     * ref1.
     */
    @Override
    public DisjointSetElement findSet(DisjointSetElement e) {

        if(e == null) 
            throw new NullPointerException("Elemento inesistente.");

        // se non è  presente
        if(!isPresent(e))
            throw new IllegalArgumentException("Elemento non presente in uno degli insiemi disgiunti");
        
        // ritorno il rappresentante
        return e.getRef1();

    }

    /*
     * Dopo l'unione di due insiemi effettivamente disgiunti il rappresentante
     * dell'insieme unito è il rappresentate dell'insieme che aveva il numero
     * maggiore di elementi tra l'insieme di cui faceva parte {@code e1} e
     * l'insieme di cui faceva parte {@code e2}. Nel caso in cui entrambi gli
     * insiemi avevano lo stesso numero di elementi il rappresentante
     * dell'insieme unito è il rappresentante del vecchio insieme di cui faceva
     * parte {@code e1}.
     * 
     * Questo comportamento è la risultante naturale di una strategia che
     * minimizza il numero di operazioni da fare per realizzare l'unione nel
     * caso di rappresentazione con liste concatenate.
     * 
     */
    @Override
    public void union(DisjointSetElement e1, DisjointSetElement e2) {

        if(e1 == null || e2 == null) 
            throw new NullPointerException("Elemento inesistente.");

        // se non è presente
        if(!isPresent(e1) || !isPresent(e2))
            throw new IllegalArgumentException("Elemento non presente in uno degli insiemi disgiunti");

        // var locali
        DisjointSetElement temp1; // rappresentante della lista più grande
        DisjointSetElement succ1; // successivo della lista più grande
        DisjointSetElement temp2; // rappresentante della lista più piccola

        // se fanno parte di insiemi diversi
        if(e1.getRef1() != e2.getRef1()) {

            // vedo quale dei due insiemi è più grande
            if(e1.getRef1().getNumber() >= e2.getRef1().getNumber()) {

                // sentinelle 
                temp1 = e1.getRef1(); // rappresentante della lista più grande
                succ1 = e1.getRef1().getRef2(); // successivo del rappresentante della lista più grande

                temp2 = e2.getRef1(); // rappresentante della lista più piccola

            }
            else {

                // sentinelle 
                temp1 = e2.getRef1(); // rappresentante della lista più grande
                succ1 = e2.getRef1().getRef2(); // successivo del rappresentante della lista più grande

                temp2 = e1.getRef1(); // rappresentante della lista più piccola

            }
            // aggiorno la cardinalitò sommando quelle duei due insiemi
            temp1.getRef1().setNumber(temp1.getRef1().getNumber()+temp2.getRef1().getNumber());
            
            // il successivo della testa della prima lista è il rappresentante della lista più piccola
            temp1.setRef2(temp2); 
            disjointSetsCollection.remove(temp2);  // lo tolgo dal set
            temp2.setNumber(0); // resetto la sua cardinalità 

            // scorro la lista concatenata più piccola ed
            // aggiorno i puntatori al primo riferimento (cioè il rappresentante)
            // con quello della lista più lunga (che diventa il rappresentante)
            while(temp2.getRef2() != null) {
                temp2.setRef1(temp1.getRef1());
                temp2 = temp2.getRef2();
            }
            temp2.setRef1(temp1.getRef1()); // trovo ed aggiorno il puntatore della coda

            // la coda della lista più piccola si attacca al resto della lista più grande
            temp2.setRef2(succ1);
            
        }
    }

    @Override
    public Set<DisjointSetElement> getCurrentRepresentatives() {
        return this.disjointSetsCollection;
    }

    @Override
    public Set<DisjointSetElement> getCurrentElementsOfSetContaining(
            DisjointSetElement e) {

        if(e == null) 
            throw new NullPointerException("Elemento inesistente.");

        // se non è già presente
        if(!isPresent(e))
            throw new IllegalArgumentException("Elemento non presente in uno degli insiemi disgiunti");

        // set da ritornare
        HashSet<DisjointSetElement> retSet = new HashSet<DisjointSetElement>(); 
        DisjointSetElement temp; // sentinella

        temp = e.getRef1();
        
        // partendo dal rappresentante scorro la lista
        while(temp != null) {
            retSet.add(temp); // aggiungendo al set gli elementi
            temp = temp.getRef2(); // scorro
        }

        return retSet;
    }

    @Override
    public int getCardinalityOfSetContaining(DisjointSetElement e) {

        if(e == null) 
            throw new NullPointerException("Elemento inesistente.");

        // se è già presente
        if(!isPresent(e))
            throw new IllegalArgumentException("Elemento già presente in uno degli insiemi disgiunti");

        // ritorno la cardinalità dell'insieme contenuta nel rappresentante
        return e.getRef1().getNumber();
    }

}
