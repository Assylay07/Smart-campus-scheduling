package graph.topo;

import graph.core.Graph;
import graph.core.Metrics;
import java.util.*;

public class KahnTopoSort {
    private final Graph g;
    private final Metrics metrics;


    public KahnTopoSort(Graph g, Metrics metrics) {
        this.g = g; this.metrics = metrics;
    }


    public List<Integer> topoOrder() {
        int n = g.n;
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) for (var e : g.neighbors(u)) indeg[e.to]++;


        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) { queue.add(i); metrics.countKahnPush(); }
        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.remove(); metrics.countKahnPop();
            order.add(u);
            for (var e : g.neighbors(u)) {
                if (--indeg[e.to] == 0) { queue.add(e.to); metrics.countKahnPush(); }
            }
        }
        return order;
    }
}
