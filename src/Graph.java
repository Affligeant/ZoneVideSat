/**
 * Graph represented by a node matrix.
 * Non-oriented.
 */
public class Graph {

    public Integer[][] nodes;

    public Graph(int nbNodes) {
        this.nodes = new Integer[nbNodes][nbNodes];

        //Initialisation du tableau à 0 partout (aucun arc)
        for(int i = 0; i < nbNodes; i++) {
            for(int j = 0; j < nbNodes; j++) {
                this.nodes[i][j] = 0;
            }
        }
    }

    public void addArc(int node1, int node2) { nodes[node1][node2] = 1; nodes[node2][node1] = 1; }


    /**
     * @param nbSommet Node concerned
     * @param nbVoid Node of void area
     * @return attribute corresponding to nbSommet is the nbVoid Node.
     */
    private int resolve(int nbSommet, int nbVoid) {
        return (nbVoid * nodes.length) + (nbSommet + 1);
    }

    /**
     * Computes a SAT formula from the graph which satisfies the problem of a
     * void area of size {@code voidSize}.
     * @param voidSize Length of the desired void area.
     * @return A String in DIMACS CNF format.
     */
    public String voidSAT(int voidSize) {

        String result = "c Fichier au format DIMACS CNF généré pour le mini-projet 2 du TP2 de complexité.\n";

        StringBuilder clauses = new StringBuilder();

        int nbVariables = nodes.length * voidSize;
        int nbClauses = 0;




        //Clauses pour satisfaire la taille de la solution
        for(int i = 0; i < voidSize; i++) {
            StringBuilder clause = new StringBuilder();
            for(int j = 0; j < nodes.length; j++) {
                clause.append( resolve(j, i) );
                clause.append(" ");
            }

            clause.append("0\n"); //Clause terminée
            nbClauses++;
            clauses.append(clause.toString());
        }

        //Clauses pour satisfaire la non-duplication des nœuds dans la solution
        // i.e un nœud du graphe qui correspond à un nœud de la zone vide ne peut
        // pas correspondre en même temps à un autre nœud de la zone vide.
        for(int j = 0; j < nodes.length; j++) {
            for(int i = 0; i < voidSize; i++) {
                for(int k = 0; k < voidSize - i; k++) {
                    String c = "-" + resolve(j, i) + " -" + resolve(j, k) + " 0\n";
                    clauses.append(c);
                    nbClauses++;
                }
            }
        }

        //Clauses pour satisfaire l'absence de lien entre les nœuds de la solution.
        for(int i = 0; i < voidSize; i++) {
            for(int j = 0; j < voidSize; j++) {
                if(j != i) {
                    for(int k = 0; k < nodes.length; k++) {
                        for(int l = 0; l < nodes.length; l++) {

                            // Si il y a un arc entre les nœuds k et l, les deux ne peuvent
                            // appartenir en même temps à la solution. i.e. au moins un des deux
                            // est faux. i.e. les variables "k est le nœud i" et "l est le nœud j"
                            // contiennent au minimum un faux.

                            if(nodes[k][l] == 1) {
                                String c = "-" + resolve(k, i) + " -" + resolve(l, j) + " 0\n";
                                clauses.append(c);
                                nbClauses++;
                            }
                        }
                    }
                }
            }
        }

        result += "p cnf " + nbVariables + " " + nbClauses + "\n";
        result += clauses.toString();

        return result;
    }

}
