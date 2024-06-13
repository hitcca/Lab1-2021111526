package main.java.com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.java.com.example.Graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;

class Case1 {
    private Graph graph;

    @BeforeEach
    public void setUp() {
        graph = new Graph();

        // 构建图
        graph.addEdge("a", "b");
        graph.addEdge("b", "c");
        graph.addEdge("a", "d");
        graph.addEdge("d", "c");
        graph.addEdge("a", "e");
        graph.addEdge("e", "c");
    }

    @Test
    public void testWord1NotInGraph() {
        String result = graph.queryBridgeWords("x", "b");
        assertEquals("No x or b in the graph!", result);
    }

    @Test
    public void testWord2NotInGraph() {
        String result = graph.queryBridgeWords("a", "y");
        assertEquals("No a or y in the graph!", result);
    }

    @Test
    public void testNoBridgeWords() {
        String result = graph.queryBridgeWords("a", "c");
        assertEquals("No bridge words from a to c!", result);
    }

    @Test
    public void testOneBridgeWord() {
        String result = graph.queryBridgeWords("a", "c");
        assertTrue(result.contains("The bridge words from a to c are: b"));
    }

    @Test
    public void testMultipleBridgeWords() {
        graph.addEdge("a", "f");
        graph.addEdge("f", "c");
        String result = graph.queryBridgeWords("a", "c");
        assertTrue(result.contains("The bridge words from a to c are: b, d, e"));
    }
}

class Case2 {
    Map<String, Map<String, Integer>> adjList = new HashMap<>();

    void addEdge(String from, String to) {
        adjList.putIfAbsent(from, new HashMap<>());
        adjList.get(from).put(to, 1); // 假设所有边的权重为1
    }

    public String queryBridgeWords(String word1, String word2) {
        if (!adjList.containsKey(word1) || !adjList.containsKey(word2)) {
            return "No " + word1 + " or " + word2 + " in the graph!";
        }
        Set<String> bridgeWords = adjList.get(word1).keySet().stream()
            .filter(w -> adjList.get(w) != null && adjList.get(w).containsKey(word2))
            .collect(Collectors.toSet());
        if (bridgeWords.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        }
        return "The bridge words from " + word1 + " to " + word2 + " are: " + String.join(", ", bridgeWords) + ".";
    }
}
