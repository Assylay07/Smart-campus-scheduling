package graph;

import graph.core.*;
import graph.scc.*;
import graph.topo.*;
import graph.dagsp.*;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SmallDeterministicTests {

    @Test
    public void testSCCCondensationAndTopo() {
        Graph g = new Graph(5, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(2, 3, 1);

        Metrics m = new Metrics();
        TarjanSCC tarjan = new TarjanSCC(g, m);
        var comps = tarjan.run();
        assertEquals(2, comps.size());

        CondensationGraph cond = new CondensationGraph(g, comps);
        KahnTopoSort topo = new KahnTopoSort(cond.dag, new Metrics());
        List<Integer> order = topo.topoOrder();
        assertEquals(2, order.size());
    }

    @Test
    public void testDagShortestAndLongest() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(0, 2, 5);
        g.addEdge(2, 3, 4);

        List<Integer> topo = List.of(0, 1, 2, 3);
        Metrics m = new Metrics();
        DagShortestPaths dsp = new DagShortestPaths(g, m);

        var shortest = dsp.shortestFrom(0, topo);
        var longest = dsp.longestFrom(0, topo);

        assertEquals(0, shortest.dist[0]);
        assertEquals(9, longest.dist[3]);  // 0->1->2->3 = 9
        assertEquals(9, longest.reconstruct(3).stream()
                .mapToInt(Integer::intValue).findAny().isPresent() ? longest.dist[3] : -1);
    }
}
