**Author:** Assylay Shukirbay  
**Course:** Design and Analysis of Algorithms

---
# Smart Campus Scheduling

##  Overview
This project implements core graph algorithms used in dependency and scheduling analysis:
- Strongly Connected Components (SCC) - via Tarjan’s algorithm
- Condensation DAG construction
- Topological Sorting - via Kahn’s algorithm
- Shortest and Longest Paths in DAG - via Dynamic Programming

Each algorithm is instrumented with a common Metrics interface for counting operations and measuring execution time.

---

##  Project Structure
```
smart_campus/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java
│   │   │   └── graph/
│   │   │       ├── core/
│   │   │       ├── scc/
│   │   │       ├── topo/
│   │   │       ├── dagsp/
│   │   │       └── data/
│   │   └── resources/
│   │       └── data/   ← 9 JSON datasets (small, medium, large)
│   └── test/
│       └── java/
│           └── graph/
├── target/
└── .gitignore

```

---

##  Building & Running

###  Requirements
- **Java 17+**
- **Apache Maven 3.8+**

### Build
```bash
mvn clean compile
```

### Run on a single dataset
```bash
mvn compile exec:java "-Dexec.mainClass=graph.Main" "-Dexec.args=src/main/resources/data/small1.json"
```

### Run on all datasets
```bash
mvn compile exec:java "-Dexec.mainClass=graph.Main" "-Dexec.args=src/main/resources/data"
```
(or PowerShell / Bash script)

---

## Instrumentation (Metrics)

All algorithms share a common metrics system for consistent measurement.

| Metric | Description | Used In |
|--------|--------------|---------|
| `dfsVisits` | Number of vertices visited during DFS | SCC |
| `edgesVisited` | Number of edges traversed | SCC |
| `kahnPush`, `kahnPop` | Queue operations | TopoSort |
| `relaxations` | Edge relaxations | DAG Shortest/Longest |
| `timeNs` | Execution time in nanoseconds | All |

Timing is implemented via `System.nanoTime()` to ensure high-resolution measurement.

---

## Testing

Unit tests are implemented using JUnit 5.

```bash
mvn test
```

### Test coverage:
-  SCC correctness on small deterministic graphs
-  Topological order verification
-  DAG shortest & longest path correctness
-  Edge cases (empty graph, single vertex, cyclic graph)

---

## Experimental Data

### Datasets
All datasets are in `/src/main/resources/data/`.

| Dataset | Vertices | Edges | Notes |
|----------|-----------|--------|-------|
| small1–3 | 6–8 | sparse | simple verification |
| medium1–3 | 10–15 | medium density | mixed SCCs |
| large1–3 | 20–50 | dense | stress test for metrics |

### Results (Summary)
All datasets successfully processed:

| Task | Algorithm | Metrics Measured | Typical Time |
|------|------------|------------------|---------------|
| SCC | Tarjan | DFS visits, edges | < 1 ms |
| Topological Sort | Kahn | Push/Pop counts | < 1 ms |
| DAG-SP (shortest) | DP | Relaxations | < 1 ms |
| DAG-SP (longest) | DP | Relaxations | < 1 ms |

---

## Analysis

- **SCC** — fast on sparse graphs; complexity scales linearly with edges.
- **Condensation DAG** — simplifies cyclic graphs for further DAG-only algorithms.
- **Topological Sort** — bottleneck only appears in dense DAGs (large queue).
- **DAG Shortest/Longest** — most efficient after topological order; depends on SCC count.

**Structural effects:**
- Graph **density** increases DFS edge traversals.
- Large **SCCs** reduce DAG size, improving later steps.
- Sparse graphs lead to many `INF` distances in DAG-SP, as no paths exist between components.

---

## Conclusion

- **Tarjan’s algorithm** is the best choice for detecting SCCs in linear time `O(V + E)`.
- **Kahn’s algorithm** provides a clear and efficient topological order for DAG processing.
- **Dynamic Programming over Topo order** allows both shortest and critical path analysis in DAGs efficiently.
- For large or dense graphs, separating the SCC and condensation steps improves overall performance and clarity.



