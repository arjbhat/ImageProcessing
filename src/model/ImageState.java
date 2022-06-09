package model;

/**
 * The observable image. Does not provide any features that allow it to be mutated.
 */
public interface ImageState {

  /**
   * Returns the maximum value of all color channels in this image.
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
   * @return an RGB color at a specific position
   */
  RGBColor getColorAt(int row, int col) throws IllegalArgumentException;
}
