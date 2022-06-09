package model.macros;

import model.Image;

/**
 * The macro that transforms an image by flipping the image horizontally. It's the mirror image.
 */
public class HorizontalFlip implements Macro {
  @Override
  public Image execute(Image img) {
    return img.transform((c, y, x)
            -> img.getColorAt(y, img.getWidth() - x - 1));
  }
}
