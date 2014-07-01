/*----------------------------------------------------------------
 *  Author:        Andy James
 *  Written:       6/24/2014
 *  Last updated:  6/25/2014
 *
 *  Compilation:   javac-algs4 Percolation.java
 *  Execution:     java-algs4 Percolation N
 *  
 *  Define a percolation system.
 *
 *----------------------------------------------------------------*/

public class Percolation {
    
    private int size; // Size (open grid edge) of system
    private boolean[][] open; // Grid tracking open site
    private WeightedQuickUnionUF uf1; // UF object to monitor percolation
    private WeightedQuickUnionUF uf2; // UF object to monitor full status
    
    /*----------------------------------------------------------------
     *  create N-by-N grid, with all sites blocked
     *----------------------------------------------------------------*/
    public Percolation(int N)
    {
        if (N <= 0) 
            throw new 
            IllegalArgumentException("N argument should be positive");
        size = N;
        open = new boolean[size][size];
        // 1st UF object has a sites + 2 virtual nodes
        uf1 = new WeightedQuickUnionUF(size * size + 2); 
        // 2nd UF object has a sites + 1 virtual node (one unused)
        uf2 = new WeightedQuickUnionUF(size * size + 2);
    }
    
    /*----------------------------------------------------------------
     *  open site (row i, column j) if it is not already
     *----------------------------------------------------------------*/
    public void open(int i, int j)
    {
        validateIndices(i, j);
        int index;
        open[i-1][j-1] = true;
        index = xyTo1D(i, j);
        if (i == 1)
        {
            uf1.union(0, index);
            uf2.union(0, index);
        }
        if (i == size)
        {
            uf1.union(1, index);
        }
        if (i > 1)
        {
            if (isOpen(i-1, j))
            {
                uf1.union(xyTo1D(i-1, j), index);
                uf2.union(xyTo1D(i-1, j), index);
            }
        }
        if (i < size)
        {
            if (isOpen(i+1, j))
            {
                uf1.union(xyTo1D(i+1, j), index);
                uf2.union(xyTo1D(i+1, j), index);
            }
        }
        if (j > 1)
        {
            if (isOpen(i, j-1))
            {
                uf1.union(xyTo1D(i, j-1), index);
                uf2.union(xyTo1D(i, j-1), index);
            }
        }
        if (j < size)
        {
            if (isOpen(i, j+1))
            {
                uf1.union(xyTo1D(i, j+1), index);
                uf2.union(xyTo1D(i, j+1), index);
            }
        }
    }
    
    /*----------------------------------------------------------------
     *  is site (row i, column j) open?
     *----------------------------------------------------------------*/
    public boolean isOpen(int i, int j)
    {
        validateIndices(i, j);
        return open[i-1][j-1];
    }
    
    /*----------------------------------------------------------------
     *  is site (row i, column j) full?
     *----------------------------------------------------------------*/
    public boolean isFull(int i, int j)
    {
        validateIndices(i, j);
        int index = xyTo1D(i, j);
        if (isOpen(i, j) && (uf2.connected(index, 0)))
        {
            return true;
        }
        return false;
    }
    
    /*----------------------------------------------------------------
     *  does the system percolate?
     *----------------------------------------------------------------*/
    public boolean percolates()
    {
        if (uf1.connected(0, 1))
        {
            return true;
        }
        return false;
    }
    
    /*----------------------------------------------------------------
     *  helper function which maps 2-dimensional (row, column) pair 
     *  to a 1-dimensional union find object index
     *----------------------------------------------------------------*/
    private int xyTo1D(int i, int j)      
    {
        return (i-1) * size + j - 1 + 2; // offset 2 for virtual nodes
    }
    
    /*----------------------------------------------------------------
     *  validates to ensure i and j are in the interval [0,size]
     *----------------------------------------------------------------*/
    private void validateIndices(int i, int j)
    {
        if (i < 1 || i > size) 
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j < 1 || j > size) 
            throw new IndexOutOfBoundsException("column index j out of bounds");
    }
    
    /*----------------------------------------------------------------
     *  test client
     *----------------------------------------------------------------*/
    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        int countOpen = 0;
        Percolation percolation = new Percolation(N);
        StdRandom.setSeed(System.currentTimeMillis());
        // run the percolation
        while (!percolation.percolates())
        {
            int x = StdRandom.uniform(1, N+1);
            int y = StdRandom.uniform(1, N+1);
            if (!percolation.isOpen(x, y))
            {
                percolation.open(x, y);
                countOpen++;
            }
        }
        // output information about percolation
        System.out.println("Percolation threshold:\t" + (double) countOpen / (N * N));
    }
}
