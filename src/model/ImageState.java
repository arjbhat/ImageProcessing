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

  /**
   * Checks if two images are same. It first checks if the two objects have the same reference and
   * returns true if they do. If they don't then it checks if it's an instance of the ImageState
   * and if they have the same width, height, max-value, and all the colors in the image are
   * in the same position as this one.
   *
   * @param other the object that we are comparing this image with
   * @return true if they represent the same image and false otherwise
   */
  boolean equals(Object other);

  /**
   * Returns an integer hash using Objects.hash on the height, width, max-value, and
   * Arrays.deepHashCode on the array storing the colors.
   *
   * @return an integer hash of this object
   */
  int hashCode();
}
