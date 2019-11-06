package me.aldebrn.mingolden;
public class Status {
  public int iterations;
  public double argmin;
  public double minimum;
  public boolean converged;
  public Status(int iterations, double argmin, double minimum, boolean converged) {
    this.iterations = iterations;
    this.argmin = argmin;
    this.minimum = minimum;
    this.converged = converged;
  }
}
