package model.macros;

/**
 * This macro blurs the image. It extends the convolve macro, which uses an odd (pos) integer to
 * change an image based on the surrounding pixels.
 */
public class Blur extends Convolve {
  /**
   * Constructs a blur macro using an odd (pos) integer matrix.
   */
  public Blur() {
    super(new double[][]{
        {1 / 16., 1 / 8., 1 / 16.},
        {1 / 8., 1 / 4., 1 / 8.},
        {1 / 16., 1 / 8., 1 / 16.}
    });
  }
}
