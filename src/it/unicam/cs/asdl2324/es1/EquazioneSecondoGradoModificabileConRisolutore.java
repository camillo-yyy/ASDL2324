/**
 * 
 */
package it.unicam.cs.asdl2324.es1;

/**
 * Un oggetto di questa classe permette di rappresentare una equazione di
 * secondo grado e di trovarne le soluzioni reali. I valori dei parametri
 * dell'equazione possono cambiare nel tempo. All'inizio e ogni volta che viene
 * cambiato un parametro la soluzione dell'equazione non esiste e deve essere
 * calcolata con il metodo <code>solve()</code>. E' possibile sapere se al
 * momento la soluzione dell'equazione esiste con il metodo
 * <code>isSolved()</code>. Qualora la soluzione corrente non esista e si tenti
 * di ottenerla verra' lanciata una eccezione.
 * 
 * @author Template: Luca Tesei, Implementation: Samuele Camilletti 
 *         
 *
 */
public class EquazioneSecondoGradoModificabileConRisolutore {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    private static final double EPSILON = 1.0E-15;

    /* Coefficienti dell'equazione */
    private double a;

    private double b;

    private double c;

    private boolean solved; // Boolean per verifica stato equazione (risolta o meno)

    private SoluzioneEquazioneSecondoGrado lastSolution; // pointer a al risultato dell'equazione

    /**
     * Costruisce una equazione di secondo grado modificabile. All'inizio
     * l'equazione non è risolta.
     * 
     * 
     * @param a
     *              coefficiente del termine x^2, deve essere diverso da zero.
     * @param b
     *              coefficiente del termine x
     * @param c
     *              termine noto
     * @throws IllegalArgumentException
     *                                      se il parametro <code>a</code> è
     *                                      zero
     * 
     */
    public EquazioneSecondoGradoModificabileConRisolutore(double a, double b,
            double c) {
        // Controllo che a non sia pari a 0 altrimenti lancio una eccezione
        if(Math.abs(a) < EPSILON) throw new IllegalArgumentException("Il coefficiente di a non può essere pari a zero in una eq. di secondo grado");

        this.a = a;
        this.b = b;
        this.c = c;

        solved = false; // La variabile booleana di risoluzione è inizialmente falsa.
    }

    /**
     * @return il valore corrente del parametro a
     */
    public double getA() {
        return a;
    }

    /**
     * Cambia il parametro a di questa equazione. Dopo questa operazione
     * l'equazione andra' risolta di nuovo.
     * 
     * @param a
     *              il nuovo valore del parametro a
     * @throws IllegalArgumentException
     *                                      se il nuovo valore è zero
     */
    public void setA(double a) {
        // Controllo che a non sia pari a 0 altrimenti lancio una eccezione
        if(Math.abs(a) < EPSILON) throw new IllegalArgumentException("Il coefficiente di a non può essere pari a zero in una eq. di secondo grado");

        this.a = a;
        solved = false; // Resetto il solved

    }

    /**
     * @return il valore corrente del parametro b
     */
    public double getB() {
        return b;
    }

    /**
     * Cambia il parametro b di questa equazione. Dopo questa operazione
     * l'equazione andra' risolta di nuovo.
     * 
     * @param b
     *              il nuovo valore del parametro b
     */
    public void setB(double b) {
        this.b = b;
        solved = false; // Resetto il solved
    }

    /**
     * @return il valore corrente del parametro c
     */
    public double getC() {
        return c;
    }

    /**
     * Cambia il parametro c di questa equazione. Dopo questa operazione
     * l'equazione andra' risolta di nuovo.
     * 
     * @param c
     *              il nuovo valore del parametro c
     */
    public void setC(double c) {
        this.c = c;
        solved = false; // Resetto il solved
    }

    /**
     * Determina se l'equazione, nel suo stato corrente, è gia' stata risolta.
     * 
     * @return true se l'equazione è risolta, false altrimenti
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Risolve l'equazione risultante dai parametri a, b e c correnti. Se
     * l'equazione era gia' stata risolta con i parametri correnti non viene
     * risolta di nuovo.
     */
    public void solve() {
        

        if(solved == false) {
            double delta;

            delta = b*b - 4*a*c; // delta calcolo

            // genero object corrispondente in base al delta
            if(Math.abs(delta) < EPSILON) lastSolution = new SoluzioneEquazioneSecondoGrado(this, -b / (2*a));
            else if(delta < 0) lastSolution = new SoluzioneEquazioneSecondoGrado(this);
            else if(delta > 0) lastSolution = new SoluzioneEquazioneSecondoGrado(this, (-b + Math.sqrt(delta)) / (2*a), (-b - Math.sqrt(delta)) / (2*a));

            solved = true; // cambio stato
        }

    }

    /**
     * Restituisce la soluzione dell'equazione risultante dai parametri
     * correnti. L'equazione con i parametri correnti deve essere stata
     * precedentemente risolta.
     * 
     * @return la soluzione
     * @throws IllegalStateException
     *                                   se l'equazione risulta non risolta,
     *                                   all'inizio o dopo il cambiamento di
     *                                   almeno uno dei parametri
     */
    public SoluzioneEquazioneSecondoGrado getSolution() {
        // se la soluzione e' stata calcolata
        if(solved == true) {
            return lastSolution;
        }
        else throw new IllegalStateException("Soluzione non esistente");
    }

}
