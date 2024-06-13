import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class YourGraphTest {
  private MyGraph graph;

  @BeforeEach
  void setUp() {
    graph = new MyGraph();
    // 初始化图结构，确保覆盖所有条件
    graph.adjList.put("apple", new HashMap<>());
    graph.adjList.get("apple").put("banana", 1);
    graph.adjList.get("apple").put("cherry", 1); // 增加一个分支条件
    graph.adjList.put("banana", new HashMap<>());
    graph.adjList.put("date", new HashMap<>());
    graph.adjList.get("banana").put("date", 1);
    graph.adjList.put("cherry", new HashMap<>());
    graph.adjList.get("cherry").put("date", 1); // 增加一个分支条件
  }

  // 语句覆盖测试
  @Test
  void testQueryBridgeWords_FullGraph() {
    String result = graph.queryBridgeWords("apple", "date");
    assertEquals("The bridge words from apple to date are: banana, cherry.", result);
  }

  // 分支覆盖测试 - 检查word1或word2不在图中的情况
  @Test
  void testQueryBridgeWords_OneWordMissing() {
    String resultWord1Missing = graph.queryBridgeWords("apple", "orange");
    assertEquals("No apple or orange in the graph!", resultWord1Missing);

    String resultWord2Missing = graph.queryBridgeWords("apple", "date");
    // 假设"date"被移除，以测试word2不在图中的情况
    graph.adjList.remove("date");
    assertEquals("The bridge words from apple to date are: banana, cherry.", resultWord2Missing);
  }

  // 条件覆盖测试 - 检查桥接词存在与否的情况
  @Test
  void testQueryBridgeWords_BridgeWordsExistence() {
    String resultWithBridgeWords = graph.queryBridgeWords("apple", "date");
    assertEquals("The bridge words from apple to date are: banana, cherry.", resultWithBridgeWords);

    // 移除所有桥接词，以测试没有桥接词的情况
    graph.adjList.get("apple").remove("banana");
    graph.adjList.get("apple").remove("cherry");
    String resultWithoutBridgeWords = graph.queryBridgeWords("apple", "date");
    assertEquals("No bridge words from apple to date!", resultWithoutBridgeWords);
  }

  // 测试所有可能的边界条件和异常流
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
}