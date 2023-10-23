package it.unicam.cs.asdl2324.es2;

/**
 * Uno scassinatore è un oggetto che prende una certa cassaforte e trova la
 * combinazione utilizzando la "forza bruta".
 * 
 * @author Luca Tesei | implementazione: Camilletti Samuele
 *
 */
public class Burglar {

    // Attributi
    private CombinationLock tr; // Riferimento alla cassaforte 
    private int attempts; // Numero di tentativi

    /**
     * Costruisce uno scassinatore per una certa cassaforte.
     * 
     * @param aCombinationLock
     * @throw NullPointerException se la cassaforte passata è nulla
     */
    public Burglar(CombinationLock aCombinationLock) {
        
        // Se il riferimento è nullo
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
        
        // Prima lettera
        for(int i = 65; i<=90; i++) {

            // Seconda lettera
            for(int j = 65; j<=90; j++){

                // Terza lettera  
                for(int w = 65; w<=90; w++){

                    // Setto le 3 posizioni
                    this.tr.setPosition((char) i);
                    this.tr.setPosition((char) j);
                    this.tr.setPosition((char) w);

                    this.tr.open(); // provo ad aprire
                    this.attempts++; // incremento il n. tentativi
                    if(this.tr.isOpen()) { // Controllo Se si è aperta 
                        String a = ""; // essendo String immutabile ogni volta ricrea un oggetto stringa 
                        a += (char) i;
                        a += (char) j;
                        a += (char) w;

                        return a; // ritorno con la stringa risultante
                    }

                }

            }

        }

        return null; // scenario impossibile
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
