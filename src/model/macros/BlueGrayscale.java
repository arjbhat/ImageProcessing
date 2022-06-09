package model.macros;

import model.Image;
import model.RGBColor;

/**
 * The macro that transforms an image to its blue grayscale. (representing the blue channel)
 */
public class BlueGrayscale implements Macro {

  @Override
  public Image execute(Image img) {
    return img.transform((c, y, x)
            -> new RGBColor(c.getBlue(), c.getBlue(), c.getBlue()));
  }
}
