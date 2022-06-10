package model.macros;

import model.ImageTransform;

/**
 * The macro that transforms an image by flipping the image horizontally. It's the mirror image.
 */
public class HorizontalFlip implements Macro {

  /**
   * Transforms images to their mirror counterpart. (horizontally)
   */
  @Override
  public ImageTransform execute(ImageTransform img) {
    return img.transform((c, y, x)
        -> img.getColorAt(y, img.getWidth() - x - 1));
  }
}
