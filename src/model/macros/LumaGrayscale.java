package model.macros;

import model.Image;
import model.RGBColor;

/**
 * The macro that transforms an image to a luma based grayscale.
 */
public class LumaGrayscale implements Macro {
  @Override
  public Image execute(Image img) {
    return img.transform((c, y, x)
            -> new RGBColor(c.getLuma(), c.getLuma(), c.getLuma()));
  }
}
