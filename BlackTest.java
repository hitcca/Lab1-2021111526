  import java.io.IOException;
  import java.util.HashMap;
  import org.junit.jupiter.api.BeforeEach;
  import org.junit.jupiter.api.Test;
  import static org.junit.jupiter.api.Assertions.*;

  class BlackTest {
    private MyGraph graph;

    @BeforeEach
    void setUp() {
      graph = new MyGraph();
      // 初始化图结构，例如：
      graph.adjList.put("apple", new HashMap<>());
      graph.adjList.get("apple").put("banana", 1);
      graph.adjList.put("banana", new HashMap<>());
      graph.adjList.get("banana").put("cherry", 1);
      graph.adjList.get("banana").put("date", 1);
      graph.adjList.put("cherry", new HashMap<>());
      graph.adjList.put("date", new HashMap<>());
      graph.adjList.put("orange", new HashMap<>());
      graph.adjList.get("apple").put("orange", 1);
      graph.adjList.get("orange").put("cherry", 1);
    }

    @Test
    void 给git testQueryBridgeWords_WithBridgeWords() {
      String result = graph.queryBridgeWords("apple", "date");
      assertEquals("The bridge words from apple to date are: banana.", result);
    }

    @Test
    void testQueryBridgeWords_NoBridgeWords() {
      String result = graph.queryBridgeWords("banana", "date");
      assertEquals("No bridge words from banana to date!", result);
    }

    @Test
    void testQueryBridgeWords_OneWordNotInGraph() {
      String result = graph.queryBridgeWords("apple", "cherry");
      assertEquals("The bridge words from apple to cherry are: banana, orange.", result);
    }

    @Test
    void testQueryBridgeWords_BothWordsNotInGraph() {
      String result = graph.queryBridgeWords("kiwi", "mango");
      assertEquals("No kiwi or mango in the graph!", result);
    }

    @Test
    void testQueryBridgeWords_SameWord() {
      // 测试word1和word2相同的情况
      String sameWord = graph.queryBridgeWords("apple", "apple");
      assertEquals("No bridge words from apple to apple!", sameWord);
    }

    @Test
    void testQueryBridgeWords_LeftNull() {
      // 测试word1和word2为null的情况（如果方法允许null输入）
      String resultWord1Null = graph.queryBridgeWords(null, "date");
      assertEquals("No null or date in the graph!", resultWord1Null);
    }

    @Test
    void testQueryBridgeWords_RightNull() {
      String resultWord2Null = graph.queryBridgeWords("apple", null);
      assertEquals("No apple or null in the graph!", resultWord2Null);
    }
    @Test
    void testQueryBridgeWords_AllNull() {
      String resultWord2Null = graph.queryBridgeWords(null, null);
      assertEquals("No null or null in the graph!", resultWord2Null);
    }
  }