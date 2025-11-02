package graph.core;

import java.util.*;


public class CondensationGraph {
    public final Graph dag;
    public final int[] compId; // compId[v] -> component index


    public CondensationGraph(Graph g, List<List<Integer>> comps) {
        int c = comps.size();
        this.dag = new Graph(c, true);
        this.compId = new int[g.n];
        for (int i = 0; i < c; i++) {
            for (int v : comps.get(i)) compId[v] = i;
        }

        Set<Long> seen = new HashSet<>();
        for (int u = 0; u < g.n; u++) {
            for (Graph.Edge e : g.neighbors(u)) {
                int cu = compId[u], cv = compId[e.to];
                if (cu != cv) {
                    long code = ((long)cu << 32) | (cv & 0xffffffffL);
                    if (!seen.contains(code)) {
                        seen.add(code);
                        dag.addEdge(cu, cv, e.w);
                    }
                }
            }
        }
    }
}
