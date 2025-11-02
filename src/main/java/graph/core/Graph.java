package graph.core;

import java.util.*;


public class Graph {
    public final int n;
    public final boolean directed;
    // adjacency: u -> list of pairs (v, w)
    private final List<List<Edge>> adj;


    public static class Edge {
        public final int to;
        public final long w;
        public Edge(int to, long w) { this.to = to; this.w = w; }
    }


    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }


    public void addEdge(int u, int v, long w) {
        adj.get(u).add(new Edge(v, w));
        if (!directed) adj.get(v).add(new Edge(u, w));
    }


    public List<Edge> neighbors(int u) { return Collections.unmodifiableList(adj.get(u)); }
}