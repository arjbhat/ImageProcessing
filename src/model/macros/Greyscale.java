package model.macros;

public class Greyscale extends MatrixTransform {
  /**
   * Constructs a grayscale macro.
   */
  public Greyscale() {
    super(new double[][]{
            {0.393, 0.769, 0.189},
            {0.393, 0.769, 0.189},
            {0.393, 0.769, 0.189}
    });
  }
}
