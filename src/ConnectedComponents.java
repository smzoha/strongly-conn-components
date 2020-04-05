import java.util.*;

/**
 * @author Shamah M Zoha
 * @since 4/1/2020
 **/
public class ConnectedComponents {

    public static void main(String[] args) {
        Scanner feed = new Scanner(System.in);

        System.out.print("Enter number of vertices: ");
        int verticesNum = feed.nextInt();

        System.out.print("Enter number of edges: ");
        int edgeNum = feed.nextInt();

        Map<Integer, Set<Integer>> adjacencyMap = new LinkedHashMap<>();
        initAdjacencyMap(adjacencyMap, verticesNum);

        for (int index = 0; index < edgeNum; index++) {
            System.out.print("Enter source and destination of edges (" + (index + 1) + " of " + edgeNum + "): ");

            int source = feed.nextInt();
            int destination = feed.nextInt();

            adjacencyMap.get(source).add(destination);
        }

        printAdjacencyMatrix(adjacencyMap, "Resultant Adjacency Matrix");

        Map<Integer, Integer> componentsInfo = findStronglyConnectedComponents(adjacencyMap);

        printConnectedComponentInfo(componentsInfo);
    }

    private static Map<Integer, Integer> findStronglyConnectedComponents(Map<Integer, Set<Integer>> adjacencyMap) {
        List<Integer> visitedList = new ArrayList<>();
        List<Integer> postTraversalOrderList = getPostTraversalOrderList(adjacencyMap, visitedList);

        Map<Integer, Set<Integer>> transposeMap = transposeMap(adjacencyMap);
        printAdjacencyMatrix(transposeMap, "Transposed Adjacency Matrix");

        List<Integer> connectedVertices = new ArrayList<>();
        visitedList.clear();

        Map<Integer, Integer> verticesComponentMap = new TreeMap<>();
        int componentCount = 1;

        while (!postTraversalOrderList.isEmpty()) {
            Integer source = postTraversalOrderList.remove(0);

            DFS(source, transposeMap, visitedList, connectedVertices);
            postTraversalOrderList.removeAll(visitedList);

            for (Integer vertex : connectedVertices) {
                verticesComponentMap.put(vertex, componentCount);
            }

            componentCount++;

            connectedVertices.clear();
        }

        return verticesComponentMap;
    }

    private static List<Integer> getPostTraversalOrderList(Map<Integer, Set<Integer>> adjacencyMap, List<Integer> visitedList) {
        List<Integer> postTraversalOrderList = new LinkedList<>();

        while (!visitedList.containsAll(adjacencyMap.keySet())) {
            Integer source = adjacencyMap.keySet().stream()
                    .filter(vertex -> !visitedList.contains(vertex))
                    .findFirst().orElse(null);

            if (Objects.nonNull(source)) {
                DFS(source, adjacencyMap, visitedList, postTraversalOrderList);
            }
        }

        Collections.reverse(postTraversalOrderList);

        return postTraversalOrderList;
    }

    private static List<Integer> DFS(Integer source, Map<Integer, Set<Integer>> adjacencyMap,
                                     List<Integer> visitedVertices, List<Integer> postOrderList) {

        visitedVertices.add(source);

        for (Integer vertex : adjacencyMap.get(source)) {
            if (!visitedVertices.contains(vertex)) {
                postOrderList = DFS(vertex, adjacencyMap, visitedVertices, postOrderList);
            }
        }

        postOrderList.add(source);

        return postOrderList;
    }

    private static Map<Integer, Set<Integer>> transposeMap(Map<Integer, Set<Integer>> adjacencyMap) {
        Map<Integer, Set<Integer>> transposedMap = new LinkedHashMap<>();
        initAdjacencyMap(transposedMap, adjacencyMap.size());

        for (Integer sourceVertex : adjacencyMap.keySet()) {
            adjacencyMap.get(sourceVertex).forEach(vertex -> transposedMap.get(vertex).add(sourceVertex));
        }

        return transposedMap;
    }

    private static void initAdjacencyMap(Map<Integer, Set<Integer>> adjacencyMap, int verticesNum) {
        for (int index = 0; index < verticesNum; index++) {
            adjacencyMap.put(index + 1, new LinkedHashSet<>());
        }
    }

    private static void printAdjacencyMatrix(Map<Integer, Set<Integer>> adjacencyMatrix, String title) {
        System.out.println("===================\n" + title);

        for (Integer source : adjacencyMatrix.keySet()) {
            System.out.println(source + " -> " + adjacencyMatrix.get(source));
        }
    }

    private static void printConnectedComponentInfo(Map<Integer, Integer> componentsInfo) {
        System.out.println("===================\nVertices-Component Pairs (Legend: Vertex -> Pair)");

        for (Integer vertex : componentsInfo.keySet()) {
            System.out.println(vertex + " -> " + componentsInfo.get(vertex));
        }
    }
}
