/**
 * 
 */
package it.unicam.cs.asdl2324.es4;

/**
 * Una Presence Facility è una facility che può essere presente oppure no. Ad
 * esempio la presenza di un proiettore HDMI oppure la presenza dell'aria
 * condizionata.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class PresenceFacility extends Facility {

    /**
     * Costruisce una presence facility.
     * 
     * @param codice
     * @param descrizione
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla.
     */
    public PresenceFacility(String codice, String descrizione) {
        super(codice, descrizione);
    }

    /*
     * Una Presence Facility soddisfa una facility solo se la facility passata è
     * una Presence Facility ed ha lo stesso codice.
     * 
     */
    @Override
    public boolean satisfies(Facility o) {
        if(o == null) throw new NullPointerException("Puntatore nullo"); // Se la facilty non esiste
        if(!(o instanceof PresenceFacility)) return false; // Se la facilty non è una istanza di PresenceFacility

        if(this.equals(o)) return true; // Se il codice è uguale (uso equals in quanto due facility sono uguali solo se hanno lo stesso codice) 
        else return false;
    }

}
