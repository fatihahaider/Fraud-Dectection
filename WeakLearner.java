import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class WeakLearner {
    private int dp; // dimension predictor
    private int sp; // sign predictor
    private int vp; // value predictor
    private int k;

    // creates new object
    private class RowObject {
        int[] row; // each row in input
        double weight; // associated weight
        int label; // associate false or clean label

        // initialize values
        private RowObject(int[] row, double weight, int label) {
            this.row = row;
            this.weight = weight;
            this.label = label;
        }
    }

    // train the weak learner
    // for each dp vp sp call predict and check if that prediciton is equal
    // to what is said in the label array

    // constructs weak learner object
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null) {
            throw new IllegalArgumentException("invalid array arguments");
        }
        if (input.length != labels.length || input.length != weights.length) {
            throw new IllegalArgumentException("invalid array length");
        }
        for (double x : weights) {
            if (x < 0) {
                throw new IllegalArgumentException("non negative weights");
            }
        }
        for (double y : labels) {
            if (y <= -1 || y >= 2) {
                throw new IllegalArgumentException("labels arent all 0 or 1's");
            }
        }

        int n = input.length;
        k = input[0].length;

        // 1 vp = 1  dp = 0  sp = 1 x
        // 2 vp = 2, dp = 1, sp = 1
        // 3 vp = 5, dp = 1, sp = 1
        // 4 vp = 1, dp = 1, sp = 1
        // 5 vp = 1, dp = 1, sp = 0 x

        RowObject[] ARR = new RowObject[n];
        double maxWeight = Integer.MIN_VALUE;

        for (int s = 0; s <= 1; s++) {
            for (int dim = 0; dim < k; dim++) {
                final int val = dim;
                for (int point = 0; point < n; point++) {
                    ARR[point] = new RowObject(input[point], weights[point],
                                               labels[point]);
                }

                Arrays.sort(ARR, new Comparator<RowObject>() {
                    public int compare(RowObject o1, RowObject o2) {
                        return Integer.compare(o1.row[val], o2.row[val]);
                    }
                });

                double totalCorrectWeight0 = 0;
                // initializing the correct weight before starting
                for (int i = 0; i < n; i++) {
                    int predict = 1 - s;
                    if (predict == ARR[i].label) {
                        totalCorrectWeight0 = totalCorrectWeight0
                                + ARR[i].weight;
                    }
                }

                for (int i = 0; i < n; i++) {
                    int predict = predict(ARR[i].row, dim, ARR[i].row[dim],
                                          s);  // can change it to 0

                    if (predict == ARR[i].label) {
                        totalCorrectWeight0 += ARR[i].weight;
                    }
                    else {
                        totalCorrectWeight0 -= ARR[i].weight;
                    }

                    if (i != n - 1 && ARR[i].row[dim] == ARR[i + 1].row[dim]) {
                        continue;
                    }

                    if (totalCorrectWeight0 > maxWeight) {
                        maxWeight = totalCorrectWeight0;
                        dp = dim;
                        vp = ARR[i].row[dim];
                        sp = s;
                    }
                }
            }


        }
    }

    // helper method to predict label
    private int predict(int[] sample, int d, int v, int s) {
        if (s == 0) {
            if (sample[d] <= v) return 0;
            else return 1;
        }
        else {
            if (sample[d] <= v) return 1;
            else return 0;
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) {
            throw new IllegalArgumentException("null array");
        }
        if (sample.length != k) {
            throw new IllegalArgumentException("invalid array length");
        }
        if (sp == 0) {
            if (sample[dp] <= vp) return 0;
            else return 1;
        }
        else {
            if (sample[dp] <= vp) return 1;
            else return 0;
        }
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return dp;
    }

    // return the value the learner uses to separate the data,
    public int valuePredictor() {
        return vp;
    }

    // return the sign the learner uses to separate the data.
    public int signPredictor() {
        return sp;
    }

    // unit testing (required)
    public static void main(String[] args) {
        In datafile = new In(args[0]);

        int n = datafile.readInt();
        int k = datafile.readInt();

        int[][] input = new int[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                input[i][j] = datafile.readInt();
            }
        }

        int[] labels = new int[n];
        for (int i = 0; i < n; i++) {
            labels[i] = datafile.readInt();
        }

        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = datafile.readDouble();
        }


        WeakLearner weakLearner = new WeakLearner(input, weights, labels);

        for (int i = 0; i < input.length; i++) {
            System.out.print(weakLearner.predict(input[i]));
        }
        System.out.println();
        StdOut.printf("vp = %d, dp = %d, sp = %d\n",
                      weakLearner.valuePredictor(),
                      weakLearner.dimensionPredictor(),
                      weakLearner.signPredictor());
    }
}

