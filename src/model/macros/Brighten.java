package model.macros;

import model.Image;
import model.RGBColor;

/**
 * The macro that transforms an image's brightness by adding or subtracting a value from each of its
 * channels.
 */
public class Brighten implements Macro {
  private final int n;

  public Brighten(int n) throws IllegalArgumentException {
    this.n = n;
  }

  @Override
  public Image execute(Image img) {
    int max = img.getMaxValue();
    return img.transform((c, y, x)
            -> new RGBColor(Math.min(max, Math.max(c.getRed() + n, 0)),
            Math.min(max, Math.max(c.getGreen() + n, 0)),
            Math.min(max, Math.max(c.getBlue() + n, 0))));
  }
}