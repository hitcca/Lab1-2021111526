import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WhiteTest {
  private MyGraph graph;

  @BeforeEach
  void setUp() {
    graph = new MyGraph();
    // 初始化图结构，确保覆盖所有条件
    graph.adjList.put("apple", new HashMap<>());
    graph.adjList.get("apple").put("banana", 1);
    graph.adjList.put("banana", new HashMap<>());
    graph.adjList.get("banana").put("date", 1);
    graph.adjList.put("date", new HashMap<>());
  }

  // 用例1: word1不在图中
  @Test
  void testCalcShortestPath_Word1NotInGraph() {
    String result = graph.calcShortestPath("orange", "date");
    assertEquals("No orange or date in the graph!", result);
  }

  // 用例2: word2不在图中
  @Test
  void testCalcShortestPath_Word2NotInGraph() {
    String result = graph.calcShortestPath("apple", "orange");
    assertEquals("No apple or orange in the graph!", result);
  }

  // 用例3: word1和word2相同
  @Test
  void testCalcShortestPath_SameWord() {
    String result = graph.calcShortestPath("apple", "apple");
    assertEquals("Shortest path from apple to apple: apple (weight=0)", result);
  }

  // 用例4: word1和word2之间有直接路径
  @Test
  void testCalcShortestPath_DirectPath() {
    String result = graph.calcShortestPath("apple", "banana");
    assertEquals("Shortest path from apple to banana: apple -> banana (weight=1)", result);
  }

  // 用例5: word1和word2之间有间接路径
  @Test
  void testCalcShortestPath_IndirectPath() {
    String result = graph.calcShortestPath("apple", "date");
    assertEquals("Shortest path from apple to date: apple -> banana -> date (weight=2)", result);
  }

  // 用例6: word1和word2之间没有路径
  @Test
  void testCalcShortestPath_NoPath() {
    graph.adjList.put("kiwi", new HashMap<>());
    String result = graph.calcShortestPath("apple", "kiwi");
    assertEquals("No path from apple to kiwi!", result);
  }

  // 用例7: word1有多个邻居，检查路径选择
  @Test
  void testCalcShortestPath_MultipleNeighbors() {
    graph.adjList.get("apple").put("cherry", 2); // 增加一个分支条件
    graph.adjList.put("cherry", new HashMap<>());
    graph.adjList.get("cherry").put("date", 1); // 增加一个分支条件
    String result = graph.calcShortestPath("apple", "date");
    assertEquals("Shortest path from apple to date: apple -> banana -> date (weight=2)", result);
  }

  // 用例8: 图中包含环路
  @Test
  void testCalcShortestPath_WithCycle() {
    // 增加一个环路
    graph.adjList.get("date").put("apple", 1);
    String result = graph.calcShortestPath("apple", "date");
    assertEquals("Shortest path from apple to date: apple -> banana -> date (weight=2)", result);
  }

  // 用例9: 图中存在负权重的边
  @Test
  void testCalcShortestPath_NegativeWeights() {
    graph.adjList.get("apple").put("banana", -1); // 增加负权重边
    String result = graph.calcShortestPath("apple", "date");
    assertEquals("Shortest path from apple to date: apple -> banana -> date (weight=0)", result);
  }

  // 用例10: word1和word2之间有多条路径
  @Test
  void testCalcShortestPath_MultiplePaths() {
    graph.adjList.get("apple").put("date", 3); // 增加直接路径但权重更大
    String result = graph.calcShortestPath("apple", "date");
    assertEquals("Shortest path from apple to date: apple -> banana -> date (weight=2)", result);
  }

  // 用例11: 图为空时
  @Test
  void testCalcShortestPath_EmptyGraph() {
    graph.adjList.clear();
    String result = graph.calcShortestPath("apple", "date");
    assertEquals("No apple or date in the graph!", result);
  }
}
