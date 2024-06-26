package main.java.com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Main class for the lab.
 */
public class Lab1 {
  /**
   * Main class for the lab.
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("请输入文本文件路径：");
    String filePath = scanner.nextLine();
    try (FileWriter writer = new FileWriter("random_walk_result.txt", false)) {
      // Empty the output file
    } catch (IOException e) {
      System.out.println("清空输出文件出错" + e.getMessage());
      // return;
    }

    MyGraph graph = new MyGraph();
    try {
      graph.readFromFile(filePath);
    } catch (IOException e) {
      System.out.println("读取文件时出错：" + e.getMessage());
    }

    while (true) {
      System.out.println("请选择功能：");
      System.out.println("1. 展示有向图");
      System.out.println("2. 查询桥接词");
      System.out.println("3. 根据桥接词生成新文本");
      System.out.println("4. 计算两个单词之间的最短路径");
      System.out.println("5. 随机游走");
      System.out.println("0. 退出");

      int choice = scanner.nextInt();
      scanner.nextLine(); // Consume newline

      switch (choice) {
        case 1:
          graph.showDirectedGraph();
          break;
        case 2:
          System.out.println("请输入两个单词：");
          String word1 = scanner.next();
          String word2 = scanner.next();
          System.out.println(graph.queryBridgeWords(word1, word2));
          break;
        case 3:
          System.out.println("请输入新文本：");
          String newText = scanner.nextLine();
          System.out.println(graph.generateNewText(newText));
          break;
        case 4:
          System.out.println("请输入一个或两个单词：");
          word1 = scanner.next();
          String input = scanner.nextLine().trim();
          if (input.isEmpty()) {
            System.out.println(graph.calcShortestPath(word1));
          } else {
            word2 = input;
            System.out.println(graph.calcShortestPath(word1, word2));
          }
          break;
        case 5:
          System.out.println("按任意键停止遍历...");
          graph.randomWalk(scanner);
          break;
        case 0:
          return;
        default:
          System.out.println("无效选择，请重新输入。");
          break;
      }
    }
  }
}

/**
 * Graph class for representing and manipulating a directed graph.
 */
class MyGraph {

  private Map<String, Map<String, Integer>> adjList = new HashMap<>();

