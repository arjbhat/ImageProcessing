package model.macros;

import java.util.function.Function;

import model.Color;
import model.Image;
import model.ImageTransform;
import model.RGBColor;

/**
 * This macro downscales an image.
 */
public class Downscale implements Macro {
  // INVARIANT: Positive integer
  private final int height;
  // INVARIANT: Positive integer
  private final int width;

  /**
   * Constructs the downscale macro.
   *
   * @param height of the dest image
   * @param width  of the dest image
   * @throws IllegalArgumentException if image is null or height and width of dest image are greater
   *                                  than the originals
   */
  public Downscale(int height, int width) throws IllegalArgumentException {
    this.height = height;
    this.width = width;
  }

  @Override
  public ImageTransform execute(ImageTransform img) throws IllegalArgumentException {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null.");
    }
    if (height <= 0 || img.getHeight() < height || width <= 0 || img.getWidth() < width) {
      throw new IllegalArgumentException("Dest-image's height and width must both be " +
          "greater than 0 and less than or equal to the original's.");
    }
    ImageTransform newImage = new Image(this.emptyImage(this.height, this.width),
        img.getMaxValue());

    return newImage.transform((c, y, x) -> {
      double row = (y * img.getHeight()) / (double) (this.height);
      double col = (x * img.getWidth()) / (double) (this.width);
      int r = this.channelCalculation(row, col, img, Color::getRed);
      int g = this.channelCalculation(row, col, img, Color::getGreen);
      int b = this.channelCalculation(row, col, img, Color::getBlue);
      return new RGBColor(r, g, b);
    });
  }

  private Color[][] emptyImage(int height, int width) {
    Color[][] colorArr = new Color[height][width];
    Color black = new RGBColor(0, 0, 0);

    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        colorArr[row][col] = black;
      }
    }

    return colorArr;
  }

  private int floor(double a) {
    return (int) a;
  }

  private int ceiling(double a, int max) {
    return Integer.min(this.ceiling(a), max);
  }

  private int ceiling(double a) {
    return (int) a + 1;
  }

  private int channelCalculation(double y, double x, ImageTransform img,
                                 Function<Color, Integer> getChannel) {
    int cA = getChannel.apply(img.getColorAt(this.floor(y),
        this.floor(x)));
    int cB = getChannel.apply(img.getColorAt(this.floor(y),
        this.ceiling(x, img.getWidth() - 1)));
    int cC = getChannel.apply(img.getColorAt(this.ceiling(y, img.getHeight() - 1),
        this.floor(x)));
    int cD = getChannel.apply(img.getColorAt(this.ceiling(y, img.getHeight() - 1),
        this.ceiling(x, img.getWidth() - 1)));

    double m = (cB * (x - this.floor(x)) + cA * (this.ceiling(x) - x));
    double n = (cD * (x - this.floor(x)) + cC * (this.ceiling(x) - x));

    return (int) (n * (y - this.floor(y)) + m * (this.ceiling(y) - y));
  }
}
