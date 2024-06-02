import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;

import java.util.Arrays;

public class Clustering {
    private Point2D[] locations; // array of all locations
    private CC connected; // connected components in mst

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        if (locations == null || k < 1 || k > locations.length) {
            throw new IllegalArgumentException("invalid arguments");
        }
        this.locations = Arrays.copyOf(locations, locations.length);
        // number of clusters
        EdgeWeightedGraph graph = new EdgeWeightedGraph(locations.length);

        for (int j = 0; j < graph.V(); j++) {
            for (int i = 0; i < graph.V(); i++) {
                Point2D first = locations[j];
                Point2D second = locations[i];
                graph.addEdge(new Edge(i, j, first.distanceSquaredTo(second)));
            }
        }

        KruskalMST mst = new KruskalMST(graph);

        Edge[] edgeArr = new Edge[locations.length - 1];
        int i = 0;
        for (Edge x : mst.edges()) {
            edgeArr[i] = x;
            i++;
        }
        Arrays.sort(edgeArr);

        EdgeWeightedGraph alteredGraph = new EdgeWeightedGraph(
                locations.length); // check length of m
        for (int j = 0; j < locations.length - k; j++) {
            alteredGraph.addEdge(edgeArr[j]);
        }
        connected = new CC(alteredGraph);
    }

    // return the cluster of the ith point
    public int clusterOf(int i) {
        if (i < 0 || i > locations.length - 1) {
            throw new IllegalArgumentException("invalid argument");
        }
        return connected.id(i);
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        if (input == null || input.length != locations.length) {
            throw new IllegalArgumentException("invalid dimension array");
        }

        int count = connected.count();
        int[] reducedDim = new int[count];

        for (int i = 0; i < locations.length; i++) {
            int index = clusterOf(i);
            reducedDim[index] += input[i];
        }
        return reducedDim;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Point2D[] locations = {
                new Point2D(0, 0),
                new Point2D(1, 1),
                new Point2D(2, 2),
                new Point2D(3, 3),
                new Point2D(4, 4)
        };

        int k = 2;

        // Create a Clustering instance
        Clustering clustering = new Clustering(locations, k);

        // Test clusterOf method
        for (int i = 0; i < locations.length; i++) {
            int cluster = clustering.clusterOf(i);
            System.out.println("Location " + i + " belongs to cluster " + cluster);
        }

        // Create sample input array
        int[] input = { 0, 1, 2, 3, 4 };

        // Reduce dimensions
        int[] reducedDimensions = clustering.reduceDimensions(input);

        // Print reduced dimensions
        System.out.println("Reduced dimensions:");
        for (int i = 0; i < reducedDimensions.length; i++) {
            System.out.println("Cluster " + i + ": " + reducedDimensions[i]);
        }
    }
}
