import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] grid;
    private int n;

    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufPerc;
    private int top;
    private int bottom;
    private int openCount;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n <= 0");

        grid = new boolean[n * n];

        this.n = n;

        uf = new WeightedQuickUnionUF(n * n + 2);
        ufPerc = new WeightedQuickUnionUF(n * n + 2);
        top = n * n;
        bottom = n * n + 1;
    }

    private int getIndex(int row, int col) {
        return n * (row - 1) + col - 1;
    }

    public void open(int row, int col) {
        checkBounds(row, col);
        if (isOpen(row, col))
            return;

        int siteId = getIndex(row, col);
        grid[siteId] = true;
        openCount++;

        // union with top virtual cell
        if (row == 1 && uf.find(siteId) != uf.find(top)) {
            uf.union(siteId, top);
            ufPerc.union(siteId, top);
        }

        // union with bottom virtual cell
        if (row == n) {
            ufPerc.union(siteId, bottom);
        }

        // union with upper cell
        if (row > 1) {
            union(siteId, row - 1, col);
        }

        // union with bottom cell
        if (row < n) {
            union(siteId, row + 1, col);
        }

        // union with left cell
        if (col > 1) {
            union(siteId, row, col - 1);
        }

        // union with right cell
        if (col < n) {
            union(siteId, row, col + 1);
        }
    }

    private void union(int siteId, int row, int col) {
        if (isOpen(row, col)) {
            int newId = getIndex(row, col);
            uf.union(siteId, newId);
            ufPerc.union(siteId, newId);
        }
    }

    public boolean isOpen(int row, int col) {
        checkBounds(row, col);

        return grid[getIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkBounds(row, col);
        if (!isOpen(row, col))
            return false;

        int siteId = getIndex(row, col);
        return uf.find(top) == uf.find(siteId);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return ufPerc.find(top) == ufPerc.find(bottom);
    }

    public static void main(String[] args) {

    }

    private void checkBounds(int i, int j) {
        if (i < 1 || i > n || j < 1 || j > n) {
            throw new IllegalArgumentException();
        }
    }
}
