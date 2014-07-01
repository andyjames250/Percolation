/*----------------------------------------------------------------
 *  Author:        Andy James
 *  Written:       6/24/2014
 *  Last updated:  6/25/2014
 *
 *  Compilation:   javac-algs4 PercolationStats.java
 *  Execution:     java-algs4 PercolationStats N T
 *  
 *  Run experiments using percolation systems.
 *
 *----------------------------------------------------------------*/

public class PercolationStats {
    
    private int size; // Size (grid edge) for experiments
    private int count; // Number of experiments to run
//    private Percolation[] experiments; // Object to hold experiments
//    private Percolation experiment; // Object to hold an experiment
    private double[] open; // To hold percolation thresholds
    
    /*----------------------------------------------------------------
     *  perform T independent computational experiments on an N-by-N grid
     *----------------------------------------------------------------*/
    public PercolationStats(int N, int T)
    {
        if (N <= 0 || T <= 0) 
            throw new 
            IllegalArgumentException("both N and T arguments should be positive");
        size = N;
        count = T;
//        experiments = new Percolation[count];
        open = new double[T];
//        for (int i = 0; i < count; i++)
//        {
//            experiments[i] = new Percolation(size);
//        }
    }
    
    /*----------------------------------------------------------------
     *  sample mean of percolation threshold
     *----------------------------------------------------------------*/
    public double mean()
    {
        return StdStats.mean(open);
//        return StdStats.sum(open) / count;
    }
    
    /*----------------------------------------------------------------
     *  sample standard deviation of percolation threshold
     *----------------------------------------------------------------*/
    public double stddev()
    {
        return StdStats.stddev(open);
//        double mean = mean();
//        double sumsq = 0;
//        for (int i = 0; i < count; i++)
//        {
//            sumsq = sumsq + (open[i] - mean) * (open[i] - mean);
//        }
//        return Math.sqrt(sumsq / (count - 1));
    }
    
    /*----------------------------------------------------------------
     *  returns lower bound of the 95% confidence interval
     *----------------------------------------------------------------*/
    public double confidenceLo()              
    {
        double mean = mean();
        double stddev = stddev();
        return mean - 1.96 * stddev / Math.sqrt(count);
    }
    
    /*----------------------------------------------------------------
     *  returns upper bound of the 95% confidence interval
     *----------------------------------------------------------------*/
    public double confidenceHi()              
    {
        double mean = mean();
        double stddev = stddev();
        return mean + 1.96 * stddev / Math.sqrt(count);
    }
    
    /*----------------------------------------------------------------
     *  helper function to run experiments
     *----------------------------------------------------------------*/
    private void runExperiments()
    {
        for (int i = 0; i < count; i++)
        {
            Percolation experiment = new Percolation(size);
            int openCount = 0;
            StdRandom.setSeed(System.currentTimeMillis());
            while (!experiment.percolates())
            {
                int x = StdRandom.uniform(1, size+1);
                int y = StdRandom.uniform(1, size+1);
                if (!experiment.isOpen(x, y))
                {
                    experiment.open(x, y);
                    openCount++;
                }
            }     
            open[i] = (double) openCount / (size * size);
        }
    }
    
    /*----------------------------------------------------------------
     *  test client
     *----------------------------------------------------------------*/
    public static void main(String[] args)    
    {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(N, T);
        
        // use helper function to run experiments
        percolationStats.runExperiments();
        
        // output statistics to stdout
        System.out.println("mean\t\t\t" + "=\t" + percolationStats.mean());
        System.out.println("stddev\t\t\t" + "=\t" + percolationStats.stddev());
        System.out.println("95% confidence interval\t" 
                           + "=\t" 
                           + percolationStats.confidenceLo() + ", " 
                           + percolationStats.confidenceHi());
    }
}