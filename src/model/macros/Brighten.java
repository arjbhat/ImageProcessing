package model.macros;

import model.ImageTransform;
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
   * Brightens or darkens all pixels by the increment.
   *
   * @param img the image that we're working on
   * @return a new image that has undergone the transformation
   * @throws IllegalArgumentException if the image passed in is null
   */
  @Override
  public ImageTransform execute(ImageTransform img) throws IllegalArgumentException {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null.");
    }
    return img.transform((c, y, x)
            -> new RGBColor(c.getRed() + n, c.getGreen() + n, c.getBlue() + n));
  }
}
