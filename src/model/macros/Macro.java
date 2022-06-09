package model.macros;

import model.Image;

/**
 * The function of a Macro is to execute commands on the image.
 */
public interface Macro {
  /**
   * We can execute any operation on an image - producing a new transformed image.
   *
   * @param img the image that we're working on
   * @return a new image that has undergone the transformation
   */
  Image execute(Image img);
}
