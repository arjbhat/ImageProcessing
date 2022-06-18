package model.macros;

import java.util.function.Function;

import model.Color;
import model.ImageTransform;
import model.RGBColor;

/**
 * The macro that transforms an image to its grayscale. (representing a channel or factor)
 */
public class Component implements Macro {
  private final Function<Color, Integer> getChannel;

  /**
   * Constructs the Grayscale Macro.
   *
   * @param getChannel the function that returns a channel (int factor of the image)
   * @throws IllegalArgumentException if inputted function is null
   */
  public Component(Function<Color, Integer> getChannel) throws IllegalArgumentException {
    if (getChannel == null) {
      throw new IllegalArgumentException("Get channel function cannot be null.");
    }
    this.getChannel = getChannel;
  }

  /**
   * Transforms all the pixels to their grayscale version.
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
        -> new RGBColor(getChannel.apply(c), getChannel.apply(c), getChannel.apply(c),
        c.getAlpha()));
  }
}
