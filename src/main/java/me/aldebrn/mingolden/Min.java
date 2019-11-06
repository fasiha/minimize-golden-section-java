package me.aldebrn.mingolden;

import java.util.function.Function;

public class Min {
  final private static double PHI_RATIO = 2 / (1 + Math.sqrt(5));

  private Min() { throw new AssertionError(); }

  public static Status min(Function<Double, Double> f, double xL, double xU, int maxIterations) {
    return min(f, xL, xU, 1e-8, maxIterations);
  }
  public static Status min(Function<Double, Double> f, double xL, double xU) { return min(f, xL, xU, 1e-8); }
  public static Status min(Function<Double, Double> f, double xL, double xU, double tol) {
    return min(f, xL, xU, tol, 100);
  }
  public static Status min(Function<Double, Double> f, double xL, double xU, double tol, int maxIterations) {
    double xF;
    double fF;
    int iteration = 0;
    double x1 = xU - PHI_RATIO * (xU - xL);
    double x2 = xL + PHI_RATIO * (xU - xL);
    // Initial bounds:
    double f1 = f.apply(x1);
    double f2 = f.apply(x2);

    // Store these values so that we can return these if they're better.
    // This happens when the minimization falls *approaches* but never
    // actually reaches one of the bounds
    double f10 = f.apply(xL);
    double f20 = f.apply(xU);
    double xL0 = xL;
    double xU0 = xU;

    // Simple, robust golden section minimization:
    while (++iteration < maxIterations && Math.abs(xU - xL) > tol) {
      if (f2 > f1) {
        xU = x2;
        x2 = x1;
        f2 = f1;
        x1 = xU - PHI_RATIO * (xU - xL);
        f1 = f.apply(x1);
      } else {
        xL = x1;
        x1 = x2;
        f1 = f2;
        x2 = xL + PHI_RATIO * (xU - xL);
        f2 = f.apply(x2);
      }
    }

    xF = 0.5 * (xU + xL);
    fF = 0.5 * (f1 + f2);

    Status status = new Status(iteration, xF, fF, true);
    if (Double.isNaN(f2) || Double.isNaN(f1) || iteration == maxIterations) { status.converged = false; }
    if (f10 < fF) {
      status.argmin = xL0;
    } else if (f20 < fF) {
      status.argmin = xU0;
    } else {
      status.argmin = xF;
    }
    return status;
  }
}
