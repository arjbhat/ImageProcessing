package model.macros;

import model.ImageTransform;

/**
 * The macro that transforms an image by flipping the image horizontally. It's the mirror image.
 */
public class HorizontalFlip implements Macro {
  /**
   * Transforms images to their mirror counterpart. (horizontally)
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
        -> img.getColorAt(y, img.getWidth() - x - 1));
  }
}
