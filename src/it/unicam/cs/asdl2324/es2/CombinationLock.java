package it.unicam.cs.asdl2324.es2;

/**
 * Un oggetto cassaforte con combinazione ha una manopola che può essere
 * impostata su certe posizioni contrassegnate da lettere maiuscole. La
 * serratura si apre solo se le ultime tre lettere impostate sono uguali alla
 * combinazione segreta.
 * 
 * @author Luca Tesei
 */
public class CombinationLock {

    // TODO inserire le variabili istanza che servono
    // Attributi
    private String password;
    private String lastDigits;
    private boolean isClosed; // Indica lo stato della cassaforte: true = chiusa, false = aperta;

    /**
     * Costruisce una cassaforte <b>aperta</b> con una data combinazione
     * 
     * @param aCombination
     *                         la combinazione che deve essere una stringa di 3
     *                         lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     *        stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */
    public CombinationLock(String aCombination) {
        
        // Controllo se il puntatore alla stringa è nullo
        if(aCombination == null) throw new NullPointerException("Combinazione non fornita");
        // Controllo se la stringa contiene solo carattere maiuscoli utilizzando una espressione regolare
        if(!aCombination.matches("[\\p{Lu}]+") || aCombination.length() != 3) throw new IllegalArgumentException("La combinazione può essere composta solo caratteri MAIUSCOLI");
        
        this.password = aCombination;
        this.lastDigits = "";
        this.isClosed = false;
    }

    /**
     * Imposta la manopola su una certaposizione.
     * 
     * @param aPosition
     *                      un carattere lettera maiuscola su cui viene
     *                      impostata la manopola
     * @throws IllegalArgumentException
     *                                      se il carattere fornito non è una
     *                                      lettera maiuscola dell'alfabeto
     *                                      inglese
     */
    public void setPosition(char aPosition) {
        
        // controllo con il valore ascii del char
        if(aPosition < 65 || aPosition > 90) throw new IllegalArgumentException("La combinazione può essere composta solo caratteri MAIUSCOLI");

        // controllo lunghezza se non-completa appendo altrimenti shifto di un carattere a sinistra
        if(this.lastDigits.length() < 3) this.lastDigits += aPosition;
        else {

            this.lastDigits = "";
            this.lastDigits = this.lastDigits.substring(1) + aPosition;

        }

    }

    /**
     * Tenta di aprire la serratura considerando come combinazione fornita le
     * ultime tre posizioni impostate. Se l'apertura non va a buon fine le
     * lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     */
    public void open() {
        
        if(this.isClosed == false) return;

        if(this.password.equals(this.lastDigits)) this.isClosed = false;
        else this.lastDigits = "";

    }

    /**
     * Determina se la cassaforte è aperta.
     * 
     * @return true se la cassaforte è attualmente aperta, false altrimenti
     */
    public boolean isOpen() {

        if(this.isClosed == false) return true;
        else return false;

    }

    /**
     * Chiude la cassaforte senza modificare la combinazione attuale. Fa in modo
     * che se si prova a riaprire subito senza impostare nessuna nuova posizione
     * della manopola la cassaforte non si apre. Si noti che se la cassaforte
     * era stata aperta con la combinazione giusta le ultime posizioni impostate
     * sono proprio la combinazione attuale.
     */
    public void lock() {

        if(this.isClosed == true) return;

        this.isClosed = true;
        this.lastDigits = "";
    }

    /**
     * Chiude la cassaforte e modifica la combinazione. Funziona solo se la
     * cassaforte è attualmente aperta. Se la cassaforte è attualmente chiusa
     * rimane chiusa e la combinazione non viene cambiata, ma in questo caso le
     * le lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     * 
     * @param aCombination
     *                         la nuova combinazione che deve essere una stringa
     *                         di 3 lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     *        stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */
    public void lockAndChangeCombination(String aCombination) {

        if(this.isClosed == true){

            this.lastDigits = "";
            return;

        }
        // Controllo se il puntatore alla stringa è nullo
        if(aCombination == null) throw new NullPointerException("Combinazione non fornita");
        // Controllo se la stringa contiene solo carattere maiuscoli utilizzando una espressione regolare
        if(!aCombination.matches("[\\p{Lu}]+") || aCombination.length() != 3) throw new IllegalArgumentException("La combinazione può essere composta solo caratteri MAIUSCOLI");
        this.password = aCombination;
        this.isClosed = true;
        this.lastDigits = "";
    }

    public String getLastDigit() {

        return this.lastDigits;

    }
}