package graph.scc;

import graph.core.Graph;
import graph.core.Graph.Edge;
import graph.core.Metrics;
import java.util.*;

public class TarjanSCC {
    private final Graph g;
    private final Metrics metrics;

    public TarjanSCC(Graph g, Metrics metrics) {
        this.g = g;
        this.metrics = metrics;
    }


    public List<List<Integer>> run() {
        int n = g.n;
        int[] index = new int[n];
        Arrays.fill(index, -1);
        int[] low = new int[n];
        boolean[] onStack = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        List<List<Integer>> comps = new ArrayList<>();

        final int[] idx = {0};
        for (int v = 0; v < n; v++) {
            if (index[v] == -1) dfs(v, index, low, onStack, stack, comps, idx);
        }
        return comps;
    }


    private void dfs(int v, int[] index, int[] low, boolean[] onStack, Deque<Integer> stack,
                     List<List<Integer>> comps, int[] idx) {
        index[v] = low[v] = idx[0]++;
        stack.push(v);
        onStack[v] = true;
        metrics.countDFSVisit();

        for (Edge e : g.neighbors(v)) {
            metrics.countEdgeVisited();
            int to = e.to;
            if (index[to] == -1) {
                dfs(to, index, low, onStack, stack, comps, idx);
                low[v] = Math.min(low[v], low[to]);
            } else if (onStack[to]) {
                low[v] = Math.min(low[v], index[to]);
            }
        }

        if (low[v] == index[v]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int w = stack.pop();
                onStack[w] = false;
                comp.add(w);
                if (w == v) break;
            }
            comps.add(comp);
        }
    }
}