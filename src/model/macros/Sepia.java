package model.macros;

public class Sepia extends MatrixTransform {
  /**
   * Constructs a Sepia macro.
   */
  public Sepia() {
    super(new double[][]{
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131}
    });
  }
}
