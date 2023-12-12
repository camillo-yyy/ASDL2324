/**
 * 
 */
package it.unicam.cs.asdl2324.es3;

import java.util.GregorianCalendar;

/**
 * Un time slot è un intervallo di tempo continuo che può essere associato ad
 * una prenotazione. Gli oggetti della classe sono immutabili. Non sono ammessi
 * time slot che iniziano e finiscono nello stesso istante.
 * 
 * @author Luca Tesei
 *
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /**
     * Rappresenta la soglia di tolleranza da considerare nella sovrapposizione
     * di due Time Slot. Se si sovrappongono per un numero di minuti minore o
     * uguale a questa soglia allora NON vengono considerati sovrapposti.
     */
    public static final int MINUTES_OF_TOLERANCE_FOR_OVERLAPPING = 5;

    private final GregorianCalendar start;

    private final GregorianCalendar stop;

    /**
     * Crea un time slot tra due istanti di inizio e fine
     * 
     * @param start
     *                  inizio del time slot
     * @param stop
     *                  fine del time slot
     * @throws NullPointerException
     *                                      se uno dei due istanti, start o
     *                                      stop, è null
     * @throws IllegalArgumentException
     *                                      se start è uguale o successivo a
     *                                      stop
     */
    public TimeSlot(GregorianCalendar start, GregorianCalendar stop) {

        if(start == null || stop == null) throw new NullPointerException("Uno degli oggetti passati non esiste");

        if(start.compareTo(stop) == 0 || start.compareTo(stop) > 0) throw new IllegalArgumentException("Ora Start deve essere minore di ora stop");

        this.start = start;
        this.stop = stop;
    }

    /**
     * @return the start
     */
    public GregorianCalendar getStart() {
        return start;
    }

    /**
     * @return the stop
     */
    public GregorianCalendar getStop() {
        return stop;
    }

    /*
     * Un time slot è uguale a un altro se rappresenta esattamente lo stesso
     * intervallo di tempo, cioè se inizia nello stesso istante e termina nello
     * stesso istante.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null) return false;
        if(!(obj instanceof TimeSlot)) return false;

        TimeSlot a = (TimeSlot) obj;

        if(this.start.equals(a.getStart()) && this.stop.equals(a.getStop())) return true;
        else return false;
    }

    /*
     * Il codice hash associato a un timeslot viene calcolato a partire dei due
     * istanti di inizio e fine, in accordo con i campi usati per il metodo
     * equals.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;

        // si usa il valore intero corrispondente alla rappresentazione del
        // double bit a bit (64 bit, cio� un long)
        temp = Double.doubleToLongBits(this.start.hashCode());
        // si fa il bitwise XOR tra i 64 bit originali e il loro shift a destra
        // di 32 bit, poi si fa il cast a int
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.stop.hashCode());
        result = prime * result + (int) (temp ^ (temp >>> 32));

        return result;
    }

    /*
     * Un time slot precede un altro se inizia prima. Se due time slot iniziano
     * nello stesso momento quello che finisce prima precede l'altro. Se hanno
     * stesso inizio e stessa fine sono uguali, in compatibilità con equals.
     */
    @Override
    public int compareTo(TimeSlot o) {
        GregorianCalendar a = o.getStart();
        GregorianCalendar s = o.getStop();

        if(this.start.compareTo(a) < 0) return -1;
        else if(this.start.compareTo(a) == 0) {

            if(this.stop.compareTo(s) < 0) return -1;
            else if(this.stop.compareTo(s) == 0) return 0;
            else return 1;
        }
        else return 1;
    }

    /**
     * Determina il numero di minuti di sovrapposizione tra questo timeslot e
     * quello passato.
     * 
     * @param o
     *              il time slot da confrontare con questo
     * @return il numero di minuti di sovrapposizione tra questo time slot e
     *         quello passato, oppure -1 se non c'è sovrapposizione. Se questo
     *         time slot finisce esattamente al millisecondo dove inizia il time
     *         slot <code>o</code> non c'è sovrapposizione, così come se questo
     *         time slot inizia esattamente al millisecondo in cui finisce il
     *         time slot <code>o</code>. In questi ultimi due casi il risultato
     *         deve essere -1 e non 0. Nel caso in cui la sovrapposizione non è
     *         di un numero esatto di minuti, cioè ci sono secondi e
     *         millisecondi che avanzano, il numero dei minuti di
     *         sovrapposizione da restituire deve essere arrotondato per difetto
     * @throws NullPointerException
     *                                      se il time slot passato è nullo
     * @throws IllegalArgumentException
     *                                      se i minuti di sovrapposizione
     *                                      superano Integer.MAX_VALUE
     */
    public int getMinutesOfOverlappingWith(TimeSlot o) {
    	if(o == null) {
    		throw new NullPointerException();
    	}

        // Avvio/Stop oggetto chiamante
    	long a1 = this.start.getTimeInMillis();
    	long s1 = this.stop.getTimeInMillis();
        // Avvio/Stop oggetto passato
    	long a2 = o.getStart().getTimeInMillis();
    	long s2 = o.getStop().getTimeInMillis();
    	
    	int time = -1;
    	
    	// controlla se i due timeslot iniziano in seguito all'altro
        if(s1 == a2 || s2 == a1) return -1;
        else {
        	// Se il secondo timeslot è contenuto nel primo
        	if (a1 <= a2 && s1 >= s2) time = (int) ((s2 - a2)/1000/60);

        	// Se il primo timeslot è contenuto nel secondo
        	else if (a1 >= a2 && s1 <= s2) time = (int) ((s1 - a1)/1000/60);

        	// Se il primo timeslot inizia e finisce dopo il secondo
        	else if (a1 >= a2 && a1 <= s2) time = (int) ((s2 - a1)/1000/60);

        	// Se il primo timeslot inizia e finisce prima del secondo
        	else if (a1 <= a2 && s1 >= a2) time = (int) ((s1 - a2)/1000/60);
        }
        
        if(time > Integer.MAX_VALUE) throw new IllegalArgumentException();
        else return time;
    }

    /**
     * Determina se questo time slot si sovrappone a un altro time slot dato,
     * considerando la soglia di tolleranza.
     * 
     * @param o
     *              il time slot che viene passato per il controllo di
     *              sovrapposizione
     * @return true se questo time slot si sovrappone per più (strettamente) di
     *         MINUTES_OF_TOLERANCE_FOR_OVERLAPPING minuti a quello passato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean overlapsWith(TimeSlot o) {
    	if(o == null) {
    		throw new NullPointerException();
    	}

        // Avvio/Stop oggetto chiamante
    	long a1 = this.start.getTimeInMillis();
    	long s1 = this.stop.getTimeInMillis();
        // Avvio/Stop oggetto passato
    	long a2 = o.getStart().getTimeInMillis();
    	long s2 = o.getStop().getTimeInMillis();
    	

        // Se il secondo timeslot è contenuto nel primo
        if (a1 <= a2 && s1 >= s2 && ((s2 - a2)/1000/60) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING) return true;

        // Se il primo timeslot è contenuto nel secondo
        else if (a1 >= a2 && s1 <= s2 && ((s1 - a1)/1000/60) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING) return true;

        // Se il primo timeslot inizia e finisce dopo il secondo
        else if (a1 >= a2 && s1 >= s2 && ((s2 - a1)/1000/60) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING) return true;

        // Se il primo timeslot inizia e finisce prima del secondo
        else if(a1 <= a2 && s1 <= s2 && ((s1 - a2)/1000/60) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING) return true;
        
        else return false;
    }

    /*
     * Ridefinisce il modo in cui viene reso un TimeSlot con una String.
     * 
     * Esempio 1, stringa da restituire: "[4/11/2019 11.0 - 4/11/2019 13.0]"
     * 
     * Esempio 2, stringa da restituire: "[10/11/2019 11.15 - 10/11/2019 23.45]"
     * 
     * I secondi e i millisecondi eventuali non vengono scritti.
     */
    @Override
    public String toString() {
        return "[" + this.start.get(GregorianCalendar.DAY_OF_MONTH)
                     + "/" + (this.start.get(GregorianCalendar.MONTH)+1)
                     + "/" + this.start.get(GregorianCalendar.YEAR) + " "
                     + this.start.get(GregorianCalendar.HOUR_OF_DAY) + "."
                     + this.start.get(GregorianCalendar.MINUTE) + " - "
                    + this.stop.get(GregorianCalendar.DAY_OF_MONTH)
                     + "/" + (this.stop.get(GregorianCalendar.MONTH)+1)
                     + "/" + this.stop.get(GregorianCalendar.YEAR) + " "
                    + this.stop.get(GregorianCalendar.HOUR_OF_DAY) + "."
                    + this.stop.get(GregorianCalendar.MINUTE) + "]";
    }

}
