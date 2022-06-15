package model.macros;

import model.Color;
import model.ImageTransform;
import model.RGBColor;

/**
 * The macro that transforms an image based on a matrix.
 */
public class MatrixTransform implements Macro {
  private final double[][] matrix;

  /**
   * Constructs a matrix transformation macro with a 3x3 matrix.
   *
   * @param matrix a 3x3 matrix that we transform each pixel with
   * @throws IllegalArgumentException if the matrix is null or is not a 3x3
   */
  public MatrixTransform(double[][] matrix) throws IllegalArgumentException {
    if (matrix.length != 3) {
      throw new IllegalArgumentException("Matrix height must be a 3.");
    }
    for (double[] row : matrix) {
      if (row == null) {
        throw new IllegalArgumentException("Matrix row cannot be null.");
      }
      if (row.length != 3) {
        throw new IllegalArgumentException("Matrix width must be 3");
      }
    }
    this.matrix = matrix;
  }

  /**
   * Transforms all the pixels from their components based on the given matrix.
   *
   * @param img the image that we're working on
   * @return a new image that has undergone the transformation
   * @throws IllegalArgumentException if the image passed in is null
   */
  @Override
  public ImageTransform execute(ImageTransform img) throws IllegalArgumentException {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null.");
    }
    return img.transform((c, y, x)
            -> new RGBColor(calcChannel(matrix[0], c), // r'
            calcChannel(matrix[1], c), // g'
            calcChannel(matrix[2], c))); // b'
  }

  // calculates the value for a specific channel
  private int calcChannel(double[] row, Color c) {
    return (int) (row[0] * c.getRed() + row[1] * c.getGreen() + row[2] * c.getBlue());
  }
}
