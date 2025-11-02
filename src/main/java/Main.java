import graph.core.*;
import graph.scc.*;
import graph.topo.*;
import graph.dagsp.*;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: mvn exec:java -Dexec.mainClass=graph.Main -Dexec.args=\"<path-to-file-or-folder>\"");
            return;
        }

        File input = new File(args[0]);
        List<String> datasets = new ArrayList<>();

        if (input.isFile()) {
            datasets.add(input.getPath());
        } else if (input.isDirectory()) {
            for (File f : Objects.requireNonNull(input.listFiles((d, name) -> name.endsWith(".json")))) {
                datasets.add(f.getPath());
            }
        } else {
            System.err.println(" Invalid path: " + args[0]);
            return;
        }

        for (String path : datasets) {
            System.out.println("\n==============================");
            System.out.println(" Processing dataset: " + path);
            System.out.println("==============================");

            try {
                runForDataset(path);
            } catch (Exception e) {
                System.err.println(" Error while processing " + path + ": " + e.getMessage());
            }
        }

        System.out.println("\n Done.");
    }

    private static void runForDataset(String path) throws Exception {
        Utils.GraphLoadResult loaded = Utils.loadGraphWithSource(path);
        Graph g = loaded.graph;
        int source = loaded.source;
        Metrics metrics = new Metrics();

        // SCC
        metrics.startTimer();
        TarjanSCC tarjan = new TarjanSCC(g, metrics);
        var components = tarjan.run();
        metrics.stopTimer();

        System.out.println("\n=== Strongly Connected Components ===");
        int idx = 0;
        for (var comp : components) {
            System.out.printf("Component %d (size=%d): %s%n", idx++, comp.size(), comp);
        }
        System.out.printf("Total components: %d%n", components.size());

        //Condensation Graph
        CondensationGraph cond = new CondensationGraph(g, components);
        Metrics topoMetrics = new Metrics();

        topoMetrics.startTimer();
        KahnTopoSort topo = new KahnTopoSort(cond.dag, topoMetrics);
        var topoOrder = topo.topoOrder();
        topoMetrics.stopTimer();

        System.out.println("\n=== Condensation DAG ===");
        System.out.println("Order: " + topoOrder);

        //Shortest Paths
        Metrics spMetrics = new Metrics();
        DagShortestPaths dagsp = new DagShortestPaths(cond.dag, spMetrics);

        spMetrics.startTimer();
        var shortest = dagsp.shortestFrom(source, topoOrder);
        spMetrics.stopTimer();

        System.out.println("\n=== Shortest Paths ===");
        printDistances(shortest.dist);

        //Longest Path
        Metrics lpMetrics = new Metrics();
        DagShortestPaths daglp = new DagShortestPaths(cond.dag, lpMetrics);
        lpMetrics.startTimer();
        var longest = daglp.longestFrom(source, topoOrder);
        lpMetrics.stopTimer();

        int endNode = argmax(longest.dist);
        System.out.println("\n=== Critical Path ===");
        System.out.println("Path: " + longest.reconstruct(endNode));
        System.out.println("Length: " + longest.dist[endNode]);
    }

    private static void printDistances(long[] dist) {
        System.out.println("Vertex : Distance");
        for (int i = 0; i < dist.length; i++) {
            System.out.printf("%7d : %s%n", i, dist[i] >= Long.MAX_VALUE / 4 ? "INF" : dist[i]);
        }
    }

    private static int argmax(long[] arr) {
        int idx = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[idx]) idx = i;
        }
        return idx;
    }
}
