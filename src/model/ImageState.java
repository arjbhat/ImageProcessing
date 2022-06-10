package model;

/**
 * The observable image. Does not provide any features that allow it to be mutated.
 */
public interface ImageState {

  /**
   * This defines the scale of values possible for the color intensities.
   * This can be any value. (Usually between 0 and 255)
   */
  int getMaxValue();

  /**
   * Returns the height of the image.
   *
   * @return the height
   */
  int getHeight();

  /**
   * Returns the width of the image.
   *
   * @return the width
   */
  int getWidth();

  /**
   * Returns the color at a specific position in the image.
   *
   * @return a color at a specific position
   */
  Color getColorAt(int row, int col) throws IllegalArgumentException;

  boolean equals(Object other);

  int hashCode();
}
