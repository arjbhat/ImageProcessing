package model.macros;

import model.ImageTransform;

/**
 * The function of a Macro is to execute commands on the image.
 */
public interface Macro {
  /**
   * We can execute any operation on an image - producing a new transformed image.
   * (Completely implementation based)
   *
   * @param img the image that we're working on
   * @return a new image that has undergone the transformation
   * @throws IllegalArgumentException if the image passed in is null
   */
  ImageTransform execute(ImageTransform img) throws IllegalArgumentException;
}
