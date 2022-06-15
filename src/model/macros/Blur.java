package model.macros;

public class Blur extends Convolve {
  public Blur() {
    super(new double[][]{
            {1 / 16., 1 / 8., 1 / 16.},
            {1 / 8., 1 / 4., 1 / 8.},
            {1 / 16., 1 / 8., 1 / 16.}
    });
  }
}
