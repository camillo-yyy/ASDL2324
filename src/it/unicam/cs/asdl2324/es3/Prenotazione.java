package it.unicam.cs.asdl2324.es3;

/**
 * Una prenotazione riguarda una certa aula per un certo time slot.
 * 
 * @author Luca Tesei 
 *
 */
public class Prenotazione implements Comparable<Prenotazione> {

    private final String aula;

    private final TimeSlot timeSlot;

    private String docente;

    private String motivo;

    /**
     * Costruisce una prenotazione.
     * 
     * @param aula
     *                     l'aula a cui la prenotazione si riferisce
     * @param timeSlot
     *                     il time slot della prenotazione
     * @param docente
     *                     il nome del docente che ha prenotato l'aula
     * @param motivo
     *                     il motivo della prenotazione
     * @throws NullPointerException
     *                                  se uno qualsiasi degli oggetti passati è
     *                                  null
     */
    public Prenotazione(String aula, TimeSlot timeSlot, String docente,
            String motivo) {
        if(aula == null || timeSlot == null || docente == null || motivo == null) throw new NullPointerException("Uno degli oggetti passati non esiste");

        this.aula = aula;
        this.timeSlot = timeSlot;
        this.docente = docente;
        this.motivo = motivo;
    }

    /**
     * @return the aula
     */
    public String getAula() {
        return aula;
    }

    /**
     * @return the timeSlot
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * @return the docente
     */
    public String getDocente() {
        return docente;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param docente
     *                    the docente to set
     */
    public void setDocente(String docente) {
        if(docente == null) throw new NullPointerException("oggetto passato non esiste");

        this.docente = docente;
    }

    /**
     * @param motivo
     *                   the motivo to set
     */
    public void setMotivo(String motivo) {
        if(motivo == null) throw new NullPointerException("oggetto passato non esiste");

        this.motivo = motivo;
    }

    /*
     * Due prenotazioni sono uguali se hanno la stessa aula e lo stesso time
     * slot. Il docente e il motivo possono cambiare senza influire
     * sull'uguaglianza.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null) return false;
        if(!(obj instanceof Prenotazione)) return false;

        Prenotazione a = (Prenotazione) obj;

        if(this.aula == a.getAula() && this.timeSlot.equals(a.timeSlot)) return true;
        else return false;
    }

    /*
     * L'hashcode di una prenotazione si calcola a partire dai due campi usati
     * per equals.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;

        // si usa il valore intero corrispondente alla rappresentazione del
        // double bit a bit (64 bit, cio� un long)
        temp = Double.doubleToLongBits(this.aula.hashCode());
        // si fa il bitwise XOR tra i 64 bit originali e il loro shift a destra
        // di 32 bit, poi si fa il cast a int
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.timeSlot.hashCode());
        result = prime * result + (int) (temp ^ (temp >>> 32));

        return result;
    }

    /*
     * Una prenotazione precede un altra in base all'ordine dei time slot. Se
     * due prenotazioni hanno lo stesso time slot allora una precede l'altra in
     * base all'ordine tra le aule.
     */
    @Override
    public int compareTo(Prenotazione o) {
        if(this.timeSlot.compareTo(o.getTimeSlot()) < 0) return -1;
        else if(this.timeSlot.compareTo(o.getTimeSlot()) == 0) return this.aula.compareTo(o.getAula());
        else return 1;
    }

    @Override
    public String toString() {
        return "Prenotazione [aula = " + aula + ", time slot =" + timeSlot
                + ", docente=" + docente + ", motivo=" + motivo + "]";
    }

}
