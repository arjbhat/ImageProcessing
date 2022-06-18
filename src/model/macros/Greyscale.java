package model.macros;

/**
 * This macro creates a greyscale image by doing a matrix (3x3) transformation on each pixel.
 */
public class Greyscale extends MatrixTransform {
  /**
   * Constructs a grayscale macro - similar to finding the luma component.
   */
  public Greyscale() {
    super(new double[][]{
            {0.393, 0.769, 0.189},
            {0.393, 0.769, 0.189},
            {0.393, 0.769, 0.189}
    });
  }
}