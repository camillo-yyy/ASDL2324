package it.unicam.cs.asdl2324.es2;

/**
 * Uno scassinatore è un oggetto che prende una certa cassaforte e trova la
 * combinazione utilizzando la "forza bruta".
 * 
 * @author Luca Tesei
 *
 */
public class Burglar {

    // Attributi
    private CombinationLock tr;
    private int attempts;

    /**
     * Costruisce uno scassinatore per una certa cassaforte.
     * 
     * @param aCombinationLock
     * @throw NullPointerException se la cassaforte passata è nulla
     */
    public Burglar(CombinationLock aCombinationLock) {
        
        if(aCombinationLock == null) throw new NullPointerException("Cassaforte non valida");

        this.tr = aCombinationLock;
        this.attempts = -1;

    }

    /**
     * Forza la cassaforte e restituisce la combinazione.
     * 
     * @return la combinazione della cassaforte forzata.
     */
    public String findCombination() {
        
        for(int i = 65; i<=90; i++) {

            for(int j = 65; j<=90; j++){

                for(int w = 65; w<=90; w++){

                    this.tr.setPosition((char) i);
                    this.tr.setPosition((char) j);
                    this.tr.setPosition((char) w);

                    this.tr.open();
                    this.attempts++;
                    if(this.tr.isOpen()) {
                        String a = "";
                        a += (char) i;
                        a += (char) j;
                        a += (char) w;

                        return a;
                    }

                }

            }

        }

        return null;
    }

    /**
     * Restituisce il numero di tentativi che ci sono voluti per trovare la
     * combinazione. Se la cassaforte non è stata ancora forzata restituisce -1.
     * 
     * @return il numero di tentativi che ci sono voluti per trovare la
     *         combinazione, oppure -1 se la cassaforte non è stata ancora
     *         forzata.
     */
    public long getAttempts() {
        
        return this.attempts;

    }
}
