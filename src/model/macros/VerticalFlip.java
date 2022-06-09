package model.macros;

import model.Image;

/**
 * The macro that transforms an image by flipping the image vertically.
 */
public class VerticalFlip implements Macro {
  @Override
  public Image execute(Image img) {
    return img.transform((c, y, x)
            -> img.getColorAt(img.getHeight() - y - 1, x));
  }
}
