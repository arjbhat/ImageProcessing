package model.macros;

import model.Image;
import model.RGBColor;

public class Brightness implements Macro {
  private final int n;

  public Brightness(int n) {
    this.n = n;
  }


  @Override
  public Image execute(Image img) {
    return img.transform((c, y, x)
            -> new RGBColor(Math.min(255, c.getRed() + n),
            Math.min(255, c.getGreen() + n),
            Math.min(255, c.getBlue() + n)));
  }
}
