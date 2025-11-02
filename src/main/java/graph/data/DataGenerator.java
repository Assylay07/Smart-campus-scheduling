package graph.data;

import graph.core.*;
import java.io.*;
import java.util.*;


public class DataGenerator {

    private static final Random rnd = new Random(42);

    public static void main(String[] args) throws IOException {
        new File("data").mkdirs();

        generateSet(6, 10, 3, "small");
        generateSet(10, 20, 3, "medium");
        generateSet(20, 50, 3, "large");

        System.out.println("9 datasets generated under ./data/");
    }

    private static void generateSet(int minN, int maxN, int count, String label) throws IOException {
        for (int i = 1; i <= count; i++) {
            int n = rnd.nextInt(maxN - minN + 1) + minN;
            boolean cyclic = (i % 2 == 1); // alternate cyclic/acyclic
            boolean dense = (i == count);  // last one dense
            Graph g = randomGraph(n, cyclic, dense);
            String name = String.format("data/%s_%d.json", label, i);
            Utils.saveGraph(g, name, 0);
        }
    }

    private static Graph randomGraph(int n, boolean cyclic, boolean dense) {
        Graph g = new Graph(n, true);
        int maxEdges = dense ? n * (n - 1) / 2 : n + rnd.nextInt(n);
        Set<Long> seen = new HashSet<>();

        for (int e = 0; e < maxEdges; e++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);
            if (u == v) continue;
            long code = ((long) u << 32) | v;
            if (seen.contains(code)) continue;
            seen.add(code);
            int w = 1 + rnd.nextInt(9);
            g.addEdge(u, v, w);
        }


        if (!cyclic) {
            Graph dag = new Graph(n, true);
            for (int u = 0; u < n; u++) {
                for (Graph.Edge e : g.neighbors(u)) {
                    if (u < e.to) dag.addEdge(u, e.to, e.w);
                }
            }
            return dag;
        }
        return g;
    }
}
