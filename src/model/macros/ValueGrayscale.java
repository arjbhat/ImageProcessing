package model.macros;

import model.Image;
import model.RGBColor;

/**
 * The macro that transforms an image to a value based grayscale.
 */
public class ValueGrayscale implements Macro {
  @Override
  public Image execute(Image img) {
    return img.transform((c, y, x)
            -> new RGBColor(c.getValue(), c.getValue(), c.getValue()));
  }
}
