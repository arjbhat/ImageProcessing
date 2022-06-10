package model.macros;

import java.util.function.Function;

import model.Image;
import model.RGBColor;

/**
 * The macro that transforms an image's brightness by adding or subtracting a value from each of its
 * channels.
 */
public class Brighten implements Macro {
  private final int n;

  /**
   * The macro that will brighten or darken the image based on the increment provided.
   *
   * @param n the increment or decrement to change the brightness of the image
   * @throws IllegalArgumentException if an integer is not inputted
   */
  public Brighten(int n) throws IllegalArgumentException {
    this.n = n;
  }

  /**
   * Brightens or darkens all pixels by the increment. (clamping at max and min value)
   */
  @Override
  public Image execute(Image img) {
    int max = img.getMaxValue();
    Function<Integer, Integer> minMax = num -> Math.max(Math.min(num + n, max), 0);

    return img.transform((c, y, x)
            -> new RGBColor(minMax.apply(c.getRed()),
            minMax.apply(c.getGreen()),
            minMax.apply(c.getBlue())));
  }
}