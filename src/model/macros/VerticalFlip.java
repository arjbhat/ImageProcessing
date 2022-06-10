package model.macros;

import model.ImageTransform;

/**
 * The macro that transforms an image by flipping the image vertically.
 */
public class VerticalFlip implements Macro {

  /**
   * Transforms images to their mirror counterpart. (vertically)
   */
  @Override
  public ImageTransform execute(ImageTransform img) {
    return img.transform((c, y, x)
        -> img.getColorAt(img.getHeight() - y - 1, x));
  }
}
