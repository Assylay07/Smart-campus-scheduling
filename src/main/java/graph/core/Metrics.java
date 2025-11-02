package graph.core;

public class Metrics {
    private long startTime = 0;
    private long elapsedNs = 0;
    private long dfsVisits = 0;
    private long edgesVisited = 0;
    private long kahnPush = 0;
    private long kahnPop = 0;
    private long relaxations = 0;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        elapsedNs = System.nanoTime() - startTime;
    }

    public long elapsedNs() {
        return elapsedNs;
    }

    public void countDFSVisit() {
        dfsVisits++;
    }

    public void countEdgeVisited() {
        edgesVisited++;
    }

    public void countKahnPush() {
        kahnPush++;
    }

    public void countKahnPop() {
        kahnPop++;
    }

    public void countRelaxation() {
        relaxations++;
    }

    // Getters
    public long getDfsVisits() {
        return dfsVisits;
    }

    public long getEdgesVisited() {
        return edgesVisited;
    }

    public long getKahnPush() {
        return kahnPush;
    }

    public long getKahnPop() {
        return kahnPop;
    }

    public long getRelaxations() {
        return relaxations;
    }
}
