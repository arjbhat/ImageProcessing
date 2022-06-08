package model.macros;

import model.Image;
import model.RGBColor;

public class RedGrayscale implements Macro {
  @Override
  public Image execute(Image img) {
    return img.transform((c, y, x)
            -> new RGBColor(c.getRed(), c.getRed(), c.getRed()));
  }
}
