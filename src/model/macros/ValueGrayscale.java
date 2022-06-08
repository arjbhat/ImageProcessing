package model.macros;

import model.Image;
import model.RGBColor;

public class ValueGrayscale implements Macro {
  @Override
  public Image execute(Image img) {
    return img.transform((c, y, x)
            -> new RGBColor(c.getValue(), c.getValue(), c.getValue()));
  }
}
