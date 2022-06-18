package model.macros;

/**
 * This macro creates a sepia image by doing a matrix (3x3) transformation on each pixel.
 */
public class Sepia extends MatrixTransform {

  /**
   * Constructs a sepia macro.
   */
  public Sepia() {
    super(new double[][]{
        {0.393, 0.769, 0.189},
        {0.349, 0.686, 0.168},
        {0.272, 0.534, 0.131}
    });
  }
}
