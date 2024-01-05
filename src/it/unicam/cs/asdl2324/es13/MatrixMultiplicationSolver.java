package it.unicam.cs.asdl2324.es13;

import java.util.List;

/**
 * Un sover prende una certa sequenza di matrici da moltiplicare e calcola una
 * parentesizzazione ottima, cioè che minimizza il numero di moltiplicazioni
 * scalari necessarie per moltiplicare tutte le matrici.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class MatrixMultiplicationSolver {

    // sequenza delle dimensioni delle matrici da moltiplicare
    private List<Integer> p;

    // matrice dei costi minimi
    private int[][] m;

    // matrice delle scelte dei k che corrispondono al costo minimo
    private int[][] b;

    /**
     * Costruisce un solver per una certa sequenza di matrici da moltiplicare,
     * date le loro dimensioni righeXcolonne. Il calcolo della soluzione ottima
     * viene eseguito subito, cioè come parte di questo costruttore.
     * 
     * @param p
     *              è una lista di valori che sono le dimensioni delle matrici,
     *              ad esempio se p = [10, 100, 5, 50] allora sto moltiplicando
     *              3 matrici (p.size() - 1) le cui dimensioni sono A_{0} =
     *              10x100, A_{1} = 100x5, A_{2} = 5x50
     * @throws NullPointerException
     *                                      se la lista passata è null
     * @throws IllegalArgumentException
     *                                      se la lista p contiene meno di due
     *                                      elementi (cioè deve contenere almeno
     *                                      una matrice. Nel caso di una unica
     *                                      matrice la soluzione è 0 e la
     *                                      parentesizzazione è la matrice
     *                                      stessa, cioè "A_{0}")
     */
    public MatrixMultiplicationSolver(List<Integer> p) {
        if (p == null)
            throw new NullPointerException("Lista nulla");
        if (p.size() <= 1)
            throw new IllegalArgumentException(
                    "Lista di dimensione non valida");
        this.p = p;
        this.m = new int[p.size() - 1][p.size() - 1];
        this.b = new int[p.size() - 1][p.size() - 1];
        this.solve();
    }

    /*
     * Risolve il problema della parentesizzazione ottima con la programmazione
     * dinamica.
     */
    private void solve() {

        for (int i = 0; i < this.p.size() - 1; i++) {
            this.m[i][i] = 0; // La moltiplicazione di una sola matrice non ha costo
            this.b[i][i] = i; // La scelta di k per la moltiplicazione di una sola matrice è sempre se stessa
        }

        for (int len = 2; len < this.p.size(); len++) {
            for (int i = 0; i < this.p.size() - len; i++) {
                int j = i + len - 1;
                this.m[i][j] = Integer.MAX_VALUE;
                // Cerchiamo il valore di k che minimizza il costo per la moltiplicazione 
                for (int k = i; k < j; k++) {
                    // Calcoliamo il costo per la moltiplicazione e verifichiamo se è minore del costo minimo attuale
                    int cost = this.m[i][k] + this.m[k + 1][j] + this.p.get(i) * this.p.get(k + 1) * this.p.get(j + 1);
                    if (cost < this.m[i][j]) {
                        // Aggiorniamo il costo minimo
                        this.m[i][j] = cost;
                        // Aggiorniamo la scelta di k
                        this.b[i][j] = k;
                    }
                }
            }
        }
    }

    /**
     * Restituisce il numero minimo di moltiplicazioni necessarie per
     * moltiplicare la sequenza di matrici di questo solver. Nel caso di una
     * sola matrice restituisce zero.
     * 
     * @return il numero minimo di moltiplicazioni soluzione del problema di
     *         parentesizzazione
     */
    public int getOptimalCost() {
        return m[0][p.size() - 2];
    }

    /**
     * Restituisce una parentesizzazione ottima.
     * 
     * Il formato prevede l'uso di "A_{i}" per indicare la i-esima matrice di
     * dimensione p.get(i) x p.get(i+1) con 0 <= i <= p.size() - 2. Ad esempio
     * la parentesizzazione con una sola matrice deve restituire "A_{0}", la
     * parentesizzazione con due matrici deve restituire "(A_{0} x A_{1})", la
     * parentesizzazione con tre matrici deve restituire "((A_{0} x A_{1}) x
     * A_{2})" oppure "(A_{0} x (A_{1} x A_{2}))" e così via.
     * 
     * @return una parentesizzazione ottima
     */
    public String getOptimalParenthesization() {
        return traceBack(0, p.size() - 2);
    }

    /*
     * Effettua il traceback utilizzando la matrice b che è stata riempita
     * appositamente durante il processo di calcolo del costo minimo
     */
    private String traceBack(int i, int j) {

        // Se i e j sono uguali, la sottosequenza è una sola matrice, quindi la stampiamo e terminiamo la ricorsione
        if (i == j) {
            return "A_{" + i + "}";
        }

        // Altrimenti, stampiamo la parentesi sinistra e continuiamo il traceback per la sotto-sequenza di matrici prima di k
        String result = "(" + traceBack(i, this.b[i][j]);

        // Quindi stampiamo la parentesi destra e continuiamo il traceback per la sotto-sequenza di matrici dopo k
        result += " x " + traceBack(this.b[i][j] + 1, j);

        result += ")";
        return result;
    
    }

}
