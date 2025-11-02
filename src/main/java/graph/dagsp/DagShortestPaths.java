package graph.dagsp;

import graph.core.Graph;
import graph.core.Metrics;

import java.util.*;

public class DagShortestPaths {
    private final Graph g;
    private final Metrics metrics;

    public DagShortestPaths(Graph g, Metrics metrics) {
        this.g = g;
        this.metrics = metrics;
    }

    public Result shortestFrom(int source, List<Integer> topoOrder) {
        int n = g.n;
        long INF = Long.MAX_VALUE / 4;
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        dist[source] = 0;

        for (int u : topoOrder) {
            if (dist[u] == INF) continue;
            for (var e : g.neighbors(u)) {
                metrics.countRelaxation();
                if (dist[e.to] > dist[u] + e.w) {
                    dist[e.to] = dist[u] + e.w;
                    parent[e.to] = u;
                }
            }
        }
        return new Result(dist, parent);
    }

    public Result longestFrom(int source, List<Integer> topoOrder) {
        int n = g.n;
        long NEG_INF = Long.MIN_VALUE / 4;
        long[] dist = new long[n];
        Arrays.fill(dist, NEG_INF);
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        dist[source] = 0;

        for (int u : topoOrder) {
            if (dist[u] == NEG_INF) continue;
            for (var e : g.neighbors(u)) {
                metrics.countRelaxation();
                if (dist[e.to] < dist[u] + e.w) {
                    dist[e.to] = dist[u] + e.w;
                    parent[e.to] = u;
                }
            }
        }
        return new Result(dist, parent);
    }

    public static class Result {
        public final long[] dist;
        public final int[] parent;

        public Result(long[] dist, int[] parent) {
            this.dist = dist;
            this.parent = parent;
        }

        public List<Integer> reconstruct(int t) {
            List<Integer> path = new ArrayList<>();
            if (t < 0 || t >= parent.length) return path;
            for (int cur = t; cur != -1; cur = parent[cur]) path.add(cur);
            Collections.reverse(path);
            return path;
        }
    }
}
