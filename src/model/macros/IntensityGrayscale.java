package model.macros;

import model.Image;
import model.RGBColor;

public class IntensityGrayscale implements Macro {
  @Override
  public Image execute(Image img) {
    return img.transform((c, y, x)
            -> new RGBColor(c.getIntensity(), c.getIntensity(), c.getIntensity()));
  }
}
