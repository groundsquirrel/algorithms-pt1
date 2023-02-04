import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double[] threshold;
    private int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException();
        }

        this.trials = trials;
        threshold = new double[trials];

        for (int i = 0; i < threshold.length; i++) {
            threshold[i] = calcTreshold(n);
        }
    }

    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(
                Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() +
                                   ", " + stats.confidenceHi() + "]");
    }

    private double calcTreshold(int n) {
        double counter = 0;
        int i, j;
        Percolation perc = new Percolation(n);
        while (!perc.percolates()) {
            i = StdRandom.uniformInt(n) + 1;
            j = StdRandom.uniformInt(n) + 1;
            if (!perc.isOpen(i, j)) {
                counter++;
                perc.open(i, j);
            }
        }
        return counter / (n * n);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }
}
