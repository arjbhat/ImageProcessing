package model;

/**
 * The immutable image that we are working on. Any changes to the image will only produce a new
 * image. An image is a 2D array of colors.
 */
public class Image implements ImageTransform {
  private final int height;
  private final int width;
  private final int maxValue;
  private final RGBColor[][] pane;

  /**
   * We can construct a new image by passing in a 2D array of RGBColors.
   *
   * @param img the color array that we want to construct an image with
   */
  public Image(RGBColor[][] img, int maxValue) {
    this.maxValue = maxValue;
    this.height = img.length;

    if (height == 0) {
      this.width = 0;
    } else {
      this.width = img[0].length;
    }

    this.pane = new RGBColor[height][width];

    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        RGBColor c = img[row][col];
        if (c.getRed() > maxValue || c.getGreen() > maxValue || c.getBlue() > maxValue) {
          throw new IllegalArgumentException("Color value larger than channel size");
        }
        pane[row][col] = c;
      }
    }
  }

  // So we don't have to make a copy unnecessarily - every time we transform the image.
  private Image(RGBColor[][] pane, int height, int width, int maxValue) {
    this.pane = pane;
    this.height = height;
    this.width = width;
    this.maxValue = maxValue;
  }

  /**
   * Returns the maximum value of this image.
   */
  @Override
  public int getMaxValue() {
    return maxValue;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public RGBColor getColorAt(int row, int col) throws IllegalArgumentException {
    if (row < 0 || col < 0 || row >= height || col >= width) {
      throw new IllegalArgumentException("Invalid location");
    }
    return pane[row][col];
  }

  @Override
  public Image transform(ColorFunction map) {
    RGBColor[][] newPane = new RGBColor[height][width];
    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        RGBColor c = map.apply(this.pane[row][col], row, col);
        if (c.getRed() > maxValue || c.getGreen() > maxValue || c.getBlue() > maxValue) {
          throw new IllegalArgumentException("Color value larger than channel size");
        }
        newPane[row][col] = c;
      }
    }
    return new Image(newPane, height, width, maxValue);
  }
}