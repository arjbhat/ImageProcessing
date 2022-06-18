package model.macros;

/**
 * This macro sharpens the image. It extends the convolve macro, which uses an odd (pos) integer to
 * change an image based on the surrounding pixels.
 */
public class Sharpen extends Convolve {
  /**
   * Constructs a sharpen macro using an odd (pos) integer matrix.
   */
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