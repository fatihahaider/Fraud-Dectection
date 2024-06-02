import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;

public class BoostingAlgorithm {
    private double[] weights; // array of weights
    private Clustering clusters; // clustering object
    private int[][] reducedInput; // reduced dimension array
    private int[] labelS; // array of labels
    private int m; // number of locations
    // arraylist of weaklearner objects;
    private ArrayList<WeakLearner> weaklearners = new ArrayList<WeakLearner>();

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations,
                             int k) {
        if (input == null || labels == null || locations == null || k < 1 ||
                k > locations.length) {
            throw new IllegalArgumentException("invalid arguments");
        }
        for (int i = 0; i < locations.length; i++) {
            if (locations[i] == null) {
                throw new IllegalArgumentException("null location");
            }
        }
        if (input.length != labels.length) {
            throw new IllegalArgumentException("invalid array length");
        }

        clusters = new Clustering(locations, k);
        reducedInput = new int[input.length][k];
        labelS = Arrays.copyOf(labels, labels.length);
        m = locations.length;

        for (int i = 0; i < input.length; i++) {
            reducedInput[i] = clusters.reduceDimensions(input[i]);
        }

        weights = new double[input.length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (1 * 1.0) / weights.length;
        }
    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        return weights[i];
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        WeakLearner model = new WeakLearner(reducedInput, weights, labelS);
        weaklearners.add(model);
        for (int i = 0; i < reducedInput.length; i++) {
            if (model.predict(reducedInput[i]) != labelS[i]) {
                weights[i] *= 2;
            }
        }

        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
        }

        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i] / sum;
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) {
            throw new IllegalArgumentException("null array");
        }
        if (sample.length != m) {
            throw new IllegalArgumentException("invalid array length");
        }

        int[] reduced = clusters.reduceDimensions(sample);
        int total = 0;
        for (WeakLearner x : weaklearners) {
            total += x.predict(reduced);
        }

        if (total * 2 <= weaklearners.size()) {
            return 0;
        }
        else {
            return 1;
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int test = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(trainingInput,
                                                        trainingLabels,
                                                        trainingLocations, k);
        Stopwatch time = new Stopwatch();
        for (int t = 0; t < test; t++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.getN();


        // calculate the test data set accuracy
        double testAccuracy = 0;
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                testAccuracy += 1;
        testAccuracy /= testing.getN();

        System.out.println("elapsed time:" + time.elapsedTime());

        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model: " + testAccuracy);
        StdOut.println("weight of index 2" + model.weightOf(2));

    }
}

