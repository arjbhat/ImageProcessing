package model.macros;

public class Sharpen extends Convolve {
  public Sharpen() {
    super(new double[][]{
            {-1 / 8., -1 / 8., -1 / 8., -1 / 8., -1 / 8.},
            {-1 / 8., 1 / 4., 1 / 4., 1 / 4., -1 / 8.},
            {-1 / 8., 1 / 4., 1, 1 / 4., -1 / 8.},
            {-1 / 8., 1 / 4., 1 / 4., 1 / 4., -1 / 8.},
            {-1 / 8., -1 / 8., -1 / 8., -1 / 8., -1 / 8.},
    });
  }
}
