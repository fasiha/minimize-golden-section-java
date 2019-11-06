package me.aldebrn.mingolden;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MinTest {
  private double eps = Math.ulp(1.0f);  // minimize-golden-section-1d tests use 32-bit float's ulp

  @Test
  public void basic() {
    Status status = Min.min(x -> x * x, -1.0, 1.0, 1e-3, 100);
    double res = status.argmin;
    assertEquals(0, res, 1e-3);
    assertTrue(status.converged);
  }

  @Test
  public void hyperbola() {
    // Minimizes 1 / (x - 1) in [0, 2]
    Status status = Min.min(x -> 1. / (x - 1.), 0, 2);
    assertEquals(1.0, status.argmin, eps);
    assertTrue(status.converged);
  }

  @Test
  public void negHyperbola() {
    // Minimizes -1 / (x - 1) in [0, 2]
    Status status = Min.min(x -> - 1.0 / (x - 1), 0, 2);
    assertEquals(1.0, status.argmin, eps);
    assertTrue(status.converged);
    assertEquals(41, status.iterations);
  }

  @Test
  public void parabola() {
    // Succeeds out on bounded minimization of -x^2
    Status status = Min.min(x -> - x * x, -1, 2);
    assertEquals(2, status.argmin, eps);
    assertEquals(-4, status.minimum, eps);
    assertTrue(status.converged);
  }

  @Test
  public void sqrt() {
    // Minimizes sqrt(x) in [0, inf)
    Status status = Min.min(x -> Math.sqrt(x), 0, 300);
    assertTrue(status.converged);
    assertEquals(0.0, status.argmin, eps);
  }

  @Test
  public void sqrtAbs() {
    // Minimizes sqrt(|x|)
    Status status = Min.min(x -> Math.sqrt(Math.abs(x)), -3, 3);
    assertTrue(status.converged);
    assertEquals(0.0, status.argmin, eps);
    assertEquals(0.0, status.minimum, 1e-3);
  }

  @Test
  public void tol() {
    // returns answer if tolerance not met
    Status status = Min.min(x -> x * (x - 2.0), 0, 3, 0, 200);
    assertEquals(200, status.iterations);
    assertEquals(-1.0, status.minimum, eps);
    assertEquals(1.0, status.argmin, eps);
    assertFalse(status.converged);
  }

  @Test
  public void parabolaEdge() {
    // minimizes x(x-2) in [5, 6]
    Status status = Min.min(x -> x * (x - 2.0), 5, 6);
    assertTrue(status.converged);
    assertEquals(5.0, status.argmin, eps);
  }

  @Test
  public void cubic() {
    // minimizes a cubic
    Status status = Min.min(x -> x * (x - 2) * (x - 1), -3, 3);
    assertTrue(status.converged);
    assertEquals(-3, status.argmin, eps);
  }

  @Test
  public void negCubic() {
    // maximizes a cubic
    Status status = Min.min(x -> - x * (x - 2) * (x - 1), 0, 3);
    assertTrue(status.converged);
    assertEquals(3, status.argmin, eps);
  }

  @Test
  public void boundedCubic() {
    // minimizes a cubic against bounds
    Status status = Min.min(x -> x * (x - 2) * (x - 1), 5, 6);
    assertTrue(status.converged);
    assertEquals(5, status.argmin, eps);
  }

  @Test
  public void cos() {
    // minimizes cosine
    Status status = Min.min(Math::cos, -10, 10);
    assertTrue(status.converged);
    assertEquals(-1, Math.cos(status.argmin), eps);
  }

  @Test
  public void cusp() {
    // minimizes a cusp
    Status status = Min.min(x -> Math.sqrt(Math.abs(x - 5)), 0, 10);
    assertTrue(status.converged);
    assertEquals(5, status.argmin, eps);
  }
}