  /**
   * Reads a graph from a file.
   *
   * @param filePath the path to the file
   * @throws IOException if an I/O error occurs
   */
  public void readFromFile(String filePath) throws IOException {
    StringBuilder mergedContent = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        mergedContent.append(line).append(" ");
      }
    }
    processLine(mergedContent.toString());
  }

  /**
   * Processes a line of text to build the adjacency list.
   *
   * @param line the line of text
   */
  private void processLine(String line) {
    String[] words = line.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
    for (int i = 0; i < words.length - 1; i++) {
      String word1 = words[i];
      String word2 = words[i + 1];
      adjList.computeIfAbsent(word1, k -> new HashMap<>()).merge(word2, 1, Integer::sum);
    }
  }

  /**
   * Displays the directed graph.
   */
  public void showDirectedGraph() {
    for (Map.Entry<String, Map<String, Integer>> entry : adjList.entrySet()) {
      String from = entry.getKey();
      for (Map.Entry<String, Integer> edge : entry.getValue().entrySet()) {
        String to = edge.getKey();
        int weight = edge.getValue();
        System.out.println(from + " -> " + to + " [weight=" + weight + "]");
      }
    }
  }

  /**
   * Queries bridge words between two given words.
   *
   * @param word1 the first word
   * @param word2 the second word
   * @return a string describing the bridge words
   */
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

  /**
   * Generates new text based on the input text using bridge words.
   *
   * @param inputText the input text
   * @return the generated text
   */
  public String generateNewText(String inputText) {
    String[] words = inputText.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
    if (words.length < 2) {
      return inputText;
    }

    StringBuilder result = new StringBuilder();
    for (int j = 0; j < words.length - 1; j++) {
      final int i = j;
      result.append(words[i]).append(" ");
      Set<String> bridgeWords = adjList.getOrDefault(words[i], Collections.emptyMap()).keySet().stream()
          .filter(w -> adjList.get(w) != null && adjList.get(w).containsKey(words[i + 1]))
          .collect(Collectors.toSet());
      if (!bridgeWords.isEmpty()) {
        String bridgeWord = bridgeWords.iterator().next(); // Randomly choose one bridge word
        result.append(bridgeWord).append(" ");
      }
    }
    result.append(words[words.length - 1]);
    return result.toString().trim();
  }

  /**
   * Calculates the shortest path between two words.
   *
   * @param word1 the first word
   * @param word2 the second word
   * @return a string describing the shortest path
   */
  public String calcShortestPath(String word1, String word2) {
    if (!adjList.containsKey(word1) || !adjList.containsKey(word2)) {
      return "No " + word1 + " or " + word2 + " in the graph!";
    }
    Map<String, Integer> distances = new HashMap<>();
    Map<String, String> previous = new HashMap<>();
    PriorityQueue<Map.Entry<String, Integer>> queue = new PriorityQueue<>(Map.Entry.comparingByValue());
    distances.put(word1, 0);
    queue.add(new AbstractMap.SimpleEntry<>(word1, 0));

    while (!queue.isEmpty()) {
      String current = queue.poll().getKey();
      if (current.equals(word2)) {
        break;
      }
      if (!adjList.containsKey(current)) {
        continue;
      }
      for (Map.Entry<String, Integer> neighbor : adjList.get(current).entrySet()) {
        String neighborWord = neighbor.getKey();
        int weight = neighbor.getValue();
        int newDist = distances.get(current) + weight;
        if (newDist < distances.getOrDefault(neighborWord, Integer.MAX_VALUE)) {
          distances.put(neighborWord, newDist);
          previous.put(neighborWord, current);
          queue.add(new AbstractMap.SimpleEntry<>(neighborWord, newDist));
        }
      }
    }

    if (!distances.containsKey(word2)) {
      return "No path from " + word1 + " to " + word2 + "!";
    }

    List<String> path = new ArrayList<>();
    for (String at = word2; at != null; at = previous.get(at)) {
      path.add(at);
    }
    Collections.reverse(path);
    return "Shortest path from " + word1 + " to " + word2 + ": " + String.join(" -> ", path) + " (weight="
        + distances.get(word2) + ")";
  }

  /**
   * Calculates the shortest path from a given word to all other words.
   *
   * @param word the word
   * @return a string describing the shortest paths
   */
  public String calcShortestPath(String word) {
    if (!adjList.containsKey(word)) {
      return "No " + word + " in the graph!";
    }

    Map<String, Integer> distances = new HashMap<>();
    Map<String, String> previous = new HashMap<>();
    PriorityQueue<Map.Entry<String, Integer>> queue = new PriorityQueue<>(Map.Entry.comparingByValue());
    distances.put(word, 0);
    queue.add(new AbstractMap.SimpleEntry<>(word, 0));

    while (!queue.isEmpty()) {
      String current = queue.poll().getKey();
      if (!adjList.containsKey(current)) {
        continue;
      }
      for (Map.Entry<String, Integer> neighbor : adjList.get(current).entrySet()) {
        String neighborWord = neighbor.getKey();
        int weight = neighbor.getValue();
        int newDist = distances.get(current) + weight;
        if (newDist < distances.getOrDefault(neighborWord, Integer.MAX_VALUE)) {
          distances.put(neighborWord, newDist);
          previous.put(neighborWord, current);
          queue.add(new AbstractMap.SimpleEntry<>(neighborWord, newDist));
        }
      }
    }

    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, Integer> entry : distances.entrySet()) {
      String target = entry.getKey();
      int distance = entry.getValue();
      if (!target.equals(word)) {
        List<String> path = new ArrayList<>();
        for (String at = target; at != null; at = previous.get(at)) {
          path.add(at);
        }
        Collections.reverse(path);
        result.append("Shortest path from ").append(word).append(" to ").append(target).append(": ")
            .append(String.join(" -> ", path)).append(" (weight=").append(distance).append(")\n");
      }
    }
    return result.toString().trim();
  }

  /**
   * Performs a random walk on the graph.
   *
   * @param scanner the scanner to detect user input
   */
  public void randomWalk(Scanner scanner) {
    if (adjList.isEmpty()) {
      return;
    }

    List<String> nodes = new ArrayList<>(adjList.keySet());
    String current = nodes.get(new Random().nextInt(nodes.size()));
    StringBuilder walk = new StringBuilder(current);

    Set<String> visitedEdges = new HashSet<>();
    StringBuilder walkLog = new StringBuilder(current);

    while (adjList.containsKey(current) && !adjList.get(current).isEmpty()) {
      System.out.println("Current Node: " + current);
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        System.out.println("Random walk interrupted.");
        break;
      }
      try {
        if (System.in.available() > 0) {
          scanner.nextLine();
          break;
        }
      } catch (IOException e) {
        System.out.println("Error checking input: " + e.getMessage());
        break;
      }

      List<Map.Entry<String, Integer>> edges = new ArrayList<>(adjList.get(current).entrySet());
      Map.Entry<String, Integer> edge = edges.get(new Random().nextInt(edges.size()));
      String edgeRepresentation = current + " -> " + edge.getKey();

      if (visitedEdges.contains(edgeRepresentation)) {
        break;
      }
      visitedEdges.add(edgeRepresentation);
      current = edge.getKey();
      walk.append(" -> ").append(current);
      walkLog.append(" -> ").append(current);
    }

    System.out.println("Random Walk Result: " + walk.toString());

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("random_walk_result.txt", true))) {
      writer.newLine();
      writer.write(walkLog.toString());
    } catch (IOException e) {
      System.out.println("Error writing to file: " + e.getMessage());
    }
  }
}
