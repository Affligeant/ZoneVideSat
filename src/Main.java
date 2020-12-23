public class Main {

    public static void main(String[] args) {

        //Graph à 6 noeuds
        Graph defaultGraph = new Graph(6);
        defaultGraph.addArc(0, 1);
        defaultGraph.addArc(1, 2);
        defaultGraph.addArc(2, 3);
        defaultGraph.addArc(2, 4);
        defaultGraph.addArc(4, 5);

        System.out.println(defaultGraph.voidSAT(2));
    }
}
