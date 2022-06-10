package model.macros;

import java.util.function.Function;

import model.Color;
import model.ImageTransform;
import model.RGBColor;

/**
 * The macro that transforms an image to its grayscale. (representing a channel or factor)
 */
public class Grayscale implements Macro {
  private final Function<Color, Integer> getChannel;

  /**
   * Constructs the Grayscale Macro.
   *
   * @param getChannel the function that returns a channel (int factor of the image)
   * @throws IllegalArgumentException if inputted function is null
   */
  public Grayscale(Function<Color, Integer> getChannel) throws IllegalArgumentException {
    if (getChannel == null) {
      throw new IllegalArgumentException("Get channel function cannot be null.");
    }
    this.getChannel = getChannel;
  }

  /**
   * Transforms all the pixels to their grayscale version.
   */
  @Override
  public ImageTransform execute(ImageTransform img) {
    return img.transform((c, y, x)
        -> new RGBColor(getChannel.apply(c), getChannel.apply(c), getChannel.apply(c)));
  }
}
