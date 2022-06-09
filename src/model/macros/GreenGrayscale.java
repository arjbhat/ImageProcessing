package model.macros;

import model.Image;
import model.RGBColor;

/**
 * The macro that transforms an image to its green grayscale. (representing the green channel)
 */
public class GreenGrayscale implements Macro {
  @Override
  public Image execute(Image img) {
    return img.transform((c, y, x)
            -> new RGBColor(c.getGreen(), c.getGreen(), c.getGreen()));
  }
}
