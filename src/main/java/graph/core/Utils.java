package graph.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Utils {

    public static class JsonEdge {
        public int u;
        public int v;
        public long w = 1; // default weight
    }

    public static class JsonGraph {
        public boolean directed = true;
        public int n;
        public List<JsonEdge> edges = new ArrayList<>();
        public Integer source = 0;
    }


    public static class GraphLoadResult {
        public final Graph graph;
        public final int source;

        public GraphLoadResult(Graph graph, int source) {
            this.graph = graph;
            this.source = source;
        }
    }


    public static GraphLoadResult loadGraphWithSource(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGraph json = mapper.readValue(new File(path), JsonGraph.class);

        if (json == null) throw new IOException("Empty or invalid JSON at " + path);
        if (json.n <= 0) throw new IOException("Invalid 'n' in JSON: " + json.n);

        Graph g = new Graph(json.n, json.directed);
        if (json.edges != null) {
            for (JsonEdge e : json.edges) {
                // guard: skip invalid edges
                if (e == null) continue;
                if (e.u < 0 || e.u >= json.n || e.v < 0 || e.v >= json.n) continue;
                g.addEdge(e.u, e.v, e.w);
            }
        }
        int src = (json.source != null) ? json.source : 0;
        if (src < 0 || src >= json.n) src = 0;
        return new GraphLoadResult(g, src);
    }


    public static void saveGraph(Graph g, String path, int source) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGraph jg = new JsonGraph();
        jg.n = g.n;
        jg.directed = g.directed;
        jg.source = source;

        for (int u = 0; u < g.n; u++) {
            for (Graph.Edge e : g.neighbors(u)) {
                JsonEdge je = new JsonEdge();
                je.u = u;
                je.v = e.to;
                je.w = e.w;
                jg.edges.add(je);
            }
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), jg);
    }

    public static class Task {
        public String file;
    }

    public static Task[] loadTasks(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path), Task[].class);
    }

}
